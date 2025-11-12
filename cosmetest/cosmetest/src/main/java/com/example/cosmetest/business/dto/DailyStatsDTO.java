// src/main/java/com/example/cosmetest/business/dto/DailyStatsDTO.java
package com.example.cosmetest.business.dto;

public class DailyStatsDTO {
    private int volontairesAjoutes;
    private int rdvEffectues;
    private int nouvellesPreinscriptions;

    // Constructeurs
    public DailyStatsDTO() {
    }

    public DailyStatsDTO(int volontairesAjoutes, int rdvEffectues, int nouvellesPreinscriptions) {
        this.volontairesAjoutes = volontairesAjoutes;
        this.rdvEffectues = rdvEffectues;
        this.nouvellesPreinscriptions = nouvellesPreinscriptions;
    }

    // Getters et Setters
    public int getVolontairesAjoutes() {
        return volontairesAjoutes;
    }

    public void setVolontairesAjoutes(int volontairesAjoutes) {
        this.volontairesAjoutes = volontairesAjoutes;
    }

    public int getRdvEffectues() {
        return rdvEffectues;
    }

    public void setRdvEffectues(int rdvEffectues) {
        this.rdvEffectues = rdvEffectues;
    }

    public int getNouvellesPreinscriptions() {
        return nouvellesPreinscriptions;
    }

    public void setNouvellesPreinscriptions(int nouvellesPreinscriptions) {
        this.nouvellesPreinscriptions = nouvellesPreinscriptions;
    }
}