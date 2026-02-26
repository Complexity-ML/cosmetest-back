package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.EtudeDTO;
import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.domain.model.RdvId;
import com.example.cosmetest.business.dto.PaginatedResponse;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.RdvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Contrôleur REST pour la gestion des rendez-vous
 * avec intégration pour les recherches optimisées
 */
@RestController
@RequestMapping("/api/rdvs")
public class RdvController {

    private static final Logger logger = LoggerFactory.getLogger(RdvController.class);

    @Autowired
    private RdvService rdvService;

    @Autowired
    private EtudeService etudeService;


    /**
     * Récupère les rendez-vous avec pagination
     */
    @GetMapping("/paginated")
    public ResponseEntity<PaginatedResponse<RdvDTO>> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "date,desc") String sort) {

        // Limiter la taille de page pour des raisons de performance
        int limitedSize = Math.min(size, 50);

        // Créer l'objet Pageable à partir des paramètres
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, limitedSize, Sort.by(direction, sortParams[0]));

        // Récupérer les données paginées
        Page<RdvDTO> rdvPage = rdvService.getAllRdvsPaginated(pageable);

        // Construire la réponse paginée
        PaginatedResponse<RdvDTO> response = new PaginatedResponse<>(
                rdvPage.getContent(),
                rdvPage.getNumber(),
                rdvPage.getSize(),
                rdvPage.getTotalElements(),
                rdvPage.getTotalPages()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Recherche par référence d'étude
     */
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<RdvDTO>> search(
            @RequestParam(required = false) String etudeRef,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) Integer idEtude, // Ajout du paramètre idEtude
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "date,desc") String sort) {

        int limitedSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, limitedSize, Sort.by(sort.split(",")[0]).descending());

        // Cas spécifique: recherche par ID d'étude
        if (idEtude != null && idEtude > 0) {
            List<RdvDTO> rdvs = rdvService.getRdvsByIdEtude(idEtude);

            int start = Math.min(page * limitedSize, rdvs.size());
            int end = Math.min((page + 1) * limitedSize, rdvs.size());

            if (start >= rdvs.size()) {
                start = 0;
            }
            if (end < start) {
                end = start;
            }

            List<RdvDTO> pageRdvs = rdvs.isEmpty() ? rdvs : rdvs.subList(start, end);

            return ResponseEntity.ok(new PaginatedResponse<>(
                    pageRdvs,
                    page,
                    limitedSize,
                    rdvs.size(),
                    (int) Math.ceil((double) rdvs.size() / limitedSize)
            ));
        }

        // Le reste de votre code existant pour les autres cas de recherche
        if (etudeRef != null && !etudeRef.trim().isEmpty()) {
            // Recherche par référence d'étude...
        }

        // Gestion des autres types de recherche...
        Page<RdvDTO> resultPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            resultPage = new PageImpl<>(rdvService.searchRdvsByCommentaires(keyword), pageable, size);
        } else if (date != null && !date.trim().isEmpty()) {
            resultPage = new PageImpl<>(rdvService.getRdvsByDate(Date.valueOf(date)), pageable, size);
        } else if (etat != null && !etat.trim().isEmpty()) {
            resultPage = new PageImpl<>(rdvService.getRdvsByEtat(etat.trim().toUpperCase()), pageable, size);
        } else {
            resultPage = rdvService.getAllRdvsPaginated(pageable);
        }

        return ResponseEntity.ok(new PaginatedResponse<>(
                resultPage.getContent(),
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages()
        ));
    }


    /**
     * Récupère un rendez-vous spécifique
     */
    @GetMapping("/{idEtude}/{idRdv}")
    public ResponseEntity<RdvDTO> getById(
            @PathVariable Integer idEtude,
            @PathVariable Integer idRdv)
            //@RequestParam(required = false) Integer sequence) 
            {

        RdvId rdvId;
        //if (sequence != null) {
        //    rdvId = new RdvId(idEtude, idRdv, sequence);
        //} else {
            rdvId = new RdvId(idEtude, idRdv);
        //}

        Optional<RdvDTO> rdvOpt = rdvService.getRdvById(rdvId);

        if (rdvOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(rdvOpt.get());
    }

    /**
     * Met à jour uniquement le statut d'un rendez-vous
     */
    @PatchMapping("/{idEtude}/{idRdv}/etat")
    public ResponseEntity<RdvDTO> updateStatus(
            @PathVariable Integer idEtude,
            @PathVariable Integer idRdv,
            //@PathVariable Integer sequence,
            @RequestParam String nouvelEtat) {
    
        RdvId rdvId = new RdvId(idEtude, idRdv);
        Optional<RdvDTO> rdvOpt = rdvService.getRdvById(rdvId);
    
        if (rdvOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    
        rdvService.updateRdvEtat(rdvId, nouvelEtat);
    
        // Récupérer le RDV mis à jour
        rdvOpt = rdvService.getRdvById(rdvId);
        return ResponseEntity.ok(rdvOpt.get());
    }

    /**
     * Met à jour un rendez-vous
     */
    @PutMapping("/{idEtude}/{idRdv}")
    public ResponseEntity<?> updateRdv(@PathVariable Integer idEtude,
                                       @PathVariable Integer idRdv,
                                       @RequestBody RdvDTO rdvDTO) {
        // Ensure IDs in path match those in the DTO
        rdvDTO.setIdEtude(idEtude);
        rdvDTO.setIdRdv(idRdv);

        // Check if the record exists
        RdvId rdvId = new RdvId(idEtude, idRdv);
        Optional<RdvDTO> existingRdvOpt = rdvService.getRdvById(rdvId);
        if (!existingRdvOpt.isPresent()) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "No rendezvous found with ID: " + rdvId));
        }

        List<String> warnings = new ArrayList<>();

        // Vérifier le chevauchement seulement si on assigne un volontaire
        if (rdvDTO.getIdVolontaire() != null) {
            Integer previousVolontaireId = existingRdvOpt.get().getIdVolontaire();
            // Vérifier seulement si c'est une nouvelle assignation (volontaire différent)
            if (!Objects.equals(rdvDTO.getIdVolontaire(), previousVolontaireId)) {
                if (checkForOverlappingStudies(rdvDTO.getIdVolontaire(), idEtude)) {
                    warnings.add("Le volontaire participe déjà à une étude dont la période chevauche celle-ci.");
                }
            }
        }

        rdvService.updateRdv(rdvDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        if (!warnings.isEmpty()) {
            response.put("warnings", warnings);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Supprime un rendez-vous
     */
    @DeleteMapping("/{idEtude}/{idRdv}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer idEtude,
            @PathVariable Integer idRdv) {

        RdvId rdvId = new RdvId(idEtude, idRdv);
        rdvService.deleteRdv(rdvId);
        return ResponseEntity.ok().build();
    }

    /**
     * Récupère les études avec nombre de rendez-vous
     */
    @GetMapping("/etudes/with-rdv-count")
    public ResponseEntity<List<Map<String, Object>>> getEtudesWithRdvCount() {
        // Adapter cette méthode selon votre interface EtudeService réelle
        // C'est un exemple qui doit être adapté
        List<Map<String, Object>> result = new ArrayList<>();
        return ResponseEntity.ok(result);
    }


    // Helper method
    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
    }

    /**
     * Récupère les rendez-vous d'un volontaire spécifique
     */
    @GetMapping("/by-volontaire/{idVolontaire}")
    public ResponseEntity<List<RdvDTO>> getByVolontaireId(@PathVariable Integer idVolontaire) {
        List<RdvDTO> rdvs = rdvService.getRdvsByIdVolontaire(idVolontaire);
        return ResponseEntity.ok(rdvs);
    }


    /**
     * Récupère les études avec leur nombre de rendez-vous
     */
    @GetMapping("/studies-with-count")
    public ResponseEntity<PaginatedResponse<Map<String, Object>>> getStudiesWithRdvCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "ref,asc") String sort,
            @RequestParam(defaultValue = "false") boolean sortByLatestRdvDate) {

        int limitedSize = Math.min(size, 50);
        List<Map<String, Object>> studiesWithCount = new ArrayList<>();

        try {
            // Initialiser la liste des études
            List<EtudeDTO> etudes;
            int totalElements;
            int totalPages;

            if (query != null && !query.trim().isEmpty()) {
                // Recherche par référence exacte d'abord
                Optional<EtudeDTO> specificEtude = etudeService.getEtudeByRef(query.trim());

                if (specificEtude.isPresent()) {
                    // Si une étude est trouvée par référence exacte
                    etudes = Collections.singletonList(specificEtude.get());
                    totalElements = 1;
                    totalPages = 1;
                } else {
                    // Sinon, recherche générique
                    etudes = etudeService.searchEtudes(query);
                    totalElements = etudes.size();
                    totalPages = (int) Math.ceil((double) totalElements / limitedSize);
                }

                // Si le tri par date de RDV est demandé
                if (sortByLatestRdvDate) {
                    // Enrichir chaque étude avec sa date la plus récente
                    for (Map<String, Object> study : studiesWithCount) {
                        @SuppressWarnings("unchecked")
                        List<RdvDTO> rdvs = (List<RdvDTO>) study.get("rdvs");
                        Date latestDate = null;

                        if (rdvs != null && !rdvs.isEmpty()) {
                            for (RdvDTO rdv : rdvs) {
                                try {
                                    Date rdvDate = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(rdv.getDate()));
                                    if (latestDate == null || rdvDate.after(latestDate)) {
                                        latestDate = rdvDate;
                                    }
                                } catch (Exception e) {
                                    // Gestion des erreurs de date
                                }
                            }
                        }

                        // Ajouter la date la plus récente à l'étude
                        study.put("latestRdvDate", latestDate);
                    }

                    // Trier les études par date la plus récente
                    studiesWithCount.sort((study1, study2) -> {
                        Date date1 = (Date) study1.get("latestRdvDate");
                        Date date2 = (Date) study2.get("latestRdvDate");

                        if (date1 == null && date2 == null) return 0;
                        if (date1 == null) return 1;  // null en dernier
                        if (date2 == null) return -1; // null en dernier

                        return date2.compareTo(date1); // ordre décroissant
                    });
                }

            } else {
                // Récupérer toutes les études paginées
                Pageable pageable = createPageable(page, limitedSize, sort);
                Page<EtudeDTO> etudePage = etudeService.getAllEtudesPaginated(pageable);
                etudes = etudePage.getContent();
                totalElements = (int) etudePage.getTotalElements();
                totalPages = etudePage.getTotalPages();
            }

            // Limiter les résultats à la page actuelle
            int start = page * limitedSize;
            // Vérifier que start n'est pas supérieur à la taille de la liste
            if (start >= etudes.size()) {
                start = 0;
                page = 0;
            }
            int end = Math.min(start + limitedSize, etudes.size());
            // Vérifier que end n'est pas inférieur à start
            if (end < start) {
                end = start;
            }
            List<EtudeDTO> pageEtudes = etudes.subList(start, end);

            for (EtudeDTO etude : pageEtudes) {
                Map<String, Object> studyMap = new HashMap<>();
                studyMap.put("id", etude.getIdEtude());
                studyMap.put("ref", etude.getRef());
                studyMap.put("titre", etude.getTitre());

                // Récupérer les RDVs pour cette étude
                List<RdvDTO> rdvs = rdvService.getRdvsByIdEtude(etude.getIdEtude());
                int rdvCount = rdvs.size();

                studyMap.put("rdvCount", rdvCount);
                studyMap.put("rdvs", rdvs);

                studiesWithCount.add(studyMap);
            }

            PaginatedResponse<Map<String, Object>> response = new PaginatedResponse<>(
                    studiesWithCount,
                    page,
                    limitedSize,
                    totalElements,
                    totalPages
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log détaillé de l'erreur
            logger.error("Erreur lors de la récupération des études", e);

            return ResponseEntity.ok(new PaginatedResponse<>(
                    new ArrayList<>(),
                    page,
                    limitedSize,
                    0,
                    0
            ));
        }
    }

    /**
     * Crée un ou plusieurs rendez-vous avec vérification de chevauchement des périodes d'études
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody RdvDTO rdvDTO) {
        List<RdvDTO> createdRdvs = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int totalToCreate = 1;

        // Get the single volunteer ID
        Integer idVolontaire = rdvDTO.getIdVolontaire();

        // CAS SPÉCIAL : idVolontaire est null - Création d'un RDV sans volontaire
        if (idVolontaire == null) {
            try {
                // Créer un RDV sans volontaire associé
                RdvDTO created = rdvService.createRdv(rdvDTO);
                createdRdvs.add(created);
            } catch (Exception e) {
                String errorMsg = "Erreur lors de la création du RDV sans volontaire: " + e.getMessage();
                logger.error("Erreur lors de la création du RDV sans volontaire", e);
                errors.add(errorMsg);
            }
        }
        else {
            try {
                // 1. Vérifier si le volontaire a déjà un RDV pour cette étude
                boolean hasExistingRdv = rdvService.hasVolontaireRdvForEtude(
                        idVolontaire,
                        rdvDTO.getIdEtude()
                );

                if (hasExistingRdv) {
                    errors.add("Le volontaire a déjà un rendez-vous pour cette étude.");
                } else {
                    // 2. Vérifier si le volontaire participe à d'autres études dont les périodes chevauchent celle-ci
                    boolean hasOverlappingStudy = checkForOverlappingStudies(
                            idVolontaire,
                            rdvDTO.getIdEtude()
                    );

                    if (hasOverlappingStudy) {
                        errors.add("Le volontaire participe déjà à une étude dont la période chevauche celle-ci.");
                    } else {
                        // Si aucun chevauchement, créer le RDV
                        RdvDTO created = rdvService.createRdv(rdvDTO);
                        createdRdvs.add(created);
                    }
                }
            } catch (Exception e) {
                String errorMsg = "Erreur lors de la création du RDV: " + e.getMessage();
                logger.error("Erreur lors de la création du RDV", e);
                errors.add(errorMsg);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("created", createdRdvs.size());
        response.put("total", totalToCreate);
        response.put("rdvs", createdRdvs);

        if (!errors.isEmpty()) {
            response.put("errors", errors);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Crée plusieurs rendez-vous en lot (batch)
     * Optimisé pour les créations en masse depuis l'interface de création batch
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> createBatch(@RequestBody List<RdvDTO> rdvDTOs) {
        List<RdvDTO> createdRdvs = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        int totalToCreate = rdvDTOs.size();

        Map<String, Object> response = new HashMap<>();

        if (rdvDTOs.isEmpty()) {
            response.put("created", 0);
            response.put("total", 0);
            response.put("rdvs", createdRdvs);
            response.put("errors", List.of("Aucun rendez-vous à créer"));
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Utiliser la méthode batch du service pour une transaction unique
            List<RdvDTO> batchResult = rdvService.createRdvsBatch(rdvDTOs);
            createdRdvs.addAll(batchResult);

            logger.info("Batch création réussie : {} RDV créés sur {} demandés", createdRdvs.size(), totalToCreate);

        } catch (Exception e) {
            String errorMsg = "Erreur lors de la création batch : " + e.getMessage();
            logger.error("Erreur lors de la création batch", e);
            errors.add(errorMsg);
        }

        response.put("created", createdRdvs.size());
        response.put("total", totalToCreate);
        response.put("rdvs", createdRdvs);

        if (!errors.isEmpty()) {
            response.put("errors", errors);
        }

        logger.info("Résultat batch final : {} créés / {} demandés", createdRdvs.size(), totalToCreate);

        return ResponseEntity.ok(response);
    }

    /**
     * Vérifie si un volontaire participe à des études dont les périodes chevauchent celle de l'étude spécifiée
     *
     * @param idVolontaire L'ID du volontaire
     * @param idEtude L'ID de l'étude à vérifier
     * @return true s'il y a chevauchement, false sinon
     */
    private boolean checkForOverlappingStudies(Integer idVolontaire, Integer idEtude) {
        if (idVolontaire == null || idEtude == null) {
            return false; // Données insuffisantes pour vérifier
        }

        try {
            // 1. Récupérer les informations de l'étude actuelle
            Optional<EtudeDTO> currentStudyOpt = etudeService.getEtudeById(idEtude);
            if (!currentStudyOpt.isPresent()) {
                logger.error("Étude non trouvée: {}", idEtude);
                return false;
            }

            EtudeDTO currentStudy = currentStudyOpt.get();
            Date currentStartDate = currentStudy.getDateDebut();
            Date currentEndDate = currentStudy.getDateFin();

            // Si les dates ne sont pas définies, on ne peut pas vérifier le chevauchement
            if (currentStartDate == null || currentEndDate == null) {
                return false;
            }

            // 2. Récupérer toutes les études auxquelles le volontaire participe
            List<EtudeDTO> volunteerStudies = etudeService.getEtudesByVolontaire(idVolontaire);

            // 3. Vérifier les chevauchements avec chaque étude
            for (EtudeDTO study : volunteerStudies) {
                // Ignorer l'étude courante
                if (Objects.equals(study.getIdEtude(), idEtude)) {
                    continue;
                }

                Date studyStartDate = study.getDateDebut();
                Date studyEndDate = study.getDateFin();

                // Si les dates ne sont pas définies pour cette étude, on passe à la suivante
                if (studyStartDate == null || studyEndDate == null) {
                    continue;
                }

                // Vérifier le chevauchement des périodes
                // (Nouvelle début <= Existante fin) ET (Nouvelle fin >= Existante début)
                if (currentStartDate.compareTo(studyEndDate) <= 0 &&
                        currentEndDate.compareTo(studyStartDate) >= 0) {
                    // Périodes qui se chevauchent trouvées
                    logger.info("Chevauchement détecté entre l'étude {} ({} - {}) et l'étude {} ({} - {})",
                            idEtude, currentStartDate, currentEndDate, study.getIdEtude(), studyStartDate, studyEndDate);
                    return true;
                }
            }

            return false; // Pas de chevauchement
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification des chevauchements d'études", e);
            return false; // En cas d'erreur, on permet la création
        }
    }
}