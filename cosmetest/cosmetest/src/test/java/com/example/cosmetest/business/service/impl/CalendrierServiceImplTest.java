package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.CalendrierDTO;
import com.example.cosmetest.business.dto.EtudeDTO;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.data.repository.*;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour CalendrierServiceImpl
 * Service de gestion du calendrier et de planification des RDV
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CalendrierServiceImpl - Tests unitaires")
class CalendrierServiceImplTest {

    @Mock
    private RdvRepository rdvRepository;

    @Mock
    private EtudeRepository etudeRepository;

    @Mock
    private VolontaireRepository volontaireRepository;

    @Mock
    private EtudeVolontaireRepository etudeVolontaireRepository;

    @Mock
    private EtudeService etudeService;

    @InjectMocks
    private CalendrierServiceImpl calendrierService;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Rdv rdv;
    private EtudeDTO etudeDTO;

    @BeforeEach
    void setUp() {
        dateDebut = LocalDate.now();
        dateFin = dateDebut.plusDays(7);

        rdv = new Rdv();
        RdvId rdvId = new RdvId();
        rdvId.setIdRdv(1);
        rdvId.setIdEtude(1);
        rdv.setId(rdvId);
        rdv.setDate(Date.valueOf(dateDebut));
        rdv.setHeure("10:00");
        rdv.setEtat("Planifié");
        rdv.setIdVolontaire(1);

        etudeDTO = new EtudeDTO();
        etudeDTO.setIdEtude(1);
        etudeDTO.setTitre("Étude Test");
    }

    // ==================== Tests getDonneesSemaineOptimisees() ====================

    @Test
    @DisplayName("getDonneesSemaineOptimisees() - Récupération des données de la semaine")
    void testGetDonneesSemaineOptimisees_Success() {
        // Arrange
        LocalDate lundi = LocalDate.now().with(DayOfWeek.MONDAY);

        // Act
        CalendrierDTO result = calendrierService.getDonneesSemaineOptimisees(lundi);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDateDebut().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result.getDateFin().getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
    }

    @Test
    @DisplayName("getDonneesSemaineOptimisees() - Date en milieu de semaine")
    void testGetDonneesSemaineOptimisees_MidWeek() {
        // Arrange
        LocalDate mercredi = LocalDate.now().with(DayOfWeek.WEDNESDAY);

        // Act
        CalendrierDTO result = calendrierService.getDonneesSemaineOptimisees(mercredi);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDateDebut().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(result.getDateFin().getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
    }

    // ==================== Tests getRdvsEtudeAvecDetails() ====================

    @Test
    @DisplayName("getRdvsEtudeAvecDetails() - Récupération des RDV avec pagination")
    void testGetRdvsEtudeAvecDetails_Success() {
        // Arrange
        Page<Rdv> rdvPage = new PageImpl<>(Collections.singletonList(rdv));
        when(etudeService.getEtudeById(1)).thenReturn(Optional.of(etudeDTO));
        when(rdvRepository.findByIdEtudeWithDetailsOptimized(eq(1), any(Pageable.class)))
            .thenReturn(rdvPage);

        // Act
        Map<String, Object> result = calendrierService.getRdvsEtudeAvecDetails(1, 0, 10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKeys("etude", "rdvs", "pagination");
        verify(etudeService, times(1)).getEtudeById(1);
    }

    @Test
    @DisplayName("getRdvsEtudeAvecDetails() - Pagination correcte")
    void testGetRdvsEtudeAvecDetails_Pagination() {
        // Arrange
        Page<Rdv> rdvPage = new PageImpl<>(Collections.singletonList(rdv), 
            PageRequest.of(1, 5), 20);
        when(etudeService.getEtudeById(1)).thenReturn(Optional.of(etudeDTO));
        when(rdvRepository.findByIdEtudeWithDetailsOptimized(eq(1), any(Pageable.class)))
            .thenReturn(rdvPage);

        // Act
        Map<String, Object> result = calendrierService.getRdvsEtudeAvecDetails(1, 1, 5);

        // Assert
        assertThat(result).isNotNull();
        @SuppressWarnings("unchecked")
        Map<String, Object> pagination = (Map<String, Object>) result.get("pagination");
        assertThat(pagination.get("page")).isEqualTo(1);
        assertThat(pagination.get("taille")).isEqualTo(5);
        assertThat(pagination.get("totalElements")).isEqualTo(20L);
    }

    // ==================== Tests getStatistiquesPeriode() ====================

    @Test
    @DisplayName("getStatistiquesPeriode() - Calcul des statistiques")
    void testGetStatistiquesPeriode_Success() {
        // Arrange
        when(rdvRepository.countByDateBetween(any(Date.class), any(Date.class))).thenReturn(10);
        when(rdvRepository.countRdvByEtatBetweenDates(any(Date.class), any(Date.class)))
            .thenReturn(Map.of("Planifié", 5, "Effectué", 3, "Annulé", 2));
        when(etudeRepository.countEtudesActivesEntreDates(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(3);
        when(rdvRepository.countRdvByDayOfWeekBetweenDates(any(Date.class), any(Date.class)))
            .thenReturn(Map.of("MONDAY", 2, "WEDNESDAY", 3));
        when(rdvRepository.countRdvByHourBetweenDates(any(Date.class), any(Date.class)))
            .thenReturn(Map.of("09:00", 4, "14:00", 6));

        // Act
        Map<String, Object> result = calendrierService.getStatistiquesPeriode(dateDebut, dateFin);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKeys("totalRdv", "repartitionEtats", "totalEtudes", 
                                        "repartitionJours", "repartitionHeures");
        assertThat(result.get("totalRdv")).isEqualTo(10);
        assertThat(result.get("totalEtudes")).isEqualTo(3);
    }

    @Test
    @DisplayName("getStatistiquesPeriode() - Période sans données")
    void testGetStatistiquesPeriode_NoData() {
        // Arrange
        when(rdvRepository.countByDateBetween(any(Date.class), any(Date.class))).thenReturn(0);
        when(rdvRepository.countRdvByEtatBetweenDates(any(Date.class), any(Date.class)))
            .thenReturn(Collections.emptyMap());
        when(etudeRepository.countEtudesActivesEntreDates(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(0);
        when(rdvRepository.countRdvByDayOfWeekBetweenDates(any(Date.class), any(Date.class)))
            .thenReturn(Collections.emptyMap());
        when(rdvRepository.countRdvByHourBetweenDates(any(Date.class), any(Date.class)))
            .thenReturn(Collections.emptyMap());

        // Act
        Map<String, Object> result = calendrierService.getStatistiquesPeriode(dateDebut, dateFin);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("totalRdv")).isEqualTo(0);
        assertThat(result.get("totalEtudes")).isEqualTo(0);
    }

    // ==================== Tests getCreneauxLibres() ====================

    @Test
    @DisplayName("getCreneauxLibres() - Recherche de créneaux libres")
    void testGetCreneauxLibres_Success() {
        // Act
        Map<String, Object> result = calendrierService.getCreneauxLibres(dateDebut, dateFin, "09:00", "18:00");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("creneaux");
    }

    // ==================== Tests getDonneesJournee() ====================

    @Test
    @DisplayName("getDonneesJournee() - Données d'une journée spécifique")
    void testGetDonneesJournee_Success() {
        // Arrange
        LocalDate jour = LocalDate.now();

        // Act
        Map<String, Object> result = calendrierService.getDonneesJournee(jour);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKeys("date", "rdvs");
    }

    // ==================== Tests genererRapportUtilisation() ====================

    @Test
    @DisplayName("genererRapportUtilisation() - Génération du rapport")
    void testGenererRapportUtilisation_Success() {
        // Arrange
        when(rdvRepository.countByDateBetween(any(Date.class), any(Date.class))).thenReturn(50);
        when(etudeRepository.countEtudesActivesEntreDates(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(5);

        // Act
        Map<String, Object> result = calendrierService.genererRapportUtilisation(dateDebut, dateFin);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKeys("totalRdv", "totalEtudes");
        assertThat(result.get("totalRdv")).isEqualTo(50);
        assertThat(result.get("totalEtudes")).isEqualTo(5);
    }

    @Test
    @DisplayName("genererRapportUtilisation() - Période sans activité")
    void testGenererRapportUtilisation_NoActivity() {
        // Arrange
        when(rdvRepository.countByDateBetween(any(Date.class), any(Date.class))).thenReturn(0);
        when(etudeRepository.countEtudesActivesEntreDates(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(0);

        // Act
        Map<String, Object> result = calendrierService.genererRapportUtilisation(dateDebut, dateFin);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("totalRdv")).isEqualTo(0);
        assertThat(result.get("totalEtudes")).isEqualTo(0);
    }

    // ==================== Tests getTendancesUtilisation() ====================

    @Test
    @DisplayName("getTendancesUtilisation() - Analyse des tendances sur un mois")
    void testGetTendancesUtilisation_Success() {
        // Arrange
        int nombreJours = 30;

        // Act
        Map<String, Object> result = calendrierService.getTendancesUtilisation(nombreJours);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).containsKey("periode");
    }

    // ==================== Tests invaliderCacheCalendrier() ====================

    @Test
    @DisplayName("invaliderCacheCalendrier() - Invalidation du cache")
    void testInvaliderCacheCalendrier() {
        // Act & Assert - Ne devrait pas lever d'exception
        assertThatCode(() -> calendrierService.invaliderCacheCalendrier())
            .doesNotThrowAnyException();
    }
}
