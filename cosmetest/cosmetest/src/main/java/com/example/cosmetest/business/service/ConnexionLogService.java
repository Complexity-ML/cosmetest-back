package com.example.cosmetest.business.service;

import com.example.cosmetest.domain.model.ConnexionLog;

import java.util.List;

public interface ConnexionLogService {
    void log(String login, boolean success, String ip);
    List<ConnexionLog> findAll();
}
