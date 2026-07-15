package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.mapper.RdvMapper;
import com.example.cosmetest.business.service.impl.RdvIdAllocator;
import com.example.cosmetest.business.service.impl.RdvServiceImpl;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.EtudeRepository;
import com.example.cosmetest.data.repository.GroupeRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RdvBatchValidationTest {

    @Test
    void rejectsMixedStudyBatchBeforeAnyReadOrWrite() {
        RdvRepository rdvRepository = mock(RdvRepository.class);
        EtudeRepository etudeRepository = mock(EtudeRepository.class);
        RdvServiceImpl service = new RdvServiceImpl(
                rdvRepository,
                mock(RdvMapper.class),
                etudeRepository,
                mock(AnnulationRepository.class),
                mock(EtudeVolontaireService.class),
                mock(GroupeRepository.class),
                mock(RdvIdAllocator.class));
        RdvDTO first = new RdvDTO();
        first.setIdEtude(10);
        RdvDTO second = new RdvDTO();
        second.setIdEtude(20);

        assertThatThrownBy(() -> service.createRdvsBatch(List.of(first, second)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("même étude");

        verifyNoInteractions(etudeRepository, rdvRepository);
    }
}
