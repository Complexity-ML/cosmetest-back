package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.ConnexionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnexionLogRepository extends JpaRepository<ConnexionLog, Long> {
    List<ConnexionLog> findAllByOrderByCreatedAtDesc();
}
