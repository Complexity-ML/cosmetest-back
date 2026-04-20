package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * DTO (Data Transfer Object) pour l'entité Infobancaire
 */
public class InfobancaireDTO {

    @NotBlank(message = "Le code BIC ne peut pas être vide")
    @Pattern(regexp = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$",
            message = "Le format du code BIC est invalide")
    private String bic;

    @NotBlank(message = "Le numéro IBAN ne peut pas être vide")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$",
            message = "Le format du numéro IBAN est invalide")
    private String iban;

    @NotNull(message = "L'ID du volontaire ne peut pas être null")
    @Positive(message = "L'ID du volontaire doit être un nombre positif")
    private Integer idVol;

    // Constructeurs
    public InfobancaireDTO() {
    }

    public InfobancaireDTO(String bic, String iban, Integer idVol) {
        this.bic = bic;
        this.iban = iban;
        this.idVol = idVol;
    }

    // Getters et Setters
    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    @Override
    public String toString() {
        return "InfobancaireDTO{" +
                "bic='" + bic + '\'' +
                ", iban='" + iban + '\'' +
                ", idVol=" + idVol +
                '}';
    }
}