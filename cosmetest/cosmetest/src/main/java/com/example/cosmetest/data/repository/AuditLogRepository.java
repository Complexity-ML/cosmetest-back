package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<AuditLog> findByEntiteOrderByCreatedAtDesc(String entite, Pageable pageable);
    Page<AuditLog> findByUtilisateurOrderByCreatedAtDesc(String utilisateur, Pageable pageable);
    Page<AuditLog> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime debut, LocalDateTime fin, Pageable pageable);

    @Query("""
            SELECT a FROM AuditLog a
            WHERE (:entite IS NULL OR UPPER(a.entite) = UPPER(:entite))
              AND (:utilisateur IS NULL OR LOWER(a.utilisateur) = LOWER(:utilisateur))
              AND (:action IS NULL OR a.action = :action)
              AND (:debut IS NULL OR a.createdAt >= :debut)
              AND (:fin IS NULL OR a.createdAt <= :fin)
            ORDER BY a.createdAt DESC
            """)
    Page<AuditLog> search(
            @Param("entite") String entite,
            @Param("utilisateur") String utilisateur,
            @Param("action") AuditLog.Action action,
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin,
            Pageable pageable);

    @Modifying
    @Query("DELETE FROM AuditLog a WHERE a.createdAt < :before")
    int deleteByCreatedAtBefore(LocalDateTime before);
}
