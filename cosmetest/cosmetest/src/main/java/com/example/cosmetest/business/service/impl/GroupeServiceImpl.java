package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.GroupeDTO;
import com.example.cosmetest.business.mapper.GroupeMapper;
import com.example.cosmetest.business.service.GroupeService;
import com.example.cosmetest.domain.model.Groupe;
import com.example.cosmetest.data.repository.GroupeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation des services métier pour l'entité Groupe
 */
@Service
@Transactional
public class GroupeServiceImpl implements GroupeService {

    private final GroupeRepository groupeRepository;
    private final GroupeMapper groupeMapper;

    public GroupeServiceImpl(GroupeRepository groupeRepository, GroupeMapper groupeMapper) {
        this.groupeRepository = groupeRepository;
        this.groupeMapper = groupeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupeDTO> getAllGroupes() {
        List<Groupe> groupes = groupeRepository.findAll();
        return groupeMapper.toDTOList(groupes);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GroupeDTO> getGroupeById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return groupeRepository.findById(id)
                .map(groupeMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupeDTO> getGroupesByIdEtude(int idEtude) {
        List<Groupe> groupes = groupeRepository.findByIdEtude(idEtude);
        return groupeMapper.toDTOList(groupes);
    }

    @Override
    public GroupeDTO createGroupe(GroupeDTO groupeDTO) {
        validateGroupe(groupeDTO);

        Groupe groupe = groupeMapper.toEntity(groupeDTO);
        groupe.setIdGroupe(null); // Assurer que l'ID est null pour la création
        Groupe savedGroupe = groupeRepository.save(groupe);
        return groupeMapper.toDTO(savedGroupe);
    }

    @Override
    public Optional<GroupeDTO> updateGroupe(Integer id, GroupeDTO groupeDTO) {
        if (id == null || !groupeRepository.existsById(id)) {
            return Optional.empty();
        }

        validateGroupe(groupeDTO);

        return groupeRepository.findById(id)
                .map(existingGroupe -> {
                    groupeDTO.setIdGroupe(id); // Assurer l'ID correct
                    Groupe updatedGroupe = groupeMapper.updateEntityFromDTO(existingGroupe, groupeDTO);
                    return groupeMapper.toDTO(groupeRepository.save(updatedGroupe));
                });
    }

    @Override
    public boolean deleteGroupe(Integer id) {
        if (id == null || !groupeRepository.existsById(id)) {
            return false;
        }

        groupeRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        if (id == null) {
            return false;
        }
        return groupeRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupeDTO> getGroupesByAgeRange(Integer ageMin, Integer ageMax) {
        List<Groupe> groupes = groupeRepository.findAll();

        // Filtre par âge minimum si spécifié
        if (ageMin != null) {
            groupes = groupes.stream()
                    .filter(groupe -> groupe.getAgeMinimum() >= ageMin)
                    .collect(Collectors.toList());
        }

        // Filtre par âge maximum si spécifié
        if (ageMax != null) {
            groupes = groupes.stream()
                    .filter(groupe -> groupe.getAgeMaximum() <= ageMax)
                    .collect(Collectors.toList());
        }

        return groupeMapper.toDTOList(groupes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupeDTO> getGroupesByEthnie(String ethnie) {
        if (ethnie == null || ethnie.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Groupe> groupes = groupeRepository.findByEthnie(ethnie);
        return groupeMapper.toDTOList(groupes);
    }

    /**
     * Valide les données d'un GroupeDTO
     *
     * @param groupeDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateGroupe(GroupeDTO groupeDTO) {
        if (groupeDTO == null) {
            throw new IllegalArgumentException("Le groupe ne peut pas être null");
        }

        if (groupeDTO.getIdEtude() == null || groupeDTO.getIdEtude() <= 0) {
            throw new IllegalArgumentException("L'ID d'étude doit être un nombre positif");
        }

        if (groupeDTO.getIntitule() == null || groupeDTO.getIntitule().trim().isEmpty()) {
            throw new IllegalArgumentException("L'intitulé ne peut pas être vide");
        }

        if (groupeDTO.getAgeMinimum() < 0) {
            throw new IllegalArgumentException("L'âge minimum doit être positif ou zéro");
        }

        if (groupeDTO.getAgeMaximum() < 0) {
            throw new IllegalArgumentException("L'âge maximum doit être positif ou zéro");
        }

        if (groupeDTO.getAgeMaximum() < groupeDTO.getAgeMinimum()) {
            throw new IllegalArgumentException("L'âge maximum doit être supérieur ou égal à l'âge minimum");
        }

        if (groupeDTO.getNbSujet() < 0) {
            throw new IllegalArgumentException("Le nombre de sujets doit être positif ou zéro");
        }

        if (groupeDTO.getIv() < 0) {
            throw new IllegalArgumentException("La valeur IV doit être positive ou zéro");
        }
    }
}