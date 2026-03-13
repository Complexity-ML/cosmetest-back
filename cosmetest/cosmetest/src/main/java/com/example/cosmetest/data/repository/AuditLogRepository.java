package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<AuditLog> findByEntiteOrderByCreatedAtDesc(String entite, Pageable pageable);
    Page<AuditLog> findByUtilisateurOrderByCreatedAtDesc(String utilisateur, Pageable pageable);

    @Modifying
    @Query("DELETE FROM AuditLog a WHERE a.createdAt < :before")
    int deleteByCreatedAtBefore(LocalDateTime before);
}
