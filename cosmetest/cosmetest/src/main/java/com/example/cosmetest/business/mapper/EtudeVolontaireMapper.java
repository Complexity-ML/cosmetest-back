package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.domain.model.Etude;
import com.example.cosmetest.domain.model.EtudeVolontaire;
import com.example.cosmetest.domain.model.EtudeVolontaireId;
import com.example.cosmetest.domain.model.Groupe;
import com.example.cosmetest.domain.model.Volontaire;

import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre les entités EtudeVolontaire et les DTOs
 * Cette classe fait partie de la couche BLL et assure la transformation
 * des objets entre la couche de données et la couche de présentation
 */
@Component
public class EtudeVolontaireMapper {

    /**
     * Convertit une entité EtudeVolontaire en DTO
     * @param etudeVolontaire Entité EtudeVolontaire
     * @return DTO correspondant
     */
    public EtudeVolontaireDTO toDto(EtudeVolontaire etudeVolontaire) {
        if (etudeVolontaire == null || etudeVolontaire.getId() == null) {
            return null;
        }

        EtudeVolontaireId id = etudeVolontaire.getId();
        EtudeVolontaireDTO dto = new EtudeVolontaireDTO();
        dto.setIdEtude(id.getIdEtude());
        dto.setIdGroupe(id.getIdGroupe());
        dto.setIdVolontaire(id.getIdVolontaire());
        dto.setIv(id.getIv());
        dto.setNumsujet(id.getNumsujet());
        dto.setPaye(id.getPaye());
        dto.setStatut(id.getStatut());

        return dto;
    }

    /**
     * Convertit un DTO en entité EtudeVolontaire
     * @param dto DTO à convertir
     * @return Entité EtudeVolontaire correspondante
     */
    public EtudeVolontaire toEntity(EtudeVolontaireDTO dto) {
        if (dto == null) {
            return null;
        }

        EtudeVolontaire entity = new EtudeVolontaire(new EtudeVolontaireId(
                dto.getIdEtude(),
                dto.getIdGroupe(),
                dto.getIdVolontaire(),
                dto.getIv(),
                dto.getNumsujet(),
                dto.getPaye(),
                dto.getStatut()));

        if (dto.getIdEtude() > 0) {
            Etude etudeRef = new Etude();
            etudeRef.setIdEtude(dto.getIdEtude());
            entity.setEtude(etudeRef);
        } else {
            entity.setEtude(null);
        }

        if (dto.getIdVolontaire() > 0) {
            Volontaire volontaireRef = new Volontaire();
            volontaireRef.setIdVol(dto.getIdVolontaire());
            entity.setVolontaire(volontaireRef);
        } else {
            entity.setVolontaire(null);
        }

        if (dto.getIdGroupe() > 0) {
            Groupe groupeRef = new Groupe();
            groupeRef.setIdGroupe(dto.getIdGroupe());
            entity.setGroupe(groupeRef);
        } else {
            entity.setGroupe(null);
        }

        return entity;
    }

    /**
     * Met à jour l'identifiant d'une entité existante avec les valeurs du DTO
     * @param dto DTO contenant les nouvelles valeurs
     * @param etudeVolontaire Entité à mettre à jour
     * @return Entité mise à jour
     */
    public EtudeVolontaire updateEntityFromDto(EtudeVolontaireDTO dto, EtudeVolontaire etudeVolontaire) {
        if (dto == null || etudeVolontaire == null) {
            return etudeVolontaire;
        }

        EtudeVolontaireId id = etudeVolontaire.getId();
        if (id == null) {
            id = new EtudeVolontaireId();
            etudeVolontaire.setId(id);
        }

        id.setIdEtude(dto.getIdEtude());
        id.setIdGroupe(dto.getIdGroupe());
        id.setIdVolontaire(dto.getIdVolontaire());
        id.setIv(dto.getIv());
        id.setNumsujet(dto.getNumsujet());
        id.setPaye(dto.getPaye());
        id.setStatut(dto.getStatut());

        if (dto.getIdEtude() > 0) {
            Etude etudeRef = etudeVolontaire.getEtude();
            if (etudeRef == null) {
                etudeRef = new Etude();
            }
            etudeRef.setIdEtude(dto.getIdEtude());
            etudeVolontaire.setEtude(etudeRef);
        } else {
            etudeVolontaire.setEtude(null);
        }

        if (dto.getIdVolontaire() > 0) {
            Volontaire volontaireRef = etudeVolontaire.getVolontaire();
            if (volontaireRef == null) {
                volontaireRef = new Volontaire();
            }
            volontaireRef.setIdVol(dto.getIdVolontaire());
            etudeVolontaire.setVolontaire(volontaireRef);
        } else {
            etudeVolontaire.setVolontaire(null);
        }

        if (dto.getIdGroupe() > 0) {
            Groupe groupeRef = etudeVolontaire.getGroupe();
            if (groupeRef == null) {
                groupeRef = new Groupe();
            }
            groupeRef.setIdGroupe(dto.getIdGroupe());
            etudeVolontaire.setGroupe(groupeRef);
        } else {
            etudeVolontaire.setGroupe(null);
        }

        return etudeVolontaire;
    }
}
