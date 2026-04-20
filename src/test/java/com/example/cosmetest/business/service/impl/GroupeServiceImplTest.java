package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.GroupeDTO;
import com.example.cosmetest.business.mapper.GroupeMapper;
import com.example.cosmetest.data.repository.GroupeRepository;
import com.example.cosmetest.domain.model.Groupe;
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
 * Tests unitaires pour GroupeServiceImpl
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GroupeServiceImpl - Tests unitaires")
class GroupeServiceImplTest {

    @Mock
    private GroupeRepository groupeRepository;

    @Mock
    private GroupeMapper groupeMapper;

    @InjectMocks
    private GroupeServiceImpl groupeService;

    private Groupe testGroupe1;
    private Groupe testGroupe2;
    private GroupeDTO testGroupeDTO1;
    private GroupeDTO testGroupeDTO2;

    @BeforeEach
    void setUp() {
        // Initialisation des entités de test
        testGroupe1 = new Groupe();
        testGroupe1.setIdGroupe(1);
        testGroupe1.setIdEtude(10);
        testGroupe1.setIntitule("Groupe A - Adultes");
        testGroupe1.setAgeMinimum(25);
        testGroupe1.setAgeMaximum(65);
        testGroupe1.setEthnie("Caucasien");
        testGroupe1.setNbSujet(30);
        testGroupe1.setIv(5);

        testGroupe2 = new Groupe();
        testGroupe2.setIdGroupe(2);
        testGroupe2.setIdEtude(10);
        testGroupe2.setIntitule("Groupe B - Jeunes");
        testGroupe2.setAgeMinimum(18);
        testGroupe2.setAgeMaximum(24);
        testGroupe2.setEthnie("Caucasien");
        testGroupe2.setNbSujet(20);
        testGroupe2.setIv(3);

        // Initialisation des DTOs de test
        testGroupeDTO1 = new GroupeDTO();
        testGroupeDTO1.setIdGroupe(1);
        testGroupeDTO1.setIdEtude(10);
        testGroupeDTO1.setIntitule("Groupe A - Adultes");
        testGroupeDTO1.setAgeMinimum(25);
        testGroupeDTO1.setAgeMaximum(65);
        testGroupeDTO1.setEthnie("Caucasien");
        testGroupeDTO1.setNbSujet(30);
        testGroupeDTO1.setIv(5);

        testGroupeDTO2 = new GroupeDTO();
        testGroupeDTO2.setIdGroupe(2);
        testGroupeDTO2.setIdEtude(10);
        testGroupeDTO2.setIntitule("Groupe B - Jeunes");
        testGroupeDTO2.setAgeMinimum(18);
        testGroupeDTO2.setAgeMaximum(24);
        testGroupeDTO2.setEthnie("Caucasien");
        testGroupeDTO2.setNbSujet(20);
        testGroupeDTO2.setIv(3);
    }

    // ================== getAllGroupes Tests ==================

    @Test
    @DisplayName("getAllGroupes() - Récupération de tous les groupes")
    void testGetAllGroupes_Success() {
        // Given
        List<Groupe> groupes = Arrays.asList(testGroupe1, testGroupe2);
        List<GroupeDTO> expectedDTOs = Arrays.asList(testGroupeDTO1, testGroupeDTO2);

        when(groupeRepository.findAll()).thenReturn(groupes);
        when(groupeMapper.toDTOList(groupes)).thenReturn(expectedDTOs);

        // When
        List<GroupeDTO> result = groupeService.getAllGroupes();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(testGroupeDTO1, testGroupeDTO2);

        verify(groupeRepository, times(1)).findAll();
        verify(groupeMapper, times(1)).toDTOList(groupes);
    }

    @Test
    @DisplayName("getAllGroupes() - Liste vide")
    void testGetAllGroupes_EmptyList() {
        // Given
        when(groupeRepository.findAll()).thenReturn(Arrays.asList());
        when(groupeMapper.toDTOList(any())).thenReturn(Arrays.asList());

        // When
        List<GroupeDTO> result = groupeService.getAllGroupes();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(groupeRepository, times(1)).findAll();
    }

    // ================== getGroupeById Tests ==================

    @Test
    @DisplayName("getGroupeById() - Groupe trouvé")
    void testGetGroupeById_Found() {
        // Given
        when(groupeRepository.findById(1)).thenReturn(Optional.of(testGroupe1));
        when(groupeMapper.toDTO(testGroupe1)).thenReturn(testGroupeDTO1);

        // When
        Optional<GroupeDTO> result = groupeService.getGroupeById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testGroupeDTO1);

        verify(groupeRepository, times(1)).findById(1);
        verify(groupeMapper, times(1)).toDTO(testGroupe1);
    }

    @Test
    @DisplayName("getGroupeById() - Groupe non trouvé")
    void testGetGroupeById_NotFound() {
        // Given
        when(groupeRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<GroupeDTO> result = groupeService.getGroupeById(999);

        // Then
        assertThat(result).isEmpty();

        verify(groupeRepository, times(1)).findById(999);
        verify(groupeMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("getGroupeById() - ID null retourne Optional.empty()")
    void testGetGroupeById_NullId() {
        // When
        Optional<GroupeDTO> result = groupeService.getGroupeById(null);

        // Then
        assertThat(result).isEmpty();

        verify(groupeRepository, never()).findById(any());
    }

    // ================== getGroupesByIdEtude Tests ==================

    @Test
    @DisplayName("getGroupesByIdEtude() - Groupes trouvés")
    void testGetGroupesByIdEtude_Success() {
        // Given
        List<Groupe> groupes = Arrays.asList(testGroupe1, testGroupe2);
        List<GroupeDTO> expectedDTOs = Arrays.asList(testGroupeDTO1, testGroupeDTO2);

        when(groupeRepository.findByIdEtude(10)).thenReturn(groupes);
        when(groupeMapper.toDTOList(groupes)).thenReturn(expectedDTOs);

        // When
        List<GroupeDTO> result = groupeService.getGroupesByIdEtude(10);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(groupeRepository, times(1)).findByIdEtude(10);
        verify(groupeMapper, times(1)).toDTOList(groupes);
    }

    @Test
    @DisplayName("getGroupesByIdEtude() - Aucun groupe trouvé")
    void testGetGroupesByIdEtude_Empty() {
        // Given
        when(groupeRepository.findByIdEtude(999)).thenReturn(Arrays.asList());
        when(groupeMapper.toDTOList(any())).thenReturn(Arrays.asList());

        // When
        List<GroupeDTO> result = groupeService.getGroupesByIdEtude(999);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(groupeRepository, times(1)).findByIdEtude(999);
    }

    // ================== createGroupe Tests ==================

    @Test
    @DisplayName("createGroupe() - Création réussie")
    void testCreateGroupe_Success() {
        // Given
        GroupeDTO newGroupeDTO = new GroupeDTO();
        newGroupeDTO.setIdEtude(10);
        newGroupeDTO.setIntitule("Nouveau Groupe");
        newGroupeDTO.setAgeMinimum(30);
        newGroupeDTO.setAgeMaximum(50);
        newGroupeDTO.setNbSujet(25);
        newGroupeDTO.setIv(4);

        Groupe newGroupe = new Groupe();
        Groupe savedGroupe = new Groupe();
        savedGroupe.setIdGroupe(3);

        when(groupeMapper.toEntity(newGroupeDTO)).thenReturn(newGroupe);
        when(groupeRepository.save(newGroupe)).thenReturn(savedGroupe);
        when(groupeMapper.toDTO(savedGroupe)).thenReturn(testGroupeDTO1);

        // When
        GroupeDTO result = groupeService.createGroupe(newGroupeDTO);

        // Then
        assertThat(result).isNotNull();

        verify(groupeMapper, times(1)).toEntity(newGroupeDTO);
        verify(groupeRepository, times(1)).save(newGroupe);
        verify(groupeMapper, times(1)).toDTO(savedGroupe);
    }

    @Test
    @DisplayName("createGroupe() - Validation échoue (groupe null)")
    void testCreateGroupe_NullGroupe() {
        // When/Then
        assertThatThrownBy(() -> groupeService.createGroupe(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");

        verify(groupeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createGroupe() - Validation échoue (idEtude null)")
    void testCreateGroupe_NullIdEtude() {
        // Given
        GroupeDTO invalidDTO = new GroupeDTO();
        invalidDTO.setIdEtude(null);
        invalidDTO.setIntitule("Test");
        invalidDTO.setAgeMinimum(18);
        invalidDTO.setAgeMaximum(65);
        invalidDTO.setNbSujet(10);
        invalidDTO.setIv(2);

        // When/Then
        assertThatThrownBy(() -> groupeService.createGroupe(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("étude");

        verify(groupeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createGroupe() - Validation échoue (intitulé vide)")
    void testCreateGroupe_EmptyIntitule() {
        // Given
        GroupeDTO invalidDTO = new GroupeDTO();
        invalidDTO.setIdEtude(10);
        invalidDTO.setIntitule("");
        invalidDTO.setAgeMinimum(18);
        invalidDTO.setAgeMaximum(65);
        invalidDTO.setNbSujet(10);
        invalidDTO.setIv(2);

        // When/Then
        assertThatThrownBy(() -> groupeService.createGroupe(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("intitulé");

        verify(groupeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createGroupe() - Validation échoue (âge minimum négatif)")
    void testCreateGroupe_NegativeAgeMin() {
        // Given
        GroupeDTO invalidDTO = new GroupeDTO();
        invalidDTO.setIdEtude(10);
        invalidDTO.setIntitule("Test");
        invalidDTO.setAgeMinimum(-1);
        invalidDTO.setAgeMaximum(65);
        invalidDTO.setNbSujet(10);
        invalidDTO.setIv(2);

        // When/Then
        assertThatThrownBy(() -> groupeService.createGroupe(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("âge minimum");

        verify(groupeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createGroupe() - Validation échoue (âge maximum < âge minimum)")
    void testCreateGroupe_AgeMaxLessThanMin() {
        // Given
        GroupeDTO invalidDTO = new GroupeDTO();
        invalidDTO.setIdEtude(10);
        invalidDTO.setIntitule("Test");
        invalidDTO.setAgeMinimum(50);
        invalidDTO.setAgeMaximum(30);
        invalidDTO.setNbSujet(10);
        invalidDTO.setIv(2);

        // When/Then
        assertThatThrownBy(() -> groupeService.createGroupe(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("âge maximum");

        verify(groupeRepository, never()).save(any());
    }

    @Test
    @DisplayName("createGroupe() - Validation échoue (nbSujet négatif)")
    void testCreateGroupe_NegativeNbSujet() {
        // Given
        GroupeDTO invalidDTO = new GroupeDTO();
        invalidDTO.setIdEtude(10);
        invalidDTO.setIntitule("Test");
        invalidDTO.setAgeMinimum(18);
        invalidDTO.setAgeMaximum(65);
        invalidDTO.setNbSujet(-5);
        invalidDTO.setIv(2);

        // When/Then
        assertThatThrownBy(() -> groupeService.createGroupe(invalidDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("sujets");

        verify(groupeRepository, never()).save(any());
    }

    // ================== updateGroupe Tests ==================

    @Test
    @DisplayName("updateGroupe() - Mise à jour réussie")
    void testUpdateGroupe_Success() {
        // Given
        GroupeDTO updateDTO = new GroupeDTO();
        updateDTO.setIdEtude(10);
        updateDTO.setIntitule("Groupe Modifié");
        updateDTO.setAgeMinimum(25);
        updateDTO.setAgeMaximum(60);
        updateDTO.setNbSujet(35);
        updateDTO.setIv(6);

        when(groupeRepository.existsById(1)).thenReturn(true);
        when(groupeRepository.findById(1)).thenReturn(Optional.of(testGroupe1));
        when(groupeMapper.updateEntityFromDTO(testGroupe1, updateDTO)).thenReturn(testGroupe1);
        when(groupeRepository.save(testGroupe1)).thenReturn(testGroupe1);
        when(groupeMapper.toDTO(testGroupe1)).thenReturn(testGroupeDTO1);

        // When
        Optional<GroupeDTO> result = groupeService.updateGroupe(1, updateDTO);

        // Then
        assertThat(result).isPresent();

        verify(groupeRepository, times(1)).existsById(1);
        verify(groupeRepository, times(1)).findById(1);
        verify(groupeRepository, times(1)).save(testGroupe1);
    }

    @Test
    @DisplayName("updateGroupe() - Groupe non trouvé")
    void testUpdateGroupe_NotFound() {
        // Given
        when(groupeRepository.existsById(999)).thenReturn(false);

        // When
        Optional<GroupeDTO> result = groupeService.updateGroupe(999, testGroupeDTO1);

        // Then
        assertThat(result).isEmpty();

        verify(groupeRepository, times(1)).existsById(999);
        verify(groupeRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateGroupe() - ID null retourne Optional.empty()")
    void testUpdateGroupe_NullId() {
        // When
        Optional<GroupeDTO> result = groupeService.updateGroupe(null, testGroupeDTO1);

        // Then
        assertThat(result).isEmpty();

        verify(groupeRepository, never()).existsById(any());
        verify(groupeRepository, never()).save(any());
    }

    // ================== deleteGroupe Tests ==================

    @Test
    @DisplayName("deleteGroupe() - Suppression réussie")
    void testDeleteGroupe_Success() {
        // Given
        when(groupeRepository.existsById(1)).thenReturn(true);
        doNothing().when(groupeRepository).deleteById(1);

        // When
        boolean result = groupeService.deleteGroupe(1);

        // Then
        assertThat(result).isTrue();

        verify(groupeRepository, times(1)).existsById(1);
        verify(groupeRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deleteGroupe() - Groupe non trouvé")
    void testDeleteGroupe_NotFound() {
        // Given
        when(groupeRepository.existsById(999)).thenReturn(false);

        // When
        boolean result = groupeService.deleteGroupe(999);

        // Then
        assertThat(result).isFalse();

        verify(groupeRepository, times(1)).existsById(999);
        verify(groupeRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteGroupe() - ID null retourne false")
    void testDeleteGroupe_NullId() {
        // When
        boolean result = groupeService.deleteGroupe(null);

        // Then
        assertThat(result).isFalse();

        verify(groupeRepository, never()).existsById(any());
        verify(groupeRepository, never()).deleteById(any());
    }

    // ================== existsById Tests ==================

    @Test
    @DisplayName("existsById() - Groupe existe")
    void testExistsById_True() {
        // Given
        when(groupeRepository.existsById(1)).thenReturn(true);

        // When
        boolean result = groupeService.existsById(1);

        // Then
        assertThat(result).isTrue();

        verify(groupeRepository, times(1)).existsById(1);
    }

    @Test
    @DisplayName("existsById() - Groupe n'existe pas")
    void testExistsById_False() {
        // Given
        when(groupeRepository.existsById(999)).thenReturn(false);

        // When
        boolean result = groupeService.existsById(999);

        // Then
        assertThat(result).isFalse();

        verify(groupeRepository, times(1)).existsById(999);
    }

    @Test
    @DisplayName("existsById() - ID null retourne false")
    void testExistsById_NullId() {
        // When
        boolean result = groupeService.existsById(null);

        // Then
        assertThat(result).isFalse();

        verify(groupeRepository, never()).existsById(any());
    }

    // ================== getGroupesByAgeRange Tests ==================

    @Test
    @DisplayName("getGroupesByAgeRange() - Filtre par âge minimum")
    void testGetGroupesByAgeRange_MinAge() {
        // Given
        List<Groupe> allGroupes = Arrays.asList(testGroupe1, testGroupe2);
        when(groupeRepository.findAll()).thenReturn(allGroupes);
        when(groupeMapper.toDTOList(anyList())).thenReturn(Arrays.asList(testGroupeDTO1));

        // When
        List<GroupeDTO> result = groupeService.getGroupesByAgeRange(25, null);

        // Then
        assertThat(result).isNotNull();

        verify(groupeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getGroupesByAgeRange() - Filtre par âge maximum")
    void testGetGroupesByAgeRange_MaxAge() {
        // Given
        List<Groupe> allGroupes = Arrays.asList(testGroupe1, testGroupe2);
        when(groupeRepository.findAll()).thenReturn(allGroupes);
        when(groupeMapper.toDTOList(anyList())).thenReturn(Arrays.asList(testGroupeDTO2));

        // When
        List<GroupeDTO> result = groupeService.getGroupesByAgeRange(null, 24);

        // Then
        assertThat(result).isNotNull();

        verify(groupeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getGroupesByAgeRange() - Filtre par plage d'âge")
    void testGetGroupesByAgeRange_BothAges() {
        // Given
        List<Groupe> allGroupes = Arrays.asList(testGroupe1, testGroupe2);
        when(groupeRepository.findAll()).thenReturn(allGroupes);
        when(groupeMapper.toDTOList(anyList())).thenReturn(Arrays.asList(testGroupeDTO1, testGroupeDTO2));

        // When
        List<GroupeDTO> result = groupeService.getGroupesByAgeRange(18, 65);

        // Then
        assertThat(result).isNotNull();

        verify(groupeRepository, times(1)).findAll();
    }

    // ================== getGroupesByEthnie Tests ==================

    @Test
    @DisplayName("getGroupesByEthnie() - Groupes trouvés")
    void testGetGroupesByEthnie_Success() {
        // Given
        List<Groupe> groupes = Arrays.asList(testGroupe1, testGroupe2);
        List<GroupeDTO> expectedDTOs = Arrays.asList(testGroupeDTO1, testGroupeDTO2);

        when(groupeRepository.findByEthnie("Caucasien")).thenReturn(groupes);
        when(groupeMapper.toDTOList(groupes)).thenReturn(expectedDTOs);

        // When
        List<GroupeDTO> result = groupeService.getGroupesByEthnie("Caucasien");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(groupeRepository, times(1)).findByEthnie("Caucasien");
    }

    @Test
    @DisplayName("getGroupesByEthnie() - Ethnie null retourne liste vide")
    void testGetGroupesByEthnie_NullEthnie() {
        // When
        List<GroupeDTO> result = groupeService.getGroupesByEthnie(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(groupeRepository, never()).findByEthnie(any());
    }

    @Test
    @DisplayName("getGroupesByEthnie() - Ethnie vide retourne liste vide")
    void testGetGroupesByEthnie_EmptyEthnie() {
        // When
        List<GroupeDTO> result = groupeService.getGroupesByEthnie("");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(groupeRepository, never()).findByEthnie(any());
    }
}
