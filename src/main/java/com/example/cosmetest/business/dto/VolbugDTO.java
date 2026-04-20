package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) pour l'entité Volbug
 */
public class VolbugDTO {

    @NotNull(message = "L'ID du volontaire ne peut pas être null")
    @Min(value = 1, message = "L'ID du volontaire doit être un nombre positif")
    private Integer idVol;

    // Constructeurs
    public VolbugDTO() {
    }

    public VolbugDTO(Integer idVol) {
        this.idVol = idVol;
    }

    // Getters et Setters
    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    @Override
    public String toString() {
        return "VolbugDTO{" +
                "idVol=" + idVol +
                '}';
    }
}