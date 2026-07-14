package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.domain.model.EtudeVolontaire;
import org.springframework.stereotype.Component;

@Component
public class EtudeVolontaireMapper {
    public EtudeVolontaireDTO toDto(EtudeVolontaire entity) {
        if (entity == null) return null;
        return new EtudeVolontaireDTO(entity.getId(), value(entity.getIdEtude()), value(entity.getIdGroupe()),
                value(entity.getIdVolontaire()), value(entity.getIv()), value(entity.getNumSujet()),
                value(entity.getPaye()), entity.getStatut());
    }
    public EtudeVolontaire toEntity(EtudeVolontaireDTO dto) {
        if (dto == null) return null;
        EtudeVolontaire entity = new EtudeVolontaire();
        entity.setId(dto.getId());
        return updateEntityFromDto(dto, entity);
    }
    public EtudeVolontaire updateEntityFromDto(EtudeVolontaireDTO dto, EtudeVolontaire entity) {
        if (dto == null || entity == null) return entity;
        entity.setIdEtude(dto.getIdEtude()); entity.setIdGroupe(dto.getIdGroupe());
        entity.setIdVolontaire(dto.getIdVolontaire()); entity.setIv(dto.getIv());
        entity.setNumSujet(dto.getNumsujet()); entity.setPaye(dto.getPaye()); entity.setStatut(dto.getStatut());
        return entity;
    }
    private int value(Integer value) { return value == null ? 0 : value; }
}
