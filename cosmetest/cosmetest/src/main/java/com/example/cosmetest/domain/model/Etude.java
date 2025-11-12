package com.example.cosmetest.domain.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "etude", indexes = {
		@Index(name = "idx_etude_ref", columnList = "ref")
})
public class Etude implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ETUDE")
	private Integer idEtude;

	@Column(name = "REF", nullable = false)
	private String ref;

	private String type;
	private String titre;

	@Column(columnDefinition = "TEXT")
	private String produits;

	private Date dateDebut;
	private Date dateFin;
	private String washout;
	@Column(columnDefinition = "TEXT")
	private String commentaires;
	@Column(columnDefinition = "TEXT")
	private String examens;

	// Renommer en capaciteVolontaires et changer le type en Integer
	@Column(name = "NB_SUJETS")
	private Integer capaciteVolontaires;

	private int paye;

	@OneToMany(mappedBy = "etude", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Rdv> rdvs = new ArrayList<>();

	@OneToMany(mappedBy = "etude", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EtudeVolontaire> etudeVolontaires = new ArrayList<>();

	public Etude() {
	}

	public Etude(String ref, Date dateDebut, Date dateFin, int paye) {
		this.ref = ref;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.paye = paye;
	}

	public Etude(String ref, String type, String titre, String description, Date dateDebut, Date dateFin,
				 String commentaires, Integer capaciteVolontaires, int paye, Integer montant, String produits) {
		this.ref = ref;
		this.type = type;
		this.titre = titre;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.commentaires = commentaires;
		this.capaciteVolontaires = capaciteVolontaires;
		this.paye = paye;
		this.produits = produits;
	}

	public Integer getIdEtude() {
		return this.idEtude;
	}

	public void setIdEtude(Integer idEtude) {
		this.idEtude = idEtude;
	}

	public String getRef() {
		return this.ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitre() {
		return this.titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Date getDateDebut() {
		return this.dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return this.dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public String getWashout() {
		return this.washout;
	}

	public String getProduits() {
		return produits;
	}

	public void setProduits(String produits) {
		this.produits = produits;
	}

	public void setWashout(String washout) {
		this.washout = washout;
	}

	public String getCommentaires() {
		return this.commentaires;
	}

	public void setCommentaires(String commentaires) {
		this.commentaires = commentaires;
	}

	public String getExamens() {
		return this.examens;
	}

	public void setExamens(String examens) {
		this.examens = examens;
	}

	public Integer getCapaciteVolontaires() {
		return this.capaciteVolontaires;
	}

	public void setCapaciteVolontaires(Integer capaciteVolontaires) {
		this.capaciteVolontaires = capaciteVolontaires;
	}

	// Pour maintenir la compatibilité avec l'ancien code
	public String getNbSujets() {
		return this.capaciteVolontaires != null ? this.capaciteVolontaires.toString() : null;
	}

	public void setNbSujets(String nbSujets) {
		try {
			this.capaciteVolontaires = nbSujets != null ? Integer.parseInt(nbSujets) : null;
		} catch (NumberFormatException e) {
			this.capaciteVolontaires = null;
		}
	}

	public int getPaye() {
		return this.paye;
	}

	public void setPaye(int paye) {
		this.paye = paye;
	}

	public List<Rdv> getRdvs() {
		return rdvs;
	}

	public void setRdvs(List<Rdv> rdvs) {
		this.rdvs = rdvs;
	}

	public List<EtudeVolontaire> getEtudeVolontaires() {
		return etudeVolontaires;
	}

	public void setEtudeVolontaires(List<EtudeVolontaire> etudeVolontaires) {
		this.etudeVolontaires = etudeVolontaires;
	}
}
