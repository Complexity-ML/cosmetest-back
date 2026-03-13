package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.ConnexionLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/connexions")
@CrossOrigin(origins = {"http://192.168.127.36:3000","http://192.168.127.36:5000","http://intranet:5000"}, allowCredentials = "true")
public class ConnexionController {

    private final ConnexionLogService connexionLogService;

    public ConnexionController(ConnexionLogService connexionLogService) {
        this.connexionLogService = connexionLogService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getLogs() {
        List<Map<String, Object>> logs = connexionLogService.findAll().stream()
            .map(log -> Map.<String, Object>of(
                "id", log.getId(),
                "login", log.getLogin(),
                "success", log.isSuccess(),
                "ip", log.getIp() != null ? log.getIp() : "",
                "createdAt", log.getCreatedAt().toString()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }
}
