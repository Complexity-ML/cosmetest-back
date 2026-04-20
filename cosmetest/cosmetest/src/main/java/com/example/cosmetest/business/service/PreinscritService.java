package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.PreinscritDTO;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Preinscrit
 */
public interface PreinscritService {

    /**
     * Récupère tous les préinscrits
     *
     * @return la liste de tous les préinscrits
     */
    List<PreinscritDTO> getAllPreinscrits();

    /**
     * Récupère un préinscrit par son identifiant
     *
     * @param id l'identifiant du préinscrit
     * @return le préinscrit correspondant, s'il existe
     */
    Optional<PreinscritDTO> getPreinscritById(Integer id);

    /**
     * Récupère un préinscrit par son email
     *
     * @param email l'email du préinscrit
     * @return le préinscrit correspondant, s'il existe
     */
    Optional<PreinscritDTO> getPreinscritByEmail(String email);

    /**
     * Récupère les préinscrits par nom
     *
     * @param nom le nom recherché
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsByNom(String nom);

    /**
     * Récupère les préinscrits par prénom
     *
     * @param prenom le prénom recherché
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsByPrenom(String prenom);

    /**
     * Récupère les préinscrits par nom et prénom
     *
     * @param nom le nom recherché
     * @param prenom le prénom recherché
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsByNomAndPrenom(String nom, String prenom);

    /**
     * Récupère les préinscrits par état
     *
     * @param etat l'état recherché
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsByEtat(String etat);

    /**
     * Récupère les préinscrits par ethnie
     *
     * @param ethnie l'ethnie recherchée
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsByEthnie(String ethnie);

    /**
     * Récupère les préinscrits par sexe
     *
     * @param sexe le sexe recherché
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsBySexe(String sexe);

    /**
     * Récupère les préinscrits par date de rendez-vous
     *
     * @param rdvDate la date de rendez-vous recherchée
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsByRdvDate(Date rdvDate);

    /**
     * Récupère les préinscrits par phototype
     *
     * @param phototype le phototype recherché
     * @return la liste des préinscrits correspondants
     */
    List<PreinscritDTO> getPreinscritsByPhototype(String phototype);

    /**
     * Crée un nouveau préinscrit
     *
     * @param preinscritDTO les données du préinscrit à créer
     * @return le préinscrit créé avec son identifiant généré
     */
    PreinscritDTO createPreinscrit(PreinscritDTO preinscritDTO);

    /**
     * Met à jour un préinscrit existant
     *
     * @param id l'identifiant du préinscrit à mettre à jour
     * @param preinscritDTO les nouvelles données du préinscrit
     * @return le préinscrit mis à jour
     */
    Optional<PreinscritDTO> updatePreinscrit(Integer id, PreinscritDTO preinscritDTO);

    /**
     * Supprime un préinscrit par son identifiant
     *
     * @param id l'identifiant du préinscrit à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean deletePreinscrit(Integer id);

    /**
     * Vérifie si un préinscrit existe par son identifiant
     *
     * @param id l'identifiant à vérifier
     * @return true si le préinscrit existe, false sinon
     */
    boolean existsById(Integer id);

    /**
     * Vérifie si un préinscrit existe avec cet email
     *
     * @param email l'email à vérifier
     * @return true si un préinscrit avec cet email existe, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Met à jour l'état d'un préinscrit
     *
     * @param id l'identifiant du préinscrit
     * @param etat le nouvel état
     * @return le préinscrit mis à jour
     */
    Optional<PreinscritDTO> updateEtat(Integer id, String etat);

    /**
     * Planifie un rendez-vous pour un préinscrit
     *
     * @param id l'identifiant du préinscrit
     * @param rdvDate la date du rendez-vous
     * @param rdvHeure l'heure du rendez-vous
     * @return le préinscrit mis à jour
     */
    Optional<PreinscritDTO> planifierRendezVous(Integer id, Date rdvDate, String rdvHeure);

    int countPreinscrits();

    int countNewPreinscritsToday();
}