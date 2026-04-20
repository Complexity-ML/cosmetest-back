package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO (Data Transfer Object) pour l'entité VolontaireHc (Habitudes de
 * Consommation)
 */
public class VolontaireHcDTO {

    @NotNull(message = "L'ID du volontaire ne peut pas être null")
    @Positive(message = "L'ID du volontaire doit être un nombre positif")
    private Integer idVol;

    // Lieux d'achat
    private String achatGrandesSurfaces;
    private String achatInstitutParfumerie;
    private String achatInternet;
    private String achatPharmacieParapharmacie;

    // Produits d'hygiène et soins
    private String antiTranspirant;
    private String apresRasage;
    private String apresShampoing;
    private String autobronzant;
    private String cire;
    private String cremeDepilatoire;
    private String deodorant;
    private String dissolvantOngles;
    private String eauDeToilette;
    private String epilateurElectrique;
    private String epilationDefinitive;
    private String extensionsCapillaires;
    private String gelARaser;
    private String gelDouche;
    private String gelNettoyant;
    private String gommageCorps;
    private String gommageVisage;
    private String institut;
    private String laitDouche;
    private String lissageDefrisage;
    private String lotionMicellaire;
    private String manucures;
    private String masqueCapillaire;
    private String masqueCorps;
    private String masqueVisage;
    private String mousseARaser;
    private String nettoyantIntime;
    private String ombreBarbe;
    private String parfum;
    private String permanente;
    private String produitCoiffantFixant;
    private String produitsBain;
    private String produitsBio;
    private String protecteurSolaireCorps;
    private String protecteurSolaireLevres;
    private String protecteurSolaireVisage;
    private String rasoir;
    private String rasoirElectrique;
    private String rasoirMecanique;
    private String savon;
    private String shampoing;
    private String tondeuseBarbe;
    private String tonique;

    // Produits de maquillage
    private String anticerne;
    private String baseMaquillage;
    private String blushFardAJoues;
    private String correcteurTeint;
    private String crayonLevres;
    private String crayonsYeux;
    private String cremeTeintee;
    private String demaquillantVisage;
    private String demaquillantWaterproof;
    private String demaquillantYeux;
    private String eyeliner;
    private String fardAPaupieres;
    private String fauxCils;
    private String fauxOngles;
    private String fondDeTeint;
    private String gloss;
    private String maquillageDesSourcils;
    private String maquillagePermanentLevres;
    private String maquillagePermanentSourcils;
    private String maquillagePermanentYeux;
    private String mascara;
    private String mascaraWaterproof;
    private String poudreLibre;
    private String rougeALevres;
    private String vernisAOngles;

    // Produits de soin
    private String colorationMeches;
    private String soinAmincissant;
    private String soinAntiAgeCorps;
    private String soinAntiAgeMains;
    private String soinAntiAgeVisage;
    private String soinAntiCellulite;
    private String soinAntiRidesVisage;
    private String soinAntiRougeursVisage;
    private String soinAntiTachesDecollete;
    private String soinAntiTachesMains;
    private String soinAntiTachesVisage;
    private String soinAntiVergetures;
    private String soinApresSoleil;
    private String soinContourDesLevres;
    private String soinContourDesYeux;
    private String soinEclatDuTeint;
    private String soinHydratantCorps;
    private String soinHydratantMains;
    private String soinHydratantVisage;
    private String soinMatifiantVisage;
    private String soinNourissantVisage;
    private String soinNourrissantCorps;
    private String soinNourrissantMains;
    private String soinOngles;
    private String soinPieds;
    private String soinRaffermissantCorps;
    private String soinRaffermissantVisage;

    // Constructeurs
    public VolontaireHcDTO() {
    }

    public VolontaireHcDTO(Integer idVol) {
        this.idVol = idVol;
    }

    // Getter et Setter pour idVol
    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    // Getters et Setters pour tous les attributs
    public String getAchatGrandesSurfaces() {
        return normalizeValue(achatGrandesSurfaces);
    }

    public void setAchatGrandesSurfaces(String achatGrandesSurfaces) {
        this.achatGrandesSurfaces = achatGrandesSurfaces;
    }

    public String getAchatInstitutParfumerie() {
        return normalizeValue(achatInstitutParfumerie);
    }

    public void setAchatInstitutParfumerie(String achatInstitutParfumerie) {
        this.achatInstitutParfumerie = achatInstitutParfumerie;
    }

    public String getAchatInternet() {
        return normalizeValue(achatInternet);
    }

    public void setAchatInternet(String achatInternet) {
        this.achatInternet = achatInternet;
    }

    public String getAchatPharmacieParapharmacie() {
        return normalizeValue(achatPharmacieParapharmacie);
    }

    public void setAchatPharmacieParapharmacie(String achatPharmacieParapharmacie) {
        this.achatPharmacieParapharmacie = achatPharmacieParapharmacie;
    }

    public String getAntiTranspirant() {
        return normalizeValue(antiTranspirant);
    }

    public void setAntiTranspirant(String antiTranspirant) {
        this.antiTranspirant = antiTranspirant;
    }

    public String getAnticerne() {
        return normalizeValue(anticerne);
    }

    public void setAnticerne(String anticerne) {
        this.anticerne = anticerne;
    }

    public String getApresRasage() {
        return normalizeValue(apresRasage);
    }

    public void setApresRasage(String apresRasage) {
        this.apresRasage = apresRasage;
    }

    public String getApresShampoing() {
        return normalizeValue(apresShampoing);
    }

    public void setApresShampoing(String apresShampoing) {
        this.apresShampoing = apresShampoing;
    }

    public String getAutobronzant() {
        return normalizeValue(autobronzant);
    }

    public void setAutobronzant(String autobronzant) {
        this.autobronzant = autobronzant;
    }

    public String getBaseMaquillage() {
        return normalizeValue(baseMaquillage);
    }

    public void setBaseMaquillage(String baseMaquillage) {
        this.baseMaquillage = baseMaquillage;
    }

    public String getBlushFardAJoues() {
        return normalizeValue(blushFardAJoues);
    }

    public void setBlushFardAJoues(String blushFardAJoues) {
        this.blushFardAJoues = blushFardAJoues;
    }

    public String getCire() {
        return normalizeValue(cire);
    }

    public void setCire(String cire) {
        this.cire = cire;
    }

    public String getColorationMeches() {
        return normalizeValue(colorationMeches);
    }

    public void setColorationMeches(String colorationMeches) {
        this.colorationMeches = colorationMeches;
    }

    public String getCorrecteurTeint() {
        return normalizeValue(correcteurTeint);
    }

    public void setCorrecteurTeint(String correcteurTeint) {
        this.correcteurTeint = correcteurTeint;
    }

    public String getCrayonLevres() {
        return normalizeValue(crayonLevres);
    }

    public void setCrayonLevres(String crayonLevres) {
        this.crayonLevres = crayonLevres;
    }

    public String getCrayonsYeux() {
        return normalizeValue(crayonsYeux);
    }

    public void setCrayonsYeux(String crayonsYeux) {
        this.crayonsYeux = crayonsYeux;
    }

    public String getCremeDepilatoire() {
        return normalizeValue(cremeDepilatoire);
    }

    public void setCremeDepilatoire(String cremeDepilatoire) {
        this.cremeDepilatoire = cremeDepilatoire;
    }

    public String getCremeTeintee() {
        return normalizeValue(cremeTeintee);
    }

    public void setCremeTeintee(String cremeTeintee) {
        this.cremeTeintee = cremeTeintee;
    }

    public String getDemaquillantVisage() {
        return normalizeValue(demaquillantVisage);
    }

    public void setDemaquillantVisage(String demaquillantVisage) {
        this.demaquillantVisage = demaquillantVisage;
    }

    public String getDemaquillantWaterproof() {
        return normalizeValue(demaquillantWaterproof);
    }

    public void setDemaquillantWaterproof(String demaquillantWaterproof) {
        this.demaquillantWaterproof = demaquillantWaterproof;
    }

    public String getDemaquillantYeux() {
        return normalizeValue(demaquillantYeux);
    }

    public void setDemaquillantYeux(String demaquillantYeux) {
        this.demaquillantYeux = demaquillantYeux;
    }

    public String getDeodorant() {
        return normalizeValue(deodorant);
    }

    public void setDeodorant(String deodorant) {
        this.deodorant = deodorant;
    }

    public String getDissolvantOngles() {
        return normalizeValue(dissolvantOngles);
    }

    public void setDissolvantOngles(String dissolvantOngles) {
        this.dissolvantOngles = dissolvantOngles;
    }

    public String getEauDeToilette() {
        return normalizeValue(eauDeToilette);
    }

    public void setEauDeToilette(String eauDeToilette) {
        this.eauDeToilette = eauDeToilette;
    }

    public String getEpilateurElectrique() {
        return normalizeValue(epilateurElectrique);
    }

    public void setEpilateurElectrique(String epilateurElectrique) {
        this.epilateurElectrique = epilateurElectrique;
    }

    public String getEpilationDefinitive() {
        return normalizeValue(epilationDefinitive);
    }

    public void setEpilationDefinitive(String epilationDefinitive) {
        this.epilationDefinitive = epilationDefinitive;
    }

    public String getExtensionsCapillaires() {
        return normalizeValue(extensionsCapillaires);
    }

    public void setExtensionsCapillaires(String extensionsCapillaires) {
        this.extensionsCapillaires = extensionsCapillaires;
    }

    public String getEyeliner() {
        return normalizeValue(eyeliner);
    }

    public void setEyeliner(String eyeliner) {
        this.eyeliner = eyeliner;
    }

    public String getFardAPaupieres() {
        return normalizeValue(fardAPaupieres);
    }

    public void setFardAPaupieres(String fardAPaupieres) {
        this.fardAPaupieres = fardAPaupieres;
    }

    public String getFauxCils() {
        return normalizeValue(fauxCils);
    }

    public void setFauxCils(String fauxCils) {
        this.fauxCils = fauxCils;
    }

    public String getFauxOngles() {
        return normalizeValue(fauxOngles);
    }

    public void setFauxOngles(String fauxOngles) {
        this.fauxOngles = fauxOngles;
    }

    public String getFondDeTeint() {
        return normalizeValue(fondDeTeint);
    }

    public void setFondDeTeint(String fondDeTeint) {
        this.fondDeTeint = fondDeTeint;
    }

    public String getGelARaser() {
        return normalizeValue(gelARaser);
    }

    public void setGelARaser(String gelARaser) {
        this.gelARaser = gelARaser;
    }

    public String getGelDouche() {
        return normalizeValue(gelDouche);
    }

    public void setGelDouche(String gelDouche) {
        this.gelDouche = gelDouche;
    }

    public String getGelNettoyant() {
        return normalizeValue(gelNettoyant);
    }

    public void setGelNettoyant(String gelNettoyant) {
        this.gelNettoyant = gelNettoyant;
    }

    public String getGloss() {
        return normalizeValue(gloss);
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }

    public String getGommageCorps() {
        return normalizeValue(gommageCorps);
    }

    public void setGommageCorps(String gommageCorps) {
        this.gommageCorps = gommageCorps;
    }

    public String getGommageVisage() {
        return normalizeValue(gommageVisage);
    }

    public void setGommageVisage(String gommageVisage) {
        this.gommageVisage = gommageVisage;
    }

    public String getInstitut() {
        return normalizeValue(institut);
    }

    public void setInstitut(String institut) {
        this.institut = institut;
    }

    public String getLaitDouche() {
        return normalizeValue(laitDouche);
    }

    public void setLaitDouche(String laitDouche) {
        this.laitDouche = laitDouche;
    }

    public String getLissageDefrisage() {
        return normalizeValue(lissageDefrisage);
    }

    public void setLissageDefrisage(String lissageDefrisage) {
        this.lissageDefrisage = lissageDefrisage;
    }

    public String getLotionMicellaire() {
        return normalizeValue(lotionMicellaire);
    }

    public void setLotionMicellaire(String lotionMicellaire) {
        this.lotionMicellaire = lotionMicellaire;
    }

    public String getManucures() {
        return normalizeValue(manucures);
    }

    public void setManucures(String manucures) {
        this.manucures = manucures;
    }

    public String getMaquillageDesSourcils() {
        return normalizeValue(maquillageDesSourcils);
    }

    public void setMaquillageDesSourcils(String maquillageDesSourcils) {
        this.maquillageDesSourcils = maquillageDesSourcils;
    }

    public String getMaquillagePermanentLevres() {
        return normalizeValue(maquillagePermanentLevres);
    }

    public void setMaquillagePermanentLevres(String maquillagePermanentLevres) {
        this.maquillagePermanentLevres = maquillagePermanentLevres;
    }

    public String getMaquillagePermanentSourcils() {
        return normalizeValue(maquillagePermanentSourcils);
    }

    public void setMaquillagePermanentSourcils(String maquillagePermanentSourcils) {
        this.maquillagePermanentSourcils = maquillagePermanentSourcils;
    }

    public String getMaquillagePermanentYeux() {
        return normalizeValue(maquillagePermanentYeux);
    }

    public void setMaquillagePermanentYeux(String maquillagePermanentYeux) {
        this.maquillagePermanentYeux = maquillagePermanentYeux;
    }

    public String getMascara() {
        return normalizeValue(mascara);
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getMascaraWaterproof() {
        return normalizeValue(mascaraWaterproof);
    }

    public void setMascaraWaterproof(String mascaraWaterproof) {
        this.mascaraWaterproof = mascaraWaterproof;
    }

    public String getMasqueCapillaire() {
        return normalizeValue(masqueCapillaire);
    }

    public void setMasqueCapillaire(String masqueCapillaire) {
        this.masqueCapillaire = masqueCapillaire;
    }

    public String getMasqueCorps() {
        return normalizeValue(masqueCorps);
    }

    public void setMasqueCorps(String masqueCorps) {
        this.masqueCorps = masqueCorps;
    }

    public String getMasqueVisage() {
        return normalizeValue(masqueVisage);
    }

    public void setMasqueVisage(String masqueVisage) {
        this.masqueVisage = masqueVisage;
    }

    public String getMousseARaser() {
        return normalizeValue(mousseARaser);
    }

    public void setMousseARaser(String mousseARaser) {
        this.mousseARaser = mousseARaser;
    }

    public String getNettoyantIntime() {
        return normalizeValue(nettoyantIntime);
    }

    public void setNettoyantIntime(String nettoyantIntime) {
        this.nettoyantIntime = nettoyantIntime;
    }

    public String getOmbreBarbe() {
        return normalizeValue(ombreBarbe);
    }

    public void setOmbreBarbe(String ombreBarbe) {
        this.ombreBarbe = ombreBarbe;
    }

    public String getParfum() {
        return normalizeValue(parfum);
    }

    public void setParfum(String parfum) {
        this.parfum = parfum;
    }

    public String getPermanente() {
        return normalizeValue(permanente);
    }

    public void setPermanente(String permanente) {
        this.permanente = permanente;
    }

    public String getPoudreLibre() {
        return normalizeValue(poudreLibre);
    }

    public void setPoudreLibre(String poudreLibre) {
        this.poudreLibre = poudreLibre;
    }

    public String getProduitCoiffantFixant() {
        return normalizeValue(produitCoiffantFixant);
    }

    public void setProduitCoiffantFixant(String produitCoiffantFixant) {
        this.produitCoiffantFixant = produitCoiffantFixant;
    }

    public String getProduitsBain() {
        return normalizeValue(produitsBain);
    }

    public void setProduitsBain(String produitsBain) {
        this.produitsBain = produitsBain;
    }

    public String getProduitsBio() {
        return normalizeValue(produitsBio);
    }

    public void setProduitsBio(String produitsBio) {
        this.produitsBio = produitsBio;
    }

    public String getProtecteurSolaireCorps() {
        return normalizeValue(protecteurSolaireCorps);
    }

    public void setProtecteurSolaireCorps(String protecteurSolaireCorps) {
        this.protecteurSolaireCorps = protecteurSolaireCorps;
    }

    public String getProtecteurSolaireLevres() {
        return normalizeValue(protecteurSolaireLevres);
    }

    public void setProtecteurSolaireLevres(String protecteurSolaireLevres) {
        this.protecteurSolaireLevres = protecteurSolaireLevres;
    }

    public String getProtecteurSolaireVisage() {
        return normalizeValue(protecteurSolaireVisage);
    }

    public void setProtecteurSolaireVisage(String protecteurSolaireVisage) {
        this.protecteurSolaireVisage = protecteurSolaireVisage;
    }

    public String getRasoir() {
        return normalizeValue(rasoir);
    }

    public void setRasoir(String rasoir) {
        this.rasoir = rasoir;
    }

    public String getRasoirElectrique() {
        return normalizeValue(rasoirElectrique);
    }

    public void setRasoirElectrique(String rasoirElectrique) {
        this.rasoirElectrique = rasoirElectrique;
    }

    public String getRasoirMecanique() {
        return normalizeValue(rasoirMecanique);
    }

    public void setRasoirMecanique(String rasoirMecanique) {
        this.rasoirMecanique = rasoirMecanique;
    }

    public String getRougeALevres() {
        return normalizeValue(rougeALevres);
    }

    public void setRougeALevres(String rougeALevres) {
        this.rougeALevres = rougeALevres;
    }

    public String getSavon() {
        return normalizeValue(savon);
    }

    public void setSavon(String savon) {
        this.savon = savon;
    }

    public String getShampoing() {
        return normalizeValue(shampoing);
    }

    public void setShampoing(String shampoing) {
        this.shampoing = shampoing;
    }

    public String getSoinAmincissant() {
        return normalizeValue(soinAmincissant);
    }

    public void setSoinAmincissant(String soinAmincissant) {
        this.soinAmincissant = soinAmincissant;
    }

    public String getSoinAntiAgeCorps() {
        return normalizeValue(soinAntiAgeCorps);
    }

    public void setSoinAntiAgeCorps(String soinAntiAgeCorps) {
        this.soinAntiAgeCorps = soinAntiAgeCorps;
    }

    public String getSoinAntiAgeMains() {
        return normalizeValue(soinAntiAgeMains);
    }

    public void setSoinAntiAgeMains(String soinAntiAgeMains) {
        this.soinAntiAgeMains = soinAntiAgeMains;
    }

    public String getSoinAntiAgeVisage() {
        return normalizeValue(soinAntiAgeVisage);
    }

    public void setSoinAntiAgeVisage(String soinAntiAgeVisage) {
        this.soinAntiAgeVisage = soinAntiAgeVisage;
    }

    public String getSoinAntiCellulite() {
        return normalizeValue(soinAntiCellulite);
    }

    public void setSoinAntiCellulite(String soinAntiCellulite) {
        this.soinAntiCellulite = soinAntiCellulite;
    }

    public String getSoinAntiRidesVisage() {
        return normalizeValue(soinAntiRidesVisage);
    }

    public void setSoinAntiRidesVisage(String soinAntiRidesVisage) {
        this.soinAntiRidesVisage = soinAntiRidesVisage;
    }

    public String getSoinAntiRougeursVisage() {
        return normalizeValue(soinAntiRougeursVisage);
    }

    public void setSoinAntiRougeursVisage(String soinAntiRougeursVisage) {
        this.soinAntiRougeursVisage = soinAntiRougeursVisage;
    }

    public String getSoinAntiTachesDecollete() {
        return normalizeValue(soinAntiTachesDecollete);
    }

    public void setSoinAntiTachesDecollete(String soinAntiTachesDecollete) {
        this.soinAntiTachesDecollete = soinAntiTachesDecollete;
    }

    public String getSoinAntiTachesMains() {
        return normalizeValue(soinAntiTachesMains);
    }

    public void setSoinAntiTachesMains(String soinAntiTachesMains) {
        this.soinAntiTachesMains = soinAntiTachesMains;
    }

    public String getSoinAntiTachesVisage() {
        return normalizeValue(soinAntiTachesVisage);
    }

    public void setSoinAntiTachesVisage(String soinAntiTachesVisage) {
        this.soinAntiTachesVisage = soinAntiTachesVisage;
    }

    public String getSoinAntiVergetures() {
        return normalizeValue(soinAntiVergetures);
    }

    public void setSoinAntiVergetures(String soinAntiVergetures) {
        this.soinAntiVergetures = soinAntiVergetures;
    }

    public String getSoinApresSoleil() {
        return normalizeValue(soinApresSoleil);
    }

    public void setSoinApresSoleil(String soinApresSoleil) {
        this.soinApresSoleil = soinApresSoleil;
    }

    public String getSoinContourDesLevres() {
        return normalizeValue(soinContourDesLevres);
    }

    public void setSoinContourDesLevres(String soinContourDesLevres) {
        this.soinContourDesLevres = soinContourDesLevres;
    }

    public String getSoinContourDesYeux() {
        return normalizeValue(soinContourDesYeux);
    }

    public void setSoinContourDesYeux(String soinContourDesYeux) {
        this.soinContourDesYeux = soinContourDesYeux;
    }

    public String getSoinEclatDuTeint() {
        return normalizeValue(soinEclatDuTeint);
    }

    public void setSoinEclatDuTeint(String soinEclatDuTeint) {
        this.soinEclatDuTeint = soinEclatDuTeint;
    }

    public String getSoinHydratantCorps() {
        return normalizeValue(soinHydratantCorps);
    }

    public void setSoinHydratantCorps(String soinHydratantCorps) {
        this.soinHydratantCorps = soinHydratantCorps;
    }

    public String getSoinHydratantMains() {
        return normalizeValue(soinHydratantMains);
    }

    public void setSoinHydratantMains(String soinHydratantMains) {
        this.soinHydratantMains = soinHydratantMains;
    }

    public String getSoinHydratantVisage() {
        return normalizeValue(soinHydratantVisage);
    }

    public void setSoinHydratantVisage(String soinHydratantVisage) {
        this.soinHydratantVisage = soinHydratantVisage;
    }

    public String getSoinMatifiantVisage() {
        return normalizeValue(soinMatifiantVisage);
    }

    public void setSoinMatifiantVisage(String soinMatifiantVisage) {
        this.soinMatifiantVisage = soinMatifiantVisage;
    }

    public String getSoinNourissantVisage() {
        return normalizeValue(soinNourissantVisage);
    }

    public void setSoinNourissantVisage(String soinNourissantVisage) {
        this.soinNourissantVisage = soinNourissantVisage;
    }

    public String getSoinNourrissantCorps() {
        return normalizeValue(soinNourrissantCorps);
    }

    public void setSoinNourrissantCorps(String soinNourrissantCorps) {
        this.soinNourrissantCorps = soinNourrissantCorps;
    }

    public String getSoinNourrissantMains() {
        return normalizeValue(soinNourrissantMains);
    }

    public void setSoinNourrissantMains(String soinNourrissantMains) {
        this.soinNourrissantMains = soinNourrissantMains;
    }

    public String getSoinOngles() {
        return normalizeValue(soinOngles);
    }

    public void setSoinOngles(String soinOngles) {
        this.soinOngles = soinOngles;
    }

    public String getSoinPieds() {
        return normalizeValue(soinPieds);
    }

    public void setSoinPieds(String soinPieds) {
        this.soinPieds = soinPieds;
    }

    public String getSoinRaffermissantCorps() {
        return normalizeValue(soinRaffermissantCorps);
    }

    public void setSoinRaffermissantCorps(String soinRaffermissantCorps) {
        this.soinRaffermissantCorps = soinRaffermissantCorps;
    }

    public String getSoinRaffermissantVisage() {
        return normalizeValue(soinRaffermissantVisage);
    }

    public void setSoinRaffermissantVisage(String soinRaffermissantVisage) {
        this.soinRaffermissantVisage = soinRaffermissantVisage;
    }

    public String getTondeuseBarbe() {
        return normalizeValue(tondeuseBarbe);
    }

    public void setTondeuseBarbe(String tondeuseBarbe) {
        this.tondeuseBarbe = tondeuseBarbe;
    }

    public String getTonique() {
        return normalizeValue(tonique);
    }

    public void setTonique(String tonique) {
        this.tonique = tonique;
    }

    public String getVernisAOngles() {
        return normalizeValue(vernisAOngles);
    }

    public void setVernisAOngles(String vernisAOngles) {
        this.vernisAOngles = vernisAOngles;
    }

    /**
     * Permet d'obtenir toutes les habitudes de consommation sous forme de map
     * Utile pour des traitements dynamiques et statistiques
     *
     * @return normalizeValue(Map avec les noms des attributs et leurs valeurs
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        // Lieux d'achat
        if (achatGrandesSurfaces != null)
            map.put("achatGrandesSurfaces", achatGrandesSurfaces);
        if (achatInstitutParfumerie != null)
            map.put("achatInstitutParfumerie", achatInstitutParfumerie);
        if (achatInternet != null)
            map.put("achatInternet", achatInternet);
        if (achatPharmacieParapharmacie != null)
            map.put("achatPharmacieParapharmacie", achatPharmacieParapharmacie);

        // Produits d'hygiène et soins
        if (antiTranspirant != null)
            map.put("antiTranspirant", antiTranspirant);
        if (apresRasage != null)
            map.put("apresRasage", apresRasage);
        if (apresShampoing != null)
            map.put("apresShampoing", apresShampoing);
        // Ajouter tous les autres attributs

        // Pour éviter un code trop verbeux, on pourrait utiliser la réflexion
        // Mais ici nous utilisons une approche explicite pour plus de clarté

        return map;
    }

    private String normalizeValue(String value) {
        if (value == null) {
            return "non"; // Valeur par défaut pour les NULL
        }

        value = value.toLowerCase();

        if (value.equals("oui") || value.equals("regulierement")) {
            return "oui";
        } else {
            return "non"; // "jamais", "occasionnellement", etc.
        }
    }

}