package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.mapper.RdvMapper;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Rdv;
import com.example.cosmetest.domain.model.RdvId;
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
 * Tests unitaires pour RdvServiceImpl
 * 
 * Ces tests vérifient la logique métier du service de gestion des rendez-vous
 * en mockant les dépendances (Repository et Mapper)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - RdvServiceImpl")
class RdvServiceImplTest {

    @Mock
    private RdvRepository rdvRepository;

    @Mock
    private RdvMapper rdvMapper;

    @InjectMocks
    private RdvServiceImpl rdvService;

    private Rdv testRdv1;
    private Rdv testRdv2;
    private RdvDTO testRdvDTO1;
    private RdvDTO testRdvDTO2;
    private RdvId testRdvId1;

    @BeforeEach
    void setUp() {
        // Préparer des IDs composites  
        testRdvId1 = new RdvId(101, 1);  // idEtude, idRdv

        // Préparer des rendez-vous de test
        testRdv1 = new Rdv();
        testRdv1.setId(testRdvId1);
        testRdv1.setIdVolontaire(1);
        testRdv1.setDate(Date.valueOf(LocalDate.of(2024, 6, 15)));
        testRdv1.setEtat("CONFIRME");

        testRdv2 = new Rdv();
        testRdv2.setId(new RdvId(102, 1));
        testRdv2.setIdVolontaire(2);
        testRdv2.setDate(Date.valueOf(LocalDate.of(2024, 6, 16)));
        testRdv2.setEtat("PLANIFIE");

        // Préparer des DTOs de test
        testRdvDTO1 = new RdvDTO();
        testRdvDTO1.setIdEtude(101);
        testRdvDTO1.setIdRdv(1);
        testRdvDTO1.setIdVolontaire(1);
        testRdvDTO1.setDate(Date.valueOf(LocalDate.of(2024, 6, 15)));

        testRdvDTO2 = new RdvDTO();
        testRdvDTO2.setIdEtude(102);
        testRdvDTO2.setIdRdv(1);
        testRdvDTO2.setIdVolontaire(2);
        testRdvDTO2.setDate(Date.valueOf(LocalDate.of(2024, 6, 16)));
    }

    // ===== TESTS GET ALL RDVS =====

    @Test
    @DisplayName("getAllRdvs() - Récupération de tous les rendez-vous")
    void testGetAllRdvs_Success() {
        // Given
        List<Rdv> rdvs = Arrays.asList(testRdv1, testRdv2);

        when(rdvRepository.findAll()).thenReturn(rdvs);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);
        when(rdvMapper.toDto(testRdv2)).thenReturn(testRdvDTO2);

        // When
        List<RdvDTO> result = rdvService.getAllRdvs();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testRdvDTO1, testRdvDTO2);

        verify(rdvRepository, times(1)).findAll();
        verify(rdvMapper, times(1)).toDto(testRdv1);
        verify(rdvMapper, times(1)).toDto(testRdv2);
    }

    @Test
    @DisplayName("getAllRdvs() - Liste vide")
    void testGetAllRdvs_EmptyList() {
        // Given
        when(rdvRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<RdvDTO> result = rdvService.getAllRdvs();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(rdvRepository, times(1)).findAll();
        verify(rdvMapper, never()).toDto(any());
    }

    // ===== TESTS GET ALL RDVS PAGINATED =====

    @Test
    @DisplayName("getAllRdvsPaginated() - Récupération paginée")
    void testGetAllRdvsPaginated_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Rdv> rdvs = Arrays.asList(testRdv1, testRdv2);
        Page<Rdv> rdvsPage = new PageImpl<>(rdvs, pageable, 2);

        when(rdvRepository.findAll(pageable)).thenReturn(rdvsPage);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);
        when(rdvMapper.toDto(testRdv2)).thenReturn(testRdvDTO2);

        // When
        Page<RdvDTO> result = rdvService.getAllRdvsPaginated(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);

        verify(rdvRepository, times(1)).findAll(pageable);
    }

    // ===== TESTS GET RDV BY ID =====

    @Test
    @DisplayName("getRdvById() - Rendez-vous trouvé")
    void testGetRdvById_Found() {
        // Given
        when(rdvRepository.findById(testRdvId1)).thenReturn(Optional.of(testRdv1));
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);

        // When
        Optional<RdvDTO> result = rdvService.getRdvById(testRdvId1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testRdvDTO1);

        verify(rdvRepository, times(1)).findById(testRdvId1);
        verify(rdvMapper, times(1)).toDto(testRdv1);
    }

    @Test
    @DisplayName("getRdvById() - Rendez-vous non trouvé")
    void testGetRdvById_NotFound() {
        // Given
        when(rdvRepository.findById(testRdvId1)).thenReturn(Optional.empty());

        // When
        Optional<RdvDTO> result = rdvService.getRdvById(testRdvId1);

        // Then
        assertThat(result).isEmpty();

        verify(rdvRepository, times(1)).findById(testRdvId1);
        verify(rdvMapper, never()).toDto(any());
    }

    // ===== TESTS GET RDVS BY VOLONTAIRE =====

    @Test
    @DisplayName("getRdvsByVolontaire() - Recherche par volontaire")
    void testGetRdvsByVolontaire_Success() {
        // Given
        Integer idVolontaire = 1;
        List<Rdv> rdvs = Arrays.asList(testRdv1);

        when(rdvRepository.findByIdVolontaire(idVolontaire)).thenReturn(rdvs);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);

        // When
        List<RdvDTO> result = rdvService.getRdvsByVolontaire(idVolontaire);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdVolontaire()).isEqualTo(idVolontaire);

        verify(rdvRepository, times(1)).findByIdVolontaire(idVolontaire);
        verify(rdvMapper, times(1)).toDto(testRdv1);
    }

    @Test
    @DisplayName("getRdvsByVolontaire() - Aucun rendez-vous trouvé")
    void testGetRdvsByVolontaire_NoResults() {
        // Given
        Integer idVolontaire = 999;

        when(rdvRepository.findByIdVolontaire(idVolontaire)).thenReturn(Arrays.asList());

        // When
        List<RdvDTO> result = rdvService.getRdvsByVolontaire(idVolontaire);

        // Then
        assertThat(result).isEmpty();

        verify(rdvRepository, times(1)).findByIdVolontaire(idVolontaire);
        verify(rdvMapper, never()).toDto(any());
    }

    // ===== TESTS GET RDVS BY DATE =====

    @Test
    @DisplayName("getRdvsByDate() - Recherche par date")
    void testGetRdvsByDate_Success() {
        // Given
        Date date = Date.valueOf(LocalDate.of(2024, 6, 15));
        List<Rdv> rdvs = Arrays.asList(testRdv1);

        when(rdvRepository.findByDate(date)).thenReturn(rdvs);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);

        // When
        List<RdvDTO> result = rdvService.getRdvsByDate(date);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testRdvDTO1);

        verify(rdvRepository, times(1)).findByDate(date);
        verify(rdvMapper, times(1)).toDto(testRdv1);
    }

    // ===== TESTS GET RDVS BY VOLONTAIRE AND DATE =====

    @Test
    @DisplayName("getRdvsByVolontaireAndDate() - Recherche par volontaire et date")
    void testGetRdvsByVolontaireAndDate_Success() {
        // Given
        Integer idVolontaire = 1;
        Date date = Date.valueOf(LocalDate.of(2024, 6, 15));
        List<Rdv> rdvs = Arrays.asList(testRdv1);

        when(rdvRepository.findByIdVolontaireAndDate(idVolontaire, date)).thenReturn(rdvs);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);

        // When
        List<RdvDTO> result = rdvService.getRdvsByVolontaireAndDate(idVolontaire, date);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testRdvDTO1);

        verify(rdvRepository, times(1)).findByIdVolontaireAndDate(idVolontaire, date);
    }

    // ===== TESTS GET RDVS BY VOLONTAIRE AND DATE RANGE =====

    @Test
    @DisplayName("getRdvsByVolontaireAndDateRange() - Recherche par plage de dates")
    void testGetRdvsByVolontaireAndDateRange_Success() {
        // Given
        Integer idVolontaire = 1;
        Date startDate = Date.valueOf(LocalDate.of(2024, 6, 1));
        Date endDate = Date.valueOf(LocalDate.of(2024, 6, 30));
        List<Rdv> rdvs = Arrays.asList(testRdv1);

        when(rdvRepository.findByVolontaireAndDateRange(idVolontaire, startDate, endDate))
                .thenReturn(rdvs);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);

        // When
        List<RdvDTO> result = rdvService.getRdvsByVolontaireAndDateRange(idVolontaire, startDate, endDate);

        // Then
        assertThat(result).hasSize(1);

        verify(rdvRepository, times(1)).findByVolontaireAndDateRange(idVolontaire, startDate, endDate);
    }

    // ===== TESTS GET RDVS BY GROUPE =====

    @Test
    @DisplayName("getRdvsByGroupe() - Recherche par groupe")
    void testGetRdvsByGroupe_Success() {
        // Given
        Integer idGroupe = 10;
        List<Rdv> rdvs = Arrays.asList(testRdv1, testRdv2);

        when(rdvRepository.findByIdGroupe(idGroupe)).thenReturn(rdvs);
        when(rdvMapper.toDto(any(Rdv.class)))
                .thenReturn(testRdvDTO1, testRdvDTO2);

        // When
        List<RdvDTO> result = rdvService.getRdvsByGroupe(idGroupe);

        // Then
        assertThat(result).hasSize(2);

        verify(rdvRepository, times(1)).findByIdGroupe(idGroupe);
    }

    // ===== TESTS GET RDVS BY ETAT =====

    @Test
    @DisplayName("getRdvsByEtat() - Recherche par état")
    void testGetRdvsByEtat_Success() {
        // Given
        String etat = "CONFIRME";
        List<Rdv> rdvs = Arrays.asList(testRdv1);

        when(rdvRepository.findByEtat(etat)).thenReturn(rdvs);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);

        // When
        List<RdvDTO> result = rdvService.getRdvsByEtat(etat);

        // Then
        assertThat(result).hasSize(1);

        verify(rdvRepository, times(1)).findByEtat(etat);
        verify(rdvMapper, times(1)).toDto(testRdv1);
    }

    // ===== TESTS SAVE RDV =====

    @Test
    @DisplayName("saveRdv() - Sauvegarde réussie")
    void testSaveRdv_Success() {
        // Given
        Rdv rdvToSave = new Rdv();
        rdvToSave.setId(testRdvId1);
        rdvToSave.setIdVolontaire(1);
        rdvToSave.setDate(Date.valueOf(LocalDate.of(2024, 6, 15)));
        rdvToSave.setEtat("CONFIRME");
        
        when(rdvMapper.toEntity(testRdvDTO1)).thenReturn(rdvToSave);
        when(rdvRepository.save(rdvToSave)).thenReturn(rdvToSave);
        when(rdvMapper.toDto(rdvToSave)).thenReturn(testRdvDTO1);

        // When
        RdvDTO result = rdvService.saveRdv(testRdvDTO1);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testRdvDTO1);

        verify(rdvMapper, times(1)).toEntity(testRdvDTO1);
        verify(rdvRepository, times(1)).save(rdvToSave);
        verify(rdvMapper, times(1)).toDto(rdvToSave);
    }

    // ===== TESTS DELETE RDV =====

    @Test
    @DisplayName("deleteRdv() - Suppression réussie")
    void testDeleteRdv_Success() {
        // Given
        when(rdvRepository.existsById(testRdvId1)).thenReturn(true);

        // When
        rdvService.deleteRdv(testRdvId1);

        // Then
        verify(rdvRepository, times(1)).existsById(testRdvId1);
        verify(rdvRepository, times(1)).deleteById(testRdvId1);
    }

    @Test
    @DisplayName("deleteRdv() - Rendez-vous inexistant lève une exception")
    void testDeleteRdv_NotFound() {
        // Given
        when(rdvRepository.existsById(testRdvId1)).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> rdvService.deleteRdv(testRdvId1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("n'existe pas");

        verify(rdvRepository, times(1)).existsById(testRdvId1);
        verify(rdvRepository, never()).deleteById(any());
    }

    // ===== TESTS SEARCH RDVS BY COMMENTAIRES =====

    @Test
    @DisplayName("searchRdvsByCommentaires() - Recherche par mot-clé")
    void testSearchRdvsByCommentaires_Success() {
        // Given
        String keyword = "important";
        List<Rdv> rdvs = Arrays.asList(testRdv1);

        when(rdvRepository.findByCommentairesContaining(keyword)).thenReturn(rdvs);
        when(rdvMapper.toDto(testRdv1)).thenReturn(testRdvDTO1);

        // When
        List<RdvDTO> result = rdvService.searchRdvsByCommentaires(keyword);

        // Then
        assertThat(result).hasSize(1);

        verify(rdvRepository, times(1)).findByCommentairesContaining(keyword);
        verify(rdvMapper, times(1)).toDto(testRdv1);
    }

    // ===== TESTS EXCEPTION HANDLING =====

    @Test
    @DisplayName("getAllRdvs() - Exception dans le repository")
    void testGetAllRdvs_RepositoryException() {
        // Given
        when(rdvRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When/Then
        assertThatThrownBy(() -> rdvService.getAllRdvs())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(rdvRepository, times(1)).findAll();
        verify(rdvMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("saveRdv() - Exception lors de la sauvegarde")
    void testSaveRdv_SaveException() {
        // Given
        Rdv rdvToSave = new Rdv();
        rdvToSave.setId(testRdvId1);
        rdvToSave.setIdVolontaire(1);
        rdvToSave.setDate(Date.valueOf(LocalDate.of(2024, 6, 15)));
        rdvToSave.setEtat("CONFIRME");
        
        when(rdvMapper.toEntity(testRdvDTO1)).thenReturn(rdvToSave);
        when(rdvRepository.save(rdvToSave))
                .thenThrow(new RuntimeException("Save failed"));

        // When/Then
        assertThatThrownBy(() -> rdvService.saveRdv(testRdvDTO1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Save failed");

        verify(rdvMapper, times(1)).toEntity(testRdvDTO1);
        verify(rdvRepository, times(1)).save(rdvToSave);
    }
}
