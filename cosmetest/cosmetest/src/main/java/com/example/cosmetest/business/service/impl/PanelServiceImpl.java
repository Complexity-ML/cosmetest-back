package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PanelDTO;
import com.example.cosmetest.business.mapper.PanelMapper;
import com.example.cosmetest.business.service.PanelService;
import com.example.cosmetest.domain.model.Panel;
import com.example.cosmetest.data.repository.PanelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation des services métier pour l'entité Panel
 */
@Service
@Transactional
public class PanelServiceImpl implements PanelService {

    private final PanelRepository panelRepository;
    private final PanelMapper panelMapper;

    public PanelServiceImpl(PanelRepository panelRepository, PanelMapper panelMapper) {
        this.panelRepository = panelRepository;
        this.panelMapper = panelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getAllPanels() {
        List<Panel> panels = panelRepository.findAll();
        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PanelDTO> getPanelById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        return panelRepository.findById(id)
                .map(panelMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getPanelsByIdEtude(int idEtude) {
        List<Panel> panels = panelRepository.findByIdEtude(idEtude);
        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getPanelsByIdGroupe(int idGroupe) {
        List<Panel> panels = panelRepository.findByIdGroupe(idGroupe);
        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getPanelsByIdEtudeAndIdGroupe(int idEtude, int idGroupe) {
        List<Panel> panels = panelRepository.findByIdEtudeAndIdGroupe(idEtude, idGroupe);
        return panelMapper.toDTOList(panels);
    }

    @Override
    public PanelDTO createPanel(PanelDTO panelDTO) {
        validatePanel(panelDTO);

        Panel panel = panelMapper.toEntity(panelDTO);
        panel.setIdPanel(null); // Assurer que l'ID est null pour la création
        Panel savedPanel = panelRepository.save(panel);
        return panelMapper.toDTO(savedPanel);
    }

    @Override
    public Optional<PanelDTO> updatePanel(Integer id, PanelDTO panelDTO) {
        if (id == null || !panelRepository.existsById(id)) {
            return Optional.empty();
        }

        validatePanel(panelDTO);

        return panelRepository.findById(id)
                .map(existingPanel -> {
                    panelDTO.setIdPanel(id); // Assurer l'ID correct
                    Panel updatedPanel = panelMapper.updateEntityFromDTO(existingPanel, panelDTO);
                    return panelMapper.toDTO(panelRepository.save(updatedPanel));
                });
    }

    @Override
    public boolean deletePanel(Integer id) {
        if (id == null || !panelRepository.existsById(id)) {
            return false;
        }

        panelRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        if (id == null) {
            return false;
        }
        return panelRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getPanelsBySexe(String sexe) {
        if (sexe == null || sexe.trim().isEmpty()) {
            return List.of();
        }

        List<Panel> panels = panelRepository.findBySexe(sexe);
        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getPanelsByTypePeauVisage(String typePeauVisage) {
        if (typePeauVisage == null || typePeauVisage.trim().isEmpty()) {
            return List.of();
        }

        List<Panel> panels = panelRepository.findByTypePeauVisage(typePeauVisage);
        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getPanelsByPhototype(String phototype) {
        if (phototype == null || phototype.trim().isEmpty()) {
            return List.of();
        }

        List<Panel> panels = panelRepository.findByPhototype(phototype);
        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> searchPanelsByMultipleCriteria(String sexe, String phototype, String carnation) {
        // Si tous les critères sont vides, retourner une liste vide
        if ((sexe == null || sexe.trim().isEmpty()) &&
                (phototype == null || phototype.trim().isEmpty()) &&
                (carnation == null || carnation.trim().isEmpty())) {
            return List.of();
        }

        List<Panel> panels = panelRepository.findByMultipleCriteria(
                sexe != null && !sexe.trim().isEmpty() ? sexe : null,
                phototype != null && !phototype.trim().isEmpty() ? phototype : null,
                carnation != null && !carnation.trim().isEmpty() ? carnation : null);

        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelDTO> getPanelsByIdEtudeAndSexe(int idEtude, String sexe) {
        if (sexe == null || sexe.trim().isEmpty()) {
            return getPanelsByIdEtude(idEtude);
        }

        List<Panel> panels = panelRepository.findByIdEtudeAndSexe(idEtude, sexe);
        return panelMapper.toDTOList(panels);
    }

    @Override
    @Transactional(readOnly = true)
    public PanelStatisticsDTO getPanelStatisticsByIdEtude(int idEtude) {
        List<Panel> panels = panelRepository.findByIdEtude(idEtude);

        PanelStatisticsDTO statistics = new PanelStatisticsDTO();
        statistics.setTotalPanels(panels.size());

        // Compter les panels par sexe
        int hommes = 0;
        int femmes = 0;
        Map<String, Integer> phototypesCount = new HashMap<>();

        for (Panel panel : panels) {
            String sexe = panel.getSexe();
            if ("H".equals(sexe) || "HOMME".equalsIgnoreCase(sexe)) {
                hommes++;
            } else if ("F".equals(sexe) || "FEMME".equalsIgnoreCase(sexe)) {
                femmes++;
            }

            // Compter les phototypes
            String phototype = panel.getPhototype();
            if (phototype != null && !phototype.trim().isEmpty()) {
                phototypesCount.put(phototype, phototypesCount.getOrDefault(phototype, 0) + 1);
            }
        }

        statistics.setPanelsHommes(hommes);
        statistics.setPanelsFemmes(femmes);

        // Formater la distribution des phototypes
        List<String> phototypesDistribution = phototypesCount.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.toList());

        statistics.setPhototypesDistribution(phototypesDistribution);

        return statistics;
    }

    /**
     * Valide les données d'un PanelDTO
     *
     * @param panelDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validatePanel(PanelDTO panelDTO) {
        if (panelDTO == null) {
            throw new IllegalArgumentException("Le panel ne peut pas être null");
        }

        if (panelDTO.getIdEtude() == null || panelDTO.getIdEtude() <= 0) {
            throw new IllegalArgumentException("L'ID d'étude doit être un nombre positif");
        }

        if (panelDTO.getIdGroupe() == null || panelDTO.getIdGroupe() <= 0) {
            throw new IllegalArgumentException("L'ID de groupe doit être un nombre positif");
        }

        // Validation du sexe (si fourni)
        if (panelDTO.getSexe() != null && !panelDTO.getSexe().trim().isEmpty()) {
            String sexe = panelDTO.getSexe().trim().toUpperCase();
            if (!sexe.equals("H") && !sexe.equals("F") &&
                    !sexe.equals("HOMME") && !sexe.equals("FEMME")) {
                throw new IllegalArgumentException("Le sexe doit être H, F, HOMME ou FEMME");
            }
        }

        // Validation du phototype (si fourni)
        if (panelDTO.getPhototype() != null && !panelDTO.getPhototype().trim().isEmpty()) {
            String phototype = panelDTO.getPhototype().trim();
            if (!phototype.matches("^[1-6]$|^I$|^II$|^III$|^IV$|^V$|^VI$")) {
                throw new IllegalArgumentException("Le phototype doit être entre 1 et 6 ou I et VI");
            }
        }
    }
}