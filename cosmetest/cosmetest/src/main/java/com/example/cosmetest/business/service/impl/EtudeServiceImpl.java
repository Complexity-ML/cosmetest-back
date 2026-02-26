package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.EtudeDTO;
import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.mapper.EtudeMapper;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.data.repository.EtudeRepository;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import com.example.cosmetest.domain.model.Etude;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.cosmetest.business.service.RdvService;

/**
 * Implémentation de la couche business (BLL) pour les études
 * Contient toute la logique métier liée aux études
 */
@Service
public class EtudeServiceImpl implements EtudeService {

    private static final Logger logger = LoggerFactory.getLogger(EtudeServiceImpl.class);

    private final EtudeMapper etudeMapper;

    @Autowired
    @Lazy
    private RdvService rdvService;

    @Autowired
    private EtudeRepository etudeRepository;

    @Autowired
    private EtudeVolontaireRepository etudeVolontaireRepository;

    public EtudeServiceImpl(EtudeRepository etudeRepository, EtudeMapper etudeMapper) {
        this.etudeRepository = etudeRepository;
        this.etudeMapper = etudeMapper;
    }

    @Override
    public List<EtudeDTO> getAllEtudes() {
        return etudeRepository.findByArchiveFalse().stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EtudeDTO> getAllEtudesPaginated(Pageable pageable) {
        return etudeRepository.findByArchiveFalse(pageable)
                .map(etudeMapper::toDto);
    }

    @Override
    public Optional<EtudeDTO> getEtudeById(Integer id) {
        return etudeRepository.findById(id)
                .map(etudeMapper::toDto);
    }

    @Override
    public Optional<EtudeDTO> getEtudeByRef(String ref) {
        return etudeRepository.findByRef(ref)
                .map(etudeMapper::toDto);
    }

    @Override
    public List<EtudeDTO> getEtudesByType(String type) {
        return etudeRepository.findByType(type).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudeDTO> getEtudesByTitre(String keyword) {
        return etudeRepository.findByTitreContaining(keyword).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudeDTO> getEtudesByDateRange(Date debut, Date fin) {
        // Validation des dates
        validateDateRange(debut, fin);

        return etudeRepository.findByDateDebutAndDateFin(debut, fin).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudeDTO> getActiveEtudesAtDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("La date ne peut pas être nulle");
        }

        return etudeRepository.findActiveEtudesAtDate(date.toLocalDate()).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudeDTO> getEtudesByPaye(int paye) {
        // Validation de la valeur de paye
        if (paye != 0 && paye != 2) {
            throw new IllegalArgumentException("La valeur de paye doit être 0 ou 2");
        }

        return etudeRepository.findByPaye(paye).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudeDTO> searchEtudes(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Le terme de recherche ne peut pas être vide");
        }

        return etudeRepository.searchByTitreOrCommentairesOrRef(searchTerm).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EtudeDTO saveEtude(EtudeDTO etudeDTO) {
        // Validation des données
        validateEtudeData(etudeDTO);

        Etude etude;

        if (etudeDTO.getIdEtude() != null) {
            // UPDATE : charger l'entité existante et mettre à jour ses champs
            // Cela préserve les collections (etudeVolontaires, rdvs) et évite
            // que orphanRemoval=true ne supprime toutes les associations
            Optional<Etude> existingOpt = etudeRepository.findById(etudeDTO.getIdEtude());
            if (existingOpt.isPresent()) {
                etude = etudeMapper.updateEntityFromDto(etudeDTO, existingOpt.get());
            } else {
                etude = etudeMapper.toEntity(etudeDTO);
            }
        } else {
            // CREATE : nouvelle étude
            if (isRefAlreadyUsed(etudeDTO.getRef())) {
                throw new IllegalArgumentException("La référence " + etudeDTO.getRef() + " est déjà utilisée");
            }
            etude = etudeMapper.toEntity(etudeDTO);
        }

        // Sauvegarde
        Etude savedEtude = etudeRepository.save(etude);

        // Conversion en DTO pour retour
        return etudeMapper.toDto(savedEtude);
    }

    @Override
    @Transactional
    public void deleteEtude(Integer id) {
        Etude etude = etudeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Étude introuvable - ID : " + id));

        // Soft-delete : archiver au lieu de supprimer définitivement
        etude.setArchive(true);
        etudeRepository.save(etude);
    }

    @Override
    public boolean isRefAlreadyUsed(String ref) {
        return etudeRepository.existsByRef(ref);
    }

    @Override
    public Long countEtudesByType(String type) {
        return etudeRepository.countByType(type);
    }

    @Override
    public List<EtudeDTO> getUpcomingEtudes() {
        // Récupérer la date du jour
        Date today = new Date(System.currentTimeMillis());

        // Récupérer les études dont la date de début est après aujourd'hui
        return etudeRepository.findByDateDebutAfter(today).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudeDTO> getCurrentEtudes() {
        LocalDate today = LocalDate.now();

        System.out.println("🔍 Recherche des études actives pour la date : " + today);

        List<Etude> activeEtudes = etudeRepository.findActiveEtudesAtDate(today);

        System.out.println(" Nombre d'études actives trouvées : " + activeEtudes.size());

        return activeEtudes.stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudeDTO> getCompletedEtudes() {
        // Récupérer la date du jour
        Date today = new Date(System.currentTimeMillis());

        // Récupérer les études dont la date de fin est avant aujourd'hui
        return etudeRepository.findByDateFinBefore(today).stream()
                .map(etudeMapper::toDto)
                .collect(Collectors.toList());
    }

    // Méthodes privées pour la logique métier

    /**
     * Valide les données d'une étude avant sauvegarde
     *
     * @param etudeDTO DTO à valider
     */
    private void validateEtudeData(EtudeDTO etudeDTO) {
        if (etudeDTO == null) {
            throw new IllegalArgumentException("Les données d'étude ne peuvent pas être nulles");
        }

        // Validation de la référence
        if (etudeDTO.getRef() == null || etudeDTO.getRef().trim().isEmpty()) {
            throw new IllegalArgumentException("La référence de l'étude ne peut pas être vide");
        }

        // Validation des dates
        if (etudeDTO.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début ne peut pas être nulle");
        }

        if (etudeDTO.getDateFin() == null) {
            throw new IllegalArgumentException("La date de fin ne peut pas être nulle");
        }

        // Vérifier que la date de fin est après la date de début
        if (etudeDTO.getDateFin().before(etudeDTO.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
    }

    /**
     * Valide une plage de dates
     *
     * @param debut Date de début
     * @param fin   Date de fin
     */
    private void validateDateRange(Date debut, Date fin) {
        if (debut == null) {
            throw new IllegalArgumentException("La date de début ne peut pas être nulle");
        }

        if (fin == null) {
            throw new IllegalArgumentException("La date de fin ne peut pas être nulle");
        }

        if (fin.before(debut)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }
    }

    @Override
    public int countCurrentEtudes() {
        LocalDate today = LocalDate.now();
        return (int) etudeRepository.countCurrentEtudes(today);
    }

    @Override
    public List<EtudeDTO> getRecentEtudes(int limit) {
        // Implémentation temporaire
        List<Etude> etudes = etudeRepository.findAll();

        // Trier par date - utilisez l'attribut réel de date dans votre entité
        // Par exemple, si vous avez dateDebut au lieu de dateCreation
        etudes.sort((e1, e2) -> {
            if (e1.getDateDebut() == null || e2.getDateDebut() == null) {
                return 0;
            }
            return e2.getDateDebut().compareTo(e1.getDateDebut());
        });

        // Limiter le nombre de résultats
        if (etudes.size() > limit) {
            etudes = etudes.subList(0, limit);
        }

        // Convertir en DTO - utilisez votre méthode de conversion
        return etudes.stream()
                .map(etude -> convertToDTO(etude))
                .collect(Collectors.toList());
    }

    // Ajoutez cette méthode si elle n'existe pas
    private EtudeDTO convertToDTO(Etude etude) {
        EtudeDTO dto = new EtudeDTO();
        // Remplissez le DTO avec les données de l'entité
        dto.setIdEtude(etude.getIdEtude());
        dto.setTitre(etude.getTitre());
        dto.setRef(etude.getRef());
        // etc.
        return dto;
    }

    @Override
    public Optional<Etude> getByRef(String etudeRef) {
        return etudeRepository.findByRef(etudeRef);
    }

    @Override
    public List<EtudeDTO> suggestEtudes(String query, int limit) {
        // Log de débogage
        System.out.println("Recherche de suggestions pour : " + query);
        System.out.println("Limite : " + limit);

        // Vérification des paramètres
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // Requête de suggestion par référence ou titre
            Pageable limitedPageable = PageRequest.of(0, limit);
            List<Etude> suggestions = etudeRepository.findByRefContainingIgnoreCaseOrTitreContainingIgnoreCase(
                    query.trim(),
                    limitedPageable);

            // Log du nombre de résultats
            System.out.println("Nombre de suggestions trouvées : " + suggestions.size());

            // Conversion en DTOs en utilisant le mapper existant
            return suggestions.stream()
                    .map(etudeMapper::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            // Log détaillé de l'erreur
            System.err.println("Erreur lors de la recherche de suggestions : " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Etude> findById(Integer id) {
        return etudeRepository.findById(id);
    }

    /**
     * Récupère toutes les études auxquelles un volontaire participe
     */
    @Override
    @Transactional(readOnly = true)
    public List<EtudeDTO> getEtudesByVolontaire(Integer idVolontaire) {
        if (idVolontaire == null) {
            return new ArrayList<>();
        }

        try {
            // Une seule requête JOIN au lieu du pattern N+1
            return etudeRepository.findEtudesByVolontaireId(idVolontaire).stream()
                    .map(etudeMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des études pour le volontaire " + idVolontaire, e);
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EtudeDTO getEtudeWithIv(Integer idEtude) {
        Optional<Etude> etudeOpt = etudeRepository.findById(idEtude);

        if (etudeOpt.isPresent()) {
            Etude etude = etudeOpt.get();
            EtudeDTO etudeDTO = etudeMapper.toDto(etude);

            // Récupérer l'IV depuis la table etude_volontaire
            Integer iv = etudeVolontaireRepository.findMostCommonIvByIdEtude(idEtude);

            // Si aucun IV trouvé, essayer de récupérer le premier
            if (iv == null) {
                iv = etudeVolontaireRepository.findFirstIvByIdEtude(idEtude);
            }

            etudeDTO.setIv(iv);
            return etudeDTO;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Etude saveEtudeWithIv(EtudeDTO etudeDTO) {
        // Validation des données
        validateEtudeData(etudeDTO);

        // Vérification de l'unicité de la référence pour une nouvelle étude
        if (etudeDTO.getIdEtude() == null && isRefAlreadyUsed(etudeDTO.getRef())) {
            throw new IllegalArgumentException("La référence " + etudeDTO.getRef() + " est déjà utilisée");
        }

        // Conversion en entité
        Etude etude = etudeMapper.toEntity(etudeDTO);

        // Sauvegarde
        Etude savedEtude = etudeRepository.save(etude);

        // Si un IV est défini, mettre à jour l'IV pour tous les volontaires
        if (etudeDTO.getIv() != null) {
            etudeVolontaireRepository.updateIvForAllVolontairesInEtude(
                    savedEtude.getIdEtude(),
                    etudeDTO.getIv());
        }

        return savedEtude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getIvStatistics(Integer idEtude) {
        Map<String, Object> statistics = new HashMap<>();

        Double moyenne = etudeVolontaireRepository.calculateAverageIvByIdEtude(idEtude);
        Integer min = etudeVolontaireRepository.findMinIvByIdEtude(idEtude);
        Integer max = etudeVolontaireRepository.findMaxIvByIdEtude(idEtude);
        Long nbVolontaires = etudeVolontaireRepository.countVolontairesByEtude(idEtude);

        statistics.put("moyenne", moyenne);
        statistics.put("minimum", min);
        statistics.put("maximum", max);
        statistics.put("nombreVolontaires", nbVolontaires);

        // Récupérer l'IV le plus fréquent et son nombre d'occurrences
        Integer mostCommonIv = etudeVolontaireRepository.findMostCommonIvByIdEtude(idEtude);
        if (mostCommonIv != null) {
            Long countMostCommon = etudeVolontaireRepository.countVolontairesByEtudeAndIv(idEtude, mostCommonIv);
            statistics.put("ivPlusFrequent", mostCommonIv);
            statistics.put("nombreVolontairesAvecIvPlusFrequent", countMostCommon);
        }

        return statistics;
    }

    @Override
    public String getEtudeRefById(Integer idEtude) {
        Optional<Etude> etude = etudeRepository.findById(idEtude);
        return etude.map(Etude::getRef).orElse(null);
    }

    @Override
    @Transactional
    public boolean updatePayeStatus(Integer idEtude, Integer paye) {
        try {
            // Validation des paramètres
            if (idEtude == null) {
                logger.error("❌ ID étude ne peut pas être null");
                return false;
            }

            if (paye == null || (paye != 0 && paye != 2)) {
                logger.error("❌ Statut PAYE invalide: {}. Doit être 0 ou 2", paye);
                return false;
            }

            // Récupérer l'étude existante
            Optional<Etude> etudeOpt = etudeRepository.findById(idEtude);

            if (etudeOpt.isPresent()) {
                Etude etude = etudeOpt.get();

                // Log de l'ancienne valeur pour traçabilité
                Integer oldPaye = etude.getPaye();
                logger.info("🔄 Mise à jour statut PAYE étude {} : {} -> {}",
                        idEtude, oldPaye, paye);

                // Mettre à jour le statut PAYE
                etude.setPaye(paye);

                // Sauvegarder en base
                etudeRepository.save(etude);

                logger.info(" Étude {} - Statut PAYE mis à jour avec succès: {}", idEtude, paye);
                return true;
            } else {
                logger.error("❌ Étude {} non trouvée pour mise à jour PAYE", idEtude);
                return false;
            }

        } catch (Exception e) {
            logger.error("❌ Erreur lors de la mise à jour du statut PAYE pour l'étude {}: {}",
                    idEtude, e.getMessage(), e);
            return false;
        }
    }
}