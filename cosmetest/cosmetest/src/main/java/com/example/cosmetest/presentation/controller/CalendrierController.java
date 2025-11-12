package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.CalendrierDTO;
import com.example.cosmetest.business.service.CalendrierService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.Map;
import java.util.Collections;
import java.util.List;

/**
 * Contrôleur REST optimisé pour le calendrier des rendez-vous
 * Fournit des endpoints spécialisés pour récupérer efficacement
 * toutes les données nécessaires à l'affichage du calendrier
 */
@RestController
@RequestMapping("/api/calendrier")
public class CalendrierController {

    private static final Logger logger = LoggerFactory.getLogger(CalendrierController.class);

    private final CalendrierService calendrierService;

    public CalendrierController(CalendrierService calendrierService) {
        this.calendrierService = calendrierService;
    }

    /**
     * Récupère toutes les données nécessaires pour l'affichage du calendrier
     * sur une période donnée de manière optimisée
     */
    @GetMapping("/periode")
    public ResponseEntity<CalendrierDTO> getDonneesCalendrier(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(defaultValue = "true") boolean inclureEtudesSansRdv) {

        logger.info("Récupération des données calendrier du {} au {} (inclureEtudesSansRdv: {})",
                dateDebut, dateFin, inclureEtudesSansRdv);

        try {
            CalendrierDTO donneesCalendrier = calendrierService.getDonneesCalendrierOptimisees(
                    dateDebut, dateFin, inclureEtudesSansRdv);

            logger.info("Données calendrier récupérées avec succès: {} RDV, {} études",
                    donneesCalendrier.getRdvs().size(),
                    donneesCalendrier.getEtudes().size());

            return ResponseEntity.ok(donneesCalendrier);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des données calendrier", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Récupère les données optimisées pour une semaine spécifique
     */
    @GetMapping("/semaine")
    public ResponseEntity<CalendrierDTO> getDonneesSemaine(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSemaine) {

        logger.info("Récupération des données calendrier pour la semaine du {}", dateSemaine);

        try {
            CalendrierDTO donneesCalendrier = calendrierService.getDonneesSemaineOptimisees(dateSemaine);

            logger.info("Données semaine récupérées: {} RDV, {} études",
                    donneesCalendrier.getRdvs().size(),
                    donneesCalendrier.getEtudes().size());

            return ResponseEntity.ok(donneesCalendrier);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des données de la semaine", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     *  NOUVELLE VERSION : Récupère tous les RDV d'une étude avec filtrage optionnel par date
     * 
     * @param idEtude ID de l'étude
     * @param dateSelectionnee Date spécifique pour filtrer les RDV (optionnel)
     * @param page Numéro de page
     * @param taille Taille de la page
     * @return RDV de l'étude organisés intelligemment selon la date sélectionnée
     */
    @GetMapping("/etude/{idEtude}/rdvs")
    public ResponseEntity<Map<String, Object>> getRdvsEtudeOptimises(
            @PathVariable Integer idEtude,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateSelectionnee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int taille) {

        if (dateSelectionnee != null) {
            logger.info("Récupération RDV étude {} avec focus sur la date {} (page {}, taille {})",
                    idEtude, dateSelectionnee, page, taille);
        } else {
            logger.info("Récupération RDV étude {} (page {}, taille {})",
                    idEtude, page, taille);
        }

        try {
            Map<String, Object> resultats;
            
            if (dateSelectionnee != null) {
                // 🎯 NOUVEAU : Utiliser la méthode avec date sélectionnée
                resultats = calendrierService.getRdvsEtudeAvecDateSelectionnee(idEtude, dateSelectionnee, page, taille);
            } else {
                // Comportement classique
                resultats = calendrierService.getRdvsEtudeAvecDetails(idEtude, page, taille);
            }

            // Logging adapté selon le type de réponse
            Object rdvsObject = resultats.get("rdvs");
            int totalRdvs = 0;

            if (rdvsObject instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, List<?>> rdvsParCategorie = (Map<String, List<?>>) rdvsObject;

                totalRdvs = rdvsParCategorie.values().stream()
                        .mapToInt(List::size)
                        .sum();

                if (dateSelectionnee != null) {
                    int rdvsDateSelectionnee = rdvsParCategorie.getOrDefault("selectedDate", Collections.emptyList()).size();
                    logger.info("RDV étude récupérés: {} total (dont {} pour la date {})",
                            totalRdvs, rdvsDateSelectionnee, dateSelectionnee);
                } else {
                    logger.info("RDV étude récupérés: {} organisés par catégorie", totalRdvs);
                }

            } else if (rdvsObject instanceof List) {
                totalRdvs = ((List<?>) rdvsObject).size();
                logger.info("RDV étude récupérés: {} résultats", totalRdvs);
            }

            return ResponseEntity.ok(resultats);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des RDV de l'étude {}", idEtude, e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     *  ENDPOINT SPÉCIALISÉ : Récupère uniquement les RDV d'une étude pour une date précise
     * Utile pour un affichage rapide des RDV d'un jour spécifique
     */
    @GetMapping("/etude/{idEtude}/rdvs/date/{date}")
    public ResponseEntity<Map<String, Object>> getRdvsEtudeParDate(
            @PathVariable Integer idEtude,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        logger.info("Récupération RDV étude {} pour la date spécifique {}", idEtude, date);

        try {
            Map<String, Object> resultats = calendrierService.getRdvsEtudeParDateSpecifique(idEtude, date);

            @SuppressWarnings("unchecked")
            List<CalendrierDTO.RendezVousEnrichiDTO> rdvs = (List<CalendrierDTO.RendezVousEnrichiDTO>) resultats.get("rdvs");
            
            logger.info("RDV trouvés pour l'étude {} le {} : {} rendez-vous", 
                    idEtude, date, rdvs != null ? rdvs.size() : 0);

            return ResponseEntity.ok(resultats);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des RDV de l'étude {} pour la date {}", idEtude, date, e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Récupère les statistiques du calendrier pour une période
     */
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiquesCalendrier(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {

        logger.info("Récupération des statistiques calendrier du {} au {}", dateDebut, dateFin);

        try {
            Map<String, Object> statistiques = calendrierService.getStatistiquesPeriode(dateDebut, dateFin);
            logger.info("Statistiques calculées pour la période");
            return ResponseEntity.ok(statistiques);

        } catch (Exception e) {
            logger.error("Erreur lors du calcul des statistiques", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Récupère les créneaux libres pour une période donnée
     */
    @GetMapping("/creneaux-libres")
    public ResponseEntity<Map<String, Object>> getCreneauxLibres(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(defaultValue = "08:00") String heureDebut,
            @RequestParam(defaultValue = "18:00") String heureFin) {

        logger.info("Recherche de créneaux libres du {} au {} entre {}h et {}h",
                dateDebut, dateFin, heureDebut, heureFin);

        try {
            Map<String, Object> creneauxLibres = calendrierService.getCreneauxLibres(
                    dateDebut, dateFin, heureDebut, heureFin);
            return ResponseEntity.ok(creneauxLibres);

        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de créneaux libres", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Pré-charge les données pour améliorer les performances du cache
     */
    @PostMapping("/precharger")
    public ResponseEntity<Map<String, String>> prechargerDonnees() {
        logger.info("Démarrage du pré-chargement des données calendrier");

        try {
            calendrierService.prechargerDonneesFrequentesOptimisees();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Données pré-chargées avec succès"));

        } catch (Exception e) {
            logger.error("Erreur lors du pré-chargement", e);
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Erreur lors du pré-chargement: " + e.getMessage()));
        }
    }

    /**
     * Invalide le cache du calendrier
     */
    @DeleteMapping("/cache")
    public ResponseEntity<Map<String, String>> invaliderCache() {
        logger.info("Invalidation du cache calendrier");

        try {
            calendrierService.invaliderCacheCalendrier();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cache invalidé avec succès"));

        } catch (Exception e) {
            logger.error("Erreur lors de l'invalidation du cache", e);
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Erreur lors de l'invalidation: " + e.getMessage()));
        }
    }

    /**
     * TEST SIMPLE: Tester l'affichage des dates RDV pour une étude
     */
    @GetMapping("/test/etude/{idEtude}/dates")
    public ResponseEntity<String> testEtudeRdvDates(@PathVariable Integer idEtude) {
        try {
            String datesDisplay = calendrierService.getEtudeRdvDatesDisplay(idEtude);
            if (datesDisplay == null) {
                datesDisplay = "Non calculé";
            }
            logger.info("TEST - Étude {} -> Dates RDV: '{}'", idEtude, datesDisplay);
            return ResponseEntity.ok("Étude " + idEtude + " -> " + datesDisplay);

        } catch (Exception e) {
            logger.error("Erreur test étude {}: {}", idEtude, e.getMessage());
            return ResponseEntity.ok("ERREUR: " + e.getMessage());
        }
    }
}