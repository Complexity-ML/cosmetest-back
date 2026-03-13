package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = {"http://192.168.127.36:3000","http://192.168.127.36:5000","http://intranet:5000"}, allowCredentials = "true")
public class AuditController {

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
            @RequestParam(required = false) String utilisateur) {

        Page<AuditLog> result;
        if (entite != null && !entite.isBlank()) {
            result = auditLogService.findByEntite(entite.toUpperCase(), page, size);
        } else if (utilisateur != null && !utilisateur.isBlank()) {
            result = auditLogService.findByUtilisateur(utilisateur, page, size);
        } else {
            result = auditLogService.findAll(page, size);
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
}
