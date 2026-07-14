package com.example.cosmetest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "etude_volontaire")
public class EtudeVolontaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ETUDE_VOLONTAIRE")
    private Long id;

    @Column(name = "ID_ETUDE", nullable = false)
    private Integer idEtude;
    @Column(name = "ID_GROUPE", nullable = false)
    private Integer idGroupe;
    @Column(name = "ID_VOLONTAIRE", nullable = false)
    private Integer idVolontaire;
    @Column(name = "IV")
    private Integer iv;
    @Column(name = "NUMSUJET")
    private Integer numSujet;
    @Column(name = "PAYE")
    private Integer paye;
    @Column(name = "STATUT")
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ETUDE", referencedColumnName = "ID_ETUDE", insertable = false, updatable = false)
    private Etude etude;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOLONTAIRE", referencedColumnName = "ID_VOL", insertable = false, updatable = false)
    private Volontaire volontaire;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GROUPE", referencedColumnName = "ID_GROUPE", insertable = false, updatable = false)
    private Groupe groupe;

    public EtudeVolontaire() {}
    public EtudeVolontaire(EtudeVolontaireId legacy) {
        this.idEtude = legacy.getIdEtude(); this.idGroupe = legacy.getIdGroupe();
        this.idVolontaire = legacy.getIdVolontaire(); this.iv = legacy.getIv();
        this.numSujet = legacy.getNumsujet(); this.paye = legacy.getPaye(); this.statut = legacy.getStatut();
    }
    public EtudeVolontaire(Etude etude, Groupe groupe, Volontaire volontaire) {
        setEtude(etude); setGroupe(groupe); setVolontaire(volontaire);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getIdEtude() { return idEtude; }
    public void setIdEtude(Integer value) { idEtude = value; }
    public Integer getIdGroupe() { return idGroupe; }
    public void setIdGroupe(Integer value) { idGroupe = value; }
    public Integer getIdVolontaire() { return idVolontaire; }
    public void setIdVolontaire(Integer value) { idVolontaire = value; }
    public Integer getIv() { return iv; }
    public void setIv(Integer value) { iv = value; }
    public Integer getNumSujet() { return numSujet; }
    public void setNumSujet(Integer value) { numSujet = value; }
    public Integer getPaye() { return paye; }
    public void setPaye(Integer value) { paye = value; }
    public String getStatut() { return statut; }
    public void setStatut(String value) { statut = value; }
    public Etude getEtude() { return etude; }
    public void setEtude(Etude value) { etude = value; if (value != null) idEtude = value.getIdEtude(); }
    public Volontaire getVolontaire() { return volontaire; }
    public void setVolontaire(Volontaire value) { volontaire = value; if (value != null) idVolontaire = value.getIdVol(); }
    public Groupe getGroupe() { return groupe; }
    public void setGroupe(Groupe value) { groupe = value; if (value != null) idGroupe = value.getIdGroupe(); }
}
