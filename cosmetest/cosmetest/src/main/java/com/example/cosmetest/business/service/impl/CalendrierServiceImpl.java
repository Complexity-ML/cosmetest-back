package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.*;
import com.example.cosmetest.business.service.CalendrierService;
import com.example.cosmetest.business.service.RdvService;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.VolontaireService;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.data.repository.EtudeRepository;
import com.example.cosmetest.data.repository.VolontaireRepository;
import com.example.cosmetest.domain.model.Rdv;
import com.example.cosmetest.domain.model.Etude;
import com.example.cosmetest.domain.model.Volontaire;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implémentation optimisée du service calendrier
 * Utilise des requêtes optimisées et la mise en cache pour améliorer les
 * performances
 */
@Service
@Transactional(readOnly = true)
public class CalendrierServiceImpl implements CalendrierService {

    private static final Logger logger = LoggerFactory.getLogger(CalendrierServiceImpl.class);

    private final RdvRepository rdvRepository;
    private final EtudeRepository etudeRepository;
    private final VolontaireRepository volontaireRepository;
    private final EtudeService etudeService;

    public CalendrierServiceImpl(
            RdvRepository rdvRepository,
            EtudeRepository etudeRepository,
            VolontaireRepository volontaireRepository,
            RdvService rdvService,
            EtudeService etudeService,
            VolontaireService volontaireService) {
        this.rdvRepository = rdvRepository;
        this.etudeRepository = etudeRepository;
        this.volontaireRepository = volontaireRepository;
        this.etudeService = etudeService;
    }

    @Override
    @Cacheable(value = "calendrierPeriode", key = "#dateDebut.toString() + '_' + #dateFin.toString() + '_' + #inclureEtudesSansRdv")
    public CalendrierDTO getDonneesCalendrierOptimisees(LocalDate dateDebut, LocalDate dateFin,
            boolean inclureEtudesSansRdv) {
        long startTime = System.currentTimeMillis();

        logger.info("Récupération optimisée des données calendrier du {} au {}", dateDebut, dateFin);

        CalendrierDTO calendrier = new CalendrierDTO(dateDebut, dateFin);

        try {
            // 1. Récupération optimisée des RDV avec jointures
            List<CalendrierDTO.RendezVousEnrichiDTO> rdvs = recupererRdvsOptimises(dateDebut, dateFin);
            calendrier.setRdvs(rdvs);

            // 2. Récupération des études de la période
            List<CalendrierDTO.EtudeCalendrierDTO> etudes = recupererEtudesPeriode(dateDebut, dateFin,
                    inclureEtudesSansRdv);
            calendrier.setEtudes(etudes);

            // 3. Calcul des statistiques
            CalendrierDTO.StatistiquesCalendrierDTO stats = calculerStatistiques(rdvs, etudes);
            calendrier.setStatistiques(stats);

            // 4. Métadonnées
            CalendrierDTO.MetaDonneesCalendrierDTO meta = new CalendrierDTO.MetaDonneesCalendrierDTO();
            meta.setHorodatageGeneration(LocalDateTime.now());
            meta.setDureeGenerationMs((int) (System.currentTimeMillis() - startTime));
            meta.setDonneesCache(false); // Première génération
            calendrier.setMetaDonnees(meta);

            logger.info("Données calendrier générées en {}ms: {} RDV, {} études",
                    meta.getDureeGenerationMs(), rdvs.size(), etudes.size());

            return calendrier;

        } catch (Exception e) {
            logger.error("Erreur lors de la génération des données calendrier", e);
            throw new RuntimeException("Erreur lors de la génération du calendrier", e);
        }
    }

    @Override
    public CalendrierDTO getDonneesSemaineOptimisees(LocalDate dateSemaine) {
        // Calculer le début et la fin de la semaine (lundi à dimanche)
        LocalDate debutSemaine = dateSemaine.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate finSemaine = dateSemaine.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        logger.info("Récupération des données pour la semaine du {} au {}", debutSemaine, finSemaine);

        return getDonneesCalendrierOptimisees(debutSemaine, finSemaine, true);
    }

    @Override
    public Map<String, Object> getRdvsEtudeAvecDetails(Integer idEtude, int page, int taille) {
        logger.info("Récupération des RDV de l'étude {} avec pagination (page {}, taille {})",
                idEtude, page, taille);

        try {
            // Récupération de l'étude
            Optional<EtudeDTO> etudeOpt = etudeService.getEtudeById(idEtude);
            if (etudeOpt.isEmpty()) {
                throw new IllegalArgumentException("Étude non trouvée: " + idEtude);
            }

            // Récupération paginée des RDV avec optimisation
            Pageable pageable = PageRequest.of(page, taille, Sort.by(Sort.Direction.DESC, "date"));
            Page<Rdv> rdvsPage = rdvRepository.findByIdEtudeWithDetailsOptimized(idEtude, pageable);

            // *** FIX : Utiliser la méthode optimisée comme dans recupererRdvsOptimises()
            // ***
            List<Rdv> rdvsList = rdvsPage.getContent();

            // 1. Collecter tous les IDs de volontaires uniques
            Set<Integer> idsVolontaires = rdvsList.stream()
                    .map(Rdv::getIdVolontaire)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // 2. Récupérer tous les volontaires en une seule fois
            Map<Integer, Volontaire> volontairesMap = volontaireRepository.findAllById(idsVolontaires)
                    .stream()
                    .collect(Collectors.toMap(Volontaire::getIdVol, volontaire -> volontaire));

            // 3. Enrichir les RDV avec la méthode optimisée
            List<CalendrierDTO.RendezVousEnrichiDTO> rdvsEnrichis = rdvsList.stream()
                    .map(rdv -> convertirVersRdvEnrichiAvecVolontaires(rdv, volontairesMap))
                    .collect(Collectors.toList());

            // Organisation par catégories temporelles
            Map<String, List<CalendrierDTO.RendezVousEnrichiDTO>> rdvsParCategorie = organiserRdvsParStatutTemporel(
                    rdvsEnrichis);

            Map<String, Object> resultat = new HashMap<>();
            resultat.put("etude", convertirVersEtudeMinimale(etudeOpt.get()));
            resultat.put("rdvs", rdvsParCategorie);
            resultat.put("pagination", Map.of(
                    "page", rdvsPage.getNumber(),
                    "taille", rdvsPage.getSize(),
                    "totalElements", rdvsPage.getTotalElements(),
                    "totalPages", rdvsPage.getTotalPages()));

            return resultat;

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des RDV de l'étude {}", idEtude, e);
            throw new RuntimeException("Erreur lors de la récupération des RDV de l'étude", e);
        }
    }

    @Override
    public Map<String, Object> getStatistiquesPeriode(LocalDate dateDebut, LocalDate dateFin) {
        logger.info("Calcul des statistiques pour la période {} - {}", dateDebut, dateFin);

        try {
            // Statistiques RDV
            int totalRdv = rdvRepository.countByDateBetween(Date.valueOf(dateDebut), Date.valueOf(dateFin));
            Map<String, Integer> repartitionEtats = rdvRepository.countRdvByEtatBetweenDates(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin));

            // Statistiques études
            int totalEtudes = etudeRepository.countEtudesActivesEntreDates(dateDebut, dateFin);

            // Répartition par jour de la semaine
            Map<String, Integer> repartitionJours = rdvRepository.countRdvByDayOfWeekBetweenDates(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin));

            // Répartition par tranche horaire
            Map<String, Integer> repartitionHeures = rdvRepository.countRdvByHourBetweenDates(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin));

            Map<String, Object> statistiques = new HashMap<>();
            statistiques.put("totalRdv", totalRdv);
            statistiques.put("repartitionEtats", repartitionEtats);
            statistiques.put("totalEtudes", totalEtudes);
            statistiques.put("repartitionJours", repartitionJours);
            statistiques.put("repartitionHeures", repartitionHeures);
            statistiques.put("periode", Map.of("debut", dateDebut, "fin", dateFin));

            return statistiques;

        } catch (Exception e) {
            logger.error("Erreur lors du calcul des statistiques", e);
            throw new RuntimeException("Erreur lors du calcul des statistiques", e);
        }
    }

    @Override
    public Map<String, Object> getCreneauxLibres(LocalDate dateDebut, LocalDate dateFin,
            String heureDebut, String heureFin) {
        logger.info("Recherche de créneaux libres du {} au {} entre {} et {}",
                dateDebut, dateFin, heureDebut, heureFin);

        try {
            // Récupérer tous les RDV de la période
            List<Rdv> rdvsExistants = rdvRepository.findByDateBetweenOrderByDateAscHeureAsc(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin));

            // Générer tous les créneaux possibles
            Map<LocalDate, List<String>> creneauxLibres = genererCreneauxLibres(
                    dateDebut, dateFin, heureDebut, heureFin, rdvsExistants);

            Map<String, Object> resultat = new HashMap<>();
            resultat.put("creneaux", creneauxLibres);
            resultat.put("periode", Map.of("debut", dateDebut, "fin", dateFin));
            resultat.put("horaires", Map.of("debut", heureDebut, "fin", heureFin));
            resultat.put("totalCreneaux", creneauxLibres.values().stream()
                    .mapToInt(List::size).sum());

            return resultat;

        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de créneaux libres", e);
            throw new RuntimeException("Erreur lors de la recherche de créneaux libres", e);
        }
    }

    @Override
    @CacheEvict(value = { "calendrierPeriode", "calendrierSemaine" }, allEntries = true)
    public void prechargerDonneesFrequentesOptimisees() {
        logger.info("Pré-chargement des données fréquentes du calendrier");

        // Pré-charger la semaine courante
        LocalDate today = LocalDate.now();
        getDonneesSemaineOptimisees(today);

        // Pré-charger la semaine prochaine
        getDonneesSemaineOptimisees(today.plusWeeks(1));

        // Pré-charger le mois courant
        LocalDate debutMois = today.withDayOfMonth(1);
        LocalDate finMois = today.with(TemporalAdjusters.lastDayOfMonth());
        getDonneesCalendrierOptimisees(debutMois, finMois, true);

        logger.info("Pré-chargement terminé");
    }

    @Override
    @CacheEvict(value = { "calendrierPeriode", "calendrierSemaine" }, allEntries = true)
    public void invaliderCacheCalendrier() {
        logger.info("Invalidation du cache calendrier");
    }

    @Override
    @Cacheable(value = "calendrierCache", key = "#dateDebut.toString() + '_' + #dateFin.toString() + '_' + #forceRefresh")
    public CalendrierDTO getDonneesAvecCache(LocalDate dateDebut, LocalDate dateFin, boolean forceRefresh) {
        CalendrierDTO donnees = getDonneesCalendrierOptimisees(dateDebut, dateFin, true);

        // Marquer comme données en cache
        if (donnees.getMetaDonnees() != null) {
            donnees.getMetaDonnees().setDonneesCache(!forceRefresh);
        }

        return donnees;
    }

    @Override
    public List<CalendrierDTO.RendezVousEnrichiDTO> enrichirRdvs(List<RdvDTO> rdvsNonEnrichis) {
        logger.info("Enrichissement de {} RDV", rdvsNonEnrichis.size());

        try {
            // Collecter tous les IDs d'études et de volontaires uniques
            Set<Integer> idsEtudes = rdvsNonEnrichis.stream()
                    .map(RdvDTO::getIdEtude)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Set<Integer> idsVolontaires = rdvsNonEnrichis.stream()
                    .map(RdvDTO::getIdVolontaire)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Récupérer toutes les études en une fois
            Map<Integer, Etude> etudesMap = etudeRepository.findAllById(idsEtudes)
                    .stream()
                    .collect(Collectors.toMap(Etude::getIdEtude, etude -> etude));

            // Récupérer tous les volontaires en une fois
            Map<Integer, Volontaire> volontairesMap = volontaireRepository.findAllById(idsVolontaires)
                    .stream()
                    .collect(Collectors.toMap(Volontaire::getIdVol, volontaire -> volontaire));

            // Enrichir tous les RDV
            return rdvsNonEnrichis.stream()
                    .map(rdv -> enrichirRdvUnique(rdv, etudesMap, volontairesMap))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Erreur lors de l'enrichissement des RDV", e);
            throw new RuntimeException("Erreur lors de l'enrichissement des RDV", e);
        }
    }

    @Override
    public Map<String, Object> getDonneesJournee(LocalDate date) {
        logger.info("Récupération des données de la journée {}", date);

        CalendrierDTO donneesJournee = getDonneesCalendrierOptimisees(date, date, true);

        Map<String, Object> resultat = new HashMap<>();
        resultat.put("date", date);
        resultat.put("rdvs", donneesJournee.getRdvs());
        resultat.put("etudes", donneesJournee.getEtudes());
        resultat.put("statistiques", donneesJournee.getStatistiques());

        return resultat;
    }

    @Override
    public List<Map<String, Object>> getConflitsPlanification(LocalDate dateDebut, LocalDate dateFin) {
        logger.info("Détection des conflits de planification du {} au {}", dateDebut, dateFin);

        try {
            List<Map<String, Object>> conflits = new ArrayList<>();

            // Rechercher les volontaires avec plusieurs RDV le même jour
            List<Object[]> conflitsVolontaires = rdvRepository.findVolontairesWithMultipleRdvSameDay(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin));

            for (Object[] conflit : conflitsVolontaires) {
                Map<String, Object> conflitMap = new HashMap<>();
                conflitMap.put("type", "VOLONTAIRE_MULTIPLE_RDV");
                conflitMap.put("idVolontaire", conflit[0]);
                conflitMap.put("date", conflit[1]);
                conflitMap.put("nombreRdv", conflit[2]);
                conflits.add(conflitMap);
            }

            // Rechercher les chevauchements d'horaires
            List<Object[]> chevauchements = rdvRepository.findOverlappingAppointments(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin));

            for (Object[] chevauchement : chevauchements) {
                Map<String, Object> chevauchementMap = new HashMap<>();
                chevauchementMap.put("type", "CHEVAUCHEMENT_HORAIRE");
                chevauchementMap.put("date", chevauchement[0]);
                chevauchementMap.put("heure", chevauchement[1]);
                chevauchementMap.put("nombreRdv", chevauchement[2]);
                conflits.add(chevauchementMap);
            }

            logger.info("Détectés {} conflits de planification", conflits.size());
            return conflits;

        } catch (Exception e) {
            logger.error("Erreur lors de la détection des conflits", e);
            throw new RuntimeException("Erreur lors de la détection des conflits", e);
        }
    }

    @Override
    public Map<String, Object> genererRapportUtilisation(LocalDate dateDebut, LocalDate dateFin) {
        logger.info("Génération du rapport d'utilisation du {} au {}", dateDebut, dateFin);

        try {
            Map<String, Object> rapport = new HashMap<>();

            // Données de base
            Map<String, Object> statistiques = getStatistiquesPeriode(dateDebut, dateFin);
            rapport.putAll(statistiques);

            // Taux d'utilisation par jour
            Map<LocalDate, Double> tauxUtilisationJour = calculerTauxUtilisationParJour(dateDebut, dateFin);
            rapport.put("tauxUtilisationParJour", tauxUtilisationJour);

            // Études les plus actives
            List<Map<String, Object>> etudesActives = rdvRepository.findMostActiveStudiesBetweenDates(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin), 10);
            rapport.put("etudesLesPlusActives", etudesActives);

            // Volontaires les plus actifs
            List<Map<String, Object>> volontairesActifs = rdvRepository.findMostActiveVolunteersBetweenDates(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin), 10);
            rapport.put("volontairesLesPlusActifs", volontairesActifs);

            // Métadonnées du rapport
            rapport.put("dateGeneration", LocalDateTime.now());
            rapport.put("periode", Map.of("debut", dateDebut, "fin", dateFin));

            return rapport;

        } catch (Exception e) {
            logger.error("Erreur lors de la génération du rapport", e);
            throw new RuntimeException("Erreur lors de la génération du rapport", e);
        }
    }

    @Override
    public Map<String, Object> getTendancesUtilisation(int nombreSemaines) {
        logger.info("Analyse des tendances sur {} semaines", nombreSemaines);

        try {
            LocalDate dateDebut = LocalDate.now().minusWeeks(nombreSemaines);
            LocalDate dateFin = LocalDate.now();

            Map<String, Object> tendances = new HashMap<>();

            // Évolution du nombre de RDV par semaine
            List<Map<String, Object>> evolutionRdv = rdvRepository.countRdvByWeekBetweenDates(
                    Date.valueOf(dateDebut), Date.valueOf(dateFin));
            tendances.put("evolutionRdvParSemaine", evolutionRdv);

            // Évolution des états de RDV
            Map<String, List<Map<String, Object>>> evolutionEtats = new HashMap<>();
            for (String etat : Arrays.asList("CONFIRME", "EN_ATTENTE", "ANNULE", "COMPLETE")) {
                List<Map<String, Object>> evolutionEtat = rdvRepository.countRdvByWeekAndEtatBetweenDates(
                        Date.valueOf(dateDebut), Date.valueOf(dateFin), etat);
                evolutionEtats.put(etat, evolutionEtat);
            }
            tendances.put("evolutionEtats", evolutionEtats);

            // Prédictions simples basées sur les tendances
            Map<String, Object> predictions = calculerPredictionsSimples(evolutionRdv);
            tendances.put("predictions", predictions);

            tendances.put("periode", Map.of(
                    "debut", dateDebut,
                    "fin", dateFin,
                    "nombreSemaines", nombreSemaines));

            return tendances;

        } catch (Exception e) {
            logger.error("Erreur lors de l'analyse des tendances", e);
            throw new RuntimeException("Erreur lors de l'analyse des tendances", e);
        }
    }

    // Méthodes privées utilitaires

    private List<CalendrierDTO.RendezVousEnrichiDTO> recupererRdvsOptimises(LocalDate dateDebut, LocalDate dateFin) {
        // Utiliser une requête optimisée avec jointures
        List<Rdv> rdvs = rdvRepository.findByDateBetweenWithEtudeAndVolontaireOptimized(
                Date.valueOf(dateDebut), Date.valueOf(dateFin));

        // *** CORRECTION : Enrichir avec les volontaires de manière optimisée ***

        // 1. Collecter tous les IDs de volontaires uniques
        Set<Integer> idsVolontaires = rdvs.stream()
                .map(Rdv::getIdVolontaire)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2. Récupérer tous les volontaires en une seule fois
        Map<Integer, Volontaire> volontairesMap = volontaireRepository.findAllById(idsVolontaires)
                .stream()
                .collect(Collectors.toMap(Volontaire::getIdVol, volontaire -> volontaire));

        // 3. Convertir et enrichir chaque RDV
        return rdvs.stream()
                .map(rdv -> convertirVersRdvEnrichiAvecVolontaires(rdv, volontairesMap))
                .collect(Collectors.toList());
    }

    // Nouvelle méthode pour convertir avec les volontaires pré-chargés
    private CalendrierDTO.RendezVousEnrichiDTO convertirVersRdvEnrichiAvecVolontaires(
            Rdv rdv, Map<Integer, Volontaire> volontairesMap) {

        CalendrierDTO.RendezVousEnrichiDTO rdvEnrichi = new CalendrierDTO.RendezVousEnrichiDTO();

        // Copier les données de base
        if (rdv.getId() != null) {
            rdvEnrichi.setIdRdv(rdv.getId().getIdRdv());
            rdvEnrichi.setIdEtude(rdv.getId().getIdEtude());
        }
        rdvEnrichi.setIdVolontaire(rdv.getIdVolontaire());
        rdvEnrichi.setIdGroupe(rdv.getIdGroupe());

        if (rdv.getDate() != null) {
            if (rdv.getDate() instanceof java.sql.Date) {
                rdvEnrichi.setDate(((java.sql.Date) rdv.getDate()).toLocalDate());
            } else if (rdv.getDate() instanceof java.util.Date) {
                rdvEnrichi.setDate(new java.sql.Date(rdv.getDate().getTime()).toLocalDate());
            } else {
                rdvEnrichi.setDate(LocalDate.parse(rdv.getDate().toString()));
            }
        }
        rdvEnrichi.setHeure(rdv.getHeure());
        rdvEnrichi.setEtat(rdv.getEtat());
        rdvEnrichi.setCommentaires(rdv.getCommentaires());

        // Calculer le statut temporel
        if (rdv.getDate() != null) {
            rdvEnrichi.setStatutTemporel(calculerStatutTemporel(rdv.getDate()));
        } else {
            rdvEnrichi.setStatutTemporel("unknown");
        }

        // Ajouter les données enrichies de l'étude
        if (rdv.getEtude() != null) {
            rdvEnrichi.setEtude(convertirVersEtudeMinimale(rdv.getEtude()));
        }

        // *** ENRICHIR AVEC LE VOLONTAIRE PRÉ-CHARGÉ ***
        if (rdv.getIdVolontaire() != null && volontairesMap.containsKey(rdv.getIdVolontaire())) {
            Volontaire volontaire = volontairesMap.get(rdv.getIdVolontaire());
            CalendrierDTO.VolontaireMinimalDTO volontaireMin = new CalendrierDTO.VolontaireMinimalDTO();
            volontaireMin.setId(volontaire.getIdVol());
            volontaireMin.setNom(volontaire.getNomVol());
            volontaireMin.setPrenom(volontaire.getPrenomVol());
            volontaireMin.setTitre(volontaire.getTitreVol());
            if (volontaire.getDateNaissance() != null) {
                volontaireMin.setDateNaissance(volontaire.getDateNaissance());
            }
            rdvEnrichi.setVolontaire(volontaireMin);
        } else if (rdv.getIdVolontaire() != null) {
            logger.warn("Volontaire {} non trouvé pour le RDV {}",
                    rdv.getIdVolontaire(), rdv.getId());
        }

        return rdvEnrichi;
    }

    // Modifier la méthode recupererEtudesPeriode dans CalendrierServiceImpl.java

    private List<CalendrierDTO.EtudeCalendrierDTO> recupererEtudesPeriode(LocalDate dateDebut, LocalDate dateFin,
            boolean inclureEtudesSansRdv) {

        logger.info("🔍 Récupération études période {} à {}", dateDebut, dateFin);

        // 1. Récupérer toutes les études actives dans la période
        List<Etude> etudes = etudeRepository.findEtudesActivesEntreDates(dateDebut, dateFin);
        logger.info("📚 {} études trouvées dans la période", etudes.size());

        // 2. Récupérer TOUS les RDV de la période en une seule fois
        List<Rdv> rdvsPeriode = rdvRepository.findByDateBetweenWithEtudeAndVolontaireOptimized(
                Date.valueOf(dateDebut), Date.valueOf(dateFin));
        logger.info("📅 {} RDV trouvés dans la période", rdvsPeriode.size());

        // 3. Créer une map des RDV groupés par idEtude
        Map<Integer, List<Rdv>> rdvsParEtude = rdvsPeriode.stream()
                .filter(rdv -> rdv.getId() != null && rdv.getId().getIdEtude() != null)
                .collect(Collectors.groupingBy(rdv -> rdv.getId().getIdEtude()));

        logger.info("🗂️ RDV groupés par {} études différentes", rdvsParEtude.size());

        // 4. Pour chaque étude, calculer ses dates effectives
        return etudes.stream()
                .map(etude -> {
                    CalendrierDTO.EtudeCalendrierDTO etudeCalendrier = new CalendrierDTO.EtudeCalendrierDTO();

                    // Copier les données de base
                    etudeCalendrier.setId(etude.getIdEtude());
                    etudeCalendrier.setRef(etude.getRef());
                    etudeCalendrier.setTitre(etude.getTitre());
                    etudeCalendrier.setType(etude.getType());

                    // Handle nbSujets conversion
                    Object nbSujets = etude.getNbSujets();
                    if (nbSujets instanceof String) {
                        try {
                            etudeCalendrier.setNbSujets(Integer.valueOf((String) nbSujets));
                        } catch (NumberFormatException e) {
                            logger.warn("Cannot convert nbSujets to Integer: {}", nbSujets);
                            etudeCalendrier.setNbSujets(null);
                        }
                    } else if (nbSujets instanceof Integer) {
                        etudeCalendrier.setNbSujets((Integer) nbSujets);
                    } else {
                        etudeCalendrier.setNbSujets(null);
                    }

                    // Dates théoriques (garder pour référence)
                    if (etude.getDateDebut() != null) {
                        etudeCalendrier.setDateDebut(etude.getDateDebut().toLocalDate());
                    }
                    if (etude.getDateFin() != null) {
                        etudeCalendrier.setDateFin(etude.getDateFin().toLocalDate());
                    }

                    // *** POINT CLÉ : Calculer les dates effectives à partir des RDV ***
                    List<Rdv> rdvsEtude = rdvsParEtude.getOrDefault(etude.getIdEtude(), new ArrayList<>());

                    logger.debug("📊 Étude {} (ID: {}) - {} RDV trouvés",
                            etude.getRef(), etude.getIdEtude(), rdvsEtude.size());

                    if (!rdvsEtude.isEmpty()) {
                        // Extraire les dates uniques des RDV, filtrées par la période
                        List<LocalDate> datesEffectives = rdvsEtude.stream()
                                .map(rdv -> rdv.getDate().toLocalDate())
                                .filter(date -> !date.isBefore(dateDebut) && !date.isAfter(dateFin))
                                .distinct()
                                .sorted()
                                .collect(Collectors.toList());

                        etudeCalendrier.setDatesEffectivesAvecRdv(datesEffectives);

                        // Calculer l'affichage textuel
                        String datesDisplay = formatDatesDisplay(datesEffectives);
                        etudeCalendrier.setDatesAvecRdvDisplay(datesDisplay);

                        logger.debug(" Étude {} - Dates effectives: {}",
                                etude.getRef(), datesEffectives);
                    } else {
                        // Aucun RDV trouvé
                        etudeCalendrier.setDatesEffectivesAvecRdv(new ArrayList<>());
                        etudeCalendrier.setDatesAvecRdvDisplay("Aucun RDV");

                        logger.debug("❌ Étude {} - Aucun RDV dans la période", etude.getRef());
                    }

                    // Calculer le nombre de RDV dans la période demandée
                    int nombreRdv = rdvsEtude.size();
                    etudeCalendrier.setNombreRdv(nombreRdv);
                    etudeCalendrier.setARdvDansPeriode(nombreRdv > 0);

                    return etudeCalendrier;
                })
                .collect(Collectors.toList());
    }

    /**
     * Méthode utilitaire pour formater l'affichage des dates
     */
    private String formatDatesDisplay(List<LocalDate> dates) {
        if (dates == null || dates.isEmpty()) {
            return "Aucun RDV";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH);

        if (dates.size() == 1) {
            return dates.get(0).format(formatter);
        }

        if (dates.size() <= 3) {
            return dates.stream()
                    .map(date -> date.format(formatter))
                    .collect(Collectors.joining(", "));
        }

        // Plus de 3 dates : afficher première, dernière + nombre total
        String premiere = dates.get(0).format(formatter);
        String derniere = dates.get(dates.size() - 1).format(formatter);

        return String.format("%s ... %s (%d jours)", premiere, derniere, dates.size());
    }

    private CalendrierDTO.StatistiquesCalendrierDTO calculerStatistiques(
            List<CalendrierDTO.RendezVousEnrichiDTO> rdvs,
            List<CalendrierDTO.EtudeCalendrierDTO> etudes) {

        CalendrierDTO.StatistiquesCalendrierDTO stats = new CalendrierDTO.StatistiquesCalendrierDTO();

        stats.setTotalRdv(rdvs.size());
        stats.setTotalEtudes(etudes.size());

        // Compter par état
        Map<String, Integer> repartitionEtats = rdvs.stream()
                .collect(Collectors.groupingBy(
                        rdv -> rdv.getEtat() != null ? rdv.getEtat() : "NON_DEFINI",
                        Collectors.reducing(0, e -> 1, Integer::sum)));

        stats.setRdvConfirmes(repartitionEtats.getOrDefault("CONFIRME", 0));
        stats.setRdvEnAttente(repartitionEtats.getOrDefault("EN_ATTENTE", 0));
        stats.setRdvAnnules(repartitionEtats.getOrDefault("ANNULE", 0));
        stats.setRdvCompletes(repartitionEtats.getOrDefault("COMPLETE", 0));

        // Répartition par jour
        Map<String, Integer> repartitionJours = rdvs.stream()
                .filter(rdv -> rdv.getDate() != null)
                .collect(Collectors.groupingBy(
                        rdv -> rdv.getDate().getDayOfWeek().toString(),
                        Collectors.reducing(0, e -> 1, Integer::sum)));
        stats.setRepartitionParJour(repartitionJours);

        // Répartition par heure
        Map<String, Integer> repartitionHeures = rdvs.stream()
                .filter(rdv -> rdv.getHeure() != null)
                .collect(Collectors.groupingBy(
                        rdv -> {
                            try {
                                return rdv.getHeure().substring(0, 2) + "h";
                            } catch (Exception e) {
                                return "Non défini";
                            }
                        },
                        Collectors.reducing(0, e -> 1, Integer::sum)));
        stats.setRepartitionParHeure(repartitionHeures);

        stats.setRepartitionParEtat(repartitionEtats);

        return stats;
    }

    private CalendrierDTO.EtudeMinimalDTO convertirVersEtudeMinimale(Etude etude) {
        CalendrierDTO.EtudeMinimalDTO etudeMin = new CalendrierDTO.EtudeMinimalDTO();
        etudeMin.setId(etude.getIdEtude());
        etudeMin.setRef(etude.getRef());
        etudeMin.setTitre(etude.getTitre());
        etudeMin.setType(etude.getType());

        // FIX: Handle String to Integer conversion
        Object nbSujets = etude.getNbSujets();
        if (nbSujets instanceof String) {
            try {
                etudeMin.setNbSujets(Integer.valueOf((String) nbSujets));
            } catch (NumberFormatException e) {
                logger.warn("Cannot convert nbSujets to Integer: {}", nbSujets);
                etudeMin.setNbSujets(null);
            }
        } else if (nbSujets instanceof Integer) {
            etudeMin.setNbSujets((Integer) nbSujets);
        } else {
            etudeMin.setNbSujets(null);
        }

        if (etude.getDateDebut() != null) {
            etudeMin.setDateDebut(etude.getDateDebut().toLocalDate());
        }
        if (etude.getDateFin() != null) {
            etudeMin.setDateFin(etude.getDateFin().toLocalDate());
        }

        return etudeMin;
    }

    private CalendrierDTO.EtudeMinimalDTO convertirVersEtudeMinimale(EtudeDTO etudeDTO) {
        CalendrierDTO.EtudeMinimalDTO etudeMin = new CalendrierDTO.EtudeMinimalDTO();
        etudeMin.setId(etudeDTO.getIdEtude());
        etudeMin.setRef(etudeDTO.getRef());
        etudeMin.setTitre(etudeDTO.getTitre());
        etudeMin.setType(etudeDTO.getType());

        // FIX: Handle String to Integer conversion for DTO
        Object nbSujets = etudeDTO.getNbSujets();
        if (nbSujets instanceof String) {
            try {
                etudeMin.setNbSujets(Integer.valueOf((String) nbSujets));
            } catch (NumberFormatException e) {
                logger.warn("Cannot convert nbSujets to Integer: {}", nbSujets);
                etudeMin.setNbSujets(null);
            }
        } else if (nbSujets instanceof Integer) {
            etudeMin.setNbSujets((Integer) nbSujets);
        } else {
            etudeMin.setNbSujets(null);
        }

        if (etudeDTO.getDateDebut() != null) {
            etudeMin.setDateDebut(etudeDTO.getDateDebut().toLocalDate());
        }
        if (etudeDTO.getDateFin() != null) {
            etudeMin.setDateFin(etudeDTO.getDateFin().toLocalDate());
        }

        return etudeMin;
    }

    private String calculerStatutTemporel(Date dateRdv) {
        if (dateRdv == null) {
            return "unknown";
        }

        LocalDate aujourdhui = LocalDate.now(); // Fixed character
        LocalDate dateRdvLocal = dateRdv.toLocalDate();

        if (dateRdvLocal.equals(aujourdhui)) {
            return "today";
        } else if (dateRdvLocal.isBefore(aujourdhui)) {
            return "past";
        } else {
            return "upcoming";
        }
    }

    private Map<String, List<CalendrierDTO.RendezVousEnrichiDTO>> organiserRdvsParStatutTemporel(
            List<CalendrierDTO.RendezVousEnrichiDTO> rdvs) {

        return rdvs.stream()
                .collect(Collectors.groupingBy(CalendrierDTO.RendezVousEnrichiDTO::getStatutTemporel));
    }

    private CalendrierDTO.RendezVousEnrichiDTO enrichirRdvUnique(
            RdvDTO rdv,
            Map<Integer, Etude> etudesMap,
            Map<Integer, Volontaire> volontairesMap) {

        CalendrierDTO.RendezVousEnrichiDTO rdvEnrichi = new CalendrierDTO.RendezVousEnrichiDTO();

        // Copier les données de base
        rdvEnrichi.setIdRdv(rdv.getIdRdv());
        rdvEnrichi.setIdEtude(rdv.getIdEtude());
        rdvEnrichi.setIdVolontaire(rdv.getIdVolontaire());
        rdvEnrichi.setIdGroupe(rdv.getIdGroupe());

        if (rdv.getDate() != null) {
            rdvEnrichi.setDate(rdv.getDate());
        }
        rdvEnrichi.setHeure(rdv.getHeure());
        rdvEnrichi.setEtat(rdv.getEtat());
        rdvEnrichi.setCommentaires(rdv.getCommentaires());

        // Calculer le statut temporel
        if (rdv.getDate() != null) {
            rdvEnrichi.setStatutTemporel(calculerStatutTemporel(Date.valueOf(rdv.getDate())));
        } else {
            rdvEnrichi.setStatutTemporel("unknown");
        }

        // Enrichir avec l'étude
        if (rdv.getIdEtude() != null && etudesMap.containsKey(rdv.getIdEtude())) {
            rdvEnrichi.setEtude(convertirVersEtudeMinimale(etudesMap.get(rdv.getIdEtude())));
        }

        // Enrichir avec le volontaire
        if (rdv.getIdVolontaire() != null && volontairesMap.containsKey(rdv.getIdVolontaire())) {
            Volontaire volontaire = volontairesMap.get(rdv.getIdVolontaire());
            CalendrierDTO.VolontaireMinimalDTO volontaireMin = new CalendrierDTO.VolontaireMinimalDTO();
            volontaireMin.setId(volontaire.getIdVol());
            volontaireMin.setNom(volontaire.getNomVol());
            volontaireMin.setPrenom(volontaire.getPrenomVol());
            volontaireMin.setTitre(volontaire.getTitreVol());
            if (volontaire.getDateNaissance() != null) {
                volontaireMin.setDateNaissance(volontaire.getDateNaissance());
            }
            rdvEnrichi.setVolontaire(volontaireMin);
        }

        return rdvEnrichi;
    }

    private Map<LocalDate, List<String>> genererCreneauxLibres(
            LocalDate dateDebut, LocalDate dateFin,
            String heureDebut, String heureFin,
            List<Rdv> rdvsExistants) {

        Map<LocalDate, List<String>> creneauxLibres = new HashMap<>();

        // Générer tous les créneaux de 30 minutes entre heureDebut et heureFin
        List<String> creneauxPossibles = genererCreneaux(heureDebut, heureFin, 30);

        // Pour chaque jour de la période
        LocalDate dateCourante = dateDebut;
        while (!dateCourante.isAfter(dateFin)) {
            List<String> creneauxLibresJour = new ArrayList<>(creneauxPossibles);

            // Retirer les créneaux occupés
            Date dateCouranteSQL = Date.valueOf(dateCourante);
            rdvsExistants.stream()
                    .filter(rdv -> rdv.getDate().equals(dateCouranteSQL))
                    .forEach(rdv -> {
                        String heureRdv = rdv.getHeure();
                        if (heureRdv != null) {
                            // Retirer le créneau et celui d'après (durée estimée 1h)
                            creneauxLibresJour.remove(heureRdv);
                            String heureApres = calculerHeureApres(heureRdv, 30);
                            creneauxLibresJour.remove(heureApres);
                        }
                    });

            creneauxLibres.put(dateCourante, creneauxLibresJour);
            dateCourante = dateCourante.plusDays(1);
        }

        return creneauxLibres;
    }

    private List<String> genererCreneaux(String heureDebut, String heureFin, int intervalleMinutes) {
        List<String> creneaux = new ArrayList<>();

        try {
            int heureDebutInt = Integer.parseInt(heureDebut.split(":")[0]);
            int minuteDebutInt = Integer.parseInt(heureDebut.split(":")[1]);
            int heureFinInt = Integer.parseInt(heureFin.split(":")[0]);
            int minuteFinInt = Integer.parseInt(heureFin.split(":")[1]);

            int minutesDebut = heureDebutInt * 60 + minuteDebutInt;
            int minutesFin = heureFinInt * 60 + minuteFinInt;

            for (int minutes = minutesDebut; minutes < minutesFin; minutes += intervalleMinutes) {
                int heure = minutes / 60;
                int minute = minutes % 60;
                creneaux.add(String.format("%02d:%02d", heure, minute));
            }

        } catch (Exception e) {
            logger.warn("Erreur lors de la génération des créneaux", e);
        }

        return creneaux;
    }

    private String calculerHeureApres(String heure, int minutesAjouter) {
        try {
            String[] parts = heure.split(":");
            int heureInt = Integer.parseInt(parts[0]);
            int minuteInt = Integer.parseInt(parts[1]);

            int totalMinutes = heureInt * 60 + minuteInt + minutesAjouter;
            int nouvelleHeure = (totalMinutes / 60) % 24;
            int nouvelleMinute = totalMinutes % 60;

            return String.format("%02d:%02d", nouvelleHeure, nouvelleMinute);

        } catch (Exception e) {
            logger.warn("Erreur lors du calcul de l'heure après", e);
            return heure;
        }
    }

    private Map<LocalDate, Double> calculerTauxUtilisationParJour(LocalDate dateDebut, LocalDate dateFin) {
        Map<LocalDate, Double> taux = new HashMap<>();

        // Supposons 8 créneaux par jour (8h-12h, 14h-18h)
        int creneauxMaxParJour = 8;

        LocalDate dateCourante = dateDebut;
        while (!dateCourante.isAfter(dateFin)) {
            int rdvsJour = rdvRepository.countByDate(Date.valueOf(dateCourante));
            double tauxUtilisation = (double) rdvsJour / creneauxMaxParJour * 100;
            taux.put(dateCourante, Math.min(tauxUtilisation, 100.0));

            dateCourante = dateCourante.plusDays(1);
        }

        return taux;
    }

    private Map<String, Object> calculerPredictionsSimples(List<Map<String, Object>> evolutionRdv) {
        Map<String, Object> predictions = new HashMap<>();

        if (evolutionRdv.size() < 2) {
            predictions.put("tendance", "INSUFFISANT");
            return predictions;
        }

        // Calcul simple de la tendance
        int premiereDonnee = (Integer) evolutionRdv.get(0).get("count");
        int derniereDonnee = (Integer) evolutionRdv.get(evolutionRdv.size() - 1).get("count");

        double tendance = ((double) derniereDonnee - premiereDonnee) / premiereDonnee * 100;

        if (tendance > 10) {
            predictions.put("tendance", "HAUSSE");
        } else if (tendance < -10) {
            predictions.put("tendance", "BAISSE");
        } else {
            predictions.put("tendance", "STABLE");
        }

        predictions.put("pourcentageTendance", Math.round(tendance * 100) / 100.0);

        // Prédiction simple pour la semaine suivante
        int moyenneRecente = evolutionRdv.stream()
                .skip(Math.max(0, evolutionRdv.size() - 3))
                .mapToInt(m -> (Integer) m.get("count"))
                .sum() / Math.min(3, evolutionRdv.size());

        predictions.put("previsionSemaineProchaine", moyenneRecente);

        return predictions;
    }

    // Ajouter cette méthode dans CalendrierServiceImpl.java

    @Override
    public String getEtudeRdvDatesDisplay(Integer idEtude) {
        logger.debug("Récupération dates RDV pour étude {}", idEtude);

        try {
            return etudeRepository.getEtudeRdvDatesDisplay(idEtude);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des dates RDV pour l'étude {}", idEtude, e);
            return "Erreur dates";
        }
    }

// ==================== NOUVELLES MÉTHODES À AJOUTER DANS CalendrierServiceImpl.java ====================
    // À placer juste après la méthode getRdvsEtudeAvecDetails() existante

    @Override
    public Map<String, Object> getRdvsEtudeAvecDateSelectionnee(Integer idEtude, LocalDate dateSelectionnee, int page, int taille) {
        logger.info("🎯 Récupération RDV étude {} avec focus sur la date {} (page {}, taille {})",
                idEtude, dateSelectionnee, page, taille);

        try {
            // Récupération de l'étude
            Optional<EtudeDTO> etudeOpt = etudeService.getEtudeById(idEtude);
            if (etudeOpt.isEmpty()) {
                throw new IllegalArgumentException("Étude non trouvée: " + idEtude);
            }

            // Récupération de TOUS les RDV de l'étude (sans pagination pour permettre le tri)
            List<Rdv> tousLesRdvs = rdvRepository.findById_IdEtudeOrderByDateDesc(idEtude);
            
            // Collecter tous les IDs de volontaires uniques pour optimisation
            Set<Integer> idsVolontaires = tousLesRdvs.stream()
                    .map(Rdv::getIdVolontaire)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Récupérer tous les volontaires en une seule fois
            Map<Integer, Volontaire> volontairesMap = volontaireRepository.findAllById(idsVolontaires)
                    .stream()
                    .collect(Collectors.toMap(Volontaire::getIdVol, volontaire -> volontaire));

            // Convertir tous les RDV en DTO enrichis
            List<CalendrierDTO.RendezVousEnrichiDTO> tousLesRdvsEnrichis = tousLesRdvs.stream()
                    .map(rdv -> convertirVersRdvEnrichiAvecVolontaires(rdv, volontairesMap))
                    .collect(Collectors.toList());

            // Séparer les RDV de la date sélectionnée des autres
            List<CalendrierDTO.RendezVousEnrichiDTO> rdvsDateSelectionnee = tousLesRdvsEnrichis.stream()
                    .filter(rdv -> rdv.getDate() != null && rdv.getDate().equals(dateSelectionnee))
                    .sorted((a, b) -> {
                        // Trier par heure les RDV de la date sélectionnée
                        String heureA = a.getHeure() != null ? a.getHeure() : "00:00";
                        String heureB = b.getHeure() != null ? b.getHeure() : "00:00";
                        return heureA.compareTo(heureB);
                    })
                    .collect(Collectors.toList());

            List<CalendrierDTO.RendezVousEnrichiDTO> autresRdvs = tousLesRdvsEnrichis.stream()
                    .filter(rdv -> rdv.getDate() == null || !rdv.getDate().equals(dateSelectionnee))
                    .collect(Collectors.toList());

            // Organiser les autres RDV par statut temporel
            Map<String, List<CalendrierDTO.RendezVousEnrichiDTO>> autresRdvsParCategorie = organiserRdvsParStatutTemporel(autresRdvs);

            // Construire la structure finale
            Map<String, List<CalendrierDTO.RendezVousEnrichiDTO>> rdvsOrganises = new HashMap<>();
            rdvsOrganises.put("selectedDate", rdvsDateSelectionnee);
            rdvsOrganises.putAll(autresRdvsParCategorie);

            // Calculer les métadonnées
            int totalRdvs = tousLesRdvsEnrichis.size();
            int rdvsDateSelectionneeTaille = rdvsDateSelectionnee.size();

            Map<String, Object> resultat = new HashMap<>();
            resultat.put("etude", convertirVersEtudeMinimale(etudeOpt.get()));
            resultat.put("rdvs", rdvsOrganises);
            resultat.put("dateSelectionnee", dateSelectionnee);
            resultat.put("statistiques", Map.of(
                    "totalRdvs", totalRdvs,
                    "rdvsDateSelectionnee", rdvsDateSelectionneeTaille,
                    "autresRdvs", totalRdvs - rdvsDateSelectionneeTaille,
                    "past", autresRdvsParCategorie.getOrDefault("past", Collections.emptyList()).size(),
                    "today", autresRdvsParCategorie.getOrDefault("today", Collections.emptyList()).size(),
                    "upcoming", autresRdvsParCategorie.getOrDefault("upcoming", Collections.emptyList()).size()
            ));

            logger.info(" RDV étude {} organisés: {} pour {}, {} autres (past: {}, today: {}, upcoming: {})",
                    idEtude, rdvsDateSelectionneeTaille, dateSelectionnee,
                    totalRdvs - rdvsDateSelectionneeTaille,
                    autresRdvsParCategorie.getOrDefault("past", Collections.emptyList()).size(),
                    autresRdvsParCategorie.getOrDefault("today", Collections.emptyList()).size(),
                    autresRdvsParCategorie.getOrDefault("upcoming", Collections.emptyList()).size());

            return resultat;

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des RDV de l'étude {} avec date sélectionnée {}", idEtude, dateSelectionnee, e);
            throw new RuntimeException("Erreur lors de la récupération des RDV avec date sélectionnée", e);
        }
    }

    @Override
    public Map<String, Object> getRdvsEtudeParDateSpecifique(Integer idEtude, LocalDate date) {
        logger.info("📅 Récupération RDV étude {} pour la date spécifique {}", idEtude, date);

        try {
            // Récupération de l'étude
            Optional<EtudeDTO> etudeOpt = etudeService.getEtudeById(idEtude);
            if (etudeOpt.isEmpty()) {
                throw new IllegalArgumentException("Étude non trouvée: " + idEtude);
            }

            // Récupération optimisée des RDV de la date spécifique uniquement
            Date dateSQL = Date.valueOf(date);
            
            // Utiliser une méthode simple en attendant la nouvelle méthode du repository
            List<Rdv> tousLesRdvs = rdvRepository.findById_IdEtudeOrderByDateDesc(idEtude);
            List<Rdv> rdvsDuJour = tousLesRdvs.stream()
                    .filter(rdv -> rdv.getDate() != null && rdv.getDate().equals(dateSQL))
                    .sorted((a, b) -> {
                        // Trier par heure
                        String heureA = a.getHeure() != null ? a.getHeure() : "00:00";
                        String heureB = b.getHeure() != null ? b.getHeure() : "00:00";
                        return heureA.compareTo(heureB);
                    })
                    .collect(Collectors.toList());

            // Collecter les IDs de volontaires
            Set<Integer> idsVolontaires = rdvsDuJour.stream()
                    .map(Rdv::getIdVolontaire)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            // Récupérer les volontaires
            Map<Integer, Volontaire> volontairesMap = volontaireRepository.findAllById(idsVolontaires)
                    .stream()
                    .collect(Collectors.toMap(Volontaire::getIdVol, volontaire -> volontaire));

            // Enrichir les RDV
            List<CalendrierDTO.RendezVousEnrichiDTO> rdvsEnrichis = rdvsDuJour.stream()
                    .map(rdv -> convertirVersRdvEnrichiAvecVolontaires(rdv, volontairesMap))
                    .collect(Collectors.toList());

            Map<String, Object> resultat = new HashMap<>();
            resultat.put("etude", convertirVersEtudeMinimale(etudeOpt.get()));
            resultat.put("date", date);
            resultat.put("rdvs", rdvsEnrichis);
            resultat.put("nombreRdvs", rdvsEnrichis.size());

            logger.info(" {} RDV trouvés pour l'étude {} le {}", rdvsEnrichis.size(), idEtude, date);

            return resultat;

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des RDV de l'étude {} pour la date {}", idEtude, date, e);
            throw new RuntimeException("Erreur lors de la récupération des RDV par date spécifique", e);
        }
    }
}