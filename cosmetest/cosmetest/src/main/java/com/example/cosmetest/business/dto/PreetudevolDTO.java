package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PreetudevolDTO {
    private Long idPreetudevol;

    @NotNull(message = "L'ID de l'étude ne peut pas être null")
    @Min(value = 1, message = "L'ID de l'étude doit être un nombre positif")
    private Integer idEtude;

    @NotNull(message = "L'ID du groupe ne peut pas être null")
    @Min(value = 1, message = "L'ID du groupe doit être un nombre positif")
    private Integer idGroupe;

    @NotNull(message = "L'ID du volontaire ne peut pas être null")
    @Min(value = 1, message = "L'ID du volontaire doit être un nombre positif")
    private Integer idVolontaire;

    public PreetudevolDTO() {
    }

    public PreetudevolDTO(Integer idEtude, Integer idGroupe, Integer idVolontaire) {
        this(null, idEtude, idGroupe, idVolontaire);
    }

    public PreetudevolDTO(Long idPreetudevol, Integer idEtude, Integer idGroupe, Integer idVolontaire) {
        this.idPreetudevol = idPreetudevol;
        this.idEtude = idEtude;
        this.idGroupe = idGroupe;
        this.idVolontaire = idVolontaire;
    }

    public Long getIdPreetudevol() { return idPreetudevol; }
    public void setIdPreetudevol(Long idPreetudevol) { this.idPreetudevol = idPreetudevol; }
    public Integer getIdEtude() { return idEtude; }
    public void setIdEtude(Integer idEtude) { this.idEtude = idEtude; }
    public Integer getIdGroupe() { return idGroupe; }
    public void setIdGroupe(Integer idGroupe) { this.idGroupe = idGroupe; }
    public Integer getIdVolontaire() { return idVolontaire; }
    public void setIdVolontaire(Integer idVolontaire) { this.idVolontaire = idVolontaire; }
}
