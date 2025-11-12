package com.example.cosmetest.domain.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.sql.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "rdv", indexes = {
		@Index(name = "idx_id_volontaire", columnList = "ID_VOLONTAIRE"),
		@Index(name = "idx_date", columnList = "DATE"),
		@Index(name = "idx_id_volontaire_date", columnList = "ID_VOLONTAIRE, DATE")
})
public class Rdv {

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idEtude", column = @Column(name = "ID_ETUDE")),
			@AttributeOverride(name = "idRdv", column = @Column(name = "ID_RDV")) })
	private RdvId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idEtude")
	@JoinColumn(name = "ID_ETUDE", referencedColumnName = "ID_ETUDE", foreignKey = @ForeignKey(name = "fk_rdv_etude"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Etude etude;

	@Column(name = "ID_VOLONTAIRE")
	private Integer idVolontaire;

	// Optional link to Volontaire for read/access. Updates go via idVolontaire.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_VOLONTAIRE", referencedColumnName = "ID_VOL", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_rdv_volontaire"))
	private Volontaire volontaire;
	@Column(name = "ID_GROUPE")
	private Integer idGroupe;
	@Column(name = "DATE")
	private Date date;
	@Column(name = "HEURE")
	private String heure;
	@Column(name = "ETAT")
	private String etat;
	@Column(name = "COMMENTAIRES")
	private String commentaires;

	public Rdv() {
	}

	public Rdv(RdvId id) {
		this.id = id;
	}

	public Rdv(RdvId id, Integer idVolontaire, Integer idGroupe, Date date, String heure, String etat,
			String commentaires) {
		this.id = id;
		this.idVolontaire = idVolontaire;
		this.idGroupe = idGroupe;
		this.date = date;
		this.heure = heure;
		this.etat = etat;
		this.commentaires = commentaires;
	}

	public RdvId getId() {
		return this.id;
	}

	public void setId(RdvId id) {
		this.id = id;
	}

	public Integer getIdVolontaire() {
		return this.idVolontaire;
	}

	public void setIdVolontaire(Integer idVolontaire) {
		this.idVolontaire = idVolontaire;
	}

	public Volontaire getVolontaire() {
		return this.volontaire;
	}

	// Convenience: setting the entity also updates the FK id
	public void setVolontaire(Volontaire volontaire) {
		this.volontaire = volontaire;
		this.idVolontaire = (volontaire != null ? volontaire.getIdVol() : null);
	}

	public Integer getIdGroupe() {
		return this.idGroupe;
	}

	public void setIdGroupe(Integer idGroupe) {
		this.idGroupe = idGroupe;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getHeure() {
		return this.heure;
	}

	public void setHeure(String heure) {
		this.heure = heure;
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

	public Etude getEtude() {
		return this.etude;
	}

	public void setEtude(Etude etude) {
		this.etude = etude;
		if (etude != null) {
			if (this.id == null) {
				this.id = new RdvId();
			}
			this.id.setIdEtude(etude.getIdEtude());
		}
	}

}
