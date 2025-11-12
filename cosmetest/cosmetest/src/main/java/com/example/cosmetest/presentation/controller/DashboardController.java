// src/main/java/com/example/cosmetest/presentation/controller/DashboardController.java
package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.*;
import com.example.cosmetest.business.service.DashboardService;
import com.example.cosmetest.business.service.EtudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private EtudeService etudeService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/stats-jour")
    public ResponseEntity<DailyStatsDTO> getDailyStats() {
        return ResponseEntity.ok(dashboardService.getDailyStats());
    }

    @GetMapping("/etude/recentes")
    public ResponseEntity<List<EtudeDTO>> getRecentEtudes() {
        return ResponseEntity.ok(dashboardService.getRecentEtudes(5));
    }

    @GetMapping("/activite/recente")
    public ResponseEntity<List<ActiviteRecenteDTO>> getRecentActivity() {
        return ResponseEntity.ok(dashboardService.getRecentActivities(10));
    }

    @GetMapping("/rdv/prochains")
    public ResponseEntity<List<RdvDTO>> getProchainRdvs() {
        // Récupérer les rendez-vous à venir
        List<RdvDTO> rdvs = dashboardService.getUpcomingRdvs(5);

        // Enrichir chaque RdvDTO avec la référence de l'étude correspondante
        for (RdvDTO rdv : rdvs) {
            if (rdv.getIdEtude() != null) {
                // Obtenir la référence de l'étude et l'assigner au DTO
                String etudeRef = etudeService.getEtudeRefById(rdv.getIdEtude());
                rdv.setEtudeRef(etudeRef);
            }
        }

        return ResponseEntity.ok(rdvs);
    }
}