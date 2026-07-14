package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class InfobancaireDTO {
    private Long idInfobancaire;

    @NotBlank(message = "Le code BIC ne peut pas être vide")
    private String bic;

    @NotBlank(message = "Le numéro IBAN ne peut pas être vide")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{10,30}$", message = "Le format du numéro IBAN est invalide")
    private String iban;

    @NotNull(message = "L'ID du volontaire ne peut pas être null")
    @Positive(message = "L'ID du volontaire doit être un nombre positif")
    private Integer idVol;

    public InfobancaireDTO() {
    }

    public InfobancaireDTO(String bic, String iban, Integer idVol) {
        this(null, bic, iban, idVol);
    }

    public InfobancaireDTO(Long idInfobancaire, String bic, String iban, Integer idVol) {
        this.idInfobancaire = idInfobancaire;
        this.bic = bic;
        this.iban = iban;
        this.idVol = idVol;
    }

    public Long getIdInfobancaire() { return idInfobancaire; }
    public void setIdInfobancaire(Long idInfobancaire) { this.idInfobancaire = idInfobancaire; }
    public String getBic() { return bic; }
    public void setBic(String bic) { this.bic = bic; }
    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }
    public Integer getIdVol() { return idVol; }
    public void setIdVol(Integer idVol) { this.idVol = idVol; }

    @Override
    public String toString() {
        return "InfobancaireDTO{idInfobancaire=" + idInfobancaire + ", bic='[REDACTED]', iban='[REDACTED]', idVol=" + idVol + '}';
    }
}
