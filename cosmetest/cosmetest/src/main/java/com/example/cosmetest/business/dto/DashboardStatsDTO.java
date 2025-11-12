// src/main/java/com/example/cosmetest/business/dto/DashboardStatsDTO.java
package com.example.cosmetest.business.dto;

public class DashboardStatsDTO {
    private int volontairesActifs;
    private int etudesEnCours;
    private int rdvToday;
    private int preinscrits;

    // Constructeurs
    public DashboardStatsDTO() {
    }

    public DashboardStatsDTO(int volontairesActifs, int etudesEnCours, int rdvToday, int preinscrits) {
        this.volontairesActifs = volontairesActifs;
        this.etudesEnCours = etudesEnCours;
        this.rdvToday = rdvToday;
        this.preinscrits = preinscrits;
    }

    // Getters et Setters
    public int getVolontairesActifs() {
        return volontairesActifs;
    }

    public void setVolontairesActifs(int volontairesActifs) {
        this.volontairesActifs = volontairesActifs;
    }

    public int getEtudesEnCours() {
        return etudesEnCours;
    }

    public void setEtudesEnCours(int etudesEnCours) {
        this.etudesEnCours = etudesEnCours;
    }

    public int getRdvToday() {
        return rdvToday;
    }

    public void setRdvToday(int rdvToday) {
        this.rdvToday = rdvToday;
    }

    public int getPreinscrits() {
        return preinscrits;
    }

    public void setPreinscrits(int preinscrits) {
        this.preinscrits = preinscrits;
    }
}