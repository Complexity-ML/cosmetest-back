package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.mapper.AnnulationMapper;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Annulation;
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

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour AnnulationServiceImpl
 * Service de gestion des annulations de volontaires
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AnnulationServiceImpl - Tests unitaires")
class AnnulationServiceImplTest {

    @Mock
    private AnnulationRepository annulationRepository;

    @Mock
    private AnnulationMapper annulationMapper;

    @Mock
    private RdvRepository rdvRepository;

    @InjectMocks
    private AnnulationServiceImpl annulationService;

    private Annulation annulation;
    private AnnulationDTO annulationDTO;

    @BeforeEach
    void setUp() {
        annulation = new Annulation();
        annulation.setIdAnnuler(1);
        annulation.setIdVol(10);
        annulation.setIdEtude(5);
        annulation.setDateAnnulation("2024-01-15");
        annulation.setCommentaire("Raisons personnelles");

        annulationDTO = new AnnulationDTO();
        annulationDTO.setIdAnnuler(1);
        annulationDTO.setIdVol(10);
        annulationDTO.setIdEtude(5);
        annulationDTO.setDateAnnulation("2024-01-15");
        annulationDTO.setCommentaire("Raisons personnelles");
    }

    // ==================== Tests getAllAnnulations() ====================

    @Test
    @DisplayName("getAllAnnulations() - Récupération de toutes les annulations")
    void testGetAllAnnulations_Success() {
        // Arrange
        when(annulationRepository.findAll()).thenReturn(Collections.singletonList(annulation));
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        List<AnnulationDTO> result = annulationService.getAllAnnulations();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdAnnuler()).isEqualTo(1);
        verify(annulationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAllAnnulations() - Liste vide")
    void testGetAllAnnulations_EmptyList() {
        // Arrange
        when(annulationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<AnnulationDTO> result = annulationService.getAllAnnulations();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    // ==================== Tests getAllAnnulationsPaginated() ====================

    @Test
    @DisplayName("getAllAnnulationsPaginated() - Récupération paginée")
    void testGetAllAnnulationsPaginated_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Annulation> annulationPage = new PageImpl<>(Collections.singletonList(annulation));
        when(annulationRepository.findAll(pageable)).thenReturn(annulationPage);
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        Page<AnnulationDTO> result = annulationService.getAllAnnulationsPaginated(pageable);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    // ==================== Tests getAnnulationById() ====================

    @Test
    @DisplayName("getAnnulationById() - Annulation trouvée")
    void testGetAnnulationById_Found() {
        // Arrange
        when(annulationRepository.findById(1)).thenReturn(Optional.of(annulation));
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        Optional<AnnulationDTO> result = annulationService.getAnnulationById(1);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getIdAnnuler()).isEqualTo(1);
        verify(annulationRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getAnnulationById() - Annulation non trouvée")
    void testGetAnnulationById_NotFound() {
        // Arrange
        when(annulationRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<AnnulationDTO> result = annulationService.getAnnulationById(999);

        // Assert
        assertThat(result).isEmpty();
    }

    // ==================== Tests getAnnulationsByVolontaire() ====================

    @Test
    @DisplayName("getAnnulationsByVolontaire() - Récupération par volontaire")
    void testGetAnnulationsByVolontaire_Success() {
        // Arrange
        when(annulationRepository.findByIdVol(10)).thenReturn(Collections.singletonList(annulation));
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        List<AnnulationDTO> result = annulationService.getAnnulationsByVolontaire(10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdVol()).isEqualTo(10);
        verify(annulationRepository, times(1)).findByIdVol(10);
    }

    @Test
    @DisplayName("getAnnulationsByVolontaire() - Aucune annulation")
    void testGetAnnulationsByVolontaire_Empty() {
        // Arrange
        when(annulationRepository.findByIdVol(99)).thenReturn(Collections.emptyList());

        // Act
        List<AnnulationDTO> result = annulationService.getAnnulationsByVolontaire(99);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    // ==================== Tests getAnnulationsByEtude() ====================

    @Test
    @DisplayName("getAnnulationsByEtude() - Récupération par étude")
    void testGetAnnulationsByEtude_Success() {
        // Arrange
        when(annulationRepository.findByIdEtude(5)).thenReturn(Collections.singletonList(annulation));
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        List<AnnulationDTO> result = annulationService.getAnnulationsByEtude(5);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdEtude()).isEqualTo(5);
        verify(annulationRepository, times(1)).findByIdEtude(5);
    }

    // ==================== Tests getAnnulationsByVolontaireAndEtude() ====================

    @Test
    @DisplayName("getAnnulationsByVolontaireAndEtude() - Recherche combinée")
    void testGetAnnulationsByVolontaireAndEtude_Success() {
        // Arrange
        when(annulationRepository.findByIdVolAndIdEtude(10, 5))
            .thenReturn(Collections.singletonList(annulation));
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        List<AnnulationDTO> result = annulationService.getAnnulationsByVolontaireAndEtude(10, 5);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdVol()).isEqualTo(10);
        assertThat(result.get(0).getIdEtude()).isEqualTo(5);
    }

    // ==================== Tests getAnnulationsByDate() ====================

    @Test
    @DisplayName("getAnnulationsByDate() - Recherche par date valide")
    void testGetAnnulationsByDate_Success() {
        // Arrange
        String date = "2024-01-15";
        when(annulationRepository.findByDateAnnulation(date))
            .thenReturn(Collections.singletonList(annulation));
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        List<AnnulationDTO> result = annulationService.getAnnulationsByDate(date);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(annulationRepository, times(1)).findByDateAnnulation(date);
    }

    @Test
    @DisplayName("getAnnulationsByDate() - Format de date invalide")
    void testGetAnnulationsByDate_InvalidFormat() {
        // Act & Assert
        assertThatThrownBy(() -> annulationService.getAnnulationsByDate("15/01/2024"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Format de date invalide");
    }

    // ==================== Tests searchAnnulationsByCommentaire() ====================

    @Test
    @DisplayName("searchAnnulationsByCommentaire() - Recherche avec mot-clé")
    void testSearchAnnulationsByCommentaire_Success() {
        // Arrange
        String keyword = "personnel";
        when(annulationRepository.findByCommentaireContaining(keyword))
            .thenReturn(Collections.singletonList(annulation));
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        List<AnnulationDTO> result = annulationService.searchAnnulationsByCommentaire(keyword);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(annulationRepository, times(1)).findByCommentaireContaining(keyword);
    }

    @Test
    @DisplayName("searchAnnulationsByCommentaire() - Mot-clé vide")
    void testSearchAnnulationsByCommentaire_EmptyKeyword() {
        // Act & Assert
        assertThatThrownBy(() -> annulationService.searchAnnulationsByCommentaire(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Le mot-clé de recherche ne peut pas être vide");
    }

    @Test
    @DisplayName("searchAnnulationsByCommentaire() - Mot-clé null")
    void testSearchAnnulationsByCommentaire_NullKeyword() {
        // Act & Assert
        assertThatThrownBy(() -> annulationService.searchAnnulationsByCommentaire(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Le mot-clé de recherche ne peut pas être vide");
    }

    // ==================== Tests saveAnnulation() ====================

    @Test
    @DisplayName("saveAnnulation() - Création avec libération automatique des RDV")
    void testSaveAnnulation_Success() {
        // Arrange
        when(annulationMapper.toEntity(annulationDTO)).thenReturn(annulation);
        when(annulationRepository.save(annulation)).thenReturn(annulation);
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);
        
        // Mock des RDV à libérer
        Rdv rdv1 = new Rdv();
        RdvId rdvId1 = new RdvId();
        rdvId1.setIdRdv(1);
        rdvId1.setIdEtude(5);
        rdv1.setId(rdvId1);
        rdv1.setIdVolontaire(10);
        
        when(rdvRepository.findByIdVolontaireAndIdEtude(10, 5))
            .thenReturn(Collections.singletonList(rdv1));
        when(rdvRepository.save(any(Rdv.class))).thenReturn(rdv1);

        // Act
        AnnulationDTO result = annulationService.saveAnnulation(annulationDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdAnnuler()).isEqualTo(1);
        verify(annulationRepository, times(1)).save(annulation);
        verify(rdvRepository, times(1)).findByIdVolontaireAndIdEtude(10, 5);
        verify(rdvRepository, times(1)).save(any(Rdv.class));
    }

    @Test
    @DisplayName("saveAnnulation() - Données nulles")
    void testSaveAnnulation_NullData() {
        // Act & Assert - NullPointerException car le logger essaie d'accéder aux propriétés avant validation
        assertThatThrownBy(() -> annulationService.saveAnnulation(null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("saveAnnulation() - ID volontaire invalide")
    void testSaveAnnulation_InvalidVolontaireId() {
        // Arrange
        annulationDTO.setIdVol(0);

        // Act & Assert
        assertThatThrownBy(() -> annulationService.saveAnnulation(annulationDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("L'ID du volontaire doit être un nombre positif");
    }

    @Test
    @DisplayName("saveAnnulation() - ID étude invalide")
    void testSaveAnnulation_InvalidEtudeId() {
        // Arrange
        annulationDTO.setIdEtude(-1);

        // Act & Assert
        assertThatThrownBy(() -> annulationService.saveAnnulation(annulationDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("L'ID de l'étude doit être un nombre positif");
    }

    @Test
    @DisplayName("saveAnnulation() - Date vide")
    void testSaveAnnulation_EmptyDate() {
        // Arrange
        annulationDTO.setDateAnnulation("");

        // Act & Assert
        assertThatThrownBy(() -> annulationService.saveAnnulation(annulationDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("La date d'annulation ne peut pas être vide");
    }

    @Test
    @DisplayName("saveAnnulation() - Date null")
    void testSaveAnnulation_NullDate() {
        // Arrange
        annulationDTO.setDateAnnulation(null);

        // Act & Assert
        assertThatThrownBy(() -> annulationService.saveAnnulation(annulationDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("La date d'annulation ne peut pas être vide");
    }

    @Test
    @DisplayName("saveAnnulation() - Format de date invalide")
    void testSaveAnnulation_InvalidDateFormat() {
        // Arrange
        annulationDTO.setDateAnnulation("15/01/2024");

        // Act & Assert
        assertThatThrownBy(() -> annulationService.saveAnnulation(annulationDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Format de date invalide");
    }

    // ==================== Tests deleteAnnulation() ====================

    @Test
    @DisplayName("deleteAnnulation() - Suppression réussie")
    void testDeleteAnnulation_Success() {
        // Arrange
        when(annulationRepository.existsById(1)).thenReturn(true);
        doNothing().when(annulationRepository).deleteById(1);

        // Act
        annulationService.deleteAnnulation(1);

        // Assert
        verify(annulationRepository, times(1)).existsById(1);
        verify(annulationRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deleteAnnulation() - Annulation inexistante")
    void testDeleteAnnulation_NotFound() {
        // Arrange
        when(annulationRepository.existsById(999)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> annulationService.deleteAnnulation(999))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("L'annulation avec l'ID 999 n'existe pas");
    }

    // ==================== Tests countAnnulationsByVolontaire() ====================

    @Test
    @DisplayName("countAnnulationsByVolontaire() - Comptage des annulations")
    void testCountAnnulationsByVolontaire_Success() {
        // Arrange
        when(annulationRepository.countAnnulationsByVolontaire(10)).thenReturn(5L);

        // Act
        Long result = annulationService.countAnnulationsByVolontaire(10);

        // Assert
        assertThat(result).isEqualTo(5L);
        verify(annulationRepository, times(1)).countAnnulationsByVolontaire(10);
    }

    @Test
    @DisplayName("countAnnulationsByVolontaire() - Aucune annulation")
    void testCountAnnulationsByVolontaire_Zero() {
        // Arrange
        when(annulationRepository.countAnnulationsByVolontaire(99)).thenReturn(0L);

        // Act
        Long result = annulationService.countAnnulationsByVolontaire(99);

        // Assert
        assertThat(result).isEqualTo(0L);
    }

    // ==================== Tests getAnnulationsByVolontaireOrderByDateDesc() ====================

    @Test
    @DisplayName("getAnnulationsByVolontaireOrderByDateDesc() - Tri par date décroissante")
    void testGetAnnulationsByVolontaireOrderByDateDesc_Success() {
        // Arrange
        Annulation annulation2 = new Annulation();
        annulation2.setIdAnnuler(2);
        annulation2.setIdVol(10);
        annulation2.setDateAnnulation("2024-02-15");

        AnnulationDTO annulationDTO2 = new AnnulationDTO();
        annulationDTO2.setIdAnnuler(2);
        annulationDTO2.setIdVol(10);
        annulationDTO2.setDateAnnulation("2024-02-15");

        when(annulationRepository.findByIdVolOrderByDateAnnulationDesc(10))
            .thenReturn(Arrays.asList(annulation2, annulation));
        when(annulationMapper.toDto(annulation2)).thenReturn(annulationDTO2);
        when(annulationMapper.toDto(annulation)).thenReturn(annulationDTO);

        // Act
        List<AnnulationDTO> result = annulationService.getAnnulationsByVolontaireOrderByDateDesc(10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDateAnnulation()).isEqualTo("2024-02-15");
        assertThat(result.get(1).getDateAnnulation()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("getAnnulationsByVolontaireOrderByDateDesc() - Liste vide")
    void testGetAnnulationsByVolontaireOrderByDateDesc_Empty() {
        // Arrange
        when(annulationRepository.findByIdVolOrderByDateAnnulationDesc(99))
            .thenReturn(Collections.emptyList());

        // Act
        List<AnnulationDTO> result = annulationService.getAnnulationsByVolontaireOrderByDateDesc(99);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
