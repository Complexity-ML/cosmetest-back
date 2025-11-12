package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.mapper.VolontaireHcMapper;
import com.example.cosmetest.business.service.VolontaireHcService;
import com.example.cosmetest.domain.model.VolontaireHc;
import com.example.cosmetest.data.repository.VolontaireHcRepository;
import com.example.cosmetest.presentation.request.ProduitUpdateRequest;
import com.example.cosmetest.presentation.request.ProduitsUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contrôleur REST pour la gestion des habitudes de consommation des volontaires
 */
@RestController
@RequestMapping("/api/volontaires-hc")
public class VolontaireHcController {

    private final VolontaireHcService volontaireHcService;
    private final VolontaireHcRepository volontaireHcRepository; 
    private final VolontaireHcMapper volontaireHcMapper;
    private static final Logger logger = LoggerFactory.getLogger(VolontaireHcController.class);

    public VolontaireHcController(
            VolontaireHcService volontaireHcService,
            VolontaireHcRepository volontaireHcRepository,
            VolontaireHcMapper volontaireHcMapper) {
        this.volontaireHcService = volontaireHcService;
        this.volontaireHcRepository = volontaireHcRepository;
        this.volontaireHcMapper = volontaireHcMapper;
    }

    /**
     * Récupère toutes les habitudes de consommation
     *
     * @return liste des habitudes de consommation
     */
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<VolontaireHcDTO>> getAllVolontaireHcs() {
        List<VolontaireHcDTO> volontaireHcs = volontaireHcService.getAllVolontaireHcs();
        return ResponseEntity.ok(volontaireHcs);
    }

    /**
     * Récupère les habitudes de consommation d'un volontaire par son ID
     *
     * @param idVol l'identifiant du volontaire
     * @return les habitudes de consommation du volontaire
     */
    @GetMapping("/volontaire/{idVol}")
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<VolontaireHcDTO> getVolontaireHcByIdVol(@PathVariable Integer idVol) {
        try {
            logger.info("Récupération des habitudes cosmétiques pour le volontaire ID: {}", idVol);

            // Approche améliorée pour gérer les cas d'erreur et les doublons
            try {
                // Essayer d'abord avec la requête JPA standard
                Optional<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcByIdVol(idVol);
                
                if (result.isPresent()) {
                    logger.info("Entrée trouvée via JPA pour ID: {}", idVol);
                    return ResponseEntity.ok(result.get());
                } else {
                    // Si aucun résultat, on va essayer de récupérer une liste et prendre le premier
                    logger.warn("Aucune entrée trouvée via JPA, tentative avec la méthode findByIdVolIn");
                    List<VolontaireHc> entities = volontaireHcRepository.findByIdVolIn(List.of(idVol));
                    
                    if (!entities.isEmpty()) {
                        logger.info("Entrée(s) trouvée(s) via findByIdVolIn pour ID: {}, {} résultats", 
                                  idVol, entities.size());
                        
                        // S'il y a des doublons, prenons le premier résultat
                        VolontaireHcDTO dto = volontaireHcMapper.toDTO(entities.get(0));
                        return ResponseEntity.ok(dto);
                    }
                    
                    // Si toujours rien, erreur 404
                    logger.error("Entrée inexistante pour ID: {}", idVol);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Habitudes de consommation non trouvées pour le volontaire avec l'ID: " + idVol);
                }
            } catch (IncorrectResultSizeDataAccessException e) {
                // Cette exception est lancée s'il y a plusieurs entrées pour un même ID
                logger.warn("Plusieurs entrées trouvées pour l'ID: {}, tentative de récupération de la liste", idVol);
                
                // En cas de multiples résultats, récupérer tous les résultats et prendre le premier
                List<VolontaireHc> entities = volontaireHcRepository.findByIdVolIn(List.of(idVol));
                
                if (!entities.isEmpty()) {
                    logger.info("Récupéré {} entrées pour ID: {}, utilisation de la première", 
                               entities.size(), idVol);
                    
                    // Prendre le premier résultat
                    VolontaireHcDTO dto = volontaireHcMapper.toDTO(entities.get(0));
                    return ResponseEntity.ok(dto);
                } else {
                    logger.error("Aucune entrée trouvée via liste pour ID: {}", idVol);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Habitudes de consommation non trouvées pour le volontaire avec l'ID: " + idVol);
                }
            }
        } catch (Exception e) {
            // Log l'erreur
            logger.error("Erreur lors de la récupération des habitudes cosmétiques pour l'ID: " + idVol, e);

            // Si c'est une erreur connue, la propager
            if (e instanceof ResponseStatusException) {
                throw e;
            }

            // Sinon, créer une réponse d'erreur générique
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Une erreur est survenue lors de la récupération des habitudes cosmétiques");
        }
    }

    /**
     * Crée ou met à jour les habitudes de consommation d'un volontaire
     *
     * @param volontaireHcDTO les données des habitudes de consommation
     * @return les habitudes de consommation créées ou mises à jour
     */
    @PostMapping
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<VolontaireHcDTO> saveVolontaireHc(@Valid @RequestBody VolontaireHcDTO volontaireHcDTO) {
        try {
            logger.info("Sauvegarde des habitudes cosmétiques pour le volontaire ID: {}", volontaireHcDTO.getIdVol());
            
            // Normalisation des données avant sauvegarde
            normalizeVolontaireHcDTO(volontaireHcDTO);
            
            // Vérifier s'il existe déjà plusieurs entrées pour ce volontaire
            try {
                List<VolontaireHc> existingEntries = volontaireHcRepository.findByIdVolIn(List.of(volontaireHcDTO.getIdVol()));
                
                // Si plusieurs entrées existent, supprimer les doublons avant de créer une nouvelle
                if (existingEntries.size() > 1) {
                    logger.warn("Plusieurs entrées ({}) trouvées pour le volontaire ID: {}. Nettoyage avant sauvegarde.", 
                              existingEntries.size(), volontaireHcDTO.getIdVol());
                    
                    // Suppression de toutes les entrées existantes pour éviter les doublons
                    volontaireHcService.deleteVolontaireHc(volontaireHcDTO.getIdVol());
                }
            } catch (Exception e) {
                logger.warn("Erreur lors de la vérification des entrées existantes: {}", e.getMessage());
                // Continuer même en cas d'erreur
            }
            
            VolontaireHcDTO savedVolontaireHc = volontaireHcService.saveVolontaireHc(volontaireHcDTO);
            return ResponseEntity.ok(savedVolontaireHc);
        } catch (IllegalArgumentException e) {
            logger.error("Erreur lors de la sauvegarde des habitudes cosmétiques", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Normalise les valeurs de VolontaireHcDTO pour s'assurer de la cohérence des données
     * 
     * @param dto Le DTO à normaliser
     */
    private void normalizeVolontaireHcDTO(VolontaireHcDTO dto) {
        // Traiter tous les champs sauf idVol et commentaires
        for (java.lang.reflect.Field field : VolontaireHcDTO.class.getDeclaredFields()) {
            String fieldName = field.getName();
            
            // Ne pas traiter idVol et commentaires
            if (!"idVol".equals(fieldName) && !"commentaires".equals(fieldName)) {
                field.setAccessible(true);
                
                try {
                    Object value = field.get(dto);
                    
                    // Normaliser la valeur
                    if (value == null) {
                        field.set(dto, "non");
                    } else if (value instanceof String) {
                        String strValue = ((String) value).toLowerCase();
                        if (strValue.equals("oui") || strValue.equals("yes") || 
                            strValue.equals("true") || strValue.equals("1")) {
                            field.set(dto, "oui");
                        } else {
                            field.set(dto, "non");
                        }
                    }
                } catch (IllegalAccessException e) {
                    logger.warn("Impossible d'accéder au champ {}: {}", fieldName, e.getMessage());
                }
            }
        }
    }

    /**
     * Supprime les habitudes de consommation d'un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return statut de la suppression
     */
    @DeleteMapping("/volontaire/{idVol}")
    @Transactional
    public ResponseEntity<Void> deleteVolontaireHc(@PathVariable Integer idVol) {
        logger.info("Suppression des habitudes cosmétiques pour le volontaire ID: {}", idVol);
        if (volontaireHcService.deleteVolontaireHc(idVol)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Habitudes de consommation non trouvées pour le volontaire avec l'ID: " + idVol);
        }
    }

    /**
     * Recherche les volontaires qui utilisent un produit spécifique
     *
     * @param produit le nom du produit
     * @param valeur  la valeur du produit
     * @return la liste des habitudes de consommation des volontaires concernés
     */
    @GetMapping("/by-produit")
    @Transactional(readOnly = true)
    public ResponseEntity<List<VolontaireHcDTO>> findByProduit(
            @RequestParam String produit,
            @RequestParam String valeur) {
        try {
            List<VolontaireHcDTO> volontaireHcs = volontaireHcService.findByProduit(produit, valeur);
            return ResponseEntity.ok(volontaireHcs);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Recherche les volontaires qui achètent dans un lieu spécifique
     *
     * @param lieuAchat le lieu d'achat
     * @param valeur    la valeur
     * @return la liste des habitudes de consommation des volontaires concernés
     */
    @GetMapping("/by-lieu-achat")
    @Transactional(readOnly = true)
    public ResponseEntity<List<VolontaireHcDTO>> findByLieuAchat(
            @RequestParam String lieuAchat,
            @RequestParam String valeur) {
        try {
            List<VolontaireHcDTO> volontaireHcs = volontaireHcService.findByLieuAchat(lieuAchat, valeur);
            return ResponseEntity.ok(volontaireHcs);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour un produit spécifique pour un volontaire
     *
     * @param idVol   l'identifiant du volontaire
     * @param request la requête contenant le produit et la valeur
     * @return les habitudes de consommation mises à jour
     */
    @PatchMapping("/volontaire/{idVol}/produit")
    @Transactional
    public ResponseEntity<VolontaireHcDTO> updateProduit(
            @PathVariable Integer idVol,
            @Valid @RequestBody ProduitUpdateRequest request) {
        try {
            // Normaliser la valeur
            String normalizedValue = "non";
            if (request.getValeur() != null) {
                String valeur = request.getValeur().toLowerCase();
                if (valeur.equals("oui") || valeur.equals("yes") || 
                    valeur.equals("true") || valeur.equals("1")) {
                    normalizedValue = "oui";
                }
            }
            
            return volontaireHcService.updateProduit(idVol, request.getProduit(), normalizedValue)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Habitudes de consommation non trouvées pour le volontaire avec l'ID: " + idVol));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour plusieurs produits pour un volontaire
     *
     * @param idVol   l'identifiant du volontaire
     * @param request la requête contenant les produits et leurs valeurs
     * @return les habitudes de consommation mises à jour
     */
    @PatchMapping("/volontaire/{idVol}/produits")
    @Transactional
    public ResponseEntity<VolontaireHcDTO> updateProduits(
            @PathVariable Integer idVol,
            @Valid @RequestBody ProduitsUpdateRequest request) {
        try {
            // Normaliser les valeurs
            Map<String, String> normalizedProduits = request.getProduits().entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> {
                        String value = entry.getValue();
                        if (value == null) return "non";
                        
                        String lowerValue = value.toLowerCase();
                        if (lowerValue.equals("oui") || lowerValue.equals("yes") || 
                            lowerValue.equals("true") || lowerValue.equals("1")) {
                            return "oui";
                        }
                        return "non";
                    }
                ));
            
            return volontaireHcService.updateProduits(idVol, normalizedProduits)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Habitudes de consommation non trouvées pour le volontaire avec l'ID: " + idVol));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Obtient les statistiques d'utilisation d'un produit
     *
     * @param produit le nom du produit
     * @return une map avec les valeurs possibles et le nombre de volontaires pour
     *         chaque valeur
     */
    @GetMapping("/statistiques/produit/{produit}")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Long>> getStatistiquesUtilisationProduit(@PathVariable String produit) {
        try {
            Map<String, Long> statistiques = volontaireHcService.getStatistiquesUtilisationProduit(produit);
            return ResponseEntity.ok(statistiques);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Obtient les produits les plus utilisés (avec valeur "oui")
     *
     * @param limit le nombre de produits à retourner
     * @return une map des produits et leur nombre d'utilisateurs, triée par ordre
     *         décroissant
     */
    @GetMapping("/statistiques/produits-plus-utilises")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Long>> getProduitsLesPlusUtilises(@RequestParam(defaultValue = "10") int limit) {
        try {
            Map<String, Long> produitsUtilisation = volontaireHcService.getProduitsLesPlusUtilises(limit);
            return ResponseEntity.ok(produitsUtilisation);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Obtient les lieux d'achat préférés des volontaires
     *
     * @return une map des lieux d'achat et leur fréquentation, triée par ordre
     *         décroissant
     */
    @GetMapping("/statistiques/lieux-achat-preferences")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Long>> getLieuxAchatPreferences() {
        Map<String, Long> lieuxAchatPreferences = volontaireHcService.getLieuxAchatPreferences();
        return ResponseEntity.ok(lieuxAchatPreferences);
    }

    /**
     * Recherche les volontaires qui utilisent une combinaison de produits
     *
     * @param produits une map des produits à rechercher (nom du produit -> valeur)
     * @return la liste des habitudes de consommation des volontaires concernés
     */
    @PostMapping("/by-multiple-produits")
    @Transactional(readOnly = true)
    public ResponseEntity<List<VolontaireHcDTO>> findByMultipleProduits(@RequestBody Map<String, String> produits) {
        try {
            // Normaliser les valeurs
            Map<String, String> normalizedProduits = produits.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> {
                        String value = entry.getValue();
                        if (value == null) return "non";
                        
                        String lowerValue = value.toLowerCase();
                        if (lowerValue.equals("oui") || lowerValue.equals("yes") || 
                            lowerValue.equals("true") || lowerValue.equals("1")) {
                            return "oui";
                        }
                        return "non";
                    }
                ));
            
            List<VolontaireHcDTO> volontaireHcs = volontaireHcService.findByMultipleProduits(normalizedProduits);
            return ResponseEntity.ok(volontaireHcs);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Récupère les habitudes de consommation pour plusieurs volontaires par leurs
     * IDs
     *
     * @param ids liste d'identifiants des volontaires, séparés par des virgules
     * @return liste des habitudes de consommation des volontaires spécifiés
     */
    @GetMapping("/by-volontaire")
    @Transactional(readOnly = true)
    public ResponseEntity<List<VolontaireHcDTO>> getVolontaireHcsByIds(@RequestParam String ids) {
        try {
            // Convertir la chaîne de caractères en liste d'entiers
            List<Integer> idList = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (idList.isEmpty()) {
                return ResponseEntity.ok(List.of());
            }

            List<VolontaireHcDTO> volontaireHcs = volontaireHcService.getVolontaireHcsByIds(idList);
            return ResponseEntity.ok(volontaireHcs);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Format d'identifiant invalide. Les IDs doivent être des nombres entiers.");
        }
    }
}
