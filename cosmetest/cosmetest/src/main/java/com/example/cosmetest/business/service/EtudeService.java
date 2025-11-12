package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.EtudeDTO;
import com.example.cosmetest.domain.model.Etude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface pour la couche business (BLL) des études
 * Définit les opérations métier disponibles sur les études
 */
public interface EtudeService {

    /**
     * Récupère toutes les études
     * 
     * @return Liste de toutes les études
     */
    List<EtudeDTO> getAllEtudes();

    /**
     * Récupère les études avec pagination
     * 
     * @param pageable Informations de pagination
     * @return Page d'études
     */
    Page<EtudeDTO> getAllEtudesPaginated(Pageable pageable);

    /**
     * Recherche une étude par son identifiant
     * 
     * @param id Identifiant de l'étude
     * @return L'étude trouvée ou vide si non trouvée
     */
    Optional<EtudeDTO> getEtudeById(Integer id);

    /**
     * Recherche une étude par sa référence
     * 
     * @param ref Référence de l'étude
     * @return L'étude trouvée ou vide si non trouvée
     */
    Optional<EtudeDTO> getEtudeByRef(String ref);

    /**
     * Recherche les études par type
     * 
     * @param type Type d'étude
     * @return Liste des études
     */
    List<EtudeDTO> getEtudesByType(String type);

    /**
     * Recherche les études dont le titre contient le mot-clé
     * 
     * @param keyword Mot-clé à rechercher
     * @return Liste des études
     */
    List<EtudeDTO> getEtudesByTitre(String keyword);

    /**
     * Recherche les études entre deux dates
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Liste des études
     */
    List<EtudeDTO> getEtudesByDateRange(Date debut, Date fin);

    /**
     * Recherche les études actives à une date donnée
     * 
     * @param date Date à vérifier
     * @return Liste des études
     */
    List<EtudeDTO> getActiveEtudesAtDate(Date date);

    /**
     * Recherche les études par indicateur de paiement
     * 
     * @param paye Indicateur de paiement (1 pour payé, 0 pour non payé)
     * @return Liste des études
     */
    List<EtudeDTO> getEtudesByPaye(int paye);

    /**
     * Recherche en texte intégral dans le titre et les commentaires
     * 
     * @param searchTerm Terme de recherche
     * @return Liste des études
     */
    List<EtudeDTO> searchEtudes(String searchTerm);

    /**
     * Enregistre une nouvelle étude ou met à jour une existante
     * 
     * @param etudeDTO DTO de l'étude à sauvegarder
     * @return DTO de l'étude sauvegardée
     */
    EtudeDTO saveEtude(EtudeDTO etudeDTO);

    /**
     * Supprime une étude
     * 
     * @param id Identifiant de l'étude à supprimer
     * @throws IllegalArgumentException si l'étude n'existe pas
     */
    void deleteEtude(Integer id);

    /**
     * Vérifie si une référence d'étude est déjà utilisée
     * 
     * @param ref Référence à vérifier
     * @return true si la référence est déjà utilisée
     */
    boolean isRefAlreadyUsed(String ref);

    /**
     * Compte le nombre d'études par type
     * 
     * @param type Type d'étude
     * @return Nombre d'études
     */
    Long countEtudesByType(String type);

    /**
     * Recherche des études dont la date de fin est dans le futur
     * 
     * @return Liste des études à venir
     */
    List<EtudeDTO> getUpcomingEtudes();

    /**
     * Recherche des études dont la date de début est dans le passé et la date de
     * fin dans le futur
     * 
     * @return Liste des études en cours
     */
    List<EtudeDTO> getCurrentEtudes();

    /**
     * Recherche des études dont la date de fin est passée
     * 
     * @return Liste des études terminées
     */
    List<EtudeDTO> getCompletedEtudes();

    int countCurrentEtudes();

    List<EtudeDTO> getRecentEtudes(int limit);

    Optional<Etude> getByRef(String etudeRef);

    List<EtudeDTO> suggestEtudes(String query, int limit);

    Optional<Etude> findById(Integer id);

    List<EtudeDTO> getEtudesByVolontaire(Integer idVolontaire);

    /**
     * Récupère une étude avec les informations d'indemnité volontaire
     * 
     * @param idEtude Identifiant de l'étude
     * @return DTO contenant les informations de l'étude et l'IV
     */
    EtudeDTO getEtudeWithIv(Integer idEtude);

    /**
     * Sauvegarde une étude et met à jour l'indemnité volontaire pour tous les
     * volontaires
     * 
     * @param etudeDTO DTO contenant les informations de l'étude à sauvegarder
     * @return Étude sauvegardée
     */
    Etude saveEtudeWithIv(EtudeDTO etudeDTO);

    /**
     * Obtient des statistiques sur les indemnités volontaires pour une étude
     * 
     * @param idEtude Identifiant de l'étude
     * @return Map contenant différentes statistiques sur les IV
     */
    Map<String, Object> getIvStatistics(Integer idEtude);

    String getEtudeRefById(Integer idEtude);

    /**
     * Met à jour le statut PAYE d'une étude
     * 
     * @param idEtude ID de l'étude
     * @param paye    Nouveau statut PAYE (0, 1 ou 2)
     * @return true si la mise à jour a réussi
     */
    boolean updatePayeStatus(Integer idEtude, Integer paye);
}