package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolbugDTO;
import com.example.cosmetest.business.mapper.VolbugMapper;
import com.example.cosmetest.business.service.VolbugService;
import com.example.cosmetest.domain.model.Volbug;
import com.example.cosmetest.data.repository.VolbugRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation des services métier pour l'entité Volbug
 */
@Service
@Transactional
public class VolbugServiceImpl implements VolbugService {

    private final VolbugRepository volbugRepository;
    private final VolbugMapper volbugMapper;

    public VolbugServiceImpl(VolbugRepository volbugRepository, VolbugMapper volbugMapper) {
        this.volbugRepository = volbugRepository;
        this.volbugMapper = volbugMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VolbugDTO> getAllVolbugs() {
        List<Volbug> volbugs = volbugRepository.findAll();
        return volbugMapper.toDTOList(volbugs);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VolbugDTO> getVolbugByIdVol(Integer idVol) {
        if (idVol == null) {
            return Optional.empty();
        }
        return volbugRepository.findById(idVol)
                .map(volbugMapper::toDTO);
    }

    @Override
    public VolbugDTO createVolbug(VolbugDTO volbugDTO) {
        validateVolbug(volbugDTO);

        if (volbugRepository.existsById(volbugDTO.getIdVol())) {
            throw new IllegalArgumentException("Un bug existe déjà pour ce volontaire");
        }

        Volbug volbug = volbugMapper.toEntity(volbugDTO);
        Volbug savedVolbug = volbugRepository.save(volbug);
        return volbugMapper.toDTO(savedVolbug);
    }

    @Override
    public Optional<VolbugDTO> updateVolbug(Integer idVol, VolbugDTO volbugDTO) {
        if (idVol == null || !volbugRepository.existsById(idVol)) {
            return Optional.empty();
        }

        validateVolbug(volbugDTO);

        // Vérifier si l'ID du volontaire a changé et s'il existe déjà
        if (!idVol.equals(volbugDTO.getIdVol()) && volbugRepository.existsById(volbugDTO.getIdVol())) {
            throw new IllegalArgumentException("Un bug existe déjà pour ce nouveau volontaire");
        }

        return volbugRepository.findById(idVol)
                .map(existingVolbug -> {
                    Volbug updatedVolbug = volbugMapper.updateEntityFromDTO(existingVolbug, volbugDTO);
                    return volbugMapper.toDTO(volbugRepository.save(updatedVolbug));
                });
    }

    @Override
    public boolean deleteVolbug(Integer idVol) {
        if (idVol == null || !volbugRepository.existsById(idVol)) {
            return false;
        }

        volbugRepository.deleteById(idVol);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdVol(Integer idVol) {
        if (idVol == null) {
            return false;
        }
        return volbugRepository.existsByIdVol(idVol);
    }

    /**
     * Valide les données d'un VolbugDTO
     *
     * @param volbugDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateVolbug(VolbugDTO volbugDTO) {
        if (volbugDTO == null) {
            throw new IllegalArgumentException("Le bug ne peut pas être null");
        }

        if (volbugDTO.getIdVol() == null || volbugDTO.getIdVol() <= 0) {
            throw new IllegalArgumentException("L'ID du volontaire doit être un nombre positif");
        }
    }
}