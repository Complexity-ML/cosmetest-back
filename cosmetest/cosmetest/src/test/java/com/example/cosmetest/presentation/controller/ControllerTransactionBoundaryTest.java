package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.service.impl.VolontaireHcServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ControllerTransactionBoundaryTest {

    @Test
    void docCheckAndHabitsControllersDoNotOwnTransactions() {
        assertThat(transactionalMethods(DocCheckImportController.class)).isEmpty();
        assertThat(transactionalMethods(VolontaireHcController.class)).isEmpty();
    }

    @Test
    void habitsReadBeforeWriteIsolationBelongsToTheService() throws Exception {
        Method save = VolontaireHcServiceImpl.class
                .getMethod("saveVolontaireHc", VolontaireHcDTO.class);

        Transactional transaction = save.getAnnotation(Transactional.class);
        assertThat(transaction).isNotNull();
        assertThat(transaction.isolation()).isEqualTo(Isolation.SERIALIZABLE);
    }

    @Test
    void photoProxyEndpointsDoNotKeepDatabaseTransactionsOpenDuringNetworkCalls() throws Exception {
        Method image = VolontaireController.class.getMethod(
                "getVolontairePhotoImage", Integer.class, String.class);
        Method thumbnail = VolontaireController.class.getMethod(
                "getVolontairePhotoThumbnail", Integer.class, String.class);

        assertThat(image.getAnnotation(Transactional.class)).isNull();
        assertThat(thumbnail.getAnnotation(Transactional.class)).isNull();
    }

    @Test
    void volunteerReadEndpointsDelegateTheirTransactionsToServices() {
        assertThat(Arrays.stream(VolontaireController.class.getDeclaredMethods())
                .map(method -> method.getAnnotation(Transactional.class))
                .filter(Objects::nonNull)
                .noneMatch(Transactional::readOnly)).isTrue();
    }

    private static java.util.List<String> transactionalMethods(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Transactional.class))
                .map(Method::getName)
                .toList();
    }
}
