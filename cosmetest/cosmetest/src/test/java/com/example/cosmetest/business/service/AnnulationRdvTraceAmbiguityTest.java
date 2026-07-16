package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.mapper.AnnulationMapper;
import com.example.cosmetest.business.service.impl.AnnulationServiceImpl;
import com.example.cosmetest.business.service.impl.RdvIdAllocator;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Annulation;
import com.example.cosmetest.domain.model.Rdv;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnnulationRdvTraceAmbiguityTest {

    @Test
    void plusieursRendezVousFormentUneAnnulationEtudeSansTraceRdvAmbigue() {
        AnnulationRepository annulationRepository = mock(AnnulationRepository.class);
        RdvRepository rdvRepository = mock(RdvRepository.class);
        AnnulationMapper mapper = mock(AnnulationMapper.class);
        RdvIdAllocator allocator = mock(RdvIdAllocator.class);
        AnnulationServiceImpl service = new AnnulationServiceImpl(
                annulationRepository, mapper, rdvRepository, allocator);

        AnnulationDTO request = new AnnulationDTO();
        request.setIdVol(42);
        request.setIdEtude(10);
        request.setDateAnnulation("2026-07-14");
        Rdv first = rdv(1);
        Rdv second = rdv(2);
        Annulation entity = new Annulation();
        AnnulationDTO response = new AnnulationDTO();

        when(rdvRepository.findByIdVolontaireAndIdEtude(42, 10)).thenReturn(List.of(first, second));
        when(mapper.toEntity(request)).thenReturn(entity);
        when(annulationRepository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);
        when(allocator.nextForStudy(10)).thenReturn(3, 4);
        when(rdvRepository.save(any(Rdv.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.saveAnnulation(request);

        assertThat(request.getIdRdv()).isNull();
        verify(annulationRepository).save(entity);
        verify(rdvRepository, times(2)).save(any(Rdv.class));
        verify(rdvRepository).deleteAll(List.of(first, second));
    }

    private Rdv rdv(int idRdv) {
        Rdv rdv = new Rdv();
        rdv.setIdEtude(10);
        rdv.setIdRdv(idRdv);
        rdv.setIdVolontaire(42);
        rdv.setEtat("PLANIFIE");
        return rdv;
    }
}
