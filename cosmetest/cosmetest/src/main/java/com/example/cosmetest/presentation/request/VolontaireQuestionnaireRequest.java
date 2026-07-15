package com.example.cosmetest.presentation.request;

import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.dto.VolontaireHcDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record VolontaireQuestionnaireRequest(
        @NotNull @Valid VolontaireDetailDTO details,
        @NotNull @Valid VolontaireHcDTO habitudesCosmetiques) {
}