package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.ActiveSessionService;
import com.example.cosmetest.business.service.ConnexionLogService;
import com.example.cosmetest.data.repository.SessionHistoryRepository;
import com.example.cosmetest.domain.model.ConnexionLog;
import com.example.cosmetest.domain.model.SessionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/connexions")
public class ConnexionController {

    private static final int MAX_PAGE_SIZE = 100;

    private final ConnexionLogService connexionLogService;
    private final ActiveSessionService activeSessionService;
    private final SessionHistoryRepository sessionHistoryRepository;

    public ConnexionController(ConnexionLogService connexionLogService,
                               ActiveSessionService activeSessionService,
                               SessionHistoryRepository sessionHistoryRepository) {
        this.connexionLogService = connexionLogService;
        this.activeSessionService = activeSessionService;
        this.sessionHistoryRepository = sessionHistoryRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin) {

        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size));
        Page<ConnexionLog> result;
        if (dateDebut != null && !dateDebut.isBlank() && dateFin != null && !dateFin.isBlank()) {
            java.time.LocalDateTime debut = java.time.LocalDate.parse(dateDebut).atStartOfDay();
            java.time.LocalDateTime fin = java.time.LocalDate.parse(dateFin).atTime(23, 59, 59);
            result = connexionLogService.findByDateRange(debut, fin, safePage, safeSize);
        } else {
            result = connexionLogService.findAll(safePage, safeSize);
        }

        var logs = result.getContent().stream().map(log -> Map.<String, Object>of(
            "id", log.getId(),
            "login", log.getLogin(),
            "success", log.isSuccess(),
            "ip", log.getIp() != null ? log.getIp() : "",
            "createdAt", log.getCreatedAt().toString()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
            "content", logs,
            "totalElements", result.getTotalElements(),
            "totalPages", result.getTotalPages(),
            "page", result.getNumber(),
            "size", result.getSize()
        ));
    }

    @DeleteMapping("/purge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> purge(@RequestParam String before) {
        int deleted = connexionLogService.purgeOlderThan(LocalDate.parse(before).atStartOfDay());
        return ResponseEntity.ok(Map.of("deleted", deleted, "before", before));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getActiveSessions() {
        List<Map<String, Object>> sessions = activeSessionService.getActiveSessions();
        return ResponseEntity.ok(Map.of(
            "sessions", sessions,
            "count", sessions.size()
        ));
    }

    @GetMapping("/session-history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSessionHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String login) {

        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size));
        PageRequest pageable = PageRequest.of(safePage, safeSize);
        Page<SessionHistory> result = (login != null && !login.isBlank())
                ? sessionHistoryRepository.findByLoginContainingIgnoreCaseOrderByLoginTimeDesc(login, pageable)
                : sessionHistoryRepository.findAllByOrderByLoginTimeDesc(pageable);

        var entries = result.getContent().stream().map(s -> {
            Map<String, Object> m = new java.util.HashMap<>();
            m.put("id", s.getId());
            m.put("login", s.getLogin());
            m.put("loginTime", s.getLoginTime().toString());
            m.put("logoutTime", s.getLogoutTime() != null ? s.getLogoutTime().toString() : null);
            m.put("durationSeconds", s.getDurationSeconds());
            m.put("reason", s.getReason());
            return m;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
            "content", entries,
            "totalElements", result.getTotalElements(),
            "totalPages", result.getTotalPages(),
            "page", result.getNumber(),
            "size", result.getSize()
        ));
    }

    @DeleteMapping("/session-history/purge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> purgeSessionHistory(@RequestParam String before) {
        var cutoff = LocalDate.parse(before)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();
        int deleted = sessionHistoryRepository.deleteByLogoutTimeBefore(cutoff);
        return ResponseEntity.ok(Map.of("deleted", deleted, "before", before));
    }
}
