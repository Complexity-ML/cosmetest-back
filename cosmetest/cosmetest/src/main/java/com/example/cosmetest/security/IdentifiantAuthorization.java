package com.example.cosmetest.security;

import com.example.cosmetest.business.dto.IdentifiantDTO;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import com.example.cosmetest.domain.model.Identifiant;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

/** Centralise les décisions d'autorisation liées au compte courant. */
@Component("identifiantAuthorization")
public class IdentifiantAuthorization {
    private final IdentifiantRepository identifiantRepository;

    public IdentifiantAuthorization(IdentifiantRepository identifiantRepository) {
        this.identifiantRepository = identifiantRepository;
    }

    public boolean isCurrentUser(Integer id, Authentication authentication) {
        if (id == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return identifiantRepository.findById(id)
                .map(Identifiant::getIdentifiant)
                .filter(login -> Objects.equals(login, authentication.getName()))
                .isPresent();
    }

    public boolean canUpdateOwnAccount(Integer id, IdentifiantDTO changes, Authentication authentication) {
        if (id == null || changes == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return identifiantRepository.findById(id)
                .filter(existing -> Objects.equals(existing.getIdentifiant(), authentication.getName()))
                .map(existing -> changes.getRole() == null || Objects.equals(existing.getRole(), changes.getRole()))
                .orElse(false);
    }
}
