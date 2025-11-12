package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.PanelDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Panel
 */
public interface PanelService {

    /**
     * Récupère tous les panels
     *
     * @return la liste de tous les panels
     */
    List<PanelDTO> getAllPanels();

    /**
     * Récupère un panel par son identifiant
     *
     * @param id l'identifiant du panel
     * @return le panel correspondant, s'il existe
     */
    Optional<PanelDTO> getPanelById(Integer id);

    /**
     * Récupère tous les panels associés à une étude
     *
     * @param idEtude l'identifiant de l'étude
     * @return la liste des panels associés
     */
    List<PanelDTO> getPanelsByIdEtude(int idEtude);

    /**
     * Récupère tous les panels associés à un groupe
     *
     * @param idGroupe l'identifiant du groupe
     * @return la liste des panels associés
     */
    List<PanelDTO> getPanelsByIdGroupe(int idGroupe);

    /**
     * Récupère tous les panels associés à une étude et un groupe
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @return la liste des panels associés
     */
    List<PanelDTO> getPanelsByIdEtudeAndIdGroupe(int idEtude, int idGroupe);

    /**
     * Crée un nouveau panel
     *
     * @param panelDTO les données du panel à créer
     * @return le panel créé avec son identifiant généré
     */
    PanelDTO createPanel(PanelDTO panelDTO);

    /**
     * Met à jour un panel existant
     *
     * @param id l'identifiant du panel à mettre à jour
     * @param panelDTO les nouvelles données du panel
     * @return le panel mis à jour
     */
    Optional<PanelDTO> updatePanel(Integer id, PanelDTO panelDTO);

    /**
     * Supprime un panel par son identifiant
     *
     * @param id l'identifiant du panel à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean deletePanel(Integer id);

    /**
     * Vérifie si un panel existe par son identifiant
     *
     * @param id l'identifiant à vérifier
     * @return true si le panel existe, false sinon
     */
    boolean existsById(Integer id);

    /**
     * Récupère tous les panels pour un sexe spécifique
     *
     * @param sexe le sexe recherché
     * @return la liste des panels
     */
    List<PanelDTO> getPanelsBySexe(String sexe);

    /**
     * Récupère tous les panels pour un type de peau visage spécifique
     *
     * @param typePeauVisage le type de peau visage
     * @return la liste des panels
     */
    List<PanelDTO> getPanelsByTypePeauVisage(String typePeauVisage);

    /**
     * Récupère tous les panels pour un phototype spécifique
     *
     * @param phototype le phototype
     * @return la liste des panels
     */
    List<PanelDTO> getPanelsByPhototype(String phototype);

    /**
     * Recherche avancée de panels par plusieurs critères
     *
     * @param sexe le sexe (optionnel)
     * @param phototype le phototype (optionnel)
     * @param carnation la carnation (optionnel)
     * @return la liste des panels correspondants
     */
    List<PanelDTO> searchPanelsByMultipleCriteria(String sexe, String phototype, String carnation);

    /**
     * Récupère tous les panels d'une étude filtrés par sexe
     *
     * @param idEtude l'identifiant de l'étude
     * @param sexe le sexe recherché
     * @return la liste des panels
     */
    List<PanelDTO> getPanelsByIdEtudeAndSexe(int idEtude, String sexe);

    /**
     * Récupère des statistiques sur les panels d'une étude
     *
     * @param idEtude l'identifiant de l'étude
     * @return un objet contenant des statistiques sur les panels
     */
    PanelStatisticsDTO getPanelStatisticsByIdEtude(int idEtude);

    /**
     * DTO pour les statistiques sur les panels
     */
    class PanelStatisticsDTO {
        private int totalPanels;
        private int panelsHommes;
        private int panelsFemmes;
        private List<String> phototypesDistribution;

        // Getters et setters

        public int getTotalPanels() {
            return totalPanels;
        }

        public void setTotalPanels(int totalPanels) {
            this.totalPanels = totalPanels;
        }

        public int getPanelsHommes() {
            return panelsHommes;
        }

        public void setPanelsHommes(int panelsHommes) {
            this.panelsHommes = panelsHommes;
        }

        public int getPanelsFemmes() {
            return panelsFemmes;
        }

        public void setPanelsFemmes(int panelsFemmes) {
            this.panelsFemmes = panelsFemmes;
        }

        public List<String> getPhototypesDistribution() {
            return phototypesDistribution;
        }

        public void setPhototypesDistribution(List<String> phototypesDistribution) {
            this.phototypesDistribution = phototypesDistribution;
        }
    }
}