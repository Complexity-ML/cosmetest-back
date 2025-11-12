package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolontaireDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.mapper.VolontaireMapper;
import com.example.cosmetest.data.repository.VolontaireRepository;
import com.example.cosmetest.domain.model.Volontaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour VolontaireServiceImpl
 * 
 * Ces tests vérifient la logique métier du service de gestion des volontaires
 * en mockant les dépendances (Repository et Mapper)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - VolontaireServiceImpl")
class VolontaireServiceImplTest {

    @Mock
    private VolontaireRepository volontaireRepository;

    @Mock
    private VolontaireMapper volontaireMapper;

    @InjectMocks
    private VolontaireServiceImpl volontaireService;

    private Volontaire testVolontaire1;
    private Volontaire testVolontaire2;
    private VolontaireDTO testVolontaireDTO1;
    private VolontaireDTO testVolontaireDTO2;
    private VolontaireDetailDTO testVolontaireDetailDTO;

    @BeforeEach
    void setUp() {
        // Préparer des volontaires de test
        testVolontaire1 = new Volontaire();
        testVolontaire1.setIdVol(1);
        testVolontaire1.setNomVol("Dupont");
        testVolontaire1.setPrenomVol("Jean");
        testVolontaire1.setSexe("H");
        testVolontaire1.setArchive(false);

        testVolontaire2 = new Volontaire();
        testVolontaire2.setIdVol(2);
        testVolontaire2.setNomVol("Martin");
        testVolontaire2.setPrenomVol("Marie");
        testVolontaire2.setSexe("F");
        testVolontaire2.setArchive(false);

        // Préparer des DTOs de test
        testVolontaireDTO1 = new VolontaireDTO();
        testVolontaireDTO1.setIdVol(1);
        testVolontaireDTO1.setNomVol("Dupont");
        testVolontaireDTO1.setPrenomVol("Jean");

        testVolontaireDTO2 = new VolontaireDTO();
        testVolontaireDTO2.setIdVol(2);
        testVolontaireDTO2.setNomVol("Martin");
        testVolontaireDTO2.setPrenomVol("Marie");

        testVolontaireDetailDTO = new VolontaireDetailDTO();
        testVolontaireDetailDTO.setIdVol(1);
        testVolontaireDetailDTO.setNomVol("Dupont");
        testVolontaireDetailDTO.setPrenomVol("Jean");
    }

    // ===== TESTS GET ALL VOLONTAIRES =====

    @Test
    @DisplayName("getAllVolontaires() - Récupération de tous les volontaires")
    void testGetAllVolontaires_Success() {
        // Given
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1, testVolontaire2);
        List<VolontaireDTO> expectedDTOs = Arrays.asList(testVolontaireDTO1, testVolontaireDTO2);

        when(volontaireRepository.findAll()).thenReturn(volontaires);
        when(volontaireMapper.toDTOList(volontaires)).thenReturn(expectedDTOs);

        // When
        List<VolontaireDTO> result = volontaireService.getAllVolontaires();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testVolontaireDTO1, testVolontaireDTO2);

        verify(volontaireRepository, times(1)).findAll();
        verify(volontaireMapper, times(1)).toDTOList(volontaires);
    }

    @Test
    @DisplayName("getAllVolontaires() - Liste vide")
    void testGetAllVolontaires_EmptyList() {
        // Given
        List<Volontaire> emptyList = Arrays.asList();

        when(volontaireRepository.findAll()).thenReturn(emptyList);
        when(volontaireMapper.toDTOList(emptyList)).thenReturn(Arrays.asList());

        // When
        List<VolontaireDTO> result = volontaireService.getAllVolontaires();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(volontaireRepository, times(1)).findAll();
        verify(volontaireMapper, times(1)).toDTOList(emptyList);
    }

    // ===== TESTS GET ALL ACTIVE VOLONTAIRES =====

    @Test
    @DisplayName("getAllActiveVolontaires() - Récupération des volontaires actifs")
    void testGetAllActiveVolontaires_Success() {
        // Given
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1, testVolontaire2);

        when(volontaireRepository.findByArchive(false)).thenReturn(volontaires);
        when(volontaireMapper.toDTO(testVolontaire1)).thenReturn(testVolontaireDTO1);
        when(volontaireMapper.toDTO(testVolontaire2)).thenReturn(testVolontaireDTO2);

        // When
        List<VolontaireDTO> result = volontaireService.getAllActiveVolontaires();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testVolontaireDTO1, testVolontaireDTO2);

        verify(volontaireRepository, times(1)).findByArchive(false);
        verify(volontaireMapper, times(1)).toDTO(testVolontaire1);
        verify(volontaireMapper, times(1)).toDTO(testVolontaire2);
    }

    // ===== TESTS GET VOLONTAIRE BY ID =====

    @Test
    @DisplayName("getVolontaireById() - Volontaire trouvé")
    void testGetVolontaireById_Found() {
        // Given
        Integer id = 1;

        when(volontaireRepository.findById(id)).thenReturn(Optional.of(testVolontaire1));
        when(volontaireMapper.toDTO(testVolontaire1)).thenReturn(testVolontaireDTO1);

        // When
        Optional<VolontaireDTO> result = volontaireService.getVolontaireById(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testVolontaireDTO1);

        verify(volontaireRepository, times(1)).findById(id);
        verify(volontaireMapper, times(1)).toDTO(testVolontaire1);
    }

    @Test
    @DisplayName("getVolontaireById() - Volontaire non trouvé")
    void testGetVolontaireById_NotFound() {
        // Given
        Integer id = 999;

        when(volontaireRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<VolontaireDTO> result = volontaireService.getVolontaireById(id);

        // Then
        assertThat(result).isEmpty();

        verify(volontaireRepository, times(1)).findById(id);
        verify(volontaireMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("getVolontaireById() - ID null retourne empty")
    void testGetVolontaireById_NullId() {
        // When
        Optional<VolontaireDTO> result = volontaireService.getVolontaireById(null);

        // Then
        assertThat(result).isEmpty();

        verify(volontaireRepository, never()).findById(any());
        verify(volontaireMapper, never()).toDTO(any());
    }

    // ===== TESTS GET VOLONTAIRE DETAIL BY ID =====

    @Test
    @DisplayName("getVolontaireDetailById() - Volontaire trouvé")
    void testGetVolontaireDetailById_Found() {
        // Given
        Integer id = 1;

        when(volontaireRepository.findById(id)).thenReturn(Optional.of(testVolontaire1));
        when(volontaireMapper.toDetailDTO(testVolontaire1)).thenReturn(testVolontaireDetailDTO);

        // When
        Optional<VolontaireDetailDTO> result = volontaireService.getVolontaireDetailById(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testVolontaireDetailDTO);

        verify(volontaireRepository, times(1)).findById(id);
        verify(volontaireMapper, times(1)).toDetailDTO(testVolontaire1);
    }

    @Test
    @DisplayName("getVolontaireDetailById() - ID null retourne empty")
    void testGetVolontaireDetailById_NullId() {
        // When
        Optional<VolontaireDetailDTO> result = volontaireService.getVolontaireDetailById(null);

        // Then
        assertThat(result).isEmpty();

        verify(volontaireRepository, never()).findById(any());
        verify(volontaireMapper, never()).toDetailDTO(any());
    }

    // ===== TESTS SEARCH BY NOM/PRENOM =====

    @Test
    @DisplayName("searchVolontairesByNomPrenom() - Recherche par nom et prénom")
    void testSearchVolontairesByNomPrenom_BothParams() {
        // Given
        String nom = "Dupont";
        String prenom = "Jean";
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1);
        List<VolontaireDTO> expectedDTOs = Arrays.asList(testVolontaireDTO1);

        when(volontaireRepository.findByNomVolAndPrenomVol(nom, prenom)).thenReturn(volontaires);
        when(volontaireMapper.toDTOList(volontaires)).thenReturn(expectedDTOs);

        // When
        List<VolontaireDTO> result = volontaireService.searchVolontairesByNomPrenom(nom, prenom);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testVolontaireDTO1);

        verify(volontaireRepository, times(1)).findByNomVolAndPrenomVol(nom, prenom);
        verify(volontaireMapper, times(1)).toDTOList(volontaires);
    }

    @Test
    @DisplayName("searchVolontairesByNomPrenom() - Recherche par nom seulement")
    void testSearchVolontairesByNomPrenom_OnlyNom() {
        // Given
        String nom = "Dupont";
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1);
        List<VolontaireDTO> expectedDTOs = Arrays.asList(testVolontaireDTO1);

        when(volontaireRepository.findByNomVol(nom)).thenReturn(volontaires);
        when(volontaireMapper.toDTOList(volontaires)).thenReturn(expectedDTOs);

        // When
        List<VolontaireDTO> result = volontaireService.searchVolontairesByNomPrenom(nom, null);

        // Then
        assertThat(result).hasSize(1);

        verify(volontaireRepository, times(1)).findByNomVol(nom);
        verify(volontaireRepository, never()).findByNomVolAndPrenomVol(anyString(), anyString());
        verify(volontaireMapper, times(1)).toDTOList(volontaires);
    }

    @Test
    @DisplayName("searchVolontairesByNomPrenom() - Recherche par prénom seulement")
    void testSearchVolontairesByNomPrenom_OnlyPrenom() {
        // Given
        String prenom = "Jean";
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1);
        List<VolontaireDTO> expectedDTOs = Arrays.asList(testVolontaireDTO1);

        when(volontaireRepository.findByPrenomVol(prenom)).thenReturn(volontaires);
        when(volontaireMapper.toDTOList(volontaires)).thenReturn(expectedDTOs);

        // When
        List<VolontaireDTO> result = volontaireService.searchVolontairesByNomPrenom(null, prenom);

        // Then
        assertThat(result).hasSize(1);

        verify(volontaireRepository, times(1)).findByPrenomVol(prenom);
        verify(volontaireMapper, times(1)).toDTOList(volontaires);
    }

    @Test
    @DisplayName("searchVolontairesByNomPrenom() - Aucun paramètre retourne tous")
    void testSearchVolontairesByNomPrenom_NoParams() {
        // Given
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1, testVolontaire2);
        List<VolontaireDTO> expectedDTOs = Arrays.asList(testVolontaireDTO1, testVolontaireDTO2);

        when(volontaireRepository.findAll()).thenReturn(volontaires);
        when(volontaireMapper.toDTOList(volontaires)).thenReturn(expectedDTOs);

        // When
        List<VolontaireDTO> result = volontaireService.searchVolontairesByNomPrenom(null, null);

        // Then
        assertThat(result).hasSize(2);

        verify(volontaireRepository, times(1)).findAll();
        verify(volontaireMapper, times(1)).toDTOList(volontaires);
    }

    // ===== TESTS SEARCH WITH PAGINATION =====

    @Test
    @DisplayName("searchVolontaires() avec pagination - Recherche avec mot-clé")
    void testSearchVolontairesWithPagination_WithKeyword() {
        // Given
        String keyword = "Dupont";
        Pageable pageable = PageRequest.of(0, 10);
        String searchTerm = "%" + keyword + "%";
        
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1);
        Page<Volontaire> volontairesPage = new PageImpl<>(volontaires, pageable, 1);

        when(volontaireRepository.findByFullTextSearch(searchTerm, pageable)).thenReturn(volontairesPage);
        when(volontaireMapper.toDTO(testVolontaire1)).thenReturn(testVolontaireDTO1);

        // When
        Page<VolontaireDTO> result = volontaireService.searchVolontaires(keyword, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(volontaireRepository, times(1)).findByFullTextSearch(searchTerm, pageable);
    }

    @Test
    @DisplayName("searchVolontaires() avec pagination - Mot-clé vide retourne tous")
    void testSearchVolontairesWithPagination_EmptyKeyword() {
        // Given
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);
        
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1, testVolontaire2);
        Page<Volontaire> volontairesPage = new PageImpl<>(volontaires, pageable, 2);

        when(volontaireRepository.findAll(pageable)).thenReturn(volontairesPage);
        when(volontaireMapper.toDTO(any(Volontaire.class)))
                .thenReturn(testVolontaireDTO1, testVolontaireDTO2);

        // When
        Page<VolontaireDTO> result = volontaireService.searchVolontaires(keyword, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);

        verify(volontaireRepository, times(1)).findAll(pageable);
        verify(volontaireRepository, never()).findByFullTextSearch(anyString(), any());
    }

    @Test
    @DisplayName("searchVolontaires() avec pagination - Nettoyage des caractères spéciaux")
    void testSearchVolontairesWithPagination_SpecialCharactersRemoved() {
        // Given
        String keyword = "Dupont%_[]^";
        Pageable pageable = PageRequest.of(0, 10);
        String cleanedKeyword = "Dupont";
        String searchTerm = "%" + cleanedKeyword + "%";
        
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1);
        Page<Volontaire> volontairesPage = new PageImpl<>(volontaires, pageable, 1);

        when(volontaireRepository.findByFullTextSearch(searchTerm, pageable)).thenReturn(volontairesPage);
        when(volontaireMapper.toDTO(testVolontaire1)).thenReturn(testVolontaireDTO1);

        // When
        Page<VolontaireDTO> result = volontaireService.searchVolontaires(keyword, pageable);

        // Then
        assertThat(result).isNotNull();
        verify(volontaireRepository, times(1)).findByFullTextSearch(searchTerm, pageable);
    }

    // ===== TESTS SEARCH WITHOUT PAGINATION =====

    @Test
    @DisplayName("searchVolontaires() sans pagination - Recherche avec texte")
    void testSearchVolontairesWithoutPagination_WithText() {
        // Given
        String searchText = "Dupont";
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1);
        List<VolontaireDTO> expectedDTOs = Arrays.asList(testVolontaireDTO1);

        when(volontaireRepository.searchVolontaires(searchText)).thenReturn(volontaires);
        when(volontaireMapper.toDTOList(volontaires)).thenReturn(expectedDTOs);

        // When
        List<VolontaireDTO> result = volontaireService.searchVolontaires(searchText);

        // Then
        assertThat(result).hasSize(1);

        verify(volontaireRepository, times(1)).searchVolontaires(searchText);
        verify(volontaireMapper, times(1)).toDTOList(volontaires);
    }

    @Test
    @DisplayName("searchVolontaires() sans pagination - Texte vide retourne tous")
    void testSearchVolontairesWithoutPagination_EmptyText() {
        // Given
        String searchText = "";
        List<Volontaire> volontaires = Arrays.asList(testVolontaire1, testVolontaire2);
        List<VolontaireDTO> expectedDTOs = Arrays.asList(testVolontaireDTO1, testVolontaireDTO2);

        when(volontaireRepository.findAll()).thenReturn(volontaires);
        when(volontaireMapper.toDTOList(volontaires)).thenReturn(expectedDTOs);

        // When
        List<VolontaireDTO> result = volontaireService.searchVolontaires(searchText);

        // Then
        assertThat(result).hasSize(2);

        verify(volontaireRepository, times(1)).findAll();
        verify(volontaireRepository, never()).searchVolontaires(anyString());
        verify(volontaireMapper, times(1)).toDTOList(volontaires);
    }

    // ===== TESTS EXCEPTION HANDLING =====

    @Test
    @DisplayName("getAllVolontaires() - Exception dans le repository")
    void testGetAllVolontaires_RepositoryException() {
        // Given
        when(volontaireRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When/Then
        try {
            volontaireService.getAllVolontaires();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Database error");
        }

        verify(volontaireRepository, times(1)).findAll();
        verify(volontaireMapper, never()).toDTOList(any());
    }
}
