package com.example.cosmetest.business.service;

import com.example.cosmetest.business.mapper.VolontaireMapper;
import com.example.cosmetest.business.service.impl.VolontaireServiceImpl;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.data.repository.VolontaireRepository;
import com.example.cosmetest.domain.model.Volontaire;
import com.example.cosmetest.exception.AmbiguousVolontaireException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class VolontaireEmailAmbiguityTest {

    @Test
    void emailLookupRejectsDuplicatesInsteadOfSelectingFirst() {
        VolontaireRepository repository = mock(VolontaireRepository.class);
        VolontaireMapper mapper = mock(VolontaireMapper.class);
        VolontaireServiceImpl service = new VolontaireServiceImpl(
                repository,
                mapper,
                mock(RdvRepository.class),
                mock(EtudeVolontaireRepository.class),
                mock(AnnulationRepository.class),
                mock(PhotoProxyService.class));
        when(repository.findByEmailVol("duplicate@example.test"))
                .thenReturn(List.of(new Volontaire(), new Volontaire()));

        assertThatThrownBy(() -> service.findByEmail("duplicate@example.test"))
                .isInstanceOf(AmbiguousVolontaireException.class);

        verify(mapper, never()).toDTO(any());
    }
}
