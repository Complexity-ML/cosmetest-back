package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.domain.model.VolontaireHc;

import java.util.List;
import java.util.Optional;
import java.util.Map;

/**
 * Interface pour les opérations métier sur l'entité VolontaireHc
 */
public interface VolontaireHcService {

    /**
     * Récupère toutes les habitudes de consommation des volontaires
     *
     * @return la liste de toutes les habitudes de consommation
     */
    List<VolontaireHcDTO> getAllVolontaireHcs();

    /**
     * Récupère les habitudes de consommation d'un volontaire par son ID
     *
     * @param idVol l'identifiant du volontaire
     * @return les habitudes de consommation du volontaire, si elles existent
     */
    Optional<VolontaireHcDTO> getVolontaireHcByIdVol(Integer idVol);

    /**
     * Crée ou met à jour les habitudes de consommation d'un volontaire
     *
     * @param volontaireHcDTO les données des habitudes de consommation
     * @return les habitudes de consommation créées ou mises à jour
     */
    VolontaireHcDTO saveVolontaireHc(VolontaireHcDTO volontaireHcDTO);

    /**
     * Supprime les habitudes de consommation d'un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteVolontaireHc(Integer idVol);

    /**
     * Vérifie si des habitudes de consommation existent pour un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return true si des habitudes existent, false sinon
     */
    boolean existsByIdVol(Integer idVol);

    /**
     * Recherche les volontaires qui utilisent un produit spécifique
     *
     * @param produit le nom du produit (correspond au nom de l'attribut)
     * @param valeur  la valeur du produit (généralement "oui" ou "non")
     * @return la liste des habitudes de consommation des volontaires qui utilisent
     *         ce produit
     */
    List<VolontaireHcDTO> findByProduit(String produit, String valeur);

    /**
     * Recherche les volontaires qui achètent dans un lieu spécifique
     *
     * @param lieuAchat le lieu d'achat ("achatGrandesSurfaces",
     *                  "achatInstitutParfumerie", etc.)
     * @param valeur    la valeur ("oui" ou "non")
     * @return la liste des habitudes de consommation des volontaires concernés
     */
    List<VolontaireHcDTO> findByLieuAchat(String lieuAchat, String valeur);

    /**
     * Met à jour un produit spécifique pour un volontaire
     *
     * @param idVol   l'identifiant du volontaire
     * @param produit le nom du produit à mettre à jour
     * @param valeur  la nouvelle valeur
     * @return les habitudes de consommation mises à jour
     */
    Optional<VolontaireHcDTO> updateProduit(Integer idVol, String produit, String valeur);

    /**
     * Met à jour plusieurs produits pour un volontaire
     *
     * @param idVol    l'identifiant du volontaire
     * @param produits une map des produits à mettre à jour (nom du produit ->
     *                 valeur)
     * @return les habitudes de consommation mises à jour
     */
    Optional<VolontaireHcDTO> updateProduits(Integer idVol, Map<String, String> produits);

    /**
     * Obtient les statistiques d'utilisation d'un produit
     *
     * @param produit le nom du produit
     * @return une map avec les valeurs possibles et le nombre de volontaires pour
     *         chaque valeur
     */
    Map<String, Long> getStatistiquesUtilisationProduit(String produit);

    /**
     * Obtient les produits les plus utilisés (avec valeur "oui")
     *
     * @param limit le nombre de produits à retourner
     * @return une map des produits et leur nombre d'utilisateurs, triée par ordre
     *         décroissant
     */
    Map<String, Long> getProduitsLesPlusUtilises(int limit);

    /**
     * Obtient les lieux d'achat préférés des volontaires
     *
     * @return une map des lieux d'achat et leur fréquentation, triée par ordre
     *         décroissant
     */
    Map<String, Long> getLieuxAchatPreferences();

    /**
     * Recherche les volontaires qui utilisent une combinaison de produits
     *
     * @param produits une map des produits à rechercher (nom du produit -> valeur)
     * @return la liste des habitudes de consommation des volontaires concernés
     */
    List<VolontaireHcDTO> findByMultipleProduits(Map<String, String> produits);

    /**
     * Trouve les habitudes de consommation pour une liste d'identifiants de
     * volontaires
     */
    List<VolontaireHc> findByIdVolIn(List<Integer> idList);

    /**
     * Récupère les habitudes de consommation pour une liste d'identifiants de
     * volontaires
     *
     * @param idList liste des identifiants des volontaires
     * @return liste des habitudes de consommation pour les volontaires spécifiés
     */
    List<VolontaireHcDTO> getVolontaireHcsByIds(List<Integer> idList);

}