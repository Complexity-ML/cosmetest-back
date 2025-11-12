package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.IdentifiantDTO;
import com.example.cosmetest.business.mapper.IdentifiantMapper;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import com.example.cosmetest.domain.model.Identifiant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour IdentifiantServiceImpl
 * Teste la gestion des identifiants utilisateurs avec authentification et validation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("IdentifiantServiceImpl - Tests unitaires")
class IdentifiantServiceImplTest {

    @Mock
    private IdentifiantRepository identifiantRepository;

    @Mock
    private IdentifiantMapper identifiantMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private IdentifiantServiceImpl identifiantService;

    private Identifiant identifiant;
    private IdentifiantDTO identifiantDTO;

    @BeforeEach
    void setUp() {
        identifiant = new Identifiant();
        identifiant.setIdIdentifiant(1);
        identifiant.setIdentifiant("john_doe");
        identifiant.setMdpIdentifiant("encodedPassword123");
        identifiant.setMailIdentifiant("john.doe@example.com");
        identifiant.setRole("UTILISATEUR");

        identifiantDTO = new IdentifiantDTO();
        identifiantDTO.setIdIdentifiant(1);
        identifiantDTO.setIdentifiant("john_doe");
        identifiantDTO.setMdpIdentifiant("password123");
        identifiantDTO.setMailIdentifiant("john.doe@example.com");
        identifiantDTO.setRole("UTILISATEUR");
    }

    // ==================== Tests getAllIdentifiants() ====================

    @Test
    @DisplayName("getAllIdentifiants() - Récupération de tous les identifiants")
    void testGetAllIdentifiants_Success() {
        // Arrange
        Identifiant identifiant2 = new Identifiant();
        identifiant2.setIdIdentifiant(2);
        identifiant2.setIdentifiant("jane_doe");

        List<Identifiant> identifiants = Arrays.asList(identifiant, identifiant2);
        List<IdentifiantDTO> identifiantDTOs = Arrays.asList(identifiantDTO, new IdentifiantDTO());

        when(identifiantRepository.findAll()).thenReturn(identifiants);
        when(identifiantMapper.toDTOListWithoutPassword(identifiants)).thenReturn(identifiantDTOs);

        // Act
        List<IdentifiantDTO> result = identifiantService.getAllIdentifiants();

        // Assert
        assertThat(result).hasSize(2);
        verify(identifiantRepository, times(1)).findAll();
        verify(identifiantMapper, times(1)).toDTOListWithoutPassword(identifiants);
    }

    @Test
    @DisplayName("getAllIdentifiants() - Liste vide")
    void testGetAllIdentifiants_EmptyList() {
        // Arrange
        when(identifiantRepository.findAll()).thenReturn(Collections.emptyList());
        when(identifiantMapper.toDTOListWithoutPassword(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<IdentifiantDTO> result = identifiantService.getAllIdentifiants();

        // Assert
        assertThat(result).isEmpty();
        verify(identifiantRepository, times(1)).findAll();
    }

    // ==================== Tests getIdentifiantById() ====================

    @Test
    @DisplayName("getIdentifiantById() - Identifiant trouvé")
    void testGetIdentifiantById_Found() {
        // Arrange
        when(identifiantRepository.findById(1)).thenReturn(Optional.of(identifiant));
        when(identifiantMapper.toDTOWithoutPassword(identifiant)).thenReturn(identifiantDTO);

        // Act
        Optional<IdentifiantDTO> result = identifiantService.getIdentifiantById(1);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getIdIdentifiant()).isEqualTo(1);
        verify(identifiantRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("getIdentifiantById() - Identifiant non trouvé")
    void testGetIdentifiantById_NotFound() {
        // Arrange
        when(identifiantRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<IdentifiantDTO> result = identifiantService.getIdentifiantById(999);

        // Assert
        assertThat(result).isEmpty();
        verify(identifiantRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("getIdentifiantById() - ID null retourne Optional.empty()")
    void testGetIdentifiantById_NullId() {
        // Act
        Optional<IdentifiantDTO> result = identifiantService.getIdentifiantById(null);

        // Assert
        assertThat(result).isEmpty();
        verify(identifiantRepository, never()).findById(any());
    }

    // ==================== Tests getIdentifiantByLogin() ====================

    @Test
    @DisplayName("getIdentifiantByLogin() - Login trouvé")
    void testGetIdentifiantByLogin_Found() {
        // Arrange
        when(identifiantRepository.findByIdentifiant("john_doe")).thenReturn(Optional.of(identifiant));
        when(identifiantMapper.toDTOWithoutPassword(identifiant)).thenReturn(identifiantDTO);

        // Act
        Optional<IdentifiantDTO> result = identifiantService.getIdentifiantByLogin("john_doe");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getIdentifiant()).isEqualTo("john_doe");
        verify(identifiantRepository, times(1)).findByIdentifiant("john_doe");
    }

    @Test
    @DisplayName("getIdentifiantByLogin() - Login non trouvé")
    void testGetIdentifiantByLogin_NotFound() {
        // Arrange
        when(identifiantRepository.findByIdentifiant("unknown")).thenReturn(Optional.empty());

        // Act
        Optional<IdentifiantDTO> result = identifiantService.getIdentifiantByLogin("unknown");

        // Assert
        assertThat(result).isEmpty();
        verify(identifiantRepository, times(1)).findByIdentifiant("unknown");
    }

    @Test
    @DisplayName("getIdentifiantByLogin() - Login null ou vide retourne Optional.empty()")
    void testGetIdentifiantByLogin_NullOrEmpty() {
        // Act & Assert
        assertThat(identifiantService.getIdentifiantByLogin(null)).isEmpty();
        assertThat(identifiantService.getIdentifiantByLogin("")).isEmpty();
        assertThat(identifiantService.getIdentifiantByLogin("   ")).isEmpty();
        
        verify(identifiantRepository, never()).findByIdentifiant(any());
    }

    // ==================== Tests getIdentifiantByEmail() ====================

    @Test
    @DisplayName("getIdentifiantByEmail() - Email trouvé")
    void testGetIdentifiantByEmail_Found() {
        // Arrange
        when(identifiantRepository.findByMailIdentifiant("john.doe@example.com")).thenReturn(Optional.of(identifiant));
        when(identifiantMapper.toDTOWithoutPassword(identifiant)).thenReturn(identifiantDTO);

        // Act
        Optional<IdentifiantDTO> result = identifiantService.getIdentifiantByEmail("john.doe@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getMailIdentifiant()).isEqualTo("john.doe@example.com");
        verify(identifiantRepository, times(1)).findByMailIdentifiant("john.doe@example.com");
    }

    @Test
    @DisplayName("getIdentifiantByEmail() - Email null ou vide retourne Optional.empty()")
    void testGetIdentifiantByEmail_NullOrEmpty() {
        // Act & Assert
        assertThat(identifiantService.getIdentifiantByEmail(null)).isEmpty();
        assertThat(identifiantService.getIdentifiantByEmail("")).isEmpty();
        
        verify(identifiantRepository, never()).findByMailIdentifiant(any());
    }

    // ==================== Tests createIdentifiant() ====================

    @Test
    @DisplayName("createIdentifiant() - Création réussie")
    void testCreateIdentifiant_Success() {
        // Arrange
        when(identifiantRepository.existsByIdentifiant("john_doe")).thenReturn(false);
        when(identifiantRepository.existsByMailIdentifiant("john.doe@example.com")).thenReturn(false);
        when(identifiantMapper.toEntity(identifiantDTO)).thenReturn(identifiant);
        when(identifiantRepository.save(any(Identifiant.class))).thenReturn(identifiant);
        when(identifiantMapper.toDTOWithoutPassword(identifiant)).thenReturn(identifiantDTO);

        // Act
        IdentifiantDTO result = identifiantService.createIdentifiant(identifiantDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getIdIdentifiant()).isEqualTo(1);
        verify(identifiantRepository, times(1)).save(any(Identifiant.class));
    }

    @Test
    @DisplayName("createIdentifiant() - Login déjà utilisé")
    void testCreateIdentifiant_LoginExists() {
        // Arrange
        when(identifiantRepository.existsByIdentifiant("john_doe")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Ce login est déjà utilisé");
        
        verify(identifiantRepository, never()).save(any());
    }

    @Test
    @DisplayName("createIdentifiant() - Email déjà utilisé")
    void testCreateIdentifiant_EmailExists() {
        // Arrange
        when(identifiantRepository.existsByIdentifiant("john_doe")).thenReturn(false);
        when(identifiantRepository.existsByMailIdentifiant("john.doe@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cet email est déjà utilisé");
        
        verify(identifiantRepository, never()).save(any());
    }

    @Test
    @DisplayName("createIdentifiant() - DTO null")
    void testCreateIdentifiant_NullDTO() {
        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("L'identifiant ne peut pas être null");
    }

    @Test
    @DisplayName("createIdentifiant() - Login vide")
    void testCreateIdentifiant_EmptyLogin() {
        // Arrange
        identifiantDTO.setIdentifiant("");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Le login ne peut pas être vide");
    }

    @Test
    @DisplayName("createIdentifiant() - Login trop court")
    void testCreateIdentifiant_LoginTooShort() {
        // Arrange
        identifiantDTO.setIdentifiant("ab");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Le login doit contenir entre 3 et 50 caractères");
    }

    @Test
    @DisplayName("createIdentifiant() - Login trop long")
    void testCreateIdentifiant_LoginTooLong() {
        // Arrange
        identifiantDTO.setIdentifiant("a".repeat(51));

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Le login doit contenir entre 3 et 50 caractères");
    }

    @Test
    @DisplayName("createIdentifiant() - Mot de passe vide")
    void testCreateIdentifiant_EmptyPassword() {
        // Arrange
        identifiantDTO.setMdpIdentifiant("");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Le mot de passe ne peut pas être vide");
    }

    @Test
    @DisplayName("createIdentifiant() - Mot de passe trop court")
    void testCreateIdentifiant_PasswordTooShort() {
        // Arrange
        identifiantDTO.setMdpIdentifiant("12345");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Le mot de passe doit contenir au moins 6 caractères");
    }

    @Test
    @DisplayName("createIdentifiant() - Email vide")
    void testCreateIdentifiant_EmptyEmail() {
        // Arrange
        identifiantDTO.setMailIdentifiant("");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("L'email ne peut pas être vide");
    }

    @Test
    @DisplayName("createIdentifiant() - Email invalide")
    void testCreateIdentifiant_InvalidEmail() {
        // Arrange
        identifiantDTO.setMailIdentifiant("invalid-email");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("L'email doit être valide");
    }

    @Test
    @DisplayName("createIdentifiant() - Rôle vide")
    void testCreateIdentifiant_EmptyRole() {
        // Arrange
        identifiantDTO.setRole("");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Le rôle ne peut pas être vide");
    }

    @Test
    @DisplayName("createIdentifiant() - Rôle invalide")
    void testCreateIdentifiant_InvalidRole() {
        // Arrange
        identifiantDTO.setRole("INVALID_ROLE");

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.createIdentifiant(identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Le rôle doit être l'un des suivants");
    }

    // ==================== Tests updateIdentifiant() ====================

    @Test
    @DisplayName("updateIdentifiant() - Mise à jour réussie")
    void testUpdateIdentifiant_Success() {
        // Arrange
        when(identifiantRepository.existsById(1)).thenReturn(true);
        when(identifiantRepository.findById(1)).thenReturn(Optional.of(identifiant));
        when(identifiantRepository.findByIdentifiant("john_doe")).thenReturn(Optional.of(identifiant));
        when(identifiantRepository.findByMailIdentifiant("john.doe@example.com")).thenReturn(Optional.of(identifiant));
        when(identifiantMapper.updateEntityFromDTO(any(Identifiant.class), any(IdentifiantDTO.class))).thenReturn(identifiant);
        when(identifiantRepository.save(identifiant)).thenReturn(identifiant);
        when(identifiantMapper.toDTOWithoutPassword(identifiant)).thenReturn(identifiantDTO);

        // Act
        Optional<IdentifiantDTO> result = identifiantService.updateIdentifiant(1, identifiantDTO);

        // Assert
        assertThat(result).isPresent();
        verify(identifiantRepository, times(1)).save(identifiant);
    }

    @Test
    @DisplayName("updateIdentifiant() - ID null retourne Optional.empty()")
    void testUpdateIdentifiant_NullId() {
        // Act
        Optional<IdentifiantDTO> result = identifiantService.updateIdentifiant(null, identifiantDTO);

        // Assert
        assertThat(result).isEmpty();
        verify(identifiantRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateIdentifiant() - Identifiant non trouvé")
    void testUpdateIdentifiant_NotFound() {
        // Arrange
        when(identifiantRepository.existsById(999)).thenReturn(false);

        // Act
        Optional<IdentifiantDTO> result = identifiantService.updateIdentifiant(999, identifiantDTO);

        // Assert
        assertThat(result).isEmpty();
        verify(identifiantRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateIdentifiant() - Login déjà utilisé par un autre utilisateur")
    void testUpdateIdentifiant_LoginUsedByOther() {
        // Arrange
        Identifiant otherIdentifiant = new Identifiant();
        otherIdentifiant.setIdIdentifiant(2);
        otherIdentifiant.setIdentifiant("john_doe");

        when(identifiantRepository.existsById(1)).thenReturn(true);
        when(identifiantRepository.findByIdentifiant("john_doe")).thenReturn(Optional.of(otherIdentifiant));

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.updateIdentifiant(1, identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Ce login est déjà utilisé");
    }

    @Test
    @DisplayName("updateIdentifiant() - Email déjà utilisé par un autre utilisateur")
    void testUpdateIdentifiant_EmailUsedByOther() {
        // Arrange
        Identifiant otherIdentifiant = new Identifiant();
        otherIdentifiant.setIdIdentifiant(2);
        otherIdentifiant.setMailIdentifiant("john.doe@example.com");

        when(identifiantRepository.existsById(1)).thenReturn(true);
        when(identifiantRepository.findByIdentifiant("john_doe")).thenReturn(Optional.of(identifiant));
        when(identifiantRepository.findByMailIdentifiant("john.doe@example.com")).thenReturn(Optional.of(otherIdentifiant));

        // Act & Assert
        assertThatThrownBy(() -> identifiantService.updateIdentifiant(1, identifiantDTO))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Cet email est déjà utilisé");
    }

    // ==================== Tests deleteIdentifiant() ====================

    @Test
    @DisplayName("deleteIdentifiant() - Suppression réussie")
    void testDeleteIdentifiant_Success() {
        // Arrange
        when(identifiantRepository.existsById(1)).thenReturn(true);

        // Act
        boolean result = identifiantService.deleteIdentifiant(1);

        // Assert
        assertThat(result).isTrue();
        verify(identifiantRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deleteIdentifiant() - Identifiant non trouvé")
    void testDeleteIdentifiant_NotFound() {
        // Arrange
        when(identifiantRepository.existsById(999)).thenReturn(false);

        // Act
        boolean result = identifiantService.deleteIdentifiant(999);

        // Assert
        assertThat(result).isFalse();
        verify(identifiantRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteIdentifiant() - ID null retourne false")
    void testDeleteIdentifiant_NullId() {
        // Act
        boolean result = identifiantService.deleteIdentifiant(null);

        // Assert
        assertThat(result).isFalse();
        verify(identifiantRepository, never()).deleteById(any());
    }

    // ==================== Tests changerMotDePasse() ====================

    @Test
    @DisplayName("changerMotDePasse() - Changement réussi")
    void testChangerMotDePasse_Success() {
        // Arrange
        when(identifiantRepository.findById(1)).thenReturn(Optional.of(identifiant));
        when(passwordEncoder.matches("oldPassword", "encodedPassword123")).thenReturn(true);
        when(passwordEncoder.encode("newPassword123")).thenReturn("encodedNewPassword");

        // Act
        boolean result = identifiantService.changerMotDePasse(1, "oldPassword", "newPassword123");

        // Assert
        assertThat(result).isTrue();
        verify(identifiantRepository, times(1)).save(identifiant);
    }

    @Test
    @DisplayName("changerMotDePasse() - Ancien mot de passe incorrect")
    void testChangerMotDePasse_WrongOldPassword() {
        // Arrange
        when(identifiantRepository.findById(1)).thenReturn(Optional.of(identifiant));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword123")).thenReturn(false);

        // Act
        boolean result = identifiantService.changerMotDePasse(1, "wrongPassword", "newPassword123");

        // Assert
        assertThat(result).isFalse();
        verify(identifiantRepository, never()).save(any());
    }

    @Test
    @DisplayName("changerMotDePasse() - Nouveau mot de passe trop court")
    void testChangerMotDePasse_NewPasswordTooShort() {
        // Act & Assert
        assertThatThrownBy(() -> identifiantService.changerMotDePasse(1, "oldPassword", "12345"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Le nouveau mot de passe doit contenir au moins 6 caractères");
    }

    @Test
    @DisplayName("changerMotDePasse() - Paramètres null retourne false")
    void testChangerMotDePasse_NullParameters() {
        // Act & Assert
        assertThat(identifiantService.changerMotDePasse(null, "old", "new")).isFalse();
        assertThat(identifiantService.changerMotDePasse(1, null, "new")).isFalse();
        assertThat(identifiantService.changerMotDePasse(1, "old", null)).isFalse();
        
        verify(identifiantRepository, never()).save(any());
    }

    @Test
    @DisplayName("changerMotDePasse() - Identifiant non trouvé")
    void testChangerMotDePasse_NotFound() {
        // Arrange
        when(identifiantRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        boolean result = identifiantService.changerMotDePasse(999, "oldPassword", "newPassword123");

        // Assert
        assertThat(result).isFalse();
        verify(identifiantRepository, never()).save(any());
    }

    // ==================== Tests getIdentifiantsByRole() ====================

    @Test
    @DisplayName("getIdentifiantsByRole() - Identifiants trouvés")
    void testGetIdentifiantsByRole_Found() {
        // Arrange
        List<Identifiant> identifiants = Arrays.asList(identifiant);
        List<IdentifiantDTO> dtos = Arrays.asList(identifiantDTO);

        when(identifiantRepository.findByRole("UTILISATEUR")).thenReturn(identifiants);
        when(identifiantMapper.toDTOListWithoutPassword(identifiants)).thenReturn(dtos);

        // Act
        List<IdentifiantDTO> result = identifiantService.getIdentifiantsByRole("UTILISATEUR");

        // Assert
        assertThat(result).hasSize(1);
        verify(identifiantRepository, times(1)).findByRole("UTILISATEUR");
    }

    @Test
    @DisplayName("getIdentifiantsByRole() - Rôle null ou vide retourne liste vide")
    void testGetIdentifiantsByRole_NullOrEmpty() {
        // Act & Assert
        assertThat(identifiantService.getIdentifiantsByRole(null)).isEmpty();
        assertThat(identifiantService.getIdentifiantsByRole("")).isEmpty();
        
        verify(identifiantRepository, never()).findByRole(any());
    }

    // ==================== Tests loginExiste() ====================

    @Test
    @DisplayName("loginExiste() - Login existe")
    void testLoginExiste_True() {
        // Arrange
        when(identifiantRepository.existsByIdentifiant("john_doe")).thenReturn(true);

        // Act
        boolean result = identifiantService.loginExiste("john_doe");

        // Assert
        assertThat(result).isTrue();
        verify(identifiantRepository, times(1)).existsByIdentifiant("john_doe");
    }

    @Test
    @DisplayName("loginExiste() - Login n'existe pas")
    void testLoginExiste_False() {
        // Arrange
        when(identifiantRepository.existsByIdentifiant("unknown")).thenReturn(false);

        // Act
        boolean result = identifiantService.loginExiste("unknown");

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("loginExiste() - Login null ou vide retourne false")
    void testLoginExiste_NullOrEmpty() {
        // Act & Assert
        assertThat(identifiantService.loginExiste(null)).isFalse();
        assertThat(identifiantService.loginExiste("")).isFalse();
        
        verify(identifiantRepository, never()).existsByIdentifiant(any());
    }

    // ==================== Tests emailExiste() ====================

    @Test
    @DisplayName("emailExiste() - Email existe")
    void testEmailExiste_True() {
        // Arrange
        when(identifiantRepository.existsByMailIdentifiant("john.doe@example.com")).thenReturn(true);

        // Act
        boolean result = identifiantService.emailExiste("john.doe@example.com");

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("emailExiste() - Email n'existe pas")
    void testEmailExiste_False() {
        // Arrange
        when(identifiantRepository.existsByMailIdentifiant("unknown@example.com")).thenReturn(false);

        // Act
        boolean result = identifiantService.emailExiste("unknown@example.com");

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("emailExiste() - Email null ou vide retourne false")
    void testEmailExiste_NullOrEmpty() {
        // Act & Assert
        assertThat(identifiantService.emailExiste(null)).isFalse();
        assertThat(identifiantService.emailExiste("")).isFalse();
        
        verify(identifiantRepository, never()).existsByMailIdentifiant(any());
    }

    // ==================== Tests authentifier() ====================

    @Test
    @DisplayName("authentifier() - Authentification réussie")
    void testAuthentifier_Success() {
        // Arrange
        when(identifiantRepository.findByIdentifiant("john_doe")).thenReturn(Optional.of(identifiant));
        when(passwordEncoder.matches("password123", "encodedPassword123")).thenReturn(true);
        when(identifiantMapper.toDTOWithoutPassword(identifiant)).thenReturn(identifiantDTO);

        // Act
        Optional<IdentifiantDTO> result = identifiantService.authentifier("john_doe", "password123");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getIdentifiant()).isEqualTo("john_doe");
        verify(identifiantRepository, times(1)).findByIdentifiant("john_doe");
    }

    @Test
    @DisplayName("authentifier() - Mot de passe incorrect")
    void testAuthentifier_WrongPassword() {
        // Arrange
        when(identifiantRepository.findByIdentifiant("john_doe")).thenReturn(Optional.of(identifiant));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword123")).thenReturn(false);

        // Act
        Optional<IdentifiantDTO> result = identifiantService.authentifier("john_doe", "wrongPassword");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("authentifier() - Login non trouvé")
    void testAuthentifier_LoginNotFound() {
        // Arrange
        when(identifiantRepository.findByIdentifiant("unknown")).thenReturn(Optional.empty());

        // Act
        Optional<IdentifiantDTO> result = identifiantService.authentifier("unknown", "password123");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("authentifier() - Paramètres null retourne Optional.empty()")
    void testAuthentifier_NullParameters() {
        // Act & Assert
        assertThat(identifiantService.authentifier(null, "password")).isEmpty();
        assertThat(identifiantService.authentifier("login", null)).isEmpty();
        
        verify(identifiantRepository, never()).findByIdentifiant(any());
    }
}
