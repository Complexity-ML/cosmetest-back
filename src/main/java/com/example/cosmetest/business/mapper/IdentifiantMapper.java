package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.IdentifiantDTO;
import com.example.cosmetest.domain.model.Identifiant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Identifiant et IdentifiantDTO
 */
@Component
public class IdentifiantMapper {

    private final PasswordEncoder passwordEncoder;

    public IdentifiantMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Convertit une entité Identifiant en IdentifiantDTO
     * Note: inclut le mot de passe (à utiliser avec précaution)
     *
     * @param identifiant l'entité à convertir
     * @return le DTO correspondant
     */
    public IdentifiantDTO toDTO(Identifiant identifiant) {
        if (identifiant == null) {
            return null;
        }

        IdentifiantDTO dto = new IdentifiantDTO();
        dto.setIdIdentifiant(identifiant.getIdIdentifiant());
        dto.setIdentifiant(identifiant.getIdentifiant());
        dto.setMdpIdentifiant(identifiant.getMdpIdentifiant()); // Attention: inclut le mot de passe
        dto.setMailIdentifiant(identifiant.getMailIdentifiant());
        dto.setRole(identifiant.getRole());
        dto.setDescription(identifiant.getDescription());
        return dto;
    }

    /**
     * Convertit une entité Identifiant en IdentifiantDTO sans mot de passe
     * (pour une utilisation dans les réponses API)
     *
     * @param identifiant l'entité à convertir
     * @return le DTO correspondant sans mot de passe
     */
    public IdentifiantDTO toDTOWithoutPassword(Identifiant identifiant) {
        if (identifiant == null) {
            return null;
        }

        return IdentifiantDTO.withoutPassword(
                identifiant.getIdIdentifiant(),
                identifiant.getIdentifiant(),
                identifiant.getMailIdentifiant(),
                identifiant.getRole(),
                identifiant.getDescription());
    }

    /**
     * Convertit un IdentifiantDTO en entité Identifiant
     * Note: encode le mot de passe pour le stockage sécurisé
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Identifiant toEntity(IdentifiantDTO dto) {
        if (dto == null) {
            return null;
        }

        Identifiant identifiant = new Identifiant();
        identifiant.setIdIdentifiant(dto.getIdIdentifiant());
        identifiant.setIdentifiant(dto.getIdentifiant());

        // Si un mot de passe est fourni, l'encoder
        if (dto.getMdpIdentifiant() != null && !dto.getMdpIdentifiant().isEmpty()) {
            identifiant.setMdpIdentifiant(passwordEncoder.encode(dto.getMdpIdentifiant()));
        }

        identifiant.setMailIdentifiant(dto.getMailIdentifiant());
        identifiant.setRole(dto.getRole());
        identifiant.setDescription(dto.getDescription());

        return identifiant;
    }

    /**
     * Convertit une liste d'entités Identifiant en liste de IdentifiantDTO sans mot
     * de passe
     *
     * @param identifiants la liste d'entités
     * @return la liste de DTOs sans mot de passe
     */
    public List<IdentifiantDTO> toDTOListWithoutPassword(List<Identifiant> identifiants) {
        return identifiants.stream()
                .map(this::toDTOWithoutPassword)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Identifiant existante avec les données d'un
     * IdentifiantDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto    les données à appliquer
     * @return l'entité mise à jour
     */
    public Identifiant updateEntityFromDTO(Identifiant entity, IdentifiantDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        if (dto.getIdentifiant() != null) {
            entity.setIdentifiant(dto.getIdentifiant());
        }

        // Ne mettre à jour le mot de passe que s'il est fourni
        if (dto.getMdpIdentifiant() != null && !dto.getMdpIdentifiant().isEmpty()) {
            entity.setMdpIdentifiant(passwordEncoder.encode(dto.getMdpIdentifiant()));
        }

        if (dto.getMailIdentifiant() != null) {
            entity.setMailIdentifiant(dto.getMailIdentifiant());
        }

        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }

        entity.setDescription(dto.getDescription());

        return entity;
    }

}