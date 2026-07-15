package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.VolontaireNotificationDTO;
import com.example.cosmetest.business.service.VolontaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/volontaires/notifications", "/api/v1/volontaires/notifications"})
public class VolontaireNotificationController {

    private final VolontaireService volontaireService;

    public VolontaireNotificationController(VolontaireService volontaireService) {
        this.volontaireService = volontaireService;
    }

    @GetMapping("/today")
    public ResponseEntity<TodayNotificationsResponse> getTodayNotifications(
            @RequestParam(defaultValue = "50") int limit) {
        int boundedLimit = Math.max(1, Math.min(limit, 100));
        List<VolontaireNotificationDTO> notifications =
                volontaireService.getTodayNotifications(boundedLimit);
        return ResponseEntity.ok(new TodayNotificationsResponse(
                notifications == null ? List.of() : notifications,
                volontaireService.countVolontairesAddedToday(),
                LocalDate.now().toString()));
    }

    public record TodayNotificationsResponse(
            List<VolontaireNotificationDTO> data,
            int total,
            String date) {
    }
}
