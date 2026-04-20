package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.domain.model.Infobancaire;
import com.example.cosmetest.domain.model.InfobancaireId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Infobancaire et InfobancaireDTO
 */
@Component
public class InfobancaireMapper {

    /**
     * Convertit une entité Infobancaire en InfobancaireDTO
     *
     * @param infobancaire l'entité à convertir
     * @return le DTO correspondant
     */
    public InfobancaireDTO toDTO(Infobancaire infobancaire) {
        if (infobancaire == null || infobancaire.getId() == null) {
            return null;
        }

        InfobancaireId id = infobancaire.getId();
        return new InfobancaireDTO(
                id.getBic(),
                id.getIban(),
                id.getIdVol()
        );
    }

    /**
     * Convertit un InfobancaireDTO en entité Infobancaire
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Infobancaire toEntity(InfobancaireDTO dto) {
        if (dto == null) {
            return null;
        }

        InfobancaireId id = new InfobancaireId(
                dto.getBic(),
                dto.getIban(),
                dto.getIdVol()
        );

        return new Infobancaire(id);
    }

    /**
     * Convertit une liste d'entités Infobancaire en liste de InfobancaireDTO
     *
     * @param infobancaires la liste d'entités
     * @return la liste de DTOs
     */
    public List<InfobancaireDTO> toDTOList(List<Infobancaire> infobancaires) {
        return infobancaires.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de InfobancaireDTO en liste d'entités Infobancaire
     *
     * @param dtos la liste de DTOs
     * @return la liste d'entités
     */
    public List<Infobancaire> toEntityList(List<InfobancaireDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Infobancaire existante avec les données d'un InfobancaireDTO
     * Pour Infobancaire, comme l'identifiant contient toutes les données, c'est une opération de remplacement complet
     *
     * @param entity l'entité à mettre à jour
     * @param dto les données à appliquer
     * @return l'entité mise à jour
     */
    public Infobancaire updateEntityFromDTO(Infobancaire entity, InfobancaireDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        InfobancaireId newId = new InfobancaireId(
                dto.getBic(),
                dto.getIban(),
                dto.getIdVol()
        );

        entity.setId(newId);
        return entity;
    }
}