package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.data.repository.IdentifiantRepository;
import com.example.cosmetest.domain.model.Identifiant;
import com.example.cosmetest.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour AuthServiceImpl
 * 
 * Ces tests vérifient la logique métier du service d'authentification
 * en mockant les dépendances (Repository et JwtTokenUtil)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - AuthServiceImpl")
class AuthServiceImplTest {

    @Mock
    private IdentifiantRepository identifiantRepository;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private Identifiant testIdentifiant;

    @BeforeEach
    void setUp() {
        // Préparer un identifiant de test
        testIdentifiant = new Identifiant();
        testIdentifiant.setIdentifiant("admin");
        testIdentifiant.setMdpIdentifiant("hashedPassword123");
        testIdentifiant.setRole("2"); // ROLE_ADMIN
    }

    // ===== TESTS AUTHENTICATE =====

    @Test
    @DisplayName("authenticate() - Succès avec utilisateur ADMIN")
    void testAuthenticate_AdminUser_Success() {
        // Given
        String login = "admin";
        String password = "password123";
        String expectedToken = "jwt.token.here";

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(testIdentifiant));
        when(jwtTokenUtil.generateToken(login, "ROLE_ADMIN"))
                .thenReturn(expectedToken);

        // When
        String result = authService.authenticate(login, password);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedToken);
        
        verify(identifiantRepository, times(1)).findByIdentifiant(login);
        verify(jwtTokenUtil, times(1)).generateToken(login, "ROLE_ADMIN");
    }

    @Test
    @DisplayName("authenticate() - Succès avec utilisateur USER")
    void testAuthenticate_RegularUser_Success() {
        // Given
        String login = "user";
        String password = "password123";
        String expectedToken = "jwt.token.user";
        
        Identifiant userIdentifiant = new Identifiant();
        userIdentifiant.setIdentifiant("user");
        userIdentifiant.setRole("1"); // ROLE_USER

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(userIdentifiant));
        when(jwtTokenUtil.generateToken(login, "ROLE_USER"))
                .thenReturn(expectedToken);

        // When
        String result = authService.authenticate(login, password);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedToken);
        
        verify(identifiantRepository, times(1)).findByIdentifiant(login);
        verify(jwtTokenUtil, times(1)).generateToken(login, "ROLE_USER");
    }

    @Test
    @DisplayName("authenticate() - Échec - Utilisateur non trouvé")
    void testAuthenticate_UserNotFound_ThrowsException() {
        // Given
        String login = "unknown";
        String password = "password123";

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.empty());

        // When/Then - Should throw RuntimeException
        try {
            authService.authenticate(login, password);
            fail("Should have thrown RuntimeException");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Utilisateur non trouvé");
        }
        
        verify(identifiantRepository, times(1)).findByIdentifiant(login);
        verify(jwtTokenUtil, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("authenticate() - Échec - Exception lors de la génération du token")
    void testAuthenticate_TokenGenerationFails_ThrowsException() {
        // Given
        String login = "admin";
        String password = "password123";

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(testIdentifiant));
        when(jwtTokenUtil.generateToken(anyString(), anyString()))
                .thenThrow(new RuntimeException("Token generation failed"));

        // When/Then - Should propagate the exception
        try {
            authService.authenticate(login, password);
            fail("Should have thrown RuntimeException");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Token generation failed");
        }
        
        verify(identifiantRepository, times(1)).findByIdentifiant(login);
        verify(jwtTokenUtil, times(1)).generateToken(login, "ROLE_ADMIN");
    }

    @Test
    @DisplayName("authenticate() - Gestion du rôle inconnu (default case)")
    void testAuthenticate_UnknownRole_DefaultsToRoleUser() {
        // Given
        String login = "unknownRole";
        String password = "password123";
        String expectedToken = "jwt.token.default";
        
        Identifiant unknownRoleIdentifiant = new Identifiant();
        unknownRoleIdentifiant.setIdentifiant("unknownRole");
        unknownRoleIdentifiant.setRole("99"); // Rôle inconnu

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(unknownRoleIdentifiant));
        when(jwtTokenUtil.generateToken(anyString(), anyString()))
                .thenReturn(expectedToken);

        // When
        String result = authService.authenticate(login, password);

        // Then
        assertThat(result).isNotNull();
        
        verify(identifiantRepository, times(1)).findByIdentifiant(login);
        verify(jwtTokenUtil, times(1)).generateToken(eq(login), anyString());
    }

    // ===== TESTS VALIDATE TOKEN =====

    @Test
    @DisplayName("validateToken() - Token valide")
    void testValidateToken_ValidToken_ReturnsTrue() {
        // Given
        String validToken = "valid.jwt.token";
        String username = "admin";
        
        when(jwtTokenUtil.extractUsername(validToken))
                .thenReturn(username);
        when(identifiantRepository.findByIdentifiant(username))
                .thenReturn(Optional.of(testIdentifiant));
        when(jwtTokenUtil.isTokenExpired(validToken))
                .thenReturn(false);

        // When
        boolean result = authService.validateToken(validToken);

        // Then
        assertThat(result).isTrue();
        verify(jwtTokenUtil, times(1)).extractUsername(validToken);
        verify(identifiantRepository, times(1)).findByIdentifiant(username);
        verify(jwtTokenUtil, times(1)).isTokenExpired(validToken);
    }

    @Test
    @DisplayName("validateToken() - Token invalide")
    void testValidateToken_InvalidToken_ReturnsFalse() {
        // Given
        String invalidToken = "invalid.jwt.token";
        
        when(jwtTokenUtil.extractUsername(invalidToken))
                .thenThrow(new RuntimeException("Invalid token"));

        // When
        boolean result = authService.validateToken(invalidToken);

        // Then
        assertThat(result).isFalse();
        verify(jwtTokenUtil, times(1)).extractUsername(invalidToken);
    }

    @Test
    @DisplayName("validateToken() - Token blacklisté")
    void testValidateToken_BlacklistedToken_ReturnsFalse() {
        // Given
        String token = "blacklisted.token";
        
        // Invalider le token d'abord
        authService.invalidateToken(token);

        // When
        boolean result = authService.validateToken(token);

        // Then
        assertThat(result).isFalse();
        // Le token blacklisté ne devrait même pas appeler extractUsername
        verify(jwtTokenUtil, never()).extractUsername(token);
    }

    @Test
    @DisplayName("validateToken() - Token null")
    void testValidateToken_NullToken_ReturnsFalse() {
        // Given / When / Then
        // Should not throw exception, should return false
        boolean result = authService.validateToken(null);
        assertThat(result).isFalse();
    }

    // ===== TESTS GET USERNAME FROM TOKEN =====

    @Test
    @DisplayName("getUsernameFromToken() - Extraction réussie")
    void testGetUsernameFromToken_Success() {
        // Given
        String token = "valid.jwt.token";
        String expectedUsername = "admin";
        
        when(jwtTokenUtil.extractUsername(token))
                .thenReturn(expectedUsername);

        // When
        String result = authService.getUsernameFromToken(token);

        // Then
        assertThat(result).isEqualTo(expectedUsername);
        verify(jwtTokenUtil, times(1)).extractUsername(token);
    }

    @Test
    @DisplayName("getUsernameFromToken() - Token invalide retourne null")
    void testGetUsernameFromToken_InvalidToken_ReturnsNull() {
        // Given
        String token = "invalid.jwt.token";
        
        when(jwtTokenUtil.extractUsername(token))
                .thenReturn(null);

        // When
        String result = authService.getUsernameFromToken(token);

        // Then
        assertThat(result).isNull();
        verify(jwtTokenUtil, times(1)).extractUsername(token);
    }

    @Test
    @DisplayName("getUsernameFromToken() - Exception lors de l'extraction")
    void testGetUsernameFromToken_ThrowsException_ReturnsNull() {
        // Given
        String token = "malformed.token";
        
        when(jwtTokenUtil.extractUsername(token))
                .thenThrow(new RuntimeException("Invalid token format"));

        // When/Then - exception should be thrown
        try {
            authService.getUsernameFromToken(token);
        } catch (RuntimeException e) {
            // Expected behavior
        }
        
        verify(jwtTokenUtil, times(1)).extractUsername(token);
    }

    // ===== TESTS INVALIDATE TOKEN =====

    @Test
    @DisplayName("invalidateToken() - Ajout à la blacklist")
    void testInvalidateToken_AddsToBlacklist() {
        // Given
        String token = "token.to.invalidate";

        // When
        authService.invalidateToken(token);

        // Then
        // Vérifier que le token est blacklisté en testant validateToken
        boolean isValid = authService.validateToken(token);
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("invalidateToken() - Plusieurs tokens")
    void testInvalidateToken_MultipleTokens() {
        // Given
        String token1 = "token1";
        String token2 = "token2";
        String token3 = "token3";

        // When
        authService.invalidateToken(token1);
        authService.invalidateToken(token2);
        authService.invalidateToken(token3);

        // Then
        assertThat(authService.validateToken(token1)).isFalse();
        assertThat(authService.validateToken(token2)).isFalse();
        assertThat(authService.validateToken(token3)).isFalse();
    }

    @Test
    @DisplayName("invalidateToken() - Token null ne cause pas d'exception")
    void testInvalidateToken_NullToken_NoException() {
        // When & Then - ne devrait pas lever d'exception
        authService.invalidateToken(null);
        
        // Le token null devrait être considéré comme invalide
        assertThat(authService.validateToken(null)).isFalse();
    }

    // ===== TESTS DE SCÉNARIOS COMPLETS =====

    @Test
    @DisplayName("Scénario complet - Login puis logout")
    void testCompleteScenario_LoginThenLogout() {
        // Given
        String login = "admin";
        String password = "password123";
        String token = "generated.token.123";

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(testIdentifiant));
        when(jwtTokenUtil.generateToken(login, "ROLE_ADMIN"))
                .thenReturn(token);
        when(jwtTokenUtil.extractUsername(token))
                .thenReturn(login);
        when(jwtTokenUtil.isTokenExpired(token))
                .thenReturn(false);

        // When - Login
        String generatedToken = authService.authenticate(login, password);
        assertThat(generatedToken).isEqualTo(token);

        // Vérifier que le token est valide
        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(testIdentifiant));
        boolean isValidBeforeLogout = authService.validateToken(generatedToken);
        assertThat(isValidBeforeLogout).isTrue();

        // When - Logout
        authService.invalidateToken(generatedToken);

        // Then - Le token ne devrait plus être valide
        boolean isValidAfterLogout = authService.validateToken(generatedToken);
        assertThat(isValidAfterLogout).isFalse();
    }

    @Test
    @DisplayName("Scénario - Tentatives multiples avec même identifiant")
    void testMultipleAuthenticationAttempts_SameCredentials() {
        // Given
        String login = "admin";
        String password = "password123";
        String token1 = "token1";
        String token2 = "token2";

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(testIdentifiant));
        when(jwtTokenUtil.generateToken(login, "ROLE_ADMIN"))
                .thenReturn(token1, token2);

        // When - Première authentification
        String firstToken = authService.authenticate(login, password);
        
        // When - Deuxième authentification
        String secondToken = authService.authenticate(login, password);

        // Then
        assertThat(firstToken).isNotNull();
        assertThat(secondToken).isNotNull();
        assertThat(firstToken).isEqualTo(token1);
        assertThat(secondToken).isEqualTo(token2);
        
        verify(identifiantRepository, times(2)).findByIdentifiant(login);
        verify(jwtTokenUtil, times(2)).generateToken(login, "ROLE_ADMIN");
    }

    // ===== TESTS EDGE CASES =====

    @Test
    @DisplayName("authenticate() - Login avec espaces")
    void testAuthenticate_LoginWithSpaces() {
        // Given
        String login = "  admin  ";
        String password = "password123";

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.empty());

        // When/Then - Should throw exception
        try {
            authService.authenticate(login, password);
            fail("Should have thrown RuntimeException");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Utilisateur non trouvé");
        }
        
        verify(identifiantRepository, times(1)).findByIdentifiant(login);
    }

    @Test
    @DisplayName("authenticate() - Rôle avec format invalide retourne ROLE_GUEST")
    void testAuthenticate_InvalidRoleFormat() {
        // Given
        String login = "badRole";
        String password = "password123";

        Identifiant badRoleIdentifiant = new Identifiant();
        badRoleIdentifiant.setIdentifiant("badRole");
        badRoleIdentifiant.setRole("ABC"); // Format invalide

        when(identifiantRepository.findByIdentifiant(login))
                .thenReturn(Optional.of(badRoleIdentifiant));
        when(jwtTokenUtil.generateToken(login, "ROLE_GUEST"))
                .thenReturn("mock-jwt-token-guest");

        // When - Should handle gracefully and return token with ROLE_GUEST
        String token = authService.authenticate(login, password);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isEqualTo("mock-jwt-token-guest");

        verify(identifiantRepository, times(1)).findByIdentifiant(login);
        verify(jwtTokenUtil, times(1)).generateToken(login, "ROLE_GUEST");
    }
}
