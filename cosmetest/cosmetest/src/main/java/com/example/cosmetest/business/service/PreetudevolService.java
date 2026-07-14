package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.PreetudevolDTO;

import java.util.List;
import java.util.Optional;

public interface PreetudevolService {
    List<PreetudevolDTO> getAllPreetudevols();
    Optional<PreetudevolDTO> getPreetudevolById(Long id);
    Optional<PreetudevolDTO> getPreetudevolById(int idEtude, int idGroupe, int idVolontaire);
    List<PreetudevolDTO> getPreetudevolsByIdEtude(int idEtude);
    List<PreetudevolDTO> getPreetudevolsByIdGroupe(int idGroupe);
    List<PreetudevolDTO> getPreetudevolsByIdVolontaire(int idVolontaire);
    List<PreetudevolDTO> getPreetudevolsByEtudeAndGroupe(int idEtude, int idGroupe);
    List<PreetudevolDTO> getPreetudevolsByEtudeAndVolontaire(int idEtude, int idVolontaire);
    PreetudevolDTO createPreetudevol(PreetudevolDTO dto);
    Optional<PreetudevolDTO> updatePreetudevol(Long id, PreetudevolDTO dto);
    Optional<PreetudevolDTO> updatePreetudevol(int idEtude, int idGroupe, int idVolontaire, PreetudevolDTO dto);
    boolean deletePreetudevol(Long id);
    boolean deletePreetudevol(int idEtude, int idGroupe, int idVolontaire);
    int deletePreetudevolsByIdEtude(int idEtude);
    int deletePreetudevolsByIdGroupe(int idGroupe);
    int deletePreetudevolsByIdVolontaire(int idVolontaire);
    boolean existsById(int idEtude, int idGroupe, int idVolontaire);
}
