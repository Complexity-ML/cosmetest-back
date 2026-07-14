package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.business.service.InfobancaireService;
import com.example.cosmetest.business.service.PreetudevolService;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SurrogateIdControllerTest {

    @Test
    void exposesSimpleInfobancaireRoutesByTechnicalId() throws Exception {
        InfobancaireService service = mock(InfobancaireService.class);
        InfobancaireDTO dto = new InfobancaireDTO(42L, "BNPAFRPP", "FR7630004000031234567890143", 100);
        when(service.getInfobancaireById(42L)).thenReturn(Optional.of(dto));

        assertThat(new InfobancaireController(service).getInfobancaireByTechnicalId(42L).getBody()).isSameAs(dto);
        assertRoute(InfobancaireController.class, "getInfobancaireByTechnicalId", GetMapping.class, "/{id}", Long.class);
        assertRoute(InfobancaireController.class, "updateInfobancaireByTechnicalId", PutMapping.class, "/{id}", Long.class, InfobancaireDTO.class);
        assertRoute(InfobancaireController.class, "deleteInfobancaireByTechnicalId", DeleteMapping.class, "/{id}", Long.class);
    }

    @Test
    void legacyInfobancaireNotFoundErrorDoesNotLeakBankData() {
        InfobancaireService service = mock(InfobancaireService.class);
        String bic = "SECRETBI";
        String iban = "FR7612345678901234567890123";
        when(service.getInfobancaireById(bic, iban, 100)).thenReturn(Optional.empty());

        ResponseStatusException error = catchThrowableOfType(
                ResponseStatusException.class,
                () -> new InfobancaireController(service).getInfobancaireById(bic, iban, 100));

        assertThat(error.getReason()).doesNotContain(bic, iban);
    }

    @Test
    void exposesSimplePreetudevolRoutesByTechnicalId() throws Exception {
        PreetudevolService service = mock(PreetudevolService.class);
        PreetudevolDTO dto = new PreetudevolDTO(42L, 1, 2, 3);
        when(service.getPreetudevolById(42L)).thenReturn(Optional.of(dto));

        assertThat(new PreetudevolController(service).getPreetudevolByTechnicalId(42L).getBody()).isSameAs(dto);
        assertRoute(PreetudevolController.class, "getPreetudevolByTechnicalId", GetMapping.class, "/{id}", Long.class);
        assertRoute(PreetudevolController.class, "updatePreetudevolByTechnicalId", PutMapping.class, "/{id}", Long.class, PreetudevolDTO.class);
        assertRoute(PreetudevolController.class, "deletePreetudevolByTechnicalId", DeleteMapping.class, "/{id}", Long.class);
    }

    private void assertRoute(Class<?> controller, String methodName, Class annotationType, String route, Class<?>... parameters) throws Exception {
        Method method = controller.getMethod(methodName, parameters);
        String[] values;
        if (annotationType == GetMapping.class) values = method.getAnnotation(GetMapping.class).value();
        else if (annotationType == PutMapping.class) values = method.getAnnotation(PutMapping.class).value();
        else values = method.getAnnotation(DeleteMapping.class).value();
        assertThat(values).containsExactly(route);
    }
}
