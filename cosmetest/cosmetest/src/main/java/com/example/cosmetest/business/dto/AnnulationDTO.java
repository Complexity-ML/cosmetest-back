package com.example.cosmetest.business.dto;

/**
 * Data Transfer Object pour les annulations
 * Utilisé pour transférer les données entre la couche de présentation et la couche business
 */
public class AnnulationDTO {

    private Integer idAnnuler;
    private int idVol;
    private int idEtude;
    private String dateAnnulation;
    private String commentaire;

    // Constructeur par défaut
    public AnnulationDTO() {
    }

    // Constructeur avec paramètres essentiels
    public AnnulationDTO(int idVol, int idEtude, String dateAnnulation) {
        this.idVol = idVol;
        this.idEtude = idEtude;
        this.dateAnnulation = dateAnnulation;
    }

    // Constructeur avec tous les paramètres
    public AnnulationDTO(Integer idAnnuler, int idVol, int idEtude, String dateAnnulation, String commentaire) {
        this.idAnnuler = idAnnuler;
        this.idVol = idVol;
        this.idEtude = idEtude;
        this.dateAnnulation = dateAnnulation;
        this.commentaire = commentaire;
    }

    // Getters et Setters
    public Integer getIdAnnuler() {
        return idAnnuler;
    }

    public void setIdAnnuler(Integer idAnnuler) {
        this.idAnnuler = idAnnuler;
    }

    public int getIdVol() {
        return idVol;
    }

    public void setIdVol(int idVol) {
        this.idVol = idVol;
    }

    public int getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(int idEtude) {
        this.idEtude = idEtude;
    }

    public String getDateAnnulation() {
        return dateAnnulation;
    }

    public void setDateAnnulation(String dateAnnulation) {
        this.dateAnnulation = dateAnnulation;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}