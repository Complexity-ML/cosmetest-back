package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.PreetudevolDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Preetudevol
 */
public interface PreetudevolService {

    /**
     * Récupère toutes les pré-études-volontaires
     *
     * @return la liste de toutes les pré-études-volontaires
     */
    List<PreetudevolDTO> getAllPreetudevols();

    /**
     * Récupère une pré-étude-volontaire par son identifiant composite
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @param idVolontaire l'identifiant du volontaire
     * @return la pré-étude-volontaire correspondante, si elle existe
     */
    Optional<PreetudevolDTO> getPreetudevolById(int idEtude, int idGroupe, int idVolontaire);

    /**
     * Récupère toutes les pré-études-volontaires d'une étude spécifique
     *
     * @param idEtude l'identifiant de l'étude
     * @return la liste des pré-études-volontaires
     */
    List<PreetudevolDTO> getPreetudevolsByIdEtude(int idEtude);

    /**
     * Récupère toutes les pré-études-volontaires d'un groupe spécifique
     *
     * @param idGroupe l'identifiant du groupe
     * @return la liste des pré-études-volontaires
     */
    List<PreetudevolDTO> getPreetudevolsByIdGroupe(int idGroupe);

    /**
     * Récupère toutes les pré-études-volontaires d'un volontaire spécifique
     *
     * @param idVolontaire l'identifiant du volontaire
     * @return la liste des pré-études-volontaires
     */
    List<PreetudevolDTO> getPreetudevolsByIdVolontaire(int idVolontaire);

    /**
     * Récupère toutes les pré-études-volontaires d'une étude et d'un groupe spécifiques
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @return la liste des pré-études-volontaires
     */
    List<PreetudevolDTO> getPreetudevolsByEtudeAndGroupe(int idEtude, int idGroupe);

    /**
     * Récupère toutes les pré-études-volontaires d'une étude et d'un volontaire spécifiques
     *
     * @param idEtude l'identifiant de l'étude
     * @param idVolontaire l'identifiant du volontaire
     * @return la liste des pré-études-volontaires
     */
    List<PreetudevolDTO> getPreetudevolsByEtudeAndVolontaire(int idEtude, int idVolontaire);

    /**
     * Crée une nouvelle pré-étude-volontaire
     *
     * @param preetudevolDTO les données de la pré-étude-volontaire à créer
     * @return la pré-étude-volontaire créée
     */
    PreetudevolDTO createPreetudevol(PreetudevolDTO preetudevolDTO);

    /**
     * Met à jour une pré-étude-volontaire existante
     *
     * @param idEtude l'identifiant de l'étude actuelle
     * @param idGroupe l'identifiant du groupe actuel
     * @param idVolontaire l'identifiant du volontaire actuel
     * @param preetudevolDTO les nouvelles données de la pré-étude-volontaire
     * @return la pré-étude-volontaire mise à jour
     */
    Optional<PreetudevolDTO> updatePreetudevol(int idEtude, int idGroupe, int idVolontaire, PreetudevolDTO preetudevolDTO);

    /**
     * Supprime une pré-étude-volontaire par son identifiant composite
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @param idVolontaire l'identifiant du volontaire
     * @return true si la suppression a réussi, false sinon
     */
    boolean deletePreetudevol(int idEtude, int idGroupe, int idVolontaire);

    /**
     * Supprime toutes les pré-études-volontaires d'une étude spécifique
     *
     * @param idEtude l'identifiant de l'étude
     * @return le nombre de pré-études-volontaires supprimées
     */
    int deletePreetudevolsByIdEtude(int idEtude);

    /**
     * Supprime toutes les pré-études-volontaires d'un groupe spécifique
     *
     * @param idGroupe l'identifiant du groupe
     * @return le nombre de pré-études-volontaires supprimées
     */
    int deletePreetudevolsByIdGroupe(int idGroupe);

    /**
     * Supprime toutes les pré-études-volontaires d'un volontaire spécifique
     *
     * @param idVolontaire l'identifiant du volontaire
     * @return le nombre de pré-études-volontaires supprimées
     */
    int deletePreetudevolsByIdVolontaire(int idVolontaire);

    /**
     * Vérifie si une pré-étude-volontaire existe par son identifiant composite
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @param idVolontaire l'identifiant du volontaire
     * @return true si la pré-étude-volontaire existe, false sinon
     */
    boolean existsById(int idEtude, int idGroupe, int idVolontaire);
}