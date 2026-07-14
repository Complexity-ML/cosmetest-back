package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.mapper.EtudeVolontaireMapper;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import com.example.cosmetest.domain.model.EtudeVolontaire;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EtudeVolontaireTechnicalIdMigrationTest {

    @Mock
    private EtudeVolontaireRepository repository;

    @Test
    void mapperExposeLIdTechniqueEtLesChampsMetier() {
        EtudeVolontaire entity = new EtudeVolontaire();
        entity.setId(42L);
        entity.setIdEtude(1);
        entity.setIdGroupe(2);
        entity.setIdVolontaire(3);
        entity.setIv(100);
        entity.setNumSujet(7);
        entity.setPaye(0);
        entity.setStatut("INSCRIT");

        EtudeVolontaireDTO dto = new EtudeVolontaireMapper().toDto(entity);

        assertThat(dto.getId()).isEqualTo(42L);
        assertThat(dto.getIdEtude()).isEqualTo(1);
        assertThat(dto.getIdVolontaire()).isEqualTo(3);
    }

    @Test
    void updatePayeParIdModifieEnPlaceSansSupprimer() {
        EtudeVolontaire entity = new EtudeVolontaire();
        entity.setId(42L);
        entity.setIdEtude(1);
        entity.setIdVolontaire(3);
        entity.setPaye(0);
        entity.setStatut("INSCRIT");
        when(repository.findById(42L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        EtudeVolontaireServiceImpl service = new EtudeVolontaireServiceImpl(repository, new EtudeVolontaireMapper());
        EtudeVolontaireDTO result = service.updatePaye(42L, 1);

        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getPaye()).isEqualTo(1);
        verify(repository, never()).deleteById(42L);
    }
}
