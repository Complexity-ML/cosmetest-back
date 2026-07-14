package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Preetudevol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreetudevolRepository extends JpaRepository<Preetudevol, Long> {
    Optional<Preetudevol> findByIdEtudeAndIdGroupeAndIdVolontaire(int idEtude, int idGroupe, int idVolontaire);
    boolean existsByIdEtudeAndIdGroupeAndIdVolontaire(int idEtude, int idGroupe, int idVolontaire);
    List<Preetudevol> findByIdEtude(int idEtude);
    List<Preetudevol> findByIdGroupe(int idGroupe);
    List<Preetudevol> findByIdVolontaire(int idVolontaire);
    List<Preetudevol> findByIdEtudeAndIdGroupe(int idEtude, int idGroupe);
    List<Preetudevol> findByIdEtudeAndIdVolontaire(int idEtude, int idVolontaire);
    boolean existsByIdEtude(int idEtude);
    boolean existsByIdGroupe(int idGroupe);
    boolean existsByIdVolontaire(int idVolontaire);
    void deleteByIdEtude(int idEtude);
    void deleteByIdGroupe(int idGroupe);
    void deleteByIdVolontaire(int idVolontaire);
}
