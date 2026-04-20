package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.IdentifiantDTO;
import com.example.cosmetest.business.mapper.IdentifiantMapper;
import com.example.cosmetest.business.service.IdentifiantService;
import com.example.cosmetest.domain.model.Identifiant;
import com.example.cosmetest.data.repository.IdentifiantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implémentation des services métier pour l'entité Identifiant
 */
@Service
@Transactional
public class IdentifiantServiceImpl implements IdentifiantService {

    private final IdentifiantRepository identifiantRepository;
    private final IdentifiantMapper identifiantMapper;
    private final PasswordEncoder passwordEncoder;

    // Liste des rôles valides
    private static final List<String> ROLES_VALIDES = Arrays.asList("ADMIN", "UTILISATEUR", "MODERATEUR");

    // Regex pour la validation d'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    public IdentifiantServiceImpl(IdentifiantRepository identifiantRepository,
                                  IdentifiantMapper identifiantMapper,
                                  PasswordEncoder passwordEncoder) {
        this.identifiantRepository = identifiantRepository;
        this.identifiantMapper = identifiantMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IdentifiantDTO> getAllIdentifiants() {
        List<Identifiant> identifiants = identifiantRepository.findAll();
        return identifiantMapper.toDTOListWithoutPassword(identifiants);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdentifiantDTO> getIdentifiantById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return identifiantRepository.findById(id)
                .map(identifiantMapper::toDTOWithoutPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdentifiantDTO> getIdentifiantByLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            return Optional.empty();
        }
        return identifiantRepository.findByIdentifiant(login)
                .map(identifiantMapper::toDTOWithoutPassword);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdentifiantDTO> getIdentifiantByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return identifiantRepository.findByMailIdentifiant(email)
                .map(identifiantMapper::toDTOWithoutPassword);
    }

    @Override
    public IdentifiantDTO createIdentifiant(IdentifiantDTO identifiantDTO) {
        validateIdentifiant(identifiantDTO);

        // Vérifier si le login ou l'email existe déjà
        if (loginExiste(identifiantDTO.getIdentifiant())) {
            throw new IllegalArgumentException("Ce login est déjà utilisé");
        }

        if (emailExiste(identifiantDTO.getMailIdentifiant())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        Identifiant identifiant = identifiantMapper.toEntity(identifiantDTO);
        identifiant.setIdIdentifiant(null); // Assurer que l'ID est null pour la création
        Identifiant savedIdentifiant = identifiantRepository.save(identifiant);
        return identifiantMapper.toDTOWithoutPassword(savedIdentifiant);
    }

    @Override
    public Optional<IdentifiantDTO> updateIdentifiant(Integer id, IdentifiantDTO identifiantDTO) {
        if (id == null || !identifiantRepository.existsById(id)) {
            return Optional.empty();
        }

        validateIdentifiantForUpdate(id, identifiantDTO);

        return identifiantRepository.findById(id)
                .map(existingIdentifiant -> {
                    identifiantDTO.setIdIdentifiant(id); // Assurer l'ID correct
                    Identifiant updatedIdentifiant = identifiantMapper.updateEntityFromDTO(existingIdentifiant, identifiantDTO);
                    return identifiantMapper.toDTOWithoutPassword(identifiantRepository.save(updatedIdentifiant));
                });
    }

    @Override
    public boolean deleteIdentifiant(Integer id) {
        if (id == null || !identifiantRepository.existsById(id)) {
            return false;
        }

        identifiantRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean changerMotDePasse(Integer id, String ancienMdp, String nouveauMdp) {
        if (id == null || ancienMdp == null || nouveauMdp == null) {
            return false;
        }

        // Valider le nouveau mot de passe
        if (nouveauMdp.length() < 6) {
            throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 6 caractères");
        }

        return identifiantRepository.findById(id)
                .map(identifiant -> {
                    // Vérifier l'ancien mot de passe
                    if (!passwordEncoder.matches(ancienMdp, identifiant.getMdpIdentifiant())) {
                        return false;
                    }

                    // Mettre à jour avec le nouveau mot de passe
                    identifiant.setMdpIdentifiant(passwordEncoder.encode(nouveauMdp));
                    identifiantRepository.save(identifiant);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IdentifiantDTO> getIdentifiantsByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return List.of();
        }

        List<Identifiant> identifiants = identifiantRepository.findByRole(role);
        return identifiantMapper.toDTOListWithoutPassword(identifiants);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean loginExiste(String login) {
        if (login == null || login.trim().isEmpty()) {
            return false;
        }
        return identifiantRepository.existsByIdentifiant(login);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean emailExiste(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return identifiantRepository.existsByMailIdentifiant(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IdentifiantDTO> authentifier(String login, String motDePasse) {
        if (login == null || motDePasse == null) {
            return Optional.empty();
        }

        return identifiantRepository.findByIdentifiant(login)
                .filter(identifiant -> passwordEncoder.matches(motDePasse, identifiant.getMdpIdentifiant()))
                .map(identifiantMapper::toDTOWithoutPassword);
    }

    /**
     * Valide les données d'un IdentifiantDTO pour la création
     *
     * @param identifiantDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateIdentifiant(IdentifiantDTO identifiantDTO) {
        if (identifiantDTO == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null");
        }

        if (identifiantDTO.getIdentifiant() == null || identifiantDTO.getIdentifiant().trim().isEmpty()) {
            throw new IllegalArgumentException("Le login ne peut pas être vide");
        }

        if (identifiantDTO.getIdentifiant().length() < 3 || identifiantDTO.getIdentifiant().length() > 50) {
            throw new IllegalArgumentException("Le login doit contenir entre 3 et 50 caractères");
        }

        if (identifiantDTO.getMdpIdentifiant() == null || identifiantDTO.getMdpIdentifiant().trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }

        if (identifiantDTO.getMdpIdentifiant().length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
        }

        if (identifiantDTO.getMailIdentifiant() == null || identifiantDTO.getMailIdentifiant().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }

        if (!EMAIL_PATTERN.matcher(identifiantDTO.getMailIdentifiant()).matches()) {
            throw new IllegalArgumentException("L'email doit être valide");
        }

        if (identifiantDTO.getRole() == null || identifiantDTO.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Le rôle ne peut pas être vide");
        }

        if (!ROLES_VALIDES.contains(identifiantDTO.getRole())) {
            throw new IllegalArgumentException("Le rôle doit être l'un des suivants: " + String.join(", ", ROLES_VALIDES));
        }
    }

    /**
     * Valide les données d'un IdentifiantDTO pour la mise à jour
     *
     * @param id             l'ID de l'identifiant à mettre à jour
     * @param identifiantDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateIdentifiantForUpdate(Integer id, IdentifiantDTO identifiantDTO) {
        if (identifiantDTO == null) {
            throw new IllegalArgumentException("L'identifiant ne peut pas être null");
        }

        // Vérifier si le login a changé et s'il existe déjà
        if (identifiantDTO.getIdentifiant() != null && !identifiantDTO.getIdentifiant().trim().isEmpty()) {
            Optional<Identifiant> existingByLogin = identifiantRepository.findByIdentifiant(identifiantDTO.getIdentifiant());
            if (existingByLogin.isPresent() && !existingByLogin.get().getIdIdentifiant().equals(id)) {
                throw new IllegalArgumentException("Ce login est déjà utilisé");
            }

            if (identifiantDTO.getIdentifiant().length() < 3 || identifiantDTO.getIdentifiant().length() > 50) {
                throw new IllegalArgumentException("Le login doit contenir entre 3 et 50 caractères");
            }
        }

        // Vérifier si l'email a changé et s'il existe déjà
        if (identifiantDTO.getMailIdentifiant() != null && !identifiantDTO.getMailIdentifiant().trim().isEmpty()) {
            Optional<Identifiant> existingByEmail = identifiantRepository.findByMailIdentifiant(identifiantDTO.getMailIdentifiant());
            if (existingByEmail.isPresent() && !existingByEmail.get().getIdIdentifiant().equals(id)) {
                throw new IllegalArgumentException("Cet email est déjà utilisé");
            }

            if (!EMAIL_PATTERN.matcher(identifiantDTO.getMailIdentifiant()).matches()) {
                throw new IllegalArgumentException("L'email doit être valide");
            }
        }

        // Vérifier si le mot de passe est valide
        if (identifiantDTO.getMdpIdentifiant() != null && !identifiantDTO.getMdpIdentifiant().trim().isEmpty()) {
            if (identifiantDTO.getMdpIdentifiant().length() < 6) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
            }
        }

        // Vérifier si le rôle est valide
        if (identifiantDTO.getRole() != null && !identifiantDTO.getRole().trim().isEmpty()) {
            if (!ROLES_VALIDES.contains(identifiantDTO.getRole())) {
                throw new IllegalArgumentException("Le rôle doit être l'un des suivants: " + String.join(", ", ROLES_VALIDES));
            }
        }
    }
}