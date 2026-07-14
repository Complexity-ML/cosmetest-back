package com.example.cosmetest.business.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class PhotoProxyService {

    private final URI baseUri;
    private final boolean checkEnabled;
    private final int connectionTimeoutMillis;
    private final int readTimeoutMillis;
    private final long maxResponseBytes;

    public PhotoProxyService(
            @Value("${photo.server.url}") String baseUrl,
            @Value("${photo.check.enabled:true}") boolean checkEnabled,
            @Value("${photo.connection.timeout:5000}") int connectionTimeoutMillis,
            @Value("${photo.read.timeout:5000}") int readTimeoutMillis,
            @Value("${photo.max-response-size:10485760}") long maxResponseBytes) {
        this.baseUri = validateBaseUri(baseUrl);
        this.checkEnabled = checkEnabled;
        if (connectionTimeoutMillis <= 0 || readTimeoutMillis <= 0 || maxResponseBytes <= 0) {
            throw new IllegalArgumentException("La configuration du proxy photo doit être strictement positive");
        }
        this.connectionTimeoutMillis = connectionTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.maxResponseBytes = maxResponseBytes;
    }

    public boolean exists(String fileName) {
        if (!checkEnabled) {
            validateFileName(fileName);
            return true;
        }
        HttpURLConnection connection = null;
        try {
            connection = open(fileName, "HEAD");
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                return true;
            }
            if (status == HttpURLConnection.HTTP_NOT_FOUND) {
                return false;
            }
            throw new PhotoUpstreamException("Le serveur photo a répondu avec le statut " + status);
        } catch (SocketTimeoutException e) {
            throw new PhotoTimeoutException("Délai d'attente dépassé pour le serveur photo", e);
        } catch (IOException e) {
            throw new PhotoUpstreamException("Serveur photo inaccessible", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public PhotoPayload fetch(String fileName) {
        HttpURLConnection connection = null;
        try {
            connection = open(fileName, "GET");
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_NOT_FOUND) {
                throw new PhotoNotFoundException("Photo introuvable");
            }
            if (status < 200 || status >= 300) {
                throw new PhotoUpstreamException("Le serveur photo a répondu avec le statut " + status);
            }
            long declaredLength = connection.getContentLengthLong();
            if (declaredLength > maxResponseBytes) {
                throw new PhotoTooLargeException("La photo dépasse la taille maximale autorisée");
            }
            byte[] bytes;
            try (InputStream input = connection.getInputStream()) {
                bytes = readLimited(input);
            }
            return new PhotoPayload(bytes, safeContentType(connection.getContentType()));
        } catch (PhotoProxyException e) {
            throw e;
        } catch (SocketTimeoutException e) {
            throw new PhotoTimeoutException("Délai d'attente dépassé pour le serveur photo", e);
        } catch (IOException e) {
            throw new PhotoUpstreamException("Serveur photo inaccessible", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String publicUrl(String fileName) {
        return resolve(fileName).toASCIIString();
    }

    private HttpURLConnection open(String fileName, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) resolve(fileName).toURL().openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(connectionTimeoutMillis);
        connection.setReadTimeout(readTimeoutMillis);
        connection.setInstanceFollowRedirects(false);
        connection.setUseCaches(false);
        return connection;
    }

    private URI resolve(String fileName) {
        validateFileName(fileName);
        String encoded = UriUtils.encodePathSegment(fileName, StandardCharsets.UTF_8);
        URI resolved = baseUri.resolve(encoded).normalize();
        if (!sameOrigin(baseUri, resolved) || !resolved.getPath().startsWith(baseUri.getPath())) {
            throw new InvalidPhotoPathException("Chemin de photo non autorisé");
        }
        return resolved;
    }

    private byte[] readLimited(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        long total = 0;
        int read;
        while ((read = input.read(buffer)) != -1) {
            total += read;
            if (total > maxResponseBytes) {
                throw new PhotoTooLargeException("La photo dépasse la taille maximale autorisée");
            }
            output.write(buffer, 0, read);
        }
        return output.toByteArray();
    }

    private static URI validateBaseUri(String baseUrl) {
        try {
            URI uri = URI.create(baseUrl).normalize();
            String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
            if (!(scheme.equals("http") || scheme.equals("https")) || uri.getHost() == null
                    || uri.getUserInfo() != null || uri.getQuery() != null || uri.getFragment() != null) {
                throw new IllegalArgumentException("photo.server.url doit être une URL HTTP(S) absolue sans paramètres");
            }
            String path = uri.getPath();
            if (path == null || !path.endsWith("/")) {
                throw new IllegalArgumentException("photo.server.url doit se terminer par '/'");
            }
            return uri;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Configuration photo.server.url invalide", e);
        }
    }

    private static void validateFileName(String fileName) {
        if (fileName == null || fileName.isBlank() || fileName.equals(".") || fileName.equals("..")
                || fileName.indexOf('/') >= 0 || fileName.indexOf('\\') >= 0 || fileName.indexOf('%') >= 0
                || fileName.chars().anyMatch(Character::isISOControl)) {
            throw new InvalidPhotoPathException("Nom de fichier photo non autorisé");
        }
        String lowerCaseName = fileName.toLowerCase(Locale.ROOT);
        boolean supportedPhotoDocument = lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg")
                || lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".gif")
                || lowerCaseName.endsWith(".webp") || lowerCaseName.endsWith(".pdf");
        if (!supportedPhotoDocument) {
            throw new InvalidPhotoPathException("Extension de photo ou document non autorisée");
        }
    }

    private static boolean sameOrigin(URI first, URI second) {
        return first.getScheme().equalsIgnoreCase(second.getScheme())
                && first.getHost().equalsIgnoreCase(second.getHost())
                && effectivePort(first) == effectivePort(second);
    }

    private static int effectivePort(URI uri) {
        if (uri.getPort() >= 0) {
            return uri.getPort();
        }
        return "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
    }

    private static MediaType safeContentType(String value) {
        try {
            return value == null ? MediaType.APPLICATION_OCTET_STREAM : MediaType.parseMediaType(value);
        } catch (IllegalArgumentException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public record PhotoPayload(byte[] bytes, MediaType contentType) { }

    public static class PhotoProxyException extends RuntimeException {
        public PhotoProxyException(String message) { super(message); }
        public PhotoProxyException(String message, Throwable cause) { super(message, cause); }
    }

    public static final class InvalidPhotoPathException extends PhotoProxyException {
        public InvalidPhotoPathException(String message) { super(message); }
    }

    public static final class PhotoNotFoundException extends PhotoProxyException {
        public PhotoNotFoundException(String message) { super(message); }
    }

    public static final class PhotoTimeoutException extends PhotoProxyException {
        public PhotoTimeoutException(String message, Throwable cause) { super(message, cause); }
    }

    public static final class PhotoTooLargeException extends PhotoProxyException {
        public PhotoTooLargeException(String message) { super(message); }
    }

    public static final class PhotoUpstreamException extends PhotoProxyException {
        public PhotoUpstreamException(String message) { super(message); }
        public PhotoUpstreamException(String message, Throwable cause) { super(message, cause); }
    }
}
