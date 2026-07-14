package com.example.cosmetest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "infobancaire")
public class Infobancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INFOBANCAIRE")
    private Long idInfobancaire;

    @Column(name = "BIC", nullable = false)
    private String bic;

    @Column(name = "IBAN", nullable = false)
    private String iban;

    @Column(name = "ID_VOL", nullable = false)
    private Integer idVol;

    public Infobancaire() {
    }

    public Infobancaire(String bic, String iban, Integer idVol) {
        this.bic = bic;
        this.iban = iban;
        this.idVol = idVol;
    }

    public Long getIdInfobancaire() { return idInfobancaire; }
    public void setIdInfobancaire(Long idInfobancaire) { this.idInfobancaire = idInfobancaire; }
    public String getBic() { return bic; }
    public void setBic(String bic) { this.bic = bic; }
    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }
    public Integer getIdVol() { return idVol; }
    public void setIdVol(Integer idVol) { this.idVol = idVol; }
}
