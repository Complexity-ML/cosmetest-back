package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.domain.model.Annulation;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités Annulation et les DTOs
 * Cette classe fait partie de la couche BLL et assure la transformation
 * des objets entre la couche de données et la couche de présentation
 */
@Component
public class AnnulationMapper {

    /**
     * Convertit une entité Annulation en DTO
     * @param annulation Entité Annulation
     * @return DTO correspondant
     */
    public AnnulationDTO toDto(Annulation annulation) {
        if (annulation == null) {
            return null;
        }

        AnnulationDTO dto = new AnnulationDTO();
        dto.setIdAnnuler(annulation.getIdAnnuler());
        dto.setIdVol(annulation.getIdVol());
        dto.setIdEtude(annulation.getIdEtude());
        dto.setDateAnnulation(annulation.getDateAnnulation());
        dto.setCommentaire(annulation.getCommentaire());

        return dto;
    }

    /**
     * Convertit un DTO en entité Annulation
     * @param dto DTO à convertir
     * @return Entité Annulation correspondante
     */
    public Annulation toEntity(AnnulationDTO dto) {
        if (dto == null) {
            return null;
        }

        Annulation annulation = new Annulation();
        annulation.setIdAnnuler(dto.getIdAnnuler());
        annulation.setIdVol(dto.getIdVol());
        annulation.setIdEtude(dto.getIdEtude());
        annulation.setDateAnnulation(dto.getDateAnnulation());
        annulation.setCommentaire(dto.getCommentaire());

        return annulation;
    }

    /**
     * Met à jour une entité existante avec les valeurs du DTO
     * @param dto DTO contenant les nouvelles valeurs
     * @param annulation Entité à mettre à jour
     * @return Entité mise à jour
     */
    public Annulation updateEntityFromDto(AnnulationDTO dto, Annulation annulation) {
        if (dto == null || annulation == null) {
            return annulation;
        }

        // Ne pas mettre à jour l'ID car c'est la clé primaire générée automatiquement
        annulation.setIdVol(dto.getIdVol());
        annulation.setIdEtude(dto.getIdEtude());
        annulation.setDateAnnulation(dto.getDateAnnulation());
        annulation.setCommentaire(dto.getCommentaire());

        return annulation;
    }
}