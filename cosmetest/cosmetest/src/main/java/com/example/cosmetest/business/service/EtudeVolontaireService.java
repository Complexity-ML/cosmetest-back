package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.domain.model.EtudeVolontaireId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface EtudeVolontaireService {
    List<EtudeVolontaireDTO> getAllEtudeVolontaires();
    Page<EtudeVolontaireDTO> getAllEtudeVolontairesPaginated(Pageable pageable);
    Optional<EtudeVolontaireDTO> getEtudeVolontaireById(Long id);
    Optional<EtudeVolontaireDTO> getEtudeVolontaireById(EtudeVolontaireId legacyId);
    List<EtudeVolontaireDTO> getEtudeVolontairesByEtude(int idEtude);
    List<EtudeVolontaireDTO> getEtudeVolontairesByVolontaire(int idVolontaire);
    List<EtudeVolontaireDTO> getEtudeVolontairesByGroupe(int idGroupe);
    List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndVolontaire(int idEtude, int idVolontaire);
    List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndGroupe(int idEtude, int idGroupe);
    List<EtudeVolontaireDTO> getEtudeVolontairesByStatut(String statut);
    List<EtudeVolontaireDTO> getEtudeVolontairesByPaye(int paye);
    EtudeVolontaireDTO saveEtudeVolontaire(EtudeVolontaireDTO dto);
    void deleteEtudeVolontaire(Long id);
    void deleteEtudeVolontaire(EtudeVolontaireId legacyId);
    boolean existsByEtudeAndVolontaire(int idEtude, int idVolontaire);
    Long countVolontairesByEtude(int idEtude);
    Long countEtudesByVolontaire(int idVolontaire);
    EtudeVolontaireDTO updateStatut(Long id, String statut);
    EtudeVolontaireDTO updatePaye(Long id, int paye);
    EtudeVolontaireDTO updateIV(Long id, int iv);
    EtudeVolontaireDTO updatePayeAndIV(Long id, int paye, int iv);
    int getIVById(Long id);
    EtudeVolontaireDTO updateNumSujet(Long id, int numSujet);
    EtudeVolontaireDTO updateVolontaire(Long id, Integer volontaireId);
    // Compatibilité temporaire stricte : résolution exacte des sept champs, conflit si plusieurs lignes.
    EtudeVolontaireDTO updateStatut(EtudeVolontaireId id, String statut);
    EtudeVolontaireDTO updatePaye(EtudeVolontaireId id, int paye);
    EtudeVolontaireDTO updateIV(EtudeVolontaireId id, int iv);
    EtudeVolontaireDTO updatePayeAndIV(EtudeVolontaireId id, int paye, int iv);
    int getIVById(EtudeVolontaireId id);
    EtudeVolontaireDTO updateNumSujet(EtudeVolontaireId id, int numSujet);
    EtudeVolontaireDTO updateVolontaire(EtudeVolontaireId id, Integer volontaireId);
    int deleteByEtudeAndVolontaire(int idEtude, int idVolontaire);
}
