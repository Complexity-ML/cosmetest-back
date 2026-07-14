package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.InfobancaireDTO;

import java.util.List;
import java.util.Optional;

public interface InfobancaireService {
    List<InfobancaireDTO> getAllInfobancaires();
    Optional<InfobancaireDTO> getInfobancaireById(Long id);
    Optional<InfobancaireDTO> getInfobancaireById(String bic, String iban, Integer idVol);
    List<InfobancaireDTO> getInfobancairesByIdVol(Integer idVol);
    List<InfobancaireDTO> getInfobancairesByBicAndIban(String bic, String iban);
    List<InfobancaireDTO> getInfobancairesByIban(String iban);
    List<InfobancaireDTO> getInfobancairesByBic(String bic);
    InfobancaireDTO createInfobancaire(InfobancaireDTO dto);
    Optional<InfobancaireDTO> updateInfobancaire(Long id, InfobancaireDTO dto);
    Optional<InfobancaireDTO> updateInfobancaire(String bic, String iban, Integer idVol, InfobancaireDTO dto);
    boolean deleteInfobancaire(Long id);
    boolean deleteInfobancaire(String bic, String iban, Integer idVol);
    boolean existsById(String bic, String iban, Integer idVol);
    boolean existsByIdVol(Integer idVol);
}
