package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.domain.model.Infobancaire;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InfobancaireMapper {
    public InfobancaireDTO toDTO(Infobancaire entity) {
        if (entity == null) return null;
        return new InfobancaireDTO(entity.getIdInfobancaire(), entity.getBic(), entity.getIban(), entity.getIdVol());
    }

    public Infobancaire toEntity(InfobancaireDTO dto) {
        if (dto == null) return null;
        return new Infobancaire(dto.getBic(), dto.getIban(), dto.getIdVol());
    }

    public List<InfobancaireDTO> toDTOList(List<Infobancaire> entities) {
        return entities.stream().map(this::toDTO).toList();
    }

    public List<Infobancaire> toEntityList(List<InfobancaireDTO> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }

    public Infobancaire updateEntityFromDTO(Infobancaire entity, InfobancaireDTO dto) {
        if (entity == null || dto == null) return entity;
        entity.setBic(dto.getBic());
        entity.setIban(dto.getIban());
        entity.setIdVol(dto.getIdVol());
        return entity;
    }
}
