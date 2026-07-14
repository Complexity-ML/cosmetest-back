package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.mapper.RdvMapper;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.business.service.RdvService;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.EtudeRepository;
import com.example.cosmetest.data.repository.GroupeRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Groupe;
import com.example.cosmetest.domain.model.Rdv;


import jakarta.persistence.EntityNotFoundException;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.example.cosmetest.domain.model.Etude;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation de la couche business (BLL) pour les rendez-vous
 * Contient toute la logique métier liée aux rendez-vous
 */
@Service
public class RdvServiceImpl implements RdvService {

    private static final Logger logger = LoggerFactory.getLogger(RdvServiceImpl.class);

    private final RdvRepository rdvRepository;
    private final RdvMapper rdvMapper;
    private final AnnulationRepository annulationRepository;
    private final EtudeRepository etudeRepository;
    private final EtudeVolontaireService etudeVolontaireService;
    private final GroupeRepository groupeRepository;
    private final RdvIdAllocator rdvIdAllocator;

    public RdvServiceImpl(RdvRepository rdvRepository, RdvMapper rdvMapper, @Lazy EtudeRepository etudeRepository,
            AnnulationRepository annulationRepository,
            EtudeVolontaireService etudeVolontaireService,
            GroupeRepository groupeRepository,
            RdvIdAllocator rdvIdAllocator) {
        this.rdvRepository = rdvRepository;
        this.rdvMapper = rdvMapper;
        this.etudeRepository = etudeRepository;
        this.annulationRepository = annulationRepository;
        this.etudeVolontaireService = etudeVolontaireService;
        this.groupeRepository = groupeRepository;
        this.rdvIdAllocator = rdvIdAllocator;
    }

    @Override
    public List<RdvDTO> getAllRdvs() {
        return rdvRepository.findAll().stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RdvDTO> getAllRdvsPaginated(Pageable pageable) {
        return rdvRepository.findOperationalRdvs(pageable)
                .map(rdvMapper::toDto);
    }

    @Override
    public Optional<RdvDTO> getRdvById(Long rdvPk) {
        return rdvRepository.findById(rdvPk)
                .map(rdvMapper::toDto);
    }

    @Override
    public Optional<RdvDTO> getRdvByBusinessKey(Integer idEtude, Integer idRdv) {
        return rdvRepository.findByIdEtudeAndIdRdv(idEtude, idRdv)
                .map(rdvMapper::toDto);
    }

    @Override
    public List<RdvDTO> getRdvsByVolontaire(Integer idVolontaire) {
        return rdvRepository.findByIdVolontaire(idVolontaire).stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByDate(Date date) {
        return rdvRepository.findByDate(date).stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByVolontaireAndDate(Integer idVolontaire, Date date) {
        return rdvRepository.findByIdVolontaireAndDate(idVolontaire, date).stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByVolontaireAndDateRange(Integer idVolontaire, Date startDate, Date endDate) {
        return rdvRepository.findByVolontaireAndDateRange(idVolontaire, startDate, endDate).stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByGroupe(Integer idGroupe) {
        return rdvRepository.findByIdGroupe(idGroupe).stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByEtat(String etat) {
        if (isCancelledEtat(etat)) {
            return List.of();
        }
        return rdvRepository.findByEtat(etat).stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RdvDTO saveRdv(RdvDTO rdvDTO) {
        // Convertir DTO en entité
        Rdv rdvEntity = rdvMapper.toEntity(rdvDTO);

        // Appliquer des règles métier avant la sauvegarde si nécessaire
        validateRdv(rdvEntity);

        ensureEtudeVolontaireAssociation(
                rdvDTO.getIdEtude(), rdvDTO.getIdGroupe(), rdvDTO.getIdVolontaire());

        // Sauvegarder l'entité
        Rdv savedRdv = rdvRepository.save(rdvEntity);

        // Convertir l'entité sauvegardée en DTO
        return rdvMapper.toDto(savedRdv);
    }

    @Override
    @Transactional
    public void deleteRdv(Long rdvPk) {
        // Vérifier si le rendez-vous existe avant de le supprimer
        if (rdvRepository.existsById(rdvPk)) {
            rdvRepository.deleteById(rdvPk);
        } else {
            throw new IllegalArgumentException("Le rendez-vous avec l'ID technique " + rdvPk + " n'existe pas");
        }
    }

    @Override
    public List<RdvDTO> searchRdvsByCommentaires(String keyword) {
        return rdvRepository.findByCommentairesContaining(keyword).stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateRdvEtat(Long rdvPk, String nouvelEtat) {
        // Récupérer le rendez-vous
        Rdv rdv = rdvRepository.findById(rdvPk)
                .orElseThrow(() -> new IllegalArgumentException("Le rendez-vous avec l'ID technique " + rdvPk + " n'existe pas"));

        // Vérifier que l'état est valide
        validateEtat(nouvelEtat);

        // Mettre à jour l'état
        rdv.setEtat(nouvelEtat);

        // Sauvegarder le rendez-vous
        rdvRepository.save(rdv);
    }

    // Méthodes privées pour la logique métier interne

    /**
     * Valide les données d'un rendez-vous avant sauvegarde
     *
     * @param rdv Rendez-vous à valider
     */
    private void validateRdv(Rdv rdv) {
        // Exemple de règle métier : vérifier que la date n'est pas nulle
        if (rdv.getDate() == null) {
            throw new IllegalArgumentException("La date du rendez-vous ne peut pas être nulle");
        }

        // Exemple de règle métier : vérifier que l'état est valide
        if (rdv.getEtat() != null) {
            validateEtat(rdv.getEtat());
        }
    }

    /**
     * Valide que l'état est une valeur autorisée
     *
     * @param etat État à valider
     */
    private void validateEtat(String etat) {
        // Exemple : liste des états autorisés
        List<String> etatsAutorises = List.of("PLANIFIE", "CONFIRME", "ANNULE", "TERMINE", "REPORTE");

        if (!etatsAutorises.contains(etat.toUpperCase())) {
            throw new IllegalArgumentException("État non valide: " + etat +
                    ". Les états autorisés sont: " + String.join(", ", etatsAutorises));
        }
    }

    @Override
    public List<RdvDTO> getRecentRdvs(int limit) {
        // Implémentation temporaire
        List<Rdv> rdvs = rdvRepository.findAll().stream()
                .filter(this::isOperationalRdv)
                .collect(Collectors.toList());

        // Trier par date (du plus récent au plus ancien)
        rdvs.sort((r1, r2) -> {
            if (r1.getDate() == null || r2.getDate() == null) {
                return 0;
            }
            return r2.getDate().compareTo(r1.getDate());
        });

        // Limiter le nombre de résultats
        if (rdvs.size() > limit) {
            rdvs = rdvs.subList(0, limit);
        }

        // Convertir en DTO
        return rdvs.stream()
                .map(this::convertToDTO) // Use the existing method
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getUpcomingRdvs(int limit) {
        try {
            Date today = new java.sql.Date(System.currentTimeMillis());
            List<Rdv> rdvs = rdvRepository.findByDateAfterOrderByDateAsc(today).stream()
                    .filter(this::isOperationalRdv)
                    .collect(Collectors.toList());

            // Add null safety for all fields
            List<RdvDTO> dtoList = new ArrayList<>();

            // Limiter les résultats
            int resultSize = Math.min(rdvs.size(), limit);
            for (int i = 0; i < resultSize; i++) {
                Rdv rdv = rdvs.get(i);
                if (rdv != null) {
                    RdvDTO dto = this.convertToDTO(rdv);
                    // Ensure all necessary fields have values to prevent NPE
                    if (dto.getDate() == null) {
                        // Either set to a default date or skip this record
                        // Option 1: Set to today's date
                        // dto.setDate(today);

                        // Option 2: Skip this record
                        continue;
                    }
                    dtoList.add(dto);
                }
            }

            return dtoList;
        } catch (Exception e) {
            // Log the error
            logger.error("Erreur dans getUpcomingRdvs: {}", e.getMessage(), e);
            // Return empty list rather than failing
            return new ArrayList<>();
        }
    }

    // Also make sure this helper method handles null values properly
    private RdvDTO convertToDTO(Rdv rdv) {
        RdvDTO dto = new RdvDTO();

        dto.setRdvPk(rdv.getRdvPk());
        dto.setIdRdv(rdv.getIdRdv());
        dto.setIdEtude(rdv.getIdEtude());

        // Copie des informations générales
        dto.setIdVolontaire(rdv.getIdVolontaire());
        dto.setIdGroupe(rdv.getIdGroupe());

        // Conversion de la date SQL en LocalDate
        if (rdv.getDate() != null) {
            dto.setDate(Date.valueOf(rdv.getDate().toLocalDate()));
        }

        dto.setHeure(rdv.getHeure());
        dto.setDuree(rdv.getDuree());
        dto.setEtat(rdv.getEtat());
        dto.setCommentaires(rdv.getCommentaires());

        // Copie de l'UUID
        // dto.setUuid(rdv.getUuid());

        return dto;
    }

    @Override
    public int countCompletedRdvToday() {
        // Get today's date as java.sql.Date (matching your entity's type)
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        // Use the "etat" field instead of "status"
        return rdvRepository.countByEtatAndDateBetween(
                "TERMINE", // Using the French value that matches your validateEtat method
                today,
                today);
    }

    @Override
    public int countRdvForToday() {
        // Get today's date as java.sql.Date (matching your entity's type)
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        // Use the simpler countByDate method that's already in your repository
        return rdvRepository.countByDate(today);
    }

    @Override
    public List<RdvDTO> getRdvsByIdEtudeWithRef(Integer idEtude) {
        List<Rdv> rdvs = rdvRepository.findByIdEtudeOrderByDateDesc(idEtude);

        return rdvs.stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RdvDTO> getRdvsByIdEtude(Integer idEtude) {
        List<Rdv> rdvs = rdvRepository.findByIdEtudeOrderByDateDesc(idEtude);
        return rdvs.stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByIdVolontaire(Integer idVolontaire) {
        List<Rdv> rdvs = rdvRepository.findByIdVolontaire(idVolontaire);
        return rdvs.stream()
                .filter(this::isOperationalRdv)
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasVolontaireRdvForEtude(Integer idVolontaire, int idEtude) {
        if (idVolontaire == null) {
            return false;
        }

        return rdvRepository.existsOperationalByVolontaireAndEtude(idVolontaire, idEtude);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RdvDTO createRdv(RdvDTO rdvDTO) {
        if (rdvDTO == null) {
            throw new IllegalArgumentException("RdvDTO cannot be null");
        }

        if (rdvDTO.getIdEtude() == null) {
            throw new IllegalArgumentException("L'ID de l'étude est obligatoire pour créer un rendez-vous.");
        }

        // Charger l'entité Etude
        Optional<Etude> etudeOpt = etudeRepository.findById(rdvDTO.getIdEtude());
        if (etudeOpt.isEmpty()) {
            throw new EntityNotFoundException("L'étude spécifiée n'existe pas.");
        }

        Integer idEtude = rdvDTO.getIdEtude();
        Rdv rdv = buildRdv(rdvDTO, etudeOpt.get(), rdvIdAllocator.nextForStudy(idEtude));

        ensureEtudeVolontaireAssociation(
                rdvDTO.getIdEtude(), rdvDTO.getIdGroupe(), rdvDTO.getIdVolontaire());

        return convertToDTO(rdvRepository.save(rdv));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<RdvDTO> createRdvsBatch(List<RdvDTO> rdvDTOs) {
        if (rdvDTOs == null || rdvDTOs.isEmpty()) {
            throw new IllegalArgumentException("La liste des RDV ne peut pas être vide");
        }

        List<RdvDTO> createdRdvs = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        logger.debug("Début création batch de {} RDV", rdvDTOs.size());

        // Pré-charger l'étude une seule fois (optimisation)
        Integer idEtude = rdvDTOs.get(0).getIdEtude();
        Optional<Etude> etudeOpt = etudeRepository.findById(idEtude);
        if (etudeOpt.isEmpty()) {
            throw new EntityNotFoundException("L'étude spécifiée n'existe pas : " + idEtude);
        }
        Etude etude = etudeOpt.get();

        // Créer tous les RDV dans la même transaction
        for (int i = 0; i < rdvDTOs.size(); i++) {
            RdvDTO rdvDTO = rdvDTOs.get(i);

            try {
                if (rdvDTO.getIdEtude() == null) {
                    throw new IllegalArgumentException("L'ID de l'étude est obligatoire");
                }

                int idRdv = rdvIdAllocator.nextForStudy(idEtude);
                Rdv rdv = buildRdv(rdvDTO, etude, idRdv);

                ensureEtudeVolontaireAssociation(
                        rdvDTO.getIdEtude(), rdvDTO.getIdGroupe(), rdvDTO.getIdVolontaire());

                // Sauvegarder
                Rdv savedRdv = rdvRepository.save(rdv);
                RdvDTO createdDTO = convertToDTO(savedRdv);
                createdRdvs.add(createdDTO);

                logger.debug("RDV {}/{} créé avec ID: {}", i + 1, rdvDTOs.size(), idRdv);

            } catch (RuntimeException e) {
                String errorMsg = "Erreur création RDV " + (i + 1) + ": " + e.getMessage();
                logger.error(errorMsg);
                errors.add(errorMsg);
                // Continue avec les autres RDV même si un échoue
            }
        }

        logger.debug("Batch terminé : {} créés sur {} demandés", createdRdvs.size(), rdvDTOs.size());

        if (!errors.isEmpty()) {
            logger.warn("Erreurs rencontrées dans le batch : {}", errors);
        }

        return createdRdvs;
    }

    private Rdv buildRdv(RdvDTO dto, Etude etude, int idRdv) {
        Rdv rdv = new Rdv();
        rdv.setIdEtude(dto.getIdEtude());
        rdv.setIdRdv(idRdv);
        rdv.setEtude(etude);
        rdv.setIdVolontaire(dto.getIdVolontaire());
        rdv.setIdGroupe(dto.getIdGroupe());
        if (dto.getDate() != null) {
            rdv.setDate(java.sql.Date.valueOf(dto.getDate()));
        }
        rdv.setHeure(dto.getHeure());
        rdv.setDuree(dto.getDuree());
        rdv.setCommentaires(dto.getCommentaires());
        rdv.setEtat(dto.getEtat() != null ? dto.getEtat() : "PLANIFIE");
        return rdv;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public RdvDTO updateRdv(RdvDTO rdvDTO) {
        Optional<Rdv> rdvOpt = rdvDTO.getRdvPk() != null
                ? rdvRepository.findById(rdvDTO.getRdvPk())
                : rdvRepository.findByIdEtudeAndIdRdv(rdvDTO.getIdEtude(), rdvDTO.getIdRdv());

        if (rdvOpt.isPresent()) {
            Rdv rdv = rdvOpt.get();
            Integer previousVolontaireId = rdv.getIdVolontaire();
            Integer nextVolontaireId = rdvDTO.getIdVolontaire();
            boolean volontaireChanged = !Objects.equals(previousVolontaireId, nextVolontaireId);
            boolean replacingAssignedVolunteer = volontaireChanged
                    && previousVolontaireId != null
                    && nextVolontaireId != null;
            boolean reassigningCancelledRdv = volontaireChanged
                    && nextVolontaireId != null
                    && isCancelledEtat(rdv.getEtat());
            boolean reassigningRdvWithCancellationTrace = volontaireChanged
                    && nextVolontaireId != null
                    && rdvDTO.getIdEtude() != null
                    && rdvDTO.getIdRdv() != null
                    && annulationRepository.existsRdvTraceForOtherVolunteer(
                            rdvDTO.getIdEtude(),
                            rdvDTO.getIdRdv(),
                            nextVolontaireId);
            if (replacingAssignedVolunteer || reassigningCancelledRdv || reassigningRdvWithCancellationTrace) {
                Rdv replacement = buildReplacementRdvWithNewId(rdv, rdvDTO);
                ensureEtudeVolontaireAssociation(
                        rdvDTO.getIdEtude(), rdvDTO.getIdGroupe(), nextVolontaireId);
                Rdv savedReplacement = rdvRepository.save(replacement);
                rdv.setEtat("ANNULE");
                rdvRepository.save(rdv);
                removeCancellationForReassignment(
                        rdvDTO.getIdEtude(), nextVolontaireId, volontaireChanged);
                removeStudyAssociationIfNoOperationalRdv(
                        rdvDTO.getIdEtude(), previousVolontaireId, true);
                logger.info("RDV {} on etude {} reused after cancellation: old RDV kept, new RDV id {} assigned to volunteer {}",
                        rdvDTO.getIdRdv(), rdvDTO.getIdEtude(), savedReplacement.getIdRdv(), nextVolontaireId);
                return convertToDTO(savedReplacement);
            }

            String nextEtat = resolveEtatForReassignment(
                    rdv.getEtat(),
                    rdvDTO.getEtat(),
                    previousVolontaireId,
                    nextVolontaireId,
                    rdvDTO.getIdEtude());

            rdv.setDate(Date.valueOf(rdvDTO.getDate()));
            rdv.setHeure(rdvDTO.getHeure());
            rdv.setDuree(rdvDTO.getDuree());
            rdv.setEtat(nextEtat);
            rdv.setCommentaires(rdvDTO.getCommentaires());
            rdv.setIdGroupe(rdvDTO.getIdGroupe());
            rdv.setIdVolontaire(rdvDTO.getIdVolontaire());
            ensureEtudeVolontaireAssociation(
                    rdvDTO.getIdEtude(), rdvDTO.getIdGroupe(), nextVolontaireId);
            Rdv savedRdv = rdvRepository.save(rdv);
            removeCancellationForReassignment(
                    rdvDTO.getIdEtude(), nextVolontaireId, volontaireChanged);
            removeStudyAssociationIfNoOperationalRdv(
                    rdvDTO.getIdEtude(), previousVolontaireId, volontaireChanged);
            return convertToDTO(savedRdv);
        } else {
            throw new IllegalArgumentException("Rdv introuvable: rdvPk=" + rdvDTO.getRdvPk()
                    + ", idEtude=" + rdvDTO.getIdEtude() + ", numeroRdv=" + rdvDTO.getIdRdv());
        }
    }

    private Rdv buildReplacementRdvWithNewId(Rdv existingRdv, RdvDTO rdvDTO) {
        Integer idEtude = rdvDTO.getIdEtude();
        Rdv replacement = new Rdv();
        replacement.setIdEtude(idEtude);
        replacement.setIdRdv(rdvIdAllocator.nextForStudy(idEtude));
        replacement.setEtude(existingRdv.getEtude());
        replacement.setIdVolontaire(rdvDTO.getIdVolontaire());
        replacement.setIdGroupe(rdvDTO.getIdGroupe());
        replacement.setDate(Date.valueOf(rdvDTO.getDate()));
        replacement.setHeure(rdvDTO.getHeure());
        replacement.setDuree(rdvDTO.getDuree());
        replacement.setEtat(hasText(rdvDTO.getEtat()) && !isCancelledEtat(rdvDTO.getEtat()) ? rdvDTO.getEtat().trim().toUpperCase() : "PLANIFIE");
        replacement.setCommentaires(rdvDTO.getCommentaires());
        return replacement;
    }

    private void ensureEtudeVolontaireAssociation(
            Integer idEtude, Integer idGroupe, Integer idVolontaire) {
        if (idVolontaire == null) {
            return;
        }
        if (idEtude == null || idEtude <= 0 || idVolontaire <= 0) {
            throw new IllegalArgumentException("Etude et volontaire valides requis pour affecter un RDV");
        }
        if (etudeVolontaireService.existsByEtudeAndVolontaire(idEtude, idVolontaire)) {
            return;
        }

        int groupeId = idGroupe != null && idGroupe > 0 ? idGroupe : 0;
        int iv = 0;
        if (groupeId > 0) {
            Groupe groupe = groupeRepository.findById(groupeId)
                    .orElseThrow(() -> new IllegalArgumentException("Groupe introuvable: " + groupeId));
            if (groupe.getIdEtude() != null && !Objects.equals(groupe.getIdEtude(), idEtude)) {
                throw new IllegalArgumentException(
                        "Le groupe " + groupeId + " n'appartient pas a l'etude " + idEtude);
            }
            iv = groupe.getIv();
        }

        EtudeVolontaireDTO association = new EtudeVolontaireDTO(
                idEtude, groupeId, idVolontaire, iv, 0, 0, "INSCRIT");
        etudeVolontaireService.saveEtudeVolontaire(association);
        logger.info(
                "Association etude-volontaire creee depuis le RDV: etude={}, groupe={}, volontaire={}, iv={}",
                idEtude, groupeId, idVolontaire, iv);
    }

    private void removeStudyAssociationIfNoOperationalRdv(
            Integer idEtude, Integer previousVolontaireId, boolean volontaireChanged) {
        if (!volontaireChanged || idEtude == null || previousVolontaireId == null) {
            return;
        }
        if (rdvRepository.existsOperationalByVolontaireAndEtude(previousVolontaireId, idEtude)) {
            return;
        }

        int deleted = etudeVolontaireService.deleteByEtudeAndVolontaire(idEtude, previousVolontaireId);
        logger.info(
                "Association etude-volontaire supprimee apres dernier RDV operationnel: etude={}, volontaire={}, lignes={}",
                idEtude, previousVolontaireId, deleted);
    }

    private void removeCancellationForReassignment(
            Integer idEtude, Integer nextVolontaireId, boolean volontaireChanged) {
        if (!volontaireChanged || idEtude == null || nextVolontaireId == null) {
            return;
        }

        int deleted = annulationRepository.deleteByIdVolAndIdEtude(nextVolontaireId, idEtude);
        if (deleted > 0) {
            logger.info(
                    "Annulation supprimee lors de la reaffectation transactionnelle: etude={}, volontaire={}, lignes={}",
                    idEtude, nextVolontaireId, deleted);
        }
    }


    private String resolveEtatForReassignment(String currentEtat, String requestedEtat, Integer previousVolontaireId,
            Integer nextVolontaireId, Integer idEtude) {
        String nextEtat = hasText(requestedEtat) ? requestedEtat.trim().toUpperCase() : currentEtat;

        if (!hasText(nextEtat)) {
            return "PLANIFIE";
        }

        boolean volontaireChanged = !Objects.equals(previousVolontaireId, nextVolontaireId);
        if (!volontaireChanged || nextVolontaireId == null || !isCancelledEtat(nextEtat)) {
            return nextEtat;
        }

        boolean nextVolontaireAnnule = idEtude != null
                && !annulationRepository.findByIdVolAndIdEtude(nextVolontaireId, idEtude).isEmpty();
        if (nextVolontaireAnnule) {
            return nextEtat;
        }

        logger.info("RDV reassigned from volunteer {} to {} on etude {}: stale ANNULE state reset to PLANIFIE",
                previousVolontaireId, nextVolontaireId, idEtude);
        return "PLANIFIE";
    }

    private boolean isCancelledEtat(String etat) {
        return "ANNULE".equalsIgnoreCase(etat);
    }

    private boolean isOperationalRdv(Rdv rdv) {
        return rdv != null && !isCancelledEtat(rdv.getEtat());
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
