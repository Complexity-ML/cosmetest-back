package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.data.repository.ConnexionLogRepository;
import com.example.cosmetest.domain.model.ConnexionLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConnexionLogServiceImpl - Tests unitaires")
class ConnexionLogServiceImplTest {

    @Mock
    private ConnexionLogRepository repository;

    @InjectMocks
    private ConnexionLogServiceImpl service;

    // ─── log() ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("log() - connexion réussie : doit sauvegarder avec success=true")
    void log_successfulLogin_shouldSaveWithSuccessTrue() {
        service.log("alice", true, "192.168.1.1");

        ArgumentCaptor<ConnexionLog> captor = ArgumentCaptor.forClass(ConnexionLog.class);
        verify(repository).save(captor.capture());
        ConnexionLog saved = captor.getValue();

        assertThat(saved.getLogin()).isEqualTo("alice");
        assertThat(saved.isSuccess()).isTrue();
        assertThat(saved.getIp()).isEqualTo("192.168.1.1");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("log() - connexion échouée : doit sauvegarder avec success=false")
    void log_failedLogin_shouldSaveWithSuccessFalse() {
        service.log("hacker", false, "10.0.0.1");

        ArgumentCaptor<ConnexionLog> captor = ArgumentCaptor.forClass(ConnexionLog.class);
        verify(repository).save(captor.capture());

        assertThat(captor.getValue().isSuccess()).isFalse();
        assertThat(captor.getValue().getLogin()).isEqualTo("hacker");
    }

    @Test
    @DisplayName("log() - doit accepter une IP null")
    void log_shouldAcceptNullIp() {
        service.log("alice", true, null);
        verify(repository).save(any(ConnexionLog.class));
    }

    // ─── findAll() ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll() - doit retourner la liste du repository triée par date desc")
    void findAll_shouldReturnRepositoryList() {
        ConnexionLog log1 = new ConnexionLog("alice", true, "127.0.0.1");
        ConnexionLog log2 = new ConnexionLog("bob", false, "10.0.0.2");
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(log1, log2));

        List<ConnexionLog> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLogin()).isEqualTo("alice");
        assertThat(result.get(1).getLogin()).isEqualTo("bob");
        verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("findAll() - doit retourner une liste vide si aucun log")
    void findAll_shouldReturnEmptyListWhenNoLogs() {
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of());

        List<ConnexionLog> result = service.findAll();

        assertThat(result).isEmpty();
    }
}
