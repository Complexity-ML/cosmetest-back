package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.domain.model.Rdv;
import com.example.cosmetest.domain.model.RdvId;

import org.springframework.stereotype.Component;

import java.sql.Date;

/**
 * Mapper pour convertir entre les entités Rdv et les DTOs
 * Cette classe fait partie de la couche BLL et assure la transformation
 * des objets entre la couche de données et la couche de présentation
 */
@Component
public class RdvMapper {

    /**
     * Convertit une entité Rdv en DTO
     * 
     * @param rdv Entité Rdv
     * @return DTO correspondant
     */
    public RdvDTO toDto(Rdv rdv) {
        if (rdv == null) {
            return null;
        }

        RdvDTO dto = new RdvDTO();
        dto.setIdEtude(rdv.getId().getIdEtude());
        dto.setIdRdv(rdv.getId().getIdRdv());
        dto.setIdVolontaire(rdv.getIdVolontaire());
        dto.setIdGroupe(rdv.getIdGroupe());
        dto.setDate(rdv.getDate());
        dto.setHeure(rdv.getHeure());
        dto.setEtat(rdv.getEtat());
        dto.setCommentaires(rdv.getCommentaires());

        // Utiliser la relation JPA pour accéder à l'objet Etude
        if (rdv.getEtude() != null) {
            dto.setEtudeRef(rdv.getEtude().getRef());
        }

        return dto;
    }

    /**
     * Convertit un DTO en entité Rdv
     * 
     * @param dto DTO à convertir
     * @return Entité Rdv correspondante
     */
    public Rdv toEntity(RdvDTO dto) {
        if (dto == null) {
            return null;
        }

        RdvId rdvId = new RdvId();
        rdvId.setIdEtude(dto.getIdEtude());
        rdvId.setIdRdv(dto.getIdRdv());
        // rdvId.setSequence(1); // Valeur par défaut ou dto.getSequence() si vous avez
        // ajouté ce champ au DTO

        Rdv rdv = new Rdv();
        rdv.setId(rdvId);
        rdv.setIdVolontaire(dto.getIdVolontaire());
        rdv.setIdGroupe(dto.getIdGroupe());
        rdv.setDate(Date.valueOf(dto.getDate()));
        rdv.setHeure(dto.getHeure());
        rdv.setEtat(dto.getEtat());
        rdv.setCommentaires(dto.getCommentaires());

        return rdv;
    }

    /**
     * Met à jour une entité existante avec les valeurs du DTO
     * 
     * @param dto DTO contenant les nouvelles valeurs
     * @param rdv Entité à mettre à jour
     * @return Entité mise à jour
     */
    public Rdv updateEntityFromDto(RdvDTO dto, Rdv rdv) {
        if (dto == null || rdv == null) {
            return rdv;
        }

        // Ne pas mettre à jour l'ID car c'est la clé primaire
        rdv.setIdVolontaire(dto.getIdVolontaire());
        rdv.setIdGroupe(dto.getIdGroupe());
        rdv.setDate(Date.valueOf(dto.getDate()));
        rdv.setHeure(dto.getHeure());
        rdv.setEtat(dto.getEtat());
        rdv.setCommentaires(dto.getCommentaires());

        return rdv;
    }
}