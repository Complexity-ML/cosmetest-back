package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Etude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository pour l'accès aux données des études
 * Fait partie de la couche d'accès aux données (DAL)
 */
@Repository
public interface EtudeRepository extends JpaRepository<Etude, Integer> {

        /**
         * Trouve toutes les études non archivées
         */
        List<Etude> findByArchiveFalse();

        /**
         * Trouve toutes les études non archivées avec pagination
         */
        Page<Etude> findByArchiveFalse(Pageable pageable);

        /**
         * Trouve une étude par sa référence
         * 
         * @param ref Référence de l'étude
         * @return Étude correspondante
         */
        Optional<Etude> findByRef(String ref);

        /**
         * Vérifie si une étude existe avec cette référence
         * 
         * @param ref Référence à vérifier
         * @return true si une étude existe avec cette référence
         */
        boolean existsByRef(String ref);

        /**
         * Trouve les études par type
         * 
         * @param type Type d'étude
         * @return Liste des études
         */
        List<Etude> findByType(String type);

        /**
         * Trouve les études dont le titre contient le mot-clé
         * 
         * @param keyword Mot-clé à rechercher
         * @return Liste des études
         */
        List<Etude> findByTitreContaining(String keyword);

        /**
         * Trouve les études entre deux dates
         * 
         * @param debut Date de début
         * @param fin   Date de fin
         * @return Liste des études
         */
        @Query("SELECT e FROM Etude e WHERE e.dateDebut >= :debut AND e.dateFin <= :fin")
        List<Etude> findByDateDebutAndDateFin(@Param("debut") Date debut, @Param("fin") Date fin);

        /**
         * Trouve les études actives à une date donnée
         * 
         * @param date Date à vérifier
         * @return Liste des études
         */
        @Query("SELECT e FROM Etude e WHERE e.dateDebut <= :date AND e.dateFin > :date")
        List<Etude> findActiveEtudesAtDate(@Param("date") LocalDate date);

        /**
         * Trouve les études par indicateur de paiement
         * 
         * @param paye Indicateur de paiement (1 pour payé, 0 pour non payé)
         * @return Liste des études
         */
        List<Etude> findByPaye(int paye);

        /**
         * Recherche en texte intégral dans le titre et les commentaires
         * 
         * @param searchTerm Terme de recherche
         * @return Liste des études
         */
        @Query("SELECT e FROM Etude e WHERE e.titre LIKE %:searchTerm% OR e.commentaires LIKE %:searchTerm% OR e.ref LIKE %:searchTerm%")
        List<Etude> searchByTitreOrCommentairesOrRef(@Param("searchTerm") String searchTerm);

        /**
         * Compte le nombre d'études par type
         * 
         * @param type Type d'étude
         * @return Nombre d'études
         */
        @Query("SELECT COUNT(e) FROM Etude e WHERE e.type = :type")
        Long countByType(@Param("type") String type);

        /**
         * Recherche des études dont la date de fin est après la date spécifiée
         * 
         * @param date Date de référence
         * @return Liste des études
         */
        List<Etude> findByDateFinAfter(Date date);

        /**
         * Recherche des études dont la date de début est avant la date spécifiée
         * 
         * @param date Date de référence
         * @return Liste des études
         */
        List<Etude> findByDateDebutBefore(Date date);

        /**
         * Recherche des études dont la date de fin est avant la date spécifiée
         * 
         * @param date Date de référence
         * @return Liste des études terminées
         */
        List<Etude> findByDateFinBefore(Date date);

        /**
         * Recherche des études dont la date de début est après la date spécifiée
         * 
         * @param date Date de référence
         * @return Liste des études à venir
         */
        List<Etude> findByDateDebutAfter(Date date);

        @Query("SELECT COUNT(e) FROM Etude e WHERE :today BETWEEN e.dateDebut AND e.dateFin")
        long countCurrentEtudes(@Param("today") LocalDate today);

        /**
         * Trouve les études actives entre deux dates
         * Une étude est active si sa période chevauche avec la période demandée
         */
        @Query("SELECT e FROM Etude e " +
                        "WHERE (e.dateDebut <= :dateFin AND e.dateFin >= :dateDebut) " +
                        "ORDER BY e.dateDebut ASC")
        List<Etude> findEtudesActivesEntreDates(
                        @Param("dateDebut") LocalDate dateDebut,
                        @Param("dateFin") LocalDate dateFin);

        /**
         * Compte les études actives entre deux dates
         */
        @Query("SELECT COUNT(e) FROM Etude e " +
                        "WHERE (e.dateDebut <= :dateFin AND e.dateFin >= :dateDebut)")
        Integer countEtudesActivesEntreDates(
                        @Param("dateDebut") LocalDate dateDebut,
                        @Param("dateFin") LocalDate dateFin);

        /**
         * Trouve les études avec le nombre de RDV dans une période donnée
         */
        @Query("SELECT e, COUNT(r) as rdvCount FROM Etude e " +
                        "LEFT JOIN Rdv r ON r.id.idEtude = e.idEtude " +
                        "AND r.date BETWEEN :dateDebut AND :dateFin " +
                        "WHERE (e.dateDebut <= :dateFinEtude AND e.dateFin >= :dateDebutEtude) " +
                        "GROUP BY e.idEtude " +
                        "ORDER BY e.dateDebut ASC")
        List<Object[]> findEtudesAvecNombreRdvDansPeriode(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin,
                        @Param("dateDebutEtude") LocalDate dateDebutEtude,
                        @Param("dateFinEtude") LocalDate dateFinEtude);

        /**
         * Trouve les études par référence ou titre avec ignoration de la casse
         * Version corrigée avec un seul paramètre
         */
        @Query("SELECT e FROM Etude e " +
                        "WHERE LOWER(e.ref) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "OR LOWER(e.titre) LIKE LOWER(CONCAT('%', :query, '%')) " +
                        "ORDER BY e.ref ASC")
        List<Etude> findByRefContainingIgnoreCaseOrTitreContainingIgnoreCase(
                        @Param("query") String query,
                        Pageable pageable);

        /**
         * Trouve les études les plus récentes
         */
        @Query("SELECT e FROM Etude e ORDER BY e.dateDebut DESC")
        List<Etude> findMostRecentEtudes(Pageable pageable);

        /**
         * Compte les études par statut de paiement dans une période
         */
        @Query("SELECT e.paye, COUNT(e) FROM Etude e " +
                        "WHERE (e.dateDebut <= :dateFin AND e.dateFin >= :dateDebut) " +
                        "GROUP BY e.paye")
        List<Object[]> countEtudesByPayeStatusInPeriod(
                        @Param("dateDebut") LocalDate dateDebut,
                        @Param("dateFin") LocalDate dateFin);

        /**
         * Trouve les études avec le plus de RDV
         */
        @Query("SELECT e.ref, e.titre, COUNT(r) as rdvCount FROM Etude e " +
                        "LEFT JOIN Rdv r ON r.id.idEtude = e.idEtude " +
                        "GROUP BY e.idEtude, e.ref, e.titre " +
                        "ORDER BY COUNT(r) DESC")
        List<Object[]> findEtudesWithMostRdvs(Pageable pageable);

        /**
         * Trouve les études sans RDV dans une période
         */
        @Query("SELECT e FROM Etude e " +
                        "WHERE NOT EXISTS (" +
                        "   SELECT 1 FROM Rdv r " +
                        "   WHERE r.id.idEtude = e.idEtude " +
                        "   AND r.date BETWEEN :dateDebut AND :dateFin" +
                        ") " +
                        "AND (e.dateDebut <= :dateFinEtude AND e.dateFin >= :dateDebutEtude)")
        List<Etude> findEtudesSansRdvDansPeriode(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin,
                        @Param("dateDebutEtude") LocalDate dateDebutEtude,
                        @Param("dateFinEtude") LocalDate dateFinEtude);

        /**
         * Calcule les statistiques des études pour une période
         */
        @Query("SELECT " +
                        "COUNT(DISTINCT e.idEtude) as totalEtudes, " +
                        "COUNT(DISTINCT CASE WHEN r.id.idEtude IS NOT NULL THEN e.idEtude END) as etudesAvecRdv, " +
                        "AVG(CAST(rdvCounts.rdvCount AS double)) as moyenneRdvParEtude " +
                        "FROM Etude e " +
                        "LEFT JOIN (SELECT r.id.idEtude as etudeId, COUNT(r) as rdvCount " +
                        "          FROM Rdv r WHERE r.date BETWEEN :dateDebut AND :dateFin " +
                        "          GROUP BY r.id.idEtude) rdvCounts ON rdvCounts.etudeId = e.idEtude " +
                        "LEFT JOIN Rdv r ON r.id.idEtude = e.idEtude AND r.date BETWEEN :dateDebut AND :dateFin " +
                        "WHERE (e.dateDebut <= :dateFinEtude AND e.dateFin >= :dateDebutEtude)")
        List<Object[]> getStatistiquesEtudesPourPeriode(
                        @Param("dateDebut") Date dateDebut,
                        @Param("dateFin") Date dateFin,
                        @Param("dateDebutEtude") LocalDate dateDebutEtude,
                        @Param("dateFinEtude") LocalDate dateFinEtude);

        /**
         * Calcule la charge de travail par période - Approche simple et portable
         * Compte le nombre d'études actives pour une date donnée
         */
        @Query("SELECT COUNT(DISTINCT e.idEtude) FROM Etude e " +
                        "WHERE :date BETWEEN e.dateDebut AND e.dateFin")
        Integer countEtudesActivesParDate(@Param("date") LocalDate date);

        /**
         * Trouve toutes les études actives dans une période donnée
         * Version optimisée pour le calcul de charge
         */
        @Query("SELECT e.idEtude, e.dateDebut, e.dateFin FROM Etude e " +
                        "WHERE (e.dateDebut <= :dateFin AND e.dateFin >= :dateDebut) " +
                        "ORDER BY e.dateDebut ASC")
        List<Object[]> findEtudesActivesInfosPourPeriode(
                        @Param("dateDebut") LocalDate dateDebut,
                        @Param("dateFin") LocalDate dateFin);

        /**
         * Méthode wrapper pour calculer la charge de travail par jour
         * Approche programmatique plus flexible et portable
         */
        default Map<LocalDate, Integer> getChargeTravailParJourAsMap(LocalDate dateDebut, LocalDate dateFin) {
                Map<LocalDate, Integer> chargeMap = new HashMap<>();

                // Récupérer toutes les études actives dans la période
                List<Object[]> etudesActives = findEtudesActivesInfosPourPeriode(dateDebut, dateFin);

                // Parcourir chaque jour de la période
                LocalDate dateCourante = dateDebut;
                while (!dateCourante.isAfter(dateFin)) {
                        final LocalDate dateFinale = dateCourante; // Variable finale pour la lambda

                        // Compter les études actives pour cette date
                        long etudesActivesCount = etudesActives.stream()
                                        .filter(row -> {
                                                LocalDate debutEtude = (LocalDate) row[1];
                                                LocalDate finEtude = (LocalDate) row[2];
                                                return !dateFinale.isBefore(debutEtude)
                                                                && !dateFinale.isAfter(finEtude);
                                        })
                                        .count();

                        chargeMap.put(dateCourante, (int) etudesActivesCount);
                        dateCourante = dateCourante.plusDays(1);
                }

                return chargeMap;
        }

        /**
         * Version alternative utilisant une requête pour chaque date
         * Moins efficace mais plus simple si le nombre de jours est faible
         */
        default Map<LocalDate, Integer> getChargeTravailParJourAsMapAlternative(LocalDate dateDebut,
                        LocalDate dateFin) {
                Map<LocalDate, Integer> chargeMap = new HashMap<>();

                LocalDate dateCourante = dateDebut;
                while (!dateCourante.isAfter(dateFin)) {
                        Integer charge = countEtudesActivesParDate(dateCourante);
                        chargeMap.put(dateCourante, charge);
                        dateCourante = dateCourante.plusDays(1);
                }

                return chargeMap;
        }

        /**
         * Version SQL portable utilisant une approche IN pour les dates
         * Plus efficace pour de petites périodes
         */
        @Query("SELECT " +
                        "CASE " +
                        "  WHEN :date1 BETWEEN e.dateDebut AND e.dateFin THEN :date1 " +
                        "  WHEN :date2 BETWEEN e.dateDebut AND e.dateFin THEN :date2 " +
                        "  WHEN :date3 BETWEEN e.dateDebut AND e.dateFin THEN :date3 " +
                        "  WHEN :date4 BETWEEN e.dateDebut AND e.dateFin THEN :date4 " +
                        "  WHEN :date5 BETWEEN e.dateDebut AND e.dateFin THEN :date5 " +
                        "  WHEN :date6 BETWEEN e.dateDebut AND e.dateFin THEN :date6 " +
                        "  WHEN :date7 BETWEEN e.dateDebut AND e.dateFin THEN :date7 " +
                        "END as dateActive, " +
                        "COUNT(DISTINCT e.idEtude) as charge " +
                        "FROM Etude e " +
                        "WHERE (:date1 BETWEEN e.dateDebut AND e.dateFin) OR " +
                        "      (:date2 BETWEEN e.dateDebut AND e.dateFin) OR " +
                        "      (:date3 BETWEEN e.dateDebut AND e.dateFin) OR " +
                        "      (:date4 BETWEEN e.dateDebut AND e.dateFin) OR " +
                        "      (:date5 BETWEEN e.dateDebut AND e.dateFin) OR " +
                        "      (:date6 BETWEEN e.dateDebut AND e.dateFin) OR " +
                        "      (:date7 BETWEEN e.dateDebut AND e.dateFin) " +
                        "GROUP BY " +
                        "CASE " +
                        "  WHEN :date1 BETWEEN e.dateDebut AND e.dateFin THEN :date1 " +
                        "  WHEN :date2 BETWEEN e.dateDebut AND e.dateFin THEN :date2 " +
                        "  WHEN :date3 BETWEEN e.dateDebut AND e.dateFin THEN :date3 " +
                        "  WHEN :date4 BETWEEN e.dateDebut AND e.dateFin THEN :date4 " +
                        "  WHEN :date5 BETWEEN e.dateDebut AND e.dateFin THEN :date5 " +
                        "  WHEN :date6 BETWEEN e.dateDebut AND e.dateFin THEN :date6 " +
                        "  WHEN :date7 BETWEEN e.dateDebut AND e.dateFin THEN :date7 " +
                        "END " +
                        "ORDER BY dateActive")
        List<Object[]> getChargeTravailPourSemaine(
                        @Param("date1") LocalDate date1,
                        @Param("date2") LocalDate date2,
                        @Param("date3") LocalDate date3,
                        @Param("date4") LocalDate date4,
                        @Param("date5") LocalDate date5,
                        @Param("date6") LocalDate date6,
                        @Param("date7") LocalDate date7);

        /**
         * Méthode pour calculer la charge d'une semaine spécifique
         */
        default Map<LocalDate, Integer> getChargeTravailSemaine(LocalDate debutSemaine) {
                List<LocalDate> datesSemaine = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                        datesSemaine.add(debutSemaine.plusDays(i));
                }

                List<Object[]> results = getChargeTravailPourSemaine(
                                datesSemaine.get(0), datesSemaine.get(1), datesSemaine.get(2),
                                datesSemaine.get(3), datesSemaine.get(4), datesSemaine.get(5),
                                datesSemaine.get(6));

                Map<LocalDate, Integer> chargeMap = new HashMap<>();

                // Initialiser toutes les dates à 0
                datesSemaine.forEach(date -> chargeMap.put(date, 0));

                // Remplir avec les résultats
                for (Object[] row : results) {
                        if (row[0] != null) {
                                LocalDate date = (LocalDate) row[0];
                                Integer charge = ((Long) row[1]).intValue();
                                chargeMap.put(date, charge);
                        }
                }

                return chargeMap;
        }

        /**
         * Version optimisée pour de grandes périodes
         * Utilise une approche par lot pour éviter trop de requêtes
         */
        default Map<LocalDate, Integer> getChargeTravailParJourOptimise(LocalDate dateDebut, LocalDate dateFin) {
                // Si la période est courte (moins de 8 jours), utiliser l'approche semaine
                long joursDiff = java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin);
                if (joursDiff <= 7) {
                        return getChargeTravailSemaine(dateDebut);
                }

                // Pour de plus longues périodes, utiliser l'approche programmatique
                return getChargeTravailParJourAsMap(dateDebut, dateFin);
        }
        // Ajouter juste cette méthode simple dans EtudeRepository.java

        /**
         * Trouve les études auxquelles un volontaire participe (via ses RDV)
         * Remplace le pattern N+1 (1 query RDV + N queries Etude) par un seul JOIN
         */
        @Query("SELECT DISTINCT e FROM Etude e " +
                        "INNER JOIN Rdv r ON r.id.idEtude = e.idEtude " +
                        "WHERE r.idVolontaire = :idVolontaire")
        List<Etude> findEtudesByVolontaireId(@Param("idVolontaire") Integer idVolontaire);

        /**
         * Récupère les dates avec RDV pour une étude (format d'affichage simple)
         */
        @Query("SELECT DISTINCT r.date FROM Rdv r " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "ORDER BY r.date ASC")
        List<Date> findRdvDatesForEtude(@Param("idEtude") Integer idEtude);

        /**
         * Méthode simple pour obtenir l'affichage des dates avec RDV - VERSION CORRIGÉE
         */
        default String getEtudeRdvDatesDisplay(Integer idEtude) {
                try {
                        List<Date> dates = findRdvDatesForEtude(idEtude);

                        if (dates == null || dates.isEmpty()) {
                                return "Aucun RDV";
                        }

                        // Convertir en LocalDate et formater
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM", Locale.FRENCH);

                        if (dates.size() == 1) {
                                return dates.get(0).toLocalDate().format(formatter);
                        }

                        if (dates.size() <= 3) {
                                return dates.stream()
                                                .map(date -> date.toLocalDate().format(formatter))
                                                .collect(Collectors.joining(", "));
                        }

                        // Plus de 3 dates : afficher première, dernière + nombre total
                        String premiere = dates.get(0).toLocalDate().format(formatter);
                        String derniere = dates.get(dates.size() - 1).toLocalDate().format(formatter);

                        return String.format("%s ... %s (%d jours)", premiere, derniere, dates.size());

                } catch (Exception e) {
                        // En cas d'erreur, retourner un message par défaut
                        return "Erreur dates";
                }
        }

        /**
         * Récupère les dates avec RDV pour une étude (format LocalDate)
         */
        @Query("SELECT DISTINCT r.date FROM Rdv r " +
                        "WHERE r.id.idEtude = :idEtude " +
                        "ORDER BY r.date ASC")
        List<Date> findRdvDatesForEtudeAsList(@Param("idEtude") Integer idEtude);

        /**
         * Méthode pour obtenir la liste des dates effectives avec RDV sous forme de
         * LocalDate
         */
        default List<LocalDate> getEtudeRdvDatesAsLocalDateList(Integer idEtude) {
                try {
                        if (idEtude == null) {
                                return new ArrayList<>();
                        }

                        List<Date> dates = findRdvDatesForEtudeAsList(idEtude);

                        if (dates == null || dates.isEmpty()) {
                                return new ArrayList<>();
                        }

                        // Convertir java.sql.Date vers LocalDate avec gestion des nulls
                        return dates.stream()
                                        .filter(Objects::nonNull) // Filtrer les dates nulles
                                        .map(Date::toLocalDate)
                                        .collect(Collectors.toList());

                } catch (Exception e) {
                        // En cas d'erreur, retourner une liste vide
                        // Note: Le logger doit être défini au niveau de la classe d'implémentation
                        System.err.println("Erreur lors de la récupération des dates RDV pour l'étude " + idEtude + ": "
                                        + e.getMessage());
                        return new ArrayList<>();
                }
        }
}