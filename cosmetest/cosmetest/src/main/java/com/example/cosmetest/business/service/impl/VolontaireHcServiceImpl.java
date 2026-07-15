package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.mapper.VolontaireHcMapper;
import com.example.cosmetest.business.service.VolontaireHcService;
import com.example.cosmetest.domain.model.VolontaireHc;
import com.example.cosmetest.data.repository.VolontaireHcRepository;
import com.example.cosmetest.exception.AmbiguousVolontaireHcException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
    @Transactional(isolation = org.springframework.transaction.annotation.Isolation.SERIALIZABLE)
    public VolontaireHcDTO saveVolontaireHc(VolontaireHcDTO volontaireHcDTO) {
        validateVolontaireHc(volontaireHcDTO);

        // Normaliser les valeurs NULL avant la persistence

        // Vérifier si une entrée existe déjà pour ce volontaire
        Optional<VolontaireHc> existingEntity = findUniqueByIdVol(volontaireHcDTO.getIdVol());

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

        return savedDto;
    }

    @Override
    public boolean deleteVolontaireHc(Integer idVol) {
        if (idVol == null) {
            return false;
        }

        Optional<VolontaireHc> volontaireHc = findUniqueByIdVol(idVol);
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

        return findUniqueByIdVol(idVol)
                .map(volontaireHc -> {
                    VolontaireHcFieldRegistry.require(produit).set(volontaireHc, valeur);
                    VolontaireHc savedVolontaireHc = volontaireHcRepository.save(volontaireHc);
                    return volontaireHcMapper.toDTO(savedVolontaireHc);
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

        return findUniqueByIdVol(idVol)
                .map(volontaireHc -> {
                    produits.forEach((produit, valeur) ->
                            VolontaireHcFieldRegistry.require(produit).set(volontaireHc, valeur));

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

        // Pour chaque champ explicitement autorisé, faire un COUNT en base.
        for (String produit : VolontaireHcFieldRegistry.names()) {
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

                                String valeurActuelle = VolontaireHcFieldRegistry.require(produit).get(target);
                                return Objects.equals(valeurRecherchee, valeurActuelle);
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

        for (String fieldName : VolontaireHcFieldRegistry.names()) {
            String value = VolontaireHcFieldRegistry.require(fieldName).get(volontaireHcDTO);
            if (!VALEURS_AUTORISEES.contains(value)) {
                throw new IllegalArgumentException(
                        "Valeur non autorisée pour " + fieldName + ": " + value);
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

        VolontaireHcFieldRegistry.require(produit);
    }

    /**
     * Valide le paramètre lieu d'achat
     *
     * @param lieuAchat le lieu d'achat à valider
     * @throws IllegalArgumentException si le lieu d'achat est invalide
     */
    private String getColumnName(String fieldName) {
        return VolontaireHcFieldRegistry.require(fieldName).columnName();
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

        return findUniqueByIdVol(idVol)
                .map(volontaireHcMapper::toDTO);
    }

    private Optional<VolontaireHc> findUniqueByIdVol(Integer idVol) {
        List<VolontaireHc> matches = volontaireHcRepository.findByIdVolIn(List.of(idVol));
        if (matches.size() > 1) {
            throw new AmbiguousVolontaireHcException(idVol, matches.size());
        }
        return matches.stream().findFirst();
    }


    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> getAllVolontaireHcs() {
        List<VolontaireHc> volontaireHcs = volontaireHcRepository.findAll();
        return volontaireHcMapper.toDTOList(volontaireHcs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolontaireHcDTO> findByProduit(String produit, String valeur) {
        validateProduitParameter(produit);
        validateValeurParameter(valeur);

        List<VolontaireHc> allVolontaireHcs = volontaireHcRepository.findAll();

        VolontaireHcFieldRegistry.FieldAccess access = VolontaireHcFieldRegistry.require(produit);
        List<VolontaireHc> filteredVolontaireHcs = allVolontaireHcs.stream()
                .filter(volontaireHc -> Objects.equals(valeur, access.get(volontaireHc)))
                .collect(Collectors.toList());

        List<VolontaireHcDTO> dtoList = volontaireHcMapper.toDTOList(filteredVolontaireHcs);

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

        return dtoList;
    }
}
