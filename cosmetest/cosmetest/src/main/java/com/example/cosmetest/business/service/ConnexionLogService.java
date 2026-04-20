package com.example.cosmetest.business.service;

import com.example.cosmetest.domain.model.ConnexionLog;
import org.springframework.data.domain.Page;

public interface ConnexionLogService {
    void log(String login, boolean success, String ip);
    Page<ConnexionLog> findAll(int page, int size);
}
