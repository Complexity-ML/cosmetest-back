package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Preetudevol;
import com.example.cosmetest.domain.model.PreetudevolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'accès aux données de l'entité Preetudevol
 */
@Repository
public interface PreetudevolRepository extends JpaRepository<Preetudevol, PreetudevolId> {

    /**
     * Trouve toutes les pré-études-volontaires associées à une étude spécifique
     * @param idEtude l'identifiant de l'étude
     * @return la liste des pré-études-volontaires
     */
    List<Preetudevol> findByIdIdEtude(int idEtude);

    /**
     * Trouve toutes les pré-études-volontaires associées à un groupe spécifique
     * @param idGroupe l'identifiant du groupe
     * @return la liste des pré-études-volontaires
     */
    List<Preetudevol> findByIdIdGroupe(int idGroupe);

    /**
     * Trouve toutes les pré-études-volontaires associées à un volontaire spécifique
     * @param idVolontaire l'identifiant du volontaire
     * @return la liste des pré-études-volontaires
     */
    List<Preetudevol> findByIdIdVolontaire(int idVolontaire);

    /**
     * Trouve toutes les pré-études-volontaires associées à une étude et un groupe spécifiques
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @return la liste des pré-études-volontaires
     */
    List<Preetudevol> findByIdIdEtudeAndIdIdGroupe(int idEtude, int idGroupe);

    /**
     * Trouve toutes les pré-études-volontaires associées à une étude et un volontaire spécifiques
     * @param idEtude l'identifiant de l'étude
     * @param idVolontaire l'identifiant du volontaire
     * @return la liste des pré-études-volontaires
     */
    List<Preetudevol> findByIdIdEtudeAndIdIdVolontaire(int idEtude, int idVolontaire);

    /**
     * Vérifie si une pré-étude-volontaire existe pour une étude spécifique
     * @param idEtude l'identifiant de l'étude
     * @return true si la pré-étude-volontaire existe, false sinon
     */
    boolean existsByIdIdEtude(int idEtude);

    /**
     * Vérifie si une pré-étude-volontaire existe pour un groupe spécifique
     * @param idGroupe l'identifiant du groupe
     * @return true si la pré-étude-volontaire existe, false sinon
     */
    boolean existsByIdIdGroupe(int idGroupe);

    /**
     * Vérifie si une pré-étude-volontaire existe pour un volontaire spécifique
     * @param idVolontaire l'identifiant du volontaire
     * @return true si la pré-étude-volontaire existe, false sinon
     */
    boolean existsByIdIdVolontaire(int idVolontaire);

    /**
     * Supprime toutes les pré-études-volontaires associées à une étude spécifique
     * @param idEtude l'identifiant de l'étude
     */
    void deleteByIdIdEtude(int idEtude);

    /**
     * Supprime toutes les pré-études-volontaires associées à un groupe spécifique
     * @param idGroupe l'identifiant du groupe
     */
    void deleteByIdIdGroupe(int idGroupe);

    /**
     * Supprime toutes les pré-études-volontaires associées à un volontaire spécifique
     * @param idVolontaire l'identifiant du volontaire
     */
    void deleteByIdIdVolontaire(int idVolontaire);
}