package com.example.cosmetest.business.dto;

/** Données minimales exposées dans le panneau de notifications. */
public record VolontaireNotificationDTO(
        Integer id,
        String nom,
        String prenom,
        String dateInclusion) {
}
