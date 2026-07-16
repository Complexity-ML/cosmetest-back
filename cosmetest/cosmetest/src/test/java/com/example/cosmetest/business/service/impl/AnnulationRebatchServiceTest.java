package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.mapper.AnnulationMapper;
import com.example.cosmetest.data.repository.AnnulationRepository;
import com.example.cosmetest.data.repository.RdvRepository;
import com.example.cosmetest.domain.model.Annulation;
import com.example.cosmetest.domain.model.Rdv;
import com.example.cosmetest.domain.model.TypeAnnulation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AnnulationRebatchServiceTest {

    @Test
    void annulerUneVolontaireRecreeTousSesCreneauxEtSupprimeTousSesAnciensRdvs() {
        AnnulationRepository annulations = mock(AnnulationRepository.class);
        AnnulationMapper mapper = mock(AnnulationMapper.class);
        RdvRepository rdvs = mock(RdvRepository.class);
        RdvIdAllocator allocator = mock(RdvIdAllocator.class);
        AnnulationServiceImpl service = new AnnulationServiceImpl(annulations, mapper, rdvs, allocator);

        AnnulationDTO request = new AnnulationDTO();
        request.setIdVol(42);
        request.setIdEtude(10);
        request.setDateAnnulation("2026-07-15");
        request.setAnnulePar(TypeAnnulation.VOLONTAIRE);
        request.setCommentaire("Annulation de Colette");

        Rdv premier = rdv(1001L, 1, "2026-07-18", "09:45", 7, 45, "1er passage");
        Rdv deuxieme = rdv(1002L, 2, "2026-07-21", "09:45", 7, 45, "2eme passage");
        Rdv troisieme = rdv(1003L, 3, "2026-07-24", "09:45", 7, 45, "3eme passage");
        List<Rdv> anciensRdvs = List.of(premier, deuxieme, troisieme);

        Annulation entity = new Annulation();
        entity.setIdVol(42);
        entity.setIdEtude(10);
        entity.setDateAnnulation("2026-07-15");
        entity.setAnnulePar(TypeAnnulation.VOLONTAIRE);
        Annulation saved = new Annulation();
        saved.setIdAnnuler(99);
        saved.setIdVol(42);
        saved.setIdEtude(10);
        AnnulationDTO response = new AnnulationDTO();
        response.setIdAnnuler(99);

        when(rdvs.findByIdVolontaireAndIdEtude(42, 10)).thenReturn(anciensRdvs);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(annulations.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(response);
        when(allocator.nextForStudy(10)).thenReturn(101, 102, 103);
        when(rdvs.save(any(Rdv.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnnulationDTO result = service.saveAnnulation(request);

        assertThat(result.getIdAnnuler()).isEqualTo(99);
        verify(annulations).save(argThat(a -> a.getAnnulePar() == TypeAnnulation.VOLONTAIRE));
        verify(rdvs).deleteAll(anciensRdvs);

        ArgumentCaptor<Rdv> replacements = ArgumentCaptor.forClass(Rdv.class);
        verify(rdvs, times(3)).save(replacements.capture());
        assertThat(replacements.getAllValues())
                .extracting(Rdv::getIdRdv)
                .containsExactly(101, 102, 103);
        assertThat(replacements.getAllValues())
                .allSatisfy(rdv -> {
                    assertThat(rdv.getIdVolontaire()).isNull();
                    assertThat(rdv.getEtat()).isEqualTo("PLANIFIE");
                });
        assertThat(replacements.getAllValues())
                .extracting(Rdv::getDate, Rdv::getHeure, Rdv::getIdGroupe, Rdv::getDuree, Rdv::getCommentaires)
                .containsExactly(
                        org.assertj.core.groups.Tuple.tuple(Date.valueOf("2026-07-18"), "09:45", 7, 45, "1er passage"),
                        org.assertj.core.groups.Tuple.tuple(Date.valueOf("2026-07-21"), "09:45", 7, 45, "2eme passage"),
                        org.assertj.core.groups.Tuple.tuple(Date.valueOf("2026-07-24"), "09:45", 7, 45, "3eme passage")
                );
    }

    private Rdv rdv(long pk, int numero, String date, String heure, int groupe, int duree, String commentaire) {
        Rdv rdv = new Rdv();
        rdv.setId(pk);
        rdv.setIdEtude(10);
        rdv.setIdRdv(numero);
        rdv.setIdVolontaire(42);
        rdv.setIdGroupe(groupe);
        rdv.setDate(Date.valueOf(date));
        rdv.setHeure(heure);
        rdv.setDuree(duree);
        rdv.setCommentaires(commentaire);
        rdv.setEtat("PLANIFIE");
        return rdv;
    }
}
