package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.service.PaiementStatsService;
import com.example.cosmetest.business.service.PaymentBatchService;
import com.example.cosmetest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaiementControllerErrorTest {

    private PaymentBatchService paymentBatchService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        paymentBatchService = mock(PaymentBatchService.class);
        PaiementController controller = new PaiementController(
                paymentBatchService, mock(PaiementStatsService.class), mock(AuditLogService.class));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void neContournePasLaPolitiqueCorsGlobaleAvecUnWildcard() {
        assertThat(PaiementController.class.getAnnotation(CrossOrigin.class)).isNull();
    }

    @Test
    void argumentInvalideUtiliseLeFormatGlobal400() throws Exception {
        when(paymentBatchService.markAllAsPaid(7))
                .thenThrow(new IllegalArgumentException("Étude inconnue"));

        mockMvc.perform(post("/api/paiements/etudes/7/mark-all-paid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Argument invalide"))
                .andExpect(jsonPath("$.details").value("Étude inconnue"));
    }

    @Test
    void erreurTechniqueNeDivulguePasLeMessageDuService() throws Exception {
        when(paymentBatchService.markAllAsPaid(7))
                .thenThrow(new IllegalStateException("jdbc:postgresql://secret/payment"));

        mockMvc.perform(post("/api/paiements/etudes/7/mark-all-paid"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Une erreur interne s'est produite"))
                .andExpect(content().string(not(containsString("secret/payment"))));
    }
}
