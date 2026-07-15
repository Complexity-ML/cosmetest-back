package com.example.cosmetest.presentation.response;

import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.dto.VolontaireHcDTO;

public record VolontaireQuestionnaireResponse(
        VolontaireDetailDTO details,
        VolontaireHcDTO habitudesCosmetiques) {
}