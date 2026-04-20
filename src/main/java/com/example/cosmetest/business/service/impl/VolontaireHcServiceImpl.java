package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.mapper.VolontaireHcMapper;
import com.example.cosmetest.business.service.VolontaireHcService;
import com.example.cosmetest.domain.model.VolontaireHc;
import com.example.cosmetest.data.repository.VolontaireHcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implémentation des services métier pour l'entité VolontaireHc
 */
@Service
@Transactional
public class VolontaireHcServiceImpl implements VolontaireHcService {

    private final VolontaireHcRepository volontaireHcRepository;
    private final VolontaireHcMapper volontaireHcMapper;

    @PersistenceContext
    private EntityManager entityManager;

    // Liste des valeurs autorisées pour les attributs de type produit/lieu d'achat
    private static final List<String> VALEURS_AUTORISEES = Arrays.asList("oui", "non", "occasionnellement",
            "regulierement", "jamais", null);

    public VolontaireHcServiceImpl(VolontaireHcRepository volontaireHcRepository,
            VolontaireHcMapper volontaireHcMapper) {
        this.volontaireHcRepository = volontaireHcRepository;
        this.volontaireHcMapper = volontaireHcMapper;
    }

    @Override
    public VolontaireHcDTO saveVolontaireHc(VolontaireHcDTO volontaireHcDTO) {
        validateVolontaireHc(volontaireHcDTO);

        // Normaliser les valeurs NULL avant la persistence
        normalizeNullValues(volontaireHcDTO);

        // Vérifier si une entrée existe déjà pour ce volontaire
        Optional<VolontaireHc> existingEntity = volontaireHcRepository.findByIdVol(volontaireHcDTO.getIdVol());

        VolontaireHc volontaireHc;
        if (existingEntity.isPresent()) {
            // Mise à jour
            volontaireHc = volontaireHcMapper.updateEntityFromDTO(existingEntity.get(), volontaireHcDTO);
        } else {
            // Création
            volontaireHc = volontaireHcMapper.toEntity(volontaireHcDTO);
        }

        VolontaireHc savedVolontaireHc = volontaireHcRepository.save(volontaireHc);
        VolontaireHcDTO savedDto = volontaireHcMapper.toDTO(savedVolontaireHc);

        // Normaliser à nouveau après la conversion
        normalizeNullValues(savedDto);

        return savedDto;
    }

    @Override
    public boolean deleteVolontaireHc(Integer idVol) {
        if (idVol == null) {
            return false;
        }

        Optional<VolontaireHc> volontaireHc = volontaireHcRepository.findByIdVol(idVol);
        if (volontaireHc.isPresent()) {
            volontaireHcRepository.delete(volontaireHc.get());
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdVol(Integer idVol) {
        if (idVol == null) {
            return false;
        }

        return volontaireHcRepository.existsByIdVol(idVol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> findByLieuAchat(String lieuAchat, String valeur) {
        validateLieuAchatParameter(lieuAchat);
        validateValeurParameter(valeur);

        List<VolontaireHc> volontaireHcs;

        // Sélectionner la méthode de repository appropriée en fonction du lieu d'achat
        switch (lieuAchat) {
            case "achatGrandesSurfaces":
                volontaireHcs = volontaireHcRepository.findByAchatGrandesSurfaces(valeur);
                break;
            case "achatInstitutParfumerie":
                volontaireHcs = volontaireHcRepository.findByAchatInstitutParfumerie(valeur);
                break;
            case "achatInternet":
                volontaireHcs = volontaireHcRepository.findByAchatInternet(valeur);
                break;
            case "achatPharmacieParapharmacie":
                volontaireHcs = volontaireHcRepository.findByAchatPharmacieParapharmacie(valeur);
                break;
            default:
                throw new IllegalArgumentException("Lieu d'achat non reconnu: " + lieuAchat);
        }

        return volontaireHcMapper.toDTOList(volontaireHcs);
    }

    @Override
    public Optional<VolontaireHcDTO> updateProduit(Integer idVol, String produit, String valeur) {
        if (idVol == null) {
            return Optional.empty();
        }

        validateProduitParameter(produit);
        validateValeurParameter(valeur);

        return volontaireHcRepository.findByIdVol(idVol)
                .map(volontaireHc -> {
                    // Utiliser la réflexion pour mettre à jour la propriété spécifique
                    try {
                        Field field = VolontaireHc.class.getDeclaredField(produit);
                        field.setAccessible(true);
                        field.set(volontaireHc, valeur);

                        VolontaireHc savedVolontaireHc = volontaireHcRepository.save(volontaireHc);
                        return volontaireHcMapper.toDTO(savedVolontaireHc);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new IllegalArgumentException("Produit invalide: " + produit, e);
                    }
                });
    }

    @Override
    public Optional<VolontaireHcDTO> updateProduits(Integer idVol, Map<String, String> produits) {
        if (idVol == null || produits == null || produits.isEmpty()) {
            return Optional.empty();
        }

        // Valider tous les produits et valeurs
        produits.forEach((produit, valeur) -> {
            validateProduitParameter(produit);
            validateValeurParameter(valeur);
        });

        return volontaireHcRepository.findByIdVol(idVol)
                .map(volontaireHc -> {
                    // Mettre à jour chaque produit
                    produits.forEach((produit, valeur) -> {
                        try {
                            Field field = VolontaireHc.class.getDeclaredField(produit);
                            field.setAccessible(true);
                            field.set(volontaireHc, valeur);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new IllegalArgumentException("Produit invalide: " + produit, e);
                        }
                    });

                    VolontaireHc savedVolontaireHc = volontaireHcRepository.save(volontaireHc);
                    return volontaireHcMapper.toDTO(savedVolontaireHc);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getStatistiquesUtilisationProduit(String produit) {
        validateProduitParameter(produit);

        String columnName = getColumnName(produit);
        @SuppressWarnings("unchecked")
        List<Object[]> results = entityManager.createNativeQuery(
                "SELECT COALESCE(" + columnName + ", 'non spécifié') as val, COUNT(*) as cnt " +
                "FROM volontaire_hc GROUP BY COALESCE(" + columnName + ", 'non spécifié')")
                .getResultList();

        Map<String, Long> stats = new HashMap<>();
        for (Object[] row : results) {
            stats.put((String) row[0], ((Number) row[1]).longValue());
        }
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getProduitsLesPlusUtilises(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("La limite doit être un nombre positif");
        }

        Map<String, Long> produitsUtilisation = new HashMap<>();

        // Pour chaque champ produit, faire un COUNT en base au lieu de charger toutes les lignes
        Field[] fields = VolontaireHc.class.getDeclaredFields();
        for (Field field : fields) {
            String produit = field.getName();
            if (produit.equals("idVol") || !field.getType().equals(String.class)) {
                continue;
            }

            String columnName = getColumnName(produit);
            Number count = (Number) entityManager.createNativeQuery(
                    "SELECT COUNT(*) FROM volontaire_hc WHERE " + columnName + " IN ('oui', 'regulierement')")
                    .getSingleResult();

            if (count.longValue() > 0) {
                produitsUtilisation.put(produit, count.longValue());
            }
        }

        // Trier par nombre d'utilisateurs décroissant et limiter
        return produitsUtilisation.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getLieuxAchatPreferences() {
        Map<String, Long> lieuxAchatPreferences = new HashMap<>();

        // 4 COUNT en base au lieu de charger toute la table
        List<String> lieuxAchat = Arrays.asList(
                "achatGrandesSurfaces",
                "achatInstitutParfumerie",
                "achatInternet",
                "achatPharmacieParapharmacie");

        for (String lieu : lieuxAchat) {
            String columnName = getColumnName(lieu);
            Number count = (Number) entityManager.createNativeQuery(
                    "SELECT COUNT(*) FROM volontaire_hc WHERE " + columnName + " IN ('oui', 'regulierement')")
                    .getSingleResult();

            if (count.longValue() > 0) {
                lieuxAchatPreferences.put(lieu, count.longValue());
            }
        }

        // Trier par nombre d'utilisateurs décroissant
        return lieuxAchatPreferences.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> findByMultipleProduits(Map<String, String> produits) {
        if (produits == null || produits.isEmpty()) {
            return Collections.emptyList();
        }

        // Valider tous les produits et valeurs
        produits.forEach((produit, valeur) -> {
            validateProduitParameter(produit);
            validateValeurParameter(valeur);
        });

        List<VolontaireHc> allVolontaireHcs = volontaireHcRepository.findAll();

        // Filtrer les volontaires qui correspondent à tous les critères
        List<VolontaireHc> filteredVolontaireHcs = allVolontaireHcs.stream()
                .filter(volontaireHc -> {
                    VolontaireHc target = volontaireHc;
                    return produits.entrySet().stream()
                            .allMatch(entry -> {
                                String produit = entry.getKey();
                                String valeurRecherchee = entry.getValue();

                                try {
                                    Field field = VolontaireHc.class.getDeclaredField(produit);
                                    field.setAccessible(true);
                                    String valeurActuelle = (String) field.get(target);

                                    return valeurRecherchee.equals(valeurActuelle);
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    return false;
                                }
                            });
                })
                .collect(Collectors.toList());

        return volontaireHcMapper.toDTOList(filteredVolontaireHcs);
    }

    /**
     * Valide les données d'un VolontaireHcDTO
     *
     * @param volontaireHcDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateVolontaireHc(VolontaireHcDTO volontaireHcDTO) {
        if (volontaireHcDTO == null) {
            throw new IllegalArgumentException("Les habitudes de consommation ne peuvent pas être null");
        }

        if (volontaireHcDTO.getIdVol() == null || volontaireHcDTO.getIdVol() <= 0) {
            throw new IllegalArgumentException("L'ID du volontaire doit être un nombre positif");
        }

        // Valider toutes les valeurs non null
        Field[] fields = VolontaireHcDTO.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();

                // Ignorer l'ID du volontaire et les champs null
                if (fieldName.equals("idVol")) {
                    continue;
                }

                Object value = field.get(volontaireHcDTO);
                if (value != null && value instanceof String) {
                    String stringValue = (String) value;
                    if (!VALEURS_AUTORISEES.contains(stringValue)) {
                        throw new IllegalArgumentException(
                                "Valeur non autorisée pour " + fieldName + ": " + stringValue);
                    }
                }
            } catch (IllegalAccessException e) {
                // Ignorer cette erreur
            }
        }
    }

    /**
     * Valide le paramètre produit
     *
     * @param produit le nom du produit à valider
     * @throws IllegalArgumentException si le produit est invalide
     */
    private void validateProduitParameter(String produit) {
        if (produit == null || produit.trim().isEmpty()) {
            throw new IllegalArgumentException("Le produit ne peut pas être vide");
        }

        try {
            VolontaireHc.class.getDeclaredField(produit);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Produit non reconnu: " + produit);
        }
    }

    /**
     * Valide le paramètre lieu d'achat
     *
     * @param lieuAchat le lieu d'achat à valider
     * @throws IllegalArgumentException si le lieu d'achat est invalide
     */
    /**
     * Résout le nom de colonne SQL à partir du nom de champ Java.
     * Utilise l'annotation @Column si présente, sinon convertit camelCase en snake_case.
     * Le champ est validé par validateProduitParameter() en amont (pas d'injection possible).
     */
    private String getColumnName(String fieldName) {
        try {
            Field field = VolontaireHc.class.getDeclaredField(fieldName);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
                return columnAnnotation.name();
            }
        } catch (NoSuchFieldException e) {
            // Ne devrait pas arriver car validateProduitParameter vérifie avant
        }
        // Fallback: camelCase → snake_case
        return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private void validateLieuAchatParameter(String lieuAchat) {
        if (lieuAchat == null || lieuAchat.trim().isEmpty()) {
            throw new IllegalArgumentException("Le lieu d'achat ne peut pas être vide");
        }

        List<String> lieuxAchatValides = Arrays.asList(
                "achatGrandesSurfaces",
                "achatInstitutParfumerie",
                "achatInternet",
                "achatPharmacieParapharmacie");

        if (!lieuxAchatValides.contains(lieuAchat)) {
            throw new IllegalArgumentException("Lieu d'achat non reconnu: " + lieuAchat);
        }
    }

    /**
     * Valide le paramètre valeur
     *
     * @param valeur la valeur à valider
     * @throws IllegalArgumentException si la valeur est invalide
     */
    private void validateValeurParameter(String valeur) {
        if (valeur == null) {
            return;
        }

        if (!VALEURS_AUTORISEES.contains(valeur)) {
            throw new IllegalArgumentException("Valeur non autorisée: " + valeur);
        }
    }

    /**
     * Récupère les habitudes de consommation pour une liste d'identifiants de
     * volontaires
     *
     * @param idList liste des identifiants des volontaires
     * @return liste des entités VolontaireHc pour les volontaires spécifiés
     */
    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHc> findByIdVolIn(List<Integer> idList) {
        if (idList == null || idList.isEmpty()) {
            return Collections.emptyList();
        }
        return volontaireHcRepository.findByIdVolIn(idList);
    }

    /**
     * Récupère les habitudes de consommation pour une liste d'identifiants de
     * volontaires
     *
     * @param idList liste des identifiants des volontaires
     * @return liste des DTOs VolontaireHcDTO pour les volontaires spécifiés
     */

    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> getVolontaireHcByIdVol(List<Integer> idList) {
        if (idList == null || idList.isEmpty()) {
            return Collections.emptyList();
        }
        return volontaireHcRepository.findByIdVolIn(idList)
                .stream()
                .map(volontaireHcMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VolontaireHcDTO> getVolontaireHcByIdVol(Integer idVol) {
        if (idVol == null) {
            return Optional.empty();
        }

        return volontaireHcRepository.findByIdVol(idVol)
                .map(volontaireHc -> {
                    VolontaireHcDTO dto = volontaireHcMapper.toDTO(volontaireHc);

                    // Normalisation des valeurs NULL en "non"
                    normalizeNullValues(dto);

                    return dto;
                });
    }

    /**
     * Normalise les valeurs NULL d'un DTO en "non"
     * 
     * @param dto le DTO à normaliser
     */
    private void normalizeNullValues(VolontaireHcDTO dto) {
        if (dto == null) {
            return;
        }

        // Utiliser la réflexion pour parcourir tous les champs et remplacer les valeurs
        // null par "non"
        Field[] fields = VolontaireHcDTO.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String fieldName = field.getName();

                // Ignorer l'ID du volontaire
                if (fieldName.equals("idVol")) {
                    continue;
                }

                Object value = field.get(dto);
                if (value == null) {
                    field.set(dto, "non");
                }
            } catch (IllegalAccessException e) {
                // Ignorer cette erreur
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> getAllVolontaireHcs() {
        List<VolontaireHc> volontaireHcs = volontaireHcRepository.findAll();
        List<VolontaireHcDTO> dtoList = volontaireHcMapper.toDTOList(volontaireHcs);

        // Normaliser tous les DTOs
        dtoList.forEach(this::normalizeNullValues);

        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> findByProduit(String produit, String valeur) {
        validateProduitParameter(produit);
        validateValeurParameter(valeur);

        List<VolontaireHc> allVolontaireHcs = volontaireHcRepository.findAll();

        // Filtrer en utilisant la réflexion pour accéder à la propriété spécifiée par
        // 'produit'
        List<VolontaireHc> filteredVolontaireHcs = allVolontaireHcs.stream()
                .filter(volontaireHc -> {
                    try {
                        Field field = VolontaireHc.class.getDeclaredField(produit);
                        field.setAccessible(true);
                        String fieldValue = (String) field.get(volontaireHc);
                        // Considérer null comme "non" pour la comparaison
                        if (fieldValue == null) {
                            return "non".equals(valeur);
                        }
                        return valeur.equals(fieldValue);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        List<VolontaireHcDTO> dtoList = volontaireHcMapper.toDTOList(filteredVolontaireHcs);
        dtoList.forEach(this::normalizeNullValues);

        return dtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> getVolontaireHcsByIds(List<Integer> idList) {
        List<VolontaireHcDTO> dtoList = volontaireHcRepository.findByIdVolIn(idList)
                .stream()
                .map(volontaireHcMapper::toDTO)
                .collect(Collectors.toList());

        // Normaliser tous les DTOs
        dtoList.forEach(this::normalizeNullValues);

        return dtoList;
    }
}
