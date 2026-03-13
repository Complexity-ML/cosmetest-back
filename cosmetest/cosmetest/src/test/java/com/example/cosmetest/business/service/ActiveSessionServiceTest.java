package com.example.cosmetest.business.service;

import com.example.cosmetest.data.repository.SessionHistoryRepository;
import com.example.cosmetest.domain.model.SessionHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ActiveSessionService - Tests unitaires")
class ActiveSessionServiceTest {

    @Mock
    private SessionHistoryRepository sessionHistoryRepository;

    @InjectMocks
    private ActiveSessionService service;

    // ─── Helpers réflexion ────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private ConcurrentHashMap<String, Object> getSessions() throws Exception {
        Field f = ActiveSessionService.class.getDeclaredField("sessions");
        f.setAccessible(true);
        return (ConcurrentHashMap<String, Object>) f.get(service);
    }

    private void setLastActivity(Object sessionInfo, Instant instant) throws Exception {
        Field f = sessionInfo.getClass().getDeclaredField("lastActivity");
        f.setAccessible(true);
        f.set(sessionInfo, instant);
    }

    private Instant getLoginTime(Object sessionInfo) throws Exception {
        Field f = sessionInfo.getClass().getDeclaredField("loginTime");
        f.setAccessible(true);
        return (Instant) f.get(sessionInfo);
    }

    // ─── register() ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("register() - doit créer une nouvelle session en mémoire")
    void register_shouldCreateSession() throws Exception {
        service.register("alice");

        assertThat(getSessions()).containsKey("alice");
        verify(sessionHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("register() - appelé deux fois, doit réinitialiser loginTime")
    void register_calledTwice_shouldResetLoginTime() throws Exception {
        service.register("alice");
        Instant first = getLoginTime(getSessions().get("alice"));

        Thread.sleep(10);
        service.register("alice");
        Instant second = getLoginTime(getSessions().get("alice"));

        assertThat(second).isAfterOrEqualTo(first);
        assertThat(getSessions()).hasSize(1);
    }

    // ─── heartbeat() ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("heartbeat() - session inexistante : doit créer l'entrée sans persister")
    void heartbeat_noExistingSession_shouldCreateEntry() throws Exception {
        service.heartbeat("bob");

        assertThat(getSessions()).containsKey("bob");
        verify(sessionHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("heartbeat() - session existante : doit mettre à jour lastActivity")
    void heartbeat_existingSession_shouldUpdateLastActivity() throws Exception {
        service.register("alice");
        Object info = getSessions().get("alice");
        setLastActivity(info, Instant.now().minusSeconds(300));

        Instant before = Instant.now();
        service.heartbeat("alice");
        Instant after = Instant.now();

        Field f = info.getClass().getDeclaredField("lastActivity");
        f.setAccessible(true);
        Instant updated = (Instant) f.get(info);

        assertThat(updated).isBetween(before, after);
        verify(sessionHistoryRepository, never()).save(any());
    }

    // ─── unregister() ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("unregister() - doit supprimer la session et persister avec raison LOGOUT")
    void unregister_existingSession_shouldRemoveAndPersistLogout() throws Exception {
        service.register("alice");
        service.unregister("alice");

        assertThat(getSessions()).doesNotContainKey("alice");

        ArgumentCaptor<SessionHistory> captor = ArgumentCaptor.forClass(SessionHistory.class);
        verify(sessionHistoryRepository).save(captor.capture());
        SessionHistory saved = captor.getValue();

        assertThat(saved.getLogin()).isEqualTo("alice");
        assertThat(saved.getReason()).isEqualTo("LOGOUT");
        assertThat(saved.getLogoutTime()).isNotNull();
        assertThat(saved.getDurationSeconds()).isNotNull().isGreaterThanOrEqualTo(0L);
    }

    @Test
    @DisplayName("unregister() - session inexistante : aucune persistance")
    void unregister_unknownUser_shouldNotSave() {
        service.unregister("ghost");

        verify(sessionHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("unregister() - loginTime doit correspondre à la session d'origine")
    void unregister_shouldPreserveOriginalLoginTime() throws Exception {
        service.register("alice");
        Instant loginTime = getLoginTime(getSessions().get("alice"));

        service.unregister("alice");

        ArgumentCaptor<SessionHistory> captor = ArgumentCaptor.forClass(SessionHistory.class);
        verify(sessionHistoryRepository).save(captor.capture());
        assertThat(captor.getValue().getLoginTime()).isEqualTo(loginTime);
    }

    // ─── evictTimedOutSessions() ───────────────────────────────────────────────

    @Test
    @DisplayName("evictTimedOutSessions() - session expirée : doit évincer et persister TIMEOUT")
    void evictTimedOutSessions_staleSession_shouldEvictAndPersistTimeout() throws Exception {
        service.register("alice");
        setLastActivity(getSessions().get("alice"), Instant.now().minusSeconds(11 * 60));

        service.evictTimedOutSessions();

        assertThat(getSessions()).doesNotContainKey("alice");

        ArgumentCaptor<SessionHistory> captor = ArgumentCaptor.forClass(SessionHistory.class);
        verify(sessionHistoryRepository).save(captor.capture());
        assertThat(captor.getValue().getLogin()).isEqualTo("alice");
        assertThat(captor.getValue().getReason()).isEqualTo("TIMEOUT");
    }

    @Test
    @DisplayName("evictTimedOutSessions() - session active : doit la conserver")
    void evictTimedOutSessions_activeSession_shouldKeep() throws Exception {
        service.register("alice");

        service.evictTimedOutSessions();

        assertThat(getSessions()).containsKey("alice");
        verify(sessionHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("evictTimedOutSessions() - mix actives/expirées : évince uniquement les expirées")
    void evictTimedOutSessions_mixed_shouldOnlyEvictStale() throws Exception {
        service.register("alice"); // active
        service.register("bob");   // sera expirée
        setLastActivity(getSessions().get("bob"), Instant.now().minusSeconds(11 * 60));

        service.evictTimedOutSessions();

        assertThat(getSessions()).containsKey("alice");
        assertThat(getSessions()).doesNotContainKey("bob");

        ArgumentCaptor<SessionHistory> captor = ArgumentCaptor.forClass(SessionHistory.class);
        verify(sessionHistoryRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getLogin()).isEqualTo("bob");
        assertThat(captor.getValue().getReason()).isEqualTo("TIMEOUT");
    }

    @Test
    @DisplayName("evictTimedOutSessions() - aucune session : aucune persistance")
    void evictTimedOutSessions_empty_shouldDoNothing() {
        service.evictTimedOutSessions();

        verify(sessionHistoryRepository, never()).save(any());
    }

    // ─── getActiveSessions() ──────────────────────────────────────────────────

    @Test
    @DisplayName("getActiveSessions() - retourne les sessions actives triées par loginTime")
    void getActiveSessions_shouldReturnSortedByLoginTime() throws Exception {
        service.register("alice");
        Thread.sleep(5); // garantit un loginTime distinct
        service.register("bob");

        List<Map<String, Object>> result = service.getActiveSessions();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).get("login")).isEqualTo("alice");
        assertThat(result.get(1).get("login")).isEqualTo("bob");
    }

    @Test
    @DisplayName("getActiveSessions() - chaque entrée contient login, connectedSince, durationSeconds, idleSeconds")
    void getActiveSessions_shouldIncludeAllFields() {
        service.register("alice");

        List<Map<String, Object>> result = service.getActiveSessions();

        assertThat(result).hasSize(1);
        Map<String, Object> s = result.get(0);
        assertThat(s).containsKeys("login", "connectedSince", "durationSeconds", "idleSeconds");
        assertThat(s.get("login")).isEqualTo("alice");
        assertThat((Long) s.get("durationSeconds")).isGreaterThanOrEqualTo(0L);
        assertThat((Long) s.get("idleSeconds")).isGreaterThanOrEqualTo(0L);
    }

    @Test
    @DisplayName("getActiveSessions() - liste vide si aucune session")
    void getActiveSessions_noSessions_shouldReturnEmpty() {
        assertThat(service.getActiveSessions()).isEmpty();
    }

    @Test
    @DisplayName("getActiveSessions() - déclenche l'éviction des sessions expirées avant de retourner")
    void getActiveSessions_shouldEvictStaleBeforeReturning() throws Exception {
        service.register("stale");
        setLastActivity(getSessions().get("stale"), Instant.now().minusSeconds(11 * 60));

        List<Map<String, Object>> result = service.getActiveSessions();

        assertThat(result).isEmpty();
        verify(sessionHistoryRepository).save(any(SessionHistory.class));
    }

    // ─── countActive() ────────────────────────────────────────────────────────

    @Test
    @DisplayName("countActive() - compte uniquement les sessions récentes")
    void countActive_shouldCountOnlyRecentSessions() throws Exception {
        service.register("alice");
        service.register("bob");
        setLastActivity(getSessions().get("bob"), Instant.now().minusSeconds(11 * 60));

        assertThat(service.countActive()).isEqualTo(1);
    }

    @Test
    @DisplayName("countActive() - retourne 0 si aucune session")
    void countActive_noSessions_shouldReturnZero() {
        assertThat(service.countActive()).isZero();
    }

    @Test
    @DisplayName("countActive() - retourne le bon compte avec plusieurs sessions actives")
    void countActive_multipleSessions_shouldReturnCorrectCount() {
        service.register("alice");
        service.register("bob");
        service.register("charlie");

        assertThat(service.countActive()).isEqualTo(3);
    }

    // ─── Robustesse DB ────────────────────────────────────────────────────────

    @Test
    @DisplayName("evictTimedOutSessions() - exception BDD : session évincée malgré l'erreur")
    void evictTimedOutSessions_dbError_shouldStillEvictFromMemory() throws Exception {
        service.register("alice");
        setLastActivity(getSessions().get("alice"), Instant.now().minusSeconds(11 * 60));
        doThrow(new RuntimeException("DB down")).when(sessionHistoryRepository).save(any());

        service.evictTimedOutSessions(); // ne doit pas propager l'exception

        assertThat(getSessions()).doesNotContainKey("alice"); // évincée malgré l'erreur BDD
    }

    @Test
    @DisplayName("unregister() - exception BDD : aucune exception propagée, session retirée")
    void unregister_dbError_shouldNotThrow() throws Exception {
        service.register("alice");
        doThrow(new RuntimeException("DB down")).when(sessionHistoryRepository).save(any());

        service.unregister("alice"); // ne doit pas lever d'exception

        assertThat(getSessions()).doesNotContainKey("alice");
    }

    // ─── Scénarios complets ───────────────────────────────────────────────────

    @Test
    @DisplayName("Scénario login → activité → logout : trace complète en BDD")
    void scenario_loginActivityLogout_shouldPersistLogout() throws Exception {
        service.register("alice");
        service.heartbeat("alice");
        service.heartbeat("alice");

        assertThat(getSessions()).containsKey("alice");
        assertThat(service.countActive()).isEqualTo(1);

        service.unregister("alice");

        assertThat(getSessions()).doesNotContainKey("alice");
        assertThat(service.countActive()).isZero();

        ArgumentCaptor<SessionHistory> captor = ArgumentCaptor.forClass(SessionHistory.class);
        verify(sessionHistoryRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getReason()).isEqualTo("LOGOUT");
    }

    @Test
    @DisplayName("Scénario inactivité → eviction automatique : trace TIMEOUT en BDD")
    void scenario_inactiveUser_shouldBeEvictedWithTimeout() throws Exception {
        service.register("inactive");
        setLastActivity(getSessions().get("inactive"), Instant.now().minusSeconds(11 * 60));

        // Le scheduler tourne
        service.evictTimedOutSessions();

        // La session active doit être vide
        assertThat(service.getActiveSessions()).isEmpty();

        // Un TIMEOUT doit être persisté (1 depuis evict, pas de 2ème depuis getActiveSessions car déjà parti)
        ArgumentCaptor<SessionHistory> captor = ArgumentCaptor.forClass(SessionHistory.class);
        verify(sessionHistoryRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getReason()).isEqualTo("TIMEOUT");
    }
}
