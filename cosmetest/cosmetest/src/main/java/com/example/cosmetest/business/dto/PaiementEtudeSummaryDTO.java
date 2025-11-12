package com.example.cosmetest.business.dto;

/**
 * Représente un résumé agrégé des paiements pour une étude.
 */
public class PaiementEtudeSummaryDTO {

    private Integer idEtude;
    private long total;
    private long payes;
    private long nonPayes;
    private long enAttente;
    private long annules;
    private long montantTotal;
    private long montantPaye;
    private long montantAnnules;
    private long montantRestant;

    public Integer getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(Integer idEtude) {
        this.idEtude = idEtude;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPayes() {
        return payes;
    }

    public void setPayes(long payes) {
        this.payes = payes;
    }

    public long getNonPayes() {
        return nonPayes;
    }

    public void setNonPayes(long nonPayes) {
        this.nonPayes = nonPayes;
    }

    public long getEnAttente() {
        return enAttente;
    }

    public void setEnAttente(long enAttente) {
        this.enAttente = enAttente;
    }

    public long getAnnules() {
        return annules;
    }

    public void setAnnules(long annules) {
        this.annules = annules;
    }

    public long getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(long montantTotal) {
        this.montantTotal = montantTotal;
    }

    public long getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(long montantPaye) {
        this.montantPaye = montantPaye;
    }

    public long getMontantAnnules() {
        return montantAnnules;
    }

    public void setMontantAnnules(long montantAnnules) {
        this.montantAnnules = montantAnnules;
    }

    public long getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(long montantRestant) {
        this.montantRestant = montantRestant;
    }
}
