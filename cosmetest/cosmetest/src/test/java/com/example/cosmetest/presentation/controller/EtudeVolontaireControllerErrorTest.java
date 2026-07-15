package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.business.service.GroupeService;
import com.example.cosmetest.business.service.RdvService;
import com.example.cosmetest.exception.GlobalExceptionHandler;
import com.example.cosmetest.exception.AmbiguousEtudeVolontaireException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EtudeVolontaireControllerErrorTest {

    private EtudeVolontaireService service;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        service = mock(EtudeVolontaireService.class);
        EtudeVolontaireController controller = new EtudeVolontaireController(
                service,
                mock(RdvService.class),
                mock(GroupeService.class),
                mock(EtudeService.class),
                mock(AuditLogService.class));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void paiementsDoNotExposeServiceFailureDetails() throws Exception {
        when(service.getAllEtudeVolontairesPaginated(any(Pageable.class)))
                .thenThrow(new RuntimeException("SQL etude_volontaire secret"));

        mockMvc.perform(get("/api/etude-volontaires/paiements"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.details").value("Erreur enregistrée côté serveur"))
                .andExpect(content().string(not(containsString("etude_volontaire"))));
    }

    @Test
    void sharedReadHandlerDoesNotExposeServiceFailureDetails() throws Exception {
        when(service.getEtudeVolontairesByEtude(12))
                .thenThrow(new RuntimeException("jdbc:mysql://secret-host"));

        mockMvc.perform(get("/api/etude-volontaires/etude/12"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(not(containsString("secret-host"))));
    }

    @Test
    void createPreservesTypedConflictInsteadOfMaskingItAsRuntimeException() throws Exception {
        when(service.saveEtudeVolontaire(any(EtudeVolontaireDTO.class)))
                .thenThrow(new AmbiguousEtudeVolontaireException(
                        "Association existante: utiliser son ID technique"));

        mockMvc.perform(post("/api/etude-volontaires")
                        .contentType("application/json")
                        .content("""
                                {"idEtude":10,"idGroupe":2,"idVolontaire":7,
                                 "iv":50,"numsujet":4,"paye":0,"statut":"INCLUS"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Association étude-volontaire ambiguë"));
    }

    @Test
    void simplifiedPaymentEndpointMatchesTheFrontendContract() throws Exception {
        EtudeVolontaireDTO association = new EtudeVolontaireDTO();
        association.setIdEtude(10);
        association.setIdGroupe(2);
        association.setIdVolontaire(7);
        association.setIv(50);
        association.setNumsujet(4);
        association.setPaye(0);
        association.setStatut("INCLUS");
        EtudeVolontaireDTO updated = new EtudeVolontaireDTO();
        updated.setIdEtude(10);
        updated.setIdVolontaire(7);
        updated.setPaye(1);
        when(service.getEtudeVolontairesByEtude(10)).thenReturn(java.util.List.of(association));
        when(service.updatePaye(any(com.example.cosmetest.domain.model.EtudeVolontaireId.class), eq(1))).thenReturn(updated);

        mockMvc.perform(patch("/api/etude-volontaires/update-paiement")
                        .param("idEtude", "10")
                        .param("idVolontaire", "7")
                        .param("nouveauStatutPaiement", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.paye").value(1));
    }
}
