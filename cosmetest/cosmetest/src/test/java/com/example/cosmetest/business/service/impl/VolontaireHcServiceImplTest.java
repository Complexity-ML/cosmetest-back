package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.business.mapper.VolontaireHcMapper;
import com.example.cosmetest.data.repository.VolontaireHcRepository;
import com.example.cosmetest.domain.model.VolontaireHc;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour VolontaireHcServiceImpl - gestion des habitudes de consommation hors cadre
 */
@ExtendWith(MockitoExtension.class)
class VolontaireHcServiceImplTest {

    @Mock
    private VolontaireHcRepository volontaireHcRepository;

    @Mock
    private VolontaireHcMapper volontaireHcMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private VolontaireHcServiceImpl volontaireHcService;

    private VolontaireHc volontaireHc;
    private VolontaireHcDTO volontaireHcDTO;

    @BeforeEach
    void setUp() {
        // Injecter l'EntityManager manuellement (non injecté par @InjectMocks via constructeur)
        ReflectionTestUtils.setField(volontaireHcService, "entityManager", entityManager);

        // Entité
        volontaireHc = new VolontaireHc();
        volontaireHc.setIdVol(1);
        volontaireHc.setAchatGrandesSurfaces("oui");
        volontaireHc.setAchatInternet("non");
        volontaireHc.setAchatPharmacieParapharmacie("occasionnellement");
        volontaireHc.setAchatInstitutParfumerie("regulierement");

        // DTO
        volontaireHcDTO = new VolontaireHcDTO();
        volontaireHcDTO.setIdVol(1);
        volontaireHcDTO.setAchatGrandesSurfaces("oui");
        volontaireHcDTO.setAchatInternet("non");
        volontaireHcDTO.setAchatPharmacieParapharmacie("occasionnellement");
        volontaireHcDTO.setAchatInstitutParfumerie("regulierement");
    }

    // ==================== Tests saveVolontaireHc ====================

    @Test
    void testSaveVolontaireHc_Creation_Success() {
        // Given
        when(volontaireHcRepository.findByIdVol(1)).thenReturn(Optional.empty());
        when(volontaireHcMapper.toEntity(any(VolontaireHcDTO.class))).thenReturn(volontaireHc);
        when(volontaireHcRepository.save(any(VolontaireHc.class))).thenReturn(volontaireHc);
        when(volontaireHcMapper.toDTO(any(VolontaireHc.class))).thenReturn(volontaireHcDTO);

        // When
        VolontaireHcDTO result = volontaireHcService.saveVolontaireHc(volontaireHcDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdVol()).isEqualTo(1);
        verify(volontaireHcRepository).findByIdVol(1);
        verify(volontaireHcMapper).toEntity(any(VolontaireHcDTO.class));
        verify(volontaireHcRepository).save(any(VolontaireHc.class));
    }

    @Test
    void testSaveVolontaireHc_Update_Success() {
        // Given
        when(volontaireHcRepository.findByIdVol(1)).thenReturn(Optional.of(volontaireHc));
        when(volontaireHcMapper.updateEntityFromDTO(any(VolontaireHc.class), any(VolontaireHcDTO.class)))
                .thenReturn(volontaireHc);
        when(volontaireHcRepository.save(any(VolontaireHc.class))).thenReturn(volontaireHc);
        when(volontaireHcMapper.toDTO(any(VolontaireHc.class))).thenReturn(volontaireHcDTO);

        // When
        VolontaireHcDTO result = volontaireHcService.saveVolontaireHc(volontaireHcDTO);

        // Then
        assertThat(result).isNotNull();
        verify(volontaireHcRepository).findByIdVol(1);
        verify(volontaireHcMapper).updateEntityFromDTO(any(VolontaireHc.class), any(VolontaireHcDTO.class));
        verify(volontaireHcRepository).save(any(VolontaireHc.class));
    }

    @Test
    void testSaveVolontaireHc_NullDTO_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> volontaireHcService.saveVolontaireHc(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("habitudes de consommation ne peuvent pas être null");
    }

    @Test
    void testSaveVolontaireHc_InvalidIdVol_ThrowsException() {
        // Given
        volontaireHcDTO.setIdVol(0);

        // When & Then
        assertThatThrownBy(() -> volontaireHcService.saveVolontaireHc(volontaireHcDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID du volontaire doit être un nombre positif");
    }

    @Test
    void testSaveVolontaireHc_InvalidValue_ThrowsException() {
        // Given
        volontaireHcDTO.setAchatInternet("invalide");

        // When & Then
        assertThatThrownBy(() -> volontaireHcService.saveVolontaireHc(volontaireHcDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Valeur non autorisée");
    }

    // ==================== Tests deleteVolontaireHc ====================

    @Test
    void testDeleteVolontaireHc_Success() {
        // Given
        when(volontaireHcRepository.findByIdVol(1)).thenReturn(Optional.of(volontaireHc));

        // When
        boolean result = volontaireHcService.deleteVolontaireHc(1);

        // Then
        assertThat(result).isTrue();
        verify(volontaireHcRepository).delete(volontaireHc);
    }

    @Test
    void testDeleteVolontaireHc_NotFound_ReturnsFalse() {
        // Given
        when(volontaireHcRepository.findByIdVol(999)).thenReturn(Optional.empty());

        // When
        boolean result = volontaireHcService.deleteVolontaireHc(999);

        // Then
        assertThat(result).isFalse();
        verify(volontaireHcRepository, never()).delete(any());
    }

    @Test
    void testDeleteVolontaireHc_NullId_ReturnsFalse() {
        // When
        boolean result = volontaireHcService.deleteVolontaireHc(null);

        // Then
        assertThat(result).isFalse();
        verify(volontaireHcRepository, never()).findByIdVol(anyInt());
    }

    // ==================== Tests existsByIdVol ====================

    @Test
    void testExistsByIdVol_Exists_ReturnsTrue() {
        // Given
        when(volontaireHcRepository.existsByIdVol(1)).thenReturn(true);

        // When
        boolean result = volontaireHcService.existsByIdVol(1);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void testExistsByIdVol_NotExists_ReturnsFalse() {
        // Given
        when(volontaireHcRepository.existsByIdVol(999)).thenReturn(false);

        // When
        boolean result = volontaireHcService.existsByIdVol(999);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void testExistsByIdVol_NullId_ReturnsFalse() {
        // When
        boolean result = volontaireHcService.existsByIdVol(null);

        // Then
        assertThat(result).isFalse();
        verify(volontaireHcRepository, never()).existsByIdVol(anyInt());
    }

    // ==================== Tests findByLieuAchat ====================

    @Test
    void testFindByLieuAchat_GrandesSurfaces_Success() {
        // Given
        List<VolontaireHc> volontaireHcs = Arrays.asList(volontaireHc);
        List<VolontaireHcDTO> expectedDTOs = Arrays.asList(volontaireHcDTO);
        
        when(volontaireHcRepository.findByAchatGrandesSurfaces("oui")).thenReturn(volontaireHcs);
        when(volontaireHcMapper.toDTOList(volontaireHcs)).thenReturn(expectedDTOs);

        // When
        List<VolontaireHcDTO> result = volontaireHcService.findByLieuAchat("achatGrandesSurfaces", "oui");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIdVol()).isEqualTo(1);
    }

    @Test
    void testFindByLieuAchat_Internet_Success() {
        // Given
        List<VolontaireHc> volontaireHcs = Arrays.asList(volontaireHc);
        List<VolontaireHcDTO> expectedDTOs = Arrays.asList(volontaireHcDTO);
        
        when(volontaireHcRepository.findByAchatInternet("non")).thenReturn(volontaireHcs);
        when(volontaireHcMapper.toDTOList(volontaireHcs)).thenReturn(expectedDTOs);

        // When
        List<VolontaireHcDTO> result = volontaireHcService.findByLieuAchat("achatInternet", "non");

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByLieuAchat_InvalidLieu_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> volontaireHcService.findByLieuAchat("invalid", "oui"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lieu d'achat non reconnu");
    }

    @Test
    void testFindByLieuAchat_InvalidValue_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> volontaireHcService.findByLieuAchat("achatInternet", "invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Valeur non autorisée");
    }

    // ==================== Tests updateProduit ====================

    @Test
    void testUpdateProduit_Success() {
        // Given
        when(volontaireHcRepository.findByIdVol(1)).thenReturn(Optional.of(volontaireHc));
        when(volontaireHcRepository.save(any(VolontaireHc.class))).thenReturn(volontaireHc);
        when(volontaireHcMapper.toDTO(any(VolontaireHc.class))).thenReturn(volontaireHcDTO);

        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.updateProduit(1, "achatInternet", "oui");

        // Then
        assertThat(result).isPresent();
        verify(volontaireHcRepository).save(any(VolontaireHc.class));
    }

    @Test
    void testUpdateProduit_NullIdVol_ReturnsEmpty() {
        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.updateProduit(null, "achatInternet", "oui");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testUpdateProduit_InvalidProduit_ThrowsException() {
        // When & Then - validateProduitParameter lève l'exception avant le repository call
        assertThatThrownBy(() -> volontaireHcService.updateProduit(1, "invalidProduit", "oui"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Produit non reconnu");
    }

    @Test
    void testUpdateProduit_VolontaireNotFound_ReturnsEmpty() {
        // Given
        when(volontaireHcRepository.findByIdVol(999)).thenReturn(Optional.empty());

        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.updateProduit(999, "achatInternet", "oui");

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== Tests updateProduits ====================

    @Test
    void testUpdateProduits_Success() {
        // Given
        Map<String, String> produits = new HashMap<>();
        produits.put("achatInternet", "oui");
        produits.put("achatGrandesSurfaces", "non");

        when(volontaireHcRepository.findByIdVol(1)).thenReturn(Optional.of(volontaireHc));
        when(volontaireHcRepository.save(any(VolontaireHc.class))).thenReturn(volontaireHc);
        when(volontaireHcMapper.toDTO(any(VolontaireHc.class))).thenReturn(volontaireHcDTO);

        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.updateProduits(1, produits);

        // Then
        assertThat(result).isPresent();
        verify(volontaireHcRepository).save(any(VolontaireHc.class));
    }

    @Test
    void testUpdateProduits_NullIdVol_ReturnsEmpty() {
        // Given
        Map<String, String> produits = new HashMap<>();
        produits.put("achatInternet", "oui");

        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.updateProduits(null, produits);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testUpdateProduits_EmptyMap_ReturnsEmpty() {
        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.updateProduits(1, Collections.emptyMap());

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testUpdateProduits_InvalidProduit_ThrowsException() {
        // Given
        Map<String, String> produits = new HashMap<>();
        produits.put("invalidProduit", "oui");

        // When & Then
        assertThatThrownBy(() -> volontaireHcService.updateProduits(1, produits))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Produit non reconnu");
    }

    // ==================== Tests getStatistiquesUtilisationProduit ====================

    @Test
    void testGetStatistiquesUtilisationProduit_Success() {
        // Given - mock native query returning GROUP BY results
        Query mockQuery = mock(Query.class);
        List<Object[]> queryResults = new ArrayList<>();
        queryResults.add(new Object[]{"oui", 2L});
        queryResults.add(new Object[]{"non", 1L});
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(queryResults);

        // When
        Map<String, Long> result = volontaireHcService.getStatistiquesUtilisationProduit("achatInternet");

        // Then
        assertThat(result).containsEntry("oui", 2L);
        assertThat(result).containsEntry("non", 1L);
    }

    @Test
    void testGetStatistiquesUtilisationProduit_WithNullValues() {
        // Given - mock native query with COALESCE handling nulls
        Query mockQuery = mock(Query.class);
        List<Object[]> queryResults = new ArrayList<>();
        queryResults.add(new Object[]{"non spécifié", 1L});
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(queryResults);

        // When
        Map<String, Long> result = volontaireHcService.getStatistiquesUtilisationProduit("achatInternet");

        // Then
        assertThat(result).containsEntry("non spécifié", 1L);
    }

    @Test
    void testGetStatistiquesUtilisationProduit_InvalidProduit_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> volontaireHcService.getStatistiquesUtilisationProduit("invalidProduit"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Produit non reconnu");
    }

    // ==================== Tests getProduitsLesPlusUtilises ====================

    @Test
    void testGetProduitsLesPlusUtilises_Success() {
        // Given - mock native COUNT queries for each product field
        Query mockQuery = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        // Each call to getSingleResult returns a count; at least some will be > 0
        when(mockQuery.getSingleResult()).thenReturn(2L);

        // When
        Map<String, Long> result = volontaireHcService.getProduitsLesPlusUtilises(3);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isLessThanOrEqualTo(3);
    }

    @Test
    void testGetProduitsLesPlusUtilises_InvalidLimit_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> volontaireHcService.getProduitsLesPlusUtilises(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("limite doit être un nombre positif");
    }

    @Test
    void testGetProduitsLesPlusUtilises_LimitExceedsProducts() {
        // Given - mock native COUNT queries
        Query mockQuery = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(1L);

        // When
        Map<String, Long> result = volontaireHcService.getProduitsLesPlusUtilises(100);

        // Then - Should not throw, just return what's available
        assertThat(result).isNotEmpty();
    }

    // ==================== Tests getLieuxAchatPreferences ====================

    @Test
    void testGetLieuxAchatPreferences_Success() {
        // Given - mock 4 native COUNT queries for each lieu d'achat
        Query mockQuery1 = mock(Query.class);
        Query mockQuery2 = mock(Query.class);
        Query mockQuery3 = mock(Query.class);
        Query mockQuery4 = mock(Query.class);

        // Return different queries for each call
        when(entityManager.createNativeQuery(anyString()))
                .thenReturn(mockQuery1, mockQuery2, mockQuery3, mockQuery4);
        when(mockQuery1.getSingleResult()).thenReturn(2L);  // achatGrandesSurfaces
        when(mockQuery2.getSingleResult()).thenReturn(1L);  // achatInstitutParfumerie
        when(mockQuery3.getSingleResult()).thenReturn(1L);  // achatInternet
        when(mockQuery4.getSingleResult()).thenReturn(0L);  // achatPharmacieParapharmacie

        // When
        Map<String, Long> result = volontaireHcService.getLieuxAchatPreferences();

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result).containsEntry("achatGrandesSurfaces", 2L);
        assertThat(result).containsEntry("achatInternet", 1L);
        assertThat(result).containsEntry("achatInstitutParfumerie", 1L);
    }

    @Test
    void testGetLieuxAchatPreferences_EmptyList() {
        // Given - all native COUNT queries return 0
        Query mockQuery = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(0L);

        // When
        Map<String, Long> result = volontaireHcService.getLieuxAchatPreferences();

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== Tests findByMultipleProduits ====================

    @Test
    void testFindByMultipleProduits_Success() {
        // Given
        Map<String, String> produits = new HashMap<>();
        produits.put("achatInternet", "non");
        produits.put("achatGrandesSurfaces", "oui");

        VolontaireHc vol1 = new VolontaireHc();
        vol1.setIdVol(1);
        vol1.setAchatInternet("non");
        vol1.setAchatGrandesSurfaces("oui");

        VolontaireHc vol2 = new VolontaireHc();
        vol2.setIdVol(2);
        vol2.setAchatInternet("oui");
        vol2.setAchatGrandesSurfaces("oui");

        when(volontaireHcRepository.findAll()).thenReturn(Arrays.asList(vol1, vol2));
        when(volontaireHcMapper.toDTOList(anyList())).thenReturn(Arrays.asList(volontaireHcDTO));

        // When
        List<VolontaireHcDTO> result = volontaireHcService.findByMultipleProduits(produits);

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByMultipleProduits_NullMap_ReturnsEmpty() {
        // When
        List<VolontaireHcDTO> result = volontaireHcService.findByMultipleProduits(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testFindByMultipleProduits_EmptyMap_ReturnsEmpty() {
        // When
        List<VolontaireHcDTO> result = volontaireHcService.findByMultipleProduits(Collections.emptyMap());

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== Tests findByIdVolIn ====================

    @Test
    void testFindByIdVolIn_Success() {
        // Given
        List<Integer> idList = Arrays.asList(1, 2, 3);
        List<VolontaireHc> volontaireHcs = Arrays.asList(volontaireHc);
        
        when(volontaireHcRepository.findByIdVolIn(idList)).thenReturn(volontaireHcs);

        // When
        List<VolontaireHc> result = volontaireHcService.findByIdVolIn(idList);

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByIdVolIn_NullList_ReturnsEmpty() {
        // When
        List<VolontaireHc> result = volontaireHcService.findByIdVolIn(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testFindByIdVolIn_EmptyList_ReturnsEmpty() {
        // When
        List<VolontaireHc> result = volontaireHcService.findByIdVolIn(Collections.emptyList());

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== Tests getVolontaireHcByIdVol (List) ====================

    @Test
    void testGetVolontaireHcByIdVolList_Success() {
        // Given
        List<Integer> idList = Arrays.asList(1, 2);
        List<VolontaireHc> volontaireHcs = Arrays.asList(volontaireHc);
        
        when(volontaireHcRepository.findByIdVolIn(idList)).thenReturn(volontaireHcs);
        when(volontaireHcMapper.toDTO(any(VolontaireHc.class))).thenReturn(volontaireHcDTO);

        // When
        List<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcByIdVol(idList);

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetVolontaireHcByIdVolList_NullList_ReturnsEmpty() {
        // When
        List<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcByIdVol((List<Integer>) null);

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== Tests getVolontaireHcByIdVol (Single) ====================

    @Test
    void testGetVolontaireHcByIdVolSingle_Success() {
        // Given
        when(volontaireHcRepository.findByIdVol(1)).thenReturn(Optional.of(volontaireHc));
        when(volontaireHcMapper.toDTO(any(VolontaireHc.class))).thenReturn(volontaireHcDTO);

        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcByIdVol(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getIdVol()).isEqualTo(1);
    }

    @Test
    void testGetVolontaireHcByIdVolSingle_NotFound_ReturnsEmpty() {
        // Given
        when(volontaireHcRepository.findByIdVol(999)).thenReturn(Optional.empty());

        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcByIdVol(999);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetVolontaireHcByIdVolSingle_NullId_ReturnsEmpty() {
        // When
        Optional<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcByIdVol((Integer) null);

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== Tests getAllVolontaireHcs ====================

    @Test
    void testGetAllVolontaireHcs_Success() {
        // Given
        List<VolontaireHc> volontaireHcs = Arrays.asList(volontaireHc);
        List<VolontaireHcDTO> expectedDTOs = Arrays.asList(volontaireHcDTO);
        
        when(volontaireHcRepository.findAll()).thenReturn(volontaireHcs);
        when(volontaireHcMapper.toDTOList(volontaireHcs)).thenReturn(expectedDTOs);

        // When
        List<VolontaireHcDTO> result = volontaireHcService.getAllVolontaireHcs();

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetAllVolontaireHcs_EmptyList() {
        // Given
        when(volontaireHcRepository.findAll()).thenReturn(Collections.emptyList());
        when(volontaireHcMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // When
        List<VolontaireHcDTO> result = volontaireHcService.getAllVolontaireHcs();

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== Tests findByProduit ====================

    @Test
    void testFindByProduit_Success() {
        // Given
        VolontaireHc vol1 = new VolontaireHc();
        vol1.setIdVol(1);
        vol1.setAchatInternet("oui");

        when(volontaireHcRepository.findAll()).thenReturn(Arrays.asList(vol1));
        when(volontaireHcMapper.toDTOList(anyList())).thenReturn(Arrays.asList(volontaireHcDTO));

        // When
        List<VolontaireHcDTO> result = volontaireHcService.findByProduit("achatInternet", "oui");

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByProduit_WithNullValueTreatedAsNon() {
        // Given
        VolontaireHc vol1 = new VolontaireHc();
        vol1.setIdVol(1);
        vol1.setAchatInternet(null);

        when(volontaireHcRepository.findAll()).thenReturn(Arrays.asList(vol1));
        when(volontaireHcMapper.toDTOList(anyList())).thenReturn(Arrays.asList(volontaireHcDTO));

        // When
        List<VolontaireHcDTO> result = volontaireHcService.findByProduit("achatInternet", "non");

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByProduit_InvalidProduit_ThrowsException() {
        // When & Then
        assertThatThrownBy(() -> volontaireHcService.findByProduit("invalidProduit", "oui"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Produit non reconnu");
    }

    // ==================== Tests getVolontaireHcsByIds ====================

    @Test
    void testGetVolontaireHcsByIds_Success() {
        // Given
        List<Integer> idList = Arrays.asList(1, 2);
        List<VolontaireHc> volontaireHcs = Arrays.asList(volontaireHc);
        
        when(volontaireHcRepository.findByIdVolIn(idList)).thenReturn(volontaireHcs);
        when(volontaireHcMapper.toDTO(any(VolontaireHc.class))).thenReturn(volontaireHcDTO);

        // When
        List<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcsByIds(idList);

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    void testGetVolontaireHcsByIds_EmptyList() {
        // Given
        when(volontaireHcRepository.findByIdVolIn(Collections.emptyList())).thenReturn(Collections.emptyList());

        // When
        List<VolontaireHcDTO> result = volontaireHcService.getVolontaireHcsByIds(Collections.emptyList());

        // Then
        assertThat(result).isEmpty();
    }
}
