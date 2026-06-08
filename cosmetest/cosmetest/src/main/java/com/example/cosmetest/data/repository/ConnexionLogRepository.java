package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.ConnexionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnexionLogRepository extends JpaRepository<ConnexionLog, Long> {
    Page<ConnexionLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<ConnexionLog> findByCreatedAtBetweenOrderByCreatedAtDesc(java.time.LocalDateTime debut, java.time.LocalDateTime fin, Pageable pageable);
    int deleteByCreatedAtBefore(java.time.LocalDateTime cutoff);
}
