package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.EtudeVolontaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface EtudeVolontaireRepository extends JpaRepository<EtudeVolontaire, Long> {
    List<EtudeVolontaire> findByIdEtude(int idEtude);
    List<EtudeVolontaire> findByIdVolontaire(int idVolontaire);
    List<EtudeVolontaire> findByIdGroupe(int idGroupe);
    List<EtudeVolontaire> findByIdEtudeAndIdVolontaire(int idEtude, int idVolontaire);
    List<EtudeVolontaire> findByIdEtudeAndIdGroupe(int idEtude, int idGroupe);
    List<EtudeVolontaire> findByStatut(String statut);
    List<EtudeVolontaire> findByPaye(int paye);

    @Query("select ev from EtudeVolontaire ev where ev.idEtude=:idEtude and ev.idGroupe=:idGroupe and ev.idVolontaire=:idVolontaire and ev.iv=:iv and ev.numSujet=:numsujet and ev.paye=:paye and ev.statut=:statut")
    List<EtudeVolontaire> findByLegacyKey(@Param("idEtude") int idEtude, @Param("idGroupe") int idGroupe,
            @Param("idVolontaire") int idVolontaire, @Param("iv") int iv, @Param("numsujet") int numsujet,
            @Param("paye") int paye, @Param("statut") String statut);

    @Query("select count(ev) from EtudeVolontaire ev where ev.idEtude=:idEtude")
    Long countVolontairesByEtude(@Param("idEtude") int idEtude);
    @Query("select count(ev) from EtudeVolontaire ev where ev.idVolontaire=:idVolontaire")
    Long countEtudesByVolontaire(@Param("idVolontaire") int idVolontaire);
    boolean existsByIdEtudeAndIdVolontaire(int idEtude, int idVolontaire);

    @Query("select count(ev) from EtudeVolontaire ev where ev.idEtude=:idEtude and ev.numSujet=:numSujet and ev.idVolontaire<>:idVolontaire")
    long countNumSujetUsedByOtherVolontaire(@Param("idEtude") int idEtude, @Param("numSujet") int numSujet,
            @Param("idVolontaire") int idVolontaire);

    @Modifying @Transactional
    @Query("delete from EtudeVolontaire ev where ev.idEtude=:idEtude and ev.idVolontaire=:idVolontaire")
    int deleteByIdEtudeAndIdVolontaire(@Param("idEtude") int idEtude, @Param("idVolontaire") int idVolontaire);
    @Modifying @Transactional
    @Query("delete from EtudeVolontaire ev where ev.idVolontaire=:idVolontaire")
    int deleteByIdVolontaire(@Param("idVolontaire") int idVolontaire);

    @Query(value="SELECT iv FROM etude_volontaire WHERE id_etude=:idEtude GROUP BY iv ORDER BY COUNT(*) DESC LIMIT 1", nativeQuery=true)
    Integer findMostCommonIvByIdEtude(@Param("idEtude") int idEtude);
    @Query(value="SELECT iv FROM etude_volontaire WHERE id_etude=:idEtude LIMIT 1", nativeQuery=true)
    Integer findFirstIvByIdEtude(@Param("idEtude") int idEtude);
    @Modifying @Transactional
    @Query("update EtudeVolontaire ev set ev.iv=:iv where ev.idEtude=:idEtude")
    int updateIvForAllVolontairesInEtude(@Param("idEtude") int idEtude, @Param("iv") int iv);
    @Query("select avg(ev.iv) from EtudeVolontaire ev where ev.idEtude=:idEtude")
    Double calculateAverageIvByIdEtude(@Param("idEtude") int idEtude);
    @Query("select min(ev.iv) from EtudeVolontaire ev where ev.idEtude=:idEtude")
    Integer findMinIvByIdEtude(@Param("idEtude") int idEtude);
    @Query("select max(ev.iv) from EtudeVolontaire ev where ev.idEtude=:idEtude")
    Integer findMaxIvByIdEtude(@Param("idEtude") int idEtude);
    @Query("select count(ev) from EtudeVolontaire ev where ev.idEtude=:idEtude and ev.iv=:iv")
    Long countVolontairesByEtudeAndIv(@Param("idEtude") int idEtude, @Param("iv") int iv);

    @Query(value="""
      SELECT ev.id_etude, COUNT(*),
       SUM(CASE WHEN a.id_etude IS NULL AND ev.paye=1 THEN 1 ELSE 0 END),
       SUM(CASE WHEN a.id_etude IS NULL AND (ev.paye=0 OR ev.paye IS NULL) THEN 1 ELSE 0 END),
       SUM(CASE WHEN a.id_etude IS NULL AND ev.paye=2 THEN 1 ELSE 0 END),
       SUM(CASE WHEN a.id_etude IS NOT NULL THEN 1 ELSE 0 END),
       COALESCE(SUM(CASE WHEN a.id_etude IS NULL THEN ev.iv ELSE 0 END),0),
       COALESCE(SUM(CASE WHEN a.id_etude IS NULL AND ev.paye=1 THEN ev.iv ELSE 0 END),0),
       COALESCE(SUM(CASE WHEN a.id_etude IS NOT NULL THEN ev.iv ELSE 0 END),0)
      FROM etude_volontaire ev
      LEFT JOIN annulation a ON a.id_etude=ev.id_etude AND a.id_vol=ev.id_volontaire
      WHERE (:idEtude IS NULL OR ev.id_etude=:idEtude)
      GROUP BY ev.id_etude
      """, nativeQuery=true)
    List<Object[]> fetchEtudePaiementSummaries(@Param("idEtude") Integer idEtude);
}
