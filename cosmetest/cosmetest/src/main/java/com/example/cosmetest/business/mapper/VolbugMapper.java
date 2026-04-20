package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.VolbugDTO;
import com.example.cosmetest.domain.model.Volbug;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Volbug et VolbugDTO
 */
@Component
public class VolbugMapper {

    /**
     * Convertit une entité Volbug en VolbugDTO
     *
     * @param volbug l'entité à convertir
     * @return le DTO correspondant
     */
    public VolbugDTO toDTO(Volbug volbug) {
        if (volbug == null) {
            return null;
        }

        return new VolbugDTO(volbug.getIdVol());
    }

    /**
     * Convertit un VolbugDTO en entité Volbug
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Volbug toEntity(VolbugDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Volbug(dto.getIdVol());
    }

    /**
     * Convertit une liste d'entités Volbug en liste de VolbugDTO
     *
     * @param volbugs la liste d'entités
     * @return la liste de DTOs
     */
    public List<VolbugDTO> toDTOList(List<Volbug> volbugs) {
        return volbugs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de VolbugDTO en liste d'entités Volbug
     *
     * @param dtos la liste de DTOs
     * @return la liste d'entités
     */
    public List<Volbug> toEntityList(List<VolbugDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Volbug existante avec les données d'un VolbugDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto les données à appliquer
     * @return l'entité mise à jour
     */
    public Volbug updateEntityFromDTO(Volbug entity, VolbugDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        entity.setIdVol(dto.getIdVol());
        return entity;
    }
}