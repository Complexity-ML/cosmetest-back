package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.mapper.RdvMapper;
import com.example.cosmetest.business.service.RdvService;
import com.example.cosmetest.data.repository.EtudeRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Rdv;
import com.example.cosmetest.domain.model.RdvId;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.domain.model.Etude;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implémentation de la couche business (BLL) pour les rendez-vous
 * Contient toute la logique métier liée aux rendez-vous
 */
@Service
public class RdvServiceImpl implements RdvService {

    private final RdvRepository rdvRepository;
    private final RdvMapper rdvMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Lazy
    private EtudeRepository etudeRepository;

    public RdvServiceImpl(RdvRepository rdvRepository, RdvMapper rdvMapper, EtudeService etudeService) {
        this.rdvRepository = rdvRepository;
        this.rdvMapper = rdvMapper;
    }

    @Override
    public List<RdvDTO> getAllRdvs() {
        return rdvRepository.findAll().stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RdvDTO> getAllRdvsPaginated(Pageable pageable) {
        return rdvRepository.findAll(pageable)
                .map(rdvMapper::toDto);
    }

    @Override
    public Optional<RdvDTO> getRdvById(RdvId id) {
        return rdvRepository.findById(id)
                .map(rdvMapper::toDto);
    }

    @Override
    public List<RdvDTO> getRdvsByVolontaire(Integer idVolontaire) {
        return rdvRepository.findByIdVolontaire(idVolontaire).stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByDate(Date date) {
        return rdvRepository.findByDate(date).stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByVolontaireAndDate(Integer idVolontaire, Date date) {
        return rdvRepository.findByIdVolontaireAndDate(idVolontaire, date).stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByVolontaireAndDateRange(Integer idVolontaire, Date startDate, Date endDate) {
        return rdvRepository.findByVolontaireAndDateRange(idVolontaire, startDate, endDate).stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByGroupe(Integer idGroupe) {
        return rdvRepository.findByIdGroupe(idGroupe).stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByEtat(String etat) {
        return rdvRepository.findByEtat(etat).stream()
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

        // Sauvegarder l'entité
        Rdv savedRdv = rdvRepository.save(rdvEntity);

        // Convertir l'entité sauvegardée en DTO
        return rdvMapper.toDto(savedRdv);
    }

    @Override
    @Transactional
    public void deleteRdv(RdvId id) {
        // Vérifier si le rendez-vous existe avant de le supprimer
        if (rdvRepository.existsById(id)) {
            rdvRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Le rendez-vous avec l'ID " + id + " n'existe pas");
        }
    }

    @Override
    public List<RdvDTO> searchRdvsByCommentaires(String keyword) {
        return rdvRepository.findByCommentairesContaining(keyword).stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateRdvEtat(RdvId id, String nouvelEtat) {
        // Récupérer le rendez-vous
        Rdv rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Le rendez-vous avec l'ID " + id + " n'existe pas"));

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
        List<Rdv> rdvs = rdvRepository.findAll();

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
            List<Rdv> rdvs = rdvRepository.findByDateAfterOrderByDateAsc(today);

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
            System.err.println("Error in getUpcomingRdvs: " + e.getMessage());
            e.printStackTrace();
            // Return empty list rather than failing
            return new ArrayList<>();
        }
    }

    // Also make sure this helper method handles null values properly
    private RdvDTO convertToDTO(Rdv rdv) {
        RdvDTO dto = new RdvDTO();

        // Copie des informations d'identifiant
        if (rdv.getId() != null) {
            dto.setIdRdv(rdv.getId().getIdRdv());
            dto.setIdEtude(rdv.getId().getIdEtude());
            // dto.setSequence(rdv.getId().getSequence()); // Copier la séquence
        }

        // Copie des informations générales
        dto.setIdVolontaire(rdv.getIdVolontaire());
        dto.setIdGroupe(rdv.getIdGroupe());

        // Conversion de la date SQL en LocalDate
        if (rdv.getDate() != null) {
            dto.setDate(Date.valueOf(rdv.getDate().toLocalDate()));
        }

        dto.setHeure(rdv.getHeure());
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
        List<Rdv> rdvs = rdvRepository.findById_IdEtudeOrderByDateDesc(idEtude);

        return rdvs.stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RdvDTO> getRdvsByIdEtude(Integer idEtude) {
        List<Rdv> rdvs = rdvRepository.findById_IdEtudeOrderByDateDesc(idEtude);
        return rdvs.stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RdvDTO> getRdvsByIdVolontaire(Integer idVolontaire) {
        List<Rdv> rdvs = rdvRepository.findByIdVolontaire(idVolontaire);
        return rdvs.stream()
                .map(rdvMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasVolontaireRdvForEtude(Integer idVolontaire, int idEtude) {
        if (idVolontaire == null) {
            return false;
        }

        // Check if any RDV exists for this volunteer and study
        Optional<Rdv> existingRdv = rdvRepository.findByVolontaireIdAndEtudeId(idVolontaire, idEtude);
        return existingRdv.isPresent();
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

        // Créer le RDV
        Rdv rdv = new Rdv();
        rdv.setIdVolontaire(rdvDTO.getIdVolontaire());
        rdv.setEtude(etudeOpt.get());

        // Générer un ID très élevé et aléatoire pour éviter les collisions
        Random random = new Random();
        Integer idEtude = rdvDTO.getIdEtude();

        // Générer un ID entre 100000 et 999999 (6 chiffres)
        Integer idRdv = 100000 + random.nextInt(900000);

        // Créer la clé primaire
        RdvId rdvId = new RdvId(idEtude, idRdv);
        rdv.setId(rdvId);

        // Définir les autres propriétés
        if (rdvDTO.getDate() != null) {
            rdv.setDate(java.sql.Date.valueOf(rdvDTO.getDate()));
        }
        rdv.setHeure(rdvDTO.getHeure());
        rdv.setCommentaires(rdvDTO.getCommentaires());
        rdv.setEtat(rdvDTO.getEtat() != null ? rdvDTO.getEtat() : "PLANIFIE");
        rdv.setIdGroupe(rdvDTO.getIdGroupe());

        // Essayer de sauvegarder avec un ID aléatoire, réessayer en cas d'échec
        int maxAttempts = 5;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                Rdv savedRdv = rdvRepository.save(rdv);
                return convertToDTO(savedRdv);
            } catch (DataIntegrityViolationException e) {
                // Si collision, générer un nouvel ID aléatoire
                idRdv = 100000 + random.nextInt(900000);
                rdvId = new RdvId(idEtude, idRdv);
                rdv.setId(rdvId);

                // Continuer si ce n'est pas la dernière tentative
                if (attempt == maxAttempts - 1) {
                    throw new RuntimeException("Impossible de créer le RDV après plusieurs tentatives", e);
                }
            }
        }

        // Ne devrait jamais atteindre ce point
        throw new RuntimeException("Erreur inattendue lors de la création du RDV");
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<RdvDTO> createRdvsBatch(List<RdvDTO> rdvDTOs) {
        if (rdvDTOs == null || rdvDTOs.isEmpty()) {
            throw new IllegalArgumentException("La liste des RDV ne peut pas être vide");
        }

        List<RdvDTO> createdRdvs = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // Logs pour debugging
        System.out.println("🚀 Début création batch de " + rdvDTOs.size() + " RDV");

        // Pré-charger l'étude une seule fois (optimisation)
        Integer idEtude = rdvDTOs.get(0).getIdEtude();
        Optional<Etude> etudeOpt = etudeRepository.findById(idEtude);
        if (etudeOpt.isEmpty()) {
            throw new EntityNotFoundException("L'étude spécifiée n'existe pas : " + idEtude);
        }
        Etude etude = etudeOpt.get();

        // Créer tous les RDV dans la même transaction
        Random random = new Random();
        for (int i = 0; i < rdvDTOs.size(); i++) {
            RdvDTO rdvDTO = rdvDTOs.get(i);

            try {
                if (rdvDTO.getIdEtude() == null) {
                    throw new IllegalArgumentException("L'ID de l'étude est obligatoire");
                }

                // Créer le RDV
                Rdv rdv = new Rdv();
                rdv.setIdVolontaire(rdvDTO.getIdVolontaire());
                rdv.setEtude(etude);

                // Générer un ID aléatoire unique pour ce batch
                Integer idRdv = 100000 + random.nextInt(900000);
                RdvId rdvId = new RdvId(idEtude, idRdv);

                // Vérifier l'unicité dans ce batch et en base
                int attempts = 0;
                while (attempts < 10) {
                    final Integer currentIdRdv = idRdv; // Variable finale pour lambda
                    boolean existsInBatch = createdRdvs.stream()
                        .anyMatch(created -> created.getIdRdv().equals(currentIdRdv));

                    if (!existsInBatch && !rdvRepository.existsById(rdvId)) {
                        break;
                    }

                    idRdv = 100000 + random.nextInt(900000);
                    rdvId = new RdvId(idEtude, idRdv);
                    attempts++;
                }

                rdv.setId(rdvId);

                // Définir les autres propriétés
                if (rdvDTO.getDate() != null) {
                    rdv.setDate(java.sql.Date.valueOf(rdvDTO.getDate()));
                }
                rdv.setHeure(rdvDTO.getHeure());
                rdv.setCommentaires(rdvDTO.getCommentaires());
                rdv.setEtat(rdvDTO.getEtat() != null ? rdvDTO.getEtat() : "PLANIFIE");
                rdv.setIdGroupe(rdvDTO.getIdGroupe());

                // Sauvegarder
                Rdv savedRdv = rdvRepository.save(rdv);
                RdvDTO createdDTO = convertToDTO(savedRdv);
                createdRdvs.add(createdDTO);

                System.out.println("✅ RDV " + (i + 1) + "/" + rdvDTOs.size() + " créé avec ID: " + idRdv);

            } catch (Exception e) {
                String errorMsg = "Erreur création RDV " + (i + 1) + ": " + e.getMessage();
                System.err.println("❌ " + errorMsg);
                errors.add(errorMsg);
                // Continue avec les autres RDV même si un échoue
            }
        }

        System.out.println("📊 Batch terminé : " + createdRdvs.size() + " créés sur " + rdvDTOs.size() + " demandés");

        if (!errors.isEmpty()) {
            System.err.println("⚠️ Erreurs rencontrées : " + errors);
        }

        return createdRdvs;
    }

    @Override
    public void updateRdv(RdvDTO rdvDTO) {
        // Création de la clé composite avec les 3 paramètres requis
        RdvId rdvId = new RdvId();
        rdvId.setIdEtude(rdvDTO.getIdEtude());
        rdvId.setIdRdv(rdvDTO.getIdRdv());

        // Si le DTO a une séquence, utilisez-la, sinon utilisez 1 comme valeur par
        // défaut
        // if (rdvDTO.getSequence() != null) {
        // rdvId.setSequence(rdvDTO.getSequence());
        // } else {
        // rdvId.setSequence(1); // Valeur par défaut
        // }

        Optional<Rdv> rdvOpt = rdvRepository.findById(rdvId);

        if (rdvOpt.isPresent()) {
            Rdv rdv = rdvOpt.get();
            rdv.setDate(Date.valueOf(rdvDTO.getDate()));
            rdv.setHeure(rdvDTO.getHeure());
            rdv.setEtat(rdvDTO.getEtat());
            rdv.setCommentaires(rdvDTO.getCommentaires());
            rdv.setIdGroupe(rdvDTO.getIdGroupe());
            rdv.setIdVolontaire(rdvDTO.getIdVolontaire());
            rdvRepository.save(rdv);
        } else {
            throw new IllegalArgumentException("Rdv not found with ID: " + rdvId);
        }
    }
}