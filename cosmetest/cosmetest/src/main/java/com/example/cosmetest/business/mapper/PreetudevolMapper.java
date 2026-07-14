package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.domain.model.Preetudevol;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PreetudevolMapper {
    public PreetudevolDTO toDTO(Preetudevol entity) {
        if (entity == null) return null;
        return new PreetudevolDTO(entity.getIdPreetudevol(), entity.getIdEtude(), entity.getIdGroupe(), entity.getIdVolontaire());
    }

    public Preetudevol toEntity(PreetudevolDTO dto) {
        if (dto == null) return null;
        return new Preetudevol(dto.getIdEtude(), dto.getIdGroupe(), dto.getIdVolontaire());
    }

    public List<PreetudevolDTO> toDTOList(List<Preetudevol> entities) {
        return entities.stream().map(this::toDTO).toList();
    }

    public List<Preetudevol> toEntityList(List<PreetudevolDTO> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }

    public Preetudevol updateEntityFromDTO(Preetudevol entity, PreetudevolDTO dto) {
        if (entity == null || dto == null) return entity;
        entity.setIdEtude(dto.getIdEtude());
        entity.setIdGroupe(dto.getIdGroupe());
        entity.setIdVolontaire(dto.getIdVolontaire());
        return entity;
    }
}
