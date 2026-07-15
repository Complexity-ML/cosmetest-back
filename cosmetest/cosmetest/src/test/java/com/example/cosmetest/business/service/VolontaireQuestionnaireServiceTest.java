package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.presentation.request.VolontaireQuestionnaireRequest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class VolontaireQuestionnaireServiceTest {

    private final VolontaireService volontaireService = mock(VolontaireService.class);
    private final VolontaireHcService habitudesService = mock(VolontaireHcService.class);
    private final VolontaireQuestionnaireService service =
            new VolontaireQuestionnaireService(volontaireService, habitudesService);

    @Test
    void savesDetailsAndHabitsForThePathVolunteerId() {
        VolontaireDetailDTO details = new VolontaireDetailDTO();
        details.setIdVol(999);
        VolontaireHcDTO habitudes = new VolontaireHcDTO();
        habitudes.setIdVol(999);
        VolontaireQuestionnaireRequest request = new VolontaireQuestionnaireRequest(details, habitudes);

        VolontaireDetailDTO savedDetails = new VolontaireDetailDTO();
        savedDetails.setIdVol(42);
        VolontaireHcDTO savedHabitudes = new VolontaireHcDTO();
        savedHabitudes.setIdVol(42);
        when(volontaireService.updateVolontaireDetail(42, details)).thenReturn(Optional.of(savedDetails));
        when(habitudesService.saveVolontaireHc(habitudes)).thenReturn(savedHabitudes);

        var result = service.save(42, request);

        assertThat(details.getIdVol()).isEqualTo(42);
        assertThat(habitudes.getIdVol()).isEqualTo(42);
        assertThat(result.details()).isSameAs(savedDetails);
        assertThat(result.habitudesCosmetiques()).isSameAs(savedHabitudes);
    }

    @Test
    void propagatesHabitFailureInsteadOfReturningPartialSuccess() {
        VolontaireDetailDTO details = new VolontaireDetailDTO();
        VolontaireHcDTO habitudes = new VolontaireHcDTO();
        when(volontaireService.updateVolontaireDetail(42, details)).thenReturn(Optional.of(details));
        when(habitudesService.saveVolontaireHc(habitudes)).thenThrow(new IllegalStateException("échec habitudes"));

        assertThatThrownBy(() -> service.save(42, new VolontaireQuestionnaireRequest(details, habitudes)))
                .isInstanceOf(IllegalStateException.class);

        verify(volontaireService).updateVolontaireDetail(42, details);
        verify(habitudesService).saveVolontaireHc(habitudes);
    }
}