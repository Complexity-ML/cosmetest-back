package com.example.cosmetest.data.repository;

import com.example.cosmetest.domain.model.PanelHc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'accès aux données de l'entité PanelHc
 */
@Repository
public interface PanelHcRepository extends JpaRepository<PanelHc, Integer> {

    /**
     * Trouve les panels où les produits bio sont utilisés
     * @return la liste des panels avec produits bio
     */
    List<PanelHc> findByProduitsBio(String valeur);

    /**
     * Trouve les panels par type d'achat
     * @param lieu le lieu d'achat
     * @return la liste des panels correspondants
     */
    @Query("SELECT p FROM PanelHc p WHERE " +
            "p.achatInstitutParfumerie = :lieu OR " +
            "p.achatGrandesSurfaces = :lieu OR " +
            "p.achatPharmacieParapharmacie = :lieu OR " +
            "p.achatInternet = :lieu")
    List<PanelHc> findByLieuAchat(String lieu);

    /**
     * Trouve les panels par méthode d'épilation
     * @param methode la méthode d'épilation
     * @return la liste des panels correspondants
     */
    @Query("SELECT p FROM PanelHc p WHERE " +
            "p.cire = :methode OR " +
            "p.cremeDepilatoire = :methode OR " +
            "p.epilateurElectrique = :methode OR " +
            "p.rasoir = :methode OR " +
            "p.institut = :methode OR " +
            "p.epilationDefinitive = :methode")
    List<PanelHc> findByMethodeEpilation(String methode);

    /**
     * Trouve les panels qui utilisent un type de maquillage spécifique
     * @param maquillage le type de maquillage
     * @return la liste des panels correspondants
     */
    @Query("SELECT p FROM PanelHc p WHERE " +
            "p.cremeTeintee = :maquillage OR " +
            "p.baseMaquillage = :maquillage OR " +
            "p.correcteurTeint = :maquillage OR " +
            "p.anticerne = :maquillage OR " +
            "p.fondDeTeint = :maquillage OR " +
            "p.poudreLibre = :maquillage OR " +
            "p.blushFardAJoues = :maquillage OR " +
            "p.mascara = :maquillage OR " +
            "p.mascaraWaterproof = :maquillage OR " +
            "p.fardAPaupieres = :maquillage OR " +
            "p.crayonsYeux = :maquillage OR " +
            "p.eyeliner = :maquillage OR " +
            "p.fauxCils = :maquillage OR " +
            "p.maquillageDesSourcils = :maquillage OR " +
            "p.rougeALevres = :maquillage OR " +
            "p.gloss = :maquillage OR " +
            "p.crayonLevres = :maquillage")
    List<PanelHc> findByTypeMaquillage(String maquillage);

    /**
     * Trouve les panels qui utilisent un type de soin visage spécifique
     * @param soin le type de soin
     * @return la liste des panels correspondants
     */
    @Query("SELECT p FROM PanelHc p WHERE " +
            "p.soinHydratantVisage = :soin OR " +
            "p.soinNourissantVisage = :soin OR " +
            "p.soinAntiRidesVisage = :soin OR " +
            "p.soinAntiAgeVisage = :soin OR " +
            "p.soinRaffermissantVisage = :soin OR " +
            "p.gommageVisage = :soin OR " +
            "p.masqueVisage = :soin OR " +
            "p.soinAntiTachesVisage = :soin OR " +
            "p.soinAntiRougeursVisage = :soin OR " +
            "p.soinMatifiantVisage = :soin OR " +
            "p.soinEclatDuTeint = :soin OR " +
            "p.soinContourDesYeux = :soin OR " +
            "p.soinContourDesLevres = :soin")
    List<PanelHc> findBySoinVisage(String soin);

    /**
     * Trouve les panels qui utilisent un type de soin corps spécifique
     * @param soin le type de soin
     * @return la liste des panels correspondants
     */
    @Query("SELECT p FROM PanelHc p WHERE " +
            "p.soinHydratantCorps = :soin OR " +
            "p.soinNourrissantCorps = :soin OR " +
            "p.soinAntiAgeCorps = :soin OR " +
            "p.soinRaffermissantCorps = :soin OR " +
            "p.gommageCorps = :soin OR " +
            "p.masqueCorps = :soin OR " +
            "p.soinAntiTachesDecollete = :soin OR " +
            "p.soinAmincissant = :soin OR " +
            "p.soinAntiCellulite = :soin OR " +
            "p.soinAntiVergetures = :soin")
    List<PanelHc> findBySoinCorps(String soin);

    /**
     * Trouve les panels qui utilisent des produits capillaires spécifiques
     * @param produit le type de produit capillaire
     * @return la liste des panels correspondants
     */
    @Query("SELECT p FROM PanelHc p WHERE " +
            "p.shampoing = :produit OR " +
            "p.apresShampoing = :produit OR " +
            "p.masqueCapillaire = :produit OR " +
            "p.colorationMeches = :produit OR " +
            "p.produitCoiffantFixant = :produit OR " +
            "p.permanente = :produit OR " +
            "p.lissageDefrisage = :produit OR " +
            "p.extensionsCapillaires = :produit")
    List<PanelHc> findByProduitCapillaire(String produit);
}