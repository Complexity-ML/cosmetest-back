package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Locale;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private static final int MAX_PAGE_SIZE = 100;

    private final AuditLogService auditLogService;

    public AuditController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String entite,
            @RequestParam(required = false) String utilisateur,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin,
            @RequestParam(required = false) String action) {

        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size));
        String normalizedEntity = blankToNull(entite);
        String normalizedUser = blankToNull(utilisateur);
        LocalDateTime debut = blankToNull(dateDebut) == null
                ? null : LocalDate.parse(dateDebut).atStartOfDay();
        LocalDateTime fin = blankToNull(dateFin) == null
                ? null : LocalDate.parse(dateFin).atTime(23, 59, 59);
        AuditLog.Action normalizedAction = parseAction(action);
        Page<AuditLog> result = auditLogService.search(
                normalizedEntity, normalizedUser, normalizedAction,
                debut, fin, safePage, safeSize);

        var logs = result.getContent().stream().map(log -> Map.<String, Object>of(
            "id", log.getId(),
            "utilisateur", log.getUtilisateur(),
            "action", log.getAction().name(),
            "entite", log.getEntite(),
            "entiteId", log.getEntiteId() != null ? log.getEntiteId() : "",
            "details", log.getDetails() != null ? log.getDetails() : "",
            "ip", log.getIp() != null ? log.getIp() : "",
            "createdAt", log.getCreatedAt().toString()
        )).toList();

        return ResponseEntity.ok(Map.of(
            "content", logs,
            "totalElements", result.getTotalElements(),
            "totalPages", result.getTotalPages(),
            "page", result.getNumber(),
            "size", result.getSize()
        ));
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private static AuditLog.Action parseAction(String action) {
        String normalized = blankToNull(action);
        if (normalized == null) return null;
        try {
            return AuditLog.Action.valueOf(normalized.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Action d'audit inconnue");
        }
    }

    @DeleteMapping("/purge")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> purge(
            @RequestParam String before,
            HttpServletRequest request) {
        java.time.LocalDateTime cutoff = java.time.LocalDate.parse(before).atStartOfDay();
        int deleted = auditLogService.purgeOlderThan(cutoff);
        return ResponseEntity.ok(Map.of("deleted", deleted, "before", before));
    }
}
