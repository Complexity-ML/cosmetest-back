package com.example.cosmetest.presentation.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Classe de requête pour la mise à jour de plusieurs produits
 */
public class ProduitsUpdateRequest {

    @NotNull(message = "La liste des produits ne peut pas être null")
    @NotEmpty(message = "La liste des produits ne peut pas être vide")
    private Map<String, String> produits;

    // Constructeurs
    public ProduitsUpdateRequest() {
    }

    public ProduitsUpdateRequest(Map<String, String> produits) {
        this.produits = produits;
    }

    // Getters et Setters
    public Map<String, String> getProduits() {
        return produits;
    }

    public void setProduits(Map<String, String> produits) {
        this.produits = produits;
    }
}