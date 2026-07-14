package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.business.mapper.PreetudevolMapper;
import com.example.cosmetest.data.repository.PreetudevolRepository;
import com.example.cosmetest.domain.model.Preetudevol;
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
class PreetudevolServiceImplTest {
    @Mock PreetudevolRepository repository;
    private PreetudevolServiceImpl service;

    @BeforeEach
    void setUp() { service = new PreetudevolServiceImpl(repository, new PreetudevolMapper()); }

    @Test
    void createIgnoresClientIdAndReturnsGeneratedId() {
        PreetudevolDTO request = new PreetudevolDTO(999L, 1, 2, 3);
        when(repository.existsByIdEtudeAndIdGroupeAndIdVolontaire(1, 2, 3)).thenReturn(false);
        when(repository.save(any())).thenAnswer(invocation -> {
            Preetudevol entity = invocation.getArgument(0);
            assertThat(entity.getIdPreetudevol()).isNull();
            entity.setIdPreetudevol(42L);
            return entity;
        });

        PreetudevolDTO result = service.createPreetudevol(request);

        assertThat(result.getIdPreetudevol()).isEqualTo(42L);
    }

    @Test
    void updateByTechnicalIdMutatesInPlaceAndPreservesId() {
        Preetudevol entity = entity(42L, 1, 2, 3);
        PreetudevolDTO request = new PreetudevolDTO(999L, 4, 5, 6);
        when(repository.findById(42L)).thenReturn(Optional.of(entity));
        when(repository.findByIdEtudeAndIdGroupeAndIdVolontaire(4, 5, 6)).thenReturn(Optional.empty());
        when(repository.save(entity)).thenReturn(entity);

        PreetudevolDTO result = service.updatePreetudevol(42L, request).orElseThrow();

        assertThat(result.getIdPreetudevol()).isEqualTo(42L);
        assertThat(entity.getIdEtude()).isEqualTo(4);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void legacyLookupUsesBusinessTriplet() {
        Preetudevol entity = entity(42L, 1, 2, 3);
        when(repository.findByIdEtudeAndIdGroupeAndIdVolontaire(1, 2, 3)).thenReturn(Optional.of(entity));

        assertThat(service.getPreetudevolById(1, 2, 3))
                .get().extracting(PreetudevolDTO::getIdPreetudevol).isEqualTo(42L);
    }

    private Preetudevol entity(Long id, int idEtude, int idGroupe, int idVolontaire) {
        Preetudevol entity = new Preetudevol(idEtude, idGroupe, idVolontaire);
        entity.setIdPreetudevol(id);
        return entity;
    }
}
