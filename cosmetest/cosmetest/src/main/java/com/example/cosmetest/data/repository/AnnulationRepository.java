package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Annulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository pour l'accès aux données des annulations
 * Fait partie de la couche d'accès aux données (DAL)
 */
@Repository
public interface AnnulationRepository extends JpaRepository<Annulation, Integer> {

    /**
     * Trouve les annulations par identifiant de volontaire
     * @param idVol Identifiant du volontaire
     * @return Liste des annulations
     */
    List<Annulation> findByIdVol(int idVol);

    /**
     * Trouve les annulations par identifiant d'étude
     * @param idEtude Identifiant de l'étude
     * @return Liste des annulations
     */
    List<Annulation> findByIdEtude(int idEtude);

    /**
     * Trouve les annulations par identifiant de volontaire et d'étude
     * @param idVol Identifiant du volontaire
     * @param idEtude Identifiant de l'étude
     * @return Liste des annulations
     */
    List<Annulation> findByIdVolAndIdEtude(int idVol, int idEtude);

    /**
     * Trouve les annulations par date d'annulation
     * @param dateAnnulation Date d'annulation
     * @return Liste des annulations
     */
    List<Annulation> findByDateAnnulation(String dateAnnulation);

    /**
     * Trouve les annulations contenant un mot-clé dans le commentaire
     * @param keyword Mot-clé à rechercher
     * @return Liste des annulations
     */
    List<Annulation> findByCommentaireContaining(String keyword);

    /**
     * Compte le nombre d'annulations par volontaire
     * @param idVol Identifiant du volontaire
     * @return Nombre d'annulations
     */
    @Query("SELECT COUNT(a) FROM Annulation a WHERE a.idVol = :idVol")
    Long countAnnulationsByVolontaire(@Param("idVol") int idVol);

    @Query("SELECT COUNT(a) FROM Annulation a WHERE a.idVol = :idVol AND (a.dateAnnulation LIKE CONCAT(:yearN, '%') OR a.dateAnnulation LIKE CONCAT(:yearN1, '%'))")
    Long countAnnulationsByVolontaireCurrentAndLastYear(@Param("idVol") int idVol, @Param("yearN") String yearN, @Param("yearN1") String yearN1);

    @Query("SELECT COUNT(a) FROM Annulation a WHERE a.idVol = :idVol AND a.dateAnnulation LIKE CONCAT(:year, '%')")
    Long countAnnulationsByVolontaireAndYear(@Param("idVol") int idVol, @Param("year") String year);

    /**
     * Trouve les annulations par identifiant de volontaire, triées par date d'annulation
     * @param idVol Identifiant du volontaire
     * @return Liste des annulations triées
     */
    @Query("SELECT a FROM Annulation a WHERE a.idVol = :idVol ORDER BY a.dateAnnulation DESC")
    List<Annulation> findByIdVolOrderByDateAnnulationDesc(@Param("idVol") int idVol);

    @Modifying
    @Transactional
    @Query("DELETE FROM Annulation a WHERE a.idVol = :idVol")
    int deleteByIdVol(@Param("idVol") int idVol);
}