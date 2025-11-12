package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolbugDTO;
import com.example.cosmetest.business.mapper.VolbugMapper;
import com.example.cosmetest.data.repository.VolbugRepository;
import com.example.cosmetest.domain.model.Volbug;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour VolbugServiceImpl - gestion des bugs liés aux volontaires
 */
@ExtendWith(MockitoExtension.class)
class VolbugServiceImplTest {

    @Mock
    private VolbugRepository volbugRepository;

    @Mock
    private VolbugMapper volbugMapper;

    @InjectMocks
    private VolbugServiceImpl volbugService;

    private Volbug volbug;
    private VolbugDTO volbugDTO;

    @BeforeEach
    void setUp() {
        // Entité - Volbug n'a qu'un seul champ: idVol
        volbug = new Volbug();
        volbug.setIdVol(1);

        // DTO
        volbugDTO = new VolbugDTO();
        volbugDTO.setIdVol(1);
    }

    // ==================== Tests getAllVolbugs ====================

    @Test
    void testGetAllVolbugs_Success() {
        // Given
        List<Volbug> volbugs = Arrays.asList(volbug);
        List<VolbugDTO> expectedDTOs = Arrays.asList(volbugDTO);

        when(volbugRepository.findAll()).thenReturn(volbugs);
        when(volbugMapper.toDTOList(volbugs)).thenReturn(expectedDTOs);

        // When
        List<VolbugDTO> result = volbugService.getAllVolbugs();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdVol()).isEqualTo(1);
        verify(volbugRepository).findAll();
        verify(volbugMapper).toDTOList(volbugs);
    }

    @Test
    void testGetAllVolbugs_EmptyList() {
        // Given
        when(volbugRepository.findAll()).thenReturn(Collections.emptyList());
        when(volbugMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // When
        List<VolbugDTO> result = volbugService.getAllVolbugs();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetAllVolbugs_MultipleVolbugs() {
        // Given
        Volbug volbug2 = new Volbug();
        volbug2.setIdVol(2);

        VolbugDTO volbugDTO2 = new VolbugDTO();
        volbugDTO2.setIdVol(2);

        List<Volbug> volbugs = Arrays.asList(volbug, volbug2);
        List<VolbugDTO> expectedDTOs = Arrays.asList(volbugDTO, volbugDTO2);

        when(volbugRepository.findAll()).thenReturn(volbugs);
        when(volbugMapper.toDTOList(volbugs)).thenReturn(expectedDTOs);

        // When
        List<VolbugDTO> result = volbugService.getAllVolbugs();

        // Then
        assertThat(result).hasSize(2);
    }

    // ==================== Tests getVolbugByIdVol ====================

    @Test
    void testGetVolbugByIdVol_Found() {
        // Given
        when(volbugRepository.findById(1)).thenReturn(Optional.of(volbug));
        when(volbugMapper.toDTO(volbug)).thenReturn(volbugDTO);

        // When
        Optional<VolbugDTO> result = volbugService.getVolbugByIdVol(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getIdVol()).isEqualTo(1);
    }

    @Test
    void testGetVolbugByIdVol_NotFound() {
        // Given
        when(volbugRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<VolbugDTO> result = volbugService.getVolbugByIdVol(999);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetVolbugByIdVol_NullId_ReturnsEmpty() {
        // When
        Optional<VolbugDTO> result = volbugService.getVolbugByIdVol(null);

        // Then
        assertThat(result).isEmpty();
        verify(volbugRepository, never()).findById(any());
    }

    // ==================== Tests createVolbug ====================

    @Test
    void testCreateVolbug_Success() {
        // Given
        when(volbugRepository.existsById(1)).thenReturn(false);
        when(volbugMapper.toEntity(volbugDTO)).thenReturn(volbug);
        when(volbugRepository.save(volbug)).thenReturn(volbug);
        when(volbugMapper.toDTO(volbug)).thenReturn(volbugDTO);

        // When
        VolbugDTO result = volbugService.createVolbug(volbugDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdVol()).isEqualTo(1);
        verify(volbugRepository).existsById(1);
        verify(volbugRepository).save(volbug);
    }

    @Test
    void testCreateVolbug_AlreadyExists_ThrowsException() {
        // Given
        when(volbugRepository.existsById(1)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> volbugService.createVolbug(volbugDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Un bug existe déjà pour ce volontaire");

        verify(volbugRepository, never()).save(any());
    }

    @Test
    void testCreateVolbug_NullDTO_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> volbugService.createVolbug(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("bug ne peut pas être null");
    }

    @Test
    void testCreateVolbug_NullIdVol_ThrowsException() {
        // Given
        volbugDTO.setIdVol(null);

        // When & Then
        assertThatThrownBy(() -> volbugService.createVolbug(volbugDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID du volontaire doit être un nombre positif");
    }

    @Test
    void testCreateVolbug_InvalidIdVol_ThrowsException() {
        // Given
        volbugDTO.setIdVol(0);

        // When & Then
        assertThatThrownBy(() -> volbugService.createVolbug(volbugDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID du volontaire doit être un nombre positif");
    }

    @Test
    void testCreateVolbug_NegativeIdVol_ThrowsException() {
        // Given
        volbugDTO.setIdVol(-1);

        // When & Then
        assertThatThrownBy(() -> volbugService.createVolbug(volbugDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID du volontaire doit être un nombre positif");
    }

    // ==================== Tests updateVolbug ====================

    @Test
    void testUpdateVolbug_Success() {
        // Given
        when(volbugRepository.existsById(1)).thenReturn(true);
        when(volbugRepository.findById(1)).thenReturn(Optional.of(volbug));
        when(volbugMapper.updateEntityFromDTO(volbug, volbugDTO)).thenReturn(volbug);
        when(volbugRepository.save(volbug)).thenReturn(volbug);
        when(volbugMapper.toDTO(volbug)).thenReturn(volbugDTO);

        // When
        Optional<VolbugDTO> result = volbugService.updateVolbug(1, volbugDTO);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getIdVol()).isEqualTo(1);
        verify(volbugRepository).save(volbug);
    }

    @Test
    void testUpdateVolbug_NullId_ReturnsEmpty() {
        // When
        Optional<VolbugDTO> result = volbugService.updateVolbug(null, volbugDTO);

        // Then
        assertThat(result).isEmpty();
        verify(volbugRepository, never()).existsById(any());
    }

    @Test
    void testUpdateVolbug_NotFound_ReturnsEmpty() {
        // Given
        when(volbugRepository.existsById(999)).thenReturn(false);

        // When
        Optional<VolbugDTO> result = volbugService.updateVolbug(999, volbugDTO);

        // Then
        assertThat(result).isEmpty();
        verify(volbugRepository, never()).findById(any());
    }

    @Test
    void testUpdateVolbug_ChangeIdToExisting_ThrowsException() {
        // Given
        volbugDTO.setIdVol(2); // Changing ID
        when(volbugRepository.existsById(1)).thenReturn(true);
        when(volbugRepository.existsById(2)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> volbugService.updateVolbug(1, volbugDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Un bug existe déjà pour ce nouveau volontaire");
    }

    @Test
    void testUpdateVolbug_ChangeIdToNew_Success() {
        // Given
        volbugDTO.setIdVol(2); // Changing ID to new one
        when(volbugRepository.existsById(1)).thenReturn(true);
        when(volbugRepository.existsById(2)).thenReturn(false); // New ID doesn't exist
        when(volbugRepository.findById(1)).thenReturn(Optional.of(volbug));
        when(volbugMapper.updateEntityFromDTO(volbug, volbugDTO)).thenReturn(volbug);
        when(volbugRepository.save(volbug)).thenReturn(volbug);
        when(volbugMapper.toDTO(volbug)).thenReturn(volbugDTO);

        // When
        Optional<VolbugDTO> result = volbugService.updateVolbug(1, volbugDTO);

        // Then
        assertThat(result).isPresent();
        verify(volbugRepository).save(volbug);
    }

    @Test
    void testUpdateVolbug_NullDTO_ThrowsException() {
        // Given
        when(volbugRepository.existsById(1)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> volbugService.updateVolbug(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("bug ne peut pas être null");
    }

    @Test
    void testUpdateVolbug_InvalidIdVol_ThrowsException() {
        // Given
        volbugDTO.setIdVol(0);
        when(volbugRepository.existsById(1)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> volbugService.updateVolbug(1, volbugDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID du volontaire doit être un nombre positif");
    }

    // ==================== Tests deleteVolbug ====================

    @Test
    void testDeleteVolbug_Success() {
        // Given
        when(volbugRepository.existsById(1)).thenReturn(true);

        // When
        boolean result = volbugService.deleteVolbug(1);

        // Then
        assertThat(result).isTrue();
        verify(volbugRepository).deleteById(1);
    }

    @Test
    void testDeleteVolbug_NotFound_ReturnsFalse() {
        // Given
        when(volbugRepository.existsById(999)).thenReturn(false);

        // When
        boolean result = volbugService.deleteVolbug(999);

        // Then
        assertThat(result).isFalse();
        verify(volbugRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteVolbug_NullId_ReturnsFalse() {
        // When
        boolean result = volbugService.deleteVolbug(null);

        // Then
        assertThat(result).isFalse();
        verify(volbugRepository, never()).existsById(any());
    }

    // ==================== Tests existsByIdVol ====================

    @Test
    void testExistsByIdVol_Exists_ReturnsTrue() {
        // Given
        when(volbugRepository.existsByIdVol(1)).thenReturn(true);

        // When
        boolean result = volbugService.existsByIdVol(1);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testExistsByIdVol_NotExists_ReturnsFalse() {
        // Given
        when(volbugRepository.existsByIdVol(999)).thenReturn(false);

        // When
        boolean result = volbugService.existsByIdVol(999);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testExistsByIdVol_NullId_ReturnsFalse() {
        // When
        boolean result = volbugService.existsByIdVol(null);

        // Then
        assertThat(result).isFalse();
        verify(volbugRepository, never()).existsByIdVol(anyInt());
    }
}
