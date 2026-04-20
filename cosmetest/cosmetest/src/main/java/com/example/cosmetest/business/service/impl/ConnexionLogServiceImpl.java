package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.service.ConnexionLogService;
import com.example.cosmetest.data.repository.ConnexionLogRepository;
import com.example.cosmetest.domain.model.ConnexionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
    public Page<ConnexionLog> findAll(int page, int size) {
        return repository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }
}
