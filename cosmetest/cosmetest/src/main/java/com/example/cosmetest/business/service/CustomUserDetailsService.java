package com.example.cosmetest.business.service;

import com.example.cosmetest.domain.model.Identifiant;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Service pour charger les détails utilisateur pour Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

    private final IdentifiantRepository identifiantRepository;

    public CustomUserDetailsService(IdentifiantRepository identifiantRepository) {
        this.identifiantRepository = identifiantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Identifiant identifiant = identifiantRepository.findByIdentifiant(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        String role = mapRoleToSpringRole(identifiant.getRole());

        return new User(
                identifiant.getIdentifiant(),
                identifiant.getMdpIdentifiant(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    @Override
    @Transactional
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Identifiant identifiant = identifiantRepository.findByIdentifiant(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + user.getUsername()));
        identifiant.setMdpIdentifiant(newPassword);
        identifiantRepository.save(identifiant);
        return User.withUserDetails(user).password(newPassword).build();
    }

    private String mapRoleToSpringRole(String role) {
        if (role == null) {
            return "ROLE_GUEST";
        }

        // Gérer les rôles en chiffres (ancien format) pour rétrocompatibilité
        if (role.matches("\\d+")) {
            int roleId = Integer.parseInt(role);
            switch (roleId) {
                case 2:
                    return "ROLE_ADMIN";
                case 1:
                    return "ROLE_USER";
                default:
                    return "ROLE_GUEST";
            }
        }

        // Gérer les rôles en chaîne (nouveau format)
        switch (role.toUpperCase()) {
            case "ADMIN":
            case "MODERATEUR":
                return "ROLE_ADMIN";
            case "UTILISATEUR":
                return "ROLE_USER";
            default:
                return "ROLE_GUEST";
        }
    }
}