package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.ActiveSessionService;
import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.service.ConnexionLogService;
import com.example.cosmetest.data.repository.SessionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminControllerHardeningTest {

    @Test
    void auditBoundsPaginationAndUsesGlobalCorsPolicy() {
        AuditLogService service = mock(AuditLogService.class);
        when(service.findAll(0, 100)).thenReturn(Page.empty());
        AuditController controller = new AuditController(service);

        controller.getLogs(-5, 10_000, null, null, null, null);

        verify(service).findAll(0, 100);
        assertThat(AuditController.class.getAnnotation(CrossOrigin.class)).isNull();
    }

    @Test
    void connexionBoundsPaginationAndUsesGlobalCorsPolicy() {
        ConnexionLogService logService = mock(ConnexionLogService.class);
        when(logService.findAll(0, 100)).thenReturn(Page.empty());
        ConnexionController controller = new ConnexionController(
                logService,
                mock(ActiveSessionService.class),
                mock(SessionHistoryRepository.class));

        controller.getLogs(-5, 10_000, null, null);

        verify(logService).findAll(0, 100);
        assertThat(ConnexionController.class.getAnnotation(CrossOrigin.class)).isNull();
    }
}
