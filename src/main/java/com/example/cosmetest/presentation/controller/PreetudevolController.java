package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.business.service.PreetudevolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des pré-études-volontaires
 */
@RestController
@RequestMapping("/api/preetudevols")
public class PreetudevolController {

    private final PreetudevolService preetudevolService;

    public PreetudevolController(PreetudevolService preetudevolService) {
        this.preetudevolService = preetudevolService;
    }

    /**
     * Récupère toutes les pré-études-volontaires
     *
     * @return liste des pré-études-volontaires
     */
    @GetMapping
    public ResponseEntity<List<PreetudevolDTO>> getAllPreetudevols() {
        List<PreetudevolDTO> preetudevols = preetudevolService.getAllPreetudevols();
        return ResponseEntity.ok(preetudevols);
    }

    /**
     * Récupère une pré-étude-volontaire par son identifiant composite
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @param idVolontaire l'identifiant du volontaire
     * @return la pré-étude-volontaire correspondante
     */
    @GetMapping("/id")
    public ResponseEntity<PreetudevolDTO> getPreetudevolById(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire) {
        return preetudevolService.getPreetudevolById(idEtude, idGroupe, idVolontaire)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Pré-étude-volontaire non trouvée avec ID Etude: " + idEtude +
                                ", ID Groupe: " + idGroupe + ", ID Volontaire: " + idVolontaire));
    }

    /**
     * Récupère les pré-études-volontaires d'une étude spécifique
     *
     * @param idEtude l'identifiant de l'étude
     * @return la liste des pré-études-volontaires
     */
    @GetMapping("/etude/{idEtude}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByIdEtude(@PathVariable int idEtude) {
        List<PreetudevolDTO> preetudevols = preetudevolService.getPreetudevolsByIdEtude(idEtude);
        return ResponseEntity.ok(preetudevols);
    }

    /**
     * Récupère les pré-études-volontaires d'un groupe spécifique
     *
     * @param idGroupe l'identifiant du groupe
     * @return la liste des pré-études-volontaires
     */
    @GetMapping("/groupe/{idGroupe}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByIdGroupe(@PathVariable int idGroupe) {
        List<PreetudevolDTO> preetudevols = preetudevolService.getPreetudevolsByIdGroupe(idGroupe);
        return ResponseEntity.ok(preetudevols);
    }

    /**
     * Récupère les pré-études-volontaires d'un volontaire spécifique
     *
     * @param idVolontaire l'identifiant du volontaire
     * @return la liste des pré-études-volontaires
     */
    @GetMapping("/volontaire/{idVolontaire}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByIdVolontaire(@PathVariable int idVolontaire) {
        List<PreetudevolDTO> preetudevols = preetudevolService.getPreetudevolsByIdVolontaire(idVolontaire);
        return ResponseEntity.ok(preetudevols);
    }

    /**
     * Récupère les pré-études-volontaires d'une étude et d'un groupe spécifiques
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @return la liste des pré-études-volontaires
     */
    @GetMapping("/etude/{idEtude}/groupe/{idGroupe}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByEtudeAndGroupe(
            @PathVariable int idEtude, @PathVariable int idGroupe) {
        List<PreetudevolDTO> preetudevols = preetudevolService.getPreetudevolsByEtudeAndGroupe(idEtude, idGroupe);
        return ResponseEntity.ok(preetudevols);
    }

    /**
     * Récupère les pré-études-volontaires d'une étude et d'un volontaire spécifiques
     *
     * @param idEtude l'identifiant de l'étude
     * @param idVolontaire l'identifiant du volontaire
     * @return la liste des pré-études-volontaires
     */
    @GetMapping("/etude/{idEtude}/volontaire/{idVolontaire}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByEtudeAndVolontaire(
            @PathVariable int idEtude, @PathVariable int idVolontaire) {
        List<PreetudevolDTO> preetudevols = preetudevolService.getPreetudevolsByEtudeAndVolontaire(idEtude, idVolontaire);
        return ResponseEntity.ok(preetudevols);
    }

    /**
     * Crée une nouvelle pré-étude-volontaire
     *
     * @param preetudevolDTO les données de la pré-étude-volontaire à créer
     * @return la pré-étude-volontaire créée
     */
    @PostMapping
    public ResponseEntity<PreetudevolDTO> createPreetudevol(@Valid @RequestBody PreetudevolDTO preetudevolDTO) {
        try {
            PreetudevolDTO createdPreetudevol = preetudevolService.createPreetudevol(preetudevolDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPreetudevol);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour une pré-étude-volontaire existante
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @param idVolontaire l'identifiant du volontaire
     * @param preetudevolDTO les nouvelles données de la pré-étude-volontaire
     * @return la pré-étude-volontaire mise à jour
     */
    @PutMapping("/{idEtude}/{idGroupe}/{idVolontaire}")
    public ResponseEntity<PreetudevolDTO> updatePreetudevol(
            @PathVariable int idEtude,
            @PathVariable int idGroupe,
            @PathVariable int idVolontaire,
            @Valid @RequestBody PreetudevolDTO preetudevolDTO) {
        try {
            return preetudevolService.updatePreetudevol(idEtude, idGroupe, idVolontaire, preetudevolDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Pré-étude-volontaire non trouvée avec ID Etude: " + idEtude +
                                    ", ID Groupe: " + idGroupe + ", ID Volontaire: " + idVolontaire));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime une pré-étude-volontaire
     *
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @param idVolontaire l'identifiant du volontaire
     * @return statut de la suppression
     */
    @DeleteMapping("/{idEtude}/{idGroupe}/{idVolontaire}")
    public ResponseEntity<Void> deletePreetudevol(
            @PathVariable int idEtude,
            @PathVariable int idGroupe,
            @PathVariable int idVolontaire) {
        if (preetudevolService.deletePreetudevol(idEtude, idGroupe, idVolontaire)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Pré-étude-volontaire non trouvée avec ID Etude: " + idEtude +
                            ", ID Groupe: " + idGroupe + ", ID Volontaire: " + idVolontaire);
        }
    }

    /**
     * Supprime toutes les pré-études-volontaires d'une étude
     *
     * @param idEtude l'identifiant de l'étude
     * @return le nombre de pré-études-volontaires supprimées
     */
    @DeleteMapping("/etude/{idEtude}")
    public ResponseEntity<Integer> deletePreetudevolsByIdEtude(@PathVariable int idEtude) {
        int count = preetudevolService.deletePreetudevolsByIdEtude(idEtude);
        return ResponseEntity.ok(count);
    }

    /**
     * Supprime toutes les pré-études-volontaires d'un groupe
     *
     * @param idGroupe l'identifiant du groupe
     * @return le nombre de pré-études-volontaires supprimées
     */
    @DeleteMapping("/groupe/{idGroupe}")
    public ResponseEntity<Integer> deletePreetudevolsByIdGroupe(@PathVariable int idGroupe) {
        int count = preetudevolService.deletePreetudevolsByIdGroupe(idGroupe);
        return ResponseEntity.ok(count);
    }

    /**
     * Supprime toutes les pré-études-volontaires d'un volontaire
     *
     * @param idVolontaire l'identifiant du volontaire
     * @return le nombre de pré-études-volontaires supprimées
     */
    @DeleteMapping("/volontaire/{idVolontaire}")
    public ResponseEntity<Integer> deletePreetudevolsByIdVolontaire(@PathVariable int idVolontaire) {
        int count = preetudevolService.deletePreetudevolsByIdVolontaire(idVolontaire);
        return ResponseEntity.ok(count);
    }
}