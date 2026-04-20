package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.PanelHcDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité PanelHc
 */
public interface PanelHcService {

    /**
     * Récupère tous les panels HC
     *
     * @return la liste de tous les panels HC
     */
    List<PanelHcDTO> getAllPanelsHc();

    /**
     * Récupère un panel HC par son identifiant
     *
     * @param id l'identifiant du panel HC
     * @return le panel HC correspondant, s'il existe
     */
    Optional<PanelHcDTO> getPanelHcById(int id);

    /**
     * Crée un nouveau panel HC
     *
     * @param panelHcDTO les données du panel HC à créer
     * @return le panel HC créé avec son identifiant généré
     */
    PanelHcDTO createPanelHc(PanelHcDTO panelHcDTO);

    /**
     * Met à jour un panel HC existant
     *
     * @param id l'identifiant du panel HC à mettre à jour
     * @param panelHcDTO les nouvelles données du panel HC
     * @return le panel HC mis à jour
     */
    Optional<PanelHcDTO> updatePanelHc(int id, PanelHcDTO panelHcDTO);

    /**
     * Supprime un panel HC par son identifiant
     *
     * @param id l'identifiant du panel HC à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean deletePanelHc(int id);

    /**
     * Trouve les panels par lieu d'achat
     *
     * @param lieu le lieu d'achat (valeur attendue dans les champs d'achat)
     * @return la liste des panels correspondants
     */
    List<PanelHcDTO> getPanelsByLieuAchat(String lieu);

    /**
     * Trouve les panels qui utilisent des produits bio
     *
     * @param valeur la valeur du champ produitsBio (généralement "Oui" ou "Non")
     * @return la liste des panels correspondants
     */
    List<PanelHcDTO> getPanelsByProduitsBio(String valeur);

    /**
     * Trouve les panels par méthode d'épilation
     *
     * @param methode la méthode d'épilation
     * @return la liste des panels correspondants
     */
    List<PanelHcDTO> getPanelsByMethodeEpilation(String methode);

    /**
     * Trouve les panels par type de maquillage utilisé
     *
     * @param maquillage le type de maquillage
     * @return la liste des panels correspondants
     */
    List<PanelHcDTO> getPanelsByTypeMaquillage(String maquillage);

    /**
     * Trouve les panels par type de soin visage utilisé
     *
     * @param soin le type de soin visage
     * @return la liste des panels correspondants
     */
    List<PanelHcDTO> getPanelsBySoinVisage(String soin);

    /**
     * Trouve les panels par type de soin corps utilisé
     *
     * @param soin le type de soin corps
     * @return la liste des panels correspondants
     */
    List<PanelHcDTO> getPanelsBySoinCorps(String soin);

    /**
     * Trouve les panels par type de produit capillaire utilisé
     *
     * @param produit le type de produit capillaire
     * @return la liste des panels correspondants
     */
    List<PanelHcDTO> getPanelsByProduitCapillaire(String produit);
}