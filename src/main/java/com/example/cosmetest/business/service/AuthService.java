package com.example.cosmetest.business.service;

/**
 * Interface pour les services d'authentification
 */
public interface AuthService {

    /**
     * Authentifie un utilisateur et génère un jeton JWT
     *
     * @param login    le login de l'utilisateur
     * @param password le mot de passe de l'utilisateur
     * @return le jeton JWT si l'authentification réussit, sinon null
     */
    String authenticate(String login, String password);

    /**
     * Valide un jeton JWT
     *
     * @param token le jeton JWT à valider
     * @return true si le jeton est valide, false sinon
     */
    boolean validateToken(String token);

    /**
     * Extrait le nom d'utilisateur d'un jeton JWT
     *
     * @param token le jeton JWT
     * @return le nom d'utilisateur
     */
    String getUsernameFromToken(String token);

    // Nouvelle méthode pour la déconnexion
    void invalidateToken(String token);

}