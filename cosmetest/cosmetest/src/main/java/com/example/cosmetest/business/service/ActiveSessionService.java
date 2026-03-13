package com.example.cosmetest.business.service;

import com.example.cosmetest.data.repository.SessionHistoryRepository;
import com.example.cosmetest.domain.model.SessionHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks users currently active (in-memory, reset on restart).
 * A session is considered "active" if the user made a request within the last ACTIVE_TTL_SECONDS.
 * loginTime = time of explicit login (or first seen heartbeat if session survived a restart).
 * lastActivity = time of last authenticated request.
 */
@Service
public class ActiveSessionService {

    private static final Logger logger = LoggerFactory.getLogger(ActiveSessionService.class);

    /** A session inactive for more than this is considered expired and hidden */
    private static final long ACTIVE_TTL_SECONDS = 10 * 60; // 10 minutes

    private static class SessionInfo {
        final Instant loginTime;
        volatile Instant lastActivity;

        SessionInfo(Instant loginTime) {
            this.loginTime = loginTime;
            this.lastActivity = loginTime;
        }
    }

    private final ConcurrentHashMap<String, SessionInfo> sessions = new ConcurrentHashMap<>();
    private final SessionHistoryRepository sessionHistoryRepository;

    public ActiveSessionService(SessionHistoryRepository sessionHistoryRepository) {
        this.sessionHistoryRepository = sessionHistoryRepository;
    }

    /** Called on explicit login — resets loginTime */
    public void register(String login) {
        sessions.put(login, new SessionInfo(Instant.now()));
    }

    /** Called on every authenticated request — updates lastActivity */
    public void heartbeat(String login) {
        sessions.compute(login, (k, existing) -> {
            if (existing == null) {
                // No explicit login recorded (e.g., after server restart) — create entry
                return new SessionInfo(Instant.now());
            }
            existing.lastActivity = Instant.now();
            return existing;
        });
    }

    /** Called on logout — persists the session history record */
    public void unregister(String login) {
        SessionInfo info = sessions.remove(login);
        if (info != null) {
            try {
                sessionHistoryRepository.save(
                    new SessionHistory(login, info.loginTime, Instant.now(), "LOGOUT")
                );
            } catch (Exception ex) {
                logger.error("Échec de persistance du logout pour '{}': {}", login, ex.getMessage());
            }
        }
    }

    /** Runs every minute — persists and removes sessions inactive for more than ACTIVE_TTL_SECONDS */
    @Scheduled(fixedDelay = 60_000)
    public void evictTimedOutSessions() {
        Instant cutoff = Instant.now().minusSeconds(ACTIVE_TTL_SECONDS);
        sessions.forEach((login, info) -> {
            if (info.lastActivity.isBefore(cutoff)) {
                if (sessions.remove(login, info)) { // atomic: only removes if value still matches
                    try {
                        sessionHistoryRepository.save(
                            new SessionHistory(login, info.loginTime, Instant.now(), "TIMEOUT")
                        );
                    } catch (Exception ex) {
                        logger.error("Échec de persistance du timeout pour '{}': {}", login, ex.getMessage());
                    }
                }
            }
        });
    }

    /** Returns sessions active within the last ACTIVE_TTL_SECONDS, sorted by loginTime */
    public List<Map<String, Object>> getActiveSessions() {

        List<Map<String, Object>> result = new ArrayList<>();
        sessions.forEach((login, info) -> {
            long durationSeconds = Instant.now().getEpochSecond() - info.loginTime.getEpochSecond();
            long idleSeconds = Instant.now().getEpochSecond() - info.lastActivity.getEpochSecond();
            result.add(Map.of(
                "login", login,
                "connectedSince", info.loginTime.toString(),
                "durationSeconds", durationSeconds,
                "idleSeconds", idleSeconds
            ));
        });
        result.sort((a, b) -> ((String) a.get("connectedSince")).compareTo((String) b.get("connectedSince")));
        return result;
    }

    public int countActive() {
        Instant cutoff = Instant.now().minusSeconds(ACTIVE_TTL_SECONDS);
        return (int) sessions.values().stream().filter(s -> !s.lastActivity.isBefore(cutoff)).count();
    }
}
