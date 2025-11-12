package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.service.AnnulationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour les annulations
 * Fait partie de la couche de présentation et gère les requêtes HTTP
 */
@RestController
@RequestMapping("/api/annulations")
public class AnnulationController {

    private final AnnulationService annulationService;

    public AnnulationController(AnnulationService annulationService) {
        this.annulationService = annulationService;
    }

    /**
     * Récupère toutes les annulations
     * @return Liste de toutes les annulations
     */
    @GetMapping
    public ResponseEntity<List<AnnulationDTO>> getAllAnnulations() {
        List<AnnulationDTO> annulations = annulationService.getAllAnnulations();
        return ResponseEntity.ok(annulations);
    }

    /**
     * Récupère les annulations avec pagination
     * @param page Numéro de page
     * @param size Taille de la page
     * @param sortBy Champ de tri
     * @param direction Direction du tri
     * @return Page d'annulations
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<AnnulationDTO>> getAllAnnulationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateAnnulation") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<AnnulationDTO> annulationsPage = annulationService.getAllAnnulationsPaginated(pageable);

        return ResponseEntity.ok(annulationsPage);
    }

    /**
     * Récupère une annulation par son ID
     * @param id ID de l'annulation
     * @return L'annulation ou 404 si non trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnnulationDTO> getAnnulationById(@PathVariable Integer id) {
        Optional<AnnulationDTO> annulation = annulationService.getAnnulationById(id);

        return annulation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupère les annulations d'un volontaire
     * @param idVol ID du volontaire
     * @return Liste des annulations
     */
    @GetMapping("/volontaire/{idVol}")
    public ResponseEntity<List<AnnulationDTO>> getAnnulationsByVolontaire(@PathVariable int idVol) {
        List<AnnulationDTO> annulations = annulationService.getAnnulationsByVolontaire(idVol);
        return ResponseEntity.ok(annulations);
    }

    /**
     * Récupère les annulations d'une étude
     * @param idEtude ID de l'étude
     * @return Liste des annulations
     */
    @GetMapping("/etude/{idEtude}")
    public ResponseEntity<List<AnnulationDTO>> getAnnulationsByEtude(@PathVariable int idEtude) {
        List<AnnulationDTO> annulations = annulationService.getAnnulationsByEtude(idEtude);
        return ResponseEntity.ok(annulations);
    }

    /**
     * Récupère les annulations d'un volontaire pour une étude spécifique
     * @param idVol ID du volontaire
     * @param idEtude ID de l'étude
     * @return Liste des annulations
     */
    @GetMapping("/volontaire/{idVol}/etude/{idEtude}")
    public ResponseEntity<List<AnnulationDTO>> getAnnulationsByVolontaireAndEtude(
            @PathVariable int idVol,
            @PathVariable int idEtude) {

        List<AnnulationDTO> annulations = annulationService.getAnnulationsByVolontaireAndEtude(idVol, idEtude);
        return ResponseEntity.ok(annulations);
    }

    /**
     * Récupère les annulations à une date spécifique
     * @param date Date recherchée
     * @return Liste des annulations
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<AnnulationDTO>> getAnnulationsByDate(@PathVariable String date) {
        List<AnnulationDTO> annulations = annulationService.getAnnulationsByDate(date);
        return ResponseEntity.ok(annulations);
    }

    /**
     * Recherche des annulations par mot-clé dans les commentaires
     * @param keyword Mot-clé à rechercher
     * @return Liste des annulations
     */
    @GetMapping("/search")
    public ResponseEntity<List<AnnulationDTO>> searchAnnulationsByCommentaire(@RequestParam String keyword) {
        List<AnnulationDTO> annulations = annulationService.searchAnnulationsByCommentaire(keyword);
        return ResponseEntity.ok(annulations);
    }

    /**
     * Crée une nouvelle annulation
     * @param annulationDTO Données de l'annulation
     * @return Annulation créée
     */
    @PostMapping
    public ResponseEntity<AnnulationDTO> createAnnulation(@RequestBody AnnulationDTO annulationDTO) {
        AnnulationDTO createdAnnulation = annulationService.saveAnnulation(annulationDTO);
        return new ResponseEntity<>(createdAnnulation, HttpStatus.CREATED);
    }

    /**
     * Met à jour une annulation existante
     * @param id ID de l'annulation
     * @param annulationDTO Nouvelles données
     * @return Annulation mise à jour ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnnulationDTO> updateAnnulation(
            @PathVariable Integer id,
            @RequestBody AnnulationDTO annulationDTO) {

        // Vérifier que l'ID dans le chemin correspond à l'ID dans le DTO
        if (annulationDTO.getIdAnnuler() != null && !annulationDTO.getIdAnnuler().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        // Vérifier que l'annulation existe
        if (!annulationService.getAnnulationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Mettre à jour l'ID dans le DTO
        annulationDTO.setIdAnnuler(id);

        // Sauvegarder les modifications
        AnnulationDTO updatedAnnulation = annulationService.saveAnnulation(annulationDTO);

        return ResponseEntity.ok(updatedAnnulation);
    }

    /**
     * Supprime une annulation
     * @param id ID de l'annulation
     * @return Réponse vide avec statut 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnulation(@PathVariable Integer id) {
        // Vérifier que l'annulation existe
        if (!annulationService.getAnnulationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        annulationService.deleteAnnulation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Compte le nombre d'annulations par volontaire
     * @param idVol ID du volontaire
     * @return Nombre d'annulations
     */
    @GetMapping("/count/volontaire/{idVol}")
    public ResponseEntity<Long> countAnnulationsByVolontaire(@PathVariable int idVol) {
        Long count = annulationService.countAnnulationsByVolontaire(idVol);
        return ResponseEntity.ok(count);
    }

    /**
     * Récupère les annulations d'un volontaire triées par date (la plus récente d'abord)
     * @param idVol ID du volontaire
     * @return Liste des annulations triées
     */
    @GetMapping("/volontaire/{idVol}/recent")
    public ResponseEntity<List<AnnulationDTO>> getAnnulationsByVolontaireOrderByDateDesc(@PathVariable int idVol) {
        List<AnnulationDTO> annulations = annulationService.getAnnulationsByVolontaireOrderByDateDesc(idVol);
        return ResponseEntity.ok(annulations);
    }
}