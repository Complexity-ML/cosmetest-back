package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

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
            @RequestParam(required = false) String dateFin) {

        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size));
        Page<AuditLog> result;
        if (dateDebut != null && !dateDebut.isBlank() && dateFin != null && !dateFin.isBlank()) {
            LocalDateTime debut = LocalDate.parse(dateDebut).atStartOfDay();
            LocalDateTime fin = LocalDate.parse(dateFin).atTime(23, 59, 59);
            result = auditLogService.findByDateRange(debut, fin, safePage, safeSize);
        } else if (entite != null && !entite.isBlank()) {
            result = auditLogService.findByEntite(entite.toUpperCase(), safePage, safeSize);
        } else if (utilisateur != null && !utilisateur.isBlank()) {
            result = auditLogService.findByUtilisateur(utilisateur, safePage, safeSize);
        } else {
            result = auditLogService.findAll(safePage, safeSize);
        }

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
