// src/main/java/com/example/cosmetest/business/dto/ActiviteRecenteDTO.java
package com.example.cosmetest.business.dto;

import java.time.LocalDate;

public class ActiviteRecenteDTO {
    private Long id;
    private String type;
    private LocalDate date;
    private String user;
    private String description;

    // Constructeurs
    public ActiviteRecenteDTO() {
    }

    public ActiviteRecenteDTO(Long id, String type, LocalDate date, String user, String description) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.user = user;
        this.description = description;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}