package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.business.mapper.PreetudevolMapper;
import com.example.cosmetest.business.service.PreetudevolService;
import com.example.cosmetest.domain.model.Preetudevol;
import com.example.cosmetest.domain.model.PreetudevolId;
import com.example.cosmetest.data.repository.PreetudevolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation des services métier pour l'entité Preetudevol
 */
@Service
@Transactional
public class PreetudevolServiceImpl implements PreetudevolService {

    private final PreetudevolRepository preetudevolRepository;
    private final PreetudevolMapper preetudevolMapper;

    public PreetudevolServiceImpl(PreetudevolRepository preetudevolRepository, PreetudevolMapper preetudevolMapper) {
        this.preetudevolRepository = preetudevolRepository;
        this.preetudevolMapper = preetudevolMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreetudevolDTO> getAllPreetudevols() {
        List<Preetudevol> preetudevols = preetudevolRepository.findAll();
        return preetudevolMapper.toDTOList(preetudevols);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PreetudevolDTO> getPreetudevolById(int idEtude, int idGroupe, int idVolontaire) {
        PreetudevolId id = new PreetudevolId(idEtude, idGroupe, idVolontaire);
        return preetudevolRepository.findById(id)
                .map(preetudevolMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByIdEtude(int idEtude) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdEtude(idEtude);
        return preetudevolMapper.toDTOList(preetudevols);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByIdGroupe(int idGroupe) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdGroupe(idGroupe);
        return preetudevolMapper.toDTOList(preetudevols);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByIdVolontaire(int idVolontaire) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdVolontaire(idVolontaire);
        return preetudevolMapper.toDTOList(preetudevols);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByEtudeAndGroupe(int idEtude, int idGroupe) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdEtudeAndIdIdGroupe(idEtude, idGroupe);
        return preetudevolMapper.toDTOList(preetudevols);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByEtudeAndVolontaire(int idEtude, int idVolontaire) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdEtudeAndIdIdVolontaire(idEtude, idVolontaire);
        return preetudevolMapper.toDTOList(preetudevols);
    }

    @Override
    public PreetudevolDTO createPreetudevol(PreetudevolDTO preetudevolDTO) {
        validatePreetudevol(preetudevolDTO);

        // Vérifier si la pré-étude-volontaire existe déjà
        PreetudevolId id = new PreetudevolId(
                preetudevolDTO.getIdEtude(),
                preetudevolDTO.getIdGroupe(),
                preetudevolDTO.getIdVolontaire()
        );

        if (preetudevolRepository.existsById(id)) {
            throw new IllegalArgumentException("Cette pré-étude-volontaire existe déjà");
        }

        Preetudevol preetudevol = preetudevolMapper.toEntity(preetudevolDTO);
        Preetudevol savedPreetudevol = preetudevolRepository.save(preetudevol);
        return preetudevolMapper.toDTO(savedPreetudevol);
    }

    @Override
    public Optional<PreetudevolDTO> updatePreetudevol(int idEtude, int idGroupe, int idVolontaire, PreetudevolDTO preetudevolDTO) {
        validatePreetudevol(preetudevolDTO);

        PreetudevolId id = new PreetudevolId(idEtude, idGroupe, idVolontaire);

        // Vérifier si l'entité à mettre à jour existe
        if (!preetudevolRepository.existsById(id)) {
            return Optional.empty();
        }

        // Vérifier si la nouvelle clé existe déjà (si elle est différente de l'ancienne)
        PreetudevolId newId = new PreetudevolId(
                preetudevolDTO.getIdEtude(),
                preetudevolDTO.getIdGroupe(),
                preetudevolDTO.getIdVolontaire()
        );

        if (!id.equals(newId) && preetudevolRepository.existsById(newId)) {
            throw new IllegalArgumentException("La nouvelle pré-étude-volontaire existe déjà");
        }

        // Pour les entités avec une clé composite qui contient toutes les données,
        // il est souvent plus simple de supprimer l'ancienne et de créer une nouvelle
        preetudevolRepository.deleteById(id);
        Preetudevol newPreetudevol = preetudevolMapper.toEntity(preetudevolDTO);
        Preetudevol savedPreetudevol = preetudevolRepository.save(newPreetudevol);
        return Optional.of(preetudevolMapper.toDTO(savedPreetudevol));
    }

    @Override
    public boolean deletePreetudevol(int idEtude, int idGroupe, int idVolontaire) {
        PreetudevolId id = new PreetudevolId(idEtude, idGroupe, idVolontaire);

        if (!preetudevolRepository.existsById(id)) {
            return false;
        }

        preetudevolRepository.deleteById(id);
        return true;
    }

    @Override
    public int deletePreetudevolsByIdEtude(int idEtude) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdEtude(idEtude);
        int count = preetudevols.size();

        if (count > 0) {
            preetudevolRepository.deleteByIdIdEtude(idEtude);
        }

        return count;
    }

    @Override
    public int deletePreetudevolsByIdGroupe(int idGroupe) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdGroupe(idGroupe);
        int count = preetudevols.size();

        if (count > 0) {
            preetudevolRepository.deleteByIdIdGroupe(idGroupe);
        }

        return count;
    }

    @Override
    public int deletePreetudevolsByIdVolontaire(int idVolontaire) {
        List<Preetudevol> preetudevols = preetudevolRepository.findByIdIdVolontaire(idVolontaire);
        int count = preetudevols.size();

        if (count > 0) {
            preetudevolRepository.deleteByIdIdVolontaire(idVolontaire);
        }

        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(int idEtude, int idGroupe, int idVolontaire) {
        PreetudevolId id = new PreetudevolId(idEtude, idGroupe, idVolontaire);
        return preetudevolRepository.existsById(id);
    }

    /**
     * Valide les données d'un PreetudevolDTO
     *
     * @param preetudevolDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validatePreetudevol(PreetudevolDTO preetudevolDTO) {
        if (preetudevolDTO == null) {
            throw new IllegalArgumentException("La pré-étude-volontaire ne peut pas être null");
        }

        if (preetudevolDTO.getIdEtude() == null || preetudevolDTO.getIdEtude() <= 0) {
            throw new IllegalArgumentException("L'ID de l'étude doit être un nombre positif");
        }

        if (preetudevolDTO.getIdGroupe() == null || preetudevolDTO.getIdGroupe() <= 0) {
            throw new IllegalArgumentException("L'ID du groupe doit être un nombre positif");
        }

        if (preetudevolDTO.getIdVolontaire() == null || preetudevolDTO.getIdVolontaire() <= 0) {
            throw new IllegalArgumentException("L'ID du volontaire doit être un nombre positif");
        }
    }
}