package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Integer> {
    /**
     * Trouve tous les groupes associés à une étude spécifique
     * @param idEtude l'identifiant de l'étude
     * @return la liste des groupes associés à cette étude
     */
    List<Groupe> findByIdEtude(int idEtude);

    /**
     * Trouve tous les groupes avec un âge minimum spécifié
     * @param ageMinimum l'âge minimum recherché
     * @return la liste des groupes correspondants
     */
    List<Groupe> findByAgeMinimum(int ageMinimum);

    /**
     * Trouve tous les groupes avec un âge maximum spécifié
     * @param ageMaximum l'âge maximum recherché
     * @return la liste des groupes correspondants
     */
    List<Groupe> findByAgeMaximum(int ageMaximum);

    /**
     * Trouve tous les groupes d'une ethnie spécifique
     * @param ethnie l'ethnie recherchée
     * @return la liste des groupes correspondants
     */
    List<Groupe> findByEthnie(String ethnie);
}