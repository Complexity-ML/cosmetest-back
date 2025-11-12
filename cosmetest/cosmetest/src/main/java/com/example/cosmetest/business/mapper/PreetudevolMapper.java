package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.domain.model.Preetudevol;
import com.example.cosmetest.domain.model.PreetudevolId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Preetudevol et PreetudevolDTO
 */
@Component
public class PreetudevolMapper {

    /**
     * Convertit une entité Preetudevol en PreetudevolDTO
     *
     * @param preetudevol l'entité à convertir
     * @return le DTO correspondant
     */
    public PreetudevolDTO toDTO(Preetudevol preetudevol) {
        if (preetudevol == null || preetudevol.getId() == null) {
            return null;
        }

        PreetudevolId id = preetudevol.getId();
        return new PreetudevolDTO(
                id.getIdEtude(),
                id.getIdGroupe(),
                id.getIdVolontaire()
        );
    }

    /**
     * Convertit un PreetudevolDTO en entité Preetudevol
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Preetudevol toEntity(PreetudevolDTO dto) {
        if (dto == null) {
            return null;
        }

        PreetudevolId id = new PreetudevolId(
                dto.getIdEtude(),
                dto.getIdGroupe(),
                dto.getIdVolontaire()
        );

        return new Preetudevol(id);
    }

    /**
     * Convertit une liste d'entités Preetudevol en liste de PreetudevolDTO
     *
     * @param preetudevolList la liste d'entités
     * @return la liste de DTOs
     */
    public List<PreetudevolDTO> toDTOList(List<Preetudevol> preetudevolList) {
        return preetudevolList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de PreetudevolDTO en liste d'entités Preetudevol
     *
     * @param dtoList la liste de DTOs
     * @return la liste d'entités
     */
    public List<Preetudevol> toEntityList(List<PreetudevolDTO> dtoList) {
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Preetudevol existante avec les données d'un PreetudevolDTO
     * Pour Preetudevol, comme l'identifiant contient toutes les données, c'est une opération de remplacement complet
     *
     * @param entity l'entité à mettre à jour
     * @param dto les données à appliquer
     * @return l'entité mise à jour
     */
    public Preetudevol updateEntityFromDTO(Preetudevol entity, PreetudevolDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        PreetudevolId newId = new PreetudevolId(
                dto.getIdEtude(),
                dto.getIdGroupe(),
                dto.getIdVolontaire()
        );

        entity.setId(newId);
        return entity;
    }
}