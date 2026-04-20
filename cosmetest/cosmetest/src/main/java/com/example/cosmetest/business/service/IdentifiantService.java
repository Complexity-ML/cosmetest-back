package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.IdentifiantDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Identifiant
 */
public interface IdentifiantService {

    /**
     * Récupère tous les identifiants (sans leurs mots de passe)
     *
     * @return la liste de tous les identifiants
     */
    List<IdentifiantDTO> getAllIdentifiants();

    /**
     * Récupère un identifiant par son ID (sans mot de passe)
     *
     * @param id l'identifiant de l'identifiant
     * @return l'identifiant correspondant, s'il existe
     */
    Optional<IdentifiantDTO> getIdentifiantById(Integer id);

    /**
     * Récupère un identifiant par son login (sans mot de passe)
     *
     * @param login le login de l'identifiant
     * @return l'identifiant correspondant, s'il existe
     */
    Optional<IdentifiantDTO> getIdentifiantByLogin(String login);

    /**
     * Récupère un identifiant par son email (sans mot de passe)
     *
     * @param email l'email de l'identifiant
     * @return l'identifiant correspondant, s'il existe
     */
    Optional<IdentifiantDTO> getIdentifiantByEmail(String email);

    /**
     * Crée un nouvel identifiant
     *
     * @param identifiantDTO les données de l'identifiant à créer
     * @return l'identifiant créé avec son identifiant généré (sans mot de passe)
     */
    IdentifiantDTO createIdentifiant(IdentifiantDTO identifiantDTO);

    /**
     * Met à jour un identifiant existant
     *
     * @param id l'identifiant de l'identifiant à mettre à jour
     * @param identifiantDTO les nouvelles données de l'identifiant
     * @return l'identifiant mis à jour (sans mot de passe)
     */
    Optional<IdentifiantDTO> updateIdentifiant(Integer id, IdentifiantDTO identifiantDTO);

    /**
     * Supprime un identifiant par son identifiant
     *
     * @param id l'identifiant de l'identifiant à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteIdentifiant(Integer id);

    /**
     * Change le mot de passe d'un identifiant
     *
     * @param id l'identifiant de l'identifiant
     * @param ancienMdp l'ancien mot de passe (pour vérification)
     * @param nouveauMdp le nouveau mot de passe
     * @return true si le changement a réussi, false sinon
     */
    boolean changerMotDePasse(Integer id, String ancienMdp, String nouveauMdp);

    /**
     * Récupère tous les identifiants ayant un rôle spécifique
     *
     * @param role le rôle recherché
     * @return la liste des identifiants ayant ce rôle
     */
    List<IdentifiantDTO> getIdentifiantsByRole(String role);

    /**
     * Vérifie si un login existe déjà
     *
     * @param login le login à vérifier
     * @return true si le login existe déjà, false sinon
     */
    boolean loginExiste(String login);

    /**
     * Vérifie si un email existe déjà
     *
     * @param email l'email à vérifier
     * @return true si l'email existe déjà, false sinon
     */
    boolean emailExiste(String email);

    /**
     * Authentifie un utilisateur
     *
     * @param login le login
     * @param motDePasse le mot de passe
     * @return l'identifiant correspondant s'il est authentifié, vide sinon
     */
    Optional<IdentifiantDTO> authentifier(String login, String motDePasse);
}