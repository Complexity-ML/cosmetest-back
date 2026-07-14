package com.example.cosmetest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "preetudevol")
public class Preetudevol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PREETUDEVOL")
    private Long idPreetudevol;

    @Column(name = "ID_ETUDE", nullable = false)
    private Integer idEtude;

    @Column(name = "ID_GROUPE", nullable = false)
    private Integer idGroupe;

    @Column(name = "ID_VOLONTAIRE", nullable = false)
    private Integer idVolontaire;

    public Preetudevol() {
    }

    public Preetudevol(Integer idEtude, Integer idGroupe, Integer idVolontaire) {
        this.idEtude = idEtude;
        this.idGroupe = idGroupe;
        this.idVolontaire = idVolontaire;
    }

    public Long getIdPreetudevol() { return idPreetudevol; }
    public void setIdPreetudevol(Long idPreetudevol) { this.idPreetudevol = idPreetudevol; }
    public Integer getIdEtude() { return idEtude; }
    public void setIdEtude(Integer idEtude) { this.idEtude = idEtude; }
    public Integer getIdGroupe() { return idGroupe; }
    public void setIdGroupe(Integer idGroupe) { this.idGroupe = idGroupe; }
    public Integer getIdVolontaire() { return idVolontaire; }
    public void setIdVolontaire(Integer idVolontaire) { this.idVolontaire = idVolontaire; }
}
