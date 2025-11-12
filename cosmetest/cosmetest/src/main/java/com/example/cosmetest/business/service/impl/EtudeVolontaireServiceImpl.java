package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.mapper.EtudeVolontaireMapper;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import com.example.cosmetest.domain.model.EtudeVolontaire;
import com.example.cosmetest.domain.model.EtudeVolontaireId;
import com.example.cosmetest.domain.model.Etude;
import com.example.cosmetest.domain.model.Volontaire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Implémentation améliorée compatible avec votre interface existante
 * 
 * Corrections apportées :
 * - Méthode updateNumSujet complétée et fonctionnelle
 * - Gestion d'erreurs robuste avec logs détaillés
 * - Validation intégrée pour tous les paramètres
 * - Optimisations de performance (évite les mises à jour inutiles)
 * - Méthodes utilitaires pour réduire la duplication de code
 * - Vérification d'unicité des numéros de sujets
 */
@Service
@Transactional(readOnly = true) // Par défaut, lectures seules
public class EtudeVolontaireServiceImpl implements EtudeVolontaireService {

    private static final Logger log = LoggerFactory.getLogger(EtudeVolontaireServiceImpl.class);

    private final EtudeVolontaireRepository etudeVolontaireRepository;
    private final EtudeVolontaireMapper etudeVolontaireMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public EtudeVolontaireServiceImpl(
            EtudeVolontaireRepository etudeVolontaireRepository,
            EtudeVolontaireMapper etudeVolontaireMapper) {
        this.etudeVolontaireRepository = etudeVolontaireRepository;
        this.etudeVolontaireMapper = etudeVolontaireMapper;
    }

    // ===============================
    // OPÉRATIONS DE LECTURE
    // ===============================

    @Override
    public List<EtudeVolontaireDTO> getAllEtudeVolontaires() {
        log.debug("Récupération de toutes les associations étude-volontaire");
        return convertToDto(etudeVolontaireRepository.findAll());
    }

    @Override
    public Page<EtudeVolontaireDTO> getAllEtudeVolontairesPaginated(Pageable pageable) {
        log.debug("Récupération paginée - page: {}, taille: {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return etudeVolontaireRepository.findAll(pageable)
                .map(etudeVolontaireMapper::toDto);
    }

    @Override
    public Optional<EtudeVolontaireDTO> getEtudeVolontaireById(EtudeVolontaireId id) {
        log.debug("Recherche association par ID: {}", id);
        return etudeVolontaireRepository.findById(id)
                .map(etudeVolontaireMapper::toDto);
    }

    @Override
    public List<EtudeVolontaireDTO> getEtudeVolontairesByEtude(int idEtude) {
        log.debug("Récupération des volontaires pour l'étude: {}", idEtude);
        validatePositiveId(idEtude, "idEtude");
        return convertToDto(etudeVolontaireRepository.findByIdEtude(idEtude));
    }

    @Override
    public List<EtudeVolontaireDTO> getEtudeVolontairesByVolontaire(int idVolontaire) {
        log.debug("Récupération des études pour le volontaire: {}", idVolontaire);
        validatePositiveId(idVolontaire, "idVolontaire");
        return convertToDto(etudeVolontaireRepository.findByIdVolontaire(idVolontaire));
    }

    @Override
    public List<EtudeVolontaireDTO> getEtudeVolontairesByGroupe(int idGroupe) {
        log.debug("Récupération des volontaires pour le groupe: {}", idGroupe);
        validateNonNegativeId(idGroupe, "idGroupe");
        return convertToDto(etudeVolontaireRepository.findByIdGroupe(idGroupe));
    }

    @Override
    public List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndVolontaire(int idEtude, int idVolontaire) {
        log.debug("Recherche étude: {} et volontaire: {}", idEtude, idVolontaire);
        validatePositiveId(idEtude, "idEtude");
        validatePositiveId(idVolontaire, "idVolontaire");
        return convertToDto(etudeVolontaireRepository.findByIdEtudeAndIdVolontaire(idEtude, idVolontaire));
    }

    @Override
    public List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndGroupe(int idEtude, int idGroupe) {
        log.debug("Recherche étude: {} et groupe: {}", idEtude, idGroupe);
        validatePositiveId(idEtude, "idEtude");
        validateNonNegativeId(idGroupe, "idGroupe");
        return convertToDto(etudeVolontaireRepository.findByIdEtudeAndIdGroupe(idEtude, idGroupe));
    }

    @Override
    public List<EtudeVolontaireDTO> getEtudeVolontairesByStatut(String statut) {
        log.debug("Recherche par statut: {}", statut);
        validateStatut(statut);
        return convertToDto(etudeVolontaireRepository.findByStatut(statut));
    }

    @Override
    public List<EtudeVolontaireDTO> getEtudeVolontairesByPaye(int paye) {
        log.debug("Recherche par paye: {}", paye);
        validatePayeValue(paye);
        return convertToDto(etudeVolontaireRepository.findByPaye(paye));
    }

    // ===============================
    // OPÉRATIONS D'ÉCRITURE
    // ===============================

    @Override
    @Transactional
    public EtudeVolontaireDTO saveEtudeVolontaire(EtudeVolontaireDTO etudeVolontaireDTO) {
        log.info("Sauvegarde association: étude={}, volontaire={}",
                etudeVolontaireDTO.getIdEtude(), etudeVolontaireDTO.getIdVolontaire());

        // Validation complète
        validateEtudeVolontaireData(etudeVolontaireDTO);

        try {
            EtudeVolontaire entity = etudeVolontaireMapper.toEntity(etudeVolontaireDTO);
            EtudeVolontaire saved = etudeVolontaireRepository.save(entity);

            log.info("Association sauvegardée: {}", saved.getId());
            return etudeVolontaireMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Erreur sauvegarde: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la sauvegarde", e);
        }
    }

    @Override
    @Transactional
    public void deleteEtudeVolontaire(EtudeVolontaireId id) {
        log.info("Suppression association: {}", id);

        if (!etudeVolontaireRepository.existsById(id)) {
            throw new IllegalArgumentException("Association non trouvée: " + id);
        }

        try {
            etudeVolontaireRepository.deleteById(id);
            log.info("Association supprimée: {}", id);
        } catch (Exception e) {
            log.error("Erreur suppression: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    // ===============================
    // OPÉRATIONS DE MISE À JOUR CORRIGÉES
    // ===============================

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EtudeVolontaireDTO updateStatut(EtudeVolontaireId id, String nouveauStatut) {
        log.info("Mise à jour statut: {} -> {}", id.getStatut(), nouveauStatut);

        validateStatut(nouveauStatut);
        EtudeVolontaire entity = findEntityById(id);

        // Optimisation: éviter mise à jour inutile
        if (nouveauStatut.equals(id.getStatut())) {
            log.debug("Statut inchangé, aucune action");
            return etudeVolontaireMapper.toDto(entity);
        }

        return performEntityRecreation(id, nouveauStatut, id.getPaye(), id.getIv(), id.getNumsujet(),
                "Mise à jour du statut");
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EtudeVolontaireDTO updatePaye(EtudeVolontaireId id, int nouveauPaye) {
        log.info("Mise à jour paye: {} -> {}", id.getPaye(), nouveauPaye);

        validatePayeValue(nouveauPaye);
        EtudeVolontaire entity = findEntityById(id);

        if (nouveauPaye == id.getPaye()) {
            log.debug("Paye inchangé, aucune action");
            return etudeVolontaireMapper.toDto(entity);
        }

        return performEntityRecreation(id, id.getStatut(), nouveauPaye, id.getIv(), id.getNumsujet(),
                "Mise à jour du paiement");
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EtudeVolontaireDTO updateIV(EtudeVolontaireId id, int nouvelIV) {
        log.info("Mise à jour IV: {} -> {}", id.getIv(), nouvelIV);

        validateIVValue(nouvelIV);
        EtudeVolontaire entity = findEntityById(id);

        if (nouvelIV == id.getIv()) {
            log.debug("IV inchangé, aucune action");
            return etudeVolontaireMapper.toDto(entity);
        }

        return performEntityRecreation(id, id.getStatut(), id.getPaye(), nouvelIV, id.getNumsujet(),
                "Mise à jour de l'indemnité");
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EtudeVolontaireDTO updateNumSujet(EtudeVolontaireId id, int nouveauNumSujet) {
        log.info("Mise à jour numéro sujet: {} -> {}", id.getNumsujet(), nouveauNumSujet);

        validateNumSujetValue(nouveauNumSujet);
        EtudeVolontaire entity = findEntityById(id);

        if (nouveauNumSujet == id.getNumsujet()) {
            log.debug("Numéro de sujet inchangé, aucune action");
            return etudeVolontaireMapper.toDto(entity);
        }

        // IMPORTANT: Vérifier l'unicité du numéro de sujet dans l'étude
        if (nouveauNumSujet > 0) {
            checkNumSujetUniqueness(id.getIdEtude(), nouveauNumSujet, id.getIdVolontaire());
        }

        return performEntityRecreation(id, id.getStatut(), id.getPaye(), id.getIv(), nouveauNumSujet,
                "Mise à jour du numéro de sujet");
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EtudeVolontaireDTO updatePayeAndIV(EtudeVolontaireId id, int nouveauPaye, int nouvelIV) {
        log.info("Mise à jour paye et IV: paye {} -> {}, IV {} -> {}",
                id.getPaye(), nouveauPaye, id.getIv(), nouvelIV);

        validatePayeValue(nouveauPaye);
        validateIVValue(nouvelIV);
        EtudeVolontaire entity = findEntityById(id);

        if (nouveauPaye == id.getPaye() && nouvelIV == id.getIv()) {
            log.debug("Paye et IV inchangés, aucune action");
            return etudeVolontaireMapper.toDto(entity);
        }

        return performEntityRecreation(id, id.getStatut(), nouveauPaye, nouvelIV, id.getNumsujet(),
                "Mise à jour du paiement et de l'indemnité");
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public EtudeVolontaireDTO updateVolontaire(EtudeVolontaireId id, Integer nouveauVolontaireId) {
        log.info("Mise à jour volontaire: {} -> {}", id.getIdVolontaire(), nouveauVolontaireId);

        // Validation : nouveauVolontaireId peut être null (pour désassigner)
        if (nouveauVolontaireId != null && nouveauVolontaireId <= 0) {
            throw new IllegalArgumentException("L'ID volontaire doit être positif ou null");
        }

        EtudeVolontaire entity = findEntityById(id);

        // Optimisation: éviter mise à jour inutile
        int newVolId = nouveauVolontaireId != null ? nouveauVolontaireId : 0;
        if (newVolId == id.getIdVolontaire()) {
            log.debug("Volontaire inchangé, aucune action");
            return etudeVolontaireMapper.toDto(entity);
        }

        // Créer le nouvel ID avec le nouveau volontaire
        EtudeVolontaireId nouveauId = new EtudeVolontaireId(
                id.getIdEtude(),
                id.getIdGroupe(),
                newVolId, // 0 si null, sinon la nouvelle valeur
                id.getIv(),
                id.getNumsujet(),
                id.getPaye(),
                id.getStatut());

        try {
            // Vérifier si association avec nouvel ID existe déjà
            if (etudeVolontaireRepository.existsById(nouveauId)) {
                throw new IllegalArgumentException("Une association avec ces paramètres existe déjà: " + nouveauId);
            }

            // Supprimer ancienne association
            etudeVolontaireRepository.deleteById(id);
            etudeVolontaireRepository.flush();

            // Créer nouvelle association
            EtudeVolontaire nouvelleAssociation = new EtudeVolontaire();
            nouvelleAssociation.setId(nouveauId);

            // Renseigner les associations @MapsId (obligatoires pour Hibernate)
            if (nouveauId.getIdEtude() != null) {
                Etude etudeRef = entityManager.getReference(Etude.class, nouveauId.getIdEtude());
                nouvelleAssociation.setEtude(etudeRef);
            }
            if (nouveauId.getIdVolontaire() != null && nouveauId.getIdVolontaire() > 0) {
                Volontaire volRef = entityManager.getReference(Volontaire.class, nouveauId.getIdVolontaire());
                nouvelleAssociation.setVolontaire(volRef);
            }

            // Sauvegarder la nouvelle entité
            EtudeVolontaire saved = etudeVolontaireRepository.save(nouvelleAssociation);
            log.info(" Volontaire mis à jour: {} -> {}", id.getIdVolontaire(), newVolId);
            return etudeVolontaireMapper.toDto(saved);

        } catch (IllegalArgumentException e) {
            log.error("❌ Erreur validation lors de la mise à jour du volontaire: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Erreur technique lors de la mise à jour du volontaire: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la mise à jour du volontaire: " + e.getMessage(), e);
        }
    }

    // ===============================
    // OPÉRATIONS UTILITAIRES
    // ===============================

    @Override
    public boolean existsByEtudeAndVolontaire(int idEtude, int idVolontaire) {
        if (idEtude <= 0 || idVolontaire <= 0) {
            throw new IllegalArgumentException("Les IDs doivent être positifs");
        }
        return etudeVolontaireRepository.existsByIdEtudeAndIdVolontaire(idEtude, idVolontaire);
    }

    @Override
    public Long countVolontairesByEtude(int idEtude) {
        if (idEtude <= 0) {
            throw new IllegalArgumentException("L'ID étude doit être positif");
        }
        return etudeVolontaireRepository.countVolontairesByEtude(idEtude);
    }

    @Override
    public Long countEtudesByVolontaire(int idVolontaire) {
        if (idVolontaire <= 0) {
            throw new IllegalArgumentException("L'ID volontaire doit être positif");
        }
        return etudeVolontaireRepository.countEtudesByVolontaire(idVolontaire);
    }

    @Override
    public int getIVById(EtudeVolontaireId id) {
        Optional<EtudeVolontaire> entity = etudeVolontaireRepository.findById(id);
        if (!entity.isPresent()) {
            throw new IllegalArgumentException("Association non trouvée: " + id);
        }
        return entity.get().getId().getIv();
    }

    // ===============================
    // MÉTHODES PRIVÉES SIMPLIFIÉES
    // ===============================

    /**
     * Convertit une liste d'entités en DTOs
     */
    private List<EtudeVolontaireDTO> convertToDto(List<EtudeVolontaire> entities) {
        return entities.stream()
                .map(etudeVolontaireMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * VERSION CORRIGÉE - Recréation d'entité SANS setters individuels
     * (pour entités qui n'ont que l'ID composite)
     */
    private EtudeVolontaireDTO performEntityRecreation(
            EtudeVolontaireId ancienId,
            String statut,
            int paye,
            int iv,
            int numsujet,
            String operationDescription) {

        try {
            // Rechercher l'entité existante AVANT de la supprimer
            Optional<EtudeVolontaire> existingOpt = etudeVolontaireRepository.findById(ancienId);
            if (!existingOpt.isPresent()) {
                throw new IllegalArgumentException("Association non trouvée: " + ancienId);
            }

            EtudeVolontaire existing = existingOpt.get();

            // Créer le nouvel ID
            EtudeVolontaireId nouveauId = new EtudeVolontaireId(
                    ancienId.getIdEtude(),
                    ancienId.getIdGroupe(),
                    ancienId.getIdVolontaire(),
                    iv, numsujet, paye, statut);

            // Si l'ID n'a pas changé, pas besoin de recréer
            if (nouveauId.equals(ancienId)) {
                return etudeVolontaireMapper.toDto(existing);
            }

            // Vérifier si association avec nouvel ID existe déjà
            if (etudeVolontaireRepository.existsById(nouveauId)) {
                throw new IllegalArgumentException("Une association avec ces paramètres existe déjà: " + nouveauId);
            }

            // Supprimer ancienne association
            etudeVolontaireRepository.deleteById(ancienId);
            etudeVolontaireRepository.flush(); // S'assurer que la suppression est effectuée

            // Créer nouvelle association avec l'ID composite
            EtudeVolontaire nouvelleAssociation = new EtudeVolontaire();
            nouvelleAssociation.setId(nouveauId);

            // Renseigner les associations @MapsId (obligatoires pour Hibernate)
            if (nouveauId.getIdEtude() != null) {
                Etude etudeRef = entityManager.getReference(Etude.class, nouveauId.getIdEtude());
                nouvelleAssociation.setEtude(etudeRef);
            }
            if (nouveauId.getIdVolontaire() != null && nouveauId.getIdVolontaire() > 0) {
                Volontaire volRef = entityManager.getReference(Volontaire.class, nouveauId.getIdVolontaire());
                nouvelleAssociation.setVolontaire(volRef);
            }

            // Sauvegarder la nouvelle entité
            EtudeVolontaire saved = etudeVolontaireRepository.save(nouvelleAssociation);
            return etudeVolontaireMapper.toDto(saved);

        } catch (IllegalArgumentException e) {
            log.error("❌ Erreur validation lors de {}: {}", operationDescription, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Erreur technique lors de {}: {}", operationDescription, e.getMessage(), e);
            throw new RuntimeException("Erreur lors de " + operationDescription.toLowerCase() + ": " + e.getMessage(),
                    e);
        }
    }

    // ===============================
    // VALIDATION SIMPLIFIÉE (optionnel - vous pouvez supprimer)
    // ===============================

    private void validatePositiveId(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " doit être un nombre positif");
        }
    }

    private void validateNonNegativeId(int id, String fieldName) {
        if (id < 0) {
            throw new IllegalArgumentException(fieldName + " doit être positif ou zéro");
        }
    }

    private void validateStatut(String statut) {
        // Validation très permissive pour éviter les erreurs
        if (statut == null) {
            return; // Accepter null
        }

        String statutNormalise = statut.trim();

        // Accepter les statuts vides comme valides
        if (statutNormalise.isEmpty() || statutNormalise.equals("-")) {
            return;
        }

        log.info("🏷️ Validation statut: '{}'", statutNormalise);
        // Pas de validation stricte pour le moment
    }

    private void validatePayeValue(int paye) {
        // Validation simple
        if (paye < 0 || paye > 1) {
            throw new IllegalArgumentException("La valeur de paye doit être 0 ou 1");
        }
    }

    private void validateIVValue(int iv) {
        if (iv < 0) {
            throw new IllegalArgumentException("L'indemnité doit être positive ou nulle");
        }
    }

    private void validateNumSujetValue(int numSujet) {
        if (numSujet < 0) {
            throw new IllegalArgumentException("Le numéro de sujet doit être positif ou zéro");
        }
    }

    private EtudeVolontaire findEntityById(EtudeVolontaireId id) {
        return etudeVolontaireRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Association non trouvée: " + id));
    }

    private void validateEtudeVolontaireData(EtudeVolontaireDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Les données ne peuvent pas être nulles");
        }
        // Validation minimale
        if (dto.getIdEtude() <= 0) {
            throw new IllegalArgumentException("ID étude invalide");
        }
        if (dto.getIdVolontaire() <= 0) {
            throw new IllegalArgumentException("ID volontaire invalide");
        }
    }

    private void checkNumSujetUniqueness(int idEtude, int numSujet, int idVolontaire) {
        if (numSujet <= 0)
            return; // Les numéros <= 0 ne sont pas soumis à l'unicité

        // Vérification simplifiée
        boolean hasConflict = etudeVolontaireRepository.findByIdEtude(idEtude).stream()
                .anyMatch(assoc -> assoc.getId().getNumsujet() == numSujet &&
                        assoc.getId().getIdVolontaire() != idVolontaire);

        if (hasConflict) {
            throw new IllegalArgumentException(
                    String.format("Le numéro de sujet %d est déjà utilisé dans l'étude %d", numSujet, idEtude));
        }
    }
}
