package com.example.cosmetest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

/**
 * Gestionnaire global d'exceptions pour l'application
 * Centralise la gestion des erreurs pour toutes les API
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gère les exceptions d'entité non trouvée
     * @param ex Exception levée
     * @return Réponse d'erreur 404
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Ressource non trouvée",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Ressource non trouvée",
                ex.getResourcePath());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        if (ex.getStatusCode().is5xxServerError()) {
            return handleGlobalException(ex);
        }
        ErrorResponse error = new ErrorResponse(
                ex.getStatusCode().value(),
                "Requête refusée",
                ex.getReason() != null ? ex.getReason() : "La requête ne peut pas être traitée");
        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    /**
     * Gère les exceptions d'argument invalide
     * @param ex Exception levée
     * @return Map des erreurs de validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String key = error instanceof FieldError fieldError ? fieldError.getField() : "_global";
            errors.put(key, error.getDefaultMessage());
        });
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST.value(),
                "Données invalides",
                "Un ou plusieurs champs sont invalides",
                errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Requête invalide",
                "Paramètres ou contenu de requête invalides");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                "UNAUTHORIZED",
                HttpStatus.UNAUTHORIZED.value(),
                "Identifiants incorrects",
                "Authentification requise",
                Collections.emptyMap(),
                request.getRequestURI()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(
                "FORBIDDEN",
                HttpStatus.FORBIDDEN.value(),
                "Accès refusé",
                "Droits insuffisants",
                Collections.emptyMap(),
                request.getRequestURI()), HttpStatus.FORBIDDEN);
    }

    /**
     * Gère les exceptions d'argument invalide
     * @param ex Exception levée
     * @return Réponse d'erreur 400
     */
    @ExceptionHandler(AmbiguousEtudeVolontaireException.class)
    public ResponseEntity<ErrorResponse> handleAmbiguousEtudeVolontaire(AmbiguousEtudeVolontaireException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Association étude-volontaire ambiguë",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AmbiguousVolontaireHcException.class)
    public ResponseEntity<ErrorResponse> handleAmbiguousVolontaireHc(AmbiguousVolontaireHcException ex) {
        ErrorResponse error = new ErrorResponse(
                "AMBIGUOUS_VOLUNTEER_HABITS",
                HttpStatus.CONFLICT.value(),
                "Habitudes cosmétiques ambiguës",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AmbiguousVolontaireException.class)
    public ResponseEntity<ErrorResponse> handleAmbiguousVolontaire(AmbiguousVolontaireException ex) {
        ErrorResponse error = new ErrorResponse(
                "AMBIGUOUS_VOLUNTEER",
                HttpStatus.CONFLICT.value(),
                "Volontaire ambigu",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AmbiguousRdvTraceException.class)
    public ResponseEntity<ErrorResponse> handleAmbiguousRdvTrace(AmbiguousRdvTraceException ex) {
        ErrorResponse error = new ErrorResponse(
                "AMBIGUOUS_APPOINTMENT_TRACE",
                HttpStatus.CONFLICT.value(),
                "Rendez-vous ambigu pour l'annulation",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AmbiguousRepairGroupException.class)
    public ResponseEntity<ErrorResponse> handleAmbiguousRepairGroup(AmbiguousRepairGroupException ex) {
        ErrorResponse error = new ErrorResponse(
                "AMBIGUOUS_REPAIR_GROUP",
                HttpStatus.CONFLICT.value(),
                "Groupe ambigu pour la réparation",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Argument invalide",
                ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les autres exceptions non traitées
     * @param ex Exception levée
     * @return Réponse d'erreur 500
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        logger.error("Erreur interne non gérée", ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Une erreur interne s'est produite",
                "Erreur enregistrée côté serveur");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Classe interne pour les réponses d'erreur
     */
    public static class ErrorResponse {
        private final String code;
        private int status;
        private String message;
        private String details;
        private final Map<String, String> fieldErrors;
        private final String correlationId;
        private final Instant timestamp;
        private final String path;

        public ErrorResponse(int status, String message, String details) {
            this(defaultCode(status), status, message, details, Collections.emptyMap());
        }

        public ErrorResponse(String code, int status, String message, String details) {
            this(code, status, message, details, Collections.emptyMap());
        }

        public ErrorResponse(
                String code,
                int status,
                String message,
                String details,
                Map<String, String> fieldErrors) {
            this(code, status, message, details, fieldErrors, null);
        }

        public ErrorResponse(
                String code,
                int status,
                String message,
                String details,
                Map<String, String> fieldErrors,
                String path) {
            this.code = code;
            this.status = status;
            this.message = message;
            this.details = details;
            this.fieldErrors = fieldErrors == null
                    ? Collections.emptyMap()
                    : Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
            this.correlationId = UUID.randomUUID().toString();
            this.timestamp = Instant.now();
            this.path = path;
        }

        private static String defaultCode(int status) {
            return switch (status) {
                case 400 -> "BAD_REQUEST";
                case 401 -> "UNAUTHORIZED";
                case 403 -> "FORBIDDEN";
                case 404 -> "RESOURCE_NOT_FOUND";
                case 409 -> "RESOURCE_CONFLICT";
                default -> status >= 500 ? "INTERNAL_ERROR" : "REQUEST_REJECTED";
            };
        }

        public String getCode() {
            return code;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getDetails() {
            return details;
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }

        public String getCorrelationId() {
            return correlationId;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public String getPath() {
            return path;
        }

        /** Compatibilité avec l'ancien contrat de sécurité Spring. */
        public String getError() {
            return switch (status) {
                case 401 -> "Unauthorized";
                case 403 -> "Forbidden";
                case 404 -> "Not Found";
                default -> status >= 500 ? "Internal Server Error" : "Bad Request";
            };
        }
    }
}