package com.example.cosmetest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "volontaire_hc")
public class VolontaireHc implements Serializable {

    // Purchase channels
    @Column(length = 50, name = "achat_grandes_surfaces", nullable = true)
    private String achatGrandesSurfaces;

    @Column(length = 50, name = "achat_institut_parfumerie", nullable = true)
    private String achatInstitutParfumerie;

    @Column(length = 50, name = "achat_internet", nullable = true)
    private String achatInternet;

    @Column(length = 50, name = "achat_pharmacie_parapharmacie", nullable = true)
    private String achatPharmacieParapharmacie;

    // Product usage fields - typically frequency or yes/no responses
    @Column(length = 50, name = "anti_transpirant", nullable = true)
    private String antiTranspirant;

    @Column(length = 20, name = "anti_cerne", nullable = true)
    private String anticerne;

    @Column(length = 20, name = "apres_rasage", nullable = true)
    private String apresRasage;

    @Column(length = 20, name = "apres_shampoing", nullable = true)
    private String apresShampoing;

    @Column(length = 20, name = "autobronzant", nullable = true)
    private String autobronzant;

    @Column(length = 20, name = "base_maquillage", nullable = true)
    private String baseMaquillage;

    @Column(name = "blush_fard_a_joues", length = 20, nullable = true)
    private String blushFardAJoues;

    @Column(length = 20, nullable = true)
    private String cire;

    @Column(length = 20, nullable = true)
    private String colorationMeches;

    @Column(length = 20, nullable = true)
    private String correcteurTeint;

    @Column(length = 20, nullable = true)
    private String crayonLevres;

    @Column(length = 20, nullable = true)
    private String crayonsYeux;

    @Column(length = 20, nullable = true)
    private String cremeDepilatoire;

    @Column(length = 20, nullable = true)
    private String cremeTeintee;

    @Column(length = 20, nullable = true)
    private String demaquillantVisage;

    @Column(length = 20, nullable = true)
    private String demaquillantWaterproof;

    @Column(length = 20, nullable = true)
    private String demaquillantYeux;

    @Column(length = 20, nullable = true)
    private String deodorant;

    @Column(length = 20, nullable = true)
    private String dissolvantOngles;

    @Column(length = 20, nullable = true)
    private String eauDeToilette;

    @Column(length = 20, nullable = true)
    private String epilateurElectrique;

    @Column(length = 20, nullable = true)
    private String epilationDefinitive;

    @Column(length = 20, nullable = true)
    private String extensionsCapillaires;

    @Column(length = 20, nullable = true)
    private String eyeliner;

    @Column(name = "fard_a_paupieres", length = 20, nullable = true)
    private String fardAPaupieres;

    @Column(length = 20, nullable = true)
    private String fauxCils;

    @Column(length = 20, nullable = true)
    private String fauxOngles;

    @Column(length = 20, nullable = true)
    private String fondDeTeint;

    @Column(name = "gel_a_raser", length = 20, nullable = true)
    private String gelARaser;

    @Column(length = 20, nullable = true)
    private String gelDouche;

    @Column(length = 20, nullable = true)
    private String gelNettoyant;

    @Column(length = 20, nullable = true)
    private String gloss;

    @Column(length = 20, nullable = true)
    private String gommageCorps;

    @Column(length = 20, nullable = true)
    private String gommageVisage;

    @Id
    @Column(name = "id_vol", nullable = false)
    private int idVol;

    @Column(length = 50, nullable = true)
    private String institut;

    @Column(length = 20, nullable = true)
    private String laitDouche;

    @Column(length = 20, nullable = true)
    private String lissageDefrisage;

    @Column(length = 20, nullable = true)
    private String lotionMicellaire;

    @Column(length = 20, nullable = true)
    private String manucures;

    @Column(length = 20, nullable = true)
    private String maquillageDesSourcils;

    @Column(length = 20, nullable = true)
    private String maquillagePermanentLevres;

    @Column(length = 20, nullable = true)
    private String maquillagePermanentSourcils;

    @Column(length = 20, nullable = true)
    private String maquillagePermanentYeux;

    @Column(length = 20, nullable = true)
    private String mascara;

    @Column(length = 20, nullable = true)
    private String mascaraWaterproof;

    @Column(length = 20, nullable = true)
    private String masqueCapillaire;

    @Column(length = 20, nullable = true)
    private String masqueCorps;

    @Column(length = 20, nullable = true)
    private String masqueVisage;

    @Column(name = "mousse_a_raser", length = 50, nullable = true)
    private String mousseARaser;

    @Column(length = 20, nullable = true)
    private String nettoyantIntime;

    @Column(length = 50, nullable = true)
    private String ombreBarbe;

    @Column(length = 50, nullable = true)
    private String parfum;

    @Column(length = 20, nullable = true)
    private String permanente;

    @Column(length = 20, nullable = true)
    private String poudreLibre;

    @Column(length = 20, nullable = true)
    private String produitCoiffantFixant;

    @Column(length = 20, nullable = true)
    private String produitsBain;

    @Column(length = 50, nullable = true)
    private String produitsBio;

    @Column(length = 20, nullable = true)
    private String protecteurSolaireCorps;

    @Column(length = 20, nullable = true)
    private String protecteurSolaireLevres;

    @Column(length = 20, nullable = true)
    private String protecteurSolaireVisage;

    @Column(length = 50, nullable = true)
    private String rasoir;

    @Column(length = 50, nullable = true)
    private String rasoirElectrique;

    @Column(length = 50, nullable = true)
    private String rasoirMecanique;

    @Column(name = "rouge_a_levres", length = 20, nullable = true)
    private String rougeALevres;

    @Column(length = 20, nullable = true)
    private String savon;

    @Column(length = 20, nullable = true)
    private String shampoing;

    @Column(length = 20, nullable = true)
    private String soinAmincissant;

    @Column(length = 20, nullable = true)
    private String soinAntiAgeCorps;

    @Column(length = 20, nullable = true)
    private String soinAntiAgeMains;

    @Column(length = 20, nullable = true)
    private String soinAntiAgeVisage;

    @Column(length = 20, nullable = true)
    private String soinAntiCellulite;

    @Column(length = 20, nullable = true)
    private String soinAntiRidesVisage;

    @Column(length = 20, nullable = true)
    private String soinAntiRougeursVisage;

    @Column(length = 20, nullable = true)
    private String soinAntiTachesDecollete;

    @Column(length = 20, nullable = true)
    private String soinAntiTachesMains;

    @Column(length = 20, nullable = true)
    private String soinAntiTachesVisage;

    @Column(length = 20, nullable = true)
    private String soinAntiVergetures;

    @Column(length = 20, nullable = true)
    private String soinApresSoleil;

    @Column(length = 20, nullable = true)
    private String soinContourDesLevres;

    @Column(length = 20, nullable = true)
    private String soinContourDesYeux;

    @Column(length = 20, nullable = true)
    private String soinEclatDuTeint;

    @Column(length = 20, nullable = true)
    private String soinHydratantCorps;

    @Column(length = 20, nullable = true)
    private String soinHydratantMains;

    @Column(length = 20, nullable = true)
    private String soinHydratantVisage;

    @Column(length = 20, nullable = true)
    private String soinMatifiantVisage;

    @Column(length = 20, nullable = true)
    private String soinNourissantVisage;

    @Column(length = 20, nullable = true)
    private String soinNourrissantCorps;

    @Column(length = 20, nullable = true)
    private String soinNourrissantMains;

    @Column(length = 20, nullable = true)
    private String soinOngles;

    @Column(length = 20, nullable = true)
    private String soinPieds;

    @Column(length = 20, nullable = true)
    private String soinRaffermissantCorps;

    @Column(length = 20, nullable = true)
    private String soinRaffermissantVisage;

    @Column(length = 50, nullable = true)
    private String tondeuseBarbe;

    @Column(length = 50, nullable = true)
    private String tonique;

    @Column(name = "vernis_a_ongles", length = 50, nullable = true)
    private String vernisAOngles;

    public VolontaireHc() {}

    public VolontaireHc(int idVol) { this.idVol = idVol; }

    // Getters/setters copied from previous superclass
    public String getAchatGrandesSurfaces() { return this.achatGrandesSurfaces; }
    public void setAchatGrandesSurfaces(String v) { this.achatGrandesSurfaces = v; }
    public String getAchatInstitutParfumerie() { return this.achatInstitutParfumerie; }
    public void setAchatInstitutParfumerie(String v) { this.achatInstitutParfumerie = v; }
    public String getAchatInternet() { return this.achatInternet; }
    public void setAchatInternet(String v) { this.achatInternet = v; }
    public String getAchatPharmacieParapharmacie() { return this.achatPharmacieParapharmacie; }
    public void setAchatPharmacieParapharmacie(String v) { this.achatPharmacieParapharmacie = v; }
    public String getAntiTranspirant() { return this.antiTranspirant; }
    public void setAntiTranspirant(String v) { this.antiTranspirant = v; }
    public String getAnticerne() { return this.anticerne; }
    public void setAnticerne(String v) { this.anticerne = v; }
    public String getApresRasage() { return this.apresRasage; }
    public void setApresRasage(String v) { this.apresRasage = v; }
    public String getApresShampoing() { return this.apresShampoing; }
    public void setApresShampoing(String v) { this.apresShampoing = v; }
    public String getAutobronzant() { return this.autobronzant; }
    public void setAutobronzant(String v) { this.autobronzant = v; }
    public String getBaseMaquillage() { return this.baseMaquillage; }
    public void setBaseMaquillage(String v) { this.baseMaquillage = v; }
    public String getBlushFardAJoues() { return this.blushFardAJoues; }
    public void setBlushFardAJoues(String v) { this.blushFardAJoues = v; }
    public String getCire() { return this.cire; }
    public void setCire(String v) { this.cire = v; }
    public String getColorationMeches() { return this.colorationMeches; }
    public void setColorationMeches(String v) { this.colorationMeches = v; }
    public String getCorrecteurTeint() { return this.correcteurTeint; }
    public void setCorrecteurTeint(String v) { this.correcteurTeint = v; }
    public String getCrayonLevres() { return this.crayonLevres; }
    public void setCrayonLevres(String v) { this.crayonLevres = v; }
    public String getCrayonsYeux() { return this.crayonsYeux; }
    public void setCrayonsYeux(String v) { this.crayonsYeux = v; }
    public String getCremeDepilatoire() { return this.cremeDepilatoire; }
    public void setCremeDepilatoire(String v) { this.cremeDepilatoire = v; }
    public String getCremeTeintee() { return this.cremeTeintee; }
    public void setCremeTeintee(String v) { this.cremeTeintee = v; }
    public String getDemaquillantVisage() { return this.demaquillantVisage; }
    public void setDemaquillantVisage(String v) { this.demaquillantVisage = v; }
    public String getDemaquillantWaterproof() { return this.demaquillantWaterproof; }
    public void setDemaquillantWaterproof(String v) { this.demaquillantWaterproof = v; }
    public String getDemaquillantYeux() { return this.demaquillantYeux; }
    public void setDemaquillantYeux(String v) { this.demaquillantYeux = v; }
    public String getDeodorant() { return this.deodorant; }
    public void setDeodorant(String v) { this.deodorant = v; }
    public String getDissolvantOngles() { return this.dissolvantOngles; }
    public void setDissolvantOngles(String v) { this.dissolvantOngles = v; }
    public String getEauDeToilette() { return this.eauDeToilette; }
    public void setEauDeToilette(String v) { this.eauDeToilette = v; }
    public String getEpilateurElectrique() { return this.epilateurElectrique; }
    public void setEpilateurElectrique(String v) { this.epilateurElectrique = v; }
    public String getEpilationDefinitive() { return this.epilationDefinitive; }
    public void setEpilationDefinitive(String v) { this.epilationDefinitive = v; }
    public String getExtensionsCapillaires() { return this.extensionsCapillaires; }
    public void setExtensionsCapillaires(String v) { this.extensionsCapillaires = v; }
    public String getEyeliner() { return this.eyeliner; }
    public void setEyeliner(String v) { this.eyeliner = v; }
    public String getFardAPaupieres() { return this.fardAPaupieres; }
    public void setFardAPaupieres(String v) { this.fardAPaupieres = v; }
    public String getFauxCils() { return this.fauxCils; }
    public void setFauxCils(String v) { this.fauxCils = v; }
    public String getFauxOngles() { return this.fauxOngles; }
    public void setFauxOngles(String v) { this.fauxOngles = v; }
    public String getFondDeTeint() { return this.fondDeTeint; }
    public void setFondDeTeint(String v) { this.fondDeTeint = v; }
    public String getGelARaser() { return this.gelARaser; }
    public void setGelARaser(String v) { this.gelARaser = v; }
    public String getGelDouche() { return this.gelDouche; }
    public void setGelDouche(String v) { this.gelDouche = v; }
    public String getGelNettoyant() { return this.gelNettoyant; }
    public void setGelNettoyant(String v) { this.gelNettoyant = v; }
    public String getGloss() { return this.gloss; }
    public void setGloss(String v) { this.gloss = v; }
    public String getGommageCorps() { return this.gommageCorps; }
    public void setGommageCorps(String v) { this.gommageCorps = v; }
    public String getGommageVisage() { return this.gommageVisage; }
    public void setGommageVisage(String v) { this.gommageVisage = v; }
    public int getIdVol() { return this.idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }
    public String getInstitut() { return this.institut; }
    public void setInstitut(String v) { this.institut = v; }
    public String getLaitDouche() { return this.laitDouche; }
    public void setLaitDouche(String v) { this.laitDouche = v; }
    public String getLissageDefrisage() { return this.lissageDefrisage; }
    public void setLissageDefrisage(String v) { this.lissageDefrisage = v; }
    public String getLotionMicellaire() { return this.lotionMicellaire; }
    public void setLotionMicellaire(String v) { this.lotionMicellaire = v; }
    public String getManucures() { return this.manucures; }
    public void setManucures(String v) { this.manucures = v; }
    public String getMaquillageDesSourcils() { return this.maquillageDesSourcils; }
    public void setMaquillageDesSourcils(String v) { this.maquillageDesSourcils = v; }
    public String getMaquillagePermanentLevres() { return this.maquillagePermanentLevres; }
    public void setMaquillagePermanentLevres(String v) { this.maquillagePermanentLevres = v; }
    public String getMaquillagePermanentSourcils() { return this.maquillagePermanentSourcils; }
    public void setMaquillagePermanentSourcils(String v) { this.maquillagePermanentSourcils = v; }
    public String getMaquillagePermanentYeux() { return this.maquillagePermanentYeux; }
    public void setMaquillagePermanentYeux(String v) { this.maquillagePermanentYeux = v; }
    public String getMascara() { return this.mascara; }
    public void setMascara(String v) { this.mascara = v; }
    public String getMascaraWaterproof() { return this.mascaraWaterproof; }
    public void setMascaraWaterproof(String v) { this.mascaraWaterproof = v; }
    public String getMasqueCapillaire() { return this.masqueCapillaire; }
    public void setMasqueCapillaire(String v) { this.masqueCapillaire = v; }
    public String getMasqueCorps() { return this.masqueCorps; }
    public void setMasqueCorps(String v) { this.masqueCorps = v; }
    public String getMasqueVisage() { return this.masqueVisage; }
    public void setMasqueVisage(String v) { this.masqueVisage = v; }
    public String getMousseARaser() { return this.mousseARaser; }
    public void setMousseARaser(String v) { this.mousseARaser = v; }
    public String getNettoyantIntime() { return this.nettoyantIntime; }
    public void setNettoyantIntime(String v) { this.nettoyantIntime = v; }
    public String getOmbreBarbe() { return this.ombreBarbe; }
    public void setOmbreBarbe(String v) { this.ombreBarbe = v; }
    public String getParfum() { return this.parfum; }
    public void setParfum(String v) { this.parfum = v; }
    public String getPermanente() { return this.permanente; }
    public void setPermanente(String v) { this.permanente = v; }
    public String getPoudreLibre() { return this.poudreLibre; }
    public void setPoudreLibre(String v) { this.poudreLibre = v; }
    public String getProduitCoiffantFixant() { return this.produitCoiffantFixant; }
    public void setProduitCoiffantFixant(String v) { this.produitCoiffantFixant = v; }
    public String getProduitsBain() { return this.produitsBain; }
    public void setProduitsBain(String v) { this.produitsBain = v; }
    public String getProduitsBio() { return this.produitsBio; }
    public void setProduitsBio(String v) { this.produitsBio = v; }
    public String getProtecteurSolaireCorps() { return this.protecteurSolaireCorps; }
    public void setProtecteurSolaireCorps(String v) { this.protecteurSolaireCorps = v; }
    public String getProtecteurSolaireLevres() { return this.protecteurSolaireLevres; }
    public void setProtecteurSolaireLevres(String v) { this.protecteurSolaireLevres = v; }
    public String getProtecteurSolaireVisage() { return this.protecteurSolaireVisage; }
    public void setProtecteurSolaireVisage(String v) { this.protecteurSolaireVisage = v; }
    public String getRasoir() { return this.rasoir; }
    public void setRasoir(String v) { this.rasoir = v; }
    public String getRasoirElectrique() { return this.rasoirElectrique; }
    public void setRasoirElectrique(String v) { this.rasoirElectrique = v; }
    public String getRasoirMecanique() { return this.rasoirMecanique; }
    public void setRasoirMecanique(String v) { this.rasoirMecanique = v; }
    public String getRougeALevres() { return this.rougeALevres; }
    public void setRougeALevres(String v) { this.rougeALevres = v; }
    public String getSavon() { return this.savon; }
    public void setSavon(String v) { this.savon = v; }
    public String getShampoing() { return this.shampoing; }
    public void setShampoing(String v) { this.shampoing = v; }
    public String getSoinAmincissant() { return this.soinAmincissant; }
    public void setSoinAmincissant(String v) { this.soinAmincissant = v; }
    public String getSoinAntiAgeCorps() { return this.soinAntiAgeCorps; }
    public void setSoinAntiAgeCorps(String v) { this.soinAntiAgeCorps = v; }
    public String getSoinAntiAgeMains() { return this.soinAntiAgeMains; }
    public void setSoinAntiAgeMains(String v) { this.soinAntiAgeMains = v; }
    public String getSoinAntiAgeVisage() { return this.soinAntiAgeVisage; }
    public void setSoinAntiAgeVisage(String v) { this.soinAntiAgeVisage = v; }
    public String getSoinAntiCellulite() { return this.soinAntiCellulite; }
    public void setSoinAntiCellulite(String v) { this.soinAntiCellulite = v; }
    public String getSoinAntiRidesVisage() { return this.soinAntiRidesVisage; }
    public void setSoinAntiRidesVisage(String v) { this.soinAntiRidesVisage = v; }
    public String getSoinAntiRougeursVisage() { return this.soinAntiRougeursVisage; }
    public void setSoinAntiRougeursVisage(String v) { this.soinAntiRougeursVisage = v; }
    public String getSoinAntiTachesDecollete() { return this.soinAntiTachesDecollete; }
    public void setSoinAntiTachesDecollete(String v) { this.soinAntiTachesDecollete = v; }
    public String getSoinAntiTachesMains() { return this.soinAntiTachesMains; }
    public void setSoinAntiTachesMains(String v) { this.soinAntiTachesMains = v; }
    public String getSoinAntiTachesVisage() { return this.soinAntiTachesVisage; }
    public void setSoinAntiTachesVisage(String v) { this.soinAntiTachesVisage = v; }
    public String getSoinAntiVergetures() { return this.soinAntiVergetures; }
    public void setSoinAntiVergetures(String v) { this.soinAntiVergetures = v; }
    public String getSoinApresSoleil() { return this.soinApresSoleil; }
    public void setSoinApresSoleil(String v) { this.soinApresSoleil = v; }
    public String getSoinContourDesLevres() { return this.soinContourDesLevres; }
    public void setSoinContourDesLevres(String v) { this.soinContourDesLevres = v; }
    public String getSoinContourDesYeux() { return this.soinContourDesYeux; }
    public void setSoinContourDesYeux(String v) { this.soinContourDesYeux = v; }
    public String getSoinEclatDuTeint() { return this.soinEclatDuTeint; }
    public void setSoinEclatDuTeint(String v) { this.soinEclatDuTeint = v; }
    public String getSoinHydratantCorps() { return this.soinHydratantCorps; }
    public void setSoinHydratantCorps(String v) { this.soinHydratantCorps = v; }
    public String getSoinHydratantMains() { return this.soinHydratantMains; }
    public void setSoinHydratantMains(String v) { this.soinHydratantMains = v; }
    public String getSoinHydratantVisage() { return this.soinHydratantVisage; }
    public void setSoinHydratantVisage(String v) { this.soinHydratantVisage = v; }
    public String getSoinMatifiantVisage() { return this.soinMatifiantVisage; }
    public void setSoinMatifiantVisage(String v) { this.soinMatifiantVisage = v; }
    public String getSoinNourissantVisage() { return this.soinNourissantVisage; }
    public void setSoinNourissantVisage(String v) { this.soinNourissantVisage = v; }
    public String getSoinNourrissantCorps() { return this.soinNourrissantCorps; }
    public void setSoinNourrissantCorps(String v) { this.soinNourrissantCorps = v; }
    public String getSoinNourrissantMains() { return this.soinNourrissantMains; }
    public void setSoinNourrissantMains(String v) { this.soinNourrissantMains = v; }
    public String getSoinOngles() { return this.soinOngles; }
    public void setSoinOngles(String v) { this.soinOngles = v; }
    public String getSoinPieds() { return this.soinPieds; }
    public void setSoinPieds(String v) { this.soinPieds = v; }
    public String getSoinRaffermissantCorps() { return this.soinRaffermissantCorps; }
    public void setSoinRaffermissantCorps(String v) { this.soinRaffermissantCorps = v; }
    public String getSoinRaffermissantVisage() { return this.soinRaffermissantVisage; }
    public void setSoinRaffermissantVisage(String v) { this.soinRaffermissantVisage = v; }
    public String getTondeuseBarbe() { return this.tondeuseBarbe; }
    public void setTondeuseBarbe(String v) { this.tondeuseBarbe = v; }
    public String getTonique() { return this.tonique; }
    public void setTonique(String v) { this.tonique = v; }
    public String getVernisAOngles() { return this.vernisAOngles; }
    public void setVernisAOngles(String v) { this.vernisAOngles = v; }
}
