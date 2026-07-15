package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.exception.AmbiguousRepairGroupException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class EtudeVolontaireRepairServiceTest {

    @Test
    void rejectsVolunteerAppointmentsPointingToSeveralGroupsWithoutPartialRepair() {
        EtudeVolontaireService associationService = mock(EtudeVolontaireService.class);
        RdvService rdvService = mock(RdvService.class);
        GroupeService groupeService = mock(GroupeService.class);
        EtudeVolontaireRepairService service = new EtudeVolontaireRepairService(
                associationService, rdvService, groupeService);
        RdvDTO first = appointment(42, 1);
        RdvDTO second = appointment(42, 2);
        when(rdvService.getRdvsByIdEtude(10)).thenReturn(List.of(first, second));
        when(associationService.getEtudeVolontairesByEtude(10)).thenReturn(List.of());

        assertThatThrownBy(() -> service.repair(10))
                .isInstanceOf(AmbiguousRepairGroupException.class);

        verify(associationService, never()).saveEtudeVolontaire(any(EtudeVolontaireDTO.class));
    }

    private static RdvDTO appointment(int volunteerId, int groupId) {
        RdvDTO dto = new RdvDTO();
        dto.setIdVolontaire(volunteerId);
        dto.setIdGroupe(groupId);
        return dto;
    }
}
