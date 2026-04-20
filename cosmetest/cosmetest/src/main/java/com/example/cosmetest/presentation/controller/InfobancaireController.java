package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.business.service.InfobancaireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des informations bancaires
 */
@RestController
@RequestMapping("/api/infobancaires")
public class InfobancaireController {

    private final InfobancaireService infobancaireService;

    public InfobancaireController(InfobancaireService infobancaireService) {
        this.infobancaireService = infobancaireService;
    }

    /**
     * Récupère toutes les informations bancaires
     *
     * @return liste des informations bancaires
     */
    @GetMapping
    public ResponseEntity<List<InfobancaireDTO>> getAllInfobancaires() {
        List<InfobancaireDTO> infobancaires = infobancaireService.getAllInfobancaires();
        return ResponseEntity.ok(infobancaires);
    }

    /**
     * Récupère une information bancaire par son identifiant composite
     *
     * @param bic   le code BIC
     * @param iban  le numéro IBAN
     * @param idVol l'identifiant du volontaire
     * @return l'information bancaire correspondante
     */
    @GetMapping("/id")
    public ResponseEntity<InfobancaireDTO> getInfobancaireById(
            @RequestParam String bic,
            @RequestParam String iban,
            @RequestParam Integer idVol) {
        return infobancaireService.getInfobancaireById(bic, iban, idVol)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Information bancaire non trouvée avec BIC: " + bic + ", IBAN: " + iban + ", ID Volontaire: " + idVol));
    }

    /**
     * Récupère les informations bancaires d'un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return la liste des informations bancaires du volontaire
     */
    @GetMapping("/volontaire/{idVol}")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByIdVol(@PathVariable Integer idVol) {
        List<InfobancaireDTO> infobancaires = infobancaireService.getInfobancairesByIdVol(idVol);
        return ResponseEntity.ok(infobancaires);
    }

    /**
     * Crée une nouvelle information bancaire
     *
     * @param infobancaireDTO les données de l'information bancaire à créer
     * @return l'information bancaire créée
     */
    @PostMapping
    public ResponseEntity<InfobancaireDTO> createInfobancaire(@Valid @RequestBody InfobancaireDTO infobancaireDTO) {
        try {
            InfobancaireDTO createdInfobancaire = infobancaireService.createInfobancaire(infobancaireDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInfobancaire);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Met à jour une information bancaire existante
     *
     * @param bic             le code BIC actuel
     * @param iban            le numéro IBAN actuel
     * @param idVol           l'identifiant du volontaire actuel
     * @param infobancaireDTO les nouvelles données de l'information bancaire
     * @return l'information bancaire mise à jour
     */
    @PutMapping
    public ResponseEntity<InfobancaireDTO> updateInfobancaire(
            @RequestParam String bic,
            @RequestParam String iban,
            @RequestParam Integer idVol,
            @Valid @RequestBody InfobancaireDTO infobancaireDTO) {
        try {
            return infobancaireService.updateInfobancaire(bic, iban, idVol, infobancaireDTO)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Information bancaire non trouvée avec BIC: " + bic + ", IBAN: " + iban + ", ID Volontaire: " + idVol));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Supprime une information bancaire
     *
     * @param bic   le code BIC
     * @param iban  le numéro IBAN
     * @param idVol l'identifiant du volontaire
     * @return statut de la suppression
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteInfobancaire(
            @RequestParam String bic,
            @RequestParam String iban,
            @RequestParam Integer idVol) {
        if (infobancaireService.deleteInfobancaire(bic, iban, idVol)) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Information bancaire non trouvée avec BIC: " + bic + ", IBAN: " + iban + ", ID Volontaire: " + idVol);
        }
    }

    /**
     * Vérifie si une information bancaire existe
     *
     * @param bic   le code BIC
     * @param iban  le numéro IBAN
     * @param idVol l'identifiant du volontaire
     * @return true si l'information existe, false sinon
     */
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkInfobancaireExists(
            @RequestParam String bic,
            @RequestParam String iban,
            @RequestParam Integer idVol) {
        boolean exists = infobancaireService.existsById(bic, iban, idVol);
        return ResponseEntity.ok(exists);
    }

    /**
     * Récupère les informations bancaires par IBAN
     *
     * @param iban le numéro IBAN
     * @return la liste des informations bancaires correspondantes
     */
    @GetMapping("/by-iban/{iban}")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByIban(@PathVariable String iban) {
        List<InfobancaireDTO> infobancaires = infobancaireService.getInfobancairesByIban(iban);
        return ResponseEntity.ok(infobancaires);
    }

    /**
     * Récupère les informations bancaires par BIC
     *
     * @param bic le code BIC
     * @return la liste des informations bancaires correspondantes
     */
    @GetMapping("/by-bic/{bic}")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByBic(@PathVariable String bic) {
        List<InfobancaireDTO> infobancaires = infobancaireService.getInfobancairesByBic(bic);
        return ResponseEntity.ok(infobancaires);
    }

    /**
     * Récupère les informations bancaires par BIC et IBAN
     *
     * @param bic  le code BIC
     * @param iban le numéro IBAN
     * @return la liste des informations bancaires correspondantes
     */
    @GetMapping("/by-bic-and-iban")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByBicAndIban(
            @RequestParam String bic,
            @RequestParam String iban) {
        List<InfobancaireDTO> infobancaires = infobancaireService.getInfobancairesByBicAndIban(bic, iban);
        return ResponseEntity.ok(infobancaires);
    }
}