package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.presentation.request.VolontaireQuestionnaireRequest;
import com.example.cosmetest.presentation.response.VolontaireQuestionnaireResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VolontaireQuestionnaireService {

    private final VolontaireService volontaireService;
    private final VolontaireHcService habitudesService;

    public VolontaireQuestionnaireService(
            VolontaireService volontaireService,
            VolontaireHcService habitudesService) {
        this.volontaireService = volontaireService;
        this.habitudesService = habitudesService;
    }

    @Transactional
    public VolontaireQuestionnaireResponse save(
            Integer volontaireId,
            VolontaireQuestionnaireRequest request) {
        if (volontaireId == null || volontaireId <= 0) {
            throw new IllegalArgumentException("L'identifiant du volontaire doit être positif");
        }

        VolontaireDetailDTO details = request.details();
        VolontaireHcDTO habitudes = request.habitudesCosmetiques();
        details.setIdVol(volontaireId);
        habitudes.setIdVol(volontaireId);

        VolontaireDetailDTO savedDetails = volontaireService
                .updateVolontaireDetail(volontaireId, details)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Volontaire non trouvé avec l'ID: " + volontaireId));
        VolontaireHcDTO savedHabitudes = habitudesService.saveVolontaireHc(habitudes);

        return new VolontaireQuestionnaireResponse(savedDetails, savedHabitudes);
    }
}