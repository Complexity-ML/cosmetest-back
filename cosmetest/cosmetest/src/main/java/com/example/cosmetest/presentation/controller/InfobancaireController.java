package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.business.service.InfobancaireService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/infobancaires")
public class InfobancaireController {
    private static final String NOT_FOUND = "Information bancaire non trouvée";
    private final InfobancaireService service;

    public InfobancaireController(InfobancaireService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<InfobancaireDTO>> getAllInfobancaires() { return ResponseEntity.ok(service.getAllInfobancaires()); }

    @GetMapping("/{id}")
    public ResponseEntity<InfobancaireDTO> getInfobancaireByTechnicalId(@PathVariable Long id) {
        return service.getInfobancaireById(id).map(ResponseEntity::ok).orElseThrow(this::notFound);
    }

    /** Route historique par triplet métier. */
    @GetMapping("/id")
    public ResponseEntity<InfobancaireDTO> getInfobancaireById(@RequestParam String bic, @RequestParam String iban, @RequestParam Integer idVol) {
        return service.getInfobancaireById(bic, iban, idVol).map(ResponseEntity::ok).orElseThrow(this::notFound);
    }

    @GetMapping("/volontaire/{idVol}")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByIdVol(@PathVariable Integer idVol) { return ResponseEntity.ok(service.getInfobancairesByIdVol(idVol)); }

    @PostMapping
    public ResponseEntity<InfobancaireDTO> createInfobancaire(@Valid @RequestBody InfobancaireDTO dto) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(service.createInfobancaire(dto)); }
        catch (IllegalArgumentException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); }
    }

    @PutMapping("/{id}")
    public ResponseEntity<InfobancaireDTO> updateInfobancaireByTechnicalId(@PathVariable Long id, @Valid @RequestBody InfobancaireDTO dto) {
        try { return service.updateInfobancaire(id, dto).map(ResponseEntity::ok).orElseThrow(this::notFound); }
        catch (IllegalArgumentException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); }
    }

    /** Route historique par triplet métier. */
    @PutMapping
    public ResponseEntity<InfobancaireDTO> updateInfobancaire(@RequestParam String bic, @RequestParam String iban,
                                                               @RequestParam Integer idVol, @Valid @RequestBody InfobancaireDTO dto) {
        try { return service.updateInfobancaire(bic, iban, idVol, dto).map(ResponseEntity::ok).orElseThrow(this::notFound); }
        catch (IllegalArgumentException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInfobancaireByTechnicalId(@PathVariable Long id) {
        if (!service.deleteInfobancaire(id)) throw notFound();
        return ResponseEntity.noContent().build();
    }

    /** Route historique par triplet métier. */
    @DeleteMapping
    public ResponseEntity<Void> deleteInfobancaire(@RequestParam String bic, @RequestParam String iban, @RequestParam Integer idVol) {
        if (!service.deleteInfobancaire(bic, iban, idVol)) throw notFound();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkInfobancaireExists(@RequestParam String bic, @RequestParam String iban, @RequestParam Integer idVol) {
        return ResponseEntity.ok(service.existsById(bic, iban, idVol));
    }

    @GetMapping("/by-iban/{iban}")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByIban(@PathVariable String iban) { return ResponseEntity.ok(service.getInfobancairesByIban(iban)); }
    @GetMapping("/by-bic/{bic}")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByBic(@PathVariable String bic) { return ResponseEntity.ok(service.getInfobancairesByBic(bic)); }
    @GetMapping("/by-bic-and-iban")
    public ResponseEntity<List<InfobancaireDTO>> getInfobancairesByBicAndIban(@RequestParam String bic, @RequestParam String iban) {
        return ResponseEntity.ok(service.getInfobancairesByBicAndIban(bic, iban));
    }

    private ResponseStatusException notFound() { return new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND); }
}
