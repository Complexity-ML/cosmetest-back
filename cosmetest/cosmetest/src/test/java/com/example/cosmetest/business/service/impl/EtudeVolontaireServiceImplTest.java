package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.mapper.EtudeVolontaireMapper;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import com.example.cosmetest.domain.model.EtudeVolontaire;
import com.example.cosmetest.domain.model.EtudeVolontaireId;
import com.example.cosmetest.exception.AmbiguousEtudeVolontaireException;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudeVolontaireServiceImplTest {
    private final EtudeVolontaireRepository repository = mock(EtudeVolontaireRepository.class);
    private final EtudeVolontaireServiceImpl service = new EtudeVolontaireServiceImpl(repository, new EtudeVolontaireMapper());

    @Test void metAJourEnPlaceParIdTechnique() {
        EtudeVolontaire entity = row(42L, 1, 3, 0);
        when(repository.findById(42L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        EtudeVolontaireDTO result = service.updatePaye(42L, 1);
        assertThat(result.getId()).isEqualTo(42L);
        assertThat(result.getPaye()).isEqualTo(1);
        verify(repository, never()).deleteById(anyLong());
    }

    @Test void routeHistoriqueRefuseUneResolutionAmbigue() {
        EtudeVolontaireId legacy = new EtudeVolontaireId(1, 2, 3, 100, 7, 0, "INSCRIT");
        when(repository.findByLegacyKey(1,2,3,100,7,0,"INSCRIT"))
                .thenReturn(List.of(row(41L,1,3,0), row(42L,1,3,0)));
        assertThatThrownBy(() -> service.updatePaye(legacy, 1))
                .isInstanceOf(AmbiguousEtudeVolontaireException.class);
        verify(repository, never()).save(any());
    }

    @Test void creationNeRemplaceJamaisUneLigneExistante() {
        EtudeVolontaireDTO dto = new EtudeVolontaireDTO(null,1,2,3,100,7,0,"INSCRIT");
        when(repository.findByIdEtudeAndIdVolontaire(1,3)).thenReturn(List.of(row(42L,1,3,0)));
        assertThatThrownBy(() -> service.saveEtudeVolontaire(dto))
                .isInstanceOf(AmbiguousEtudeVolontaireException.class);
        verify(repository, never()).deleteById(anyLong());
        verify(repository, never()).save(any());
    }

    @Test void compteLesVolontairesDistinctsDePlusieursEtudesEnUneRequete() {
        when(repository.countActiveDistinctVolunteersByStudyIds(List.of(1, 2)))
                .thenReturn(List.of(new Object[]{1, 12L}, new Object[]{2, 4L}));

        Map<Integer, Long> result = service.countActiveDistinctVolunteersByStudyIds(List.of(1, 2, 1));

        assertThat(result).containsExactly(entry(1, 12L), entry(2, 4L));
        verify(repository).countActiveDistinctVolunteersByStudyIds(List.of(1, 2));
    }

    private static EtudeVolontaire row(Long id,int etude,int volontaire,int paye) {
        EtudeVolontaire entity=new EtudeVolontaire(); entity.setId(id); entity.setIdEtude(etude);
        entity.setIdGroupe(2); entity.setIdVolontaire(volontaire); entity.setIv(100);
        entity.setNumSujet(7); entity.setPaye(paye); entity.setStatut("INSCRIT"); return entity;
    }
}
