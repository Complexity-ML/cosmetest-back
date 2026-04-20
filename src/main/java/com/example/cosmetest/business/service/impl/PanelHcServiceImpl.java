package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.PanelHcDTO;
import com.example.cosmetest.business.mapper.PanelHcMapper;
import com.example.cosmetest.business.service.PanelHcService;
import com.example.cosmetest.domain.model.PanelHc;
import com.example.cosmetest.data.repository.PanelHcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation des services métier pour l'entité PanelHc
 */
@Service
@Transactional
public class PanelHcServiceImpl implements PanelHcService {

    private final PanelHcRepository panelHcRepository;
    private final PanelHcMapper panelHcMapper;

    public PanelHcServiceImpl(PanelHcRepository panelHcRepository, PanelHcMapper panelHcMapper) {
        this.panelHcRepository = panelHcRepository;
        this.panelHcMapper = panelHcMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getAllPanelsHc() {
        List<PanelHc> panelsHc = panelHcRepository.findAll();
        return panelHcMapper.toDTOList(panelsHc);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PanelHcDTO> getPanelHcById(int id) {
        return panelHcRepository.findById(id)
                .map(panelHcMapper::toDTO);
    }

    @Override
    public PanelHcDTO createPanelHc(PanelHcDTO panelHcDTO) {
        if (panelHcDTO == null) {
            throw new IllegalArgumentException("Le panel HC ne peut pas être null");
        }

        // Supprimer l'ID pour s'assurer qu'on fait une création
        panelHcDTO.setIdPanel(0);

        PanelHc panelHc = panelHcMapper.toEntity(panelHcDTO);
        PanelHc savedPanelHc = panelHcRepository.save(panelHc);
        return panelHcMapper.toDTO(savedPanelHc);
    }

    @Override
    public Optional<PanelHcDTO> updatePanelHc(int id, PanelHcDTO panelHcDTO) {
        if (!panelHcRepository.existsById(id)) {
            return Optional.empty();
        }

        if (panelHcDTO == null) {
            throw new IllegalArgumentException("Le panel HC ne peut pas être null");
        }

        return panelHcRepository.findById(id)
                .map(existingPanelHc -> {
                    // Assurer que l'ID est correct
                    panelHcDTO.setIdPanel(id);

                    PanelHc updatedPanelHc = panelHcMapper.updateEntityFromDTO(existingPanelHc, panelHcDTO);
                    return panelHcMapper.toDTO(panelHcRepository.save(updatedPanelHc));
                });
    }

    @Override
    public boolean deletePanelHc(int id) {
        if (!panelHcRepository.existsById(id)) {
            return false;
        }

        panelHcRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getPanelsByLieuAchat(String lieu) {
        if (lieu == null || lieu.trim().isEmpty()) {
            return List.of();
        }

        List<PanelHc> panelsHc = panelHcRepository.findByLieuAchat(lieu);
        return panelHcMapper.toDTOList(panelsHc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getPanelsByProduitsBio(String valeur) {
        if (valeur == null || valeur.trim().isEmpty()) {
            return List.of();
        }

        List<PanelHc> panelsHc = panelHcRepository.findByProduitsBio(valeur);
        return panelHcMapper.toDTOList(panelsHc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getPanelsByMethodeEpilation(String methode) {
        if (methode == null || methode.trim().isEmpty()) {
            return List.of();
        }

        List<PanelHc> panelsHc = panelHcRepository.findByMethodeEpilation(methode);
        return panelHcMapper.toDTOList(panelsHc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getPanelsByTypeMaquillage(String maquillage) {
        if (maquillage == null || maquillage.trim().isEmpty()) {
            return List.of();
        }

        List<PanelHc> panelsHc = panelHcRepository.findByTypeMaquillage(maquillage);
        return panelHcMapper.toDTOList(panelsHc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getPanelsBySoinVisage(String soin) {
        if (soin == null || soin.trim().isEmpty()) {
            return List.of();
        }

        List<PanelHc> panelsHc = panelHcRepository.findBySoinVisage(soin);
        return panelHcMapper.toDTOList(panelsHc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getPanelsBySoinCorps(String soin) {
        if (soin == null || soin.trim().isEmpty()) {
            return List.of();
        }

        List<PanelHc> panelsHc = panelHcRepository.findBySoinCorps(soin);
        return panelHcMapper.toDTOList(panelsHc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PanelHcDTO> getPanelsByProduitCapillaire(String produit) {
        if (produit == null || produit.trim().isEmpty()) {
            return List.of();
        }

        List<PanelHc> panelsHc = panelHcRepository.findByProduitCapillaire(produit);
        return panelHcMapper.toDTOList(panelsHc);
    }
}
