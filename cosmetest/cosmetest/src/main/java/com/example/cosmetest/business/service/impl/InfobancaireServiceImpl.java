package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.InfobancaireDTO;
import com.example.cosmetest.business.mapper.InfobancaireMapper;
import com.example.cosmetest.business.service.InfobancaireService;
import com.example.cosmetest.data.repository.InfobancaireRepository;
import com.example.cosmetest.domain.model.Infobancaire;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class InfobancaireServiceImpl implements InfobancaireService {
    private static final Pattern BIC_PATTERN = Pattern.compile("^[A-Z]{4}[A-Z]{2}[A-Z0-9]{1,2}([A-Z0-9]{2,4})?$");
    private static final Pattern IBAN_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z0-9]{10,30}$");

    private final InfobancaireRepository repository;
    private final InfobancaireMapper mapper;

    public InfobancaireServiceImpl(InfobancaireRepository repository, InfobancaireMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override @Transactional(readOnly = true)
    public List<InfobancaireDTO> getAllInfobancaires() { return mapper.toDTOList(repository.findAll()); }

    @Override @Transactional(readOnly = true)
    public Optional<InfobancaireDTO> getInfobancaireById(Long id) {
        return id == null ? Optional.empty() : repository.findById(id).map(mapper::toDTO);
    }

    @Override @Transactional(readOnly = true)
    public Optional<InfobancaireDTO> getInfobancaireById(String bic, String iban, Integer idVol) {
        if (bic == null || iban == null || idVol == null) return Optional.empty();
        return repository.findByBicAndIbanAndIdVol(bic, iban, idVol).map(mapper::toDTO);
    }

    @Override @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByIdVol(Integer idVol) {
        return idVol == null ? List.of() : mapper.toDTOList(repository.findByIdVol(idVol));
    }

    @Override @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByBicAndIban(String bic, String iban) {
        return bic == null || iban == null ? List.of() : mapper.toDTOList(repository.findByBicAndIban(bic, iban));
    }

    @Override @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByIban(String iban) {
        return isBlank(iban) ? List.of() : mapper.toDTOList(repository.findByIban(iban));
    }

    @Override @Transactional(readOnly = true)
    public List<InfobancaireDTO> getInfobancairesByBic(String bic) {
        return isBlank(bic) ? List.of() : mapper.toDTOList(repository.findByBic(bic));
    }

    @Override
    public InfobancaireDTO createInfobancaire(InfobancaireDTO dto) {
        validate(dto);
        if (repository.existsByBicAndIbanAndIdVol(dto.getBic(), dto.getIban(), dto.getIdVol())) {
            throw new IllegalArgumentException("Cette information bancaire existe déjà");
        }
        Infobancaire entity = mapper.toEntity(dto);
        entity.setIdInfobancaire(null);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<InfobancaireDTO> updateInfobancaire(Long id, InfobancaireDTO dto) {
        if (id == null) return Optional.empty();
        validate(dto);
        return repository.findById(id).map(entity -> updateExisting(entity, dto));
    }

    @Override
    public Optional<InfobancaireDTO> updateInfobancaire(String bic, String iban, Integer idVol, InfobancaireDTO dto) {
        if (bic == null || iban == null || idVol == null) return Optional.empty();
        validate(dto);
        return repository.findByBicAndIbanAndIdVol(bic, iban, idVol).map(entity -> updateExisting(entity, dto));
    }

    private InfobancaireDTO updateExisting(Infobancaire entity, InfobancaireDTO dto) {
        repository.findByBicAndIbanAndIdVol(dto.getBic(), dto.getIban(), dto.getIdVol())
                .filter(other -> !other.getIdInfobancaire().equals(entity.getIdInfobancaire()))
                .ifPresent(other -> { throw new IllegalArgumentException("La nouvelle information bancaire existe déjà"); });
        mapper.updateEntityFromDTO(entity, dto);
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public boolean deleteInfobancaire(Long id) {
        if (id == null || !repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    @Override
    public boolean deleteInfobancaire(String bic, String iban, Integer idVol) {
        if (bic == null || iban == null || idVol == null) return false;
        return repository.findByBicAndIbanAndIdVol(bic, iban, idVol).map(entity -> {
            repository.deleteById(entity.getIdInfobancaire());
            return true;
        }).orElse(false);
    }

    @Override @Transactional(readOnly = true)
    public boolean existsById(String bic, String iban, Integer idVol) {
        return bic != null && iban != null && idVol != null && repository.existsByBicAndIbanAndIdVol(bic, iban, idVol);
    }

    @Override @Transactional(readOnly = true)
    public boolean existsByIdVol(Integer idVol) { return idVol != null && repository.existsByIdVol(idVol); }

    private static boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }

    private void validate(InfobancaireDTO dto) {
        if (dto == null) throw new IllegalArgumentException("L'information bancaire ne peut pas être null");
        if (isBlank(dto.getBic())) throw new IllegalArgumentException("Le code BIC ne peut pas être vide");
        if (!BIC_PATTERN.matcher(dto.getBic()).matches()) throw new IllegalArgumentException("Le format du code BIC est invalide");
        if (isBlank(dto.getIban())) throw new IllegalArgumentException("Le numéro IBAN ne peut pas être vide");
        if (!IBAN_PATTERN.matcher(dto.getIban()).matches()) throw new IllegalArgumentException("Le format du numéro IBAN est invalide");
        if (dto.getIdVol() == null || dto.getIdVol() <= 0) throw new IllegalArgumentException("L'ID du volontaire doit être un nombre positif");
    }
}
