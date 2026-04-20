package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.CalendrierDTO;

import java.time.LocalDate;
import java.util.Map;

/**
 * Interface pour les opérations métier optimisées du calendrier des rendez-vous
 * Fournit des méthodes spécialisées pour récupérer efficacement toutes les
 * données nécessaires à l'affichage du calendrier
 */
public interface CalendrierService {

    /**
     * Récupère toutes les données nécessaires au calendrier pour une période donnée
     * de manière optimisée avec un minimum d'appels à la base de données
     * 
     * @param dateDebut            Date de début de la période
     * @param dateFin              Date de fin de la période
     * @param inclureEtudesSansRdv Inclure les études sans RDV dans la période
     * @return Objet CalendrierDTO contenant tous les RDV enrichis et études
     */
    CalendrierDTO getDonneesCalendrierOptimisees(LocalDate dateDebut, LocalDate dateFin, boolean inclureEtudesSansRdv);

    /**
     * Récupère les données optimisées pour une semaine spécifique
     * La semaine commence le lundi et se termine le dimanche
     * 
     * @param dateSemaine Une date quelconque de la semaine désirée
     * @return Données du calendrier pour la semaine
     */
    CalendrierDTO getDonneesSemaineOptimisees(LocalDate dateSemaine);

    /**
     * Récupère tous les RDV d'une étude avec leurs détails complets
     * et pagination optimisée
     * 
     * @param idEtude ID de l'étude
     * @param page    Numéro de page (commence à 0)
     * @param taille  Taille de la page
     * @return Map contenant les RDV paginés et les métadonnées
     */
    Map<String, Object> getRdvsEtudeAvecDetails(Integer idEtude, int page, int taille);

    /**
     *  Récupère tous les RDV d'une étude avec focus sur une date sélectionnée
     * Organise les RDV en mettant en avant ceux de la date sélectionnée
     * 
     * @param idEtude ID de l'étude
     * @param dateSelectionnee Date à mettre en avant
     * @param page Numéro de page
     * @param taille Taille de la page
     * @return Map contenant les RDV organisés avec la date sélectionnée en premier
     */
    Map<String, Object> getRdvsEtudeAvecDateSelectionnee(Integer idEtude, LocalDate dateSelectionnee, int page, int taille);

    /**
     *  Récupère uniquement les RDV d'une étude pour une date spécifique
     * Méthode optimisée pour récupérer rapidement les RDV d'un jour précis
     * 
     * @param idEtude ID de l'étude
     * @param date Date spécifique
     * @return Map contenant les RDV de la date et les métadonnées
     */
    Map<String, Object> getRdvsEtudeParDateSpecifique(Integer idEtude, LocalDate date);

    /**
     * Calcule les statistiques détaillées pour une période donnée
     * 
     * @param dateDebut Date de début
     * @param dateFin   Date de fin
     * @return Map contenant les statistiques complètes
     */
    Map<String, Object> getStatistiquesPeriode(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Trouve les créneaux libres dans une période donnée
     * 
     * @param dateDebut  Date de début de la recherche
     * @param dateFin    Date de fin de la recherche
     * @param heureDebut Heure de début des créneaux (format "HH:mm")
     * @param heureFin   Heure de fin des créneaux (format "HH:mm")
     * @return Map contenant les créneaux libres organisés par date
     */
    Map<String, Object> getCreneauxLibres(LocalDate dateDebut, LocalDate dateFin,
            String heureDebut, String heureFin);

    /**
     * Pré-charge en cache les données fréquemment utilisées
     * pour améliorer les performances
     */
    void prechargerDonneesFrequentesOptimisees();

    /**
     * Invalide tout le cache du calendrier
     * Utile après des modifications importantes des données
     */
    void invaliderCacheCalendrier();

    /**
     * Récupère les données du calendrier avec mise en cache intelligente
     * 
     * @param dateDebut    Date de début
     * @param dateFin      Date de fin
     * @param forceRefresh Forcer le rafraîchissement du cache
     * @return Données du calendrier avec métadonnées de cache
     */
    CalendrierDTO getDonneesAvecCache(LocalDate dateDebut, LocalDate dateFin, boolean forceRefresh);

    /**
     * Optimise et enrichit les données RDV existantes
     * Ajoute les informations d'études et de volontaires manquantes
     * 
     * @param rdvsNonEnrichis Liste des RDV à enrichir
     * @return Liste des RDV enrichis avec toutes les informations nécessaires
     */
    java.util.List<CalendrierDTO.RendezVousEnrichiDTO> enrichirRdvs(
            java.util.List<com.example.cosmetest.business.dto.RdvDTO> rdvsNonEnrichis);

    /**
     * Récupère les données d'une journée spécifique avec optimisations
     * 
     * @param date Date de la journée
     * @return Données complètes de la journée
     */
    Map<String, Object> getDonneesJournee(LocalDate date);

    /**
     * Calcule les conflits potentiels de planning pour une période
     * 
     * @param dateDebut Date de début
     * @param dateFin   Date de fin
     * @return Liste des conflits détectés
     */
    java.util.List<Map<String, Object>> getConflitsPlanification(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Génère un rapport d'utilisation du calendrier
     * 
     * @param dateDebut Date de début du rapport
     * @param dateFin   Date de fin du rapport
     * @return Rapport détaillé d'utilisation
     */
    Map<String, Object> genererRapportUtilisation(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Récupère les tendances d'utilisation du calendrier
     * 
     * @param nombreSemaines Nombre de semaines à analyser
     * @return Données de tendance
     */
    Map<String, Object> getTendancesUtilisation(int nombreSemaines);

    /**
     * Récupère l'affichage formaté des dates RDV pour une étude
     */
    String getEtudeRdvDatesDisplay(Integer idEtude);
}