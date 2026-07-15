package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.service.VolontaireHcService;
import com.example.cosmetest.presentation.request.ProduitUpdateRequest;
import com.example.cosmetest.presentation.request.ProduitsUpdateRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping({"/api/volontaires-hc", "/api/v1/volontaires-hc"})
public class VolontaireHcController {

    private final VolontaireHcService volontaireHcService;

    private static final Logger logger = LoggerFactory.getLogger(VolontaireHcController.class);

    public VolontaireHcController(VolontaireHcService volontaireHcService) {
        this.volontaireHcService = volontaireHcService;
    }

    /**
     * Récupère toutes les habitudes de consommation
     *
     * @return liste des habitudes de consommation
     */
    @GetMapping
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
    public ResponseEntity<VolontaireHcDTO> getVolontaireHcByIdVol(@PathVariable Integer idVol) {
        return volontaireHcService.getVolontaireHcByIdVol(idVol)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Habitudes de consommation non trouvées pour le volontaire avec l'ID: " + idVol));
    }

    /**
     * Crée ou met à jour les habitudes de consommation d'un volontaire
     *
     * @param volontaireHcDTO les données des habitudes de consommation
     * @return les habitudes de consommation créées ou mises à jour
     */
    @PostMapping
    public ResponseEntity<VolontaireHcDTO> saveVolontaireHc(@Valid @RequestBody VolontaireHcDTO volontaireHcDTO) {
        return ResponseEntity.ok(volontaireHcService.saveVolontaireHc(volontaireHcDTO));
    }


    /**
     * Supprime les habitudes de consommation d'un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return statut de la suppression
     */
    @DeleteMapping("/volontaire/{idVol}")
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
