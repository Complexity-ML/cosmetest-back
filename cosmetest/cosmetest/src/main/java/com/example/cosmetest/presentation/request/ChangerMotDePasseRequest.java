package com.example.cosmetest.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Classe de requête pour le changement de mot de passe
 */
public class ChangerMotDePasseRequest {

    @NotBlank(message = "L'ancien mot de passe ne peut pas être vide")
    private String ancienMotDePasse;

    @NotBlank(message = "Le nouveau mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le nouveau mot de passe doit contenir au moins 6 caractères")
    private String nouveauMotDePasse;

    // Constructeurs
    public ChangerMotDePasseRequest() {
    }

    public ChangerMotDePasseRequest(String ancienMotDePasse, String nouveauMotDePasse) {
        this.ancienMotDePasse = ancienMotDePasse;
        this.nouveauMotDePasse = nouveauMotDePasse;
    }

    // Getters et Setters
    public String getAncienMotDePasse() {
        return ancienMotDePasse;
    }

    public void setAncienMotDePasse(String ancienMotDePasse) {
        this.ancienMotDePasse = ancienMotDePasse;
    }

    public String getNouveauMotDePasse() {
        return nouveauMotDePasse;
    }

    public void setNouveauMotDePasse(String nouveauMotDePasse) {
        this.nouveauMotDePasse = nouveauMotDePasse;
    }
}