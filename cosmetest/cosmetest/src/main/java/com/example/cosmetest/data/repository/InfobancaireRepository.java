package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.Infobancaire;
import com.example.cosmetest.domain.model.InfobancaireId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'accès aux données de l'entité Infobancaire
 */
@Repository
public interface InfobancaireRepository extends JpaRepository<Infobancaire, InfobancaireId> {

    /**
     * Trouve toutes les informations bancaires associées à un volontaire
     * @param idVol l'identifiant du volontaire
     * @return la liste des informations bancaires
     */
    List<Infobancaire> findByIdIdVol(Integer idVol);

    /**
     * Trouve une information bancaire par BIC et IBAN
     * @param bic le code BIC
     * @param iban le numéro IBAN
     * @return la liste des informations bancaires
     */
    List<Infobancaire> findByIdBicAndIdIban(String bic, String iban);

    /**
     * Vérifie si une information bancaire existe pour un volontaire spécifique
     * @param idVol l'identifiant du volontaire
     * @return true si l'information existe, false sinon
     */
    boolean existsByIdIdVol(Integer idVol);

    /**
     * Vérifie si une information bancaire existe avec un IBAN spécifique
     * @param iban le numéro IBAN
     * @return true si l'information existe, false sinon
     */
    boolean existsByIdIban(String iban);

    /**
     * Trouve une information bancaire par IBAN
     * @param iban le numéro IBAN
     * @return la liste des informations bancaires
     */
    List<Infobancaire> findByIdIban(String iban);

    /**
     * Trouve une information bancaire par BIC
     * @param bic le code BIC
     * @return la liste des informations bancaires
     */
    List<Infobancaire> findByIdBic(String bic);
}