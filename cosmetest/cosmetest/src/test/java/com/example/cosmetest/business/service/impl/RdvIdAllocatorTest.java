package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Rdv;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("RdvIdAllocator")
class RdvIdAllocatorTest {

    @Test
    @DisplayName("alloue 1 lorsque l'étude ne contient aucun rendez-vous")
    void allocatesFirstIdentifier() {
        RdvRepository repository = mock(RdvRepository.class);
        when(repository.findLastRdvForEtudeForUpdate(42)).thenReturn(Optional.empty());
        when(repository.existsByIdEtudeAndIdRdv(42, 1)).thenReturn(false);

        RdvIdAllocator allocator = new RdvIdAllocator(repository);

        assertThat(allocator.nextForStudy(42)).isEqualTo(1);
        verify(repository).findLastRdvForEtudeForUpdate(42);
    }

    @Test
    @DisplayName("alloue l'identifiant suivant après verrouillage du dernier rendez-vous")
    void allocatesAfterLockedLastIdentifier() {
        RdvRepository repository = mock(RdvRepository.class);
        Rdv last = new Rdv();
        last.setId(100L);
        last.setIdEtude(42);
        last.setIdRdv(7);
        when(repository.findLastRdvForEtudeForUpdate(42)).thenReturn(Optional.of(last));
        when(repository.existsByIdEtudeAndIdRdv(42, 8)).thenReturn(false);

        RdvIdAllocator allocator = new RdvIdAllocator(repository);

        assertThat(allocator.nextForStudy(42)).isEqualTo(8);
    }

    @Test
    @DisplayName("saute une collision résiduelle")
    void skipsResidualCollision() {
        RdvRepository repository = mock(RdvRepository.class);
        Rdv last = new Rdv();
        last.setId(100L);
        last.setIdEtude(42);
        last.setIdRdv(7);
        when(repository.findLastRdvForEtudeForUpdate(42)).thenReturn(Optional.of(last));
        when(repository.existsByIdEtudeAndIdRdv(42, 8)).thenReturn(true);
        when(repository.existsByIdEtudeAndIdRdv(42, 9)).thenReturn(false);

        RdvIdAllocator allocator = new RdvIdAllocator(repository);

        assertThat(allocator.nextForStudy(42)).isEqualTo(9);
    }
}

