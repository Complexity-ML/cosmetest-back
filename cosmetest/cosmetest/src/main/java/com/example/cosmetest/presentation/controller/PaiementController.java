package com.example.cosmetest.presentation.controller;
import com.example.cosmetest.business.dto.PaiementEtudeSummaryDTO;
import com.example.cosmetest.business.dto.PaymentBatchResultDTO;
import com.example.cosmetest.business.service.PaiementStatsService;
import com.example.cosmetest.business.service.PaymentBatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour les opérations de paiements.
 */
@RestController
@RequestMapping("/api/paiements")
@CrossOrigin(origins = "*")
public class PaiementController {

    private final PaymentBatchService paymentBatchService;
    private final PaiementStatsService paiementStatsService;

    public PaiementController(PaymentBatchService paymentBatchService,
                              PaiementStatsService paiementStatsService) {
        this.paymentBatchService = paymentBatchService;
        this.paiementStatsService = paiementStatsService;
    }

    /**
     * Retourne un résumé agrégé des paiements pour chaque étude.
     */
    @GetMapping("/etudes/summary")
    public ResponseEntity<List<PaiementEtudeSummaryDTO>> getEtudeSummaries() {
        return ResponseEntity.ok(paiementStatsService.getAllEtudeSummaries());
    }

    /**
     * Retourne un résumé agrégé des paiements pour une étude donnée.
     */
    @GetMapping("/etudes/{idEtude}/summary")
    public ResponseEntity<PaiementEtudeSummaryDTO> getEtudeSummary(@PathVariable int idEtude) {
        PaiementEtudeSummaryDTO summary = paiementStatsService.getSummaryForEtude(idEtude);
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(summary);
    }

    /**
     * Marque comme payés tous les paiements non payés d'une étude, en excluant les volontaires annulés.
     */
    @PostMapping("/etudes/{idEtude}/mark-all-paid")
    public ResponseEntity<?> markAllAsPaid(@PathVariable int idEtude) {
        try {
            PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                java.util.Map.of(
                    "success", false,
                    "message", e.getMessage()
                )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                java.util.Map.of(
                    "success", false,
                    "message", "Erreur lors de l'opération de paiement en masse",
                    "error", e.getMessage()
                )
            );
        }
    }
}
