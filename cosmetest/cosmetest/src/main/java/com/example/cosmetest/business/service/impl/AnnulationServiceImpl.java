package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.mapper.AnnulationMapper;
import com.example.cosmetest.business.service.AnnulationService;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Annulation;
import com.example.cosmetest.domain.model.Rdv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation de la couche business (BLL) pour les annulations
 * Contient toute la logique métier liée aux annulations
 */
@Service
public class AnnulationServiceImpl implements AnnulationService {

    private static final Logger logger = LoggerFactory.getLogger(AnnulationServiceImpl.class);

    private final AnnulationRepository annulationRepository;
    private final AnnulationMapper annulationMapper;
    private final RdvRepository rdvRepository;

    // Format de date attendu (à adapter selon votre format)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AnnulationServiceImpl(
            AnnulationRepository annulationRepository, 
            AnnulationMapper annulationMapper,
            RdvRepository rdvRepository) {
        this.annulationRepository = annulationRepository;
        this.annulationMapper = annulationMapper;
        this.rdvRepository = rdvRepository;
    }

    @Override
    public List<AnnulationDTO> getAllAnnulations() {
        return annulationRepository.findAll().stream()
                .map(annulationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AnnulationDTO> getAllAnnulationsPaginated(Pageable pageable) {
        return annulationRepository.findAll(pageable)
                .map(annulationMapper::toDto);
    }

    @Override
    public Optional<AnnulationDTO> getAnnulationById(Integer id) {
        return annulationRepository.findById(id)
                .map(annulationMapper::toDto);
    }

    @Override
    public List<AnnulationDTO> getAnnulationsByVolontaire(int idVol) {
        return annulationRepository.findByIdVol(idVol).stream()
                .map(annulationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnulationDTO> getAnnulationsByEtude(int idEtude) {
        return annulationRepository.findByIdEtude(idEtude).stream()
                .map(annulationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnulationDTO> getAnnulationsByVolontaireAndEtude(int idVol, int idEtude) {
        return annulationRepository.findByIdVolAndIdEtude(idVol, idEtude).stream()
                .map(annulationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnulationDTO> getAnnulationsByDate(String dateAnnulation) {
        // Vérification de la validité de la date avant la recherche
        validateDate(dateAnnulation);

        return annulationRepository.findByDateAnnulation(dateAnnulation).stream()
                .map(annulationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnulationDTO> searchAnnulationsByCommentaire(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot-clé de recherche ne peut pas être vide");
        }

        return annulationRepository.findByCommentaireContaining(keyword).stream()
                .map(annulationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnnulationDTO saveAnnulation(AnnulationDTO annulationDTO) {
        logger.info("🔄 Début création annulation pour volontaire {} dans étude {}", 
            annulationDTO.getIdVol(), annulationDTO.getIdEtude());
        
        // Validation des données avant sauvegarde
        validateAnnulationData(annulationDTO);

        // Conversion en entité
        Annulation annulation = annulationMapper.toEntity(annulationDTO);

        // Sauvegarde de l'annulation
        Annulation savedAnnulation = annulationRepository.save(annulation);
        logger.info("✅ Annulation enregistrée");

        // 🔥 AUTOMATIQUEMENT : Libérer tous les RDV du volontaire pour cette étude
        try {
            logger.info("🗓️ Libération des créneaux RDV pour volontaire {} dans étude {}...", 
                annulationDTO.getIdVol(), annulationDTO.getIdEtude());
            
            // Récupérer tous les RDV du volontaire dans cette étude
            List<Rdv> rdvs = rdvRepository.findByIdVolontaireAndIdEtude(
                annulationDTO.getIdVol(), 
                annulationDTO.getIdEtude()
            );
            
            logger.info("📋 {} RDV trouvés à libérer", rdvs.size());
            
            // Mettre idVolontaire à null pour chaque RDV (libérer le créneau)
            int rdvsLiberes = 0;
            for (Rdv rdv : rdvs) {
                rdv.setIdVolontaire(null);
                rdvRepository.save(rdv);
                rdvsLiberes++;
                logger.debug("✅ Créneau RDV {} libéré", rdv.getId());
            }
            
            logger.info("✅ {} créneaux RDV libérés avec succès", rdvsLiberes);
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors de la libération des créneaux RDV: {}", e.getMessage(), e);
            // On ne propage pas l'erreur pour ne pas bloquer l'annulation
            // L'annulation est déjà enregistrée, c'est le plus important
        }

        // Conversion en DTO pour retour
        return annulationMapper.toDto(savedAnnulation);
    }

    @Override
    @Transactional
    public void deleteAnnulation(Integer id) {
        if (!annulationRepository.existsById(id)) {
            throw new IllegalArgumentException("L'annulation avec l'ID " + id + " n'existe pas");
        }

        annulationRepository.deleteById(id);
    }

    @Override
    public Long countAnnulationsByVolontaire(int idVol) {
        return annulationRepository.countAnnulationsByVolontaire(idVol);
    }

    @Override
    public List<AnnulationDTO> getAnnulationsByVolontaireOrderByDateDesc(int idVol) {
        return annulationRepository.findByIdVolOrderByDateAnnulationDesc(idVol).stream()
                .map(annulationMapper::toDto)
                .collect(Collectors.toList());
    }

    // Méthodes privées pour la logique métier

    /**
     * Valide les données d'une annulation avant sauvegarde
     * @param annulationDTO DTO à valider
     */
    private void validateAnnulationData(AnnulationDTO annulationDTO) {
        if (annulationDTO == null) {
            throw new IllegalArgumentException("Les données d'annulation ne peuvent pas être nulles");
        }

        // Validation de l'ID du volontaire
        if (annulationDTO.getIdVol() <= 0) {
            throw new IllegalArgumentException("L'ID du volontaire doit être un nombre positif");
        }

        // Validation de l'ID de l'étude
        if (annulationDTO.getIdEtude() <= 0) {
            throw new IllegalArgumentException("L'ID de l'étude doit être un nombre positif");
        }

        // Validation de la date
        if (annulationDTO.getDateAnnulation() == null || annulationDTO.getDateAnnulation().trim().isEmpty()) {
            throw new IllegalArgumentException("La date d'annulation ne peut pas être vide");
        }

        validateDate(annulationDTO.getDateAnnulation());
    }

    /**
     * Valide le format de la date
     * @param dateString Date à valider
     */
    private void validateDate(String dateString) {
        try {
            LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Format attendu: yyyy-MM-dd", e);
        }
    }
}