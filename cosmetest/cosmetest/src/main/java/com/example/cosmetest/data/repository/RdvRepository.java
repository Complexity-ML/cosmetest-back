package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Rdv;
import com.example.cosmetest.domain.model.RdvId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface RdvRepository extends JpaRepository<Rdv, RdvId> {

        @Query("SELECT r FROM Rdv r WHERE r.idVolontaire = :idVolontaire")
        List<Rdv> findByIdVolontaire(@Param("idVolontaire") Integer idVolontaire);

        @Query("SELECT r FROM Rdv r WHERE r.idVolontaire = :idVolontaire AND r.id.idEtude = :idEtude")
        List<Rdv> findByIdVolontaireAndIdEtude(@Param("idVolontaire") Integer idVolontaire, @Param("idEtude") Integer idEtude);

        @Query("SELECT r FROM Rdv r WHERE r.date = :date")
        List<Rdv> findByDate(@Param("date") Date date);

        @Query("SELECT r FROM Rdv r WHERE r.idVolontaire = :idVolontaire AND r.date = :date")
        List<Rdv> findByIdVolontaireAndDate(@Param("idVolontaire") Integer idVolontaire, @Param("date") Date date);

        @Query("SELECT r FROM Rdv r WHERE r.idGroupe = :idGroupe")
        List<Rdv> findByIdGroupe(@Param("idGroupe") Integer idGroupe);

        @Query("SELECT r FROM Rdv r WHERE r.etat = :etat")
        List<Rdv> findByEtat(@Param("etat") String etat);

        @Query("SELECT r FROM Rdv r WHERE r.idVolontaire = :idVolontaire AND r.date >= :startDate AND r.date <= :endDate")
        List<Rdv> findByVolontaireAndDateRange(
                        @Param("idVolontaire") Integer idVolontaire,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("SELECT COUNT(r) FROM Rdv r WHERE r.idGroupe = :idGroupe")
        Long countRdvsByGroupe(@Param("idGroupe") Integer idGroupe);

        @Query("SELECT r FROM Rdv r WHERE r.commentaires LIKE %:keyword%")
        List<Rdv> findByCommentairesContaining(@Param("keyword") String keyword);

        @Query("SELECT r FROM Rdv r WHERE r.date > :date ORDER BY r.date ASC")
        List<Rdv> findByDateAfterOrderByDateAsc(@Param("date") Date date);

        @Query("SELECT COUNT(r) FROM Rdv r WHERE r.date = :date")
        int countByDate(@Param("date") Date date);

        @Query("SELECT COUNT(r) FROM Rdv r WHERE r.etat = :etat AND r.date BETWEEN :startDate AND :endDate")
        int countByEtatAndDateBetween(@Param("etat") String etat, @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);

        @Query("SELECT COUNT(r) FROM Rdv r WHERE r.date BETWEEN :startDate AND :endDate")
        int countByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

        @Query("SELECT r FROM Rdv r WHERE r.id.idEtude = :idEtude ORDER BY r.date DESC")
        List<Rdv> findById_IdEtudeOrderByDateDesc(@Param("idEtude") Integer idEtude);

        @Query("SELECT r FROM Rdv r WHERE r.idVolontaire = :idVolontaire AND r.id.idEtude = :idEtude")
        Optional<Rdv> findByVolontaireIdAndEtudeId(@Param("idVolontaire") Integer idVolontaire,
                        @Param("idEtude") int idEtude);

        @Query("SELECT r FROM Rdv r WHERE r.date >= :date ORDER BY r.date ASC, r.heure ASC")
        Page<Rdv> findByDateGreaterThanEqualOrderByDateAscHeureAsc(@Param("date") Date date, Pageable pageable);

        @Query("SELECT MAX(r.id.idRdv) FROM Rdv r WHERE r.id.idEtude = :idEtude")
        Integer findMaxRdvIdForEtude(@Param("idEtude") Integer idEtude);

        @Query("SELECT r FROM Rdv r WHERE r.id.idEtude = :idEtude")
        List<Rdv> findByIdEtude(@Param("idEtude") Integer idEtude);

        // ==================== MÉTHODES OPTIMISÉES POUR LE CALENDRIER
        // ====================

        /**
         * MÉTHODE CORRIGÉE : Récupère les RDV avec les données d'études ET de
         * volontaires en une seule requête optimisée
         */
        @Query("SELECT r FROM Rdv r " +
                        "LEFT JOIN FETCH r.etude e " +
                        "LEFT JOIN FETCH Volontaire v ON v.idVol = r.idVolontaire " + // ← AJOUT JOINTURE VOLONTAIRES
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "ORDER BY r.date ASC, r.heure ASC")
        List<Rdv> findByDateBetweenWithEtudeAndVolontaireOptimized(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Récupère les RDV d'une étude avec détails optimisés et pagination
         */
        @Query("SELECT r FROM Rdv r " +
                        "LEFT JOIN FETCH r.etude e " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "ORDER BY r.date DESC, r.heure DESC")
        Page<Rdv> findByIdEtudeWithDetailsOptimized(
                        @Param("idEtude") Integer idEtude,
                        Pageable pageable);

        /**
         * Compte les RDV par état entre deux dates (version brute)
         */
        @Query("SELECT r.etat, COUNT(r) FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "GROUP BY r.etat")
        List<Object[]> countRdvByEtatBetweenDatesRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Méthode wrapper pour retourner une Map des états
         */
        default Map<String, Integer> countRdvByEtatBetweenDates(Date dateDebut, Date dateFin) {
                List<Object[]> results = countRdvByEtatBetweenDatesRaw(dateDebut, dateFin);
                return results.stream()
                                .collect(Collectors.toMap(
                                                row -> row[0] != null ? (String) row[0] : "NON_DEFINI",
                                                row -> ((Long) row[1]).intValue(),
                                                (existing, replacement) -> existing,
                                                HashMap::new));
        }

        /**
         * Compte les RDV par jour de la semaine entre deux dates
         */
        @Query("SELECT DAYOFWEEK(r.date) as dayOfWeek, COUNT(r) FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "GROUP BY DAYOFWEEK(r.date) " +
                        "ORDER BY DAYOFWEEK(r.date)")
        List<Object[]> countRdvByDayOfWeekBetweenDatesRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Méthode wrapper pour convertir les numéros de jours en noms français
         */
        default Map<String, Integer> countRdvByDayOfWeekBetweenDates(Date dateDebut, Date dateFin) {
                List<Object[]> results = countRdvByDayOfWeekBetweenDatesRaw(dateDebut, dateFin);
                String[] jours = { "", "Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi" };

                return results.stream()
                                .collect(Collectors.toMap(
                                                row -> jours[(Integer) row[0]],
                                                row -> ((Long) row[1]).intValue(),
                                                (existing, replacement) -> existing,
                                                HashMap::new));
        }

        /**
         * Compte les RDV par heure entre deux dates
         */
        @Query("SELECT SUBSTRING(r.heure, 1, 2) as heure, COUNT(r) FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "AND r.heure IS NOT NULL " +
                        "GROUP BY SUBSTRING(r.heure, 1, 2) " +
                        "ORDER BY SUBSTRING(r.heure, 1, 2)")
        List<Object[]> countRdvByHourBetweenDatesRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Méthode wrapper pour formater les heures
         */
        default Map<String, Integer> countRdvByHourBetweenDates(Date dateDebut, Date dateFin) {
                List<Object[]> results = countRdvByHourBetweenDatesRaw(dateDebut, dateFin);
                return results.stream()
                                .collect(Collectors.toMap(
                                                row -> row[0] + "h",
                                                row -> ((Long) row[1]).intValue(),
                                                (existing, replacement) -> existing,
                                                HashMap::new));
        }

        /**
         * Récupère les RDV ordonnés par date et heure
         */
        @Query("SELECT r FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "ORDER BY r.date ASC, r.heure ASC")
        List<Rdv> findByDateBetweenOrderByDateAscHeureAsc(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Compte les RDV par étude et période
         */
        @Query("SELECT COUNT(r) FROM Rdv r " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "AND r.date BETWEEN :dateDebut AND :dateFin")
        Integer countByIdEtudeAndDateBetween(
                        @Param("idEtude") Integer idEtude,
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Trouve les volontaires avec plusieurs RDV le même jour
         */
        @Query("SELECT r.idVolontaire, r.date, COUNT(r) FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "AND r.idVolontaire IS NOT NULL " +
                        "GROUP BY r.idVolontaire, r.date " +
                        "HAVING COUNT(r) > 1 " +
                        "ORDER BY r.date, r.idVolontaire")
        List<Object[]> findVolontairesWithMultipleRdvSameDay(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Trouve les RDV qui se chevauchent (même date et heure)
         */
        @Query("SELECT r.date, r.heure, COUNT(r) FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "AND r.heure IS NOT NULL " +
                        "GROUP BY r.date, r.heure " +
                        "HAVING COUNT(r) > 1 " +
                        "ORDER BY r.date, r.heure")
        List<Object[]> findOverlappingAppointments(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Trouve les études les plus actives (avec le plus de RDV) dans une période
         */
        @Query("SELECT e.ref, e.titre, COUNT(r) as rdvCount FROM Rdv r " +
                        "INNER JOIN r.etude e " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "GROUP BY e.idEtude, e.ref, e.titre " +
                        "ORDER BY COUNT(r) DESC")
        List<Object[]> findMostActiveStudiesBetweenDatesRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin,
                        Pageable pageable);

        /**
         * Méthode wrapper pour retourner une liste de Maps
         */
        default List<Map<String, Object>> findMostActiveStudiesBetweenDates(
                        Date dateDebut, Date dateFin, int limit) {
                Pageable pageable = PageRequest.of(0, limit);
                List<Object[]> results = findMostActiveStudiesBetweenDatesRaw(dateDebut, dateFin, pageable);

                return results.stream()
                                .map(row -> {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("ref", row[0]);
                                        map.put("titre", row[1]);
                                        map.put("nombreRdv", ((Long) row[2]).intValue());
                                        return map;
                                })
                                .collect(Collectors.toList());
        }

        /**
         * Trouve les volontaires les plus actifs dans une période
         */
        @Query("SELECT v.nomVol, v.prenomVol, COUNT(r) as rdvCount FROM Rdv r " +
                        "INNER JOIN Volontaire v ON v.idVol = r.idVolontaire " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "GROUP BY r.idVolontaire, v.nomVol, v.prenomVol " +
                        "ORDER BY COUNT(r) DESC")
        List<Object[]> findMostActiveVolunteersBetweenDatesRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin,
                        Pageable pageable);

        /**
         * Méthode wrapper pour les volontaires les plus actifs
         */
        default List<Map<String, Object>> findMostActiveVolunteersBetweenDates(
                        Date dateDebut, Date dateFin, int limit) {
                Pageable pageable = PageRequest.of(0, limit);
                List<Object[]> results = findMostActiveVolunteersBetweenDatesRaw(dateDebut, dateFin, pageable);

                return results.stream()
                                .map(row -> {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("nom", row[0]);
                                        map.put("prenom", row[1]);
                                        map.put("nombreRdv", ((Long) row[2]).intValue());
                                        return map;
                                })
                                .collect(Collectors.toList());
        }

        /**
         * Compte les RDV par semaine entre deux dates - Version corrigée avec
         * WEEKOFYEAR
         */
        @Query("SELECT YEAR(r.date) as annee, WEEKOFYEAR(r.date) as semaine, COUNT(r) FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "GROUP BY YEAR(r.date), WEEKOFYEAR(r.date) " +
                        "ORDER BY YEAR(r.date), WEEKOFYEAR(r.date)")
        List<Object[]> countRdvByWeekBetweenDatesRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         * Méthode wrapper pour les statistiques par semaine
         */
        default List<Map<String, Object>> countRdvByWeekBetweenDates(Date dateDebut, Date dateFin) {
                List<Object[]> results = countRdvByWeekBetweenDatesRaw(dateDebut, dateFin);

                return results.stream()
                                .map(row -> {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("annee", row[0]);
                                        map.put("semaine", row[1]);
                                        map.put("count", ((Long) row[2]).intValue());
                                        return map;
                                })
                                .collect(Collectors.toList());
        }

        /**
         * Compte les RDV par semaine et état entre deux dates - Version corrigée avec
         * WEEKOFYEAR
         */
        @Query("SELECT YEAR(r.date) as annee, WEEKOFYEAR(r.date) as semaine, COUNT(r) FROM Rdv r " +
                        "WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "AND r.etat = :etat " +
                        "GROUP BY YEAR(r.date), WEEKOFYEAR(r.date) " +
                        "ORDER BY YEAR(r.date), WEEKOFYEAR(r.date)")
        List<Object[]> countRdvByWeekAndEtatBetweenDatesRaw(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin,
                        @Param("etat") String etat);

        /**
         * Méthode wrapper pour les statistiques par semaine et état
         */
        default List<Map<String, Object>> countRdvByWeekAndEtatBetweenDates(
                        Date dateDebut, Date dateFin, String etat) {
                List<Object[]> results = countRdvByWeekAndEtatBetweenDatesRaw(dateDebut, dateFin, etat);

                return results.stream()
                                .map(row -> {
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("annee", row[0]);
                                        map.put("semaine", row[1]);
                                        map.put("etat", etat);
                                        map.put("count", ((Long) row[2]).intValue());
                                        return map;
                                })
                                .collect(Collectors.toList());
        }

        /**
         *  Récupère les RDV d'une étude pour une date spécifique avec optimisation
         * Méthode spécialisée pour éviter de charger tous les RDV d'une étude
         */
        @Query("SELECT r FROM Rdv r " +
                        "LEFT JOIN FETCH r.etude e " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "AND r.date = :date " +
                        "ORDER BY r.heure ASC")
        List<Rdv> findByIdEtudeAndDateOptimized(
                        @Param("idEtude") Integer idEtude,
                        @Param("date") Date date);

        /**
         *  Compte les RDV d'une étude pour une date spécifique
         * Utile pour des vérifications rapides
         */
        @Query("SELECT COUNT(r) FROM Rdv r " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "AND r.date = :date")
        Integer countByIdEtudeAndDate(
                        @Param("idEtude") Integer idEtude,
                        @Param("date") Date date);

        /**
         *  Récupère les dates avec RDV pour une étude dans une période
         * Optimisé pour obtenir rapidement la liste des dates occupées
         */
        @Query("SELECT DISTINCT r.date FROM Rdv r " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "AND r.date BETWEEN :dateDebut AND :dateFin " +
                        "ORDER BY r.date ASC")
        List<Date> findDistinctDatesByIdEtudeAndPeriod(
                        @Param("idEtude") Integer idEtude,
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);

        /**
         *  Récupère les RDV d'une étude groupés par date dans une période
         * Version brute pour traitement côté service
         */
        @Query("SELECT r.date, COUNT(r) as nombreRdv FROM Rdv r " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "AND r.date BETWEEN :dateDebut AND :dateFin " +
                        "GROUP BY r.date " +
                        "ORDER BY r.date ASC")
        List<Object[]> countRdvsByDateForEtudeInPeriod(
                        @Param("idEtude") Integer idEtude,
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin);
}