package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO (Data Transfer Object) pour l'entité Groupe
 */
public class GroupeDTO {

    private Integer idGroupe;

    @NotNull(message = "L'ID d'étude ne peut pas être null")
    @Min(value = 1, message = "L'ID d'étude doit être un nombre positif")
    private Integer idEtude;

    @NotBlank(message = "L'intitulé ne peut pas être vide")
    private String intitule;

    private String description;

    @Min(value = 0, message = "L'âge minimum doit être positif ou zéro")
    private int ageMinimum;

    @Min(value = 0, message = "L'âge maximum doit être positif ou zéro")
    private int ageMaximum;

    private String ethnie;

    @PositiveOrZero(message = "Le nombre de sujets doit être positif ou zéro")
    private int nbSujet;

    @PositiveOrZero(message = "La valeur IV doit être positive ou zéro")
    private int iv;

    // Constructeurs
    public GroupeDTO() {
    }

    public GroupeDTO(Integer idGroupe, Integer idEtude, String intitule, String description,
                     int ageMinimum, int ageMaximum, String ethnie, int nbSujet, int iv) {
        this.idGroupe = idGroupe;
        this.idEtude = idEtude;
        this.intitule = intitule;
        this.description = description;
        this.ageMinimum = ageMinimum;
        this.ageMaximum = ageMaximum;
        this.ethnie = ethnie;
        this.nbSujet = nbSujet;
        this.iv = iv;
    }

    // Getters et Setters
    public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    public Integer getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(Integer idEtude) {
        this.idEtude = idEtude;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAgeMinimum() {
        return ageMinimum;
    }

    public void setAgeMinimum(int ageMinimum) {
        this.ageMinimum = ageMinimum;
    }

    public int getAgeMaximum() {
        return ageMaximum;
    }

    public void setAgeMaximum(int ageMaximum) {
        this.ageMaximum = ageMaximum;
    }

    public String getEthnie() {
        return ethnie;
    }

    public void setEthnie(String ethnie) {
        this.ethnie = ethnie;
    }

    public int getNbSujet() {
        return nbSujet;
    }

    public void setNbSujet(int nbSujet) {
        this.nbSujet = nbSujet;
    }

    public int getIv() {
        return iv;
    }

    public void setIv(int iv) {
        this.iv = iv;
    }
}