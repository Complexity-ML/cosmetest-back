package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PreetudevolDTO;
import com.example.cosmetest.business.mapper.PreetudevolMapper;
import com.example.cosmetest.business.service.PreetudevolService;
import com.example.cosmetest.data.repository.PreetudevolRepository;
import com.example.cosmetest.domain.model.Preetudevol;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PreetudevolServiceImpl implements PreetudevolService {
    private final PreetudevolRepository repository;
    private final PreetudevolMapper mapper;

    public PreetudevolServiceImpl(PreetudevolRepository repository, PreetudevolMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override @Transactional(readOnly = true)
    public List<PreetudevolDTO> getAllPreetudevols() { return mapper.toDTOList(repository.findAll()); }

    @Override @Transactional(readOnly = true)
    public Optional<PreetudevolDTO> getPreetudevolById(Long id) {
        return id == null ? Optional.empty() : repository.findById(id).map(mapper::toDTO);
    }

    @Override @Transactional(readOnly = true)
    public Optional<PreetudevolDTO> getPreetudevolById(int idEtude, int idGroupe, int idVolontaire) {
        return repository.findByIdEtudeAndIdGroupeAndIdVolontaire(idEtude, idGroupe, idVolontaire).map(mapper::toDTO);
    }

    @Override @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByIdEtude(int idEtude) { return mapper.toDTOList(repository.findByIdEtude(idEtude)); }
    @Override @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByIdGroupe(int idGroupe) { return mapper.toDTOList(repository.findByIdGroupe(idGroupe)); }
    @Override @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByIdVolontaire(int idVolontaire) { return mapper.toDTOList(repository.findByIdVolontaire(idVolontaire)); }
    @Override @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByEtudeAndGroupe(int idEtude, int idGroupe) { return mapper.toDTOList(repository.findByIdEtudeAndIdGroupe(idEtude, idGroupe)); }
    @Override @Transactional(readOnly = true)
    public List<PreetudevolDTO> getPreetudevolsByEtudeAndVolontaire(int idEtude, int idVolontaire) { return mapper.toDTOList(repository.findByIdEtudeAndIdVolontaire(idEtude, idVolontaire)); }

    @Override
    public PreetudevolDTO createPreetudevol(PreetudevolDTO dto) {
        validate(dto);
        if (repository.existsByIdEtudeAndIdGroupeAndIdVolontaire(dto.getIdEtude(), dto.getIdGroupe(), dto.getIdVolontaire())) {
            throw new IllegalArgumentException("Cette pré-étude-volontaire existe déjà");
        }
        Preetudevol entity = mapper.toEntity(dto);
        entity.setIdPreetudevol(null);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<PreetudevolDTO> updatePreetudevol(Long id, PreetudevolDTO dto) {
        if (id == null) return Optional.empty();
        validate(dto);
        return repository.findById(id).map(entity -> updateExisting(entity, dto));
    }

    @Override
    public Optional<PreetudevolDTO> updatePreetudevol(int idEtude, int idGroupe, int idVolontaire, PreetudevolDTO dto) {
        validate(dto);
        return repository.findByIdEtudeAndIdGroupeAndIdVolontaire(idEtude, idGroupe, idVolontaire)
                .map(entity -> updateExisting(entity, dto));
    }

    private PreetudevolDTO updateExisting(Preetudevol entity, PreetudevolDTO dto) {
        repository.findByIdEtudeAndIdGroupeAndIdVolontaire(dto.getIdEtude(), dto.getIdGroupe(), dto.getIdVolontaire())
                .filter(other -> !other.getIdPreetudevol().equals(entity.getIdPreetudevol()))
                .ifPresent(other -> { throw new IllegalArgumentException("La nouvelle pré-étude-volontaire existe déjà"); });
        mapper.updateEntityFromDTO(entity, dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public boolean deletePreetudevol(Long id) {
        if (id == null || !repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean deletePreetudevol(int idEtude, int idGroupe, int idVolontaire) {
        return repository.findByIdEtudeAndIdGroupeAndIdVolontaire(idEtude, idGroupe, idVolontaire).map(entity -> {
            repository.deleteById(entity.getIdPreetudevol());
            return true;
        }).orElse(false);
    }

    @Override
    public int deletePreetudevolsByIdEtude(int idEtude) {
        int count = repository.findByIdEtude(idEtude).size();
        if (count > 0) repository.deleteByIdEtude(idEtude);
        return count;
    }
    @Override
    public int deletePreetudevolsByIdGroupe(int idGroupe) {
        int count = repository.findByIdGroupe(idGroupe).size();
        if (count > 0) repository.deleteByIdGroupe(idGroupe);
        return count;
    }
    @Override
    public int deletePreetudevolsByIdVolontaire(int idVolontaire) {
        int count = repository.findByIdVolontaire(idVolontaire).size();
        if (count > 0) repository.deleteByIdVolontaire(idVolontaire);
        return count;
    }

    @Override @Transactional(readOnly = true)
    public boolean existsById(int idEtude, int idGroupe, int idVolontaire) {
        return repository.existsByIdEtudeAndIdGroupeAndIdVolontaire(idEtude, idGroupe, idVolontaire);
    }

    private void validate(PreetudevolDTO dto) {
        if (dto == null) throw new IllegalArgumentException("La pré-étude-volontaire ne peut pas être null");
        if (dto.getIdEtude() == null || dto.getIdEtude() <= 0) throw new IllegalArgumentException("L'ID de l'étude doit être un nombre positif");
        if (dto.getIdGroupe() == null || dto.getIdGroupe() <= 0) throw new IllegalArgumentException("L'ID du groupe doit être un nombre positif");
        if (dto.getIdVolontaire() == null || dto.getIdVolontaire() <= 0) throw new IllegalArgumentException("L'ID du volontaire doit être un nombre positif");
    }
}
