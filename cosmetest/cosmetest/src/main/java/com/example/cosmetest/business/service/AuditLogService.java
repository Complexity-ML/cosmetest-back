package com.example.cosmetest.business.service;

import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.data.domain.Page;

public interface AuditLogService {
    void log(String utilisateur, AuditLog.Action action, String entite, String entiteId, String details, String ip);
    Page<AuditLog> findAll(int page, int size);
    Page<AuditLog> findByEntite(String entite, int page, int size);
    Page<AuditLog> findByUtilisateur(String utilisateur, int page, int size);
}
