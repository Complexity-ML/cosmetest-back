package com.example.cosmetest.presentation.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Classe de requête pour l'authentification
 */
public class LoginRequest {

    @NotBlank(message = "Le login ne peut pas être vide")
    private String login;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String motDePasse;

    // Constructeurs
    public LoginRequest() {
    }

    public LoginRequest(String login, String motDePasse) {
        this.login = login;
        this.motDePasse = motDePasse;
    }

    // Getters et Setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}