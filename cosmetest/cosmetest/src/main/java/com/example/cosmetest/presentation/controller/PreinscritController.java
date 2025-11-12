package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.PreinscritDTO;
import com.example.cosmetest.business.service.PreinscritService;
import com.example.cosmetest.presentation.request.PlanifierRendezVousRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des préinscrits
 */
@RestController
@RequestMapping("/api/preinscrits")
public class PreinscritController {

    private final PreinscritService preinscritService;

    public PreinscritController(PreinscritService preinscritService) {
        this.preinscritService = preinscritService;
    }

    /**
     * Récupère tous les préinscrits
     *
     * @return liste des préinscrits
     */
    @GetMapping
    public ResponseEntity<List<PreinscritDTO>> getAllPreinscrits() {
        List<PreinscritDTO> preinscrits = preinscritService.getAllPreinscrits();
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère un préinscrit par son ID
     *
     * @param id l'identifiant du préinscrit
     * @return le préinscrit correspondant
     */
    @GetMapping("/{id}")
    public ResponseEntity<PreinscritDTO> getPreinscritById(@PathVariable Integer id) {
        return preinscritService.getPreinscritById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préinscrit non trouvé avec l'ID: " + id));
    }

    /**
     * Récupère un préinscrit par son email
     *
     * @param email l'email du préinscrit
     * @return le préinscrit correspondant
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<PreinscritDTO> getPreinscritByEmail(@PathVariable String email) {
        return preinscritService.getPreinscritByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préinscrit non trouvé avec l'email: " + email));
    }

    /**
     * Récupère les préinscrits par nom
     *
     * @param nom le nom recherché
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/nom/{nom}")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsByNom(@PathVariable String nom) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsByNom(nom);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère les préinscrits par prénom
     *
     * @param prenom le prénom recherché
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/prenom/{prenom}")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsByPrenom(@PathVariable String prenom) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsByPrenom(prenom);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère les préinscrits par nom et prénom
     *
     * @param nom le nom recherché
     * @param prenom le prénom recherché
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/recherche")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsByNomAndPrenom(
            @RequestParam String nom,
            @RequestParam String prenom) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsByNomAndPrenom(nom, prenom);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère les préinscrits par état
     *
     * @param etat l'état recherché
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/etat/{etat}")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsByEtat(@PathVariable String etat) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsByEtat(etat);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère les préinscrits par ethnie
     *
     * @param ethnie l'ethnie recherchée
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/ethnie/{ethnie}")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsByEthnie(@PathVariable String ethnie) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsByEthnie(ethnie);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère les préinscrits par sexe
     *
     * @param sexe le sexe recherché
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/sexe/{sexe}")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsBySexe(@PathVariable String sexe) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsBySexe(sexe);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère les préinscrits par date de rendez-vous
     *
     * @param rdvDate la date de rendez-vous recherchée
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/rdv-date/{rdvDate}")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsByRdvDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date rdvDate) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsByRdvDate(rdvDate);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Récupère les préinscrits par phototype
     *
     * @param phototype le phototype recherché
     * @return la liste des préinscrits correspondants
     */
    @GetMapping("/phototype/{phototype}")
    public ResponseEntity<List<PreinscritDTO>> getPreinscritsByPhototype(@PathVariable String phototype) {
        List<PreinscritDTO> preinscrits = preinscritService.getPreinscritsByPhototype(phototype);
        return ResponseEntity.ok(preinscrits);
    }

    /**
     * Crée un nouveau préinscrit
     *
     * @param preinscritDTO les données du préinscrit à créer
     * @return le préinscrit créé
     */
    @PostMapping
    public ResponseEntity<PreinscritDTO> createPreinscrit(@Valid @RequestBody PreinscritDTO preinscritDTO) {
        try {
            PreinscritDTO createdPreinscrit = preinscritService.createPreinscrit(preinscritDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPreinscrit);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour un préinscrit existant
     *
     * @param id l'identifiant du préinscrit à mettre à jour
     * @param preinscritDTO les nouvelles données du préinscrit
     * @return le préinscrit mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<PreinscritDTO> updatePreinscrit(@PathVariable Integer id, @Valid @RequestBody PreinscritDTO preinscritDTO) {
        try {
            return preinscritService.updatePreinscrit(id, preinscritDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préinscrit non trouvé avec l'ID: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime un préinscrit
     *
     * @param id l'identifiant du préinscrit à supprimer
     * @return statut de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreinscrit(@PathVariable Integer id) {
        if (preinscritService.deletePreinscrit(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Préinscrit non trouvé avec l'ID: " + id);
        }
    }

    /**
     * Met à jour l'état d'un préinscrit
     *
     * @param id l'identifiant du préinscrit
     * @param etat le nouvel état
     * @return le préinscrit mis à jour
     */
    @PatchMapping("/{id}/etat")
    public ResponseEntity<PreinscritDTO> updateEtat(@PathVariable Integer id, @RequestParam String etat) {
        try {
            return preinscritService.updateEtat(id, etat)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préinscrit non trouvé avec l'ID: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Planifie un rendez-vous pour un préinscrit
     *
     * @param id l'identifiant du préinscrit
     * @param request la requête contenant la date et l'heure du rendez-vous
     * @return le préinscrit mis à jour
     */
    @PatchMapping("/{id}/rdv")
    public ResponseEntity<PreinscritDTO> planifierRendezVous(
            @PathVariable Integer id,
            @Valid @RequestBody PlanifierRendezVousRequest request) {
        try {
            return preinscritService.planifierRendezVous(id, request.getRdvDate(), request.getRdvHeure())
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préinscrit non trouvé avec l'ID: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Vérifie si un email existe déjà
     *
     * @param email l'email à vérifier
     * @return true si l'email existe déjà, false sinon
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = preinscritService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}