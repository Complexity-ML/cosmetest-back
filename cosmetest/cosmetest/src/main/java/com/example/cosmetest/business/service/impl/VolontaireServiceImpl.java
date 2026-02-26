package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolontaireDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.mapper.VolontaireMapper;
import com.example.cosmetest.business.service.VolontaireService;
import com.example.cosmetest.domain.model.Volontaire;
import com.example.cosmetest.data.repository.VolontaireRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * Implémentation des services métier pour l'entité Volontaire
 */
@Service
@Transactional
public class VolontaireServiceImpl implements VolontaireService {

    private final VolontaireRepository volontaireRepository;
    private final VolontaireMapper volontaireMapper;
    private static final Logger logger = LoggerFactory.getLogger(VolontaireServiceImpl.class);

    @Value("${photo.server.url}")
    private String photoServerUrl;

    @Value("${photo.check.enabled:true}") // true par défaut
    private boolean photoCheckEnabled;

    @Value("${photo.connection.timeout:5000}") // 5000ms par défaut
    private int photoConnectionTimeout;

    @Value("${photo.read.timeout:5000}") // 5000ms par défaut
    private int photoReadTimeout;

    public VolontaireServiceImpl(VolontaireRepository volontaireRepository, VolontaireMapper volontaireMapper) {
        this.volontaireRepository = volontaireRepository;
        this.volontaireMapper = volontaireMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getAllVolontaires() {
        List<Volontaire> volontaires = volontaireRepository.findAll();
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getAllActiveVolontaires() {
        try {
            // Étape 1 : Récupérer les volontaires du repository
            List<Volontaire> volontaires = null;
            try {
                volontaires = volontaireRepository.findByArchive(false);
                logger.debug("Repository a retourné {} volontaires", volontaires.size());
            } catch (Exception e) {
                logger.error("Erreur lors de la récupération des volontaires", e);
                e.printStackTrace();
                throw e;
            }

            // Étape 2 : Créer une liste mutable
            List<VolontaireDTO> result = new ArrayList<>();

            // Étape 3 : Remplir la liste en convertissant chaque volontaire
            try {
                for (Volontaire volontaire : volontaires) {
                    VolontaireDTO dto = volontaireMapper.toDTO(volontaire);
                    result.add(dto);
                }
                logger.debug("Conversion en DTOs réussie");
            } catch (Exception e) {
                logger.error("Erreur lors de la conversion des volontaires", e);
                e.printStackTrace();
                throw e;
            }

            return result;
        } catch (Exception e) {
            logger.error("Erreur générale dans getAllActiveVolontaires", e);
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VolontaireDTO> getVolontaireById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return volontaireRepository.findById(id)
                .map(volontaireMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VolontaireDetailDTO> getVolontaireDetailById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return volontaireRepository.findById(id)
                .map(volontaireMapper::toDetailDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> searchVolontairesByNomPrenom(String nom, String prenom) {
        List<Volontaire> volontaires;

        if (nom != null && prenom != null) {
            volontaires = volontaireRepository.findByNomVolAndPrenomVol(nom, prenom);
        } else if (nom != null) {
            volontaires = volontaireRepository.findByNomVol(nom);
        } else if (prenom != null) {
            volontaires = volontaireRepository.findByPrenomVol(prenom);
        } else {
            volontaires = volontaireRepository.findAll();
        }

        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VolontaireDTO> searchVolontaires(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return volontaireRepository.findAll(pageable).map(volontaireMapper::toDTO);
        }

        // Nettoyer le mot-clé pour éviter les injections SQL
        String cleanKeyword = keyword.trim()
                .replaceAll("[%_\\[\\]^]", ""); // Enlever les caractères spéciaux SQL

        // Ajouter les wildcards pour la recherche partielle
        String searchTerm = "%" + cleanKeyword + "%";

        // Utiliser la méthode du repository qui exploite l'index fulltext
        Page<Volontaire> volontairesPage = volontaireRepository.findByFullTextSearch(searchTerm, pageable);

        // Convertir les entités en DTOs
        return volontairesPage.map(volontaireMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> searchVolontaires(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return getAllVolontaires();
        }

        List<Volontaire> volontaires = volontaireRepository.searchVolontaires(searchText);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> searchVolontairesByCriteria(String sexe, Integer ageMin, Integer ageMax,
            String ethnie, String phototype, String typePeau) {
        List<Volontaire> volontaires = volontaireRepository.findAll();

        // Filtrer par sexe
        if (sexe != null && !sexe.trim().isEmpty()) {
            volontaires = volontaires.stream()
                    .filter(v -> sexe.equals(v.getSexe()))
                    .collect(Collectors.toList());
        }

        // Filtrer par âge
        if (ageMin != null || ageMax != null) {
            LocalDate now = LocalDate.now();
            LocalDate minBirthDate = (ageMax != null) ? now.minusYears(ageMax) : null;
            LocalDate maxBirthDate = (ageMin != null) ? now.minusYears(ageMin) : null;

            volontaires = volontaires.stream()
                    .filter(v -> {
                        LocalDate birthDate = v.getDateNaissance();
                        if (birthDate == null) {
                            return false;
                        }

                        boolean isAfterMinBirthDate = minBirthDate == null ||
                                !birthDate.isBefore(minBirthDate);
                        boolean isBeforeMaxBirthDate = maxBirthDate == null ||
                                !birthDate.isAfter(maxBirthDate);

                        return isAfterMinBirthDate && isBeforeMaxBirthDate;
                    })
                    .collect(Collectors.toList());
        }

        // Filtrer par ethnie
        if (ethnie != null && !ethnie.trim().isEmpty()) {
            volontaires = volontaires.stream()
                    .filter(v -> ethnie.equals(v.getEthnie()))
                    .collect(Collectors.toList());
        }

        // Filtrer par phototype
        if (phototype != null && !phototype.trim().isEmpty()) {
            volontaires = volontaires.stream()
                    .filter(v -> phototype.equals(v.getPhototype()))
                    .collect(Collectors.toList());
        }

        // Filtrer par type de peau
        if (typePeau != null && !typePeau.trim().isEmpty()) {
            volontaires = volontaires.stream()
                    .filter(v -> typePeau.equals(v.getTypePeauVisage()))
                    .collect(Collectors.toList());
        }

        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    public VolontaireDTO createVolontaire(VolontaireDTO volontaireDTO) {
        validateVolontaire(volontaireDTO);

        Volontaire volontaire = volontaireMapper.toEntity(volontaireDTO);
        volontaire.setIdVol(null); // Assurer que l'ID est null pour la création

        // Définir la date d'insertion si elle n'est pas définie
        if (volontaire.getDateI() == null) {
            volontaire.setDateI(Date.valueOf(LocalDate.now()));
        }

        // Définir l'état d'archivage par défaut
        if (volontaire.getArchive() == null) {
            volontaire.setArchive(false);
        }

        Volontaire savedVolontaire = volontaireRepository.save(volontaire);
        return volontaireMapper.toDTO(savedVolontaire);
    }

    @Override
    public VolontaireDetailDTO createVolontaireDetail(VolontaireDetailDTO volontaireDetailDTO) {
        validateVolontaire(volontaireDetailDTO);

        Volontaire volontaire = volontaireMapper.toEntity(volontaireDetailDTO);
        volontaire.setIdVol(null); // Assurer que l'ID est null pour la création

        // Définir la date d'insertion si elle n'est pas définie
        if (volontaire.getDateI() == null) {
            volontaire.setDateI(Date.valueOf(LocalDate.now()));
        }

        // Définir l'état d'archivage par défaut
        if (volontaire.getArchive() == null) {
            volontaire.setArchive(false);
        }

        Volontaire savedVolontaire = volontaireRepository.save(volontaire);
        return volontaireMapper.toDetailDTO(savedVolontaire);
    }

    @Override
    public Optional<VolontaireDTO> updateVolontaire(Integer id, VolontaireDTO volontaireDTO) {
        if (id == null || !volontaireRepository.existsById(id)) {
            return Optional.empty();
        }

        validateVolontaire(volontaireDTO);

        return volontaireRepository.findById(id)
                .map(existingVolontaire -> {
                    volontaireDTO.setIdVol(id); // Assurer l'ID correct
                    Volontaire updatedVolontaire = volontaireMapper.updateEntityFromDTO(existingVolontaire,
                            volontaireDTO);
                    return volontaireMapper.toDTO(volontaireRepository.save(updatedVolontaire));
                });
    }

    @Override
    public Optional<VolontaireDetailDTO> updateVolontaireDetail(Integer id, VolontaireDetailDTO volontaireDetailDTO) {
        if (id == null || !volontaireRepository.existsById(id)) {
            return Optional.empty();
        }

        // Pas de validateVolontaire ici : cet endpoint met à jour les détails (peau, cheveux, etc.)
        // Les champs obligatoires (nomVol, prenomVol) sont déjà en base et ne sont pas modifiés

        return volontaireRepository.findById(id)
                .map(existingVolontaire -> {
                    volontaireDetailDTO.setIdVol(id); // Assurer l'ID correct
                    Volontaire updatedVolontaire = volontaireMapper.updateEntityFromDetailDTO(existingVolontaire,
                            volontaireDetailDTO);
                    return volontaireMapper.toDetailDTO(volontaireRepository.save(updatedVolontaire));
                });
    }

    @Override
    public Optional<VolontaireDTO> toggleArchiveVolontaire(Integer id, boolean archive) {
        if (id == null || !volontaireRepository.existsById(id)) {
            return Optional.empty();
        }

        return volontaireRepository.findById(id)
                .map(volontaire -> {
                    volontaire.setArchive(archive);
                    return volontaireMapper.toDTO(volontaireRepository.save(volontaire));
                });
    }

    @Override
    public boolean deleteVolontaire(Integer id) {
        if (id == null || !volontaireRepository.existsById(id)) {
            return false;
        }

        volontaireRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        if (id == null) {
            return false;
        }
        return volontaireRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesByAgeRange(Integer ageMin, Integer ageMax) {
        if (ageMin == null && ageMax == null) {
            return getAllVolontaires();
        }

        LocalDate now = LocalDate.now();
        Date minBirthDate = (ageMax != null) ? Date.valueOf(now.minusYears(ageMax)) : null;
        Date maxBirthDate = (ageMin != null) ? Date.valueOf(now.minusYears(ageMin)) : null;

        List<Volontaire> volontaires = volontaireRepository.findByAgeBetween(minBirthDate, maxBirthDate);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesBySexe(String sexe) {
        if (sexe == null || sexe.trim().isEmpty()) {
            return List.of();
        }

        List<Volontaire> volontaires = volontaireRepository.findBySexe(sexe);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesByEthnie(String ethnie) {
        if (ethnie == null || ethnie.trim().isEmpty()) {
            return List.of();
        }

        List<Volontaire> volontaires = volontaireRepository.findByEthnie(ethnie);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesByPhototype(String phototype) {
        if (phototype == null || phototype.trim().isEmpty()) {
            return List.of();
        }

        List<Volontaire> volontaires = volontaireRepository.findByPhototype(phototype);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesByTypePeau(String typePeau) {
        if (typePeau == null || typePeau.trim().isEmpty()) {
            return List.of();
        }

        List<Volontaire> volontaires = volontaireRepository.findByTypePeauVisage(typePeau);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesByAcne(String acne) {
        if (acne == null || acne.trim().isEmpty()) {
            return List.of();
        }

        List<Volontaire> volontaires = volontaireRepository.findByAcne(acne);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesBySanteCompatible(String santeCompatible) {
        if (santeCompatible == null || santeCompatible.trim().isEmpty()) {
            return List.of();
        }

        List<Volontaire> volontaires = volontaireRepository.findBySanteCompatible(santeCompatible);
        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isVolontaireCompatibleWithAgeRange(Integer idVol, Integer ageMin, Integer ageMax) {
        if (idVol == null || (ageMin == null && ageMax == null)) {
            return false;
        }

        Optional<Volontaire> volontaireOpt = volontaireRepository.findById(idVol);
        if (volontaireOpt.isEmpty() || volontaireOpt.get().getDateNaissance() == null) {
            return false;
        }

        Volontaire volontaire = volontaireOpt.get();
        LocalDate birthDate = volontaire.getDateNaissance();
        if (birthDate == null) {
            return false;
        }

        int age = Period.between(birthDate, LocalDate.now()).getYears();

        boolean isAboveMinAge = ageMin == null || age >= ageMin;
        boolean isBelowMaxAge = ageMax == null || age <= ageMax;

        return isAboveMinAge && isBelowMaxAge;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calculateVolontaireAge(Integer idVol) {
        if (idVol == null) {
            return null;
        }

        Optional<Volontaire> volontaireOpt = volontaireRepository.findById(idVol);
        if (volontaireOpt.isEmpty() || volontaireOpt.get().getDateNaissance() == null) {
            return null;
        }

        Volontaire volontaire = volontaireOpt.get();
        LocalDate birthDate = volontaire.getDateNaissance();
        if (birthDate == null) {
            return null;
        }

        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireDTO> getVolontairesAddedBetweenDates(LocalDate dateDebut, LocalDate dateFin) {
        if (dateDebut == null || dateFin == null) {
            return List.of();
        }

        Date sqlDateDebut = Date.valueOf(dateDebut);
        Date sqlDateFin = Date.valueOf(dateFin);

        List<Volontaire> volontaires = volontaireRepository.findAll()
                .stream()
                .filter(v -> {
                    Date dateI = Date.valueOf(v.getDateI());
                    return dateI != null &&
                            (dateI.after(sqlDateDebut) || dateI.equals(sqlDateDebut)) &&
                            (dateI.before(sqlDateFin) || dateI.equals(sqlDateFin));
                })
                .collect(Collectors.toList());

        return volontaireMapper.toDTOList(volontaires);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getVolontairesStatistics() {
        List<Volontaire> allVolontaires = volontaireRepository.findAll();
        int totalVolontaires = allVolontaires.size();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalVolontaires", totalVolontaires);

        // Répartition par sexe
        Map<String, Long> sexeDistribution = allVolontaires.stream()
                .filter(v -> v.getSexe() != null)
                .collect(Collectors.groupingBy(Volontaire::getSexe, Collectors.counting()));
        statistics.put("sexeDistribution", sexeDistribution);

        // Répartition par ethnie
        Map<String, Long> ethnieDistribution = allVolontaires.stream()
                .filter(v -> v.getEthnie() != null)
                .collect(Collectors.groupingBy(Volontaire::getEthnie, Collectors.counting()));
        statistics.put("ethnieDistribution", ethnieDistribution);

        // Répartition par phototype
        Map<String, Long> phototypeDistribution = allVolontaires.stream()
                .filter(v -> v.getPhototype() != null)
                .collect(Collectors.groupingBy(Volontaire::getPhototype, Collectors.counting()));
        statistics.put("phototypeDistribution", phototypeDistribution);

        // Répartition par type de peau
        Map<String, Long> typePeauDistribution = allVolontaires.stream()
                .filter(v -> v.getTypePeauVisage() != null)
                .collect(Collectors.groupingBy(Volontaire::getTypePeauVisage, Collectors.counting()));
        statistics.put("typePeauDistribution", typePeauDistribution);

        // Répartition par tranche d'âge
        Map<String, Long> ageDistribution = new HashMap<>();
        LocalDate now = LocalDate.now();

        allVolontaires.stream()
                .filter(v -> v.getDateNaissance() != null)
                .forEach(v -> {
                    LocalDate birthDate = v.getDateNaissance();
                    int age = Period.between(birthDate, now).getYears();

                    String ageRange;
                    if (age < 18) {
                        ageRange = "Moins de 18 ans";
                    } else if (age < 30) {
                        ageRange = "18-29 ans";
                    } else if (age < 40) {
                        ageRange = "30-39 ans";
                    } else if (age < 50) {
                        ageRange = "40-49 ans";
                    } else if (age < 60) {
                        ageRange = "50-59 ans";
                    } else {
                        ageRange = "60 ans et plus";
                    }

                    ageDistribution.merge(ageRange, 1L, Long::sum);
                });

        statistics.put("ageDistribution", ageDistribution);

        return statistics;
    }

    /**
     * Valide les données d'un VolontaireDTO
     *
     * @param volontaireDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateVolontaire(VolontaireDTO volontaireDTO) {
        if (volontaireDTO == null) {
            throw new IllegalArgumentException("Le volontaire ne peut pas être null");
        }

        if (volontaireDTO.getNomVol() == null || volontaireDTO.getNomVol().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }

        if (volontaireDTO.getPrenomVol() == null || volontaireDTO.getPrenomVol().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }

        if (volontaireDTO.getEmailVol() != null && !volontaireDTO.getEmailVol().trim().isEmpty()) {
            String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            if (!volontaireDTO.getEmailVol().matches(emailRegex)) {
                throw new IllegalArgumentException("Le format de l'email est invalide");
            }
        }

        if (volontaireDTO.getCpVol() != null && !volontaireDTO.getCpVol().trim().isEmpty()) {
            String cpRegex = "^[0-9]{5}$";
            if (!volontaireDTO.getCpVol().matches(cpRegex)) {
                throw new IllegalArgumentException("Le code postal doit contenir 5 chiffres");
            }
        }

        if (volontaireDTO.getSexe() != null && !volontaireDTO.getSexe().trim().isEmpty()) {
            String sexe = volontaireDTO.getSexe().trim();
            if (!(sexe.equalsIgnoreCase("F") || sexe.equalsIgnoreCase("M") ||
                    sexe.equalsIgnoreCase("Masculin") || sexe.equalsIgnoreCase("Feminin")
                    || sexe.equalsIgnoreCase("Féminin") ||
                    sexe.equalsIgnoreCase("Autre"))) {
                throw new IllegalArgumentException("Le sexe doit être F, M, MASCULIN, FEMININ ou Autre");
            }
        }

        if (volontaireDTO.getDateNaissance() != null) {
            LocalDate now = LocalDate.now();
            if (volontaireDTO.getDateNaissance().isAfter(now)) {
                throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur");
            }
        }

        if (volontaireDTO.getPoids() != null && volontaireDTO.getPoids() < 0) {
            throw new IllegalArgumentException("Le poids doit être positif ou zéro");
        }

        if (volontaireDTO.getTaille() != null && volontaireDTO.getTaille() < 0) {
            throw new IllegalArgumentException("La taille doit être positive ou zéro");
        }
    }

    @Override
    public int countActiveVolontaires() {
        // Utilisez countByArchive(false) au lieu de countByArchiveFalse()
        return volontaireRepository.countByArchive(false);
    }

    @Override
    public int countVolontairesAddedToday() {
        LocalDate today = LocalDate.now();
        // Convertir en java.sql.Date pour la compatibilité
        Date startOfDay = Date.valueOf(today);
        Date endOfDay = Date.valueOf(today.plusDays(1));

        return volontaireRepository.countByDateIBetween(startOfDay, endOfDay);
    }

    @Override
    public List<VolontaireDTO> getRecentVolontaires(int limit) {
        // Utilisez Pageable pour limiter les résultats
        Pageable pageable = PageRequest.of(0, limit);
        List<Volontaire> volontaires = volontaireRepository.findRecentVolontaires(pageable);

        return volontaires.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private VolontaireDTO convertToDTO(Volontaire volontaire) {
        VolontaireDTO dto = new VolontaireDTO();
        dto.setIdVol(volontaire.getIdVol());
        dto.setNomVol(volontaire.getNomVol());
        dto.setPrenomVol(volontaire.getPrenomVol());

        // Vérifiez si votre DTO a une méthode setDateAjout
        // Si non, vous devrez l'ajouter ou adapter cette partie
        if (volontaire.getDateI() != null) {
            // Assurez-vous que cette méthode existe dans votre DTO
            dto.setDateI(volontaire.getDateI());
        }

        // Autres mappings...
        return dto;
    }

    @Override
    public Map<String, Object> getVolontairePhoto(Integer id, String photoType) {
        return getVolontaireById(id)
                .map(volontaire -> {
                    String nomVolontaire = volontaire.getNomVol();
                    LocalDate dateNaissance = volontaire.getDateNaissance();

                    if (nomVolontaire == null || dateNaissance == null) {
                        return null;
                    }

                    // Formatage de la date
                    String dateFormatted = dateNaissance.format(DateTimeFormatter.ofPattern("ddMMyy"));
                    logger.debug("Recherche photo pour: nom='{}', date='{}', type='{}'",
                            nomVolontaire, dateFormatted, photoType);

                    // Construction du préfixe selon le type de photo
                    String prefix;
                    switch (photoType.toLowerCase()) {
                        case "face":
                            prefix = "f_";
                            break;
                        case "face_pp":
                            prefix = "fpp_";
                            break;
                        case "face_pc":
                            prefix = "fpc_";
                            break;
                        case "droite":
                            prefix = "d_";
                            break;
                        case "droite_pp":
                            prefix = "dpp_";
                            break;
                        case "droite_pc":
                            prefix = "dpc_";
                            break;
                        case "gauche":
                            prefix = "g_";
                            break;
                        case "gauche_pp":
                            prefix = "gpp_";
                            break;
                        case "gauche_pc":
                            prefix = "gpc_";
                            break;
                        default:
                            return null;
                    }

                    // Tester toutes les variantes possibles
                    List<String> fileVariants = generatePhotoFileVariants(prefix, nomVolontaire, dateFormatted);
                    logger.debug("Génération de {} variantes à tester pour {}", fileVariants.size(), nomVolontaire);

                    for (int i = 0; i < fileVariants.size(); i++) {
                        String fileName = fileVariants.get(i);
                        logger.debug("Test variante {}/{}: '{}'", i + 1, fileVariants.size(), fileName);

                        if (checkPhotoExists(fileName)) {
                            logger.debug("Photo trouvée: '{}'", fileName);
                            Map<String, Object> result = new HashMap<>();
                            result.put("photoUrl", photoServerUrl + fileName);
                            result.put("fileName", fileName);
                            result.put("exists", true);
                            return result;
                        }
                    }

                    // Aucune variante trouvée
                    String originalFileName = prefix + nomVolontaire.toLowerCase() + dateFormatted + ".JPG";
                    logger.debug("Aucune photo trouvée après {} tentatives pour {}", fileVariants.size(), nomVolontaire);

                    Map<String, Object> result = new HashMap<>();
                    result.put("photoUrl", null);
                    result.put("fileName", originalFileName);
                    result.put("exists", false);
                    result.put("message", "La photo demandée n'existe pas");
                    result.put("variantsTestedCount", fileVariants.size()); // Info de debug
                    result.put("allVariantsTested", fileVariants); // Liste complète pour debug
                    return result;
                })
                .orElse(null);
    }

    private List<String> generatePhotoFileVariants(String prefix, String nomVolontaire, String dateFormatted) {
        List<String> variants = new ArrayList<>();
        String nom = nomVolontaire.toLowerCase();

        // Variantes de formatage du nom
        String[] nameFormats = {
                nom, // "da silva costa"
                nom.replace(" ", "_"), // "da_silva_costa"
                nom.replace(" ", ""), // "dasilvacosta"
                nom.replace(" ", "-") // "da-silva-costa"
        };

        // Extensions possibles
        String[] extensions = { ".JPG", ".jpg", ".JPEG", ".jpeg", ".PNG", ".png", ".pdf", ".PDF" };

        // Générer toutes les combinaisons
        for (String nameFormat : nameFormats) {
            for (String ext : extensions) {
                // Avec date
                variants.add(prefix + nameFormat + dateFormatted + ext);
                // Sans date (au cas où)
                variants.add(prefix + nameFormat + ext);
            }
        }

        return variants;
    }

    private boolean checkPhotoExists(String fileName) {
        if (!photoCheckEnabled) {
            logger.debug("Vérification photo désactivée, retour true pour: {}", fileName);
            return true;
        }

        // IMPORTANT : Encoder correctement l'URL pour gérer les espaces et caractères
        // spéciaux
        String encodedFileName;
        try {
            // Encoder seulement le nom du fichier, pas l'URL complète
            encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replace("+", "%20"); // URLEncoder remplace espaces par +, on veut %20
        } catch (Exception e) {
            logger.warn("Erreur lors de l'encodage du nom de fichier '{}': {}", fileName, e.getMessage());
            return false;
        }

        String photoUrl = photoServerUrl + encodedFileName;
        logger.debug("URL encodée: {}", photoUrl);

        try {
            URL url = URI.create(photoUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(photoConnectionTimeout);
            connection.setReadTimeout(photoReadTimeout);

            int responseCode = connection.getResponseCode();
            boolean exists = (responseCode == HttpURLConnection.HTTP_OK);
            logger.debug("Vérification photo '{}': {}", fileName, exists ? "trouvée" : "non trouvée");

            return exists;

        } catch (Exception e) {
            logger.warn("Erreur lors de la vérification de '{}': {} - {}",
                    photoUrl, e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getAllVolontairePhotos(Integer id) {
        return getVolontaireById(id)
                .map(volontaire -> {
                    String nomVolontaire = volontaire.getNomVol();
                    LocalDate dateNaissance = volontaire.getDateNaissance();

                    if (nomVolontaire == null || dateNaissance == null) {
                        return Collections.<Map<String, Object>>emptyList();
                    }

                    // Formatage de la date similaire au code PHP (format "dmy")
                    String dateFormatted = dateNaissance.format(DateTimeFormatter.ofPattern("ddMMyy"));
                    String nom = nomVolontaire.toLowerCase();

                    // Liste des préfixes pour tous les types de photos
                    String[] prefixes = { "f_", "fpp_", "fpc_", "d_", "dpp_", "dpc_", "g_", "gpp_", "gpc_" };

                    // Création d'une liste pour stocker les informations des photos
                    List<Map<String, Object>> photos = new ArrayList<>();

                    // Pour chaque préfixe, vérifier si la photo existe
                    for (String prefix : prefixes) {
                        String fileName = prefix + nom + dateFormatted + ".JPG";
                        boolean exists = checkPhotoExists(fileName);

                        // Déterminer le type de photo en français pour l'affichage
                        String photoType;
                        String photoCode;
                        switch (prefix) {
                            case "f_":
                                photoType = "Face";
                                photoCode = "face";
                                break;
                            case "fpp_":
                                photoType = "Face polarisée parallèle";
                                photoCode = "face_pp";
                                break;
                            case "fpc_":
                                photoType = "Face polarisée croisée";
                                photoCode = "face_pc";
                                break;
                            case "d_":
                                photoType = "Droite";
                                photoCode = "droite";
                                break;
                            case "dpp_":
                                photoType = "Droite polarisée parallèle";
                                photoCode = "droite_pp";
                                break;
                            case "dpc_":
                                photoType = "Droite polarisée croisée";
                                photoCode = "droite_pc";
                                break;
                            case "g_":
                                photoType = "Gauche";
                                photoCode = "gauche";
                                break;
                            case "gpp_":
                                photoType = "Gauche polarisée parallèle";
                                photoCode = "gauche_pp";
                                break;
                            case "gpc_":
                                photoType = "Gauche polarisée croisée";
                                photoCode = "gauche_pc";
                                break;
                            default:
                                photoType = "Inconnue";
                                photoCode = "inconnu";
                        }

                        // Si la photo existe, ajouter ses informations à la liste
                        if (exists) {
                            Map<String, Object> photoInfo = new HashMap<>();
                            photoInfo.put("type", photoType);
                            photoInfo.put("code", photoCode);
                            photoInfo.put("photoUrl", photoServerUrl + fileName);
                            photoInfo.put("fileName", fileName);
                            photos.add(photoInfo);
                        }
                    }

                    return photos;
                })
                .orElse(Collections.<Map<String, Object>>emptyList());
    }

    @Override
    public Page<VolontaireDTO> getVolontairesPaginated(int page, int size, boolean includeArchived, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Volontaire> volontairesPage;

        if (search != null && !search.trim().isEmpty()) {
            volontairesPage = includeArchived
                    ? volontaireRepository.searchAll(search, pageable)
                    : volontaireRepository.searchActive(search, pageable);
        } else {
            volontairesPage = includeArchived
                    ? volontaireRepository.findAll(pageable)
                    : volontaireRepository.findByArchiveFalse(pageable);
        }

        return volontairesPage.map(this::convertToDto);
    }

    @Override
    public Page<VolontaireDTO> searchByMultipleFields(String nom, String prenom, String email, String tel,
            String idVol, boolean includeArchived, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Nettoyer les paramètres : transformer les chaînes vides en null
        String cleanNom = (nom != null && !nom.trim().isEmpty()) ? nom.trim() : null;
        String cleanPrenom = (prenom != null && !prenom.trim().isEmpty()) ? prenom.trim() : null;
        String cleanEmail = (email != null && !email.trim().isEmpty()) ? email.trim() : null;
        String cleanTel = (tel != null && !tel.trim().isEmpty()) ? tel.trim() : null;
        String cleanIdVol = (idVol != null && !idVol.trim().isEmpty()) ? idVol.trim() : null;

        // Si aucun champ renseigné, retourner la liste paginée standard
        if (cleanNom == null && cleanPrenom == null && cleanEmail == null && cleanTel == null && cleanIdVol == null) {
            Page<Volontaire> volontairesPage = includeArchived
                    ? volontaireRepository.findAll(pageable)
                    : volontaireRepository.findByArchiveFalse(pageable);
            return volontairesPage.map(this::convertToDto);
        }

        Page<Volontaire> volontairesPage = volontaireRepository.searchByMultipleFields(
                includeArchived, cleanNom, cleanPrenom, cleanEmail, cleanTel, cleanIdVol, pageable);
        return volontairesPage.map(this::convertToDto);
    }

    private VolontaireDTO convertToDto(Volontaire volontaire) {
        VolontaireDTO dto = new VolontaireDTO(
                volontaire.getIdVol(),
                volontaire.getTitreVol(),
                volontaire.getNomVol(),
                volontaire.getPrenomVol(),
                volontaire.getEmailVol(),
                volontaire.getSexe(),
                volontaire.getDateNaissance(),
                volontaire.getArchive(),
                volontaire.getTypePeauVisage());
        dto.setPhototype(volontaire.getPhototype());
        dto.setEthnie(volontaire.getEthnie());
        return dto;
    }

    @Override
    public List<Volontaire> findAllByIdIn(List<Integer> ids) {
        return volontaireRepository.findAllByIdVolIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VolontaireDTO> findByEmail(String email) {
        List<Volontaire> volontaires = volontaireRepository.findByEmailVol(email);
        if (volontaires.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(volontaireMapper.toDTO(volontaires.get(0)));
    }
}