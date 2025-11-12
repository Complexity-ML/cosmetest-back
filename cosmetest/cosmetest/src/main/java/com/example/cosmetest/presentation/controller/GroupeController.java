package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.GroupeDTO;
import com.example.cosmetest.business.service.GroupeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des groupes
 */
@RestController
@RequestMapping("/api/groupes")
public class GroupeController {

    private final GroupeService groupeService;

    public GroupeController(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    /**
     * Récupère tous les groupes
     *
     * @return liste des groupes
     */
    @GetMapping
    public ResponseEntity<List<GroupeDTO>> getAllGroupes() {
        List<GroupeDTO> groupes = groupeService.getAllGroupes();
        return ResponseEntity.ok(groupes);
    }

    /**
     * Récupère un groupe par son ID
     *
     * @param id l'identifiant du groupe
     * @return le groupe correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<GroupeDTO> getGroupeById(@PathVariable Integer id) {
        return groupeService.getGroupeById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupe non trouvé avec l'ID: " + id));
    }

    /**
     * Récupère les groupes d'une étude spécifique
     *
     * @param idEtude l'identifiant de l'étude
     * @return la liste des groupes de l'étude
     */
    @GetMapping("/etude/{idEtude}")
    public ResponseEntity<List<GroupeDTO>> getGroupesByIdEtude(@PathVariable int idEtude) {
        List<GroupeDTO> groupes = groupeService.getGroupesByIdEtude(idEtude);
        return ResponseEntity.ok(groupes);
    }

    /**
     * Crée un nouveau groupe
     *
     * @param groupeDTO les données du groupe à créer
     * @return le groupe créé
     */
    @PostMapping
    public ResponseEntity<GroupeDTO> createGroupe(@Valid @RequestBody GroupeDTO groupeDTO) {
        try {
            GroupeDTO createdGroupe = groupeService.createGroupe(groupeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroupe);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour un groupe existant
     *
     * @param id l'identifiant du groupe à mettre à jour
     * @param groupeDTO les nouvelles données du groupe
     * @return le groupe mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<GroupeDTO> updateGroupe(@PathVariable Integer id, @Valid @RequestBody GroupeDTO groupeDTO) {
        try {
            return groupeService.updateGroupe(id, groupeDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupe non trouvé avec l'ID: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime un groupe
     *
     * @param id l'identifiant du groupe à supprimer
     * @return statut de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupe(@PathVariable Integer id) {
        if (groupeService.deleteGroupe(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupe non trouvé avec l'ID: " + id);
        }
    }

    /**
     * Récupère les groupes par tranche d'âge
     *
     * @param ageMin l'âge minimum (optionnel)
     * @param ageMax l'âge maximum (optionnel)
     * @return la liste des groupes correspondants
     */
    @GetMapping("/filtrerParAge")
    public ResponseEntity<List<GroupeDTO>> getGroupesByAgeRange(
            @RequestParam(required = false) Integer ageMin,
            @RequestParam(required = false) Integer ageMax) {
        List<GroupeDTO> groupes = groupeService.getGroupesByAgeRange(ageMin, ageMax);
        return ResponseEntity.ok(groupes);
    }

    /**
     * Récupère les groupes par ethnie
     *
     * @param ethnie l'ethnie recherchée
     * @return la liste des groupes correspondants
     */
    @GetMapping("/ethnie/{ethnie}")
    public ResponseEntity<List<GroupeDTO>> getGroupesByEthnie(@PathVariable String ethnie) {
        List<GroupeDTO> groupes = groupeService.getGroupesByEthnie(ethnie);
        return ResponseEntity.ok(groupes);
    }
}