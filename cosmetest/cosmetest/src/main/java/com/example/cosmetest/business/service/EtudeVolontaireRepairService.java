package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.GroupeDTO;
import com.example.cosmetest.business.dto.RdvDTO;
import com.example.cosmetest.exception.AmbiguousRepairGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EtudeVolontaireRepairService {

    private final EtudeVolontaireService associationService;
    private final RdvService rdvService;
    private final GroupeService groupeService;

    public EtudeVolontaireRepairService(
            EtudeVolontaireService associationService,
            RdvService rdvService,
            GroupeService groupeService) {
        this.associationService = associationService;
        this.rdvService = rdvService;
        this.groupeService = groupeService;
    }

    @Transactional
    public RepairResult repair(int studyId) {
        List<RdvDTO> appointments = rdvService.getRdvsByIdEtude(studyId);
        Set<Integer> appointmentVolunteerIds = appointments.stream()
                .map(RdvDTO::getIdVolontaire)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        List<EtudeVolontaireDTO> existing = associationService.getEtudeVolontairesByEtude(studyId);
        Set<Integer> existingVolunteerIds = existing.stream()
                .map(EtudeVolontaireDTO::getIdVolontaire)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Integer> missing = new HashSet<>(appointmentVolunteerIds);
        missing.removeAll(existingVolunteerIds);

        List<GroupeDTO> groups = groupeService.getGroupesByIdEtude(studyId);
        Map<Integer, Integer> ivByGroup = new HashMap<>();
        for (GroupeDTO group : groups) {
            if (group.getIdGroupe() != null) {
                ivByGroup.put(group.getIdGroupe(), group.getIv());
            }
        }

        List<EtudeVolontaireDTO> repairs = new ArrayList<>();
        for (Integer volunteerId : missing) {
            Set<Integer> appointmentGroupIds = appointments.stream()
                    .filter(rdv -> Objects.equals(volunteerId, rdv.getIdVolontaire()))
                    .map(RdvDTO::getIdGroupe)
                    .filter(id -> id != null && id > 0)
                    .collect(Collectors.toSet());
            int groupId = resolveUniqueGroup(volunteerId, studyId, appointmentGroupIds, ivByGroup.keySet());
            repairs.add(new EtudeVolontaireDTO(
                    studyId, groupId, volunteerId, ivByGroup.getOrDefault(groupId, 0),
                    0, 0, "INSCRIT"));
        }

        repairs.forEach(associationService::saveEtudeVolontaire);
        return new RepairResult(
                studyId,
                appointmentVolunteerIds.size(),
                existingVolunteerIds.size(),
                missing.size(),
                repairs.size());
    }

    private int resolveUniqueGroup(
            Integer volunteerId,
            int studyId,
            Set<Integer> appointmentGroupIds,
            Set<Integer> studyGroupIds) {
        if (appointmentGroupIds.size() == 1) {
            return appointmentGroupIds.iterator().next();
        }
        if (appointmentGroupIds.size() > 1) {
            throw new AmbiguousRepairGroupException(volunteerId, studyId, appointmentGroupIds.size());
        }
        if (studyGroupIds.size() == 1) {
            return studyGroupIds.iterator().next();
        }
        if (studyGroupIds.size() > 1) {
            throw new AmbiguousRepairGroupException(volunteerId, studyId, studyGroupIds.size());
        }
        throw new IllegalArgumentException("Aucun groupe disponible pour l'étude " + studyId);
    }

    public record RepairResult(
            int etudeId,
            int volontairesInRdvs,
            int existingAssociations,
            int missing,
            int repaired) {
    }
}
