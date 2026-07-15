package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.service.VolontaireHcService;
import com.example.cosmetest.exception.AmbiguousVolontaireHcException;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VolontaireHcAmbiguityTest {

    private final VolontaireHcService service = mock(VolontaireHcService.class);
    private final VolontaireHcController controller = new VolontaireHcController(service);

    @Test
    void readRejectsDuplicateRowsInsteadOfSelectingFirst() {
        when(service.getVolontaireHcByIdVol(42))
                .thenThrow(new AmbiguousVolontaireHcException(42, 2));

        assertThatThrownBy(() -> controller.getVolontaireHcByIdVol(42))
                .isInstanceOf(AmbiguousVolontaireHcException.class);
    }

    @Test
    void saveRejectsDuplicateRowsWithoutDeletingOrOverwritingThem() {
        VolontaireHcDTO request = new VolontaireHcDTO();
        request.setIdVol(42);
        when(service.saveVolontaireHc(request))
                .thenThrow(new AmbiguousVolontaireHcException(42, 2));

        assertThatThrownBy(() -> controller.saveVolontaireHc(request))
                .isInstanceOf(AmbiguousVolontaireHcException.class);

        verify(service, never()).deleteVolontaireHc(anyInt());
    }

    @Test
    void savePreservesDocumentedFrequencyValues() {
        VolontaireHcDTO request = new VolontaireHcDTO();
        request.setIdVol(42);
        request.setMasqueVisage("occasionnellement");
        when(service.saveVolontaireHc(request)).thenReturn(request);

        controller.saveVolontaireHc(request);

        assertThat(request.getMasqueVisage()).isEqualTo("occasionnellement");
        verify(service).saveVolontaireHc(request);
    }
}