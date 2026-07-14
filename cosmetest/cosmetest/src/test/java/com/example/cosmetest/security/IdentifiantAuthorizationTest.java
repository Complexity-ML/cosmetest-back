package com.example.cosmetest.security;

import com.example.cosmetest.business.dto.IdentifiantDTO;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import com.example.cosmetest.domain.model.Identifiant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("IdentifiantAuthorization")
class IdentifiantAuthorizationTest {

    @Test
    @DisplayName("La mise à jour de son propre compte ne lit l'identifiant qu'une fois")
    void canUpdateOwnAccountReadsAccountOnlyOnce() {
        IdentifiantRepository repository = mock(IdentifiantRepository.class);
        Authentication authentication = mock(Authentication.class);
        Identifiant existing = new Identifiant();
        existing.setIdentifiant("alice");
        existing.setRole("UTILISATEUR");
        IdentifiantDTO changes = new IdentifiantDTO();
        changes.setRole("UTILISATEUR");

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("alice");
        when(repository.findById(7)).thenReturn(Optional.of(existing));

        IdentifiantAuthorization authorization = new IdentifiantAuthorization(repository);

        assertThat(authorization.canUpdateOwnAccount(7, changes, authentication)).isTrue();
        verify(repository, times(1)).findById(7);
    }
}
