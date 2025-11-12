package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Volbug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'accès aux données de l'entité Volbug
 */
@Repository
public interface VolbugRepository extends JpaRepository<Volbug, Integer> {

    /**
     * Vérifie si un bug existe pour un volontaire spécifique
     * @param idVol l'identifiant du volontaire
     * @return true si un bug existe, false sinon
     */
    boolean existsByIdVol(int idVol);
}