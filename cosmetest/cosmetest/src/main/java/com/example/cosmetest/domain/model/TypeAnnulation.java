package com.example.cosmetest.domain.model;

/**
 * Énumération pour indiquer qui a effectué l'annulation
 */
public enum TypeAnnulation {
    COSMETEST("Cosmetest"),
    VOLONTAIRE("Volontaire");

    private final String libelle;

    TypeAnnulation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    /**
     * Convertit une chaîne en TypeAnnulation
     * @param value valeur à convertir
     * @return TypeAnnulation correspondant ou null si non trouvé
     */
    public static TypeAnnulation fromString(String value) {
        if (value == null) {
            return null;
        }
        for (TypeAnnulation type : TypeAnnulation.values()) {
            if (type.name().equalsIgnoreCase(value) || type.libelle.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
