package com.example.cosmetest.business.dto;

import java.sql.Date;

/**
 * Data Transfer Object pour les études
 * Utilisé pour transférer les données entre la couche de présentation et la couche business
 */
public class EtudeDTO {

    private Integer idEtude;
    private String ref;
    private String type;
    private String produits;
    private String titre;
    private Date dateDebut;
    private Date dateFin;
    private String washout;
    private String commentaires;
    private String examens;
    private String nbSujets;
    private int paye;
    private int iv;
     // Ajout pour stocker le montant de la rémunération

    // Constructeur par défaut
    public EtudeDTO() {
    }

    // Constructeur avec les champs obligatoires
    public EtudeDTO(String ref, Date dateDebut, Date dateFin, int paye) {
        this.ref = ref;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.paye = paye;
    }

    // Constructeur avec tous les champs
    // Constructeur avec tous les champs, incluant iv
    public EtudeDTO(Integer idEtude, String ref, String type, String titre, Date dateDebut, Date dateFin,
                    String washout, String commentaires, String examens, String nbSujets, int paye, int iv, String produits) {
        this.idEtude = idEtude;
        this.ref = ref;
        this.type = type;
        this.titre = titre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.washout = washout;
        this.commentaires = commentaires;
        this.examens = examens;
        this.nbSujets = nbSujets;
        this.paye = paye;
        this.iv = iv;
        this.produits = produits;
    }

    // Getters et Setters
    public Integer getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(Integer idEtude) {
        this.idEtude = idEtude;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    // Méthode pour obtenir la description (utilise commentaires)
    public String getDescription() {
        return commentaires;
    }

    // Méthode pour définir la description (stocke dans commentaires)
    public void setDescription(String description) {
        this.commentaires = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getWashout() {
        return washout;
    }

    public void setWashout(String washout) {
        this.washout = washout;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public String getExamens() {
        return examens;
    }

    public void setExamens(String examens) {
        this.examens = examens;
    }

    public String getNbSujets() {
        return nbSujets;
    }

    public void setNbSujets(String nbSujets) {
        this.nbSujets = nbSujets;
    }

    // Récupérer la capacité de volontaires comme un entier
    public Integer getCapaciteVolontaires() {
        if (nbSujets == null || nbSujets.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(nbSujets);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Définir la capacité de volontaires
    public void setCapaciteVolontaires(Integer capaciteVolontaires) {
        this.nbSujets = capaciteVolontaires != null ? capaciteVolontaires.toString() : null;
    }

    public int getPaye() {
        return paye;
    }

    public void setPaye(int paye) {
        this.paye = paye;
    }

    // Convertir paye en booléen pour le frontend
    public boolean isPayeBool() {
        return paye == 1;
    }

    // Définir paye à partir d'un booléen
    public void setPayeBool(boolean payeBool) {
        this.paye = payeBool ? 1 : 0;
    }

    // Getter et Setter pour l'Indemnité Volontaire (IV)
    public Integer getIv() {
        return iv;
    }

    public void setIv(int iv) {
        this.iv = iv;
    }

    public String getProduits() {
        return produits;
    }

    public void setProduits(String produits) {
        this.produits = produits;
    }

    // Getter pour le frontend qui attend "id" au lieu de "idEtude"
    public Integer getId() {
        return idEtude;
    }

    // Champ pour le nombre de volontaires inscrits (utilisé par le dashboard)
    private int volontaires = 0;

    public int getVolontaires() {
        return volontaires;
    }

    public void setVolontaires(int volontaires) {
        this.volontaires = volontaires;
    }

    // Getter pour le statut calculé basé sur les dates
    public String getStatus() {
        if (dateDebut == null || dateFin == null) {
            return "À venir";
        }
        Date today = new Date(System.currentTimeMillis());
        if (today.before(dateDebut)) {
            return "À venir";
        } else if (today.after(dateFin)) {
            return "Terminée";
        } else {
            return "En cours";
        }
    }
}