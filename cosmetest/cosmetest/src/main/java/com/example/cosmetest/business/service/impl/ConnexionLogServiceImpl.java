package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.service.ConnexionLogService;
import com.example.cosmetest.data.repository.ConnexionLogRepository;
import com.example.cosmetest.domain.model.ConnexionLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnexionLogServiceImpl implements ConnexionLogService {

    private final ConnexionLogRepository repository;

    public ConnexionLogServiceImpl(ConnexionLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void log(String login, boolean success, String ip) {
        repository.save(new ConnexionLog(login, success, ip));
    }

    @Override
    public List<ConnexionLog> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }
}
