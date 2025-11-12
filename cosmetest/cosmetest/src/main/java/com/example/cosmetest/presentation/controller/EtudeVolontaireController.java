package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.domain.model.EtudeVolontaireId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur REST pour les associations étude-volontaire
 * Version améliorée avec gestion d'erreurs uniforme et validation
 */
@RestController
@RequestMapping("/api/etude-volontaires")
@CrossOrigin(origins = "*")
public class EtudeVolontaireController {

    private final EtudeVolontaireService etudeVolontaireService;

    public EtudeVolontaireController(EtudeVolontaireService etudeVolontaireService) {
        this.etudeVolontaireService = etudeVolontaireService;
    }

    // ===============================
    // ENDPOINTS DE LECTURE
    // ===============================

    @GetMapping
    public ResponseEntity<ApiResponse<List<EtudeVolontaireDTO>>> getAllEtudeVolontaires() {
        try {
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
            Page<EtudeVolontaireDTO> etudeVolontairesPage = etudeVolontaireService
                    .getAllEtudeVolontairesPaginated(pageable);
            List<EtudeVolontaireDTO> etudeVolontaires = etudeVolontairesPage.getContent();
            return ResponseEntity.ok(ApiResponse.success(etudeVolontaires, "Associations récupérées avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des associations", e.getMessage()));
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<EtudeVolontaireDTO>>> getAllEtudeVolontairesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<EtudeVolontaireDTO> etudeVolontairesPage = etudeVolontaireService
                    .getAllEtudeVolontairesPaginated(pageable);
            return ResponseEntity.ok(ApiResponse.success(etudeVolontairesPage, "Page récupérée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération paginée", e.getMessage()));
        }
    }

    @GetMapping("/by-composite-id")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> getEtudeVolontaireById(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut) {
        try {
            EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye, statut);
            Optional<EtudeVolontaireDTO> etudeVolontaire = etudeVolontaireService.getEtudeVolontaireById(id);

            if (etudeVolontaire.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(etudeVolontaire.get(), "Association trouvée"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Association non trouvée", "Aucune association avec ces identifiants"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche", e.getMessage()));
        }
    }

    // Endpoints par critères simplifiés
    @GetMapping("/etude/{idEtude}")
    public ResponseEntity<ApiResponse<List<EtudeVolontaireDTO>>> getVolontairesByEtude(@PathVariable int idEtude) {
        return handleServiceCall(
                () -> etudeVolontaireService.getEtudeVolontairesByEtude(idEtude),
                "Volontaires de l'étude récupérés avec succès");
    }

    @GetMapping("/volontaire/{idVolontaire}")
    public ResponseEntity<ApiResponse<List<EtudeVolontaireDTO>>> getEtudesByVolontaire(@PathVariable int idVolontaire) {
        return handleServiceCall(
                () -> etudeVolontaireService.getEtudeVolontairesByVolontaire(idVolontaire),
                "Études du volontaire récupérées avec succès");
    }

    @GetMapping("/groupe/{idGroupe}")
    public ResponseEntity<ApiResponse<List<EtudeVolontaireDTO>>> getVolontairesByGroupe(@PathVariable int idGroupe) {
        return handleServiceCall(
                () -> etudeVolontaireService.getEtudeVolontairesByGroupe(idGroupe),
                "Volontaires du groupe récupérés avec succès");
    }

    // ===============================
    // ENDPOINTS DE MODIFICATION
    // ===============================

    @PostMapping
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> createEtudeVolontaire(
            @Valid @RequestBody EtudeVolontaireDTO etudeVolontaireDTO) {
        try {
            EtudeVolontaireDTO created = etudeVolontaireService.saveEtudeVolontaire(etudeVolontaireDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Association créée avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Données invalides", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la création", e.getMessage()));
        }
    }

    @PatchMapping("/update-volontaire")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updateVolontaire(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut,
            @RequestParam(required = false) Integer nouveauVolontaireId) { //  Peut être null

        return handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateVolontaire(id, nouveauVolontaireId);
                },
                "Volontaire mis à jour avec succès");
    }

    @PatchMapping("/update-statut")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updateStatut(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut,
            @RequestParam String nouveauStatut) {

        return handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateStatut(id, nouveauStatut);
                },
                "Statut mis à jour avec succès");
    }

    @PatchMapping("/update-numsujet")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updateNumSujet(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut,
            @RequestParam int nouveauNumSujet) {

        return handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateNumSujet(id, nouveauNumSujet);
                },
                "Numéro de sujet mis à jour avec succès");
    }

    @PatchMapping("/update-iv")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updateIV(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut,
            @RequestParam int nouvelIV) {

        return handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateIV(id, nouvelIV);
                },
                "Indemnité mise à jour avec succès");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteEtudeVolontaire(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut) {
        try {
            EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye, statut);
            etudeVolontaireService.deleteEtudeVolontaire(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Association supprimée avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Association non trouvée", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la suppression", e.getMessage()));
        }
    }

    // Ajoutez cet endpoint dans votre EtudeVolontaireController.java
    // Dans la section "ENDPOINTS DE MODIFICATION"

    @PatchMapping("/update-paye")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updatePaye(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut,
            @RequestParam int nouveauPaye) {

        return handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updatePaye(id, nouveauPaye);
                },
                "Statut de paiement mis à jour avec succès");
    }

    // Ajoutez aussi cet endpoint si vous l'utilisez dans votre React :
    @GetMapping("/paiements")
    public ResponseEntity<ApiResponse<List<EtudeVolontaireDTO>>> getAllPaiements() {
        try {
            // Utiliser la même logique que getAllEtudeVolontaires mais avec un nom plus
            // explicite
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
            Page<EtudeVolontaireDTO> etudeVolontairesPage = etudeVolontaireService
                    .getAllEtudeVolontairesPaginated(pageable);
            List<EtudeVolontaireDTO> paiements = etudeVolontairesPage.getContent();
            return ResponseEntity.ok(ApiResponse.success(paiements, "Données de paiements récupérées avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des paiements", e.getMessage()));
        }
    }

    // ===============================
    // ENDPOINTS UTILITAIRES
    // ===============================

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> checkExistence(
            @RequestParam int idEtude,
            @RequestParam int idVolontaire) {
        return handleServiceCall(
                () -> etudeVolontaireService.existsByEtudeAndVolontaire(idEtude, idVolontaire),
                "Vérification effectuée");
    }

    @GetMapping("/count/volontaires/{idEtude}")
    public ResponseEntity<ApiResponse<Long>> countVolontairesByEtude(@PathVariable int idEtude) {
        return handleServiceCall(
                () -> etudeVolontaireService.countVolontairesByEtude(idEtude),
                "Comptage effectué");
    }

    // ===============================
    // MÉTHODES UTILITAIRES PRIVÉES
    // ===============================

    /**
     * Méthode générique pour gérer les appels de service avec gestion d'erreurs
     * uniforme
     */
    private <T> ResponseEntity<ApiResponse<T>> handleServiceCall(ServiceCall<T> serviceCall, String successMessage) {
        try {
            T result = serviceCall.call();
            return ResponseEntity.ok(ApiResponse.success(result, successMessage));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Paramètres invalides", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur interne", e.getMessage()));
        }
    }

    /**
     * Méthode spécialisée pour les opérations de mise à jour
     */
    private ResponseEntity<ApiResponse<EtudeVolontaireDTO>> handleUpdateOperation(
            ServiceCall<EtudeVolontaireDTO> updateCall, String successMessage) {
        try {
            EtudeVolontaireDTO result = updateCall.call();
            return ResponseEntity.ok(ApiResponse.success(result, successMessage));
        } catch (IllegalArgumentException e) {
            // Gérer les différents types d'erreurs métier
            if (e.getMessage().contains("existe déjà")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error("CONFLIT", e.getMessage()));
            } else if (e.getMessage().contains("n'existe pas")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("NON_TROUVE", e.getMessage()));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("VALIDATION", e.getMessage()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("ERREUR_INTERNE", e.getMessage()));
        }
    }

    /**
     * Interface fonctionnelle pour les appels de service
     */
    @FunctionalInterface
    private interface ServiceCall<T> {
        T call() throws Exception;
    }
}

// ===============================
// CLASSES DE RÉPONSE
// ===============================

/**
 * Classe de réponse API standardisée
 */
class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ErrorResponse error;

    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> error(String errorType, String errorMessage) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = new ErrorResponse(errorType, errorMessage);
        return response;
    }

    // Getters et setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }
}

/**
 * Classe pour les détails d'erreur
 */
class ErrorResponse {
    private String type;
    private String message;
    private String timestamp;

    public ErrorResponse(String type, String message) {
        this.type = type;
        this.message = message;
        this.timestamp = java.time.Instant.now().toString();
    }

    // Getters et setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}