package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.business.mapper.InfobancaireMapper;
import com.example.cosmetest.data.repository.InfobancaireRepository;
import com.example.cosmetest.domain.model.Infobancaire;
import com.example.cosmetest.domain.model.InfobancaireId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour InfobancaireServiceImpl
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InfobancaireServiceImpl - Tests unitaires")
class InfobancaireServiceImplTest {

    @Mock
    private InfobancaireRepository infobancaireRepository;

    @Mock
    private InfobancaireMapper infobancaireMapper;

    @InjectMocks
    private InfobancaireServiceImpl infobancaireService;

    private Infobancaire testInfobancaire1;
    private Infobancaire testInfobancaire2;
    private InfobancaireDTO testInfobancaireDTO1;
    private InfobancaireDTO testInfobancaireDTO2;
    private InfobancaireId testId1;
    private InfobancaireId testId2;

    @BeforeEach
    void setUp() {
        // IDs composites (bic, iban, idVol)
        testId1 = new InfobancaireId("BNPAFRPP", "FR7630004000031234567890143", 100);
        testId2 = new InfobancaireId("CEPAFRPP", "FR7630006000011234567890189", 101);

        // Entités
        testInfobancaire1 = new Infobancaire();
        testInfobancaire1.setId(testId1);

        testInfobancaire2 = new Infobancaire();
        testInfobancaire2.setId(testId2);

        // DTOs
        testInfobancaireDTO1 = new InfobancaireDTO();
        testInfobancaireDTO1.setBic("BNPAFRPP");
        testInfobancaireDTO1.setIban("FR7630004000031234567890143");
        testInfobancaireDTO1.setIdVol(100);

        testInfobancaireDTO2 = new InfobancaireDTO();
        testInfobancaireDTO2.setBic("CEPAFRPP");
        testInfobancaireDTO2.setIban("FR7630006000011234567890189");
        testInfobancaireDTO2.setIdVol(101);
    }

    // ================== getAllInfobancaires Tests ==================

    @Test
    @DisplayName("getAllInfobancaires() - Récupération de toutes les informations bancaires")
    void testGetAllInfobancaires_Success() {
        // Given
        List<Infobancaire> infobancaires = Arrays.asList(testInfobancaire1, testInfobancaire2);
        List<InfobancaireDTO> expectedDTOs = Arrays.asList(testInfobancaireDTO1, testInfobancaireDTO2);

        when(infobancaireRepository.findAll()).thenReturn(infobancaires);
        when(infobancaireMapper.toDTOList(infobancaires)).thenReturn(expectedDTOs);

        // When
        List<InfobancaireDTO> result = infobancaireService.getAllInfobancaires();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(testInfobancaireDTO1, testInfobancaireDTO2);

        verify(infobancaireRepository, times(1)).findAll();
        verify(infobancaireMapper, times(1)).toDTOList(infobancaires);
    }

    @Test
    @DisplayName("getAllInfobancaires() - Liste vide")
    void testGetAllInfobancaires_EmptyList() {
        // Given
        when(infobancaireRepository.findAll()).thenReturn(Arrays.asList());
        when(infobancaireMapper.toDTOList(any())).thenReturn(Arrays.asList());

        // When
        List<InfobancaireDTO> result = infobancaireService.getAllInfobancaires();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(infobancaireRepository, times(1)).findAll();
    }

    // ================== getInfobancaireById Tests ==================

    @Test
    @DisplayName("getInfobancaireById() - Information bancaire trouvée")
    void testGetInfobancaireById_Found() {
        // Given
        when(infobancaireRepository.findById(testId1)).thenReturn(Optional.of(testInfobancaire1));
        when(infobancaireMapper.toDTO(testInfobancaire1)).thenReturn(testInfobancaireDTO1);

        // When
        Optional<InfobancaireDTO> result = infobancaireService.getInfobancaireById("BNPAFRPP", "FR7630004000031234567890143", 100);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testInfobancaireDTO1);

        verify(infobancaireRepository, times(1)).findById(any(InfobancaireId.class));
        verify(infobancaireMapper, times(1)).toDTO(testInfobancaire1);
    }

    @Test
    @DisplayName("getInfobancaireById() - Information bancaire non trouvée")
    void testGetInfobancaireById_NotFound() {
        // Given
        when(infobancaireRepository.findById(any(InfobancaireId.class))).thenReturn(Optional.empty());

        // When
        Optional<InfobancaireDTO> result = infobancaireService.getInfobancaireById("BNPAFRPP", "FR7630004000031234567890143", 999);

        // Then
        assertThat(result).isEmpty();

        verify(infobancaireRepository, times(1)).findById(any(InfobancaireId.class));
        verify(infobancaireMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("getInfobancaireById() - Paramètres null retourne Optional.empty()")
    void testGetInfobancaireById_NullParameters() {
        // When
        Optional<InfobancaireDTO> result1 = infobancaireService.getInfobancaireById(null, "FR7630004000031234567890143", 100);
        Optional<InfobancaireDTO> result2 = infobancaireService.getInfobancaireById("BNPAFRPP", null, 100);
        Optional<InfobancaireDTO> result3 = infobancaireService.getInfobancaireById("BNPAFRPP", "FR7630004000031234567890143", null);

        // Then
        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();
        assertThat(result3).isEmpty();

        verify(infobancaireRepository, never()).findById(any());
    }

    // ================== getInfobancairesByIdVol Tests ==================

    @Test
    @DisplayName("getInfobancairesByIdVol() - Informations bancaires trouvées")
    void testGetInfobancairesByIdVol_Success() {
        // Given
        List<Infobancaire> infobancaires = Arrays.asList(testInfobancaire1);
        List<InfobancaireDTO> expectedDTOs = Arrays.asList(testInfobancaireDTO1);

        when(infobancaireRepository.findByIdIdVol(100)).thenReturn(infobancaires);
        when(infobancaireMapper.toDTOList(infobancaires)).thenReturn(expectedDTOs);

        // When
        List<InfobancaireDTO> result = infobancaireService.getInfobancairesByIdVol(100);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(infobancaireRepository, times(1)).findByIdIdVol(100);
        verify(infobancaireMapper, times(1)).toDTOList(infobancaires);
    }

    @Test
    @DisplayName("getInfobancairesByIdVol() - ID null retourne liste vide")
    void testGetInfobancairesByIdVol_NullId() {
        // When
        List<InfobancaireDTO> result = infobancaireService.getInfobancairesByIdVol(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(infobancaireRepository, never()).findByIdIdVol(any());
    }

    @Test
    @DisplayName("getInfobancairesByIdVol() - Aucune information bancaire trouvée")
    void testGetInfobancairesByIdVol_Empty() {
        // Given
        when(infobancaireRepository.findByIdIdVol(999)).thenReturn(Arrays.asList());
        when(infobancaireMapper.toDTOList(any())).thenReturn(Arrays.asList());

        // When
        List<InfobancaireDTO> result = infobancaireService.getInfobancairesByIdVol(999);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(infobancaireRepository, times(1)).findByIdIdVol(999);
    }

    // ================== getInfobancairesByBicAndIban Tests ==================

    @Test
    @DisplayName("getInfobancairesByBicAndIban() - Informations bancaires trouvées")
    void testGetInfobancairesByBicAndIban_Success() {
        // Given
        List<Infobancaire> infobancaires = Arrays.asList(testInfobancaire1);
        List<InfobancaireDTO> expectedDTOs = Arrays.asList(testInfobancaireDTO1);

        when(infobancaireRepository.findByIdBicAndIdIban("BNPAFRPP", "FR7630004000031234567890143")).thenReturn(infobancaires);
        when(infobancaireMapper.toDTOList(infobancaires)).thenReturn(expectedDTOs);

        // When
        List<InfobancaireDTO> result = infobancaireService.getInfobancairesByBicAndIban("BNPAFRPP", "FR7630004000031234567890143");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(infobancaireRepository, times(1)).findByIdBicAndIdIban("BNPAFRPP", "FR7630004000031234567890143");
    }

    @Test
    @DisplayName("getInfobancairesByBicAndIban() - Paramètres null retourne liste vide")
    void testGetInfobancairesByBicAndIban_NullParameters() {
        // When
        List<InfobancaireDTO> result1 = infobancaireService.getInfobancairesByBicAndIban(null, "FR7630004000031234567890143");
        List<InfobancaireDTO> result2 = infobancaireService.getInfobancairesByBicAndIban("BNPAFRPP", null);

        // Then
        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();

        verify(infobancaireRepository, never()).findByIdBicAndIdIban(any(), any());
    }

    // ================== getInfobancairesByIban Tests ==================

    @Test
    @DisplayName("getInfobancairesByIban() - Informations bancaires trouvées")
    void testGetInfobancairesByIban_Success() {
        // Given
        List<Infobancaire> infobancaires = Arrays.asList(testInfobancaire1);
        List<InfobancaireDTO> expectedDTOs = Arrays.asList(testInfobancaireDTO1);

        when(infobancaireRepository.findByIdIban("FR7630004000031234567890143")).thenReturn(infobancaires);
        when(infobancaireMapper.toDTOList(infobancaires)).thenReturn(expectedDTOs);

        // When
        List<InfobancaireDTO> result = infobancaireService.getInfobancairesByIban("FR7630004000031234567890143");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(infobancaireRepository, times(1)).findByIdIban("FR7630004000031234567890143");
    }

    @Test
    @DisplayName("getInfobancairesByIban() - IBAN null ou vide retourne liste vide")
    void testGetInfobancairesByIban_NullOrEmpty() {
        // When
        List<InfobancaireDTO> result1 = infobancaireService.getInfobancairesByIban(null);
        List<InfobancaireDTO> result2 = infobancaireService.getInfobancairesByIban("");

        // Then
        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();

        verify(infobancaireRepository, never()).findByIdIban(any());
    }

    // ================== getInfobancairesByBic Tests ==================

    @Test
    @DisplayName("getInfobancairesByBic() - Informations bancaires trouvées")
    void testGetInfobancairesByBic_Success() {
        // Given
        List<Infobancaire> infobancaires = Arrays.asList(testInfobancaire1);
        List<InfobancaireDTO> expectedDTOs = Arrays.asList(testInfobancaireDTO1);

        when(infobancaireRepository.findByIdBic("BNPAFRPP")).thenReturn(infobancaires);
        when(infobancaireMapper.toDTOList(infobancaires)).thenReturn(expectedDTOs);

        // When
        List<InfobancaireDTO> result = infobancaireService.getInfobancairesByBic("BNPAFRPP");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);

        verify(infobancaireRepository, times(1)).findByIdBic("BNPAFRPP");
    }

    @Test
    @DisplayName("getInfobancairesByBic() - BIC null ou vide retourne liste vide")
    void testGetInfobancairesByBic_NullOrEmpty() {
        // When
        List<InfobancaireDTO> result1 = infobancaireService.getInfobancairesByBic(null);
        List<InfobancaireDTO> result2 = infobancaireService.getInfobancairesByBic("");

        // Then
        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();

        verify(infobancaireRepository, never()).findByIdBic(any());
    }

    // ================== createInfobancaire Tests ==================

    @Test
    @DisplayName("createInfobancaire() - Création réussie")
    void testCreateInfobancaire_Success() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(false);
        when(infobancaireMapper.toEntity(testInfobancaireDTO1)).thenReturn(testInfobancaire1);
        when(infobancaireRepository.save(testInfobancaire1)).thenReturn(testInfobancaire1);
        when(infobancaireMapper.toDTO(testInfobancaire1)).thenReturn(testInfobancaireDTO1);

        // When
        InfobancaireDTO result = infobancaireService.createInfobancaire(testInfobancaireDTO1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBic()).isEqualTo("BNPAFRPP");

        verify(infobancaireRepository, times(1)).existsById(any(InfobancaireId.class));
        verify(infobancaireRepository, times(1)).save(testInfobancaire1);
    }

    @Test
    @DisplayName("createInfobancaire() - Information bancaire déjà existante")
    void testCreateInfobancaire_AlreadyExists() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(true);

        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(testInfobancaireDTO1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existe déjà");

        verify(infobancaireRepository, times(1)).existsById(any(InfobancaireId.class));
        verify(infobancaireRepository, never()).save(any());
    }

    @Test
    @DisplayName("createInfobancaire() - Validation échoue (DTO null)")
    void testCreateInfobancaire_NullDTO() {
        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");

        verify(infobancaireRepository, never()).save(any());
    }

    @Test
    @DisplayName("createInfobancaire() - Validation échoue (BIC vide)")
    void testCreateInfobancaire_EmptyBic() {
        // Given
        InfobancaireDTO invalidDTO = new InfobancaireDTO();
        invalidDTO.setBic("");
        invalidDTO.setIban("FR7630004000031234567890143");
        invalidDTO.setIdVol(100);

        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("BIC");

        verify(infobancaireRepository, never()).save(any());
    }

    @Test
    @DisplayName("createInfobancaire() - Validation échoue (format BIC invalide)")
    void testCreateInfobancaire_InvalidBicFormat() {
        // Given
        InfobancaireDTO invalidDTO = new InfobancaireDTO();
        invalidDTO.setBic("INVALID");
        invalidDTO.setIban("FR7630004000031234567890143");
        invalidDTO.setIdVol(100);

        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("BIC");

        verify(infobancaireRepository, never()).save(any());
    }

    @Test
    @DisplayName("createInfobancaire() - Validation échoue (IBAN vide)")
    void testCreateInfobancaire_EmptyIban() {
        // Given
        InfobancaireDTO invalidDTO = new InfobancaireDTO();
        invalidDTO.setBic("BNPAFRPP");
        invalidDTO.setIban("");
        invalidDTO.setIdVol(100);

        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("IBAN");

        verify(infobancaireRepository, never()).save(any());
    }

    @Test
    @DisplayName("createInfobancaire() - Validation échoue (format IBAN invalide)")
    void testCreateInfobancaire_InvalidIbanFormat() {
        // Given
        InfobancaireDTO invalidDTO = new InfobancaireDTO();
        invalidDTO.setBic("BNPAFRPP");
        invalidDTO.setIban("INVALID_IBAN");
        invalidDTO.setIdVol(100);

        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("IBAN");

        verify(infobancaireRepository, never()).save(any());
    }

    @Test
    @DisplayName("createInfobancaire() - Validation échoue (ID volontaire invalide)")
    void testCreateInfobancaire_InvalidIdVol() {
        // Given
        InfobancaireDTO invalidDTO = new InfobancaireDTO();
        invalidDTO.setBic("BNPAFRPP");
        invalidDTO.setIban("FR7630004000031234567890143");
        invalidDTO.setIdVol(0);

        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("volontaire");

        verify(infobancaireRepository, never()).save(any());
    }

    // ================== updateInfobancaire Tests ==================

    @Test
    @DisplayName("updateInfobancaire() - Mise à jour réussie")
    void testUpdateInfobancaire_Success() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(true).thenReturn(false);
        when(infobancaireMapper.toEntity(testInfobancaireDTO1)).thenReturn(testInfobancaire1);
        when(infobancaireRepository.save(testInfobancaire1)).thenReturn(testInfobancaire1);
        when(infobancaireMapper.toDTO(testInfobancaire1)).thenReturn(testInfobancaireDTO1);
        doNothing().when(infobancaireRepository).deleteById(any(InfobancaireId.class));

        // When
        Optional<InfobancaireDTO> result = infobancaireService.updateInfobancaire("BNPAFRPP", "FR7630004000031234567890143", 100, testInfobancaireDTO1);

        // Then
        assertThat(result).isPresent();

        verify(infobancaireRepository, times(1)).deleteById(any(InfobancaireId.class));
        verify(infobancaireRepository, times(1)).save(testInfobancaire1);
    }

    @Test
    @DisplayName("updateInfobancaire() - Information bancaire non trouvée")
    void testUpdateInfobancaire_NotFound() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(false);

        // When
        Optional<InfobancaireDTO> result = infobancaireService.updateInfobancaire("BNPAFRPP", "FR7630004000031234567890143", 999, testInfobancaireDTO1);

        // Then
        assertThat(result).isEmpty();

        verify(infobancaireRepository, times(1)).existsById(any(InfobancaireId.class));
        verify(infobancaireRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateInfobancaire() - Paramètres null retourne Optional.empty()")
    void testUpdateInfobancaire_NullParameters() {
        // When
        Optional<InfobancaireDTO> result1 = infobancaireService.updateInfobancaire(null, "FR7630004000031234567890143", 100, testInfobancaireDTO1);
        Optional<InfobancaireDTO> result2 = infobancaireService.updateInfobancaire("BNPAFRPP", null, 100, testInfobancaireDTO1);
        Optional<InfobancaireDTO> result3 = infobancaireService.updateInfobancaire("BNPAFRPP", "FR7630004000031234567890143", null, testInfobancaireDTO1);

        // Then
        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();
        assertThat(result3).isEmpty();

        verify(infobancaireRepository, never()).existsById(any());
    }

    // ================== deleteInfobancaire Tests ==================

    @Test
    @DisplayName("deleteInfobancaire() - Suppression réussie")
    void testDeleteInfobancaire_Success() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(true);
        doNothing().when(infobancaireRepository).deleteById(any(InfobancaireId.class));

        // When
        boolean result = infobancaireService.deleteInfobancaire("BNPAFRPP", "FR7630004000031234567890143", 100);

        // Then
        assertThat(result).isTrue();

        verify(infobancaireRepository, times(1)).existsById(any(InfobancaireId.class));
        verify(infobancaireRepository, times(1)).deleteById(any(InfobancaireId.class));
    }

    @Test
    @DisplayName("deleteInfobancaire() - Information bancaire non trouvée")
    void testDeleteInfobancaire_NotFound() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(false);

        // When
        boolean result = infobancaireService.deleteInfobancaire("BNPAFRPP", "FR7630004000031234567890143", 999);

        // Then
        assertThat(result).isFalse();

        verify(infobancaireRepository, times(1)).existsById(any(InfobancaireId.class));
        verify(infobancaireRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteInfobancaire() - Paramètres null retourne false")
    void testDeleteInfobancaire_NullParameters() {
        // When
        boolean result1 = infobancaireService.deleteInfobancaire(null, "FR7630004000031234567890143", 100);
        boolean result2 = infobancaireService.deleteInfobancaire("BNPAFRPP", null, 100);
        boolean result3 = infobancaireService.deleteInfobancaire("BNPAFRPP", "FR7630004000031234567890143", null);

        // Then
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();

        verify(infobancaireRepository, never()).existsById(any());
        verify(infobancaireRepository, never()).deleteById(any());
    }

    // ================== existsById Tests ==================

    @Test
    @DisplayName("existsById() - Information bancaire existe")
    void testExistsById_True() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(true);

        // When
        boolean result = infobancaireService.existsById("BNPAFRPP", "FR7630004000031234567890143", 100);

        // Then
        assertThat(result).isTrue();

        verify(infobancaireRepository, times(1)).existsById(any(InfobancaireId.class));
    }

    @Test
    @DisplayName("existsById() - Information bancaire n'existe pas")
    void testExistsById_False() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(false);

        // When
        boolean result = infobancaireService.existsById("BNPAFRPP", "FR7630004000031234567890143", 999);

        // Then
        assertThat(result).isFalse();

        verify(infobancaireRepository, times(1)).existsById(any(InfobancaireId.class));
    }

    @Test
    @DisplayName("existsById() - Paramètres null retourne false")
    void testExistsById_NullParameters() {
        // When
        boolean result1 = infobancaireService.existsById(null, "FR7630004000031234567890143", 100);
        boolean result2 = infobancaireService.existsById("BNPAFRPP", null, 100);
        boolean result3 = infobancaireService.existsById("BNPAFRPP", "FR7630004000031234567890143", null);

        // Then
        assertThat(result1).isFalse();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();

        verify(infobancaireRepository, never()).existsById(any());
    }

    // ================== existsByIdVol Tests ==================

    @Test
    @DisplayName("existsByIdVol() - Information bancaire existe pour le volontaire")
    void testExistsByIdVol_True() {
        // Given
        when(infobancaireRepository.existsByIdIdVol(100)).thenReturn(true);

        // When
        boolean result = infobancaireService.existsByIdVol(100);

        // Then
        assertThat(result).isTrue();

        verify(infobancaireRepository, times(1)).existsByIdIdVol(100);
    }

    @Test
    @DisplayName("existsByIdVol() - Aucune information bancaire pour le volontaire")
    void testExistsByIdVol_False() {
        // Given
        when(infobancaireRepository.existsByIdIdVol(999)).thenReturn(false);

        // When
        boolean result = infobancaireService.existsByIdVol(999);

        // Then
        assertThat(result).isFalse();

        verify(infobancaireRepository, times(1)).existsByIdIdVol(999);
    }

    @Test
    @DisplayName("existsByIdVol() - ID null retourne false")
    void testExistsByIdVol_NullId() {
        // When
        boolean result = infobancaireService.existsByIdVol(null);

        // Then
        assertThat(result).isFalse();

        verify(infobancaireRepository, never()).existsByIdIdVol(any());
    }

    // ================== Gestion des Exceptions Tests ==================

    @Test
    @DisplayName("getAllInfobancaires() - Exception dans le repository")
    void testGetAllInfobancaires_RepositoryException() {
        // Given
        when(infobancaireRepository.findAll()).thenThrow(new RuntimeException("Erreur DB"));

        // When/Then
        assertThatThrownBy(() -> infobancaireService.getAllInfobancaires())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erreur DB");

        verify(infobancaireRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("createInfobancaire() - Exception lors de la sauvegarde")
    void testCreateInfobancaire_SaveException() {
        // Given
        when(infobancaireRepository.existsById(any(InfobancaireId.class))).thenReturn(false);
        when(infobancaireMapper.toEntity(testInfobancaireDTO1)).thenReturn(testInfobancaire1);
        when(infobancaireRepository.save(testInfobancaire1)).thenThrow(new RuntimeException("Erreur contrainte DB"));

        // When/Then
        assertThatThrownBy(() -> infobancaireService.createInfobancaire(testInfobancaireDTO1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("contrainte");

        verify(infobancaireRepository, times(1)).save(testInfobancaire1);
    }
}
