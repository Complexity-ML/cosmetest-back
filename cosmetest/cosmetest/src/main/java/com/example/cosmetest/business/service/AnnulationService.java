package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.AnnulationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour la couche business (BLL) des annulations
 * Définit les opérations métier disponibles sur les annulations
 */
public interface AnnulationService {

    /**
     * Récupère toutes les annulations
     * @return Liste de toutes les annulations
     */
    List<AnnulationDTO> getAllAnnulations();

    /**
     * Récupère les annulations avec pagination
     * @param pageable Informations de pagination
     * @return Page d'annulations
     */
    Page<AnnulationDTO> getAllAnnulationsPaginated(Pageable pageable);

    /**
     * Recherche une annulation par son identifiant
     * @param id Identifiant de l'annulation
     * @return L'annulation trouvée ou vide si non trouvée
     */
    Optional<AnnulationDTO> getAnnulationById(Integer id);

    /**
     * Recherche les annulations d'un volontaire
     * @param idVol Identifiant du volontaire
     * @return Liste des annulations du volontaire
     */
    List<AnnulationDTO> getAnnulationsByVolontaire(int idVol);

    /**
     * Recherche les annulations d'une étude
     * @param idEtude Identifiant de l'étude
     * @return Liste des annulations de l'étude
     */
    List<AnnulationDTO> getAnnulationsByEtude(int idEtude);

    /**
     * Recherche les annulations d'un volontaire pour une étude spécifique
     * @param idVol Identifiant du volontaire
     * @param idEtude Identifiant de l'étude
     * @return Liste des annulations correspondantes
     */
    List<AnnulationDTO> getAnnulationsByVolontaireAndEtude(int idVol, int idEtude);

    /**
     * Recherche les annulations par date
     * @param dateAnnulation Date de l'annulation
     * @return Liste des annulations à cette date
     */
    List<AnnulationDTO> getAnnulationsByDate(String dateAnnulation);

    /**
     * Recherche des annulations par mots-clés dans les commentaires
     * @param keyword Mot-clé à rechercher
     * @return Liste des annulations correspondantes
     */
    List<AnnulationDTO> searchAnnulationsByCommentaire(String keyword);

    /**
     * Enregistre une nouvelle annulation ou met à jour une existante
     * @param annulationDTO DTO de l'annulation à sauvegarder
     * @return DTO de l'annulation sauvegardée
     */
    AnnulationDTO saveAnnulation(AnnulationDTO annulationDTO);

    /**
     * Supprime une annulation
     * @param id Identifiant de l'annulation à supprimer
     */
    void deleteAnnulation(Integer id);

    /**
     * Compte le nombre d'annulations par volontaire
     * @param idVol Identifiant du volontaire
     * @return Nombre d'annulations
     */
    Long countAnnulationsByVolontaire(int idVol);

    /**
     * Récupère les annulations d'un volontaire triées par date (la plus récente d'abord)
     * @param idVol Identifiant du volontaire
     * @return Liste des annulations triées
     */
    List<AnnulationDTO> getAnnulationsByVolontaireOrderByDateDesc(int idVol);
}