package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.domain.model.AuditLog;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuditControllerActionFilterTest {

    @Test
    void actionParameterFiltersAuditLogs() throws Exception {
        AuditLogService service = mock(AuditLogService.class);
        AuditLog delete = auditLog(2L, "bob", AuditLog.Action.DELETE);
        when(service.search(isNull(), isNull(), eq(AuditLog.Action.DELETE),
                isNull(), isNull(), eq(0), eq(50))).thenReturn(
                new PageImpl<>(List.of(delete), PageRequest.of(0, 50), 1));

        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new AuditController(service))
                .build();

        mvc.perform(get("/api/audit").param("action", "DELETE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].action").value("DELETE"));

        verify(service).search(null, null, AuditLog.Action.DELETE, null, null, 0, 50);
    }

    @Test
    void actionCombinesWithUserAndDateRange() throws Exception {
        AuditLogService service = mock(AuditLogService.class);
        LocalDateTime debut = LocalDateTime.of(2026, 7, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 7, 16, 23, 59, 59);
        when(service.search(null, "alice", AuditLog.Action.UPDATE, debut, fin, 0, 50))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 50), 0));

        MockMvc mvc = MockMvcBuilders.standaloneSetup(new AuditController(service)).build();

        mvc.perform(get("/api/audit")
                        .param("action", "update")
                        .param("utilisateur", " alice ")
                        .param("dateDebut", "2026-07-01")
                        .param("dateFin", "2026-07-16"))
                .andExpect(status().isOk());

        verify(service).search(null, "alice", AuditLog.Action.UPDATE, debut, fin, 0, 50);
    }

    @Test
    void unknownActionIsRejected() throws Exception {
        AuditLogService service = mock(AuditLogService.class);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new AuditController(service)).build();

        mvc.perform(get("/api/audit").param("action", "DROP_TABLE"))
                .andExpect(status().isBadRequest());
    }

    private static AuditLog auditLog(Long id, String user, AuditLog.Action action) {
        AuditLog log = mock(AuditLog.class);
        when(log.getId()).thenReturn(id);
        when(log.getUtilisateur()).thenReturn(user);
        when(log.getAction()).thenReturn(action);
        when(log.getEntite()).thenReturn("VOLONTAIRE");
        when(log.getEntiteId()).thenReturn(id.toString());
        when(log.getCreatedAt()).thenReturn(LocalDateTime.of(2026, 7, 16, 10, 0));
        return log;
    }
}
