package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.EtudeVolontaire;
import com.example.cosmetest.domain.model.EtudeVolontaireId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository pour l'accès aux données des associations étude-volontaire
 * Fait partie de la couche d'accès aux données (DAL)
 */
@Repository
public interface EtudeVolontaireRepository extends JpaRepository<EtudeVolontaire, EtudeVolontaireId> {

    /**
     * Trouve les associations par identifiant d'étude
     * @param idEtude Identifiant de l'étude
     * @return Liste des associations
     */
    @Query("SELECT ev FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude")
    List<EtudeVolontaire> findByIdEtude(@Param("idEtude") int idEtude);

    /**
     * Trouve les associations par identifiant de volontaire
     * @param idVolontaire Identifiant du volontaire
     * @return Liste des associations
     */
    @Query("SELECT ev FROM EtudeVolontaire ev WHERE ev.id.idVolontaire = :idVolontaire")
    List<EtudeVolontaire> findByIdVolontaire(@Param("idVolontaire") int idVolontaire);

    /**
     * Trouve les associations par identifiant de groupe
     * @param idGroupe Identifiant du groupe
     * @return Liste des associations
     */
    @Query("SELECT ev FROM EtudeVolontaire ev WHERE ev.id.idGroupe = :idGroupe")
    List<EtudeVolontaire> findByIdGroupe(@Param("idGroupe") int idGroupe);

    /**
     * Trouve les associations par étude et volontaire
     * @param idEtude Identifiant de l'étude
     * @param idVolontaire Identifiant du volontaire
     * @return Liste des associations
     */
    @Query("SELECT ev FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude AND ev.id.idVolontaire = :idVolontaire")
    List<EtudeVolontaire> findByIdEtudeAndIdVolontaire(@Param("idEtude") int idEtude, @Param("idVolontaire") int idVolontaire);

    /**
     * Trouve les associations par étude et groupe
     * @param idEtude Identifiant de l'étude
     * @param idGroupe Identifiant du groupe
     * @return Liste des associations
     */
    @Query("SELECT ev FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude AND ev.id.idGroupe = :idGroupe")
    List<EtudeVolontaire> findByIdEtudeAndIdGroupe(@Param("idEtude") int idEtude, @Param("idGroupe") int idGroupe);

    /**
     * Trouve les associations par statut
     * @param statut Statut recherché
     * @return Liste des associations
     */
    @Query("SELECT ev FROM EtudeVolontaire ev WHERE ev.id.statut = :statut")
    List<EtudeVolontaire> findByStatut(@Param("statut") String statut);

    /**
     * Trouve les associations par indicateur de paiement
     * @param paye Indicateur de paiement (1 pour payé, 0 pour non payé)
     * @return Liste des associations
     */
    @Query("SELECT ev FROM EtudeVolontaire ev WHERE ev.id.paye = :paye")
    List<EtudeVolontaire> findByPaye(@Param("paye") int paye);

    /**
     * Compte le nombre de volontaires par étude
     * @param idEtude Identifiant de l'étude
     * @return Nombre de volontaires
     */
    @Query("SELECT COUNT(ev) FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude")
    Long countVolontairesByEtude(@Param("idEtude") int idEtude);

    /**
     * Compte le nombre d'études par volontaire
     * @param idVolontaire Identifiant du volontaire
     * @return Nombre d'études
     */
    @Query("SELECT COUNT(ev) FROM EtudeVolontaire ev WHERE ev.id.idVolontaire = :idVolontaire")
    Long countEtudesByVolontaire(@Param("idVolontaire") int idVolontaire);

    /**
     * Vérifie si une association existe avec les identifiants donnés
     * @param idEtude Identifiant de l'étude
     * @param idVolontaire Identifiant du volontaire
     * @return true si l'association existe
     */
    @Query("SELECT COUNT(ev) > 0 FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude AND ev.id.idVolontaire = :idVolontaire")
    boolean existsByIdEtudeAndIdVolontaire(@Param("idEtude") int idEtude, @Param("idVolontaire") int idVolontaire);

    /**
     * Récupère l'indemnité volontaire (IV) la plus fréquente pour une étude donnée
     * @param idEtude Identifiant de l'étude
     * @return Valeur de l'IV la plus fréquente ou null si aucune association
     */
    @Query(value = "SELECT iv FROM etude_volontaire WHERE id_etude = :idEtude GROUP BY iv ORDER BY COUNT(*) DESC LIMIT 1",
            nativeQuery = true)
    Integer findMostCommonIvByIdEtude(@Param("idEtude") int idEtude);

    /**
     * Récupère la première indemnité volontaire trouvée pour une étude
     * Utilisé comme fallback si aucune valeur fréquente n'est trouvée
     * @param idEtude Identifiant de l'étude
     * @return Première valeur d'IV trouvée ou null si aucune association
     */
    @Query(value = "SELECT iv FROM etude_volontaire WHERE id_etude = :idEtude LIMIT 1",
            nativeQuery = true)
    Integer findFirstIvByIdEtude(@Param("idEtude") int idEtude);

    /**
     * Met à jour l'indemnité volontaire pour tous les volontaires d'une étude
     * @param idEtude Identifiant de l'étude
     * @param iv Nouvelle valeur d'indemnité volontaire
     * @return Nombre de lignes affectées
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE etude_volontaire SET iv = :iv WHERE id_etude = :idEtude",
            nativeQuery = true)
    int updateIvForAllVolontairesInEtude(@Param("idEtude") int idEtude, @Param("iv") int iv);

    /**
     * Calcule la moyenne des indemnités volontaires pour une étude
     * @param idEtude Identifiant de l'étude
     * @return Moyenne des IV
     */
    @Query("SELECT AVG(ev.id.iv) FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude")
    Double calculateAverageIvByIdEtude(@Param("idEtude") int idEtude);

    /**
     * Trouve la valeur minimale d'indemnité volontaire pour une étude
     * @param idEtude Identifiant de l'étude
     * @return Valeur minimale d'IV
     */
    @Query("SELECT MIN(ev.id.iv) FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude")
    Integer findMinIvByIdEtude(@Param("idEtude") int idEtude);

    /**
     * Trouve la valeur maximale d'indemnité volontaire pour une étude
     * @param idEtude Identifiant de l'étude
     * @return Valeur maximale d'IV
     */
    @Query("SELECT MAX(ev.id.iv) FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude")
    Integer findMaxIvByIdEtude(@Param("idEtude") int idEtude);

    /**
     * Compte le nombre de volontaires avec une indemnité spécifique dans une étude
     * @param idEtude Identifiant de l'étude
     * @param iv Valeur d'indemnité volontaire
     * @return Nombre de volontaires
     */
    @Query("SELECT COUNT(ev) FROM EtudeVolontaire ev WHERE ev.id.idEtude = :idEtude AND ev.id.iv = :iv")
    Long countVolontairesByEtudeAndIv(@Param("idEtude") int idEtude, @Param("iv") int iv);

    /**
     * Fournit un résumé agrégé des paiements par étude.
     * @param idEtude identifiant de l'étude ou null pour toutes les études
     * @return lignes brutes (idEtude, total, payes, nonPayes, enAttente, annules, montantTotal, montantPaye, montantAnnules)
     */
    @Query(value = """
        SELECT
            ev.id_etude AS idEtude,
            COUNT(*) AS total,
            SUM(CASE WHEN ev.paye = 1 THEN 1 ELSE 0 END) AS payes,
            SUM(CASE WHEN ev.paye = 0 OR ev.paye IS NULL THEN 1 ELSE 0 END) AS nonPayes,
            SUM(CASE WHEN ev.paye = 2 THEN 1 ELSE 0 END) AS enAttente,
            SUM(CASE WHEN a.id_etude IS NOT NULL THEN 1 ELSE 0 END) AS annules,
            COALESCE(SUM(ev.iv), 0) AS montantTotal,
            COALESCE(SUM(CASE WHEN ev.paye = 1 THEN ev.iv ELSE 0 END), 0) AS montantPaye,
            COALESCE(SUM(CASE WHEN a.id_etude IS NOT NULL THEN ev.iv ELSE 0 END), 0) AS montantAnnules
        FROM etude_volontaire ev
        LEFT JOIN annulation a ON a.id_etude = ev.id_etude AND a.id_vol = ev.id_volontaire
        WHERE (:idEtude IS NULL OR ev.id_etude = :idEtude)
        GROUP BY ev.id_etude
    """, nativeQuery = true)
    List<Object[]> fetchEtudePaiementSummaries(@Param("idEtude") Integer idEtude);
}
