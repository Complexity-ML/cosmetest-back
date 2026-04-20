package com.example.cosmetest.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.sql.Date;

/**
 * Classe de requête pour la planification de rendez-vous
 */
public class PlanifierRendezVousRequest {

    @NotNull(message = "La date de rendez-vous ne peut pas être nulle")
    private Date rdvDate;

    @NotNull(message = "L'heure de rendez-vous ne peut pas être nulle")
    @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Le format de l'heure doit être HH:MM")
    private String rdvHeure;

    // Constructeurs
    public PlanifierRendezVousRequest() {
    }

    public PlanifierRendezVousRequest(Date rdvDate, String rdvHeure) {
        this.rdvDate = rdvDate;
        this.rdvHeure = rdvHeure;
    }

    // Getters et Setters
    public Date getRdvDate() {
        return rdvDate;
    }

    public void setRdvDate(Date rdvDate) {
        this.rdvDate = rdvDate;
    }

    public String getRdvHeure() {
        return rdvHeure;
    }

    public void setRdvHeure(String rdvHeure) {
        this.rdvHeure = rdvHeure;
    }
}