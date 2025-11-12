package com.example.cosmetest.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Classe de requête pour la mise à jour d'un produit
 */
public class ProduitUpdateRequest {

    @NotBlank(message = "Le nom du produit ne peut pas être vide")
    private String produit;

    @Pattern(regexp = "^(oui|non|occasionnellement|regulierement|jamais)$",
            message = "La valeur doit être 'oui', 'non', 'occasionnellement', 'regulierement' ou 'jamais'")
    private String valeur;

    // Constructeurs
    public ProduitUpdateRequest() {
    }

    public ProduitUpdateRequest(String produit, String valeur) {
        this.produit = produit;
        this.valeur = valeur;
    }

    // Getters et Setters
    public String getProduit() {
        return produit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
}