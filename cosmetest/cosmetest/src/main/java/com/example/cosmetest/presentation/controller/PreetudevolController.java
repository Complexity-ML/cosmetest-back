package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.business.service.PreetudevolService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/preetudevols")
public class PreetudevolController {
    private final PreetudevolService service;

    public PreetudevolController(PreetudevolService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<PreetudevolDTO>> getAllPreetudevols() { return ResponseEntity.ok(service.getAllPreetudevols()); }

    @GetMapping("/{id}")
    public ResponseEntity<PreetudevolDTO> getPreetudevolByTechnicalId(@PathVariable Long id) {
        return service.getPreetudevolById(id).map(ResponseEntity::ok).orElseThrow(this::notFound);
    }

    /** Route historique par triplet métier. */
    @GetMapping("/id")
    public ResponseEntity<PreetudevolDTO> getPreetudevolById(@RequestParam int idEtude, @RequestParam int idGroupe, @RequestParam int idVolontaire) {
        return service.getPreetudevolById(idEtude, idGroupe, idVolontaire).map(ResponseEntity::ok).orElseThrow(this::notFound);
    }

    @GetMapping("/etude/{idEtude}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByIdEtude(@PathVariable int idEtude) { return ResponseEntity.ok(service.getPreetudevolsByIdEtude(idEtude)); }
    @GetMapping("/groupe/{idGroupe}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByIdGroupe(@PathVariable int idGroupe) { return ResponseEntity.ok(service.getPreetudevolsByIdGroupe(idGroupe)); }
    @GetMapping("/volontaire/{idVolontaire}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByIdVolontaire(@PathVariable int idVolontaire) { return ResponseEntity.ok(service.getPreetudevolsByIdVolontaire(idVolontaire)); }
    @GetMapping("/etude/{idEtude}/groupe/{idGroupe}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByEtudeAndGroupe(@PathVariable int idEtude, @PathVariable int idGroupe) {
        return ResponseEntity.ok(service.getPreetudevolsByEtudeAndGroupe(idEtude, idGroupe));
    }
    @GetMapping("/etude/{idEtude}/volontaire/{idVolontaire}")
    public ResponseEntity<List<PreetudevolDTO>> getPreetudevolsByEtudeAndVolontaire(@PathVariable int idEtude, @PathVariable int idVolontaire) {
        return ResponseEntity.ok(service.getPreetudevolsByEtudeAndVolontaire(idEtude, idVolontaire));
    }

    @PostMapping
    public ResponseEntity<PreetudevolDTO> createPreetudevol(@Valid @RequestBody PreetudevolDTO dto) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(service.createPreetudevol(dto)); }
        catch (IllegalArgumentException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreetudevolDTO> updatePreetudevolByTechnicalId(@PathVariable Long id, @Valid @RequestBody PreetudevolDTO dto) {
        try { return service.updatePreetudevol(id, dto).map(ResponseEntity::ok).orElseThrow(this::notFound); }
        catch (IllegalArgumentException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); }
    }

    /** Route historique par triplet métier. */
    @PutMapping("/{idEtude}/{idGroupe}/{idVolontaire}")
    public ResponseEntity<PreetudevolDTO> updatePreetudevol(@PathVariable int idEtude, @PathVariable int idGroupe,
                                                             @PathVariable int idVolontaire, @Valid @RequestBody PreetudevolDTO dto) {
        try { return service.updatePreetudevol(idEtude, idGroupe, idVolontaire, dto).map(ResponseEntity::ok).orElseThrow(this::notFound); }
        catch (IllegalArgumentException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreetudevolByTechnicalId(@PathVariable Long id) {
        if (!service.deletePreetudevol(id)) throw notFound();
        return ResponseEntity.noContent().build();
    }

    /** Route historique par triplet métier. */
    @DeleteMapping("/{idEtude}/{idGroupe}/{idVolontaire}")
    public ResponseEntity<Void> deletePreetudevol(@PathVariable int idEtude, @PathVariable int idGroupe, @PathVariable int idVolontaire) {
        if (!service.deletePreetudevol(idEtude, idGroupe, idVolontaire)) throw notFound();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/etude/{idEtude}")
    public ResponseEntity<Integer> deletePreetudevolsByIdEtude(@PathVariable int idEtude) { return ResponseEntity.ok(service.deletePreetudevolsByIdEtude(idEtude)); }
    @DeleteMapping("/groupe/{idGroupe}")
    public ResponseEntity<Integer> deletePreetudevolsByIdGroupe(@PathVariable int idGroupe) { return ResponseEntity.ok(service.deletePreetudevolsByIdGroupe(idGroupe)); }
    @DeleteMapping("/volontaire/{idVolontaire}")
    public ResponseEntity<Integer> deletePreetudevolsByIdVolontaire(@PathVariable int idVolontaire) { return ResponseEntity.ok(service.deletePreetudevolsByIdVolontaire(idVolontaire)); }

    private ResponseStatusException notFound() { return new ResponseStatusException(HttpStatus.NOT_FOUND, "Pré-étude-volontaire non trouvée"); }
}
