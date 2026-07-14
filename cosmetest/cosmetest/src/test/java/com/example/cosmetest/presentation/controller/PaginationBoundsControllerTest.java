package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.business.service.AnnulationService;
import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.business.service.GroupeService;
import com.example.cosmetest.business.service.RdvService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaginationBoundsControllerTest {

    @Test
    void etudeVolontairesBorneLaTailleEtNormaliseLaPage() {
        EtudeVolontaireService service = mock(EtudeVolontaireService.class);
        when(service.getAllEtudeVolontairesPaginated(any(Pageable.class)))
                .thenReturn(Page.empty());
        EtudeVolontaireController controller = new EtudeVolontaireController(
                service, mock(RdvService.class), mock(GroupeService.class),
                mock(EtudeService.class), mock(AuditLogService.class));

        controller.getAllEtudeVolontaires(-4, 10_000);

        var pageable = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(service).getAllEtudeVolontairesPaginated(pageable.capture());
        assertEquals(0, pageable.getValue().getPageNumber());
        assertEquals(100, pageable.getValue().getPageSize());
    }

    @Test
    void paiementsRetourneUnePageBornee() {
        EtudeVolontaireService service = mock(EtudeVolontaireService.class);
        Page<EtudeVolontaireDTO> expected = new PageImpl<>(List.of());
        when(service.getAllEtudeVolontairesPaginated(any(Pageable.class))).thenReturn(expected);
        EtudeVolontaireController controller = new EtudeVolontaireController(
                service, mock(RdvService.class), mock(GroupeService.class),
                mock(EtudeService.class), mock(AuditLogService.class));

        var response = controller.getAllPaiements(0, Integer.MAX_VALUE);

        assertEquals(expected, response.getBody().getData());
        var pageable = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(service).getAllEtudeVolontairesPaginated(pageable.capture());
        assertEquals(100, pageable.getValue().getPageSize());
    }

    @Test
    void annulationsRacineRetourneUnePageBornee() {
        AnnulationService service = mock(AnnulationService.class);
        when(service.getAllAnnulationsPaginated(any(Pageable.class))).thenReturn(Page.empty());
        AnnulationController controller = new AnnulationController(service, mock(AuditLogService.class));

        controller.getAllAnnulations(-1, Integer.MAX_VALUE);

        var pageable = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(service).getAllAnnulationsPaginated(pageable.capture());
        assertEquals(0, pageable.getValue().getPageNumber());
        assertEquals(100, pageable.getValue().getPageSize());
    }

    @Test
    void annulationsBorneLaTailleDePage() {
        AnnulationService service = mock(AnnulationService.class);
        when(service.getAllAnnulationsPaginated(any(Pageable.class))).thenReturn(Page.empty());
        AnnulationController controller = new AnnulationController(service, mock(AuditLogService.class));

        controller.getAllAnnulationsPaginated(-1, 5_000, "dateAnnulation", "DESC");

        var pageable = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(service).getAllAnnulationsPaginated(pageable.capture());
        assertEquals(0, pageable.getValue().getPageNumber());
        assertEquals(100, pageable.getValue().getPageSize());
    }

    @Test
    void rdvsBorneAussiLesTaillesNegatives() {
        RdvService service = mock(RdvService.class);
        when(service.getAllRdvsPaginated(any(Pageable.class))).thenReturn(Page.empty());
        RdvController controller = new RdvController(service, mock(EtudeService.class), mock(AuditLogService.class));

        controller.getPaginated(-2, -10, "date,desc");

        var pageable = org.mockito.ArgumentCaptor.forClass(Pageable.class);
        verify(service).getAllRdvsPaginated(pageable.capture());
        assertEquals(0, pageable.getValue().getPageNumber());
        assertEquals(1, pageable.getValue().getPageSize());
    }
}
