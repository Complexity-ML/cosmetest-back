package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.VolbugDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Volbug
 */
public interface VolbugService {

    /**
     * Récupère tous les bugs de volontaires
     *
     * @return la liste de tous les bugs
     */
    List<VolbugDTO> getAllVolbugs();

    /**
     * Récupère un bug par l'identifiant du volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return le bug correspondant, s'il existe
     */
    Optional<VolbugDTO> getVolbugByIdVol(Integer idVol);

    /**
     * Crée un nouveau bug de volontaire
     *
     * @param volbugDTO les données du bug à créer
     * @return le bug créé
     */
    VolbugDTO createVolbug(VolbugDTO volbugDTO);

    /**
     * Met à jour un bug existant
     *
     * @param idVol l'identifiant du volontaire
     * @param volbugDTO les nouvelles données du bug
     * @return le bug mis à jour
     */
    Optional<VolbugDTO> updateVolbug(Integer idVol, VolbugDTO volbugDTO);

    /**
     * Supprime un bug par l'identifiant du volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVolbug(Integer idVol);

    /**
     * Vérifie si un bug existe pour un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return true si le bug existe, false sinon
     */
    boolean existsByIdVol(Integer idVol);
}