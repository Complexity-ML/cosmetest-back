package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.VolontaireDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.domain.model.Volontaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Volontaire
 */
public interface VolontaireService {

    /**
     * Récupère tous les volontaires (version simplifiée)
     *
     * @return la liste de tous les volontaires
     */
    List<VolontaireDTO> getAllVolontaires();

    /**
     * Récupère tous les volontaires non archivés (version simplifiée)
     *
     * @return la liste des volontaires non archivés
     */
    List<VolontaireDTO> getAllActiveVolontaires();

    /**
     * Récupère un volontaire par son identifiant (version simplifiée)
     *
     * @param id l'identifiant du volontaire
     * @return le volontaire correspondant, s'il existe
     */
    Optional<VolontaireDTO> getVolontaireById(Integer id);

    /**
     * Récupère un volontaire par son identifiant (version détaillée)
     *
     * @param id l'identifiant du volontaire
     * @return le volontaire détaillé correspondant, s'il existe
     */
    Optional<VolontaireDetailDTO> getVolontaireDetailById(Integer id);

    /**
     * Recherche de volontaires par nom et/ou prénom
     *
     * @param nom le nom à rechercher (peut être null)
     * @param prenom le prénom à rechercher (peut être null)
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> searchVolontairesByNomPrenom(String nom, String prenom);

    /**
     * Recherche de volontaires par texte (recherche plein texte)
     *
     * @param searchText le texte à rechercher
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> searchVolontaires(String searchText);

    /**
     * Recherche de volontaires par mot-clé avec pagination
     * Cette méthode utilise l'index fulltext pour rechercher dans:
     * nom, prénom, téléphones et email
     *
     * @param keyword mot-clé de recherche
     * @param pageable configuration de pagination
     * @return page de volontaires correspondant au critère de recherche
     */
    Page<VolontaireDTO> searchVolontaires(String keyword, Pageable pageable);

    /**
     * Recherche de volontaires par critères variés
     *
     * @param sexe le sexe (peut être null)
     * @param ageMin l'âge minimum (peut être null)
     * @param ageMax l'âge maximum (peut être null)
     * @param ethnie l'ethnie (peut être null)
     * @param phototype le phototype (peut être null)
     * @param typePeau le type de peau (peut être null)
     * @return la liste des volontaires correspondant aux critères
     */
    List<VolontaireDTO> searchVolontairesByCriteria(String sexe, Integer ageMin, Integer ageMax,
                                                    String ethnie, String phototype, String typePeau);

    /**
     * Crée un nouveau volontaire (version simplifiée)
     *
     * @param volontaireDTO les données du volontaire à créer
     * @return le volontaire créé avec son identifiant généré
     */
    VolontaireDTO createVolontaire(VolontaireDTO volontaireDTO);

    /**
     * Crée un nouveau volontaire (version détaillée)
     *
     * @param volontaireDetailDTO les données détaillées du volontaire à créer
     * @return le volontaire détaillé créé avec son identifiant généré
     */
    VolontaireDetailDTO createVolontaireDetail(VolontaireDetailDTO volontaireDetailDTO);

    /**
     * Met à jour un volontaire existant (version simplifiée)
     *
     * @param id l'identifiant du volontaire à mettre à jour
     * @param volontaireDTO les nouvelles données du volontaire
     * @return le volontaire mis à jour
     */
    Optional<VolontaireDTO> updateVolontaire(Integer id, VolontaireDTO volontaireDTO);

    /**
     * Met à jour un volontaire existant (version détaillée)
     *
     * @param id l'identifiant du volontaire à mettre à jour
     * @param volontaireDetailDTO les nouvelles données détaillées du volontaire
     * @return le volontaire détaillé mis à jour
     */
    Optional<VolontaireDetailDTO> updateVolontaireDetail(Integer id, VolontaireDetailDTO volontaireDetailDTO);

    /**
     * Archive ou désarchive un volontaire
     *
     * @param id l'identifiant du volontaire
     * @param archive true pour archiver, false pour désarchiver
     * @return le volontaire mis à jour
     */
    Optional<VolontaireDTO> toggleArchiveVolontaire(Integer id, boolean archive);

    /**
     * Supprime un volontaire (suppression physique)
     *
     * @param id l'identifiant du volontaire à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVolontaire(Integer id);

    /**
     * Vérifie si un volontaire existe par son identifiant
     *
     * @param id l'identifiant à vérifier
     * @return true si le volontaire existe, false sinon
     */
    boolean existsById(Integer id);

    /**
     * Récupère les volontaires par plage d'âge
     *
     * @param ageMin l'âge minimum
     * @param ageMax l'âge maximum
     * @return la liste des volontaires dans la plage d'âge
     */
    List<VolontaireDTO> getVolontairesByAgeRange(Integer ageMin, Integer ageMax);

    /**
     * Récupère les volontaires par sexe
     *
     * @param sexe le sexe recherché
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> getVolontairesBySexe(String sexe);

    /**
     * Récupère les volontaires par ethnie
     *
     * @param ethnie l'ethnie recherchée
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> getVolontairesByEthnie(String ethnie);

    /**
     * Récupère les volontaires par phototype
     *
     * @param phototype le phototype recherché
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> getVolontairesByPhototype(String phototype);

    /**
     * Récupère les volontaires par type de peau
     *
     * @param typePeau le type de peau recherché
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> getVolontairesByTypePeau(String typePeau);

    /**
     * Récupère les volontaires par état d'acné
     *
     * @param acne la valeur de l'acné (Oui/Non)
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> getVolontairesByAcne(String acne);

    /**
     * Récupère les volontaires par compatibilité de santé
     *
     * @param santeCompatible la valeur de compatibilité (Oui/Non)
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> getVolontairesBySanteCompatible(String santeCompatible);

    /**
     * Vérifie si un volontaire est compatible avec une plage d'âge spécifique
     *
     * @param idVol l'identifiant du volontaire
     * @param ageMin l'âge minimum
     * @param ageMax l'âge maximum
     * @return true si le volontaire est compatible, false sinon
     */
    boolean isVolontaireCompatibleWithAgeRange(Integer idVol, Integer ageMin, Integer ageMax);

    /**
     * Calcule l'âge d'un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return l'âge calculé, ou null si la date de naissance n'est pas définie
     */
    Integer calculateVolontaireAge(Integer idVol);

    /**
     * Trouve les volontaires qui ont été ajoutés entre deux dates
     *
     * @param dateDebut la date de début
     * @param dateFin la date de fin
     * @return la liste des volontaires correspondants
     */
    List<VolontaireDTO> getVolontairesAddedBetweenDates(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Récupère les statistiques de base sur les volontaires
     * (Nombre total, répartition par sexe, répartition par ethnie, etc.)
     *
     * @return un objet contenant les différentes statistiques
     */
    Map<String, Object> getVolontairesStatistics();


    int countActiveVolontaires();

    int countVolontairesAddedToday();

    List<VolontaireDTO> getRecentVolontaires(int limit);

    /**
     * Récupère les informations d'une photo pour un volontaire
     *
     * @param id ID du volontaire
     * @param photoType type de la photo (face, droite, etc.)
     * @return Map contenant les informations de la photo
     */
    Map<String, Object> getVolontairePhoto(Integer id, String photoType);

    /**
     * Récupère toutes les photos disponibles pour un volontaire
     *
     * @param id ID du volontaire
     * @return Liste des photos disponibles
     */
    List<Map<String, Object>> getAllVolontairePhotos(Integer id);

    Page<VolontaireDTO> getVolontairesPaginated(int page, int size, boolean includeArchived, String search);

    List<Volontaire> findAllByIdIn(List<Integer> idList);
}