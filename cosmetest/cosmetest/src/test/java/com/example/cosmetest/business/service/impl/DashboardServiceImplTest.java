package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.*;
import com.example.cosmetest.business.service.*;
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
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour DashboardServiceImpl
 * Teste l'agrégation des statistiques du tableau de bord
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardServiceImpl - Tests unitaires")
class DashboardServiceImplTest {

    @Mock
    private VolontaireService volontaireService;

    @Mock
    private EtudeService etudeService;

    @Mock
    private RdvService rdvService;

    @Mock
    private PreinscritService preinscritService;

    @Mock
    private RdvRepository rdvRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private VolontaireDTO volontaireDTO;
    private EtudeDTO etudeDTO;
    private RdvDTO rdvDTO;

    @BeforeEach
    void setUp() {
        volontaireDTO = new VolontaireDTO();
        volontaireDTO.setIdVol(1);
        volontaireDTO.setPrenomVol("Jean");
        volontaireDTO.setNomVol("Dupont");
        volontaireDTO.setDateI(LocalDate.now().minusDays(1));

        etudeDTO = new EtudeDTO();
        etudeDTO.setIdEtude(1);
        etudeDTO.setRef("ETU-001");

        rdvDTO = new RdvDTO();
        rdvDTO.setIdRdv(1);
        rdvDTO.setIdEtude(1);
        rdvDTO.setDate(Date.valueOf(LocalDate.now().plusDays(1)));
        rdvDTO.setHeure("10:00:00");
    }

    // ==================== Tests getDashboardStats() ====================

    @Test
    @DisplayName("getDashboardStats() - Récupération des statistiques du dashboard")
    void testGetDashboardStats_Success() {
        // Arrange
        when(volontaireService.countActiveVolontaires()).thenReturn(150);
        // L'implémentation utilise getCurrentEtudes().size() au lieu de countCurrentEtudes()
        List<EtudeDTO> etudes = Arrays.asList(
                new EtudeDTO(), new EtudeDTO(), new EtudeDTO(), new EtudeDTO(), new EtudeDTO(),
                new EtudeDTO(), new EtudeDTO(), new EtudeDTO(), new EtudeDTO(), new EtudeDTO()
        ); // 10 études
        when(etudeService.getCurrentEtudes()).thenReturn(etudes);
        when(rdvService.countRdvForToday()).thenReturn(5);
        when(preinscritService.countPreinscrits()).thenReturn(25);

        // Act
        DashboardStatsDTO result = dashboardService.getDashboardStats();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getVolontairesActifs()).isEqualTo(150);
        assertThat(result.getEtudesEnCours()).isEqualTo(10);
        assertThat(result.getRdvToday()).isEqualTo(5);
        assertThat(result.getPreinscrits()).isEqualTo(25);

        verify(volontaireService, times(1)).countActiveVolontaires();
        verify(etudeService, times(1)).getCurrentEtudes();
        verify(rdvService, times(1)).countRdvForToday();
        verify(preinscritService, times(1)).countPreinscrits();
    }

    @Test
    @DisplayName("getDashboardStats() - Toutes les statistiques à zéro")
    void testGetDashboardStats_AllZero() {
        // Arrange
        when(volontaireService.countActiveVolontaires()).thenReturn(0);
        // L'implémentation utilise getCurrentEtudes().size() au lieu de countCurrentEtudes()
        when(etudeService.getCurrentEtudes()).thenReturn(Collections.emptyList());
        when(rdvService.countRdvForToday()).thenReturn(0);
        when(preinscritService.countPreinscrits()).thenReturn(0);

        // Act
        DashboardStatsDTO result = dashboardService.getDashboardStats();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getVolontairesActifs()).isEqualTo(0);
        assertThat(result.getEtudesEnCours()).isEqualTo(0);
        assertThat(result.getRdvToday()).isEqualTo(0);
        assertThat(result.getPreinscrits()).isEqualTo(0);
    }

    @Test
    @DisplayName("getDashboardStats() - Grandes valeurs")
    void testGetDashboardStats_LargeValues() {
        // Arrange
        when(volontaireService.countActiveVolontaires()).thenReturn(10000);
        // L'implémentation utilise getCurrentEtudes().size() au lieu de countCurrentEtudes()
        // Créer une liste de 500 études
        List<EtudeDTO> largeEtudesList = new java.util.ArrayList<>();
        for (int i = 0; i < 500; i++) {
            largeEtudesList.add(new EtudeDTO());
        }
        when(etudeService.getCurrentEtudes()).thenReturn(largeEtudesList);
        when(rdvService.countRdvForToday()).thenReturn(200);
        when(preinscritService.countPreinscrits()).thenReturn(1500);

        // Act
        DashboardStatsDTO result = dashboardService.getDashboardStats();

        // Assert
        assertThat(result.getVolontairesActifs()).isEqualTo(10000);
        assertThat(result.getEtudesEnCours()).isEqualTo(500);
        assertThat(result.getRdvToday()).isEqualTo(200);
        assertThat(result.getPreinscrits()).isEqualTo(1500);
    }

    // ==================== Tests getDailyStats() ====================

    @Test
    @DisplayName("getDailyStats() - Récupération des statistiques quotidiennes")
    void testGetDailyStats_Success() {
        // Arrange
        when(volontaireService.countVolontairesAddedToday()).thenReturn(5);
        when(rdvService.countCompletedRdvToday()).thenReturn(3);
        when(preinscritService.countNewPreinscritsToday()).thenReturn(8);

        // Act
        DailyStatsDTO result = dashboardService.getDailyStats();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getVolontairesAjoutes()).isEqualTo(5);
        assertThat(result.getRdvEffectues()).isEqualTo(3);
        assertThat(result.getNouvellesPreinscriptions()).isEqualTo(8);

        verify(volontaireService, times(1)).countVolontairesAddedToday();
        verify(rdvService, times(1)).countCompletedRdvToday();
        verify(preinscritService, times(1)).countNewPreinscritsToday();
    }

    @Test
    @DisplayName("getDailyStats() - Aucune activité aujourd'hui")
    void testGetDailyStats_NoActivity() {
        // Arrange
        when(volontaireService.countVolontairesAddedToday()).thenReturn(0);
        when(rdvService.countCompletedRdvToday()).thenReturn(0);
        when(preinscritService.countNewPreinscritsToday()).thenReturn(0);

        // Act
        DailyStatsDTO result = dashboardService.getDailyStats();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getVolontairesAjoutes()).isEqualTo(0);
        assertThat(result.getRdvEffectues()).isEqualTo(0);
        assertThat(result.getNouvellesPreinscriptions()).isEqualTo(0);
    }

    // ==================== Tests getProchainRdvs() ====================

    @Test
    @DisplayName("getProchainRdvs() - Récupération des prochains RDV")
    void testGetProchainRdvs_Success() {
        // Arrange
        RdvDTO rdv2 = new RdvDTO();
        rdv2.setIdRdv(2);
        rdv2.setIdEtude(2);

        List<RdvDTO> rdvs = Arrays.asList(rdvDTO, rdv2);
        when(rdvService.getUpcomingRdvs(5)).thenReturn(rdvs);

        // Act
        List<RdvDTO> result = dashboardService.getProchainRdvs(5);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIdRdv()).isEqualTo(1);
        verify(rdvService, times(1)).getUpcomingRdvs(5);
    }

    @Test
    @DisplayName("getProchainRdvs() - Aucun RDV à venir")
    void testGetProchainRdvs_Empty() {
        // Arrange
        when(rdvService.getUpcomingRdvs(5)).thenReturn(Collections.emptyList());

        // Act
        List<RdvDTO> result = dashboardService.getProchainRdvs(5);

        // Assert
        assertThat(result).isEmpty();
        verify(rdvService, times(1)).getUpcomingRdvs(5);
    }

    @Test
    @DisplayName("getProchainRdvs() - Limitation du nombre de résultats")
    void testGetProchainRdvs_WithLimit() {
        // Arrange
        List<RdvDTO> rdvs = Arrays.asList(rdvDTO);
        when(rdvService.getUpcomingRdvs(10)).thenReturn(rdvs);

        // Act
        List<RdvDTO> result = dashboardService.getProchainRdvs(10);

        // Assert
        assertThat(result).hasSize(1);
        verify(rdvService, times(1)).getUpcomingRdvs(10);
    }

    // ==================== Tests getRecentEtudes() ====================

    @Test
    @DisplayName("getRecentEtudes() - Récupération des études récentes")
    void testGetRecentEtudes_Success() {
        // Arrange
        EtudeDTO etude2 = new EtudeDTO();
        etude2.setIdEtude(2);
        etude2.setRef("ETU-002");

        List<EtudeDTO> etudes = Arrays.asList(etudeDTO, etude2);
        when(etudeService.getRecentEtudes(5)).thenReturn(etudes);

        // Act
        List<EtudeDTO> result = dashboardService.getRecentEtudes(5);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIdEtude()).isEqualTo(1);
        verify(etudeService, times(1)).getRecentEtudes(5);
    }

    @Test
    @DisplayName("getRecentEtudes() - Aucune étude récente")
    void testGetRecentEtudes_Empty() {
        // Arrange
        when(etudeService.getRecentEtudes(5)).thenReturn(Collections.emptyList());

        // Act
        List<EtudeDTO> result = dashboardService.getRecentEtudes(5);

        // Assert
        assertThat(result).isEmpty();
        verify(etudeService, times(1)).getRecentEtudes(5);
    }

    // ==================== Tests getRecentActivities() ====================

    @Test
    @DisplayName("getRecentActivities() - Activités avec volontaires et RDV")
    void testGetRecentActivities_Success() {
        // Arrange
        List<VolontaireDTO> volontaires = Collections.singletonList(volontaireDTO);
        List<RdvDTO> rdvs = Collections.singletonList(rdvDTO);

        when(volontaireService.getRecentVolontaires(10)).thenReturn(volontaires);
        when(rdvService.getRecentRdvs(10)).thenReturn(rdvs);

        // Act
        List<ActiviteRecenteDTO> result = dashboardService.getRecentActivities(10);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSizeGreaterThanOrEqualTo(1);
        verify(volontaireService, times(1)).getRecentVolontaires(10);
        verify(rdvService, times(1)).getRecentRdvs(10);
    }

    @Test
    @DisplayName("getRecentActivities() - Aucune activité récente")
    void testGetRecentActivities_Empty() {
        // Arrange
        when(volontaireService.getRecentVolontaires(10)).thenReturn(Collections.emptyList());
        when(rdvService.getRecentRdvs(10)).thenReturn(Collections.emptyList());

        // Act
        List<ActiviteRecenteDTO> result = dashboardService.getRecentActivities(10);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getRecentActivities() - Gestion des volontaires avec noms null")
    void testGetRecentActivities_WithNullNames() {
        // Arrange
        VolontaireDTO volontaireNull = new VolontaireDTO();
        volontaireNull.setIdVol(2);
        volontaireNull.setPrenomVol(null);
        volontaireNull.setNomVol(null);
        volontaireNull.setDateI(LocalDate.now());

        when(volontaireService.getRecentVolontaires(10)).thenReturn(Collections.singletonList(volontaireNull));
        when(rdvService.getRecentRdvs(10)).thenReturn(Collections.emptyList());

        // Act
        List<ActiviteRecenteDTO> result = dashboardService.getRecentActivities(10);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).contains("a ajouté le volontaire");
    }

    @Test
    @DisplayName("getRecentActivities() - Gestion des RDV avec date null")
    void testGetRecentActivities_WithNullDate() {
        // Arrange
        RdvDTO rdvNullDate = new RdvDTO();
        rdvNullDate.setIdRdv(2);
        rdvNullDate.setIdEtude(2);
        rdvNullDate.setDate(null); // Date null

        when(volontaireService.getRecentVolontaires(10)).thenReturn(Collections.emptyList());
        when(rdvService.getRecentRdvs(10)).thenReturn(Collections.singletonList(rdvNullDate));

        // Act
        List<ActiviteRecenteDTO> result = dashboardService.getRecentActivities(10);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDate()).isNotNull(); // Devrait utiliser LocalDate.now()
    }

    @Test
    @DisplayName("getRecentActivities() - Tri des activités par date")
    void testGetRecentActivities_Sorted() {
        // Arrange
        VolontaireDTO v1 = new VolontaireDTO();
        v1.setIdVol(1);
        v1.setPrenomVol("Jean");
        v1.setNomVol("Dupont");
        v1.setDateI(LocalDate.now().minusDays(2));

        VolontaireDTO v2 = new VolontaireDTO();
        v2.setIdVol(2);
        v2.setPrenomVol("Marie");
        v2.setNomVol("Martin");
        v2.setDateI(LocalDate.now().minusDays(1));

        when(volontaireService.getRecentVolontaires(10)).thenReturn(Arrays.asList(v1, v2));
        when(rdvService.getRecentRdvs(10)).thenReturn(Collections.emptyList());

        // Act
        List<ActiviteRecenteDTO> result = dashboardService.getRecentActivities(10);

        // Assert
        assertThat(result).hasSize(2);
        // La plus récente devrait être en premier
        assertThat(result.get(0).getDate()).isAfterOrEqualTo(result.get(1).getDate());
    }

    @Test
    @DisplayName("getRecentActivities() - Limitation du nombre de résultats")
    void testGetRecentActivities_LimitResults() {
        // Arrange
        List<VolontaireDTO> volontaires = Arrays.asList(
            createVolontaire(1, "Jean", "Dupont"),
            createVolontaire(2, "Marie", "Martin"),
            createVolontaire(3, "Pierre", "Durand")
        );

        when(volontaireService.getRecentVolontaires(2)).thenReturn(volontaires);
        when(rdvService.getRecentRdvs(2)).thenReturn(Collections.emptyList());

        // Act
        List<ActiviteRecenteDTO> result = dashboardService.getRecentActivities(2);

        // Assert
        assertThat(result).hasSizeLessThanOrEqualTo(2);
    }

    @Test
    @DisplayName("getRecentActivities() - Exception lors de la récupération")
    void testGetRecentActivities_Exception() {
        // Arrange
        when(volontaireService.getRecentVolontaires(10)).thenThrow(new RuntimeException("Database error"));

        // Act
        List<ActiviteRecenteDTO> result = dashboardService.getRecentActivities(10);

        // Assert - Le service doit gérer l'exception et retourner une liste vide
        assertThat(result).isEmpty();
    }

    // ==================== Tests getUpcomingRdvs() ====================

    @Test
    @DisplayName("getUpcomingRdvs() - Récupération des RDV à venir")
    void testGetUpcomingRdvs_Success() {
        // Arrange
        Rdv rdv1 = createRdv(1, 1, LocalDate.now().plusDays(1));
        Rdv rdv2 = createRdv(2, 2, LocalDate.now().plusDays(2));

        Page<Rdv> rdvPage = new PageImpl<>(Arrays.asList(rdv1, rdv2));
        when(rdvRepository.findByDateGreaterThanEqualOrderByDateAscHeureAsc(any(java.sql.Date.class), any(Pageable.class)))
            .thenReturn(rdvPage);

        // Act
        List<RdvDTO> result = dashboardService.getUpcomingRdvs(5);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getIdRdv()).isEqualTo(1);
        assertThat(result.get(1).getIdRdv()).isEqualTo(2);
        verify(rdvRepository, times(1)).findByDateGreaterThanEqualOrderByDateAscHeureAsc(any(java.sql.Date.class), any(Pageable.class));
    }

    @Test
    @DisplayName("getUpcomingRdvs() - Aucun RDV à venir")
    void testGetUpcomingRdvs_EmptyPage() {
        // Arrange
        Page<Rdv> emptyPage = new PageImpl<>(Collections.emptyList());
        when(rdvRepository.findByDateGreaterThanEqualOrderByDateAscHeureAsc(any(java.sql.Date.class), any(Pageable.class)))
            .thenReturn(emptyPage);

        // Act
        List<RdvDTO> result = dashboardService.getUpcomingRdvs(5);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getUpcomingRdvs() - Limite de 10 résultats")
    void testGetUpcomingRdvs_WithLimit() {
        // Arrange
        Rdv rdv = createRdv(1, 1, LocalDate.now().plusDays(1));
        Page<Rdv> rdvPage = new PageImpl<>(Collections.singletonList(rdv));
        
        when(rdvRepository.findByDateGreaterThanEqualOrderByDateAscHeureAsc(any(java.sql.Date.class), any(Pageable.class)))
            .thenReturn(rdvPage);

        // Act
        List<RdvDTO> result = dashboardService.getUpcomingRdvs(10);

        // Assert
        assertThat(result).hasSize(1);
        verify(rdvRepository, times(1)).findByDateGreaterThanEqualOrderByDateAscHeureAsc(any(java.sql.Date.class), any(Pageable.class));
    }

    // ==================== Méthodes utilitaires ====================

    private VolontaireDTO createVolontaire(Integer id, String prenom, String nom) {
        VolontaireDTO v = new VolontaireDTO();
        v.setIdVol(id);
        v.setPrenomVol(prenom);
        v.setNomVol(nom);
        v.setDateI(LocalDate.now().minusDays(id));
        return v;
    }

    private Rdv createRdv(Integer idRdv, Integer idEtude, LocalDate date) {
        Rdv rdv = new Rdv();
        RdvId rdvId = new RdvId();
        rdvId.setIdRdv(idRdv);
        rdvId.setIdEtude(idEtude);
        rdv.setId(rdvId);
        rdv.setDate(java.sql.Date.valueOf(date));
        rdv.setHeure("10:00:00");
        rdv.setEtat("Planifié");
        return rdv;
    }
}
