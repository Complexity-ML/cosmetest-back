package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.PanelDTO;
import com.example.cosmetest.business.service.PanelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des panels
 */
@RestController
@RequestMapping("/api/panels")
public class PanelController {

    private final PanelService panelService;

    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }

    /**
     * Récupère tous les panels
     *
     * @return liste des panels
     */
    @GetMapping
    public ResponseEntity<List<PanelDTO>> getAllPanels() {
        List<PanelDTO> panels = panelService.getAllPanels();
        return ResponseEntity.ok(panels);
    }

    /**
     * Récupère un panel par son ID
     *
     * @param id l'identifiant du panel
     * @return le panel correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<PanelDTO> getPanelById(@PathVariable Integer id) {
        return panelService.getPanelById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel non trouvé avec l'ID: " + id));
    }

    /**
     * Récupère les panels d'une étude spécifique
     *
     * @param idEtude l'identifiant de l'étude
     * @return la liste des panels de l'étude
     */
    @GetMapping("/etude/{idEtude}")
    public ResponseEntity<List<PanelDTO>> getPanelsByIdEtude(@PathVariable int idEtude) {
        List<PanelDTO> panels = panelService.getPanelsByIdEtude(idEtude);
        return ResponseEntity.ok(panels);
    }

    /**
     * Récupère les panels d'un groupe spécifique
     *
     * @param idGroupe l'identifiant du groupe
     * @return la liste des panels du groupe
     */
    @GetMapping("/groupe/{idGroupe}")
    public ResponseEntity<List<PanelDTO>> getPanelsByIdGroupe(@PathVariable int idGroupe) {
        List<PanelDTO> panels = panelService.getPanelsByIdGroupe(idGroupe);
        return ResponseEntity.ok(panels);
    }

    /**
     * Récupère les panels d'une étude et d'un groupe spécifiques
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @return la liste des panels correspondants
     */
    @GetMapping("/etude/{idEtude}/groupe/{idGroupe}")
    public ResponseEntity<List<PanelDTO>> getPanelsByIdEtudeAndIdGroupe(
            @PathVariable int idEtude,
            @PathVariable int idGroupe) {
        List<PanelDTO> panels = panelService.getPanelsByIdEtudeAndIdGroupe(idEtude, idGroupe);
        return ResponseEntity.ok(panels);
    }

    /**
     * Crée un nouveau panel
     *
     * @param panelDTO les données du panel à créer
     * @return le panel créé
     */
    @PostMapping
    public ResponseEntity<PanelDTO> createPanel(@Valid @RequestBody PanelDTO panelDTO) {
        try {
            PanelDTO createdPanel = panelService.createPanel(panelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPanel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour un panel existant
     *
     * @param id l'identifiant du panel à mettre à jour
     * @param panelDTO les nouvelles données du panel
     * @return le panel mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<PanelDTO> updatePanel(@PathVariable Integer id, @Valid @RequestBody PanelDTO panelDTO) {
        try {
            return panelService.updatePanel(id, panelDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel non trouvé avec l'ID: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime un panel
     *
     * @param id l'identifiant du panel à supprimer
     * @return statut de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePanel(@PathVariable Integer id) {
        if (panelService.deletePanel(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Panel non trouvé avec l'ID: " + id);
        }
    }

    /**
     * Récupère les panels par sexe
     *
     * @param sexe le sexe recherché
     * @return la liste des panels correspondants
     */
    @GetMapping("/by-sexe/{sexe}")
    public ResponseEntity<List<PanelDTO>> getPanelsBySexe(@PathVariable String sexe) {
        List<PanelDTO> panels = panelService.getPanelsBySexe(sexe);
        return ResponseEntity.ok(panels);
    }

    /**
     * Récupère les panels par type de peau visage
     *
     * @param typePeauVisage le type de peau recherché
     * @return la liste des panels correspondants
     */
    @GetMapping("/by-type-peau/{typePeauVisage}")
    public ResponseEntity<List<PanelDTO>> getPanelsByTypePeauVisage(@PathVariable String typePeauVisage) {
        List<PanelDTO> panels = panelService.getPanelsByTypePeauVisage(typePeauVisage);
        return ResponseEntity.ok(panels);
    }

    /**
     * Récupère les panels par phototype
     *
     * @param phototype le phototype recherché
     * @return la liste des panels correspondants
     */
    @GetMapping("/by-phototype/{phototype}")
    public ResponseEntity<List<PanelDTO>> getPanelsByPhototype(@PathVariable String phototype) {
        List<PanelDTO> panels = panelService.getPanelsByPhototype(phototype);
        return ResponseEntity.ok(panels);
    }

    /**
     * Recherche avancée de panels par plusieurs critères
     *
     * @param sexe le sexe (optionnel)
     * @param phototype le phototype (optionnel)
     * @param carnation la carnation (optionnel)
     * @return la liste des panels correspondants
     */
    @GetMapping("/search")
    public ResponseEntity<List<PanelDTO>> searchPanelsByMultipleCriteria(
            @RequestParam(required = false) String sexe,
            @RequestParam(required = false) String phototype,
            @RequestParam(required = false) String carnation) {
        List<PanelDTO> panels = panelService.searchPanelsByMultipleCriteria(sexe, phototype, carnation);
        return ResponseEntity.ok(panels);
    }

    /**
     * Récupère les panels d'une étude filtrés par sexe
     *
     * @param idEtude l'identifiant de l'étude
     * @param sexe le sexe recherché
     * @return la liste des panels correspondants
     */
    @GetMapping("/etude/{idEtude}/sexe/{sexe}")
    public ResponseEntity<List<PanelDTO>> getPanelsByIdEtudeAndSexe(
            @PathVariable int idEtude,
            @PathVariable String sexe) {
        List<PanelDTO> panels = panelService.getPanelsByIdEtudeAndSexe(idEtude, sexe);
        return ResponseEntity.ok(panels);
    }

    /**
     * Récupère des statistiques sur les panels d'une étude
     *
     * @param idEtude l'identifiant de l'étude
     * @return des statistiques sur les panels
     */
    @GetMapping("/etude/{idEtude}/statistiques")
    public ResponseEntity<PanelService.PanelStatisticsDTO> getPanelStatisticsByIdEtude(@PathVariable int idEtude) {
        PanelService.PanelStatisticsDTO statistics = panelService.getPanelStatisticsByIdEtude(idEtude);
        return ResponseEntity.ok(statistics);
    }
}