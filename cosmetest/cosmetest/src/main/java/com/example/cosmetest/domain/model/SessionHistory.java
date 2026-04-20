package com.example.cosmetest.domain.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "session_history")
public class SessionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(name = "login_time", nullable = false)
    private Instant loginTime;

    @Column(name = "logout_time")
    private Instant logoutTime;

    @Column(name = "duration_seconds")
    private Long durationSeconds;

    /** LOGOUT = explicit, TIMEOUT = inactive >30min, SERVER_RESTART = lost on restart */
    @Column(length = 20)
    private String reason;

    public SessionHistory() {}

    public SessionHistory(String login, Instant loginTime, Instant logoutTime, String reason) {
        this.login = login;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.durationSeconds = logoutTime != null
                ? logoutTime.getEpochSecond() - loginTime.getEpochSecond()
                : null;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public Instant getLoginTime() { return loginTime; }
    public Instant getLogoutTime() { return logoutTime; }
    public Long getDurationSeconds() { return durationSeconds; }
    public String getReason() { return reason; }
}
