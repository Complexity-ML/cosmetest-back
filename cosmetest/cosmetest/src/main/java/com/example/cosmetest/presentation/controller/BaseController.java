package com.example.cosmetest.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Récupère le nom de l'utilisateur actuellement authentifié.
     *
     * @return Nom d'utilisateur si authentifié, "Anonyme" sinon
     */
    protected String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return "Anonyme";
    }

    /**
     * Méthode pour enregistrer les actions utilisateur.
     *
     * @param username Nom d'utilisateur (ou "Anonyme" si non connecté)
     * @param action   Description de l'action effectuée
     */
    protected void logUserAction(String username, String action) {
        logger.info("Utilisateur : {}, Action : {}", username, action);
    }
}