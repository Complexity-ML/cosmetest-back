package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.PanelHcDTO;
import com.example.cosmetest.business.service.PanelHcService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des panels HC
 */
@RestController
@RequestMapping("/api/panels-hc")
public class PanelHcController {

    private final PanelHcService panelHcService;

    public PanelHcController(PanelHcService panelHcService) {
        this.panelHcService = panelHcService;
    }

    /**
     * Récupère tous les panels HC
     *
     * @return liste des panels HC
     */
    @GetMapping
    public ResponseEntity<List<PanelHcDTO>> getAllPanelsHc() {
        List<PanelHcDTO> panelsHc = panelHcService.getAllPanelsHc();
        return ResponseEntity.ok(panelsHc);
    }

    /**
     * Récupère un panel HC par son ID
     *
     * @param id l'identifiant du panel
     * @return le panel HC correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<PanelHcDTO> getPanelHcById(@PathVariable int id) {
        return panelHcService.getPanelHcById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel HC non trouvé avec l'ID: " + id));
    }

    /**
     * Crée un nouveau panel HC
     *
     * @param panelHcDTO les données du panel à créer
     * @return le panel HC créé
     */
    @PostMapping
    public ResponseEntity<PanelHcDTO> createPanelHc(@Valid @RequestBody PanelHcDTO panelHcDTO) {
        try {
            PanelHcDTO createdPanelHc = panelHcService.createPanelHc(panelHcDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPanelHc);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour un panel HC existant
     *
     * @param id l'identifiant du panel à mettre à jour
     * @param panelHcDTO les nouvelles données du panel
     * @return le panel HC mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<PanelHcDTO> updatePanelHc(@PathVariable int id, @Valid @RequestBody PanelHcDTO panelHcDTO) {
        try {
            return panelHcService.updatePanelHc(id, panelHcDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel HC non trouvé avec l'ID: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime un panel HC
     *
     * @param id l'identifiant du panel à supprimer
     * @return statut de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePanelHc(@PathVariable int id) {
        if (panelHcService.deletePanelHc(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel HC non trouvé avec l'ID: " + id);
        }
    }

    /**
     * Récupère les panels HC par lieu d'achat
     *
     * @param lieu le lieu d'achat
     * @return liste des panels correspondants
     */
    @GetMapping("/lieu-achat")
    public ResponseEntity<List<PanelHcDTO>> getPanelsByLieuAchat(@RequestParam String lieu) {
        List<PanelHcDTO> panelsHc = panelHcService.getPanelsByLieuAchat(lieu);
        return ResponseEntity.ok(panelsHc);
    }

    /**
     * Récupère les panels HC par utilisation de produits bio
     *
     * @param valeur la valeur du champ produitsBio (généralement "Oui" ou "Non")
     * @return liste des panels correspondants
     */
    @GetMapping("/produits-bio")
    public ResponseEntity<List<PanelHcDTO>> getPanelsByProduitsBio(@RequestParam String valeur) {
        List<PanelHcDTO> panelsHc = panelHcService.getPanelsByProduitsBio(valeur);
        return ResponseEntity.ok(panelsHc);
    }

    /**
     * Récupère les panels HC par méthode d'épilation
     *
     * @param methode la méthode d'épilation
     * @return liste des panels correspondants
     */
    @GetMapping("/epilation")
    public ResponseEntity<List<PanelHcDTO>> getPanelsByMethodeEpilation(@RequestParam String methode) {
        List<PanelHcDTO> panelsHc = panelHcService.getPanelsByMethodeEpilation(methode);
        return ResponseEntity.ok(panelsHc);
    }

    /**
     * Récupère les panels HC par type de maquillage
     *
     * @param type le type de maquillage
     * @return liste des panels correspondants
     */
    @GetMapping("/maquillage")
    public ResponseEntity<List<PanelHcDTO>> getPanelsByTypeMaquillage(@RequestParam String type) {
        List<PanelHcDTO> panelsHc = panelHcService.getPanelsByTypeMaquillage(type);
        return ResponseEntity.ok(panelsHc);
    }

    /**
     * Récupère les panels HC par type de soin visage
     *
     * @param soin le type de soin visage
     * @return liste des panels correspondants
     */
    @GetMapping("/soin-visage")
    public ResponseEntity<List<PanelHcDTO>> getPanelsBySoinVisage(@RequestParam String soin) {
        List<PanelHcDTO> panelsHc = panelHcService.getPanelsBySoinVisage(soin);
        return ResponseEntity.ok(panelsHc);
    }

    /**
     * Récupère les panels HC par type de soin corps
     *
     * @param soin le type de soin corps
     * @return liste des panels correspondants
     */
    @GetMapping("/soin-corps")
    public ResponseEntity<List<PanelHcDTO>> getPanelsBySoinCorps(@RequestParam String soin) {
        List<PanelHcDTO> panelsHc = panelHcService.getPanelsBySoinCorps(soin);
        return ResponseEntity.ok(panelsHc);
    }

    /**
     * Récupère les panels HC par type de produit capillaire
     *
     * @param produit le type de produit capillaire
     * @return liste des panels correspondants
     */
    @GetMapping("/produit-capillaire")
    public ResponseEntity<List<PanelHcDTO>> getPanelsByProduitCapillaire(@RequestParam String produit) {
        List<PanelHcDTO> panelsHc = panelHcService.getPanelsByProduitCapillaire(produit);
        return ResponseEntity.ok(panelsHc);
    }
}