package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.ActiveSessionService;
import com.example.cosmetest.business.service.ConnexionLogService;
import com.example.cosmetest.data.repository.SessionHistoryRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SessionHistoryPurgeControllerTest {

    @Test
    void purgeDeletesOnlySessionsEndedBeforeSelectedDate() {
        SessionHistoryRepository repository = mock(SessionHistoryRepository.class);
        ConnexionController controller = new ConnexionController(
                mock(ConnexionLogService.class),
                mock(ActiveSessionService.class),
                repository);
        var cutoff = LocalDate.of(2026, 1, 1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        when(repository.deleteByLogoutTimeBefore(cutoff)).thenReturn(7);

        var response = controller.purgeSessionHistory("2026-01-01");

        assertThat(response.getBody()).containsEntry("deleted", 7);
        assertThat(response.getBody()).containsEntry("before", "2026-01-01");
        verify(repository).deleteByLogoutTimeBefore(cutoff);
    }
}
