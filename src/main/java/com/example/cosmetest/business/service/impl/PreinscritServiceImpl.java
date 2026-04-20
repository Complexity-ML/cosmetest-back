package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PreinscritDTO;
import com.example.cosmetest.business.mapper.PreinscritMapper;
import com.example.cosmetest.business.service.PreinscritService;
import com.example.cosmetest.domain.model.Preinscrit;
import com.example.cosmetest.data.repository.PreinscritRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implémentation des services métier pour l'entité Preinscrit
 */
@Service
@Transactional
public class PreinscritServiceImpl implements PreinscritService {

    private final PreinscritRepository preinscritRepository;
    private final PreinscritMapper preinscritMapper;

    // Liste des états valides
    private static final List<String> ETATS_VALIDES = Arrays.asList("En attente", "Validé", "Refusé", "Annulé");

    // Regex pour la validation d'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    // Regex pour la validation des numéros de téléphone
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    public PreinscritServiceImpl(PreinscritRepository preinscritRepository, PreinscritMapper preinscritMapper) {
        this.preinscritRepository = preinscritRepository;
        this.preinscritMapper = preinscritMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getAllPreinscrits() {
        List<Preinscrit> preinscrits = preinscritRepository.findAll();
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PreinscritDTO> getPreinscritById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return preinscritRepository.findById(id)
                .map(preinscritMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PreinscritDTO> getPreinscritByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return preinscritRepository.findByEmail(email)
                .map(preinscritMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findByNom(nom);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsByPrenom(String prenom) {
        if (prenom == null || prenom.trim().isEmpty()) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findByPrenom(prenom);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsByNomAndPrenom(String nom, String prenom) {
        if (nom == null || nom.trim().isEmpty() || prenom == null || prenom.trim().isEmpty()) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findByNomAndPrenom(nom, prenom);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsByEtat(String etat) {
        if (etat == null || etat.trim().isEmpty()) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findByEtat(etat);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsByEthnie(String ethnie) {
        if (ethnie == null || ethnie.trim().isEmpty()) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findByEthnie(ethnie);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsBySexe(String sexe) {
        if (sexe == null || sexe.trim().isEmpty()) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findBySexe(sexe);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsByRdvDate(Date rdvDate) {
        if (rdvDate == null) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findByRdvDate(rdvDate);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreinscritDTO> getPreinscritsByPhototype(String phototype) {
        if (phototype == null || phototype.trim().isEmpty()) {
            return List.of();
        }
        List<Preinscrit> preinscrits = preinscritRepository.findByPhototype(phototype);
        return preinscritMapper.toDTOList(preinscrits);
    }

    @Override
    public PreinscritDTO createPreinscrit(PreinscritDTO preinscritDTO) {
        validatePreinscrit(preinscritDTO);

        // Vérifier si l'email existe déjà
        if (preinscritRepository.existsByEmail(preinscritDTO.getEmail())) {
            throw new IllegalArgumentException("Un préinscrit avec cet email existe déjà");
        }

        // Définir la date de préinscription si non fournie
        if (preinscritDTO.getDatePreInscription() == null || preinscritDTO.getDatePreInscription().trim().isEmpty()) {
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            preinscritDTO.setDatePreInscription(today.format(formatter));
        }

        // Définir l'état par défaut
        if (preinscritDTO.getEtat() == null || preinscritDTO.getEtat().trim().isEmpty()) {
            preinscritDTO.setEtat("En attente");
        }

        Preinscrit preinscrit = preinscritMapper.toEntity(preinscritDTO);
        preinscrit.setIdPreinscrit(null); // Assurer que l'ID est null pour la création
        Preinscrit savedPreinscrit = preinscritRepository.save(preinscrit);
        return preinscritMapper.toDTO(savedPreinscrit);
    }

    @Override
    public Optional<PreinscritDTO> updatePreinscrit(Integer id, PreinscritDTO preinscritDTO) {
        if (id == null || !preinscritRepository.existsById(id)) {
            return Optional.empty();
        }

        validatePreinscritForUpdate(id, preinscritDTO);

        return preinscritRepository.findById(id)
                .map(existingPreinscrit -> {
                    preinscritDTO.setIdPreinscrit(id); // Assurer l'ID correct
                    Preinscrit updatedPreinscrit = preinscritMapper.updateEntityFromDTO(existingPreinscrit, preinscritDTO);
                    return preinscritMapper.toDTO(preinscritRepository.save(updatedPreinscrit));
                });
    }

    @Override
    public boolean deletePreinscrit(Integer id) {
        if (id == null || !preinscritRepository.existsById(id)) {
            return false;
        }

        preinscritRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        if (id == null) {
            return false;
        }
        return preinscritRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return preinscritRepository.existsByEmail(email);
    }

    @Override
    public Optional<PreinscritDTO> updateEtat(Integer id, String etat) {
        if (id == null || !preinscritRepository.existsById(id)) {
            return Optional.empty();
        }

        if (etat == null || etat.trim().isEmpty()) {
            throw new IllegalArgumentException("L'état ne peut pas être vide");
        }

        if (!ETATS_VALIDES.contains(etat)) {
            throw new IllegalArgumentException("L'état doit être l'un des suivants : " + String.join(", ", ETATS_VALIDES));
        }

        return preinscritRepository.findById(id)
                .map(preinscrit -> {
                    preinscrit.setEtat(etat);
                    return preinscritMapper.toDTO(preinscritRepository.save(preinscrit));
                });
    }

    @Override
    public Optional<PreinscritDTO> planifierRendezVous(Integer id, Date rdvDate, String rdvHeure) {
        if (id == null || !preinscritRepository.existsById(id)) {
            return Optional.empty();
        }

        if (rdvDate == null) {
            throw new IllegalArgumentException("La date de rendez-vous ne peut pas être nulle");
        }

        // Vérifier que la date n'est pas dans le passé
        LocalDate today = LocalDate.now();
        LocalDate rdvLocalDate = rdvDate.toLocalDate();
        if (rdvLocalDate.isBefore(today)) {
            throw new IllegalArgumentException("La date de rendez-vous ne peut pas être dans le passé");
        }

        if (rdvHeure == null || rdvHeure.trim().isEmpty()) {
            throw new IllegalArgumentException("L'heure de rendez-vous ne peut pas être vide");
        }

        // Format d'heure simple (on pourrait ajouter une validation plus complexe)
        Pattern heurePattern = Pattern.compile("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$");
        if (!heurePattern.matcher(rdvHeure).matches()) {
            throw new IllegalArgumentException("Le format de l'heure doit être HH:MM");
        }

        return preinscritRepository.findById(id)
                .map(preinscrit -> {
                    preinscrit.setRdvDate(rdvDate);
                    preinscrit.setRdvHeure(rdvHeure);
                    return preinscritMapper.toDTO(preinscritRepository.save(preinscrit));
                });
    }

    /**
     * Valide les données d'un PreinscritDTO pour la création
     *
     * @param preinscritDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validatePreinscrit(PreinscritDTO preinscritDTO) {
        if (preinscritDTO == null) {
            throw new IllegalArgumentException("Le préinscrit ne peut pas être null");
        }

        // Validations des champs obligatoires
        if (preinscritDTO.getTitre() == null || preinscritDTO.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide");
        }

        List<String> titresValides = Arrays.asList("M.", "Mme", "Mlle");
        if (!titresValides.contains(preinscritDTO.getTitre())) {
            throw new IllegalArgumentException("Le titre doit être l'un des suivants : " + String.join(", ", titresValides));
        }

        if (preinscritDTO.getNom() == null || preinscritDTO.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }

        if (preinscritDTO.getPrenom() == null || preinscritDTO.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }

        if (preinscritDTO.getEmail() == null || preinscritDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }

        if (!EMAIL_PATTERN.matcher(preinscritDTO.getEmail()).matches()) {
            throw new IllegalArgumentException("Le format de l'email est invalide");
        }

        if (preinscritDTO.getSexe() == null || preinscritDTO.getSexe().trim().isEmpty()) {
            throw new IllegalArgumentException("Le sexe ne peut pas être vide");
        }

        if (!preinscritDTO.getSexe().equals("M") && !preinscritDTO.getSexe().equals("F")) {
            throw new IllegalArgumentException("Le sexe doit être 'M' ou 'F'");
        }

        // Validations conditionnelles
        if (preinscritDTO.getTelPortable() != null && !preinscritDTO.getTelPortable().trim().isEmpty() &&
                !PHONE_PATTERN.matcher(preinscritDTO.getTelPortable()).matches()) {
            throw new IllegalArgumentException("Le format du téléphone portable est invalide");
        }

        if (preinscritDTO.getTelDomicile() != null && !preinscritDTO.getTelDomicile().trim().isEmpty() &&
                !PHONE_PATTERN.matcher(preinscritDTO.getTelDomicile()).matches()) {
            throw new IllegalArgumentException("Le format du téléphone domicile est invalide");
        }

        if (preinscritDTO.getCodePostal() != null && !preinscritDTO.getCodePostal().trim().isEmpty() &&
                !preinscritDTO.getCodePostal().matches("^[0-9]{5}$")) {
            throw new IllegalArgumentException("Le code postal doit contenir 5 chiffres");
        }

        if (preinscritDTO.getPhototype() != null && !preinscritDTO.getPhototype().trim().isEmpty() &&
                !preinscritDTO.getPhototype().matches("^[1-6]$")) {
            throw new IllegalArgumentException("Le phototype doit être un chiffre entre 1 et 6");
        }

        if (preinscritDTO.getPeauSensible() != null && !preinscritDTO.getPeauSensible().trim().isEmpty() &&
                !preinscritDTO.getPeauSensible().equals("Oui") && !preinscritDTO.getPeauSensible().equals("Non")) {
            throw new IllegalArgumentException("La peau sensible doit être 'Oui' ou 'Non'");
        }

        if (preinscritDTO.getEtat() != null && !preinscritDTO.getEtat().trim().isEmpty() &&
                !ETATS_VALIDES.contains(preinscritDTO.getEtat())) {
            throw new IllegalArgumentException("L'état doit être l'un des suivants : " + String.join(", ", ETATS_VALIDES));
        }
    }

    /**
     * Valide les données d'un PreinscritDTO pour la mise à jour
     *
     * @param id l'ID du préinscrit à mettre à jour
     * @param preinscritDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validatePreinscritForUpdate(Integer id, PreinscritDTO preinscritDTO) {
        if (preinscritDTO == null) {
            throw new IllegalArgumentException("Le préinscrit ne peut pas être null");
        }

        // Vérifier si l'email a changé et s'il existe déjà
        if (preinscritDTO.getEmail() != null && !preinscritDTO.getEmail().trim().isEmpty()) {
            Optional<Preinscrit> existingByEmail = preinscritRepository.findByEmail(preinscritDTO.getEmail());
            if (existingByEmail.isPresent() && !existingByEmail.get().getIdPreinscrit().equals(id)) {
                throw new IllegalArgumentException("Un préinscrit avec cet email existe déjà");
            }

            if (!EMAIL_PATTERN.matcher(preinscritDTO.getEmail()).matches()) {
                throw new IllegalArgumentException("Le format de l'email est invalide");
            }
        }

        // Validations des champs mis à jour
        if (preinscritDTO.getTitre() != null && !preinscritDTO.getTitre().trim().isEmpty()) {
            List<String> titresValides = Arrays.asList("M.", "Mme", "Mlle");
            if (!titresValides.contains(preinscritDTO.getTitre())) {
                throw new IllegalArgumentException("Le titre doit être l'un des suivants : " + String.join(", ", titresValides));
            }
        }

        if (preinscritDTO.getSexe() != null && !preinscritDTO.getSexe().trim().isEmpty() &&
                !preinscritDTO.getSexe().equals("M") && !preinscritDTO.getSexe().equals("F")) {
            throw new IllegalArgumentException("Le sexe doit être 'M' ou 'F'");
        }

        if (preinscritDTO.getTelPortable() != null && !preinscritDTO.getTelPortable().trim().isEmpty() &&
                !PHONE_PATTERN.matcher(preinscritDTO.getTelPortable()).matches()) {
            throw new IllegalArgumentException("Le format du téléphone portable est invalide");
        }

        if (preinscritDTO.getTelDomicile() != null && !preinscritDTO.getTelDomicile().trim().isEmpty() &&
                !PHONE_PATTERN.matcher(preinscritDTO.getTelDomicile()).matches()) {
            throw new IllegalArgumentException("Le format du téléphone domicile est invalide");
        }

        if (preinscritDTO.getCodePostal() != null && !preinscritDTO.getCodePostal().trim().isEmpty() &&
                !preinscritDTO.getCodePostal().matches("^[0-9]{5}$")) {
            throw new IllegalArgumentException("Le code postal doit contenir 5 chiffres");
        }

        if (preinscritDTO.getPhototype() != null && !preinscritDTO.getPhototype().trim().isEmpty() &&
                !preinscritDTO.getPhototype().matches("^[1-6]$")) {
            throw new IllegalArgumentException("Le phototype doit être un chiffre entre 1 et 6");
        }

        if (preinscritDTO.getPeauSensible() != null && !preinscritDTO.getPeauSensible().trim().isEmpty() &&
                !preinscritDTO.getPeauSensible().equals("Oui") && !preinscritDTO.getPeauSensible().equals("Non")) {
            throw new IllegalArgumentException("La peau sensible doit être 'Oui' ou 'Non'");
        }

        if (preinscritDTO.getEtat() != null && !preinscritDTO.getEtat().trim().isEmpty() &&
                !ETATS_VALIDES.contains(preinscritDTO.getEtat())) {
            throw new IllegalArgumentException("L'état doit être l'un des suivants : " + String.join(", ", ETATS_VALIDES));
        }
    }

    @Override
    public int countPreinscrits() {
        return (int) preinscritRepository.count();
    }

    @Override
    public int countNewPreinscritsToday() {
        LocalDate today = LocalDate.now();
        Date startOfDay = java.sql.Date.valueOf(today);
        Date endOfDay = java.sql.Date.valueOf(today.plusDays(1));

        // Utilisez l'attribut correct pour la date d'inscription
        // Remplacez "dateInscription" par le nom réel de l'attribut dans votre entité
        int count = 0;
        for (Preinscrit p : preinscritRepository.findAll()) {
            Date dateInscription = Date.valueOf(p.getDatePreInscription()); // Utilisez le bon nom
            if (dateInscription != null &&
                    dateInscription.compareTo(startOfDay) >= 0 &&
                    dateInscription.compareTo(endOfDay) < 0) {
                count++;
            }
        }
        return count;
    }
}