package com.example.cosmetest.presentation.controller;

import com.example.cosmetest.business.dto.IdentifiantDTO;
import com.example.cosmetest.business.service.AuditLogService;
import com.example.cosmetest.business.service.IdentifiantService;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import com.example.cosmetest.domain.model.Identifiant;
import com.example.cosmetest.security.IdentifiantAuthorization;
import com.example.cosmetest.security.ApiAccessDeniedHandler;
import com.example.cosmetest.security.JwtAuthenticationEntryPoint;
import com.example.cosmetest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdentifiantController.class)
@ContextConfiguration(classes = {
        IdentifiantController.class,
        IdentifiantAuthorization.class,
        ApiAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class,
        GlobalExceptionHandler.class,
        IdentifiantControllerSecurityTest.MethodSecurityTestConfig.class
})
class IdentifiantControllerSecurityTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http,
                                                    ApiAccessDeniedHandler accessDeniedHandler,
                                                    JwtAuthenticationEntryPoint authenticationEntryPoint) throws Exception {
            return http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                    .exceptionHandling(exceptions -> exceptions
                            .authenticationEntryPoint(authenticationEntryPoint)
                            .accessDeniedHandler(accessDeniedHandler))
                    .build();
        }
    }

    @jakarta.annotation.Resource
    private MockMvc mockMvc;

    @MockitoBean
    private IdentifiantService identifiantService;

    @MockitoBean
    private AuditLogService auditLogService;

    @MockitoBean
    private IdentifiantRepository identifiantRepository;

    @Test
    void utilisateurNePeutPasCreerUnCompte() throws Exception {
        mockMvc.perform(post("/api/identifiants")
                        .with(user("alice").roles("USER"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(validIdentifiantJson("alice", "UTILISATEUR")))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.path").value("/api/identifiants"));

        verify(identifiantService, never()).createIdentifiant(any());
    }

    @Test
    void requeteNonAuthentifieeRecoitUneErreur401JsonUniforme() throws Exception {
        mockMvc.perform(get("/api/identifiants").with(anonymous()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/api/identifiants"));
    }

    @Test
    void adminPeutCreerUnCompte() throws Exception {
        IdentifiantDTO created = dto(2, "bob", "UTILISATEUR");
        when(identifiantService.createIdentifiant(any())).thenReturn(created);

        mockMvc.perform(post("/api/identifiants")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(validIdentifiantJson("bob", "UTILISATEUR")))
                .andExpect(status().isCreated());
    }

    @Test
    void argumentInvalideEstFormateParLeGestionnaireGlobal() throws Exception {
        when(identifiantService.createIdentifiant(any()))
                .thenThrow(new IllegalArgumentException("Login déjà utilisé"));

        mockMvc.perform(post("/api/identifiants")
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(validIdentifiantJson("alice", "UTILISATEUR")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Argument invalide"))
                .andExpect(jsonPath("$.details").value("Login déjà utilisé"));
    }

    @Test
    void compteAbsentEstFormateEn404ParLeGestionnaireGlobal() throws Exception {
        when(identifiantService.getIdentifiantById(404)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/identifiants/404")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Ressource non trouvée"));
    }

    @Test
    void utilisateurPeutLireLesComptes() throws Exception {
        Identifiant alice = entity(1, "alice", "UTILISATEUR");
        Identifiant bob = entity(2, "bob", "UTILISATEUR");
        when(identifiantRepository.findById(1)).thenReturn(Optional.of(alice));
        when(identifiantRepository.findById(2)).thenReturn(Optional.of(bob));
        when(identifiantService.getIdentifiantById(1)).thenReturn(Optional.of(dto(1, "alice", "UTILISATEUR")));
        when(identifiantService.getIdentifiantById(2)).thenReturn(Optional.of(dto(2, "bob", "UTILISATEUR")));

        mockMvc.perform(get("/api/identifiants/1").with(user("alice").roles("USER")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/identifiants/2").with(user("alice").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void utilisateurPeutGererLesComptesExistants() throws Exception {
        Identifiant alice = entity(1, "alice", "UTILISATEUR");
        when(identifiantRepository.findById(1)).thenReturn(Optional.of(alice));
        when(identifiantService.deleteIdentifiant(1)).thenReturn(true);
        when(identifiantService.updateIdentifiant(eq(1), any()))
                .thenReturn(Optional.of(dto(1, "alice", "ADMIN")));

        mockMvc.perform(delete("/api/identifiants/1")
                        .with(user("alice").roles("USER"))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(put("/api/identifiants/1")
                        .with(user("alice").roles("USER"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(validIdentifiantJson("alice", "ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void utilisateurPeutChangerLesMotsDePasseDesComptesExistants() throws Exception {
        when(identifiantRepository.findById(1)).thenReturn(Optional.of(entity(1, "alice", "UTILISATEUR")));
        when(identifiantRepository.findById(2)).thenReturn(Optional.of(entity(2, "bob", "UTILISATEUR")));
        when(identifiantService.changerMotDePasse(1, "ancien123", "nouveau123")).thenReturn(true);
        when(identifiantService.changerMotDePasse(2, "ancien123", "nouveau123")).thenReturn(true);
        String body = "{\"ancienMotDePasse\":\"ancien123\",\"nouveauMotDePasse\":\"nouveau123\"}";

        mockMvc.perform(post("/api/identifiants/1/changer-mot-de-passe")
                        .with(user("alice").roles("USER"))
                        .with(csrf()).contentType("application/json").content(body))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/identifiants/2/changer-mot-de-passe")
                        .with(user("alice").roles("USER"))
                        .with(csrf()).contentType("application/json").content(body))
                .andExpect(status().isOk());
    }

    private static String validIdentifiantJson(String login, String role) {
        return "{\"identifiant\":\"" + login + "\",\"mdpIdentifiant\":\"secret123\","
                + "\"mailIdentifiant\":\"" + login + "@example.com\",\"role\":\"" + role + "\"}";
    }

    private static Identifiant entity(int id, String login, String role) {
        Identifiant value = new Identifiant();
        value.setIdIdentifiant(id);
        value.setIdentifiant(login);
        value.setRole(role);
        return value;
    }

    private static IdentifiantDTO dto(int id, String login, String role) {
        return IdentifiantDTO.withoutPassword(id, login, login + "@example.com", role, null);
    }
}
