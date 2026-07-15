package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.GroupeDTO;
import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.business.service.EtudeVolontaireCommandService;
import com.example.cosmetest.business.service.EtudeVolontaireRepairService;
import com.example.cosmetest.business.service.GroupeService;
import com.example.cosmetest.business.service.RdvService;
import com.example.cosmetest.domain.model.AuditLog;
import com.example.cosmetest.domain.model.EtudeVolontaireId;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour les associations étude-volontaire
 * Version améliorée avec gestion d'erreurs uniforme et validation
 */
@RestController
@RequestMapping({"/api/etude-volontaires", "/api/v1/etude-volontaires"})
public class EtudeVolontaireController {

    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final int MAX_PAGE_SIZE = 100;

    private final EtudeVolontaireService etudeVolontaireService;
    private final RdvService rdvService;
    private final GroupeService groupeService;
    private final EtudeService etudeService;
    private final AuditLogService auditLogService;
    private final EtudeVolontaireRepairService repairService;
    private final EtudeVolontaireCommandService commandService;

    @Autowired
    public EtudeVolontaireController(EtudeVolontaireService etudeVolontaireService,
                                     RdvService rdvService,
                                     GroupeService groupeService,
                                     EtudeService etudeService,
                                     AuditLogService auditLogService,
                                     EtudeVolontaireRepairService repairService,
                                     EtudeVolontaireCommandService commandService) {
        this.etudeVolontaireService = etudeVolontaireService;
        this.rdvService = rdvService;
        this.groupeService = groupeService;
        this.etudeService = etudeService;
        this.auditLogService = auditLogService;
        this.repairService = repairService;
        this.commandService = commandService;
    }

    EtudeVolontaireController(EtudeVolontaireService etudeVolontaireService,
                              RdvService rdvService,
                              GroupeService groupeService,
                              EtudeService etudeService,
                              AuditLogService auditLogService) {
        this(etudeVolontaireService, rdvService, groupeService, etudeService, auditLogService, null,
                new EtudeVolontaireCommandService(etudeVolontaireService, auditLogService));
    }

    private String evDetails(int idEtude, int idGroupe, int idVolontaire, String action) {
        String etudeRef = etudeService.getEtudeById(idEtude).map(e -> e.getRef() + "(#" + idEtude + ")").orElse("#" + idEtude);
        String groupeLib = groupeService.getGroupeById(idGroupe).map(g -> g.getIntitule() + "(#" + idGroupe + ")").orElse("#" + idGroupe);
        return "vol:#" + idVolontaire + " etude:" + etudeRef + " groupe:" + groupeLib + (action != null ? " | " + action : "");
    }

    // ===============================
    // ENDPOINTS DE LECTURE
    // ===============================

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> getByTechnicalId(@PathVariable Long id) {
        return etudeVolontaireService.getEtudeVolontaireById(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto, "Association trouvée")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Association non trouvée", "ID technique " + id)));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updateStatutByTechnicalId(
            @PathVariable Long id, @RequestParam String nouveauStatut) {
        return ResponseEntity.ok(ApiResponse.success(etudeVolontaireService.updateStatut(id, nouveauStatut),
                "Statut mis à jour avec succès"));
    }

    @PatchMapping("/{id}/paye")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updatePayeByTechnicalId(
            @PathVariable Long id, @RequestParam int nouveauPaye) {
        return ResponseEntity.ok(ApiResponse.success(etudeVolontaireService.updatePaye(id, nouveauPaye),
                "Statut de paiement mis à jour avec succès"));
    }

    @PatchMapping("/{id}/iv")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updateIvByTechnicalId(
            @PathVariable Long id, @RequestParam int nouvelIV) {
        return ResponseEntity.ok(ApiResponse.success(etudeVolontaireService.updateIV(id, nouvelIV),
                "Indemnité mise à jour avec succès"));
    }

    @PatchMapping("/{id}/numsujet")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updateNumSujetByTechnicalId(
            @PathVariable Long id, @RequestParam int nouveauNumSujet) {
        return ResponseEntity.ok(ApiResponse.success(etudeVolontaireService.updateNumSujet(id, nouveauNumSujet),
                "Numéro de sujet mis à jour avec succès"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteByTechnicalId(@PathVariable Long id) {
        etudeVolontaireService.deleteEtudeVolontaire(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Association supprimée avec succès"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EtudeVolontaireDTO>>> getAllEtudeVolontaires(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            Pageable pageable = boundedPageable(page, size);
            Page<EtudeVolontaireDTO> etudeVolontairesPage = etudeVolontaireService
                    .getAllEtudeVolontairesPaginated(pageable);
            return ResponseEntity.ok(ApiResponse.success(etudeVolontairesPage, "Associations récupérées avec succès"));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer les associations", e);
        }
    }

    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<EtudeVolontaireDTO>>> getAllEtudeVolontairesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = boundedPageable(page, size);
            Page<EtudeVolontaireDTO> etudeVolontairesPage = etudeVolontaireService
                    .getAllEtudeVolontairesPaginated(pageable);
            return ResponseEntity.ok(ApiResponse.success(etudeVolontairesPage, "Page récupérée avec succès"));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de récupérer la page d'associations", e);
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
            throw new RuntimeException("Impossible de rechercher l'association", e);
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
            @Valid @RequestBody EtudeVolontaireDTO etudeVolontaireDTO,
            HttpServletRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String utilisateur = authentication != null ? authentication.getName() : "unknown";
        EtudeVolontaireDTO created = commandService.create(
                etudeVolontaireDTO, utilisateur, request.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Association créée avec succès"));
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
            @RequestParam(required = false) Integer nouveauVolontaireId, //  Peut être null
            HttpServletRequest request) {

        ResponseEntity<ApiResponse<EtudeVolontaireDTO>> response = handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateVolontaire(id, nouveauVolontaireId);
                },
                "Volontaire mis à jour avec succès");
        if (response.getStatusCode().is2xxSuccessful()) {
            String utilisateur = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.log(utilisateur, AuditLog.Action.UPDATE, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + idGroupe + "-" + idVolontaire, evDetails(idEtude, idGroupe, idVolontaire, "volontaire update"), request.getRemoteAddr());
        }
        return response;
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
            @RequestParam String nouveauStatut,
            HttpServletRequest request) {

        ResponseEntity<ApiResponse<EtudeVolontaireDTO>> response = handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateStatut(id, nouveauStatut);
                },
                "Statut mis à jour avec succès");
        if (response.getStatusCode().is2xxSuccessful()) {
            String utilisateur = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.log(utilisateur, AuditLog.Action.UPDATE, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + idGroupe + "-" + idVolontaire, evDetails(idEtude, idGroupe, idVolontaire, "statut update"), request.getRemoteAddr());
        }
        return response;
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
            @RequestParam int nouveauNumSujet,
            HttpServletRequest request) {

        ResponseEntity<ApiResponse<EtudeVolontaireDTO>> response = handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateNumSujet(id, nouveauNumSujet);
                },
                "Numéro de sujet mis à jour avec succès");
        if (response.getStatusCode().is2xxSuccessful()) {
            String utilisateur = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.log(utilisateur, AuditLog.Action.UPDATE, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + idGroupe + "-" + idVolontaire, evDetails(idEtude, idGroupe, idVolontaire, "numsujet update"), request.getRemoteAddr());
        }
        return response;
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
            @RequestParam int nouvelIV,
            HttpServletRequest request) {

        ResponseEntity<ApiResponse<EtudeVolontaireDTO>> response = handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updateIV(id, nouvelIV);
                },
                "Indemnité mise à jour avec succès");
        if (response.getStatusCode().is2xxSuccessful()) {
            String utilisateur = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.log(utilisateur, AuditLog.Action.UPDATE, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + idGroupe + "-" + idVolontaire, evDetails(idEtude, idGroupe, idVolontaire, "iv update"), request.getRemoteAddr());
        }
        return response;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteEtudeVolontaire(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut,
            HttpServletRequest request) {
        try {
            EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye, statut);
            etudeVolontaireService.deleteEtudeVolontaire(id);
            String utilisateur = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.log(utilisateur, AuditLog.Action.UNASSIGN, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + idGroupe + "-" + idVolontaire, evDetails(idEtude, idGroupe, idVolontaire, null), request.getRemoteAddr());
            return ResponseEntity.ok(ApiResponse.success(null, "Association supprimée avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Association non trouvée", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de supprimer l'association", e);
        }
    }

    /**
     * Supprime toutes les associations d'un volontaire dans une étude
     * (sans nécessiter la clé composite complète - plus robuste)
     */
    @DeleteMapping("/delete-by-etude-volontaire")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteByEtudeAndVolontaire(
            @RequestParam int idEtude,
            @RequestParam int idVolontaire,
            HttpServletRequest request) {
        try {
            int deleted = etudeVolontaireService.deleteByEtudeAndVolontaire(idEtude, idVolontaire);
            String utilisateur = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.log(utilisateur, AuditLog.Action.UNASSIGN, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + idVolontaire, "vol:#" + idVolontaire + " etude:" + etudeService.getEtudeById(idEtude).map(e -> e.getRef() + "(#" + idEtude + ")").orElse("#" + idEtude), request.getRemoteAddr());
            Map<String, Object> result = Map.of(
                    "idEtude", idEtude,
                    "idVolontaire", idVolontaire,
                    "deleted", deleted);
            return ResponseEntity.ok(ApiResponse.success(result, deleted + " association(s) supprimée(s)"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Paramètres invalides", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException("Impossible de supprimer l'association", e);
        }
    }

    @PatchMapping("/update-paye")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updatePaye(
            @RequestParam int idEtude,
            @RequestParam int idGroupe,
            @RequestParam int idVolontaire,
            @RequestParam int iv,
            @RequestParam int numsujet,
            @RequestParam int paye,
            @RequestParam String statut,
            @RequestParam int nouveauPaye,
            HttpServletRequest request) {

        ResponseEntity<ApiResponse<EtudeVolontaireDTO>> response = handleUpdateOperation(
                () -> {
                    EtudeVolontaireId id = new EtudeVolontaireId(idEtude, idGroupe, idVolontaire, iv, numsujet, paye,
                            statut);
                    return etudeVolontaireService.updatePaye(id, nouveauPaye);
                },
                "Statut de paiement mis à jour avec succès");
        if (response.getStatusCode().is2xxSuccessful()) {
            String utilisateur = SecurityContextHolder.getContext().getAuthentication().getName();
            auditLogService.log(utilisateur, AuditLog.Action.PAYE, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + idGroupe + "-" + idVolontaire, evDetails(idEtude, idGroupe, idVolontaire, null), request.getRemoteAddr());
        }
        return response;
    }

    /**
     * Contrat simplifié utilisé par l'interface de paiements. La clé composite
     * courante est résolue côté serveur afin d'éviter les fragments obsolètes.
     */
    @PatchMapping("/update-paiement")
    public ResponseEntity<ApiResponse<EtudeVolontaireDTO>> updatePaiement(
            @RequestParam(required = false) Long id,
            @RequestParam int idEtude,
            @RequestParam int idVolontaire,
            @RequestParam int nouveauStatutPaiement,
            HttpServletRequest request) {
        if (nouveauStatutPaiement != 0 && nouveauStatutPaiement != 1) {
            throw new IllegalArgumentException("Le statut de paiement doit être 0 ou 1");
        }
        if (id != null) {
            EtudeVolontaireDTO updated = etudeVolontaireService.updatePaye(id, nouveauStatutPaiement);
            return ResponseEntity.ok(ApiResponse.success(updated, "Paiement mis à jour avec succès"));
        }

        List<EtudeVolontaireDTO> matches = etudeVolontaireService
                .getEtudeVolontairesByEtude(idEtude)
                .stream()
                .filter(item -> item.getIdVolontaire() == idVolontaire)
                .toList();
        if (matches.isEmpty()) {
            throw new EntityNotFoundException("Association étude-volontaire non trouvée");
        }
        if (matches.size() > 1) {
            throw new com.example.cosmetest.exception.AmbiguousEtudeVolontaireException(
                    "La route historique étude/volontaire correspond à " + matches.size() + " lignes; fournir id");
        }

        EtudeVolontaireDTO current = matches.get(0);
        EtudeVolontaireId legacyId = new EtudeVolontaireId(
                current.getIdEtude(), current.getIdGroupe(), current.getIdVolontaire(),
                current.getIv(), current.getNumsujet(), current.getPaye(), current.getStatut());
        EtudeVolontaireDTO updated = etudeVolontaireService.updatePaye(legacyId, nouveauStatutPaiement);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            auditLogService.log(authentication.getName(), AuditLog.Action.PAYE, "ETUDE_VOLONTAIRE",
                    idEtude + "-" + current.getIdGroupe() + "-" + idVolontaire,
                    evDetails(idEtude, current.getIdGroupe(), idVolontaire, "paiement update"),
                    request.getRemoteAddr());
        }
        return ResponseEntity.ok(ApiResponse.success(updated, "Paiement mis à jour avec succès"));
    }

    // Ajoutez aussi cet endpoint si vous l'utilisez dans votre React :
    @GetMapping("/paiements")
    public ResponseEntity<ApiResponse<Page<EtudeVolontaireDTO>>> getAllPaiements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = boundedPageable(page, size);
        Page<EtudeVolontaireDTO> etudeVolontairesPage = etudeVolontaireService
                .getAllEtudeVolontairesPaginated(pageable);
        return ResponseEntity.ok(ApiResponse.success(
                etudeVolontairesPage,
                "Données de paiements récupérées avec succès"));
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

    private Pageable boundedPageable(int page, int size) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(MAX_PAGE_SIZE, Math.max(1, size > 0 ? size : DEFAULT_PAGE_SIZE));
        return PageRequest.of(safePage, safeSize);
    }

    /**
     * Méthode générique pour gérer les appels de service avec gestion d'erreurs
     * uniforme
     */
    private <T> ResponseEntity<ApiResponse<T>> handleServiceCall(ServiceCall<T> serviceCall, String successMessage) {
        T result = serviceCall.call();
        return ResponseEntity.ok(ApiResponse.success(result, successMessage));
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
            throw new RuntimeException("Impossible de mettre à jour l'association", e);
        }
    }

    // ===============================
    // ENDPOINT DE RÉPARATION
    // ===============================

    /**
     * Répare les associations EtudeVolontaire manquantes pour une étude donnée.
     * Parcourt les RDV de l'étude, détecte les volontaires assignés qui n'ont pas
     * d'association EtudeVolontaire, et recrée les associations manquantes.
     */
    @PostMapping("/repair/{idEtude}")
    public ResponseEntity<Map<String, Object>> repairAssociations(@PathVariable int idEtude) {
        EtudeVolontaireRepairService.RepairResult result = repairService.repair(idEtude);
        return ResponseEntity.ok(Map.of(
                "etudeId", result.etudeId(),
                "volontairesInRdvs", result.volontairesInRdvs(),
                "existingAssociations", result.existingAssociations(),
                "missing", result.missing(),
                "repaired", result.repaired()));
    }

    /**
     * Interface fonctionnelle pour les appels de service
     */
    @FunctionalInterface
    private interface ServiceCall<T> {
        T call();
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