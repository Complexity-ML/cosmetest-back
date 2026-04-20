package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.domain.model.RdvId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour la couche business (BLL) des rendez-vous
 * Définit les opérations métier disponibles sur les rendez-vous
 */
public interface RdvService {

    /**
     * Récupère tous les rendez-vous
     *
     * @return Liste de tous les rendez-vous
     */
    List<RdvDTO> getAllRdvs();

    /**
     * Récupère les rendez-vous avec pagination
     *
     * @param pageable Informations de pagination
     * @return Page de rendez-vous
     */
    Page<RdvDTO> getAllRdvsPaginated(Pageable pageable);

    /**
     * Recherche un rendez-vous par son identifiant
     *
     * @param id Identifiant du rendez-vous
     * @return Le rendez-vous trouvé ou vide si non trouvé
     */
    Optional<RdvDTO> getRdvById(RdvId id);

    /**
     * Recherche les rendez-vous d'un volontaire
     *
     * @param idVolontaire Identifiant du volontaire
     * @return Liste des rendez-vous du volontaire
     */
    List<RdvDTO> getRdvsByVolontaire(Integer idVolontaire);

    /**
     * Recherche les rendez-vous à une date spécifique
     *
     * @param date Date de recherche
     * @return Liste des rendez-vous à cette date
     */
    List<RdvDTO> getRdvsByDate(Date date);

    /**
     * Recherche les rendez-vous d'un volontaire à une date spécifique
     *
     * @param idVolontaire Identifiant du volontaire
     * @param date         Date de recherche
     * @return Liste des rendez-vous correspondants
     */
    List<RdvDTO> getRdvsByVolontaireAndDate(Integer idVolontaire, Date date);

    /**
     * Recherche les rendez-vous d'un volontaire sur une période
     *
     * @param idVolontaire Identifiant du volontaire
     * @param startDate    Date de début
     * @param endDate      Date de fin
     * @return Liste des rendez-vous dans cette période
     */
    List<RdvDTO> getRdvsByVolontaireAndDateRange(Integer idVolontaire, Date startDate, Date endDate);

    /**
     * Recherche les rendez-vous d'un groupe
     *
     * @param idGroupe Identifiant du groupe
     * @return Liste des rendez-vous du groupe
     */
    List<RdvDTO> getRdvsByGroupe(Integer idGroupe);

    /**
     * Recherche les rendez-vous selon leur état
     *
     * @param etat État recherché
     * @return Liste des rendez-vous dans cet état
     */
    List<RdvDTO> getRdvsByEtat(String etat);

    /**
     * Enregistre un nouveau rendez-vous ou met à jour un existant
     *
     * @param rdvDTO DTO du rendez-vous à sauvegarder
     * @return DTO du rendez-vous sauvegardé
     */
    RdvDTO saveRdv(RdvDTO rdvDTO);

    /**
     * Supprime un rendez-vous
     *
     * @param id Identifiant du rendez-vous à supprimer
     */
    void deleteRdv(RdvId id);

    /**
     * Recherche des rendez-vous par mots-clés dans les commentaires
     *
     * @param keyword Mot-clé à rechercher
     * @return Liste des rendez-vous correspondants
     */
    List<RdvDTO> searchRdvsByCommentaires(String keyword);

    /**
     * Met à jour l'état d'un rendez-vous
     *
     * @param id         Identifiant du rendez-vous
     * @param nouvelEtat Nouvel état
     */
    void updateRdvEtat(RdvId id, String nouvelEtat);

    int countRdvForToday();

    int countCompletedRdvToday();

    List<RdvDTO> getUpcomingRdvs(int limit);

    List<RdvDTO> getRecentRdvs(int limit);

    /**
     * Récupère tous les rendez-vous pour une étude spécifique
     *
     * @param idEtude L'identifiant de l'étude
     * @return La liste des RDVs associés à l'étude
     */
    List<RdvDTO> getRdvsByIdEtude(Integer idEtude);

    /**
     * Récupère les rendez-vous d'une étude avec leur référence
     *
     * @param idEtude L'identifiant de l'étude
     * @return La liste des RDVs avec la référence de l'étude
     */
    List<RdvDTO> getRdvsByIdEtudeWithRef(Integer idEtude);

    /**
     * Récupère tous les rendez-vous pour un volontaire spécifique
     * 
     * @param idVolontaire L'ID du volontaire
     * @return Liste des rendez-vous du volontaire
     */
    List<RdvDTO> getRdvsByIdVolontaire(Integer idVolontaire);

    boolean hasVolontaireRdvForEtude(Integer idVolontaire, int idEtude);

    RdvDTO createRdv(RdvDTO rdvDTO);

    /**
     * Crée plusieurs rendez-vous en lot (batch)
     * Optimisé pour les créations en masse avec gestion transactionnelle
     *
     * @param rdvDTOs Liste des DTO des rendez-vous à créer
     * @return Liste des DTO des rendez-vous créés
     */
    List<RdvDTO> createRdvsBatch(List<RdvDTO> rdvDTOs);

    void updateRdv(RdvDTO rdvDTO);

}
