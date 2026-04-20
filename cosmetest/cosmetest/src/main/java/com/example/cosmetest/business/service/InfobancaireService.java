package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.InfobancaireDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface pour les opérations métier sur l'entité Infobancaire
 */
public interface InfobancaireService {

    /**
     * Récupère toutes les informations bancaires
     *
     * @return la liste de toutes les informations bancaires
     */
    List<InfobancaireDTO> getAllInfobancaires();

    /**
     * Récupère une information bancaire par son identifiant composite
     *
     * @param bic   le code BIC
     * @param iban  le numéro IBAN
     * @param idVol l'identifiant du volontaire
     * @return l'information bancaire correspondante, si elle existe
     */
    Optional<InfobancaireDTO> getInfobancaireById(String bic, String iban, Integer idVol);

    /**
     * Récupère toutes les informations bancaires associées à un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return la liste des informations bancaires associées
     */
    List<InfobancaireDTO> getInfobancairesByIdVol(Integer idVol);

    /**
     * Récupère les informations bancaires par BIC et IBAN
     *
     * @param bic  le code BIC
     * @param iban le numéro IBAN
     * @return la liste des informations bancaires correspondantes
     */
    List<InfobancaireDTO> getInfobancairesByBicAndIban(String bic, String iban);

    /**
     * Crée une nouvelle information bancaire
     *
     * @param infobancaireDTO les données de l'information bancaire à créer
     * @return l'information bancaire créée
     */
    InfobancaireDTO createInfobancaire(InfobancaireDTO infobancaireDTO);

    /**
     * Met à jour une information bancaire existante
     *
     * @param bic             le code BIC actuel
     * @param iban            le numéro IBAN actuel
     * @param idVol           l'identifiant du volontaire actuel
     * @param infobancaireDTO les nouvelles données de l'information bancaire
     * @return l'information bancaire mise à jour
     */
    Optional<InfobancaireDTO> updateInfobancaire(String bic, String iban, Integer idVol,
            InfobancaireDTO infobancaireDTO);

    /**
     * Supprime une information bancaire par son identifiant composite
     *
     * @param bic   le code BIC
     * @param iban  le numéro IBAN
     * @param idVol l'identifiant du volontaire
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteInfobancaire(String bic, String iban, Integer idVol);

    /**
     * Vérifie si une information bancaire existe par son identifiant composite
     *
     * @param bic   le code BIC
     * @param iban  le numéro IBAN
     * @param idVol l'identifiant du volontaire
     * @return true si l'information bancaire existe, false sinon
     */
    boolean existsById(String bic, String iban, Integer idVol);

    /**
     * Vérifie si des informations bancaires existent pour un volontaire
     *
     * @param idVol l'identifiant du volontaire
     * @return true si des informations existent, false sinon
     */
    boolean existsByIdVol(Integer idVol);

    /**
     * Récupère les informations bancaires par IBAN
     *
     * @param iban le numéro IBAN
     * @return la liste des informations bancaires correspondantes
     */
    List<InfobancaireDTO> getInfobancairesByIban(String iban);

    /**
     * Récupère les informations bancaires par BIC
     *
     * @param bic le code BIC
     * @return la liste des informations bancaires correspondantes
     */
    List<InfobancaireDTO> getInfobancairesByBic(String bic);
}