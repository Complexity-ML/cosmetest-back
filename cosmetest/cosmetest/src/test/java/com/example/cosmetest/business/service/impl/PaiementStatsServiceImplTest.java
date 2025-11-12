package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PaiementEtudeSummaryDTO;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour PaiementStatsServiceImpl
 * Teste les statistiques et agrégations des paiements par étude
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PaiementStatsServiceImpl - Tests unitaires")
class PaiementStatsServiceImplTest {

    @Mock
    private EtudeVolontaireRepository etudeVolontaireRepository;

    @InjectMocks
    private PaiementStatsServiceImpl paiementStatsService;

    private Object[] createSummaryRow(Integer idEtude, Long total, Long payes, Long nonPayes,
                                       Long enAttente, Long annules, Long montantTotal,
                                       Long montantPaye, Long montantAnnules) {
        return new Object[]{idEtude, total, payes, nonPayes, enAttente, annules, montantTotal, montantPaye, montantAnnules};
    }

    @BeforeEach
    void setUp() {
        // Configuration commune si nécessaire
    }

    // ==================== Tests getAllEtudeSummaries() ====================

    @Test
    @DisplayName("getAllEtudeSummaries() - Récupération de toutes les statistiques d'études")
    void testGetAllEtudeSummaries_Success() {
        // Arrange
        Object[] row1 = createSummaryRow(1, 100L, 80L, 10L, 5L, 5L, 50000L, 40000L, 2500L);
        Object[] row2 = createSummaryRow(2, 50L, 40L, 5L, 3L, 2L, 25000L, 20000L, 1000L);
        List<Object[]> mockRows = Arrays.asList(row1, row2);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull())).thenReturn(mockRows);

        // Act
        List<PaiementEtudeSummaryDTO> result = paiementStatsService.getAllEtudeSummaries();

        // Assert
        assertThat(result).hasSize(2);
        
        // Vérification première étude
        PaiementEtudeSummaryDTO summary1 = result.get(0);
        assertThat(summary1.getIdEtude()).isEqualTo(1);
        assertThat(summary1.getTotal()).isEqualTo(100L);
        assertThat(summary1.getPayes()).isEqualTo(80L);
        assertThat(summary1.getNonPayes()).isEqualTo(10L);
        assertThat(summary1.getEnAttente()).isEqualTo(5L);
        assertThat(summary1.getAnnules()).isEqualTo(5L);
        assertThat(summary1.getMontantTotal()).isEqualTo(50000L);
        assertThat(summary1.getMontantPaye()).isEqualTo(40000L);
        assertThat(summary1.getMontantAnnules()).isEqualTo(2500L);
        assertThat(summary1.getMontantRestant()).isEqualTo(10000L); // 50000 - 40000

        // Vérification deuxième étude
        PaiementEtudeSummaryDTO summary2 = result.get(1);
        assertThat(summary2.getIdEtude()).isEqualTo(2);
        assertThat(summary2.getTotal()).isEqualTo(50L);
        assertThat(summary2.getMontantRestant()).isEqualTo(5000L); // 25000 - 20000

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }

    @Test
    @DisplayName("getAllEtudeSummaries() - Liste vide retournée")
    void testGetAllEtudeSummaries_EmptyList() {
        // Arrange
        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull())).thenReturn(Collections.emptyList());

        // Act
        List<PaiementEtudeSummaryDTO> result = paiementStatsService.getAllEtudeSummaries();

        // Assert
        assertThat(result).isEmpty();
        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }

    @Test
    @DisplayName("getAllEtudeSummaries() - Repository retourne null")
    void testGetAllEtudeSummaries_NullFromRepository() {
        // Arrange
        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull())).thenReturn(null);

        // Act
        List<PaiementEtudeSummaryDTO> result = paiementStatsService.getAllEtudeSummaries();

        // Assert
        assertThat(result).isEmpty();
        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }

    @Test
    @DisplayName("getAllEtudeSummaries() - Gestion des valeurs null dans les données")
    void testGetAllEtudeSummaries_WithNullValues() {
        // Arrange
        Object[] row = createSummaryRow(1, null, null, null, null, null, null, null, null);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull())).thenReturn(mockRows);

        // Act
        List<PaiementEtudeSummaryDTO> result = paiementStatsService.getAllEtudeSummaries();

        // Assert
        assertThat(result).hasSize(1);
        PaiementEtudeSummaryDTO summary = result.get(0);
        assertThat(summary.getTotal()).isEqualTo(0L);
        assertThat(summary.getPayes()).isEqualTo(0L);
        assertThat(summary.getNonPayes()).isEqualTo(0L);
        assertThat(summary.getEnAttente()).isEqualTo(0L);
        assertThat(summary.getAnnules()).isEqualTo(0L);
        assertThat(summary.getMontantTotal()).isEqualTo(0L);
        assertThat(summary.getMontantPaye()).isEqualTo(0L);
        assertThat(summary.getMontantAnnules()).isEqualTo(0L);
        assertThat(summary.getMontantRestant()).isEqualTo(0L);

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }

    @Test
    @DisplayName("getAllEtudeSummaries() - Calcul correct du montant restant")
    void testGetAllEtudeSummaries_CorrectMontantRestantCalculation() {
        // Arrange
        Object[] row = createSummaryRow(1, 100L, 75L, 15L, 5L, 5L, 100000L, 75000L, 5000L);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull())).thenReturn(mockRows);

        // Act
        List<PaiementEtudeSummaryDTO> result = paiementStatsService.getAllEtudeSummaries();

        // Assert
        assertThat(result).hasSize(1);
        PaiementEtudeSummaryDTO summary = result.get(0);
        assertThat(summary.getMontantTotal()).isEqualTo(100000L);
        assertThat(summary.getMontantPaye()).isEqualTo(75000L);
        assertThat(summary.getMontantRestant()).isEqualTo(25000L); // 100000 - 75000

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }

    @Test
    @DisplayName("getAllEtudeSummaries() - Plusieurs études avec différentes statistiques")
    void testGetAllEtudeSummaries_MultipleEtudesWithDifferentStats() {
        // Arrange
        Object[] row1 = createSummaryRow(1, 200L, 150L, 30L, 10L, 10L, 200000L, 150000L, 10000L);
        Object[] row2 = createSummaryRow(2, 100L, 100L, 0L, 0L, 0L, 100000L, 100000L, 0L);
        Object[] row3 = createSummaryRow(3, 50L, 0L, 40L, 10L, 0L, 50000L, 0L, 0L);
        List<Object[]> mockRows = Arrays.asList(row1, row2, row3);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull())).thenReturn(mockRows);

        // Act
        List<PaiementEtudeSummaryDTO> result = paiementStatsService.getAllEtudeSummaries();

        // Assert
        assertThat(result).hasSize(3);
        
        // Étude 1 - partiellement payée
        assertThat(result.get(0).getIdEtude()).isEqualTo(1);
        assertThat(result.get(0).getMontantRestant()).isEqualTo(50000L);
        
        // Étude 2 - entièrement payée
        assertThat(result.get(1).getIdEtude()).isEqualTo(2);
        assertThat(result.get(1).getMontantRestant()).isEqualTo(0L);
        assertThat(result.get(1).getPayes()).isEqualTo(100L);
        
        // Étude 3 - aucun paiement
        assertThat(result.get(2).getIdEtude()).isEqualTo(3);
        assertThat(result.get(2).getMontantRestant()).isEqualTo(50000L);
        assertThat(result.get(2).getPayes()).isEqualTo(0L);

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }

    // ==================== Tests getSummaryForEtude() ====================

    @Test
    @DisplayName("getSummaryForEtude() - Récupération des statistiques pour une étude spécifique")
    void testGetSummaryForEtude_Success() {
        // Arrange
        int idEtude = 1;
        Object[] row = createSummaryRow(1, 100L, 80L, 10L, 5L, 5L, 50000L, 40000L, 2500L);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(mockRows);

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdEtude()).isEqualTo(1);
        assertThat(result.getTotal()).isEqualTo(100L);
        assertThat(result.getPayes()).isEqualTo(80L);
        assertThat(result.getNonPayes()).isEqualTo(10L);
        assertThat(result.getEnAttente()).isEqualTo(5L);
        assertThat(result.getAnnules()).isEqualTo(5L);
        assertThat(result.getMontantTotal()).isEqualTo(50000L);
        assertThat(result.getMontantPaye()).isEqualTo(40000L);
        assertThat(result.getMontantAnnules()).isEqualTo(2500L);
        assertThat(result.getMontantRestant()).isEqualTo(10000L);

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - Étude sans données retourne null")
    void testGetSummaryForEtude_NoData() {
        // Arrange
        int idEtude = 999;
        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(Collections.emptyList());

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNull();
        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - Repository retourne null")
    void testGetSummaryForEtude_RepositoryReturnsNull() {
        // Arrange
        int idEtude = 1;
        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(null);

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNull();
        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - Étude avec tous les paiements effectués")
    void testGetSummaryForEtude_AllPaid() {
        // Arrange
        int idEtude = 1;
        Object[] row = createSummaryRow(1, 100L, 100L, 0L, 0L, 0L, 50000L, 50000L, 0L);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(mockRows);

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(100L);
        assertThat(result.getPayes()).isEqualTo(100L);
        assertThat(result.getNonPayes()).isEqualTo(0L);
        assertThat(result.getMontantTotal()).isEqualTo(50000L);
        assertThat(result.getMontantPaye()).isEqualTo(50000L);
        assertThat(result.getMontantRestant()).isEqualTo(0L);

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - Étude avec aucun paiement effectué")
    void testGetSummaryForEtude_NoPaid() {
        // Arrange
        int idEtude = 1;
        Object[] row = createSummaryRow(1, 50L, 0L, 40L, 10L, 0L, 25000L, 0L, 0L);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(mockRows);

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(50L);
        assertThat(result.getPayes()).isEqualTo(0L);
        assertThat(result.getNonPayes()).isEqualTo(40L);
        assertThat(result.getEnAttente()).isEqualTo(10L);
        assertThat(result.getMontantTotal()).isEqualTo(25000L);
        assertThat(result.getMontantPaye()).isEqualTo(0L);
        assertThat(result.getMontantRestant()).isEqualTo(25000L);

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - Étude avec des annulations")
    void testGetSummaryForEtude_WithCancellations() {
        // Arrange
        int idEtude = 1;
        Object[] row = createSummaryRow(1, 100L, 70L, 15L, 5L, 10L, 50000L, 35000L, 5000L);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(mockRows);

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(100L);
        assertThat(result.getPayes()).isEqualTo(70L);
        assertThat(result.getAnnules()).isEqualTo(10L);
        assertThat(result.getMontantAnnules()).isEqualTo(5000L);
        assertThat(result.getMontantRestant()).isEqualTo(15000L); // 50000 - 35000

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - Gestion des valeurs null dans les données")
    void testGetSummaryForEtude_WithNullValues() {
        // Arrange
        int idEtude = 1;
        Object[] row = createSummaryRow(1, null, null, null, null, null, null, null, null);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(mockRows);

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(0L);
        assertThat(result.getPayes()).isEqualTo(0L);
        assertThat(result.getNonPayes()).isEqualTo(0L);
        assertThat(result.getEnAttente()).isEqualTo(0L);
        assertThat(result.getAnnules()).isEqualTo(0L);
        assertThat(result.getMontantTotal()).isEqualTo(0L);
        assertThat(result.getMontantPaye()).isEqualTo(0L);
        assertThat(result.getMontantAnnules()).isEqualTo(0L);
        assertThat(result.getMontantRestant()).isEqualTo(0L);

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - ID étude négatif")
    void testGetSummaryForEtude_NegativeId() {
        // Arrange
        int idEtude = -1;
        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(Collections.emptyList());

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNull();
        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getSummaryForEtude() - Vérification montants cohérents")
    void testGetSummaryForEtude_ConsistentAmounts() {
        // Arrange
        int idEtude = 1;
        Object[] row = createSummaryRow(1, 120L, 90L, 20L, 5L, 5L, 120000L, 90000L, 5000L);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude)).thenReturn(mockRows);

        // Act
        PaiementEtudeSummaryDTO result = paiementStatsService.getSummaryForEtude(idEtude);

        // Assert
        assertThat(result).isNotNull();
        
        // Vérifier cohérence: total = payés + non payés + en attente + annulés
        // Note: dans les vraies données, cette cohérence peut ne pas être garantie
        // On vérifie juste que les calculs sont corrects
        assertThat(result.getMontantRestant()).isEqualTo(
            result.getMontantTotal() - result.getMontantPaye()
        );

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    // ==================== Tests de robustesse ====================

    @Test
    @DisplayName("getAllEtudeSummaries() - Exception dans le repository")
    void testGetAllEtudeSummaries_RepositoryException() {
        // Arrange
        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull()))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            paiementStatsService.getAllEtudeSummaries();
        });

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }

    @Test
    @DisplayName("getSummaryForEtude() - Exception dans le repository")
    void testGetSummaryForEtude_RepositoryException() {
        // Arrange
        int idEtude = 1;
        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(idEtude))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            paiementStatsService.getSummaryForEtude(idEtude);
        });

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(idEtude);
    }

    @Test
    @DisplayName("getAllEtudeSummaries() - Grandes valeurs de montants")
    void testGetAllEtudeSummaries_LargeAmounts() {
        // Arrange
        Object[] row = createSummaryRow(1, 10000L, 8000L, 1500L, 300L, 200L, 
                                        5000000L, 4000000L, 100000L);
        List<Object[]> mockRows = Collections.singletonList(row);

        when(etudeVolontaireRepository.fetchEtudePaiementSummaries(isNull())).thenReturn(mockRows);

        // Act
        List<PaiementEtudeSummaryDTO> result = paiementStatsService.getAllEtudeSummaries();

        // Assert
        assertThat(result).hasSize(1);
        PaiementEtudeSummaryDTO summary = result.get(0);
        assertThat(summary.getMontantTotal()).isEqualTo(5000000L);
        assertThat(summary.getMontantPaye()).isEqualTo(4000000L);
        assertThat(summary.getMontantRestant()).isEqualTo(1000000L);

        verify(etudeVolontaireRepository, times(1)).fetchEtudePaiementSummaries(isNull());
    }
}
