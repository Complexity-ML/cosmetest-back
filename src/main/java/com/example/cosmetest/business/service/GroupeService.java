package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.GroupeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Groupe
 */
public interface GroupeService {

    /**
     * Récupère tous les groupes
     *
     * @return la liste de tous les groupes
     */
    List<GroupeDTO> getAllGroupes();

    /**
     * Récupère un groupe par son identifiant
     *
     * @param id l'identifiant du groupe
     * @return le groupe correspondant, s'il existe
     */
    Optional<GroupeDTO> getGroupeById(Integer id);

    /**
     * Récupère tous les groupes associés à une étude
     *
     * @param idEtude l'identifiant de l'étude
     * @return la liste des groupes associés
     */
    List<GroupeDTO> getGroupesByIdEtude(int idEtude);

    /**
     * Crée un nouveau groupe
     *
     * @param groupeDTO les données du groupe à créer
     * @return le groupe créé avec son identifiant généré
     */
    GroupeDTO createGroupe(GroupeDTO groupeDTO);

    /**
     * Met à jour un groupe existant
     *
     * @param id l'identifiant du groupe à mettre à jour
     * @param groupeDTO les nouvelles données du groupe
     * @return le groupe mis à jour
     */
    Optional<GroupeDTO> updateGroupe(Integer id, GroupeDTO groupeDTO);

    /**
     * Supprime un groupe par son identifiant
     *
     * @param id l'identifiant du groupe à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteGroupe(Integer id);

    /**
     * Vérifie si un groupe existe par son identifiant
     *
     * @param id l'identifiant à vérifier
     * @return true si le groupe existe, false sinon
     */
    boolean existsById(Integer id);

    /**
     * Récupère les groupes filtrés par critères d'âge
     *
     * @param ageMin l'âge minimum (optionnel)
     * @param ageMax l'âge maximum (optionnel)
     * @return la liste des groupes correspondant aux critères
     */
    List<GroupeDTO> getGroupesByAgeRange(Integer ageMin, Integer ageMax);

    /**
     * Récupère les groupes par ethnie
     *
     * @param ethnie l'ethnie recherchée
     * @return la liste des groupes correspondants
     */
    List<GroupeDTO> getGroupesByEthnie(String ethnie);
}