package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.domain.model.EtudeVolontaireId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour la couche business des associations étude-volontaire
 * Version exactement compatible avec votre interface existante
 * 
 * UTILISEZ CETTE VERSION pour remplacer votre interface actuelle
 * Elle correspond exactement aux méthodes implémentées dans le service
 */
public interface EtudeVolontaireService {

    /**
     * Récupère toutes les associations étude-volontaire
     * 
     * @return Liste de toutes les associations
     */
    List<EtudeVolontaireDTO> getAllEtudeVolontaires();

    /**
     * Récupère les associations avec pagination
     * 
     * @param pageable Informations de pagination
     * @return Page d'associations
     */
    Page<EtudeVolontaireDTO> getAllEtudeVolontairesPaginated(Pageable pageable);

    /**
     * Recherche une association par son identifiant composite
     * 
     * @param id Identifiant composite
     * @return L'association trouvée ou vide si non trouvée
     */
    Optional<EtudeVolontaireDTO> getEtudeVolontaireById(EtudeVolontaireId id);

    /**
     * Recherche les associations par identifiant d'étude
     * 
     * @param idEtude Identifiant de l'étude
     * @return Liste des associations
     */
    List<EtudeVolontaireDTO> getEtudeVolontairesByEtude(int idEtude);

    /**
     * Recherche les associations par identifiant de volontaire
     * 
     * @param idVolontaire Identifiant du volontaire
     * @return Liste des associations
     */
    List<EtudeVolontaireDTO> getEtudeVolontairesByVolontaire(int idVolontaire);

    /**
     * Recherche les associations par identifiant de groupe
     * 
     * @param idGroupe Identifiant du groupe
     * @return Liste des associations
     */
    List<EtudeVolontaireDTO> getEtudeVolontairesByGroupe(int idGroupe);

    /**
     * Recherche les associations par étude et volontaire
     * 
     * @param idEtude      Identifiant de l'étude
     * @param idVolontaire Identifiant du volontaire
     * @return Liste des associations
     */
    List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndVolontaire(int idEtude, int idVolontaire);

    /**
     * Recherche les associations par étude et groupe
     * 
     * @param idEtude  Identifiant de l'étude
     * @param idGroupe Identifiant du groupe
     * @return Liste des associations
     */
    List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndGroupe(int idEtude, int idGroupe);

    /**
     * Recherche les associations par statut
     * 
     * @param statut Statut recherché
     * @return Liste des associations
     */
    List<EtudeVolontaireDTO> getEtudeVolontairesByStatut(String statut);

    /**
     * Recherche les associations par indicateur de paiement
     * 
     * @param paye Indicateur de paiement (0 ou 1)
     * @return Liste des associations
     */
    List<EtudeVolontaireDTO> getEtudeVolontairesByPaye(int paye);

    /**
     * Enregistre une nouvelle association ou met à jour une existante
     * 
     * @param etudeVolontaireDTO DTO de l'association à sauvegarder
     * @return DTO de l'association sauvegardée
     */
    EtudeVolontaireDTO saveEtudeVolontaire(EtudeVolontaireDTO etudeVolontaireDTO);

    /**
     * Supprime une association
     * 
     * @param id Identifiant composite de l'association à supprimer
     */
    void deleteEtudeVolontaire(EtudeVolontaireId id);

    /**
     * Vérifie si une association existe avec les identifiants donnés
     * 
     * @param idEtude      Identifiant de l'étude
     * @param idVolontaire Identifiant du volontaire
     * @return true si l'association existe
     */
    boolean existsByEtudeAndVolontaire(int idEtude, int idVolontaire);

    /**
     * Compte le nombre de volontaires par étude
     * 
     * @param idEtude Identifiant de l'étude
     * @return Nombre de volontaires
     */
    Long countVolontairesByEtude(int idEtude);

    /**
     * Compte le nombre d'études par volontaire
     * 
     * @param idVolontaire Identifiant du volontaire
     * @return Nombre d'études
     */
    Long countEtudesByVolontaire(int idVolontaire);

    /**
     * Met à jour le statut d'une association
     * 
     * @param id            Identifiant composite
     * @param nouveauStatut Nouveau statut
     * @return DTO de l'association mise à jour
     */
    EtudeVolontaireDTO updateStatut(EtudeVolontaireId id, String nouveauStatut);

    /**
     * Met à jour l'indicateur de paiement d'une association
     * 
     * @param id   Identifiant composite
     * @param paye Nouvel indicateur de paiement (0 ou 1)
     * @return DTO de l'association mise à jour
     */
    EtudeVolontaireDTO updatePaye(EtudeVolontaireId id, int paye);

    /**
     * Met à jour l'indemnité volontaire d'une association
     * 
     * @param id Identifiant composite
     * @param IV Nouvelle indemnité de volontariat
     * @return DTO de l'association mise à jour
     */
    EtudeVolontaireDTO updateIV(EtudeVolontaireId id, int IV);

    /**
     * Met à jour l'indicateur de paiement et l'indemnité d'une association
     * 
     * @param id   Identifiant composite
     * @param paye Nouvel indicateur de paiement (0 ou 1)
     * @param IV   Nouvelle indemnité de volontariat
     * @return DTO de l'association mise à jour
     */
    EtudeVolontaireDTO updatePayeAndIV(EtudeVolontaireId id, int paye, int IV);

    /**
     * Récupère l'indemnité volontaire d'une association
     * 
     * @param id Identifiant composite
     * @return Valeur de l'indemnité
     */
    int getIVById(EtudeVolontaireId id);

    /**
     * Met à jour le numéro de sujet d'une association
     * CORRECTION: Cette méthode était incomplète dans votre version originale
     * 
     * @param id              Identifiant composite
     * @param nouveauNumSujet Nouveau numéro de sujet
     * @return DTO de l'association mise à jour
     */
    EtudeVolontaireDTO updateNumSujet(EtudeVolontaireId id, int nouveauNumSujet);

    /**
     * Mettre à jour l'ID du volontaire (peut être null pour désassigner)
     */
    EtudeVolontaireDTO updateVolontaire(EtudeVolontaireId id, Integer nouveauVolontaireId);
}