package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Preinscrit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'accès aux données de l'entité Preinscrit
 */
@Repository
public interface PreinscritRepository extends JpaRepository<Preinscrit, Integer> {

    /**
     * Recherche un préinscrit par son email
     * @param email l'email du préinscrit
     * @return le préinscrit correspondant à l'email, s'il existe
     */
    Optional<Preinscrit> findByEmail(String email);

    /**
     * Recherche les préinscrits par nom
     * @param nom le nom recherché
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findByNom(String nom);

    /**
     * Recherche les préinscrits par prénom
     * @param prenom le prénom recherché
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findByPrenom(String prenom);

    /**
     * Recherche les préinscrits par nom et prénom
     * @param nom le nom recherché
     * @param prenom le prénom recherché
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findByNomAndPrenom(String nom, String prenom);

    /**
     * Recherche les préinscrits par état
     * @param etat l'état recherché
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findByEtat(String etat);

    /**
     * Recherche les préinscrits par ethnie
     * @param ethnie l'ethnie recherchée
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findByEthnie(String ethnie);

    /**
     * Recherche les préinscrits par sexe
     * @param sexe le sexe recherché
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findBySexe(String sexe);

    /**
     * Recherche les préinscrits par date de rendez-vous
     * @param rdvDate la date de rendez-vous recherchée
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findByRdvDate(Date rdvDate);

    /**
     * Recherche les préinscrits par phototype
     * @param phototype le phototype recherché
     * @return la liste des préinscrits correspondants
     */
    List<Preinscrit> findByPhototype(String phototype);

    /**
     * Vérifie si un préinscrit existe avec cet email
     * @param email l'email à vérifier
     * @return true si un préinscrit avec cet email existe, false sinon
     */
    boolean existsByEmail(String email);
}