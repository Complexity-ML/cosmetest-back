package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.PaiementEtudeSummaryDTO;

import java.util.List;

/**
 * Service fournissant des résumés agrégés des paiements.
 */
public interface PaiementStatsService {

    /**
     * Retourne le résumé des paiements pour chaque étude.
     */
    List<PaiementEtudeSummaryDTO> getAllEtudeSummaries();

    /**
     * Retourne le résumé des paiements pour une étude.
     * @param idEtude identifiant de l'étude
     * @return résumé ou null si l'étude n'a pas de paiements
     */
    PaiementEtudeSummaryDTO getSummaryForEtude(int idEtude);
}
