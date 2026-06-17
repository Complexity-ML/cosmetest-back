package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.domain.model.Annulation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnnulationMapperTest {

    private final AnnulationMapper mapper = new AnnulationMapper();

    @Test
    void toDtoPreservesRdvTrace() {
        Annulation annulation = new Annulation();
        annulation.setIdAnnuler(1);
        annulation.setIdVol(10);
        annulation.setIdEtude(5);
        annulation.setIdRdv(42);
        annulation.setDateAnnulation("2024-01-15");

        AnnulationDTO dto = mapper.toDto(annulation);

        assertThat(dto.getIdRdv()).isEqualTo(42);
    }

    @Test
    void toEntityPreservesRdvTrace() {
        AnnulationDTO dto = new AnnulationDTO();
        dto.setIdAnnuler(1);
        dto.setIdVol(10);
        dto.setIdEtude(5);
        dto.setIdRdv(42);
        dto.setDateAnnulation("2024-01-15");

        Annulation annulation = mapper.toEntity(dto);

        assertThat(annulation.getIdRdv()).isEqualTo(42);
    }
}
