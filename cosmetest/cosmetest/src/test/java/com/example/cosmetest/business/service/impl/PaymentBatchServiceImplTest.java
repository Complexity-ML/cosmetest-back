package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.PaymentBatchResultDTO;
import com.example.cosmetest.business.service.AnnulationService;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.domain.model.EtudeVolontaireId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour PaymentBatchServiceImpl
 * Service de traitement par lot des paiements
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentBatchServiceImpl - Tests unitaires")
class PaymentBatchServiceImplTest {

    @Mock
    private EtudeVolontaireService etudeVolontaireService;

    @Mock
    private AnnulationService annulationService;

    @Mock
    private EtudeService etudeService;

    @InjectMocks
    private PaymentBatchServiceImpl paymentBatchService;

    private int idEtude;
    private List<EtudeVolontaireDTO> associations;
    private List<AnnulationDTO> annulations;

    @BeforeEach
    void setUp() {
        idEtude = 1;
        associations = new ArrayList<>();
        annulations = new ArrayList<>();
    }

    // ==================== Tests markAllAsPaid() - Scénarios de succès ====================

    @Test
    @DisplayName("markAllAsPaid() - Tous les volontaires marqués comme payés")
    void testMarkAllAsPaid_AllSuccess() {
        // Arrange - 3 volontaires non payés
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0); // Non payé
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 0); // Non payé
        EtudeVolontaireDTO vol3 = createVolontaireDTO(1, 30, 0); // Non payé
        associations.add(vol1);
        associations.add(vol2);
        associations.add(vol3);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(Collections.emptyList());
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1))).thenReturn(vol1);
        when(etudeService.updatePayeStatus(idEtude, 2)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdEtude()).isEqualTo(idEtude);
        assertThat(result.getProcessedCount()).isEqualTo(3);
        assertThat(result.getUpdatedCount()).isEqualTo(3);
        assertThat(result.getAlreadyPaidCount()).isEqualTo(0);
        assertThat(result.getSkippedAnnules()).isEqualTo(0);
        assertThat(result.getErrorCount()).isEqualTo(0);
        assertThat(result.getErrors()).isEmpty();
        
        verify(etudeVolontaireService, atLeast(3)).updatePaye(any(EtudeVolontaireId.class), eq(1));
        verify(etudeService, times(1)).updatePayeStatus(eq(idEtude), anyInt());
    }

    @Test
    @DisplayName("markAllAsPaid() - Aucune association pour l'étude")
    void testMarkAllAsPaid_NoAssociations() {
        // Arrange
        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(Collections.emptyList());
        when(etudeService.updatePayeStatus(idEtude, 0)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdEtude()).isEqualTo(idEtude);
        assertThat(result.getProcessedCount()).isEqualTo(0);
        assertThat(result.getUpdatedCount()).isEqualTo(0);
        verify(etudeService, times(1)).updatePayeStatus(idEtude, 0);
        verify(etudeVolontaireService, never()).updatePaye(any(), anyInt());
    }

    @Test
    @DisplayName("markAllAsPaid() - Liste null d'associations")
    void testMarkAllAsPaid_NullAssociations() {
        // Arrange
        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(null);
        when(etudeService.updatePayeStatus(idEtude, 0)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProcessedCount()).isEqualTo(0);
        verify(etudeService, times(1)).updatePayeStatus(idEtude, 0);
    }

    // ==================== Tests markAllAsPaid() - Gestion des déjà payés ====================

    @Test
    @DisplayName("markAllAsPaid() - Certains volontaires déjà payés")
    void testMarkAllAsPaid_SomeAlreadyPaid() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0); // Non payé
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 1); // Déjà payé
        EtudeVolontaireDTO vol3 = createVolontaireDTO(1, 30, 1); // Déjà payé
        associations.add(vol1);
        associations.add(vol2);
        associations.add(vol3);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(Collections.emptyList());
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1))).thenReturn(vol1);
        when(etudeService.updatePayeStatus(idEtude, 2)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getProcessedCount()).isEqualTo(3);
        assertThat(result.getUpdatedCount()).isEqualTo(1); // Seulement vol1
        assertThat(result.getAlreadyPaidCount()).isEqualTo(2); // vol2 et vol3
        assertThat(result.getSkippedAnnules()).isEqualTo(0);
        
        verify(etudeVolontaireService, times(1)).updatePaye(any(EtudeVolontaireId.class), eq(1));
    }

    @Test
    @DisplayName("markAllAsPaid() - Tous déjà payés")
    void testMarkAllAsPaid_AllAlreadyPaid() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 1);
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 1);
        associations.add(vol1);
        associations.add(vol2);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(Collections.emptyList());
        when(etudeService.updatePayeStatus(idEtude, 2)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getUpdatedCount()).isEqualTo(0);
        assertThat(result.getAlreadyPaidCount()).isEqualTo(2);
        verify(etudeVolontaireService, never()).updatePaye(any(), anyInt());
        verify(etudeService, times(1)).updatePayeStatus(idEtude, 2);
    }

    // ==================== Tests markAllAsPaid() - Gestion des annulations ====================

    @Test
    @DisplayName("markAllAsPaid() - Exclure les volontaires annulés")
    void testMarkAllAsPaid_WithCancelledVolunteers() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0); // Non payé, actif
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 0); // Non payé, ANNULÉ
        EtudeVolontaireDTO vol3 = createVolontaireDTO(1, 30, 0); // Non payé, actif
        associations.add(vol1);
        associations.add(vol2);
        associations.add(vol3);

        // Annulation pour vol2
        AnnulationDTO annulation = new AnnulationDTO();
        annulation.setIdVol(20);
        annulation.setIdEtude(1);
        annulations.add(annulation);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(annulations);
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1))).thenReturn(vol1);
        when(etudeService.updatePayeStatus(idEtude, 2)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getProcessedCount()).isEqualTo(3);
        assertThat(result.getUpdatedCount()).isEqualTo(2); // vol1 et vol3
        assertThat(result.getSkippedAnnules()).isEqualTo(1); // vol2 annulé
        assertThat(result.getAlreadyPaidCount()).isEqualTo(0);
        
        verify(etudeVolontaireService, times(2)).updatePaye(any(EtudeVolontaireId.class), eq(1));
    }

    @Test
    @DisplayName("markAllAsPaid() - Tous les volontaires annulés")
    void testMarkAllAsPaid_AllCancelled() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0);
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 0);
        associations.add(vol1);
        associations.add(vol2);

        AnnulationDTO annul1 = new AnnulationDTO();
        annul1.setIdVol(10);
        AnnulationDTO annul2 = new AnnulationDTO();
        annul2.setIdVol(20);
        annulations.add(annul1);
        annulations.add(annul2);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(annulations);
        when(etudeService.updatePayeStatus(idEtude, 0)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getUpdatedCount()).isEqualTo(0);
        assertThat(result.getSkippedAnnules()).isEqualTo(2);
        verify(etudeVolontaireService, never()).updatePaye(any(), anyInt());
    }

    @Test
    @DisplayName("markAllAsPaid() - Liste null d'annulations")
    void testMarkAllAsPaid_NullAnnulations() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0);
        associations.add(vol1);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(null);
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1))).thenReturn(vol1);
        when(etudeService.updatePayeStatus(idEtude, 2)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getUpdatedCount()).isEqualTo(1);
        assertThat(result.getSkippedAnnules()).isEqualTo(0);
    }

    // ==================== Tests markAllAsPaid() - Gestion des erreurs ====================

    @Test
    @DisplayName("markAllAsPaid() - Erreur lors de la mise à jour d'un paiement")
    void testMarkAllAsPaid_UpdateError() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0);
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 0);
        EtudeVolontaireDTO vol3 = createVolontaireDTO(1, 30, 0);
        associations.add(vol1);
        associations.add(vol2);
        associations.add(vol3);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(Collections.emptyList());
        
        // Premier appel : succès, deuxième : erreur, troisième : succès
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1)))
            .thenReturn(vol1)
            .thenThrow(new RuntimeException("Erreur base de données"))
            .thenReturn(vol3);

        when(etudeService.updatePayeStatus(idEtude, 0)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getProcessedCount()).isEqualTo(3);
        assertThat(result.getUpdatedCount()).isEqualTo(2); // vol1 et vol3
        assertThat(result.getErrorCount()).isEqualTo(1); // vol2
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.getErrors().get(0)).contains("Volontaire 20");
    }

    @Test
    @DisplayName("markAllAsPaid() - Toutes les mises à jour échouent")
    void testMarkAllAsPaid_AllUpdatesFail() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0);
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 0);
        associations.add(vol1);
        associations.add(vol2);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(Collections.emptyList());
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1)))
            .thenThrow(new RuntimeException("Erreur"));
        when(etudeService.updatePayeStatus(idEtude, 0)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getUpdatedCount()).isEqualTo(0);
        assertThat(result.getErrorCount()).isEqualTo(2);
        assertThat(result.getErrors()).hasSize(2);
    }

    @Test
    @DisplayName("markAllAsPaid() - Erreur lors de la mise à jour du statut étude")
    void testMarkAllAsPaid_EtudeStatusUpdateError() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0);
        associations.add(vol1);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(Collections.emptyList());
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1))).thenReturn(vol1);
        when(etudeService.updatePayeStatus(idEtude, 2))
            .thenThrow(new RuntimeException("Erreur statut"));

        // Act - Ne doit pas lever d'exception (erreur loggée seulement)
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getUpdatedCount()).isEqualTo(1);
        assertThat(result.getErrorCount()).isEqualTo(0); // Erreur statut étude != erreur paiement
    }

    // ==================== Tests markAllAsPaid() - Scénarios mixtes ====================

    @Test
    @DisplayName("markAllAsPaid() - Mix : déjà payés, annulés, à traiter")
    void testMarkAllAsPaid_MixedScenario() {
        // Arrange
        EtudeVolontaireDTO vol1 = createVolontaireDTO(1, 10, 0);  // À payer
        EtudeVolontaireDTO vol2 = createVolontaireDTO(1, 20, 1);  // Déjà payé
        EtudeVolontaireDTO vol3 = createVolontaireDTO(1, 30, 0);  // Annulé
        EtudeVolontaireDTO vol4 = createVolontaireDTO(1, 40, 0);  // À payer
        EtudeVolontaireDTO vol5 = createVolontaireDTO(1, 50, 1);  // Déjà payé
        associations.add(vol1);
        associations.add(vol2);
        associations.add(vol3);
        associations.add(vol4);
        associations.add(vol5);

        AnnulationDTO annulation = new AnnulationDTO();
        annulation.setIdVol(30);
        annulations.add(annulation);

        when(etudeVolontaireService.getEtudeVolontairesByEtude(idEtude)).thenReturn(associations);
        when(annulationService.getAnnulationsByEtude(idEtude)).thenReturn(annulations);
        when(etudeVolontaireService.updatePaye(any(EtudeVolontaireId.class), eq(1))).thenReturn(vol1);
        when(etudeService.updatePayeStatus(idEtude, 2)).thenReturn(true);

        // Act
        PaymentBatchResultDTO result = paymentBatchService.markAllAsPaid(idEtude);

        // Assert
        assertThat(result.getProcessedCount()).isEqualTo(5);
        assertThat(result.getUpdatedCount()).isEqualTo(2);      // vol1 et vol4
        assertThat(result.getAlreadyPaidCount()).isEqualTo(2);  // vol2 et vol5
        assertThat(result.getSkippedAnnules()).isEqualTo(1);    // vol3
        assertThat(result.getErrorCount()).isEqualTo(0);
        
        verify(etudeVolontaireService, times(2)).updatePaye(any(EtudeVolontaireId.class), eq(1));
    }

    // ==================== Méthode utilitaire ====================

    private EtudeVolontaireDTO createVolontaireDTO(int idEtude, int idVolontaire, int paye) {
        EtudeVolontaireDTO dto = new EtudeVolontaireDTO();
        dto.setIdEtude(idEtude);
        dto.setIdVolontaire(idVolontaire);
        dto.setIdGroupe(1);
        dto.setIv(1);
        dto.setNumsujet(1);
        dto.setPaye(paye);
        dto.setStatut("Actif");
        return dto;
    }
}
