package com.example.cosmetest.domain.model;
// Generated 15 d�c. 2024, 15:56:41 by Hibernate Tools 6.5.1.Final

import jakarta.persistence.*;

@Entity // Indique que cette classe est une entité JPA
@Table(name = "panel_hc") // Permet de spécifier le nom de la table
public class PanelHc implements java.io.Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PANEL")
	private int idPanel;

	// Hair removal products
	@Column(length = 20)
	private String cire;

	@Column(length = 20)
	private String cremeDepilatoire;

	@Column(length = 20)
	private String epilateurElectrique;

	@Column(length = 20)
	private String rasoir;

	@Column(length = 20)
	private String institut;

	@Column(length = 20)
	private String epilationDefinitive;

	// Purchase channels
	@Column(length = 20)
	private String achatInstitutParfumerie;

	@Column(length = 20)
	private String achatGrandesSurfaces;

	@Column(length = 20)
	private String achatPharmacieParapharmacie;

	@Column(length = 20)
	private String achatInternet;

	// Body care products
	@Column(length = 20)
	private String produitsBio;

	@Column(length = 20)
	private String savon;

	@Column(length = 20)
	private String gelDouche;

	@Column(length = 20)
	private String laitDouche;

	@Column(length = 20)
	private String nettoyantIntime;

	@Column(length = 20)
	private String produitsBain;

	@Column(length = 20)
	private String deodorant;

	@Column(length = 20)
	private String antiTranspirant;

	@Column(length = 20)
	private String eauDeToilette;

	@Column(length = 20)
	private String parfum;

	// Face cleansing products
	@Column(length = 20)
	private String gelNettoyant;

	@Column(length = 20)
	private String demaquillantVisage;

	@Column(length = 20)
	private String tonique;

	@Column(length = 20)
	private String lotionMicellaire;

	@Column(length = 20)
	private String demaquillantYeux;

	@Column(length = 20)
	private String demaquillantWaterproof;

	@Column(length = 20)
	private String dissolvantOngles;

	// Face care products
	@Column(length = 20)
	private String soinHydratantVisage;

	@Column(length = 20)
	private String soinNourissantVisage;

	@Column(length = 20)
	private String soinAntiRidesVisage;

	@Column(length = 20)
	private String soinAntiAgeVisage;

	@Column(length = 20)
	private String soinRaffermissantVisage;

	@Column(length = 20)
	private String gommageVisage;

	@Column(length = 20)
	private String masqueVisage;

	@Column(length = 20)
	private String soinAntiTachesVisage;

	@Column(length = 20)
	private String soinAntiRougeursVisage;

	@Column(length = 20)
	private String soinMatifiantVisage;

	@Column(length = 20)
	private String soinEclatDuTeint;

	@Column(length = 20)
	private String soinContourDesYeux;

	@Column(length = 20)
	private String soinContourDesLevres;

	// Body care products
	@Column(length = 20)
	private String soinHydratantCorps;

	@Column(length = 20)
	private String soinNourrissantCorps;

	@Column(length = 20)
	private String soinAntiAgeCorps;

	@Column(length = 20)
	private String soinRaffermissantCorps;

	@Column(length = 20)
	private String gommageCorps;

	@Column(length = 20)
	private String masqueCorps;

	@Column(length = 20)
	private String soinAntiTachesDecollete;

	@Column(length = 20)
	private String soinAmincissant;

	@Column(length = 20)
	private String soinAntiCellulite;

	@Column(length = 20)
	private String soinAntiVergetures;

	// Hand, foot and nail care
	@Column(length = 20)
	private String soinHydratantMains;

	@Column(length = 20)
	private String soinNourrissantMains;

	@Column(length = 20)
	private String soinAntiAgeMains;

	@Column(length = 20)
	private String soinAntiTachesMains;

	@Column(length = 20)
	private String soinPieds;

	@Column(length = 20)
	private String soinOngles;

	@Column(length = 20)
	private String manucures;

	// Base makeup products
	@Column(length = 20)
	private String cremeTeintee;

	@Column(length = 20)
	private String baseMaquillage;

	@Column(length = 20)
	private String correcteurTeint;

	@Column(length = 20)
	private String anticerne;

	@Column(length = 20)
	private String fondDeTeint;

	@Column(length = 20)
	private String poudreLibre;

	@Column(name = "blush_fard_a_joues", length = 20)
	private String blushFardAJoues;

	// Eye makeup products
	@Column(length = 20)
	private String mascara;

	@Column(length = 20)
	private String mascaraWaterproof;

	@Column(name = "fard_a_paupieres", length = 20)
	private String fardAPaupieres;

	@Column(length = 20)
	private String crayonsYeux;

	@Column(length = 20)
	private String eyeliner;

	@Column(length = 20)
	private String fauxCils;

	@Column(length = 20)
	private String maquillageDesSourcils;

	// Lip products
	@Column(name = "rouge_a_levres", columnDefinition = "TEXT")
	private String rougeALevres;

	@Column(length = 20)
	private String gloss;

	@Column(length = 20)
	private String crayonLevres;

	// Nail products
	@Column(name = "vernis_a_ongles", columnDefinition = "TEXT")
	private String vernisAOngles;

	@Column(length = 20)
	private String fauxOngles;

	// Permanent makeup
	@Column(length = 20)
	private String maquillagePermanentYeux;

	@Column(length = 20)
	private String maquillagePermanentSourcils;

	@Column(length = 20)
	private String maquillagePermanentLevres;

	// Hair care products
	@Column(length = 20)
	private String shampoing;

	@Column(length = 20)
	private String apresShampoing;

	@Column(length = 20)
	private String masqueCapillaire;

	@Column(length = 20)
	private String colorationMeches;

	@Column(length = 20)
	private String produitCoiffantFixant;

	@Column(length = 20)
	private String permanente;

	@Column(length = 20)
	private String lissageDefrisage;

	@Column(length = 20)
	private String extensionsCapillaires;

	// Sun protection products
	@Column(length = 20)
	private String protecteurSolaireVisage;

	@Column(length = 20)
	private String protecteurSolaireCorps;

	@Column(length = 20)
	private String protecteurSolaireLevres;

	@Column(length = 20)
	private String soinApresSoleil;

	@Column(length = 20)
	private String autobronzant;

	public PanelHc() {
	}

	public PanelHc(int idPanel) {
		this.idPanel = idPanel;
	}

	public PanelHc(int idPanel, String cire, String cremeDepilatoire, String epilateurElectrique, String rasoir,
			String institut, String epilationDefinitive, String achatInstitutParfumerie, String achatGrandesSurfaces,
			String achatPharmacieParapharmacie, String achatInternet, String produitsBio, String savon,
			String gelDouche, String laitDouche, String nettoyantIntime, String produitsBain, String deodorant,
			String antiTranspirant, String eauDeToilette, String parfum, String gelNettoyant, String demaquillantVisage,
			String tonique, String lotionMicellaire, String demaquillantYeux, String demaquillantWaterproof,
			String dissolvantOngles, String soinHydratantVisage, String soinNourissantVisage,
			String soinAntiRidesVisage, String soinAntiAgeVisage, String soinRaffermissantVisage, String gommageVisage,
			String masqueVisage, String soinAntiTachesVisage, String soinAntiRougeursVisage, String soinMatifiantVisage,
			String soinEclatDuTeint, String soinContourDesYeux, String soinContourDesLevres, String soinHydratantCorps,
			String soinNourrissantCorps, String soinAntiAgeCorps, String soinRaffermissantCorps, String gommageCorps,
			String masqueCorps, String soinAntiTachesDecollete, String soinAmincissant, String soinAntiCellulite,
			String soinAntiVergetures, String soinHydratantMains, String soinNourrissantMains, String soinAntiAgeMains,
			String soinAntiTachesMains, String soinPieds, String soinOngles, String manucures, String cremeTeintee,
			String baseMaquillage, String correcteurTeint, String anticerne, String fondDeTeint, String poudreLibre,
			String blushFardAJoues, String mascara, String mascaraWaterproof, String fardAPaupieres, String crayonsYeux,
			String eyeliner, String fauxCils, String maquillageDesSourcils, String rougeALevres, String gloss,
			String crayonLevres, String vernisAOngles, String fauxOngles, String maquillagePermanentYeux,
			String maquillagePermanentSourcils, String maquillagePermanentLevres, String shampoing,
			String apresShampoing, String masqueCapillaire, String colorationMeches, String produitCoiffantFixant,
			String permanente, String lissageDefrisage, String extensionsCapillaires, String protecteurSolaireVisage,
			String protecteurSolaireCorps, String protecteurSolaireLevres, String soinApresSoleil,
			String autobronzant) {
		this.idPanel = idPanel;
		this.cire = cire;
		this.cremeDepilatoire = cremeDepilatoire;
		this.epilateurElectrique = epilateurElectrique;
		this.rasoir = rasoir;
		this.institut = institut;
		this.epilationDefinitive = epilationDefinitive;
		this.achatInstitutParfumerie = achatInstitutParfumerie;
		this.achatGrandesSurfaces = achatGrandesSurfaces;
		this.achatPharmacieParapharmacie = achatPharmacieParapharmacie;
		this.achatInternet = achatInternet;
		this.produitsBio = produitsBio;
		this.savon = savon;
		this.gelDouche = gelDouche;
		this.laitDouche = laitDouche;
		this.nettoyantIntime = nettoyantIntime;
		this.produitsBain = produitsBain;
		this.deodorant = deodorant;
		this.antiTranspirant = antiTranspirant;
		this.eauDeToilette = eauDeToilette;
		this.parfum = parfum;
		this.gelNettoyant = gelNettoyant;
		this.demaquillantVisage = demaquillantVisage;
		this.tonique = tonique;
		this.lotionMicellaire = lotionMicellaire;
		this.demaquillantYeux = demaquillantYeux;
		this.demaquillantWaterproof = demaquillantWaterproof;
		this.dissolvantOngles = dissolvantOngles;
		this.soinHydratantVisage = soinHydratantVisage;
		this.soinNourissantVisage = soinNourissantVisage;
		this.soinAntiRidesVisage = soinAntiRidesVisage;
		this.soinAntiAgeVisage = soinAntiAgeVisage;
		this.soinRaffermissantVisage = soinRaffermissantVisage;
		this.gommageVisage = gommageVisage;
		this.masqueVisage = masqueVisage;
		this.soinAntiTachesVisage = soinAntiTachesVisage;
		this.soinAntiRougeursVisage = soinAntiRougeursVisage;
		this.soinMatifiantVisage = soinMatifiantVisage;
		this.soinEclatDuTeint = soinEclatDuTeint;
		this.soinContourDesYeux = soinContourDesYeux;
		this.soinContourDesLevres = soinContourDesLevres;
		this.soinHydratantCorps = soinHydratantCorps;
		this.soinNourrissantCorps = soinNourrissantCorps;
		this.soinAntiAgeCorps = soinAntiAgeCorps;
		this.soinRaffermissantCorps = soinRaffermissantCorps;
		this.gommageCorps = gommageCorps;
		this.masqueCorps = masqueCorps;
		this.soinAntiTachesDecollete = soinAntiTachesDecollete;
		this.soinAmincissant = soinAmincissant;
		this.soinAntiCellulite = soinAntiCellulite;
		this.soinAntiVergetures = soinAntiVergetures;
		this.soinHydratantMains = soinHydratantMains;
		this.soinNourrissantMains = soinNourrissantMains;
		this.soinAntiAgeMains = soinAntiAgeMains;
		this.soinAntiTachesMains = soinAntiTachesMains;
		this.soinPieds = soinPieds;
		this.soinOngles = soinOngles;
		this.manucures = manucures;
		this.cremeTeintee = cremeTeintee;
		this.baseMaquillage = baseMaquillage;
		this.correcteurTeint = correcteurTeint;
		this.anticerne = anticerne;
		this.fondDeTeint = fondDeTeint;
		this.poudreLibre = poudreLibre;
		this.blushFardAJoues = blushFardAJoues;
		this.mascara = mascara;
		this.mascaraWaterproof = mascaraWaterproof;
		this.fardAPaupieres = fardAPaupieres;
		this.crayonsYeux = crayonsYeux;
		this.eyeliner = eyeliner;
		this.fauxCils = fauxCils;
		this.maquillageDesSourcils = maquillageDesSourcils;
		this.rougeALevres = rougeALevres;
		this.gloss = gloss;
		this.crayonLevres = crayonLevres;
		this.vernisAOngles = vernisAOngles;
		this.fauxOngles = fauxOngles;
		this.maquillagePermanentYeux = maquillagePermanentYeux;
		this.maquillagePermanentSourcils = maquillagePermanentSourcils;
		this.maquillagePermanentLevres = maquillagePermanentLevres;
		this.shampoing = shampoing;
		this.apresShampoing = apresShampoing;
		this.masqueCapillaire = masqueCapillaire;
		this.colorationMeches = colorationMeches;
		this.produitCoiffantFixant = produitCoiffantFixant;
		this.permanente = permanente;
		this.lissageDefrisage = lissageDefrisage;
		this.extensionsCapillaires = extensionsCapillaires;
		this.protecteurSolaireVisage = protecteurSolaireVisage;
		this.protecteurSolaireCorps = protecteurSolaireCorps;
		this.protecteurSolaireLevres = protecteurSolaireLevres;
		this.soinApresSoleil = soinApresSoleil;
		this.autobronzant = autobronzant;
	}

	public int getIdPanel() {
		return this.idPanel;
	}

	public void setIdPanel(int idPanel) {
		this.idPanel = idPanel;
	}

	public String getCire() {
		return this.cire;
	}

	public void setCire(String cire) {
		this.cire = cire;
	}

	public String getCremeDepilatoire() {
		return this.cremeDepilatoire;
	}

	public void setCremeDepilatoire(String cremeDepilatoire) {
		this.cremeDepilatoire = cremeDepilatoire;
	}

	public String getEpilateurElectrique() {
		return this.epilateurElectrique;
	}

	public void setEpilateurElectrique(String epilateurElectrique) {
		this.epilateurElectrique = epilateurElectrique;
	}

	public String getRasoir() {
		return this.rasoir;
	}

	public void setRasoir(String rasoir) {
		this.rasoir = rasoir;
	}

	public String getInstitut() {
		return this.institut;
	}

	public void setInstitut(String institut) {
		this.institut = institut;
	}

	public String getEpilationDefinitive() {
		return this.epilationDefinitive;
	}

	public void setEpilationDefinitive(String epilationDefinitive) {
		this.epilationDefinitive = epilationDefinitive;
	}

	public String getAchatInstitutParfumerie() {
		return this.achatInstitutParfumerie;
	}

	public void setAchatInstitutParfumerie(String achatInstitutParfumerie) {
		this.achatInstitutParfumerie = achatInstitutParfumerie;
	}

	public String getAchatGrandesSurfaces() {
		return this.achatGrandesSurfaces;
	}

	public void setAchatGrandesSurfaces(String achatGrandesSurfaces) {
		this.achatGrandesSurfaces = achatGrandesSurfaces;
	}

	public String getAchatPharmacieParapharmacie() {
		return this.achatPharmacieParapharmacie;
	}

	public void setAchatPharmacieParapharmacie(String achatPharmacieParapharmacie) {
		this.achatPharmacieParapharmacie = achatPharmacieParapharmacie;
	}

	public String getAchatInternet() {
		return this.achatInternet;
	}

	public void setAchatInternet(String achatInternet) {
		this.achatInternet = achatInternet;
	}

	public String getProduitsBio() {
		return this.produitsBio;
	}

	public void setProduitsBio(String produitsBio) {
		this.produitsBio = produitsBio;
	}

	public String getSavon() {
		return this.savon;
	}

	public void setSavon(String savon) {
		this.savon = savon;
	}

	public String getGelDouche() {
		return this.gelDouche;
	}

	public void setGelDouche(String gelDouche) {
		this.gelDouche = gelDouche;
	}

	public String getLaitDouche() {
		return this.laitDouche;
	}

	public void setLaitDouche(String laitDouche) {
		this.laitDouche = laitDouche;
	}

	public String getNettoyantIntime() {
		return this.nettoyantIntime;
	}

	public void setNettoyantIntime(String nettoyantIntime) {
		this.nettoyantIntime = nettoyantIntime;
	}

	public String getProduitsBain() {
		return this.produitsBain;
	}

	public void setProduitsBain(String produitsBain) {
		this.produitsBain = produitsBain;
	}

	public String getDeodorant() {
		return this.deodorant;
	}

	public void setDeodorant(String deodorant) {
		this.deodorant = deodorant;
	}

	public String getAntiTranspirant() {
		return this.antiTranspirant;
	}

	public void setAntiTranspirant(String antiTranspirant) {
		this.antiTranspirant = antiTranspirant;
	}

	public String getEauDeToilette() {
		return this.eauDeToilette;
	}

	public void setEauDeToilette(String eauDeToilette) {
		this.eauDeToilette = eauDeToilette;
	}

	public String getParfum() {
		return this.parfum;
	}

	public void setParfum(String parfum) {
		this.parfum = parfum;
	}

	public String getGelNettoyant() {
		return this.gelNettoyant;
	}

	public void setGelNettoyant(String gelNettoyant) {
		this.gelNettoyant = gelNettoyant;
	}

	public String getDemaquillantVisage() {
		return this.demaquillantVisage;
	}

	public void setDemaquillantVisage(String demaquillantVisage) {
		this.demaquillantVisage = demaquillantVisage;
	}

	public String getTonique() {
		return this.tonique;
	}

	public void setTonique(String tonique) {
		this.tonique = tonique;
	}

	public String getLotionMicellaire() {
		return this.lotionMicellaire;
	}

	public void setLotionMicellaire(String lotionMicellaire) {
		this.lotionMicellaire = lotionMicellaire;
	}

	public String getDemaquillantYeux() {
		return this.demaquillantYeux;
	}

	public void setDemaquillantYeux(String demaquillantYeux) {
		this.demaquillantYeux = demaquillantYeux;
	}

	public String getDemaquillantWaterproof() {
		return this.demaquillantWaterproof;
	}

	public void setDemaquillantWaterproof(String demaquillantWaterproof) {
		this.demaquillantWaterproof = demaquillantWaterproof;
	}

	public String getDissolvantOngles() {
		return this.dissolvantOngles;
	}

	public void setDissolvantOngles(String dissolvantOngles) {
		this.dissolvantOngles = dissolvantOngles;
	}

	public String getSoinHydratantVisage() {
		return this.soinHydratantVisage;
	}

	public void setSoinHydratantVisage(String soinHydratantVisage) {
		this.soinHydratantVisage = soinHydratantVisage;
	}

	public String getSoinNourissantVisage() {
		return this.soinNourissantVisage;
	}

	public void setSoinNourissantVisage(String soinNourissantVisage) {
		this.soinNourissantVisage = soinNourissantVisage;
	}

	public String getSoinAntiRidesVisage() {
		return this.soinAntiRidesVisage;
	}

	public void setSoinAntiRidesVisage(String soinAntiRidesVisage) {
		this.soinAntiRidesVisage = soinAntiRidesVisage;
	}

	public String getSoinAntiAgeVisage() {
		return this.soinAntiAgeVisage;
	}

	public void setSoinAntiAgeVisage(String soinAntiAgeVisage) {
		this.soinAntiAgeVisage = soinAntiAgeVisage;
	}

	public String getSoinRaffermissantVisage() {
		return this.soinRaffermissantVisage;
	}

	public void setSoinRaffermissantVisage(String soinRaffermissantVisage) {
		this.soinRaffermissantVisage = soinRaffermissantVisage;
	}

	public String getGommageVisage() {
		return this.gommageVisage;
	}

	public void setGommageVisage(String gommageVisage) {
		this.gommageVisage = gommageVisage;
	}

	public String getMasqueVisage() {
		return this.masqueVisage;
	}

	public void setMasqueVisage(String masqueVisage) {
		this.masqueVisage = masqueVisage;
	}

	public String getSoinAntiTachesVisage() {
		return this.soinAntiTachesVisage;
	}

	public void setSoinAntiTachesVisage(String soinAntiTachesVisage) {
		this.soinAntiTachesVisage = soinAntiTachesVisage;
	}

	public String getSoinAntiRougeursVisage() {
		return this.soinAntiRougeursVisage;
	}

	public void setSoinAntiRougeursVisage(String soinAntiRougeursVisage) {
		this.soinAntiRougeursVisage = soinAntiRougeursVisage;
	}

	public String getSoinMatifiantVisage() {
		return this.soinMatifiantVisage;
	}

	public void setSoinMatifiantVisage(String soinMatifiantVisage) {
		this.soinMatifiantVisage = soinMatifiantVisage;
	}

	public String getSoinEclatDuTeint() {
		return this.soinEclatDuTeint;
	}

	public void setSoinEclatDuTeint(String soinEclatDuTeint) {
		this.soinEclatDuTeint = soinEclatDuTeint;
	}

	public String getSoinContourDesYeux() {
		return this.soinContourDesYeux;
	}

	public void setSoinContourDesYeux(String soinContourDesYeux) {
		this.soinContourDesYeux = soinContourDesYeux;
	}

	public String getSoinContourDesLevres() {
		return this.soinContourDesLevres;
	}

	public void setSoinContourDesLevres(String soinContourDesLevres) {
		this.soinContourDesLevres = soinContourDesLevres;
	}

	public String getSoinHydratantCorps() {
		return this.soinHydratantCorps;
	}

	public void setSoinHydratantCorps(String soinHydratantCorps) {
		this.soinHydratantCorps = soinHydratantCorps;
	}

	public String getSoinNourrissantCorps() {
		return this.soinNourrissantCorps;
	}

	public void setSoinNourrissantCorps(String soinNourrissantCorps) {
		this.soinNourrissantCorps = soinNourrissantCorps;
	}

	public String getSoinAntiAgeCorps() {
		return this.soinAntiAgeCorps;
	}

	public void setSoinAntiAgeCorps(String soinAntiAgeCorps) {
		this.soinAntiAgeCorps = soinAntiAgeCorps;
	}

	public String getSoinRaffermissantCorps() {
		return this.soinRaffermissantCorps;
	}

	public void setSoinRaffermissantCorps(String soinRaffermissantCorps) {
		this.soinRaffermissantCorps = soinRaffermissantCorps;
	}

	public String getGommageCorps() {
		return this.gommageCorps;
	}

	public void setGommageCorps(String gommageCorps) {
		this.gommageCorps = gommageCorps;
	}

	public String getMasqueCorps() {
		return this.masqueCorps;
	}

	public void setMasqueCorps(String masqueCorps) {
		this.masqueCorps = masqueCorps;
	}

	public String getSoinAntiTachesDecollete() {
		return this.soinAntiTachesDecollete;
	}

	public void setSoinAntiTachesDecollete(String soinAntiTachesDecollete) {
		this.soinAntiTachesDecollete = soinAntiTachesDecollete;
	}

	public String getSoinAmincissant() {
		return this.soinAmincissant;
	}

	public void setSoinAmincissant(String soinAmincissant) {
		this.soinAmincissant = soinAmincissant;
	}

	public String getSoinAntiCellulite() {
		return this.soinAntiCellulite;
	}

	public void setSoinAntiCellulite(String soinAntiCellulite) {
		this.soinAntiCellulite = soinAntiCellulite;
	}

	public String getSoinAntiVergetures() {
		return this.soinAntiVergetures;
	}

	public void setSoinAntiVergetures(String soinAntiVergetures) {
		this.soinAntiVergetures = soinAntiVergetures;
	}

	public String getSoinHydratantMains() {
		return this.soinHydratantMains;
	}

	public void setSoinHydratantMains(String soinHydratantMains) {
		this.soinHydratantMains = soinHydratantMains;
	}

	public String getSoinNourrissantMains() {
		return this.soinNourrissantMains;
	}

	public void setSoinNourrissantMains(String soinNourrissantMains) {
		this.soinNourrissantMains = soinNourrissantMains;
	}

	public String getSoinAntiAgeMains() {
		return this.soinAntiAgeMains;
	}

	public void setSoinAntiAgeMains(String soinAntiAgeMains) {
		this.soinAntiAgeMains = soinAntiAgeMains;
	}

	public String getSoinAntiTachesMains() {
		return this.soinAntiTachesMains;
	}

	public void setSoinAntiTachesMains(String soinAntiTachesMains) {
		this.soinAntiTachesMains = soinAntiTachesMains;
	}

	public String getSoinPieds() {
		return this.soinPieds;
	}

	public void setSoinPieds(String soinPieds) {
		this.soinPieds = soinPieds;
	}

	public String getSoinOngles() {
		return this.soinOngles;
	}

	public void setSoinOngles(String soinOngles) {
		this.soinOngles = soinOngles;
	}

	public String getManucures() {
		return this.manucures;
	}

	public void setManucures(String manucures) {
		this.manucures = manucures;
	}

	public String getCremeTeintee() {
		return this.cremeTeintee;
	}

	public void setCremeTeintee(String cremeTeintee) {
		this.cremeTeintee = cremeTeintee;
	}

	public String getBaseMaquillage() {
		return this.baseMaquillage;
	}

	public void setBaseMaquillage(String baseMaquillage) {
		this.baseMaquillage = baseMaquillage;
	}

	public String getCorrecteurTeint() {
		return this.correcteurTeint;
	}

	public void setCorrecteurTeint(String correcteurTeint) {
		this.correcteurTeint = correcteurTeint;
	}

	public String getAnticerne() {
		return this.anticerne;
	}

	public void setAnticerne(String anticerne) {
		this.anticerne = anticerne;
	}

	public String getFondDeTeint() {
		return this.fondDeTeint;
	}

	public void setFondDeTeint(String fondDeTeint) {
		this.fondDeTeint = fondDeTeint;
	}

	public String getPoudreLibre() {
		return this.poudreLibre;
	}

	public void setPoudreLibre(String poudreLibre) {
		this.poudreLibre = poudreLibre;
	}

	public String getBlushFardAJoues() {
		return this.blushFardAJoues;
	}

	public void setBlushFardAJoues(String blushFardAJoues) {
		this.blushFardAJoues = blushFardAJoues;
	}

	public String getMascara() {
		return this.mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public String getMascaraWaterproof() {
		return this.mascaraWaterproof;
	}

	public void setMascaraWaterproof(String mascaraWaterproof) {
		this.mascaraWaterproof = mascaraWaterproof;
	}

	public String getFardAPaupieres() {
		return this.fardAPaupieres;
	}

	public void setFardAPaupieres(String fardAPaupieres) {
		this.fardAPaupieres = fardAPaupieres;
	}

	public String getCrayonsYeux() {
		return this.crayonsYeux;
	}

	public void setCrayonsYeux(String crayonsYeux) {
		this.crayonsYeux = crayonsYeux;
	}

	public String getEyeliner() {
		return this.eyeliner;
	}

	public void setEyeliner(String eyeliner) {
		this.eyeliner = eyeliner;
	}

	public String getFauxCils() {
		return this.fauxCils;
	}

	public void setFauxCils(String fauxCils) {
		this.fauxCils = fauxCils;
	}

	public String getMaquillageDesSourcils() {
		return this.maquillageDesSourcils;
	}

	public void setMaquillageDesSourcils(String maquillageDesSourcils) {
		this.maquillageDesSourcils = maquillageDesSourcils;
	}

	public String getRougeALevres() {
		return this.rougeALevres;
	}

	public void setRougeALevres(String rougeALevres) {
		this.rougeALevres = rougeALevres;
	}

	public String getGloss() {
		return this.gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getCrayonLevres() {
		return this.crayonLevres;
	}

	public void setCrayonLevres(String crayonLevres) {
		this.crayonLevres = crayonLevres;
	}

	public String getVernisAOngles() {
		return this.vernisAOngles;
	}

	public void setVernisAOngles(String vernisAOngles) {
		this.vernisAOngles = vernisAOngles;
	}

	public String getFauxOngles() {
		return this.fauxOngles;
	}

	public void setFauxOngles(String fauxOngles) {
		this.fauxOngles = fauxOngles;
	}

	public String getMaquillagePermanentYeux() {
		return this.maquillagePermanentYeux;
	}

	public void setMaquillagePermanentYeux(String maquillagePermanentYeux) {
		this.maquillagePermanentYeux = maquillagePermanentYeux;
	}

	public String getMaquillagePermanentSourcils() {
		return this.maquillagePermanentSourcils;
	}

	public void setMaquillagePermanentSourcils(String maquillagePermanentSourcils) {
		this.maquillagePermanentSourcils = maquillagePermanentSourcils;
	}

	public String getMaquillagePermanentLevres() {
		return this.maquillagePermanentLevres;
	}

	public void setMaquillagePermanentLevres(String maquillagePermanentLevres) {
		this.maquillagePermanentLevres = maquillagePermanentLevres;
	}

	public String getShampoing() {
		return this.shampoing;
	}

	public void setShampoing(String shampoing) {
		this.shampoing = shampoing;
	}

	public String getApresShampoing() {
		return this.apresShampoing;
	}

	public void setApresShampoing(String apresShampoing) {
		this.apresShampoing = apresShampoing;
	}

	public String getMasqueCapillaire() {
		return this.masqueCapillaire;
	}

	public void setMasqueCapillaire(String masqueCapillaire) {
		this.masqueCapillaire = masqueCapillaire;
	}

	public String getColorationMeches() {
		return this.colorationMeches;
	}

	public void setColorationMeches(String colorationMeches) {
		this.colorationMeches = colorationMeches;
	}

	public String getProduitCoiffantFixant() {
		return this.produitCoiffantFixant;
	}

	public void setProduitCoiffantFixant(String produitCoiffantFixant) {
		this.produitCoiffantFixant = produitCoiffantFixant;
	}

	public String getPermanente() {
		return this.permanente;
	}

	public void setPermanente(String permanente) {
		this.permanente = permanente;
	}

	public String getLissageDefrisage() {
		return this.lissageDefrisage;
	}

	public void setLissageDefrisage(String lissageDefrisage) {
		this.lissageDefrisage = lissageDefrisage;
	}

	public String getExtensionsCapillaires() {
		return this.extensionsCapillaires;
	}

	public void setExtensionsCapillaires(String extensionsCapillaires) {
		this.extensionsCapillaires = extensionsCapillaires;
	}

	public String getProtecteurSolaireVisage() {
		return this.protecteurSolaireVisage;
	}

	public void setProtecteurSolaireVisage(String protecteurSolaireVisage) {
		this.protecteurSolaireVisage = protecteurSolaireVisage;
	}

	public String getProtecteurSolaireCorps() {
		return this.protecteurSolaireCorps;
	}

	public void setProtecteurSolaireCorps(String protecteurSolaireCorps) {
		this.protecteurSolaireCorps = protecteurSolaireCorps;
	}

	public String getProtecteurSolaireLevres() {
		return this.protecteurSolaireLevres;
	}

	public void setProtecteurSolaireLevres(String protecteurSolaireLevres) {
		this.protecteurSolaireLevres = protecteurSolaireLevres;
	}

	public String getSoinApresSoleil() {
		return this.soinApresSoleil;
	}

	public void setSoinApresSoleil(String soinApresSoleil) {
		this.soinApresSoleil = soinApresSoleil;
	}

	public String getAutobronzant() {
		return this.autobronzant;
	}

	public void setAutobronzant(String autobronzant) {
		this.autobronzant = autobronzant;
	}

}
