package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) pour l'entité Preetudevol
 */
public class PreetudevolDTO {

    @NotNull(message = "L'ID de l'étude ne peut pas être null")
    @Min(value = 1, message = "L'ID de l'étude doit être un nombre positif")
    private Integer idEtude;

    @NotNull(message = "L'ID du groupe ne peut pas être null")
    @Min(value = 1, message = "L'ID du groupe doit être un nombre positif")
    private Integer idGroupe;

    @NotNull(message = "L'ID du volontaire ne peut pas être null")
    @Min(value = 1, message = "L'ID du volontaire doit être un nombre positif")
    private Integer idVolontaire;

    // Constructeurs
    public PreetudevolDTO() {
    }

    public PreetudevolDTO(Integer idEtude, Integer idGroupe, Integer idVolontaire) {
        this.idEtude = idEtude;
        this.idGroupe = idGroupe;
        this.idVolontaire = idVolontaire;
    }

    // Getters et Setters
    public Integer getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(Integer idEtude) {
        this.idEtude = idEtude;
    }

    public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    public Integer getIdVolontaire() {
        return idVolontaire;
    }

    public void setIdVolontaire(Integer idVolontaire) {
        this.idVolontaire = idVolontaire;
    }

    @Override
    public String toString() {
        return "PreetudevolDTO{" +
                "idEtude=" + idEtude +
                ", idGroupe=" + idGroupe +
                ", idVolontaire=" + idVolontaire +
                '}';
    }
}