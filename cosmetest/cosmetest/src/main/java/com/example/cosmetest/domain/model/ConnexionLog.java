package com.example.cosmetest.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "connexion_log")
public class ConnexionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private boolean success;

    @Column(length = 45)
    private String ip;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ConnexionLog() {
    }

    public ConnexionLog(String login, boolean success, String ip) {
        this.login = login;
        this.success = success;
        this.ip = ip;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public boolean isSuccess() { return success; }
    public String getIp() { return ip; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
