package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.EtudeDTO;
import com.example.cosmetest.business.service.EtudeService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Contrôleur REST pour les études
 * Fait partie de la couche de présentation et gère les requêtes HTTP
 */
@RestController
@RequestMapping("/api/etudes")
public class EtudeController {

    private final EtudeService etudeService;

    public EtudeController(EtudeService etudeService) {
        this.etudeService = etudeService;
    }

    /**
     * Récupère toutes les études
     * 
     * @return Liste de toutes les études
     */
    @GetMapping
    public ResponseEntity<List<EtudeDTO>> getAllEtudes() {
        List<EtudeDTO> etudes = etudeService.getAllEtudes();
        return ResponseEntity.ok(etudes);
    }

    /**
     * Récupère les études avec pagination
     * 
     * @param page      Numéro de page
     * @param size      Taille de la page
     * @param sortBy    Champ de tri
     * @param direction Direction du tri
     * @return Page d'études
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<EtudeDTO>> getAllEtudesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateDebut") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<EtudeDTO> etudesPage = etudeService.getAllEtudesPaginated(pageable);

        return ResponseEntity.ok(etudesPage);
    }

    /**
     * Récupère une étude par son ID
     * 
     * @param id ID de l'étude
     * @return L'étude ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<EtudeDTO> getEtudeById(@PathVariable Integer id) {
        Optional<EtudeDTO> etude = etudeService.getEtudeById(id);

        return etude
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une étude par sa référence (path variable — ne fonctionne pas si la ref contient des slashes)
     *
     * @param ref Référence de l'étude
     * @return L'étude ou 404 si non trouvée
     */
    @GetMapping("/ref/{ref}")
    public ResponseEntity<EtudeDTO> getEtudeByRef(@PathVariable String ref) {
        Optional<EtudeDTO> etude = etudeService.getEtudeByRef(ref);

        return etude
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère une étude par sa référence (query param — fonctionne avec tous les caractères)
     *
     * @param ref Référence de l'étude
     * @return L'étude ou 404 si non trouvée
     */
    @GetMapping("/search-by-ref")
    public ResponseEntity<EtudeDTO> searchByRef(@RequestParam String ref) {
        Optional<EtudeDTO> etude = etudeService.getEtudeByRef(ref);

        return etude
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les études par type
     * 
     * @param type Type d'étude
     * @return Liste des études
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<EtudeDTO>> getEtudesByType(@PathVariable String type) {
        List<EtudeDTO> etudes = etudeService.getEtudesByType(type);
        return ResponseEntity.ok(etudes);
    }

    /**
     * Récupère les études dont le titre contient le mot-clé
     * 
     * @param keyword Mot-clé à rechercher
     * @return Liste des études
     */
    @GetMapping("/titre")
    public ResponseEntity<List<EtudeDTO>> getEtudesByTitre(@RequestParam String keyword) {
        List<EtudeDTO> etudes = etudeService.getEtudesByTitre(keyword);
        return ResponseEntity.ok(etudes);
    }

    /**
     * Récupère les études entre deux dates
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Liste des études
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<EtudeDTO>> getEtudesByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date debut,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {

        List<EtudeDTO> etudes = etudeService.getEtudesByDateRange(debut, fin);
        return ResponseEntity.ok(etudes);
    }

    /**
     * Récupère les études actives à une date donnée
     * 
     * @param date Date à vérifier
     * @return Liste des études
     */
    @GetMapping("/actives")
    public ResponseEntity<List<EtudeDTO>> getActiveEtudesAtDate(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        // Si aucune date n'est fournie, utiliser la date du jour
        if (date == null) {
            date = new Date(System.currentTimeMillis());
        }

        List<EtudeDTO> etudes = etudeService.getActiveEtudesAtDate(date);
        return ResponseEntity.ok(etudes);
    }

    /**
     * Récupère les études par indicateur de paiement
     * 
     * @param paye Indicateur de paiement (1 pour payé, 0 pour non payé)
     * @return Liste des études
     */
    @GetMapping("/paye/{paye}")
    public ResponseEntity<List<EtudeDTO>> getEtudesByPaye(@PathVariable int paye) {
        List<EtudeDTO> etudes = etudeService.getEtudesByPaye(paye);
        return ResponseEntity.ok(etudes);
    }

    /**
     * Recherche en texte intégral dans le titre et les commentaires
     * 
     * @param searchTerm Terme de recherche
     * @return Liste des études
     */
    @GetMapping("/search")
    public ResponseEntity<List<EtudeDTO>> searchEtudes(@RequestParam String searchTerm) {
        List<EtudeDTO> etudes = etudeService.searchEtudes(searchTerm);
        return ResponseEntity.ok(etudes);
    }

    /**
     * Crée une nouvelle étude
     * 
     * @param etudeDTO Données de l'étude
     * @return Étude créée
     */
    @PostMapping
    public ResponseEntity<EtudeDTO> createEtude(@RequestBody EtudeDTO etudeDTO) {
        EtudeDTO createdEtude = etudeService.saveEtude(etudeDTO);
        return new ResponseEntity<>(createdEtude, HttpStatus.CREATED);
    }

    /**
     * Met à jour une étude existante
     * 
     * @param id       ID de l'étude
     * @param etudeDTO Nouvelles données
     * @return Étude mise à jour ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<EtudeDTO> updateEtude(
            @PathVariable Integer id,
            @RequestBody EtudeDTO etudeDTO) {

        // Vérifier que l'ID dans le chemin correspond à l'ID dans le DTO
        if (etudeDTO.getIdEtude() != null && !etudeDTO.getIdEtude().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        // Vérifier que l'étude existe
        if (!etudeService.getEtudeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Mettre à jour l'ID dans le DTO
        etudeDTO.setIdEtude(id);

        // Sauvegarder les modifications
        EtudeDTO updatedEtude = etudeService.saveEtude(etudeDTO);

        return ResponseEntity.ok(updatedEtude);
    }

    /**
     * Met à jour le statut PAYE d'une étude
     * 
     * @param id   ID de l'étude
     * @param paye Nouveau statut PAYE (directement l'entier)
     * @return Réponse avec le statut mis à jour
     */
    @PatchMapping("/{id}/paye")
    public ResponseEntity<?> updatePayeStatus(
            @PathVariable Integer id,
            @RequestBody Integer paye) {

        try {
            // Validation du statut PAYE
            if (paye == null || (paye != 0 && paye != 2)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Statut PAYE invalide. Doit être 0 ou 2."));
            }

            // Vérifier que l'étude existe
            Optional<EtudeDTO> existingEtude = etudeService.getEtudeById(id);
            if (!existingEtude.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            // Mettre à jour le statut PAYE
            boolean updated = etudeService.updatePayeStatus(id, paye);

            if (!updated) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Erreur lors de la mise à jour du statut PAYE"));
            }

            // Réponse de succès
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Statut PAYE mis à jour vers " + paye,
                    "idEtude", id,
                    "paye", paye));

        } catch (Exception e) {
            System.err.println("Erreur mise à jour statut PAYE pour l'étude " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Erreur lors de la mise à jour du statut PAYE",
                            "error", e.getMessage()));
        }
    }

    /**
     * Supprime une étude
     * 
     * @param id ID de l'étude
     * @return Réponse vide avec statut 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtude(@PathVariable Integer id) {
        // Vérifier que l'étude existe
        if (!etudeService.getEtudeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        etudeService.deleteEtude(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Vérifie si une référence d'étude est déjà utilisée
     * 
     * @param ref Référence à vérifier
     * @return true si la référence est déjà utilisée
     */
    @GetMapping("/check-ref/{ref}")
    public ResponseEntity<Boolean> isRefAlreadyUsed(@PathVariable String ref) {
        boolean isUsed = etudeService.isRefAlreadyUsed(ref);
        return ResponseEntity.ok(isUsed);
    }

    /**
     * Compte le nombre d'études par type
     * 
     * @param type Type d'étude
     * @return Nombre d'études
     */
    @GetMapping("/count/type/{type}")
    public ResponseEntity<Long> countEtudesByType(@PathVariable String type) {
        Long count = etudeService.countEtudesByType(type);
        return ResponseEntity.ok(count);
    }

    /**
     * Récupère les études à venir (date de début dans le futur)
     * 
     * @return Liste des études
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<EtudeDTO>> getUpcomingEtudes() {
        List<EtudeDTO> etudes = etudeService.getUpcomingEtudes();
        return ResponseEntity.ok(etudes);
    }

    /**
     * Récupère les études en cours
     * 
     * @return Liste des études
     */
    @GetMapping("/current")
    public ResponseEntity<List<EtudeDTO>> getCurrentEtudes() {
        List<EtudeDTO> etudes = etudeService.getCurrentEtudes();
        return ResponseEntity.ok(etudes);
    }

    /**
     * Récupère les études terminées
     * 
     * @return Liste des études
     */
    @GetMapping("/completed")
    public ResponseEntity<List<EtudeDTO>> getCompletedEtudes() {
        List<EtudeDTO> etudes = etudeService.getCompletedEtudes();
        return ResponseEntity.ok(etudes);
    }



}