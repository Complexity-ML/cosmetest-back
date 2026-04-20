package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.data.repository.AuditLogRepository;
import com.example.cosmetest.domain.model.AuditLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditLogServiceImpl - Tests unitaires")
class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository repository;

    @InjectMocks
    private AuditLogServiceImpl service;

    // ─── log() ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("log() - doit sauvegarder une entrée avec les bons champs")
    void log_shouldSaveAuditLogWithCorrectFields() {
        service.log("alice", AuditLog.Action.CREATE, "VOLONTAIRE", "42", "détail", "127.0.0.1");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository).save(captor.capture());
        AuditLog saved = captor.getValue();

        assertThat(saved.getUtilisateur()).isEqualTo("alice");
        assertThat(saved.getAction()).isEqualTo(AuditLog.Action.CREATE);
        assertThat(saved.getEntite()).isEqualTo("VOLONTAIRE");
        assertThat(saved.getEntiteId()).isEqualTo("42");
        assertThat(saved.getDetails()).isEqualTo("détail");
        assertThat(saved.getIp()).isEqualTo("127.0.0.1");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("log() - doit accepter details et ip null")
    void log_shouldAcceptNullDetailsAndIp() {
        service.log("bob", AuditLog.Action.DELETE, "ETUDE", "5", null, null);
        verify(repository).save(any(AuditLog.class));
    }

    // ─── findAll() ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll() - doit retourner la page du repository")
    void findAll_shouldReturnRepositoryPage() {
        AuditLog log = new AuditLog("alice", AuditLog.Action.UPDATE, "RDV", "1", null, null);
        Page<AuditLog> page = new PageImpl<>(List.of(log), PageRequest.of(0, 50), 1);
        when(repository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(page);

        Page<AuditLog> result = service.findAll(0, 50);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findAllByOrderByCreatedAtDesc(PageRequest.of(0, 50));
    }

    @Test
    @DisplayName("findAll() - doit retourner une page vide si aucun log")
    void findAll_shouldReturnEmptyPage() {
        Page<AuditLog> empty = new PageImpl<>(List.of(), PageRequest.of(0, 50), 0);
        when(repository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(empty);

        Page<AuditLog> result = service.findAll(0, 50);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    // ─── findByEntite() ───────────────────────────────────────────────────────

    @Test
    @DisplayName("findByEntite() - doit filtrer par entité")
    void findByEntite_shouldFilterByEntity() {
        Page<AuditLog> page = new PageImpl<>(List.of(), PageRequest.of(0, 50), 0);
        when(repository.findByEntiteOrderByCreatedAtDesc(eq("VOLONTAIRE"), any(Pageable.class))).thenReturn(page);

        service.findByEntite("VOLONTAIRE", 0, 50);

        verify(repository).findByEntiteOrderByCreatedAtDesc("VOLONTAIRE", PageRequest.of(0, 50));
    }

    // ─── findByUtilisateur() ──────────────────────────────────────────────────

    @Test
    @DisplayName("findByUtilisateur() - doit filtrer par utilisateur")
    void findByUtilisateur_shouldFilterByUser() {
        Page<AuditLog> page = new PageImpl<>(List.of(), PageRequest.of(0, 50), 0);
        when(repository.findByUtilisateurOrderByCreatedAtDesc(eq("alice"), any(Pageable.class))).thenReturn(page);

        service.findByUtilisateur("alice", 0, 50);

        verify(repository).findByUtilisateurOrderByCreatedAtDesc("alice", PageRequest.of(0, 50));
    }

    // ─── purgeOlderThan() ─────────────────────────────────────────────────────

    @Test
    @DisplayName("purgeOlderThan() - doit appeler le repository avec la date et retourner le compte")
    void purgeOlderThan_shouldCallRepositoryAndReturnCount() {
        LocalDateTime cutoff = LocalDateTime.of(2024, 1, 1, 0, 0);
        when(repository.deleteByCreatedAtBefore(cutoff)).thenReturn(17);

        int deleted = service.purgeOlderThan(cutoff);

        assertThat(deleted).isEqualTo(17);
        verify(repository).deleteByCreatedAtBefore(cutoff);
    }

    @Test
    @DisplayName("purgeOlderThan() - doit retourner 0 si rien à supprimer")
    void purgeOlderThan_shouldReturnZeroWhenNothingToDelete() {
        LocalDateTime cutoff = LocalDateTime.of(2020, 1, 1, 0, 0);
        when(repository.deleteByCreatedAtBefore(cutoff)).thenReturn(0);

        int deleted = service.purgeOlderThan(cutoff);

        assertThat(deleted).isZero();
    }
}
