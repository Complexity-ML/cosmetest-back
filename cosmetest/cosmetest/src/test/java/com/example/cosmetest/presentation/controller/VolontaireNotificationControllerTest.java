package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.service.PhotoProxyService;
import com.example.cosmetest.business.service.VolontaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VolontaireNotificationControllerTest {

    private MockMvc mockMvc;
    private VolontaireService volontaireService;

    @BeforeEach
    void setUp() {
        volontaireService = mock(VolontaireService.class);
        VolontaireController controller = new VolontaireController(
                volontaireService,
                mock(AuditLogService.class),
                mock(PhotoProxyService.class));
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void exposesTodayNotificationsAndClampsTheLimit() throws Exception {
        mockMvc.perform(get("/api/volontaires/notifications/today").param("limit", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.date").isString());

        verify(volontaireService).getTodayNotifications(100);
    }
}
