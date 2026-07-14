package com.example.cosmetest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Date;

@Entity
@Table(name = "rdv",
        uniqueConstraints = @UniqueConstraint(name = "uk_rdv_etude_numero", columnNames = {"ID_ETUDE", "ID_RDV"}),
        indexes = {
                @Index(name = "idx_id_volontaire", columnList = "ID_VOLONTAIRE"),
                @Index(name = "idx_date", columnList = "DATE"),
                @Index(name = "idx_id_volontaire_date", columnList = "ID_VOLONTAIRE, DATE")
        })
public class Rdv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RDV_PK")
    private Long rdvPk;

    @Column(name = "ID_ETUDE", nullable = false)
    private Integer idEtude;

    @Column(name = "ID_RDV", nullable = false)
    private Integer idRdv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ETUDE", referencedColumnName = "ID_ETUDE", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rdv_etude"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Etude etude;

    @Column(name = "ID_VOLONTAIRE")
    private Integer idVolontaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOLONTAIRE", referencedColumnName = "ID_VOL", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_rdv_volontaire"))
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
    @Column(name = "DUREE")
    private Integer duree;

    public Rdv() {
    }

    public Long getRdvPk() {
        return rdvPk;
    }

    public void setRdvPk(Long rdvPk) {
        this.rdvPk = rdvPk;
    }

    public Long getId() {
        return rdvPk;
    }

    public void setId(Long id) {
        this.rdvPk = id;
    }

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

    public Integer getNumeroRdv() {
        return idRdv;
    }

    public void setNumeroRdv(Integer numeroRdv) {
        this.idRdv = numeroRdv;
    }

    public Integer getIdVolontaire() {
        return idVolontaire;
    }

    public void setIdVolontaire(Integer idVolontaire) {
        this.idVolontaire = idVolontaire;
    }

    public Volontaire getVolontaire() {
        return volontaire;
    }

    public void setVolontaire(Volontaire volontaire) {
        this.volontaire = volontaire;
        this.idVolontaire = volontaire != null ? volontaire.getIdVol() : null;
    }

    public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    public Date getDate() {
        return date;
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

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Etude getEtude() {
        return etude;
    }

    public void setEtude(Etude etude) {
        this.etude = etude;
        if (etude != null) {
            this.idEtude = etude.getIdEtude();
        }
    }
}
