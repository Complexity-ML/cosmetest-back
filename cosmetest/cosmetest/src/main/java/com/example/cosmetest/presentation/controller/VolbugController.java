package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.VolbugDTO;
import com.example.cosmetest.business.service.VolbugService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des bugs de volontaires
 */
@RestController
@RequestMapping("/api/volbugs")
public class VolbugController {

    private final VolbugService volbugService;

    public VolbugController(VolbugService volbugService) {
        this.volbugService = volbugService;
    }

    /**
     * Récupère tous les bugs de volontaires
     *
     * @return liste des bugs
     */
    @GetMapping
    public ResponseEntity<List<VolbugDTO>> getAllVolbugs() {
        List<VolbugDTO> volbugs = volbugService.getAllVolbugs();
        return ResponseEntity.ok(volbugs);
    }

    /**
     * Récupère un bug par l'identifiant du volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return le bug correspondant
     */
    @GetMapping("/{idVol}")
    public ResponseEntity<VolbugDTO> getVolbugByIdVol(@PathVariable Integer idVol) {
        return volbugService.getVolbugByIdVol(idVol)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug non trouvé pour le volontaire avec l'ID: " + idVol));
    }

    /**
     * Crée un nouveau bug de volontaire
     *
     * @param volbugDTO les données du bug à créer
     * @return le bug créé
     */
    @PostMapping
    public ResponseEntity<VolbugDTO> createVolbug(@Valid @RequestBody VolbugDTO volbugDTO) {
        try {
            VolbugDTO createdVolbug = volbugService.createVolbug(volbugDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVolbug);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour un bug existant
     *
     * @param idVol l'identifiant du volontaire
     * @param volbugDTO les nouvelles données du bug
     * @return le bug mis à jour
     */
    @PutMapping("/{idVol}")
    public ResponseEntity<VolbugDTO> updateVolbug(@PathVariable Integer idVol, @Valid @RequestBody VolbugDTO volbugDTO) {
        try {
            return volbugService.updateVolbug(idVol, volbugDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug non trouvé pour le volontaire avec l'ID: " + idVol));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime un bug
     *
     * @param idVol l'identifiant du volontaire
     * @return statut de la suppression
     */
    @DeleteMapping("/{idVol}")
    public ResponseEntity<Void> deleteVolbug(@PathVariable Integer idVol) {
        if (volbugService.deleteVolbug(idVol)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bug non trouvé pour le volontaire avec l'ID: " + idVol);
        }
    }

    /**
     * Vérifie si un bug existe pour un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return true si le bug existe, false sinon
     */
    @GetMapping("/exists/{idVol}")
    public ResponseEntity<Boolean> existsByIdVol(@PathVariable Integer idVol) {
        boolean exists = volbugService.existsByIdVol(idVol);
        return ResponseEntity.ok(exists);
    }
}