package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.sql.Date;

/**
 * DTO (Data Transfer Object) pour l'entité Preinscrit
 */
public class PreinscritDTO {

    private Integer idPreinscrit;

    private String datePreInscription;

    @NotBlank(message = "Le titre ne peut pas être vide")
    @Pattern(regexp = "M\\.|Mme|Mlle", message = "Le titre doit être 'M.', 'Mme' ou 'Mlle'")
    private String titre;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    private String prenom;

    private String adresse;

    @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir 5 chiffres")
    private String codePostal;

    private String ville;

    @Pattern(regexp = "^[0-9]{10}$", message = "Le téléphone domicile doit contenir 10 chiffres")
    private String telDomicile;

    @Pattern(regexp = "^[0-9]{10}$", message = "Le téléphone portable doit contenir 10 chiffres")
    private String telPortable;

    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le sexe ne peut pas être vide")
    @Pattern(regexp = "M|F", message = "Le sexe doit être 'M' ou 'F'")
    private String sexe;

    private String dateNaissance;

    @Pattern(regexp = "^[1-6]$", message = "Le phototype doit être un chiffre entre 1 et 6")
    private String phototype;

    @Pattern(regexp = "Oui|Non", message = "La peau sensible doit être 'Oui' ou 'Non'")
    private String peauSensible;

    private String mapyeux;

    private String maplevres;

    private String mapsourcils;

    private String peauVisage;

    private String rideVisage;

    private String tachePigmentaireVisage;

    private String boutonsVisage;

    private String comedonsVisage;

    private String poresDilatesVisage;

    private String pochesYeuxVisage;

    private String cernesVisage;

    private String ethnie;

    private Date rdvDate;

    private String rdvHeure;

    @Pattern(regexp = "En attente|Validé|Refusé|Annulé", message = "L'état doit être 'En attente', 'Validé', 'Refusé' ou 'Annulé'")
    private String etat;

    private String commentaires;

    // Constructeurs
    public PreinscritDTO() {
    }

    // Getters et Setters
    public Integer getIdPreinscrit() {
        return idPreinscrit;
    }

    public void setIdPreinscrit(Integer idPreinscrit) {
        this.idPreinscrit = idPreinscrit;
    }

    public String getDatePreInscription() {
        return datePreInscription;
    }

    public void setDatePreInscription(String datePreInscription) {
        this.datePreInscription = datePreInscription;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getTelDomicile() {
        return telDomicile;
    }

    public void setTelDomicile(String telDomicile) {
        this.telDomicile = telDomicile;
    }

    public String getTelPortable() {
        return telPortable;
    }

    public void setTelPortable(String telPortable) {
        this.telPortable = telPortable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPhototype() {
        return phototype;
    }

    public void setPhototype(String phototype) {
        this.phototype = phototype;
    }

    public String getPeauSensible() {
        return peauSensible;
    }

    public void setPeauSensible(String peauSensible) {
        this.peauSensible = peauSensible;
    }

    public String getMapyeux() {
        return mapyeux;
    }

    public void setMapyeux(String mapyeux) {
        this.mapyeux = mapyeux;
    }

    public String getMaplevres() {
        return maplevres;
    }

    public void setMaplevres(String maplevres) {
        this.maplevres = maplevres;
    }

    public String getMapsourcils() {
        return mapsourcils;
    }

    public void setMapsourcils(String mapsourcils) {
        this.mapsourcils = mapsourcils;
    }

    public String getPeauVisage() {
        return peauVisage;
    }

    public void setPeauVisage(String peauVisage) {
        this.peauVisage = peauVisage;
    }

    public String getRideVisage() {
        return rideVisage;
    }

    public void setRideVisage(String rideVisage) {
        this.rideVisage = rideVisage;
    }

    public String getTachePigmentaireVisage() {
        return tachePigmentaireVisage;
    }

    public void setTachePigmentaireVisage(String tachePigmentaireVisage) {
        this.tachePigmentaireVisage = tachePigmentaireVisage;
    }

    public String getBoutonsVisage() {
        return boutonsVisage;
    }

    public void setBoutonsVisage(String boutonsVisage) {
        this.boutonsVisage = boutonsVisage;
    }

    public String getComedonsVisage() {
        return comedonsVisage;
    }

    public void setComedonsVisage(String comedonsVisage) {
        this.comedonsVisage = comedonsVisage;
    }

    public String getPoresDilatesVisage() {
        return poresDilatesVisage;
    }

    public void setPoresDilatesVisage(String poresDilatesVisage) {
        this.poresDilatesVisage = poresDilatesVisage;
    }

    public String getPochesYeuxVisage() {
        return pochesYeuxVisage;
    }

    public void setPochesYeuxVisage(String pochesYeuxVisage) {
        this.pochesYeuxVisage = pochesYeuxVisage;
    }

    public String getCernesVisage() {
        return cernesVisage;
    }

    public void setCernesVisage(String cernesVisage) {
        this.cernesVisage = cernesVisage;
    }

    public String getEthnie() {
        return ethnie;
    }

    public void setEthnie(String ethnie) {
        this.ethnie = ethnie;
    }

    public Date getRdvDate() {
        return rdvDate;
    }

    public void setRdvDate(Date rdvDate) {
        this.rdvDate = rdvDate;
    }

    public String getRdvHeure() {
        return rdvHeure;
    }

    public void setRdvHeure(String rdvHeure) {
        this.rdvHeure = rdvHeure;
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
}