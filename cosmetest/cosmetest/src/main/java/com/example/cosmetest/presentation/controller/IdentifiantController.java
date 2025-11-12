package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.IdentifiantDTO;
import com.example.cosmetest.business.service.IdentifiantService;
import com.example.cosmetest.presentation.request.ChangerMotDePasseRequest;
import com.example.cosmetest.presentation.request.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des identifiants
 */
@RestController
@RequestMapping("/api/identifiants")
public class IdentifiantController {

    private final IdentifiantService identifiantService;

    public IdentifiantController(IdentifiantService identifiantService) {
        this.identifiantService = identifiantService;
    }

    /**
     * Récupère tous les identifiants
     *
     * @return liste des identifiants (sans mots de passe)
     */
    @GetMapping
    public ResponseEntity<List<IdentifiantDTO>> getAllIdentifiants() {
        List<IdentifiantDTO> identifiants = identifiantService.getAllIdentifiants();
        return ResponseEntity.ok(identifiants);
    }

    /**
     * Récupère un identifiant par son ID
     *
     * @param id l'identifiant de l'identifiant
     * @return l'identifiant correspondant (sans mot de passe)
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdentifiantDTO> getIdentifiantById(@PathVariable Integer id) {
        return identifiantService.getIdentifiantById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifiant non trouvé avec l'ID: " + id));
    }

    /**
     * Récupère un identifiant par son login
     *
     * @param login le login de l'identifiant
     * @return l'identifiant correspondant (sans mot de passe)
     */
    @GetMapping("/by-login/{login}")
    public ResponseEntity<IdentifiantDTO> getIdentifiantByLogin(@PathVariable String login) {
        return identifiantService.getIdentifiantByLogin(login)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifiant non trouvé avec le login: " + login));
    }

    /**
     * Crée un nouvel identifiant
     *
     * @param identifiantDTO les données de l'identifiant à créer
     * @return l'identifiant créé (sans mot de passe)
     */
    @PostMapping
    public ResponseEntity<IdentifiantDTO> createIdentifiant(@Valid @RequestBody IdentifiantDTO identifiantDTO) {
        try {
            IdentifiantDTO createdIdentifiant = identifiantService.createIdentifiant(identifiantDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdIdentifiant);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour un identifiant existant
     *
     * @param id l'identifiant de l'identifiant à mettre à jour
     * @param identifiantDTO les nouvelles données de l'identifiant
     * @return l'identifiant mis à jour (sans mot de passe)
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdentifiantDTO> updateIdentifiant(@PathVariable Integer id, @Valid @RequestBody IdentifiantDTO identifiantDTO) {
        try {
            return identifiantService.updateIdentifiant(id, identifiantDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifiant non trouvé avec l'ID: " + id));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime un identifiant
     *
     * @param id l'identifiant de l'identifiant à supprimer
     * @return statut de la suppression
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdentifiant(@PathVariable Integer id) {
        if (identifiantService.deleteIdentifiant(id)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Identifiant non trouvé avec l'ID: " + id);
        }
    }

    /**
     * Change le mot de passe d'un identifiant
     *
     * @param id l'identifiant de l'identifiant
     * @param request requête contenant l'ancien et le nouveau mot de passe
     * @return statut du changement
     */
    @PostMapping("/{id}/changer-mot-de-passe")
    public ResponseEntity<Void> changerMotDePasse(@PathVariable Integer id, @Valid @RequestBody ChangerMotDePasseRequest request) {
        try {
            boolean success = identifiantService.changerMotDePasse(id, request.getAncienMotDePasse(), request.getNouveauMotDePasse());
            if (success) {
                return ResponseEntity.ok().build();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Échec du changement de mot de passe, ancien mot de passe incorrect");
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Récupère tous les identifiants d'un rôle spécifique
     *
     * @param role le rôle recherché
     * @return la liste des identifiants (sans mots de passe)
     */
    @GetMapping("/by-role/{role}")
    public ResponseEntity<List<IdentifiantDTO>> getIdentifiantsByRole(@PathVariable String role) {
        List<IdentifiantDTO> identifiants = identifiantService.getIdentifiantsByRole(role);
        return ResponseEntity.ok(identifiants);
    }

    /**
     * Vérifie si un login existe
     *
     * @param login le login à vérifier
     * @return true si le login existe, false sinon
     */
    @GetMapping("/check-login/{login}")
    public ResponseEntity<Boolean> checkLoginExists(@PathVariable String login) {
        boolean exists = identifiantService.loginExiste(login);
        return ResponseEntity.ok(exists);
    }

    /**
     * Vérifie si un email existe
     *
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = identifiantService.emailExiste(email);
        return ResponseEntity.ok(exists);
    }

    /**
     * Authentifie un utilisateur
     *
     * @param request requête contenant le login et le mot de passe
     * @return l'identifiant authentifié (sans mot de passe)
     */
    @PostMapping("/login")
    public ResponseEntity<IdentifiantDTO> login(@Valid @RequestBody LoginRequest request) {
        return identifiantService.authentifier(request.getLogin(), request.getMotDePasse())
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants incorrects"));
    }
}