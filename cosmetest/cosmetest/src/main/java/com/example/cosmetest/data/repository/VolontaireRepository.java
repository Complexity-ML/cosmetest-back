package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Volontaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repository pour l'accès aux données de l'entité Volontaire
 */
@Repository
public interface VolontaireRepository extends JpaRepository<Volontaire, Integer> {

        // ==================== MÉTHODES EXISTANTES ====================

        Page<Volontaire> findByArchiveFalse(Pageable pageable);

        Page<Volontaire> findAll(Pageable pageable);

        /**
         * Trouve des volontaires par nom
         */
        List<Volontaire> findByNomVol(String nomVol);

        /**
         * Trouve des volontaires par prénom
         */
        List<Volontaire> findByPrenomVol(String prenomVol);

        /**
         * Trouve des volontaires par nom et prénom
         */
        List<Volontaire> findByNomVolAndPrenomVol(String nomVol, String prenomVol);

        /**
         * Trouve des volontaires par email
         */
        List<Volontaire> findByEmailVol(String emailVol);

        /**
         * Trouve des volontaires par sexe
         */
        List<Volontaire> findBySexe(String sexe);

        /**
         * Trouve des volontaires par ethnie
         */
        List<Volontaire> findByEthnie(String ethnie);

        /**
         * Trouve des volontaires par ville
         */
        List<Volontaire> findByVilleVol(String villeVol);

        /**
         * Trouve des volontaires par code postal
         */
        List<Volontaire> findByCpVol(String cpVol);

        /**
         * Trouve des volontaires par état d'archivage
         */
        List<Volontaire> findByArchive(Boolean archive);

        /**
         * Trouve des volontaires par plage d'âge
         */
        @Query("SELECT v FROM Volontaire v WHERE v.dateNaissance BETWEEN :dateFin AND :dateDebut")
        List<Volontaire> findByAgeBetween(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);

        /**
         * Recherche fulltext utilisant l'index idx_fulltext_vol
         * Recherche dans: ID, nom, prénom, nom+prénom, email, téléphones
         */
        @Query(value = "SELECT v FROM Volontaire v WHERE " +
                        "CAST(v.idVol AS string) LIKE :keyword OR " +
                        "LOWER(v.nomVol) LIKE LOWER(:keyword) OR " +
                        "LOWER(v.prenomVol) LIKE LOWER(:keyword) OR " +
                        "LOWER(CONCAT(v.nomVol, ' ', v.prenomVol)) LIKE LOWER(:keyword) OR " +
                        "LOWER(CONCAT(v.prenomVol, ' ', v.nomVol)) LIKE LOWER(:keyword) OR " +
                        "CAST(v.telDomicileVol AS string) LIKE :keyword OR " +
                        "CAST(v.telPortableVol AS string) LIKE :keyword OR " +
                        "LOWER(v.emailVol) LIKE LOWER(:keyword)")
        Page<Volontaire> findByFullTextSearch(@Param("keyword") String keyword, Pageable pageable);

        /**
         * Version SQL native optimisée exploitant directement l'index fulltext
         * Recherche dans: ID, nom, prénom, nom+prénom, email, téléphones
         */
        @Query(value = "SELECT * FROM volontaire WHERE " +
                        "CAST(id_vol AS CHAR) LIKE :keyword OR " +
                        "LOWER(nom_vol) LIKE LOWER(:keyword) OR " +
                        "LOWER(prenom_vol) LIKE LOWER(:keyword) OR " +
                        "LOWER(CONCAT(nom_vol, ' ', prenom_vol)) LIKE LOWER(:keyword) OR " +
                        "LOWER(CONCAT(prenom_vol, ' ', nom_vol)) LIKE LOWER(:keyword) OR " +
                        "CAST(tel_domicile_vol AS CHAR) LIKE :keyword OR " +
                        "CAST(tel_portable_vol AS CHAR) LIKE :keyword OR " +
                        "LOWER(email_vol) LIKE LOWER(:keyword)", countQuery = "SELECT COUNT(*) FROM volontaire WHERE " +
                                        "CAST(id_vol AS CHAR) LIKE :keyword OR " +
                                        "LOWER(nom_vol) LIKE LOWER(:keyword) OR " +
                                        "LOWER(prenom_vol) LIKE LOWER(:keyword) OR " +
                                        "LOWER(CONCAT(nom_vol, ' ', prenom_vol)) LIKE LOWER(:keyword) OR " +
                                        "LOWER(CONCAT(prenom_vol, ' ', nom_vol)) LIKE LOWER(:keyword) OR " +
                                        "CAST(tel_domicile_vol AS CHAR) LIKE :keyword OR " +
                                        "CAST(tel_portable_vol AS CHAR) LIKE :keyword OR " +
                                        "LOWER(email_vol) LIKE LOWER(:keyword)", nativeQuery = true)
        Page<Volontaire> findByFullTextSearchNative(@Param("keyword") String keyword, Pageable pageable);

        /**
         * Trouve des volontaires par phototype
         */
        List<Volontaire> findByPhototype(String phototype);

        /**
         * Trouve des volontaires compatibles pour les études de santé
         */
        List<Volontaire> findBySanteCompatible(String santeCompatible);

        /**
         * Trouve des volontaires par type de peau du visage
         */
        List<Volontaire> findByTypePeauVisage(String typePeauVisage);

        /**
         * Trouve les volontaires qui ont déclaré avoir de l'acné
         */
        List<Volontaire> findByAcne(String acne);

        /**
         * Recherche de volontaires par texte (recherche plein texte)
         * Recherche dans: ID, nom, prénom, nom+prénom, email, téléphones
         */
        @Query(value = "SELECT * FROM volontaire v WHERE " +
                        "CAST(v.id_vol AS CHAR) LIKE CONCAT('%', :searchText, '%') OR " +
                        "LOWER(v.nom_vol) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                        "LOWER(v.prenom_vol) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                        "LOWER(CONCAT(v.nom_vol, ' ', v.prenom_vol)) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                        "LOWER(CONCAT(v.prenom_vol, ' ', v.nom_vol)) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                        "LOWER(v.email_vol) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
                        "CAST(v.tel_domicile_vol AS CHAR) LIKE CONCAT('%', :searchText, '%') OR " +
                        "CAST(v.tel_portable_vol AS CHAR) LIKE CONCAT('%', :searchText, '%')", nativeQuery = true)
        List<Volontaire> searchVolontaires(@Param("searchText") String searchText);

        /**
         * Compte le nombre de volontaires non archivés
         */
        int countByArchive(boolean archive);

        @Query("SELECT COUNT(v) FROM Volontaire v WHERE v.dateI BETWEEN :startDate AND :endDate")
        int countByDateIBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

        @Query("SELECT v FROM Volontaire v ORDER BY v.dateI DESC")
        List<Volontaire> findRecentVolontaires(Pageable pageable);

        /**
         * Recherche tous les volontaires (archivés et non archivés)
         * Recherche dans: ID, nom, prénom, nom+prénom, email
         */
        @Query("SELECT v FROM Volontaire v WHERE " +
                        "(CAST(v.idVol AS string) LIKE CONCAT('%', :search, '%') " +
                        "OR LOWER(v.nomVol) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(v.prenomVol) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(CONCAT(v.nomVol, ' ', v.prenomVol)) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(CONCAT(v.prenomVol, ' ', v.nomVol)) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(v.emailVol) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<Volontaire> searchAll(@Param("search") String search, Pageable pageable);

        /**
         * Recherche les volontaires actifs (non archivés)
         * Recherche dans: ID, nom, prénom, nom+prénom, email
         */
        @Query("SELECT v FROM Volontaire v WHERE v.archive = false AND " +
                        "(CAST(v.idVol AS string) LIKE CONCAT('%', :search, '%') " +
                        "OR LOWER(v.nomVol) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(v.prenomVol) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(CONCAT(v.nomVol, ' ', v.prenomVol)) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(CONCAT(v.prenomVol, ' ', v.nomVol)) LIKE LOWER(CONCAT('%', :search, '%')) " +
                        "OR LOWER(v.emailVol) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<Volontaire> searchActive(@Param("search") String search, Pageable pageable);

        List<Volontaire> findAllByIdVolIn(java.util.List<Integer> ids);

        // ==================== NOUVELLES MÉTHODES OPTIMISÉES POUR LE CALENDRIER
        // ====================

        /**
         * Récupère les volontaires avec leurs informations minimales pour le calendrier
         * Optimisé pour récupérer plusieurs volontaires en une seule requête
         */
        @Query("SELECT v.idVol, v.nomVol, v.prenomVol, v.titreVol, v.dateNaissance FROM Volontaire v WHERE v.idVol IN :ids")
        List<Object[]> findVolontairesMinimalByIds(@Param("ids") List<Integer> ids);

        /**
         * Méthode wrapper pour convertir en Map les informations minimales des
         * volontaires
         */
        default Map<Integer, Map<String, Object>> findVolontairesMinimalByIdsAsMap(List<Integer> ids) {
                if (ids == null || ids.isEmpty()) {
                        return new HashMap<>();
                }

                List<Object[]> results = findVolontairesMinimalByIds(ids);

                return results.stream()
                                .collect(Collectors.toMap(
                                                row -> (Integer) row[0], // idVol comme clé
                                                row -> {
                                                        Map<String, Object> volontaireMap = new HashMap<>();
                                                        volontaireMap.put("id", row[0]);
                                                        volontaireMap.put("nom", row[1]);
                                                        volontaireMap.put("prenom", row[2]);
                                                        volontaireMap.put("titre", row[3]);
                                                        volontaireMap.put("dateNaissance", row[4]);
                                                        return volontaireMap;
                                                }));
        }

        /**
         * Compte les volontaires actifs par tranche d'âge
         */
        @Query("SELECT " +
                        "CASE " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 18 THEN 'Moins de 18 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 30 THEN '18-29 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 40 THEN '30-39 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 50 THEN '40-49 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 60 THEN '50-59 ans' " +
                        "  ELSE '60 ans et plus' " +
                        "END as trancheAge, " +
                        "COUNT(v) as nombre " +
                        "FROM Volontaire v " +
                        "WHERE v.archive = false AND v.dateNaissance IS NOT NULL " +
                        "GROUP BY " +
                        "CASE " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 18 THEN 'Moins de 18 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 30 THEN '18-29 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 40 THEN '30-39 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 50 THEN '40-49 ans' " +
                        "  WHEN DATEDIFF(CURDATE(), v.dateNaissance) / 365 < 60 THEN '50-59 ans' " +
                        "  ELSE '60 ans et plus' " +
                        "END")
        List<Object[]> countVolontairesActifsByTrancheAgeRaw();

        /**
         * Méthode wrapper pour les statistiques par tranche d'âge
         */
        default Map<String, Integer> countVolontairesActifsByTrancheAge() {
                List<Object[]> results = countVolontairesActifsByTrancheAgeRaw();

                return results.stream()
                                .collect(Collectors.toMap(
                                                row -> (String) row[0],
                                                row -> ((Long) row[1]).intValue(),
                                                (existing, replacement) -> existing,
                                                HashMap::new));
        }

        /**
         * Trouve les volontaires les plus actifs dans une période (basé sur le nombre
         * de RDV)
         */
        @Query("SELECT v.idVol, v.nomVol, v.prenomVol, COUNT(r) as rdvCount " +
                        "FROM Volontaire v " +
                        "INNER JOIN Rdv r ON r.idVolontaire = v.idVol " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "GROUP BY v.idVol, v.nomVol, v.prenomVol " +
                        "ORDER BY COUNT(r) DESC")
        List<Object[]> findVolontairesLesPlusActifsRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin,
                        Pageable pageable);

        /**
         * Méthode wrapper pour les volontaires les plus actifs
         */
        default List<Map<String, Object>> findVolontairesLesPlusActifs(Date dateDebut, Date dateFin, int limit) {
                Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
                List<Object[]> results = findVolontairesLesPlusActifsRaw(dateDebut, dateFin, pageable);

                return results.stream()
                                .map(row -> {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("id", row[0]);
                                        map.put("nom", row[1]);
                                        map.put("prenom", row[2]);
                                        map.put("nombreRdv", ((Long) row[3]).intValue());
                                        return map;
                                })
                                .collect(Collectors.toList());
        }

        /**
         * Compte les volontaires par sexe (actifs uniquement)
         */
        @Query("SELECT v.sexe, COUNT(v) FROM Volontaire v " +
                        "WHERE v.archive = false AND v.sexe IS NOT NULL " +
                        "GROUP BY v.sexe")
        List<Object[]> countVolontairesActifsBySexeRaw();

        /**
         * Méthode wrapper pour les statistiques par sexe
         */
        default Map<String, Integer> countVolontairesActifsBySexe() {
                List<Object[]> results = countVolontairesActifsBySexeRaw();

                return results.stream()
                                .collect(Collectors.toMap(
                                                row -> (String) row[0],
                                                row -> ((Long) row[1]).intValue(),
                                                (existing, replacement) -> existing,
                                                HashMap::new));
        }

        /**
         * Compte les volontaires par type de peau (actifs uniquement)
         */
        @Query("SELECT v.typePeauVisage, COUNT(v) FROM Volontaire v " +
                        "WHERE v.archive = false AND v.typePeauVisage IS NOT NULL " +
                        "GROUP BY v.typePeauVisage")
        List<Object[]> countVolontairesActifsByTypePeauRaw();

        /**
         * Méthode wrapper pour les statistiques par type de peau
         */
        default Map<String, Integer> countVolontairesActifsByTypePeau() {
                List<Object[]> results = countVolontairesActifsByTypePeauRaw();

                return results.stream()
                                .collect(Collectors.toMap(
                                                row -> (String) row[0],
                                                row -> ((Long) row[1]).intValue(),
                                                (existing, replacement) -> existing,
                                                HashMap::new));
        }

        /**
         * Trouve les volontaires disponibles (sans RDV) pour une période donnée
         */
        @Query("SELECT v FROM Volontaire v " +
                        "WHERE v.archive = false " +
                        "AND NOT EXISTS (" +
                        "   SELECT 1 FROM Rdv r " +
                        "   WHERE r.idVolontaire = v.idVol " +
                        "   AND r.date BETWEEN :dateDebut AND :dateFin" +
                        ") " +
                        "ORDER BY v.nomVol, v.prenomVol")
        List<Volontaire> findVolontairesDisponiblesPourPeriode(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Compte les volontaires disponibles pour une période
         */
        @Query("SELECT COUNT(v) FROM Volontaire v " +
                        "WHERE v.archive = false " +
                        "AND NOT EXISTS (" +
                        "   SELECT 1 FROM Rdv r " +
                        "   WHERE r.idVolontaire = v.idVol " +
                        "   AND r.date BETWEEN :dateDebut AND :dateFin" +
                        ")")
        Integer countVolontairesDisponiblesPourPeriode(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Trouve les volontaires avec des RDV dans une période donnée
         */
        @Query("SELECT DISTINCT v FROM Volontaire v " +
                        "INNER JOIN Rdv r ON r.idVolontaire = v.idVol " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "ORDER BY v.nomVol, v.prenomVol")
        List<Volontaire> findVolontairesAvecRdvDansPeriode(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Statistiques globales des volontaires pour le calendrier
         */
        @Query("SELECT " +
                        "COUNT(v) as totalVolontaires, " +
                        "COUNT(CASE WHEN v.archive = false THEN 1 END) as volontairesActifs, " +
                        "COUNT(CASE WHEN v.archive = true THEN 1 END) as volontairesArchives, " +
                        "COUNT(CASE WHEN v.dateI >= :dateDebut THEN 1 END) as nouveauxVolontaires " +
                        "FROM Volontaire v")
        List<Object[]> getStatistiquesGlobalesVolontairesRaw(@Param("dateDebut") Date dateDebut);

        /**
         * Méthode wrapper pour les statistiques globales
         */
        default Map<String, Integer> getStatistiquesGlobalesVolontaires(Date dateDebut) {
                List<Object[]> results = getStatistiquesGlobalesVolontairesRaw(dateDebut);

                if (results.isEmpty()) {
                        return new HashMap<>();
                }

                Object[] row = results.get(0);
                Map<String, Integer> stats = new HashMap<>();
                stats.put("totalVolontaires", ((Long) row[0]).intValue());
                stats.put("volontairesActifs", ((Long) row[1]).intValue());
                stats.put("volontairesArchives", ((Long) row[2]).intValue());
                stats.put("nouveauxVolontaires", ((Long) row[3]).intValue());

                return stats;
        }
}