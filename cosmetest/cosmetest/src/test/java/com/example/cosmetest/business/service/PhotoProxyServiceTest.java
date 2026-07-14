package com.example.cosmetest.business.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhotoProxyServiceTest {

    private MockWebServer server;

    @AfterEach
    void tearDown() throws IOException {
        if (server != null) {
            server.shutdown();
        }
    }

    @Test
    void fetchesPhotoUnderConfiguredBaseAndEncodesFilename() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "image/jpeg")
                .setBody("photo"));
        server.start();
        PhotoProxyService service = service(server.url("/photos/").toString(), 1024);

        PhotoProxyService.PhotoPayload payload = service.fetch("f_Élodie test.JPG");

        assertThat(payload.bytes()).isEqualTo("photo".getBytes(StandardCharsets.UTF_8));
        assertThat(payload.contentType().toString()).isEqualTo("image/jpeg");
        RecordedRequest request = server.takeRequest();
        assertThat(request.getPath()).isEqualTo("/photos/f_%C3%89lodie%20test.JPG");
    }

    @Test
    void acceptsPdfDocumentsStoredWithVolunteerPhotos() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200));
        server.start();
        PhotoProxyService service = service(server.url("/photos/").toString(), 1024);

        assertThat(service.exists("f_dupont010190.pdf")).isTrue();
        assertThat(server.takeRequest().getPath()).isEqualTo("/photos/f_dupont010190.pdf");
    }

    @Test
    void rejectsTraversalAndAbsoluteUrlsBeforeAnyNetworkCall() throws Exception {
        server = new MockWebServer();
        server.start();
        PhotoProxyService service = service(server.url("/photos/").toString(), 1024);

        assertThatThrownBy(() -> service.fetch("../secret.JPG"))
                .isInstanceOf(PhotoProxyService.InvalidPhotoPathException.class);
        assertThatThrownBy(() -> service.fetch("http://169.254.169.254/latest/meta-data"))
                .isInstanceOf(PhotoProxyService.InvalidPhotoPathException.class);
        assertThatThrownBy(() -> service.fetch("payload.html"))
                .isInstanceOf(PhotoProxyService.InvalidPhotoPathException.class);
        assertThat(server.getRequestCount()).isZero();
    }

    @Test
    void neverFollowsUpstreamRedirects() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(302)
                .setHeader("Location", "http://169.254.169.254/latest/meta-data"));
        server.start();
        PhotoProxyService service = service(server.url("/photos/").toString(), 1024);

        assertThatThrownBy(() -> service.fetch("face.JPG"))
                .isInstanceOf(PhotoProxyService.PhotoUpstreamException.class)
                .hasMessageContaining("302");
        assertThat(server.getRequestCount()).isEqualTo(1);
    }

    @Test
    void rejectsResponseWhoseDeclaredSizeExceedsLimit() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200).setBody("12345"));
        server.start();

        assertThatThrownBy(() -> service(server.url("/photos/").toString(), 4).fetch("face.JPG"))
                .isInstanceOf(PhotoProxyService.PhotoTooLargeException.class);
    }

    @Test
    void rejectsChunkedResponseWhenReadBytesExceedLimit() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200).setChunkedBody("12345", 1));
        server.start();

        assertThatThrownBy(() -> service(server.url("/photos/").toString(), 4).fetch("face.JPG"))
                .isInstanceOf(PhotoProxyService.PhotoTooLargeException.class);
    }

    @Test
    void mapsReadTimeoutToDedicatedException() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200)
                .setBodyDelay(2, TimeUnit.SECONDS)
                .setBody("photo"));
        server.start();
        PhotoProxyService service = new PhotoProxyService(server.url("/photos/").toString(), true, 1_000, 100, 1024);

        assertThatThrownBy(() -> service.fetch("face.JPG"))
                .isInstanceOf(PhotoProxyService.PhotoTimeoutException.class);
    }

    @Test
    void checksExistenceWithHeadAndMapsOnly404ToFalse() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setResponseCode(200));
        server.enqueue(new MockResponse().setResponseCode(404));
        server.start();
        PhotoProxyService service = service(server.url("/photos/").toString(), 1024);

        assertThat(service.exists("face.JPG")).isTrue();
        assertThat(service.exists("missing.JPG")).isFalse();
        assertThat(server.takeRequest().getMethod()).isEqualTo("HEAD");
        assertThat(server.takeRequest().getMethod()).isEqualTo("HEAD");
    }

    @Test
    void rejectsUnsafeBaseConfiguration() {
        assertThatThrownBy(() -> service("file:///tmp/photos/", 1024))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service("https://example.org/photos/?target=http://evil", 1024))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service("https://example.org/photos", 1024))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private PhotoProxyService service(String baseUrl, long maxBytes) {
        return new PhotoProxyService(baseUrl, true, 1_000, 1_000, maxBytes);
    }
}
