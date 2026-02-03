package com.example.cosmetest.presentation.controller;

import java.util.Map;
import java.util.HashMap;

import com.example.cosmetest.presentation.request.LoginRequest;
import com.example.cosmetest.presentation.response.JwtResponse;
import com.example.cosmetest.business.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = {"http://192.168.127.36:3000","http://192.168.127.36:5000","http://intranet:5000"}, allowCredentials = "true")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    // Déterminer si on est en production (HTTPS) ou en dev (HTTP)
    private final boolean isProduction = !"dev".equals(
            System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "dev")
    );

    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Génère la valeur du cookie JWT avec les bons paramètres selon l'environnement
     * - En production (HTTPS): Secure + SameSite=None
     * - En développement (HTTP): SameSite=Lax (sans Secure)
     */
    private String buildCookieValue(String jwt, int maxAge) {
        StringBuilder cookie = new StringBuilder();
        cookie.append("jwt=").append(jwt != null ? jwt : "");
        cookie.append("; Path=/");
        cookie.append("; Max-Age=").append(maxAge);
        cookie.append("; HttpOnly");

        if (isProduction) {
            // Production: HTTPS requis
            cookie.append("; Secure");
            cookie.append("; SameSite=None");
        } else {
            // Développement: HTTP autorisé
            cookie.append("; SameSite=Lax");
        }

        return cookie.toString();
    }

    // === ROUTES /api/auth ===

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        try {
            // 1) Authentifier via Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getLogin(),
                            loginRequest.getMotDePasse()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2) Générer le token JWT via votre service
            String jwt = authService.authenticate(
                    loginRequest.getLogin(),
                    loginRequest.getMotDePasse()
            );

            if (jwt == null) {
                // Authentification échouée
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Identifiants incorrects");
            }

            // 3) Construire manuellement l'en-tête Set-Cookie (pour navigateurs web)
            String cookieValue = buildCookieValue(jwt, 86400); // 1 jour

            // 4) Fixer l'en-tête
            response.setHeader("Set-Cookie", cookieValue);
            
            // 5) Ajouter l'en-tête Authorization (pour clients API)
            response.setHeader("Authorization", "Bearer " + jwt);

            // 6) Ajouter le token dans la réponse JSON (pour applications mobiles)
            String username = authService.getUsernameFromToken(jwt);
            JwtResponse jwtResponse = new JwtResponse(jwt, username);

            return ResponseEntity.ok(jwtResponse);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Identifiants incorrects");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne : " + e.getMessage());
        }
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        // 1) Générer un Set-Cookie pour expirer le précédent
        String cookieValue = buildCookieValue(null, 0); // Expire immédiatement

        // 2) Fixer l'en-tête
        response.setHeader("Set-Cookie", cookieValue);

        // 3) Retourner la confirmation
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @GetMapping("/api/auth/user")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non authentifié");
        }
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("login", authentication.getName());
        userMap.put("roles", authentication.getAuthorities());
        return ResponseEntity.ok(userMap);
    }

    @GetMapping("/api/auth/validate")
    public ResponseEntity<?> validateToken(Authentication auth) {
        // Vérifie s'il y a un auth non-null et isAuthenticated()
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non authentifié");
        }
        // Facultatif: on pourrait renvoyer des infos (login, rôles), ou juste un message
        return ResponseEntity.ok("Token valide");
    }

    // === ROUTE /api/users/me ===

    /**
     * Endpoint pour récupérer les informations de l'utilisateur connecté
     * Accessible via /api/users/me
     * C'est essentiellement une copie de getUserInfo mais avec une URL différente
     */
    @GetMapping("/api/users/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        return getUserInfo(authentication);
    }
}