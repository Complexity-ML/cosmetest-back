package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.business.mapper.InfobancaireMapper;
import com.example.cosmetest.data.repository.InfobancaireRepository;
import com.example.cosmetest.domain.model.Infobancaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfobancaireServiceImplTest {
    @Mock InfobancaireRepository repository;
    private InfobancaireServiceImpl service;

    @BeforeEach
    void setUp() { service = new InfobancaireServiceImpl(repository, new InfobancaireMapper()); }

    @Test
    void createIgnoresClientIdAndReturnsGeneratedId() {
        InfobancaireDTO request = new InfobancaireDTO(999L, "BNPAFRPP", "FR7630004000031234567890143", 100);
        when(repository.existsByBicAndIbanAndIdVol(request.getBic(), request.getIban(), request.getIdVol())).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> {
            Infobancaire entity = invocation.getArgument(0);
            assertThat(entity.getIdInfobancaire()).isNull();
            entity.setIdInfobancaire(42L);
            return entity;
        });

        InfobancaireDTO result = service.createInfobancaire(request);

        assertThat(result.getIdInfobancaire()).isEqualTo(42L);
    }

    @Test
    void updateByTechnicalIdMutatesInPlaceAndPreservesId() {
        Infobancaire entity = entity(42L, "BNPAFRPP", "FR7630004000031234567890143", 100);
        InfobancaireDTO request = new InfobancaireDTO(999L, "CEPAFRPP", "FR7630006000011234567890189", 101);
        when(repository.findById(42L)).thenReturn(Optional.of(entity));
        when(repository.findByBicAndIbanAndIdVol(request.getBic(), request.getIban(), request.getIdVol())).thenReturn(Optional.empty());
        when(repository.save(entity)).thenReturn(entity);

        InfobancaireDTO result = service.updateInfobancaire(42L, request).orElseThrow();

        assertThat(result.getIdInfobancaire()).isEqualTo(42L);
        assertThat(entity.getBic()).isEqualTo("CEPAFRPP");
        verify(repository, never()).deleteById(any());
    }

    @Test
    void legacyLookupUsesBusinessTriplet() {
        Infobancaire entity = entity(42L, "BNPAFRPP", "FR7630004000031234567890143", 100);
        when(repository.findByBicAndIbanAndIdVol(entity.getBic(), entity.getIban(), entity.getIdVol())).thenReturn(Optional.of(entity));

        assertThat(service.getInfobancaireById(entity.getBic(), entity.getIban(), entity.getIdVol()))
                .get().extracting(InfobancaireDTO::getIdInfobancaire).isEqualTo(42L);
    }

    private Infobancaire entity(Long id, String bic, String iban, Integer idVol) {
        Infobancaire entity = new Infobancaire(bic, iban, idVol);
        entity.setIdInfobancaire(id);
        return entity;
    }
}
