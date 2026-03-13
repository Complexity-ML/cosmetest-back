package com.example.cosmetest.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log", indexes = {
    @Index(name = "idx_audit_created_at", columnList = "created_at"),
    @Index(name = "idx_audit_entite", columnList = "entite"),
    @Index(name = "idx_audit_utilisateur", columnList = "utilisateur")
})
public class AuditLog {

    public enum Action {
        CREATE, UPDATE, DELETE, ARCHIVE, UNARCHIVE, LOGIN, LOGOUT, PAYE, ANNULATION, ASSIGN, UNASSIGN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    @Column(nullable = false)
    private String entite;

    @Column(name = "entite_id")
    private String entiteId;

    @Column(length = 500)
    private String details;

    @Column(length = 45)
    private String ip;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AuditLog() {}

    public AuditLog(String utilisateur, Action action, String entite, String entiteId, String details, String ip) {
        this.utilisateur = utilisateur;
        this.action = action;
        this.entite = entite;
        this.entiteId = entiteId;
        this.details = details;
        this.ip = ip;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUtilisateur() { return utilisateur; }
    public Action getAction() { return action; }
    public String getEntite() { return entite; }
    public String getEntiteId() { return entiteId; }
    public String getDetails() { return details; }
    public String getIp() { return ip; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
