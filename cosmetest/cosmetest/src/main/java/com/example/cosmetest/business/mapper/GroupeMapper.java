package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.GroupeDTO;
import com.example.cosmetest.domain.model.Groupe;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Groupe et GroupeDTO
 */
@Component
public class GroupeMapper {

    /**
     * Convertit une entité Groupe en GroupeDTO
     *
     * @param groupe l'entité à convertir
     * @return le DTO correspondant
     */
    public GroupeDTO toDTO(Groupe groupe) {
        if (groupe == null) {
            return null;
        }

        return new GroupeDTO(
                groupe.getIdGroupe(),
                groupe.getIdEtude(),
                groupe.getIntitule(),
                groupe.getDescription(),
                groupe.getAgeMinimum(),
                groupe.getAgeMaximum(),
                groupe.getEthnie(),
                groupe.getNbSujet(),
                groupe.getIv()
        );
    }

    /**
     * Convertit un GroupeDTO en entité Groupe
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Groupe toEntity(GroupeDTO dto) {
        if (dto == null) {
            return null;
        }

        Groupe groupe = new Groupe();
        groupe.setIdGroupe(dto.getIdGroupe());
        groupe.setIdEtude(dto.getIdEtude());
        groupe.setIntitule(dto.getIntitule());
        groupe.setDescription(dto.getDescription());
        groupe.setAgeMinimum(dto.getAgeMinimum());
        groupe.setAgeMaximum(dto.getAgeMaximum());
        groupe.setEthnie(dto.getEthnie());
        groupe.setNbSujet(dto.getNbSujet());
        groupe.setIv(dto.getIv());

        return groupe;
    }

    /**
     * Convertit une liste d'entités Groupe en liste de GroupeDTO
     *
     * @param groupes la liste d'entités
     * @return la liste de DTOs
     */
    public List<GroupeDTO> toDTOList(List<Groupe> groupes) {
        return groupes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de GroupeDTO en liste d'entités Groupe
     *
     * @param dtos la liste de DTOs
     * @return la liste d'entités
     */
    public List<Groupe> toEntityList(List<GroupeDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Groupe existante avec les données d'un GroupeDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto les données à appliquer
     * @return l'entité mise à jour
     */
    public Groupe updateEntityFromDTO(Groupe entity, GroupeDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        if (dto.getIdEtude() != null) {
            entity.setIdEtude(dto.getIdEtude());
        }

        if (dto.getIntitule() != null) {
            entity.setIntitule(dto.getIntitule());
        }

        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        entity.setAgeMinimum(dto.getAgeMinimum());
        entity.setAgeMaximum(dto.getAgeMaximum());

        if (dto.getEthnie() != null) {
            entity.setEthnie(dto.getEthnie());
        }

        entity.setNbSujet(dto.getNbSujet());
        entity.setIv(dto.getIv());

        return entity;
    }
}