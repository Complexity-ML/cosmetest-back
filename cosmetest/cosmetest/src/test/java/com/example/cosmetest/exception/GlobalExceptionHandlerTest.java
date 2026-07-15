package com.example.cosmetest.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.security.authentication.BadCredentialsException;
import jakarta.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler - sécurité des erreurs")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Une erreur interne ne divulgue pas son message technique")
    void internalErrorDoesNotExposeTechnicalDetails() {
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleGlobalException(new RuntimeException("SQL secret table etude_volontaire"));

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Une erreur interne s'est produite");
        assertThat(response.getBody().getDetails())
                .doesNotContain("SQL")
                .doesNotContain("etude_volontaire");
    }

    @Test
    @DisplayName("Une ressource HTTP absente reste une 404 et non une 500")
    void missingResourceIsNotFound() {
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleNoResourceFoundException(
                        new NoResourceFoundException(HttpMethod.GET, "/assets/absent.js"));

        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("Ressource non trouvée");
    }

    @Test
    @DisplayName("Une erreur globale de validation ne provoque pas de ClassCastException")
    void validationSupportsGlobalObjectErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new ObjectError("request", "Requête incohérente"));
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(mock(), bindingResult);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleValidationExceptions(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getFieldErrors())
                .containsEntry("_global", "Requête incohérente");
    }

    @Test
    @DisplayName("Un statut HTTP métier déclaré n'est pas transformé en erreur 500")
    void responseStatusExceptionKeepsClientStatus() {
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleResponseStatusException(
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valeur refusée"));

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getDetails()).isEqualTo("Valeur refusée");
    }

    @Test
    @DisplayName("Un paramètre HTTP manquant produit une erreur 400 contrôlée")
    void missingRequestParameterIsBadRequest() {
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleBadRequestException(
                        new MissingServletRequestParameterException("dateDebut", "LocalDate"));

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getCode()).isEqualTo("BAD_REQUEST");
        assertThat(response.getBody().getDetails()).doesNotContain("MissingServletRequestParameterException");
    }

    @Test
    @DisplayName("Toutes les erreurs possèdent un identifiant de corrélation et un horodatage")
    void errorsHaveCorrelationIdAndTimestamp() {
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleEntityNotFoundException(new jakarta.persistence.EntityNotFoundException("absent"));

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("RESOURCE_NOT_FOUND");
        assertThat(response.getBody().getCorrelationId()).isNotBlank();
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Les erreurs d'authentification utilisent le même contrat structuré")
    void authenticationUsesStructuredErrorContract() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleAuthenticationException(
                        new BadCredentialsException("secret technique"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("UNAUTHORIZED");
        assertThat(response.getBody().getDetails()).doesNotContain("secret technique");
        assertThat(response.getBody().getPath()).isEqualTo("/api/auth/login");
    }

    @Test
    @DisplayName("Une recherche de volontaire ambiguë produit un conflit explicite")
    void ambiguousVolunteerIsConflict() {
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleAmbiguousVolontaire(
                        new AmbiguousVolontaireException("email", 2));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("AMBIGUOUS_VOLUNTEER");
    }
}
