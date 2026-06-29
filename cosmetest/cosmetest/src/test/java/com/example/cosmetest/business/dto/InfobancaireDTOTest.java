package com.example.cosmetest.business.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InfobancaireDTO - Validation")
class InfobancaireDTOTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("accepte un IBAN francais alphanumerique valide")
    void acceptsFrenchAlphanumericIban() {
        InfobancaireDTO dto = new InfobancaireDTO("NTSBFRM1XXX", "FR802043302626N265452680342", 100);

        Set<ConstraintViolation<InfobancaireDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("refuse un IBAN avec caractere interdit")
    void rejectsIbanWithInvalidCharacter() {
        InfobancaireDTO dto = new InfobancaireDTO("NTSBFRM1XXX", "FR802043302626-265452680342", 100);

        Set<ConstraintViolation<InfobancaireDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(violation -> "iban".equals(violation.getPropertyPath().toString()));
    }
}
