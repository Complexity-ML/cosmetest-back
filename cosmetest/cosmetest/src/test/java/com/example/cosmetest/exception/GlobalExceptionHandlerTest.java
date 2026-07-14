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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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

        ResponseEntity<java.util.Map<String, String>> response = handler.handleValidationExceptions(exception);

        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("_global", "Requête incohérente");
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
        assertThat(response.getBody().getDetails()).doesNotContain("MissingServletRequestParameterException");
    }
}
