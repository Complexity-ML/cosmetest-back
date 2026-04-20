package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Panel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'accès aux données de l'entité Panel
 */
@Repository
public interface PanelRepository extends JpaRepository<Panel, Integer> {

    /**
     * Trouve tous les panels associés à une étude
     * @param idEtude l'identifiant de l'étude
     * @return la liste des panels
     */
    List<Panel> findByIdEtude(int idEtude);

    /**
     * Trouve tous les panels associés à un groupe
     * @param idGroupe l'identifiant du groupe
     * @return la liste des panels
     */
    List<Panel> findByIdGroupe(int idGroupe);

    /**
     * Trouve tous les panels associés à une étude et un groupe
     * @param idEtude l'identifiant de l'étude
     * @param idGroupe l'identifiant du groupe
     * @return la liste des panels
     */
    List<Panel> findByIdEtudeAndIdGroupe(int idEtude, int idGroupe);

    /**
     * Trouve tous les panels pour un sexe spécifique
     * @param sexe le sexe recherché
     * @return la liste des panels
     */
    List<Panel> findBySexe(String sexe);

    /**
     * Recherche de panels par caractéristiques de peau
     * @param typePeauVisage le type de peau du visage
     * @return la liste des panels correspondants
     */
    List<Panel> findByTypePeauVisage(String typePeauVisage);

    /**
     * Recherche de panels par phototype
     * @param phototype le phototype recherché
     * @return la liste des panels correspondants
     */
    List<Panel> findByPhototype(String phototype);

    /**
     * Recherche avancée de panels par critères multiples
     * @param sexe le sexe (optionnel)
     * @param phototype le phototype (optionnel)
     * @param carnation la carnation (optionnel)
     * @return la liste des panels correspondants
     */
    @Query("SELECT p FROM Panel p WHERE (:sexe IS NULL OR p.sexe = :sexe) " +
            "AND (:phototype IS NULL OR p.phototype = :phototype) " +
            "AND (:carnation IS NULL OR p.carnation = :carnation)")
    List<Panel> findByMultipleCriteria(
            @Param("sexe") String sexe,
            @Param("phototype") String phototype,
            @Param("carnation") String carnation);

    /**
     * Recherche de panels par score minimum
     * @param scorePodMin le score minimum à rechercher
     * @return la liste des panels correspondants
     */
    List<Panel> findByScorePodMin(String scorePodMin);

    /**
     * Recherche de panels ayant des problèmes cutanés spécifiques
     * @param acne valeur pour l'acné
     * @return la liste des panels correspondants
     */
    List<Panel> findByAcne(String acne);

    /**
     * Trouve tous les panels associés à une étude et filtrés par sexe
     * @param idEtude l'identifiant de l'étude
     * @param sexe le sexe recherché
     * @return la liste des panels
     */
    List<Panel> findByIdEtudeAndSexe(int idEtude, String sexe);
}