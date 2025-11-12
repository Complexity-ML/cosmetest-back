package com.example.cosmetest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EtudeVolontaireId implements Serializable {

    @Column(name = "ID_ETUDE")
    private Integer idEtude;

    @Column(name = "ID_GROUPE")
    private Integer idGroupe;

    @Column(name = "ID_VOLONTAIRE")
    private Integer idVolontaire;

    @Column(name = "IV")
    private Integer iv;

    @Column(name = "NUMSUJET")
    private Integer numsujet;

    @Column(name = "PAYE")
    private Integer paye;

    @Column(name = "STATUT")
    private String statut;

    public EtudeVolontaireId() {
    }

    public EtudeVolontaireId(Integer idEtude, Integer idGroupe, Integer idVolontaire,
                              Integer iv, Integer numsujet, Integer paye, String statut) {
        this.idEtude = idEtude;
        this.idGroupe = idGroupe;
        this.idVolontaire = idVolontaire;
        this.iv = iv;
        this.numsujet = numsujet;
        this.paye = paye;
        this.statut = statut;
    }

    public Integer getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(Integer idEtude) {
        this.idEtude = idEtude;
    }

    public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    public Integer getIdVolontaire() {
        return idVolontaire;
    }

    public void setIdVolontaire(Integer idVolontaire) {
        this.idVolontaire = idVolontaire;
    }

    public Integer getIv() {
        return iv;
    }

    public void setIv(Integer iv) {
        this.iv = iv;
    }

    public Integer getNumsujet() {
        return numsujet;
    }

    public void setNumsujet(Integer numsujet) {
        this.numsujet = numsujet;
    }

    public Integer getPaye() {
        return paye;
    }

    public void setPaye(Integer paye) {
        this.paye = paye;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EtudeVolontaireId that)) {
            return false;
        }
        return Objects.equals(idEtude, that.idEtude)
            && Objects.equals(idGroupe, that.idGroupe)
            && Objects.equals(idVolontaire, that.idVolontaire)
            && Objects.equals(iv, that.iv)
            && Objects.equals(numsujet, that.numsujet)
            && Objects.equals(paye, that.paye)
            && Objects.equals(statut, that.statut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtude, idGroupe, idVolontaire, iv, numsujet, paye, statut);
    }
}
