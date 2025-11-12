package com.example.cosmetest.util;

import com.example.cosmetest.presentation.request.LoginRequest;
import com.example.cosmetest.presentation.response.JwtResponse;

/**
 * Classe utilitaire pour construire des données de test réutilisables
 */
public class TestDataBuilder {

    // ===== AUTH DATA BUILDERS =====
    
    public static LoginRequest createLoginRequest(String login, String password) {
        LoginRequest request = new LoginRequest();
        request.setLogin(login);
        request.setMotDePasse(password);
        return request;
    }

    public static LoginRequest createValidLoginRequest() {
        return createLoginRequest("admin", "password123");
    }

    public static JwtResponse createJwtResponse(String token, String username) {
        return new JwtResponse(token, username);
    }

    public static JwtResponse createValidJwtResponse() {
        return createJwtResponse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test", "admin");
    }

    // ===== JWT TOKEN BUILDERS =====

    public static String createValidJwtToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxOTE2MjM5MDIyfQ.test";
    }

    public static String createExpiredJwtToken() {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjM5MDIyfQ.expired";
    }

    public static String createInvalidJwtToken() {
        return "invalid.jwt.token";
    }
}
