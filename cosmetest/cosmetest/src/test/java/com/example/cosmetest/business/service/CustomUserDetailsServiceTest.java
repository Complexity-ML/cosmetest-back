package com.example.cosmetest.business.service;

import com.example.cosmetest.config.SecurityConfig;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import com.example.cosmetest.domain.model.Identifiant;
import com.example.cosmetest.security.MD5PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {
    @Test
    void authentificationMd5ReussiePuisReecritureAutomatiqueEnBCrypt() {
        IdentifiantRepository repository = mock(IdentifiantRepository.class);
        Identifiant identifiant = new Identifiant();
        identifiant.setIdentifiant("alice");
        identifiant.setRole("UTILISATEUR");
        identifiant.setMdpIdentifiant(new MD5PasswordEncoder().encode("secret123"));
        when(repository.findByIdentifiant("alice")).thenReturn(Optional.of(identifiant));
        CustomUserDetailsService service = new CustomUserDetailsService(repository);
        SecurityConfig securityConfig = new SecurityConfig(null, null, null);
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider(service, encoder);

        provider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated("alice", "secret123"));

        assertThat(identifiant.getMdpIdentifiant()).startsWith("{bcrypt}");
        assertThat(encoder.matches("secret123", identifiant.getMdpIdentifiant())).isTrue();
        verify(repository).save(identifiant);
    }

    @Test
    void reecritLeHashLorsDeLaMiseANiveauEffectueeAuLogin() {
        IdentifiantRepository repository = mock(IdentifiantRepository.class);
        Identifiant identifiant = new Identifiant();
        identifiant.setIdentifiant("alice");
        identifiant.setMdpIdentifiant("ancien-md5");
        when(repository.findByIdentifiant("alice")).thenReturn(Optional.of(identifiant));
        CustomUserDetailsService service = new CustomUserDetailsService(repository);

        service.updatePassword(User.withUsername("alice").password("ancien-md5").roles("USER").build(),
                "{bcrypt}nouveau-hash");

        assertThat(identifiant.getMdpIdentifiant()).isEqualTo("{bcrypt}nouveau-hash");
        verify(repository).save(identifiant);
    }
}
