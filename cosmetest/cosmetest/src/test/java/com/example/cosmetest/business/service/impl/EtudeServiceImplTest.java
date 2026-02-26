package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.EtudeDTO;
import com.example.cosmetest.business.mapper.EtudeMapper;
import com.example.cosmetest.data.repository.EtudeRepository;
import com.example.cosmetest.domain.model.Etude;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour EtudeServiceImpl
 * 
 * Ces tests vérifient la logique métier du service de gestion des études
 * en mockant les dépendances (Repository et Mapper)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - EtudeServiceImpl")
class EtudeServiceImplTest {

    @Mock
    private EtudeRepository etudeRepository;

    @Mock
    private EtudeMapper etudeMapper;

    @InjectMocks
    private EtudeServiceImpl etudeService;

    private Etude testEtude1;
    private Etude testEtude2;
    private EtudeDTO testEtudeDTO1;
    private EtudeDTO testEtudeDTO2;

    @BeforeEach
    void setUp() {
        // Préparer des études de test
        testEtude1 = new Etude();
        testEtude1.setIdEtude(1);
        testEtude1.setRef("ETU001");
        testEtude1.setTitre("Étude Cosmétique A");
        testEtude1.setType("Cosmetique");
        testEtude1.setPaye(0);

        testEtude2 = new Etude();
        testEtude2.setIdEtude(2);
        testEtude2.setRef("ETU002");
        testEtude2.setTitre("Étude Pharmaceutique B");
        testEtude2.setType("Pharmaceutique");
        testEtude2.setPaye(2);

        // Préparer des DTOs de test
        testEtudeDTO1 = new EtudeDTO();
        testEtudeDTO1.setIdEtude(1);
        testEtudeDTO1.setRef("ETU001");
        testEtudeDTO1.setTitre("Étude Cosmétique A");

        testEtudeDTO2 = new EtudeDTO();
        testEtudeDTO2.setIdEtude(2);
        testEtudeDTO2.setRef("ETU002");
        testEtudeDTO2.setTitre("Étude Pharmaceutique B");
    }

    // ===== TESTS GET ALL ETUDES =====

    @Test
    @DisplayName("getAllEtudes() - Récupération de toutes les études")
    void testGetAllEtudes_Success() {
        // Given
        List<Etude> etudes = Arrays.asList(testEtude1, testEtude2);

        when(etudeRepository.findByArchiveFalse()).thenReturn(etudes);
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);
        when(etudeMapper.toDto(testEtude2)).thenReturn(testEtudeDTO2);

        // When
        List<EtudeDTO> result = etudeService.getAllEtudes();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testEtudeDTO1, testEtudeDTO2);

        verify(etudeRepository, times(1)).findByArchiveFalse();
        verify(etudeMapper, times(1)).toDto(testEtude1);
        verify(etudeMapper, times(1)).toDto(testEtude2);
    }

    @Test
    @DisplayName("getAllEtudes() - Liste vide")
    void testGetAllEtudes_EmptyList() {
        // Given
        when(etudeRepository.findByArchiveFalse()).thenReturn(Arrays.asList());

        // When
        List<EtudeDTO> result = etudeService.getAllEtudes();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(etudeRepository, times(1)).findByArchiveFalse();
        verify(etudeMapper, never()).toDto(any());
    }

    // ===== TESTS GET ALL ETUDES PAGINATED =====

    @Test
    @DisplayName("getAllEtudesPaginated() - Récupération paginée")
    void testGetAllEtudesPaginated_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Etude> etudes = Arrays.asList(testEtude1, testEtude2);
        Page<Etude> etudesPage = new PageImpl<>(etudes, pageable, 2);

        when(etudeRepository.findByArchiveFalse(pageable)).thenReturn(etudesPage);
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);
        when(etudeMapper.toDto(testEtude2)).thenReturn(testEtudeDTO2);

        // When
        Page<EtudeDTO> result = etudeService.getAllEtudesPaginated(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);

        verify(etudeRepository, times(1)).findByArchiveFalse(pageable);
    }

    // ===== TESTS GET ETUDE BY ID =====

    @Test
    @DisplayName("getEtudeById() - Étude trouvée")
    void testGetEtudeById_Found() {
        // Given
        Integer id = 1;

        when(etudeRepository.findById(id)).thenReturn(Optional.of(testEtude1));
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);

        // When
        Optional<EtudeDTO> result = etudeService.getEtudeById(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testEtudeDTO1);

        verify(etudeRepository, times(1)).findById(id);
        verify(etudeMapper, times(1)).toDto(testEtude1);
    }

    @Test
    @DisplayName("getEtudeById() - Étude non trouvée")
    void testGetEtudeById_NotFound() {
        // Given
        Integer id = 999;

        when(etudeRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<EtudeDTO> result = etudeService.getEtudeById(id);

        // Then
        assertThat(result).isEmpty();

        verify(etudeRepository, times(1)).findById(id);
        verify(etudeMapper, never()).toDto(any());
    }

    // ===== TESTS GET ETUDE BY REF =====

    @Test
    @DisplayName("getEtudeByRef() - Étude trouvée par référence")
    void testGetEtudeByRef_Found() {
        // Given
        String ref = "ETU001";

        when(etudeRepository.findByRef(ref)).thenReturn(Optional.of(testEtude1));
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);

        // When
        Optional<EtudeDTO> result = etudeService.getEtudeByRef(ref);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getRef()).isEqualTo(ref);

        verify(etudeRepository, times(1)).findByRef(ref);
        verify(etudeMapper, times(1)).toDto(testEtude1);
    }

    @Test
    @DisplayName("getEtudeByRef() - Référence non trouvée")
    void testGetEtudeByRef_NotFound() {
        // Given
        String ref = "ETU999";

        when(etudeRepository.findByRef(ref)).thenReturn(Optional.empty());

        // When
        Optional<EtudeDTO> result = etudeService.getEtudeByRef(ref);

        // Then
        assertThat(result).isEmpty();

        verify(etudeRepository, times(1)).findByRef(ref);
        verify(etudeMapper, never()).toDto(any());
    }

    // ===== TESTS GET ETUDES BY TYPE =====

    @Test
    @DisplayName("getEtudesByType() - Recherche par type")
    void testGetEtudesByType_Success() {
        // Given
        String type = "Cosmetique";
        List<Etude> etudes = Arrays.asList(testEtude1);

        when(etudeRepository.findByType(type)).thenReturn(etudes);
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);

        // When
        List<EtudeDTO> result = etudeService.getEtudesByType(type);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testEtudeDTO1);

        verify(etudeRepository, times(1)).findByType(type);
        verify(etudeMapper, times(1)).toDto(testEtude1);
    }

    @Test
    @DisplayName("getEtudesByType() - Aucun résultat")
    void testGetEtudesByType_NoResults() {
        // Given
        String type = "Inexistant";

        when(etudeRepository.findByType(type)).thenReturn(Arrays.asList());

        // When
        List<EtudeDTO> result = etudeService.getEtudesByType(type);

        // Then
        assertThat(result).isEmpty();

        verify(etudeRepository, times(1)).findByType(type);
        verify(etudeMapper, never()).toDto(any());
    }

    // ===== TESTS GET ETUDES BY TITRE =====

    @Test
    @DisplayName("getEtudesByTitre() - Recherche par mot-clé dans titre")
    void testGetEtudesByTitre_Success() {
        // Given
        String keyword = "Cosmétique";
        List<Etude> etudes = Arrays.asList(testEtude1);

        when(etudeRepository.findByTitreContaining(keyword)).thenReturn(etudes);
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);

        // When
        List<EtudeDTO> result = etudeService.getEtudesByTitre(keyword);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitre()).contains(keyword);

        verify(etudeRepository, times(1)).findByTitreContaining(keyword);
        verify(etudeMapper, times(1)).toDto(testEtude1);
    }

    // ===== TESTS GET ETUDES BY DATE RANGE =====

    @Test
    @DisplayName("getEtudesByDateRange() - Recherche par plage de dates valide")
    void testGetEtudesByDateRange_ValidRange() {
        // Given
        Date debut = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date fin = Date.valueOf(LocalDate.of(2024, 12, 31));
        List<Etude> etudes = Arrays.asList(testEtude1, testEtude2);

        when(etudeRepository.findByDateDebutAndDateFin(debut, fin)).thenReturn(etudes);
        when(etudeMapper.toDto(any(Etude.class)))
                .thenReturn(testEtudeDTO1, testEtudeDTO2);

        // When
        List<EtudeDTO> result = etudeService.getEtudesByDateRange(debut, fin);

        // Then
        assertThat(result).hasSize(2);

        verify(etudeRepository, times(1)).findByDateDebutAndDateFin(debut, fin);
    }

    @Test
    @DisplayName("getEtudesByDateRange() - Date de début nulle lève une exception")
    void testGetEtudesByDateRange_NullStartDate() {
        // Given
        Date debut = null;
        Date fin = Date.valueOf(LocalDate.of(2024, 12, 31));

        // When/Then
        assertThatThrownBy(() -> etudeService.getEtudesByDateRange(debut, fin))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date");

        verify(etudeRepository, never()).findByDateDebutAndDateFin(any(), any());
    }

    @Test
    @DisplayName("getEtudesByDateRange() - Date de fin nulle lève une exception")
    void testGetEtudesByDateRange_NullEndDate() {
        // Given
        Date debut = Date.valueOf(LocalDate.of(2024, 1, 1));
        Date fin = null;

        // When/Then
        assertThatThrownBy(() -> etudeService.getEtudesByDateRange(debut, fin))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date");

        verify(etudeRepository, never()).findByDateDebutAndDateFin(any(), any());
    }

    @Test
    @DisplayName("getEtudesByDateRange() - Date de début après date de fin")
    void testGetEtudesByDateRange_InvalidRange() {
        // Given
        Date debut = Date.valueOf(LocalDate.of(2024, 12, 31));
        Date fin = Date.valueOf(LocalDate.of(2024, 1, 1));

        // When/Then
        assertThatThrownBy(() -> etudeService.getEtudesByDateRange(debut, fin))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("début");

        verify(etudeRepository, never()).findByDateDebutAndDateFin(any(), any());
    }

    // ===== TESTS GET ACTIVE ETUDES AT DATE =====

    @Test
    @DisplayName("getActiveEtudesAtDate() - Études actives à une date")
    void testGetActiveEtudesAtDate_Success() {
        // Given
        Date date = Date.valueOf(LocalDate.of(2024, 6, 15));
        List<Etude> etudes = Arrays.asList(testEtude1, testEtude2);

        when(etudeRepository.findActiveEtudesAtDate(date.toLocalDate())).thenReturn(etudes);
        when(etudeMapper.toDto(any(Etude.class)))
                .thenReturn(testEtudeDTO1, testEtudeDTO2);

        // When
        List<EtudeDTO> result = etudeService.getActiveEtudesAtDate(date);

        // Then
        assertThat(result).hasSize(2);

        verify(etudeRepository, times(1)).findActiveEtudesAtDate(date.toLocalDate());
    }

    @Test
    @DisplayName("getActiveEtudesAtDate() - Date nulle lève une exception")
    void testGetActiveEtudesAtDate_NullDate() {
        // When/Then
        assertThatThrownBy(() -> etudeService.getActiveEtudesAtDate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date");

        verify(etudeRepository, never()).findActiveEtudesAtDate(any());
    }

    // ===== TESTS GET ETUDES BY PAYE =====

    @Test
    @DisplayName("getEtudesByPaye() - Recherche des études non payées (paye=0)")
    void testGetEtudesByPaye_Unpaid() {
        // Given
        int paye = 0;
        List<Etude> etudes = Arrays.asList(testEtude1);

        when(etudeRepository.findByPaye(paye)).thenReturn(etudes);
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);

        // When
        List<EtudeDTO> result = etudeService.getEtudesByPaye(paye);

        // Then
        assertThat(result).hasSize(1);

        verify(etudeRepository, times(1)).findByPaye(paye);
        verify(etudeMapper, times(1)).toDto(testEtude1);
    }

    @Test
    @DisplayName("getEtudesByPaye() - Recherche des études payées (paye=2)")
    void testGetEtudesByPaye_Paid() {
        // Given
        int paye = 2;
        List<Etude> etudes = Arrays.asList(testEtude2);

        when(etudeRepository.findByPaye(paye)).thenReturn(etudes);
        when(etudeMapper.toDto(testEtude2)).thenReturn(testEtudeDTO2);

        // When
        List<EtudeDTO> result = etudeService.getEtudesByPaye(paye);

        // Then
        assertThat(result).hasSize(1);

        verify(etudeRepository, times(1)).findByPaye(paye);
    }

    @Test
    @DisplayName("getEtudesByPaye() - Valeur invalide lève une exception")
    void testGetEtudesByPaye_InvalidValue() {
        // Given
        int paye = 1; // Valeur invalide

        // When/Then
        assertThatThrownBy(() -> etudeService.getEtudesByPaye(paye))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("paye")
                .hasMessageContaining("0 ou 2");

        verify(etudeRepository, never()).findByPaye(anyInt());
    }

    // ===== TESTS SEARCH ETUDES =====

    @Test
    @DisplayName("searchEtudes() - Recherche avec terme valide")
    void testSearchEtudes_ValidTerm() {
        // Given
        String searchTerm = "Cosmétique";
        List<Etude> etudes = Arrays.asList(testEtude1);

        when(etudeRepository.searchByTitreOrCommentairesOrRef(searchTerm)).thenReturn(etudes);
        when(etudeMapper.toDto(testEtude1)).thenReturn(testEtudeDTO1);

        // When
        List<EtudeDTO> result = etudeService.searchEtudes(searchTerm);

        // Then
        assertThat(result).hasSize(1);

        verify(etudeRepository, times(1)).searchByTitreOrCommentairesOrRef(searchTerm);
        verify(etudeMapper, times(1)).toDto(testEtude1);
    }

    @Test
    @DisplayName("searchEtudes() - Terme vide lève une exception")
    void testSearchEtudes_EmptyTerm() {
        // Given
        String searchTerm = "";

        // When/Then
        assertThatThrownBy(() -> etudeService.searchEtudes(searchTerm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("recherche");

        verify(etudeRepository, never()).searchByTitreOrCommentairesOrRef(anyString());
    }

    @Test
    @DisplayName("searchEtudes() - Terme null lève une exception")
    void testSearchEtudes_NullTerm() {
        // Given
        String searchTerm = null;

        // When/Then
        assertThatThrownBy(() -> etudeService.searchEtudes(searchTerm))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("recherche");

        verify(etudeRepository, never()).searchByTitreOrCommentairesOrRef(anyString());
    }

    // ===== TESTS EXCEPTION HANDLING =====

    @Test
    @DisplayName("getAllEtudes() - Exception dans le repository")
    void testGetAllEtudes_RepositoryException() {
        // Given
        when(etudeRepository.findByArchiveFalse()).thenThrow(new RuntimeException("Database error"));

        // When/Then
        assertThatThrownBy(() -> etudeService.getAllEtudes())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(etudeRepository, times(1)).findByArchiveFalse();
        verify(etudeMapper, never()).toDto(any());
    }
}
