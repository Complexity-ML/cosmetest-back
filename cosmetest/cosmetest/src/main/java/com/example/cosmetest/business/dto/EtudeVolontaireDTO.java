package com.example.cosmetest.business.dto;

/**
 * Data Transfer Object pour les associations étude-volontaire
 * Utilisé pour transférer les données entre la couche de présentation et la couche business
 */
public class EtudeVolontaireDTO {

    private int idEtude;
    private int idGroupe;
    private int idVolontaire;
    private int iv;
    private int numsujet;
    private int paye;
    private String statut;

    // Constructeur par défaut
    public EtudeVolontaireDTO() {
    }

    // Constructeur avec tous les champs
    public EtudeVolontaireDTO(int idEtude, int idGroupe, int idVolontaire, int iv, int numsujet, int paye, String statut) {
        this.idEtude = idEtude;
        this.idGroupe = idGroupe;
        this.idVolontaire = idVolontaire;
        this.iv = iv;
        this.numsujet = numsujet;
        this.paye = paye;
        this.statut = statut;
    }

    // Getters et Setters
    public int getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(int idEtude) {
        this.idEtude = idEtude;
    }

    public int getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(int idGroupe) {
        this.idGroupe = idGroupe;
    }

    public int getIdVolontaire() {
        return idVolontaire;
    }

    public void setIdVolontaire(int idVolontaire) {
        this.idVolontaire = idVolontaire;
    }

    public int getIv() {
        return iv;
    }

    public void setIv(int iv) {
        this.iv = iv;
    }

    public int getNumsujet() {
        return numsujet;
    }

    public void setNumsujet(int numsujet) {
        this.numsujet = numsujet;
    }

    public int getPaye() {
        return paye;
    }

    public void setPaye(int paye) {
        this.paye = paye;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}