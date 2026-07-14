package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.service.ActiveSessionService;
import com.example.cosmetest.business.service.AuthService;
import com.example.cosmetest.business.service.ConnexionLogService;
import com.example.cosmetest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {
    private AuthService authService;
    private AuthenticationManager authenticationManager;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authenticationManager = mock(AuthenticationManager.class);
        AuthController controller = new AuthController(authService, authenticationManager,
                mock(ConnexionLogService.class), mock(ActiveSessionService.class));
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void loginPlaceLeJwtDansCookieHeaderEtCorpsJsonPourCompatibiliteProduction() throws Exception {
        String jwt = "jwt-ultra-secret";
        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("alice", null));
        when(authService.authenticate("alice", "secret123")).thenReturn(jwt);
        when(authService.getUsernameFromToken(jwt)).thenReturn("alice");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"alice\",\"motDePasse\":\"secret123\"}"))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", containsString("jwt=" + jwt)))
                .andExpect(header().string("Authorization", "Bearer " + jwt))
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.token").value(jwt));
    }

    @Test
    void erreurTechniqueDeLoginEstTraiteeGlobalementSansDivulgation() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("alice", null));
        when(authService.authenticate("alice", "secret123"))
                .thenThrow(new IllegalStateException("jdbc:postgresql://secret-host/auth"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"alice\",\"motDePasse\":\"secret123\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Une erreur interne s'est produite"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                        .string(not(containsString("secret-host"))));
    }

    @Test
    void identifiantsInvalidesRetournentLeMemeFormatJson401() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("mot de passe incorrect"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"alice\",\"motDePasse\":\"secret123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.path").value("/api/auth/login"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                        .string(not(containsString("mot de passe incorrect"))));
    }
}
