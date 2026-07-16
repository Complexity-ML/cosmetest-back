package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.SessionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

public interface SessionHistoryRepository extends JpaRepository<SessionHistory, Long> {
    Page<SessionHistory> findAllByOrderByLoginTimeDesc(Pageable pageable);
    Page<SessionHistory> findByLoginContainingIgnoreCaseOrderByLoginTimeDesc(String login, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM SessionHistory s WHERE s.logoutTime < :before")
    int deleteByLogoutTimeBefore(@Param("before") Instant before);
}
