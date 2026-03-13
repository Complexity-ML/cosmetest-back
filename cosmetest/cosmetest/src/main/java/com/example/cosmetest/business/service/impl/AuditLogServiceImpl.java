package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.data.repository.AuditLogRepository;
import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogServiceImpl(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void log(String utilisateur, AuditLog.Action action, String entite, String entiteId, String details, String ip) {
        repository.save(new AuditLog(utilisateur, action, entite, entiteId, details, ip));
    }

    @Override
    public Page<AuditLog> findAll(int page, int size) {
        return repository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }

    @Override
    public Page<AuditLog> findByEntite(String entite, int page, int size) {
        return repository.findByEntiteOrderByCreatedAtDesc(entite, PageRequest.of(page, size));
    }

    @Override
    public Page<AuditLog> findByUtilisateur(String utilisateur, int page, int size) {
        return repository.findByUtilisateurOrderByCreatedAtDesc(utilisateur, PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public int purgeOlderThan(LocalDateTime before) {
        return repository.deleteByCreatedAtBefore(before);
    }
}
