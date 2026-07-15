package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.mapper.AnnulationMapper;
import com.example.cosmetest.business.service.impl.AnnulationServiceImpl;
import com.example.cosmetest.business.service.impl.RdvIdAllocator;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Rdv;
import com.example.cosmetest.exception.AmbiguousRdvTraceException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AnnulationRdvTraceAmbiguityTest {

    @Test
    void requiresRdvIdWhenSeveralAppointmentsCanBeTraced() {
        AnnulationRepository annulationRepository = mock(AnnulationRepository.class);
        RdvRepository rdvRepository = mock(RdvRepository.class);
        AnnulationServiceImpl service = new AnnulationServiceImpl(
                annulationRepository,
                mock(AnnulationMapper.class),
                rdvRepository,
                mock(RdvIdAllocator.class));
        AnnulationDTO request = new AnnulationDTO();
        request.setIdVol(42);
        request.setIdEtude(10);
        request.setDateAnnulation("2026-07-14");
        Rdv first = new Rdv();
        first.setIdRdv(1);
        Rdv second = new Rdv();
        second.setIdRdv(2);
        when(rdvRepository.findByIdVolontaireAndIdEtude(42, 10))
                .thenReturn(List.of(first, second));

        assertThatThrownBy(() -> service.saveAnnulation(request))
                .isInstanceOf(AmbiguousRdvTraceException.class);

        verify(annulationRepository, never()).save(any());
        verify(rdvRepository, never()).save(any());
    }
}
