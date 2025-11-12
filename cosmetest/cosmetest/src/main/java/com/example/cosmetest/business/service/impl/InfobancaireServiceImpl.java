package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.business.mapper.InfobancaireMapper;
import com.example.cosmetest.business.service.InfobancaireService;
import com.example.cosmetest.domain.model.Infobancaire;
import com.example.cosmetest.domain.model.InfobancaireId;
import com.example.cosmetest.data.repository.InfobancaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implémentation des services métier pour l'entité Infobancaire
 */
@Service
@Transactional
public class InfobancaireServiceImpl implements InfobancaireService {

    private final InfobancaireRepository infobancaireRepository;
    private final InfobancaireMapper infobancaireMapper;

    // Regex pour la validation du BIC
    private static final Pattern BIC_PATTERN = Pattern.compile("^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$");

    // Regex pour la validation de l'IBAN
    private static final Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$");

    public InfobancaireServiceImpl(InfobancaireRepository infobancaireRepository, InfobancaireMapper infobancaireMapper) {
        this.infobancaireRepository = infobancaireRepository;
        this.infobancaireMapper = infobancaireMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfobancaireDTO> getAllInfobancaires() {
        List<Infobancaire> infobancaires = infobancaireRepository.findAll();
        return infobancaireMapper.toDTOList(infobancaires);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InfobancaireDTO> getInfobancaireById(String bic, String iban, Integer idVol) {
        if (bic == null || iban == null || idVol == null) {
            return Optional.empty();
        }

        InfobancaireId id = new InfobancaireId(bic, iban, idVol);
        return infobancaireRepository.findById(id)
                .map(infobancaireMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByIdVol(Integer idVol) {
        if (idVol == null) {
            return List.of();
        }

        List<Infobancaire> infobancaires = infobancaireRepository.findByIdIdVol(idVol);
        return infobancaireMapper.toDTOList(infobancaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByBicAndIban(String bic, String iban) {
        if (bic == null || iban == null) {
            return List.of();
        }

        List<Infobancaire> infobancaires = infobancaireRepository.findByIdBicAndIdIban(bic, iban);
        return infobancaireMapper.toDTOList(infobancaires);
    }

    @Override
    public InfobancaireDTO createInfobancaire(InfobancaireDTO infobancaireDTO) {
        validateInfobancaire(infobancaireDTO);

        // Vérifier si l'information bancaire existe déjà
        InfobancaireId id = new InfobancaireId(
                infobancaireDTO.getBic(),
                infobancaireDTO.getIban(),
                infobancaireDTO.getIdVol()
        );

        if (infobancaireRepository.existsById(id)) {
            throw new IllegalArgumentException("Cette information bancaire existe déjà");
        }

        Infobancaire infobancaire = infobancaireMapper.toEntity(infobancaireDTO);
        Infobancaire savedInfobancaire = infobancaireRepository.save(infobancaire);
        return infobancaireMapper.toDTO(savedInfobancaire);
    }

    @Override
    public Optional<InfobancaireDTO> updateInfobancaire(String bic, String iban, Integer idVol, InfobancaireDTO infobancaireDTO) {
        if (bic == null || iban == null || idVol == null) {
            return Optional.empty();
        }

        validateInfobancaire(infobancaireDTO);

        InfobancaireId id = new InfobancaireId(bic, iban, idVol);

        // Vérifier si l'entité à mettre à jour existe
        if (!infobancaireRepository.existsById(id)) {
            return Optional.empty();
        }

        // Vérifier si la nouvelle clé existe déjà (si elle est différente de l'ancienne)
        InfobancaireId newId = new InfobancaireId(
                infobancaireDTO.getBic(),
                infobancaireDTO.getIban(),
                infobancaireDTO.getIdVol()
        );

        if (!id.equals(newId) && infobancaireRepository.existsById(newId)) {
            throw new IllegalArgumentException("La nouvelle information bancaire existe déjà");
        }

        // Pour les entités avec une clé composite qui contient toutes les données,
        // il est souvent plus simple de supprimer l'ancienne et de créer une nouvelle
        infobancaireRepository.deleteById(id);
        Infobancaire newInfobancaire = infobancaireMapper.toEntity(infobancaireDTO);
        Infobancaire savedInfobancaire = infobancaireRepository.save(newInfobancaire);
        return Optional.of(infobancaireMapper.toDTO(savedInfobancaire));
    }

    @Override
    public boolean deleteInfobancaire(String bic, String iban, Integer idVol) {
        if (bic == null || iban == null || idVol == null) {
            return false;
        }

        InfobancaireId id = new InfobancaireId(bic, iban, idVol);

        if (!infobancaireRepository.existsById(id)) {
            return false;
        }

        infobancaireRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(String bic, String iban, Integer idVol) {
        if (bic == null || iban == null || idVol == null) {
            return false;
        }

        InfobancaireId id = new InfobancaireId(bic, iban, idVol);
        return infobancaireRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdVol(Integer idVol) {
        if (idVol == null) {
            return false;
        }

        return infobancaireRepository.existsByIdIdVol(idVol);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByIban(String iban) {
        if (iban == null || iban.trim().isEmpty()) {
            return List.of();
        }

        List<Infobancaire> infobancaires = infobancaireRepository.findByIdIban(iban);
        return infobancaireMapper.toDTOList(infobancaires);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByBic(String bic) {
        if (bic == null || bic.trim().isEmpty()) {
            return List.of();
        }

        List<Infobancaire> infobancaires = infobancaireRepository.findByIdBic(bic);
        return infobancaireMapper.toDTOList(infobancaires);
    }

    /**
     * Valide les données d'un InfobancaireDTO
     *
     * @param infobancaireDTO le DTO à valider
     * @throws IllegalArgumentException si les données sont invalides
     */
    private void validateInfobancaire(InfobancaireDTO infobancaireDTO) {
        if (infobancaireDTO == null) {
            throw new IllegalArgumentException("L'information bancaire ne peut pas être null");
        }

        if (infobancaireDTO.getBic() == null || infobancaireDTO.getBic().trim().isEmpty()) {
            throw new IllegalArgumentException("Le code BIC ne peut pas être vide");
        }

        if (!BIC_PATTERN.matcher(infobancaireDTO.getBic()).matches()) {
            throw new IllegalArgumentException("Le format du code BIC est invalide");
        }

        if (infobancaireDTO.getIban() == null || infobancaireDTO.getIban().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro IBAN ne peut pas être vide");
        }

        if (!IBAN_PATTERN.matcher(infobancaireDTO.getIban()).matches()) {
            throw new IllegalArgumentException("Le format du numéro IBAN est invalide");
        }

        if (infobancaireDTO.getIdVol() == null || infobancaireDTO.getIdVol() <= 0) {
            throw new IllegalArgumentException("L'ID du volontaire doit être un nombre positif");
        }
    }
}