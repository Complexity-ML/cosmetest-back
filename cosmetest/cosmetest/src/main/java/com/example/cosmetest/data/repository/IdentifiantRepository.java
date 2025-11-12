package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Identifiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentifiantRepository extends JpaRepository<Identifiant, Integer> {

    /**
     * Trouve un identifiant par son nom d'utilisateur (login)
     * @param identifiant le nom d'utilisateur
     * @return l'identifiant correspondant, s'il existe
     */
    Optional<Identifiant> findByIdentifiant(String identifiant);

    /**
     * Trouve un identifiant par son adresse email
     * @param mailIdentifiant l'adresse email
     * @return l'identifiant correspondant, s'il existe
     */
    Optional<Identifiant> findByMailIdentifiant(String mailIdentifiant);

    /**
     * Vérifie si un identifiant existe avec ce nom d'utilisateur
     * @param identifiant le nom d'utilisateur à vérifier
     * @return true si l'identifiant existe, false sinon
     */
    boolean existsByIdentifiant(String identifiant);

    /**
     * Vérifie si un identifiant existe avec cette adresse email
     * @param mailIdentifiant l'adresse email à vérifier
     * @return true si l'identifiant existe, false sinon
     */
    boolean existsByMailIdentifiant(String mailIdentifiant);

    /**
     * Trouve tous les identifiants ayant un rôle spécifique
     * @param role le rôle recherché
     * @return la liste des identifiants avec ce rôle
     */
    java.util.List<Identifiant> findByRole(String role);
}