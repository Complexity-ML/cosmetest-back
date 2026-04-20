package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) pour l'entité Volontaire
 * Contient uniquement les champs essentiels pour les opérations courantes
 */
public class VolontaireDTO {

    private Integer idVol;

    private String titreVol;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nomVol;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenomVol;

    private String adresseVol;

    @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir 5 chiffres")
    private String cpVol;

    private String villeVol;

    private String telDomicileVol;

    private String telPortableVol;

    @Email(message = "L'email doit être valide")
    private String emailVol;

    @Pattern(regexp = "^(F|M|(?i)Feminin|(?i)Masculin|Autre)$", message = "Le sexe doit être F, M, Feminin, Masculin ou Autre")
    private String sexe;

    private LocalDate dateNaissance;

    private Boolean archive;

    private String commentairesVol;

    private String ethnie;

    private String sousEthnie;

    private String phototype;

    private String typePeauVisage;

    @PositiveOrZero(message = "Le poids doit être positif ou zéro")
    private Integer poids;

    @PositiveOrZero(message = "La taille doit être positive ou zéro")
    private Integer taille;

    private LocalDate dateI;

    private String santeCompatible;


    // Constructeurs
    public VolontaireDTO() {
    }

    public VolontaireDTO(Integer idVol, String titreVol, String nomVol, String prenomVol,
                         String emailVol, String sexe, LocalDate dateNaissance, Boolean archive,
                         String typePeauVisage) {
        this.idVol = idVol;
        this.titreVol = titreVol;
        this.nomVol = nomVol;
        this.prenomVol = prenomVol;
        this.emailVol = emailVol;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.archive = archive;
        this.typePeauVisage = typePeauVisage;
    }

    // Getters et Setters
    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    public String getTitreVol() {
        return titreVol;
    }

    public void setTitreVol(String titreVol) {
        this.titreVol = titreVol;
    }

    public String getNomVol() {
        return nomVol;
    }

    public void setNomVol(String nomVol) {
        this.nomVol = nomVol;
    }

    public String getPrenomVol() {
        return prenomVol;
    }

    public void setPrenomVol(String prenomVol) {
        this.prenomVol = prenomVol;
    }

    public String getAdresseVol() {
        return adresseVol;
    }

    public void setAdresseVol(String adresseVol) {
        this.adresseVol = adresseVol;
    }

    public String getCpVol() {
        return cpVol;
    }

    public void setCpVol(String cpVol) {
        this.cpVol = cpVol;
    }

    public String getVilleVol() {
        return villeVol;
    }

    public void setVilleVol(String villeVol) {
        this.villeVol = villeVol;
    }

    public String getTelDomicileVol() {
        return telDomicileVol;
    }

    public void setTelDomicileVol(String telDomicileVol) {
        this.telDomicileVol = telDomicileVol;
    }

    public String getTelPortableVol() {
        return telPortableVol;
    }

    public void setTelPortableVol(String telPortableVol) {
        this.telPortableVol = telPortableVol;
    }

    public String getEmailVol() {
        return emailVol;
    }

    public void setEmailVol(String emailVol) {
        this.emailVol = emailVol;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public String getCommentairesVol() {
        return commentairesVol;
    }

    public void setCommentairesVol(String commentairesVol) {
        this.commentairesVol = commentairesVol;
    }

    public String getEthnie() {
        return ethnie;
    }

    public void setEthnie(String ethnie) {
        this.ethnie = ethnie;
    }

    public String getSousEthnie() {
        return sousEthnie;
    }

    public void setSousEthnie(String sousEthnie) {
        this.sousEthnie = sousEthnie;
    }

    public String getPhototype() {
        return phototype;
    }

    public void setPhototype(String phototype) {
        this.phototype = phototype;
    }

    public String getTypePeauVisage() {
        return typePeauVisage;
    }

    public void setTypePeauVisage(String typePeauVisage) {
        this.typePeauVisage = typePeauVisage;
    }

    public Integer getPoids() {
        return poids;
    }

    public void setPoids(Integer poids) {
        this.poids = poids;
    }

    public Integer getTaille() {
        return taille;
    }

    public void setTaille(Integer taille) {
        this.taille = taille;
    }

    // Modifiez la méthode getter pour gérer le cas null
    public LocalDate getDateI() {
        // Gérer le cas où dateI est null
        if (this.dateI == null) {
            return null;
        }
        return this.dateI;
    }

    // Si votre attribut est String mais que vous essayez d'affecter un Date
    public void setDateI(LocalDate date) {
        if (date == null) {
            this.dateI = null;
        } else {
            this.dateI = LocalDate.parse(date.toString());
        }
    }

    public String getSanteCompatible() {
        return santeCompatible;
    }

    public void setSanteCompatible(String santeCompatible) {
        this.santeCompatible = santeCompatible;
    }

    /**
     * Récupère l'ID du volontaire de manière sécurisée
     * Cette méthode s'assure que l'ID est valide avant de le retourner
     *
     * @return Integer représentant l'ID du volontaire, ou null si l'ID n'est pas valide
     */
    public Integer getVolontaireId() {
        // Vérifie que idVol n'est pas null et est supérieur à 0
        if (idVol == null || idVol <= 0) {
            return null;
        }

        return idVol;
    }

    @Override
    public String toString() {
        return "VolontaireDTO{" +
                "idVol=" + idVol +
                ", nomVol='" + nomVol + '\'' +
                ", prenomVol='" + prenomVol + '\'' +
                ", emailVol='" + emailVol + '\'' +
                '}';
    }
}
