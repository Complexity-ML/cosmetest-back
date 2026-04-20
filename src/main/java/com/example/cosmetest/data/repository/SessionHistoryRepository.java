package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.SessionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionHistoryRepository extends JpaRepository<SessionHistory, Long> {
    Page<SessionHistory> findAllByOrderByLoginTimeDesc(Pageable pageable);
    Page<SessionHistory> findByLoginContainingIgnoreCaseOrderByLoginTimeDesc(String login, Pageable pageable);
}
