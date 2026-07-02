package com.example.cosmetest.business.dto;

import java.time.LocalDate;

public class DocCheckDuplicateCandidateDTO {
    private Integer idVolontaire;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String telephone;
    private String email;
    private String reason;

    public DocCheckDuplicateCandidateDTO() {
    }

    public DocCheckDuplicateCandidateDTO(VolontaireDTO volontaire, String reason) {
        this.idVolontaire = volontaire.getIdVol();
        this.nom = volontaire.getNomVol();
        this.prenom = volontaire.getPrenomVol();
        this.dateNaissance = volontaire.getDateNaissance();
        this.telephone = volontaire.getTelPortableVol();
        this.email = volontaire.getEmailVol();
        this.reason = reason;
    }

    public Integer getIdVolontaire() {
        return idVolontaire;
    }

    public void setIdVolontaire(Integer idVolontaire) {
        this.idVolontaire = idVolontaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
