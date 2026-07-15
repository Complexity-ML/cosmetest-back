package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.mapper.VolontaireHcMapper;
import com.example.cosmetest.business.service.impl.VolontaireHcServiceImpl;
import com.example.cosmetest.data.repository.VolontaireHcRepository;
import com.example.cosmetest.domain.model.VolontaireHc;
import com.example.cosmetest.exception.AmbiguousVolontaireHcException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VolontaireHcServiceAmbiguityTest {

    private final VolontaireHcRepository repository = mock(VolontaireHcRepository.class);
    private final VolontaireHcMapper mapper = mock(VolontaireHcMapper.class);
    private final VolontaireHcServiceImpl service = new VolontaireHcServiceImpl(repository, mapper);

    @Test
    void readRejectsAmbiguousRows() {
        when(repository.findByIdVolIn(List.of(42)))
                .thenReturn(List.of(new VolontaireHc(), new VolontaireHc()));

        assertThatThrownBy(() -> service.getVolontaireHcByIdVol(42))
                .isInstanceOf(AmbiguousVolontaireHcException.class);

        verify(mapper, never()).toDTO(any());
    }

    @Test
    void saveRejectsAmbiguousRowsWithoutWriting() {
        VolontaireHcDTO request = new VolontaireHcDTO();
        request.setIdVol(42);
        when(repository.findByIdVolIn(List.of(42)))
                .thenReturn(List.of(new VolontaireHc(), new VolontaireHc()));

        assertThatThrownBy(() -> service.saveVolontaireHc(request))
                .isInstanceOf(AmbiguousVolontaireHcException.class);

        verify(repository, never()).delete(any());
        verify(repository, never()).save(any());
    }

    @Test
    void readDoesNotInventNegativeAnswersForMissingValues() {
        VolontaireHc entity = new VolontaireHc();
        VolontaireHcDTO dto = new VolontaireHcDTO();
        dto.setIdVol(42);
        when(repository.findByIdVolIn(List.of(42))).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        VolontaireHcDTO result = service.getVolontaireHcByIdVol(42).orElseThrow();

        assertThat(result.getMasqueVisage()).isNull();
    }
}