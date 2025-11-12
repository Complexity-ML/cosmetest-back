package com.example.cosmetest.business.dto;

import java.sql.Date;
import java.time.LocalDate;
/**
 * Data Transfer Object pour les rendez-vous
 * Utilisé pour transférer les données entre la couche de présentation et la couche business
 */
public class RdvDTO {

    private Integer idEtude;
    private Integer idRdv;
    private Integer idVolontaire;
    private Integer idGroupe;
    private Date date;
    private String heure;
    private String etat;
    private String commentaires;
    private String etudeRef;
    //private UUID uuid;
    //private Integer sequence; // Nouveau champ




    // Constructeur par défaut
    public RdvDTO( ) {
        
    }

    // Constructeur avec tous les champs
    public RdvDTO(Integer idEtude, Integer idRdv, Integer idVolontaire, Integer idGroupe,
                  Date date, String heure, String etat, String commentaires, String etudeRef) {
        this.idEtude = idEtude;
        this.idRdv = idRdv;
        this.idVolontaire = idVolontaire;
        this.idGroupe = idGroupe;
        this.date = date;
        this.heure = heure;
        this.etat = etat;
        this.commentaires = commentaires;
        this.etudeRef = etudeRef;
    }

    // Getters et Setters
    public Integer getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(Integer idEtude) {
        this.idEtude = idEtude;
    }

    public Integer getIdRdv() {
        return idRdv;
    }

    public void setIdRdv(Integer idRdv) {
        this.idRdv = idRdv;
    }

    public Integer getIdVolontaire() {
        return idVolontaire;
    }

    public void setIdVolontaire(Integer idVolontaire) {
        this.idVolontaire = idVolontaire;
    }

    public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    /**
     * Safe getter for the date field that handles null values
     * Add this method to your RdvDTO class
     */
    public LocalDate getDate() {
        // Handle null date gracefully
        if (this.date == null) {
            return null;
        }
        return this.date.toLocalDate();
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public void setEtudeRef(String etudeRef) {
        this.etudeRef = etudeRef;
    }

    public String getEtudeRef() {
        return this.etudeRef;
    }

    //public UUID getUuid() {
    //    return uuid;
    //}

    //public void setUuid(UUID uuid) {
    //    this.uuid = uuid;
    //}

    //public Integer getSequence() {
    //    return sequence;
    //}

    //public void setSequence(Integer sequence) {
    //    this.sequence = sequence;
    //}

}