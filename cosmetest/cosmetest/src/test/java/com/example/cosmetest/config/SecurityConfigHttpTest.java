package com.example.cosmetest.config;

import com.example.cosmetest.business.service.ActiveSessionService;
import com.example.cosmetest.business.service.AuthService;
import com.example.cosmetest.business.service.ConnexionLogService;
import com.example.cosmetest.business.service.CustomUserDetailsService;
import com.example.cosmetest.exception.GlobalExceptionHandler;
import com.example.cosmetest.presentation.controller.AuthController;
import com.example.cosmetest.security.ApiAccessDeniedHandler;
import com.example.cosmetest.security.JwtAuthenticationEntryPoint;
import com.example.cosmetest.security.JwtAuthenticationFilter;
import com.example.cosmetest.security.JwtCookieFilter;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class, SecurityConfig.class,
        ApiAccessDeniedHandler.class, JwtAuthenticationEntryPoint.class, GlobalExceptionHandler.class})
class SecurityConfigHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private ConnexionLogService connexionLogService;
    @MockitoBean
    private ActiveSessionService activeSessionService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockitoBean
    private JwtCookieFilter jwtCookieFilter;

    @BeforeEach
    void laisserPasserLesFiltresJwtMockes() throws Exception {
        doAnswer(invocation -> {
            invocation.<FilterChain>getArgument(2).doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
        doAnswer(invocation -> {
            invocation.<FilterChain>getArgument(2).doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtCookieFilter).doFilter(any(), any(), any());
    }

    @Test
    void endpointUtilisateurAuthNEstPasPublicEtRetourneLe401JsonCommun() throws Exception {
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/api/auth/user"));
    }

    @Test
    void endpointNotificationsNEstPasPublic() throws Exception {
        mockMvc.perform(get("/api/volontaires/notifications/today"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/api/volontaires/notifications/today"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void endpointMetierEstAccessibleAuxUtilisateursAuthentifies() throws Exception {
        mockMvc.perform(post("/api/etude-volontaires/repair/12"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void journauxAuditEtConnexionRestentReservesAuxAdministrateurs() throws Exception {
        mockMvc.perform(get("/api/audit"))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/connexions"))
                .andExpect(status().isForbidden());
    }
}
