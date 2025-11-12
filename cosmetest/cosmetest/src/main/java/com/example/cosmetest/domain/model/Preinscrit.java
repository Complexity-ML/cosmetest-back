package com.example.cosmetest.domain.model;
// Generated 15 d�c. 2024, 15:56:41 by Hibernate Tools 6.5.1.Final

import java.sql.Date;

import jakarta.persistence.*;

@Entity // Indique que cette classe est une entité JPA
@Table(name = "preinscrit") // Permet de spécifier le nom de la table
public class Preinscrit implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PREINSCRIT")
	private Integer idPreinscrit;
	private String datePreInscription;
	private String titre;
	private String nom;
	private String prenom;
	private String adresse;
	private String codePostal;
	private String ville;
	private String telDomicile;
	private String telPortable;
	private String email;
	private String sexe;
	private String dateNaissance;
	private String phototype;
	private String peauSensible;
	@Column(name = "map_yeux")
	private String mapyeux;
	@Column(name = "map_levres")
	private String maplevres;
	@Column(name = "map_sourcils")
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
	private String etat;
	private String commentaires;

	public Preinscrit() {
	}

	public Preinscrit(String datePreInscription, String titre, String nom, String prenom, String adresse,
			String codePostal, String ville, String telDomicile, String telPortable, String email, String sexe,
			String dateNaissance, String phototype, String peauSensible, String maplevres, String mapsourcils,
			String peauVisage, String rideVisage, String tachePigmentaireVisage, String boutonsVisage,
			String comedonsVisage, String poresDilatesVisage, String pochesYeuxVisage, String cernesVisage) {
		this.datePreInscription = datePreInscription;
		this.titre = titre;
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.codePostal = codePostal;
		this.ville = ville;
		this.telDomicile = telDomicile;
		this.telPortable = telPortable;
		this.email = email;
		this.sexe = sexe;
		this.dateNaissance = dateNaissance;
		this.phototype = phototype;
		this.peauSensible = peauSensible;
		this.maplevres = maplevres;
		this.mapsourcils = mapsourcils;
		this.peauVisage = peauVisage;
		this.rideVisage = rideVisage;
		this.tachePigmentaireVisage = tachePigmentaireVisage;
		this.boutonsVisage = boutonsVisage;
		this.comedonsVisage = comedonsVisage;
		this.poresDilatesVisage = poresDilatesVisage;
		this.pochesYeuxVisage = pochesYeuxVisage;
		this.cernesVisage = cernesVisage;
	}

	public Preinscrit(String datePreInscription, String titre, String nom, String prenom, String adresse,
			String codePostal, String ville, String telDomicile, String telPortable, String email, String sexe,
			String dateNaissance, String phototype, String peauSensible, String mapyeux, String maplevres,
			String mapsourcils, String peauVisage, String rideVisage, String tachePigmentaireVisage,
			String boutonsVisage, String comedonsVisage, String poresDilatesVisage, String pochesYeuxVisage,
			String cernesVisage, String ethnie, Date rdvDate, String rdvHeure, String etat, String commentaires) {
		this.datePreInscription = datePreInscription;
		this.titre = titre;
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.codePostal = codePostal;
		this.ville = ville;
		this.telDomicile = telDomicile;
		this.telPortable = telPortable;
		this.email = email;
		this.sexe = sexe;
		this.dateNaissance = dateNaissance;
		this.phototype = phototype;
		this.peauSensible = peauSensible;
		this.mapyeux = mapyeux;
		this.maplevres = maplevres;
		this.mapsourcils = mapsourcils;
		this.peauVisage = peauVisage;
		this.rideVisage = rideVisage;
		this.tachePigmentaireVisage = tachePigmentaireVisage;
		this.boutonsVisage = boutonsVisage;
		this.comedonsVisage = comedonsVisage;
		this.poresDilatesVisage = poresDilatesVisage;
		this.pochesYeuxVisage = pochesYeuxVisage;
		this.cernesVisage = cernesVisage;
		this.ethnie = ethnie;
		this.rdvDate = rdvDate;
		this.rdvHeure = rdvHeure;
		this.etat = etat;
		this.commentaires = commentaires;
	}

	public Integer getIdPreinscrit() {
		return this.idPreinscrit;
	}

	public void setIdPreinscrit(Integer idPreinscrit) {
		this.idPreinscrit = idPreinscrit;
	}

	public String getDatePreInscription() {
		return this.datePreInscription;
	}

	public void setDatePreInscription(String datePreInscription) {
		this.datePreInscription = datePreInscription;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getAdresse() {
		return this.adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getCodePostal() {
		return this.codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return this.ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getTelDomicile() {
		return this.telDomicile;
	}

	public void setTelDomicile(String telDomicile) {
		this.telDomicile = telDomicile;
	}

	public String getTelPortable() {
		return this.telPortable;
	}

	public void setTelPortable(String telPortable) {
		this.telPortable = telPortable;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSexe() {
		return this.sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getDateNaissance() {
		return this.dateNaissance;
	}

	public void setDateNaissance(String dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getPhototype() {
		return this.phototype;
	}

	public void setPhototype(String phototype) {
		this.phototype = phototype;
	}

	public String getPeauSensible() {
		return this.peauSensible;
	}

	public void setPeauSensible(String peauSensible) {
		this.peauSensible = peauSensible;
	}

	public String getMapyeux() {
		return this.mapyeux;
	}

	public void setMapyeux(String mapyeux) {
		this.mapyeux = mapyeux;
	}

	public String getMaplevres() {
		return this.maplevres;
	}

	public void setMaplevres(String maplevres) {
		this.maplevres = maplevres;
	}

	public String getMapsourcils() {
		return this.mapsourcils;
	}

	public void setMapsourcils(String mapsourcils) {
		this.mapsourcils = mapsourcils;
	}

	public String getPeauVisage() {
		return this.peauVisage;
	}

	public void setPeauVisage(String peauVisage) {
		this.peauVisage = peauVisage;
	}

	public String getRideVisage() {
		return this.rideVisage;
	}

	public void setRideVisage(String rideVisage) {
		this.rideVisage = rideVisage;
	}

	public String getTachePigmentaireVisage() {
		return this.tachePigmentaireVisage;
	}

	public void setTachePigmentaireVisage(String tachePigmentaireVisage) {
		this.tachePigmentaireVisage = tachePigmentaireVisage;
	}

	public String getBoutonsVisage() {
		return this.boutonsVisage;
	}

	public void setBoutonsVisage(String boutonsVisage) {
		this.boutonsVisage = boutonsVisage;
	}

	public String getComedonsVisage() {
		return this.comedonsVisage;
	}

	public void setComedonsVisage(String comedonsVisage) {
		this.comedonsVisage = comedonsVisage;
	}

	public String getPoresDilatesVisage() {
		return this.poresDilatesVisage;
	}

	public void setPoresDilatesVisage(String poresDilatesVisage) {
		this.poresDilatesVisage = poresDilatesVisage;
	}

	public String getPochesYeuxVisage() {
		return this.pochesYeuxVisage;
	}

	public void setPochesYeuxVisage(String pochesYeuxVisage) {
		this.pochesYeuxVisage = pochesYeuxVisage;
	}

	public String getCernesVisage() {
		return this.cernesVisage;
	}

	public void setCernesVisage(String cernesVisage) {
		this.cernesVisage = cernesVisage;
	}

	public String getEthnie() {
		return this.ethnie;
	}

	public void setEthnie(String ethnie) {
		this.ethnie = ethnie;
	}

	public Date getRdvDate() {
		return this.rdvDate;
	}

	public void setRdvDate(Date rdvDate) {
		this.rdvDate = rdvDate;
	}

	public String getRdvHeure() {
		return this.rdvHeure;
	}

	public void setRdvHeure(String rdvHeure) {
		this.rdvHeure = rdvHeure;
	}

	public String getEtat() {
		return this.etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getCommentaires() {
		return this.commentaires;
	}

	public void setCommentaires(String commentaires) {
		this.commentaires = commentaires;
	}

}
