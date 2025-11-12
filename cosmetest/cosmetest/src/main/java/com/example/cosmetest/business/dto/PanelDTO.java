package com.example.cosmetest.business.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) pour l'entité Panel
 * Note: Seuls les attributs principaux sont validés explicitement
 */
public class PanelDTO {

    private Integer idPanel;

    @NotNull(message = "L'ID d'étude ne peut pas être null")
    @Min(value = 1, message = "L'ID d'étude doit être un nombre positif")
    private Integer idEtude;

    @NotNull(message = "L'ID de groupe ne peut pas être null")
    @Min(value = 1, message = "L'ID de groupe doit être un nombre positif")
    private Integer idGroupe;

    private String sexe;
    private String carnation;
    private String bronzage;
    private String coupsDeSoleil;
    private String expositionSolaire;
    private String phototype;
    private String sensibiliteCutanee;
    private String typePeauVisage;

    // Attributs de sécheresse
    private String secheresseLevres;
    private String secheresseCou;
    private String secheressePoitrineDecollete;
    private String secheresseVentreTaille;
    private String secheresseFessesHanches;
    private String secheresseBras;
    private String secheresseMains;
    private String secheresseJambes;
    private String secheressePieds;

    // Attributs de taches pigmentaires
    private String tachesPigmentairesVisage;
    private String tachesPigmentairesCou;
    private String tachesPigmentairesDecollete;
    private String tachesPigmentairesMains;

    // Attributs de fermeté
    private String perteDeFermeteVisage;
    private String perteDeFermeteCou;
    private String perteDeFermeteDecollete;

    // Caractéristiques physiques diverses
    private String pilosite;
    private String cicatrices;
    private String tatouages;
    private String piercings;

    // Attributs de vergetures
    private String vergeturesJambes;
    private String vergeturesFessesHanches;
    private String vergeturesVentreTaille;
    private String vergeturesPoitrineDecollete;

    // Attributs de cellulite
    private String celluliteJambes;
    private String celluliteFessesHanches;
    private String celluliteVentreTaille;
    private String celluliteBras;

    // Attributs capillaires
    private String couleurCheveux;
    private String natureCheveux;
    private String longueurCheveux;
    private String epaisseurCheveux;
    private String natureCuirChevelu;
    private String cheveuxAbimes;
    private String cheveuxCassants;
    private String cheveuxPlats;
    private String cheveuxTernes;
    private String pointesFourchues;
    private String pellicules;
    private String demangeaisonsDuCuirChevelu;
    private String cuirCheveluSensible;
    private String chuteDeCheveux;
    private String calvitie;

    // Attributs des cils
    private String epaisseurCils;
    private String longueurCils;
    private String courbureCils;
    private String cilsAbimes;
    private String cilsBroussailleux;
    private String chuteDeCils;

    // Attributs des ongles
    private String onglesMous;
    private String onglesCassants;
    private String onglesStries;
    private String onglesDedoubles;

    // Attributs cutanés
    private String lesionsRetentionnelles;
    private String lesionsInflammatoires;
    private String cernesPigmentaires;
    private String poches;
    private String poresVisibles;
    private String teintInhomogene;
    private String teintTerne;

    // Attributs hormonaux et médicaux
    private String menopause;
    private String ths;
    private String contraception;

    // Attributs de pathologies
    private String acne;
    private String couperoseRosacee;
    private String psoriasis;
    private String dermiteSeborrheique;
    private String eczema;
    private String angiome;
    private String pityriasis;
    private String vitiligo;
    private String melanome;
    private String zona;
    private String herpes;
    private String pelade;
    private String reactionAllergique;
    private String desensibilisation;
    private String terrainAtopique;

    // Attributs des scores
    private String scorePodMin;
    private String scorePogMin;
    private String scoreFrontMin;
    private String scoreLionMin;
    private String scorePpdMin;
    private String scorePpgMin;
    private String scoreDodMin;
    private String scoreDogMin;
    private String scoreSngdMin;
    private String scoreSnggMin;
    private String scoreLevsupMin;
    private String scoreComlevdMin;
    private String scoreComlevgMin;
    private String scorePtoseMin;
    private String scorePodMax;
    private String scorePogMax;
    private String scoreFrontMax;
    private String scoreLionMax;
    private String scorePpdMax;
    private String scorePpgMax;
    private String scoreDodMax;
    private String scoreDogMax;
    private String scoreSngdMax;
    private String scoreSnggMax;
    private String scoreLevsupMax;
    private String scoreComlevdMax;
    private String scoreComlevgMax;
    private String scorePtoseMax;

    // Attributs d'origine
    private String originePere;
    private String origineMere;
    private String sousEthnie;
    private String bouffeeChaleurMenaupose;

    // Attributs visuels
    private String yeux;
    private String levres;
    private String cernesVasculaires;
    private String mapyeux;
    private String maplevres;
    private String mapsourcils;

    // Constructeur par défaut
    public PanelDTO() {
    }

    // Constructeur minimal avec les champs obligatoires
    public PanelDTO(Integer idEtude, Integer idGroupe) {
        this.idEtude = idEtude;
        this.idGroupe = idGroupe;
    }

    // Getters et Setters pour tous les attributs
    public Integer getIdPanel() {
        return idPanel;
    }

    public void setIdPanel(Integer idPanel) {
        this.idPanel = idPanel;
    }

    public Integer getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(Integer idEtude) {
        this.idEtude = idEtude;
    }

    public Integer getIdGroupe() {
        return idGroupe;
    }

    public void setIdGroupe(Integer idGroupe) {
        this.idGroupe = idGroupe;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getCarnation() {
        return carnation;
    }

    public void setCarnation(String carnation) {
        this.carnation = carnation;
    }

    public String getBronzage() {
        return bronzage;
    }

    public void setBronzage(String bronzage) {
        this.bronzage = bronzage;
    }

    public String getCoupsDeSoleil() {
        return coupsDeSoleil;
    }

    public void setCoupsDeSoleil(String coupsDeSoleil) {
        this.coupsDeSoleil = coupsDeSoleil;
    }

    public String getExpositionSolaire() {
        return expositionSolaire;
    }

    public void setExpositionSolaire(String expositionSolaire) {
        this.expositionSolaire = expositionSolaire;
    }

    public String getPhototype() {
        return phototype;
    }

    public void setPhototype(String phototype) {
        this.phototype = phototype;
    }

    public String getSensibiliteCutanee() {
        return sensibiliteCutanee;
    }

    public void setSensibiliteCutanee(String sensibiliteCutanee) {
        this.sensibiliteCutanee = sensibiliteCutanee;
    }

    public String getTypePeauVisage() {
        return typePeauVisage;
    }

    public void setTypePeauVisage(String typePeauVisage) {
        this.typePeauVisage = typePeauVisage;
    }

    public String getSecheresseLevres() {
        return secheresseLevres;
    }

    public void setSecheresseLevres(String secheresseLevres) {
        this.secheresseLevres = secheresseLevres;
    }

    public String getSecheresseCou() {
        return secheresseCou;
    }

    public void setSecheresseCou(String secheresseCou) {
        this.secheresseCou = secheresseCou;
    }

    public String getSecheressePoitrineDecollete() {
        return secheressePoitrineDecollete;
    }

    public void setSecheressePoitrineDecollete(String secheressePoitrineDecollete) {
        this.secheressePoitrineDecollete = secheressePoitrineDecollete;
    }

    public String getSecheresseVentreTaille() {
        return secheresseVentreTaille;
    }

    public void setSecheresseVentreTaille(String secheresseVentreTaille) {
        this.secheresseVentreTaille = secheresseVentreTaille;
    }

    public String getSecheresseFessesHanches() {
        return secheresseFessesHanches;
    }

    public void setSecheresseFessesHanches(String secheresseFessesHanches) {
        this.secheresseFessesHanches = secheresseFessesHanches;
    }

    public String getSecheresseBras() {
        return secheresseBras;
    }

    public void setSecheresseBras(String secheresseBras) {
        this.secheresseBras = secheresseBras;
    }

    public String getSecheresseMains() {
        return secheresseMains;
    }

    public void setSecheresseMains(String secheresseMains) {
        this.secheresseMains = secheresseMains;
    }

    public String getSecheresseJambes() {
        return secheresseJambes;
    }

    public void setSecheresseJambes(String secheresseJambes) {
        this.secheresseJambes = secheresseJambes;
    }

    public String getSecheressePieds() {
        return secheressePieds;
    }

    public void setSecheressePieds(String secheressePieds) {
        this.secheressePieds = secheressePieds;
    }

    public String getTachesPigmentairesVisage() {
        return tachesPigmentairesVisage;
    }

    public void setTachesPigmentairesVisage(String tachesPigmentairesVisage) {
        this.tachesPigmentairesVisage = tachesPigmentairesVisage;
    }

    public String getTachesPigmentairesCou() {
        return tachesPigmentairesCou;
    }

    public void setTachesPigmentairesCou(String tachesPigmentairesCou) {
        this.tachesPigmentairesCou = tachesPigmentairesCou;
    }

    public String getTachesPigmentairesDecollete() {
        return tachesPigmentairesDecollete;
    }

    public void setTachesPigmentairesDecollete(String tachesPigmentairesDecollete) {
        this.tachesPigmentairesDecollete = tachesPigmentairesDecollete;
    }

    public String getTachesPigmentairesMains() {
        return tachesPigmentairesMains;
    }

    public void setTachesPigmentairesMains(String tachesPigmentairesMains) {
        this.tachesPigmentairesMains = tachesPigmentairesMains;
    }

    public String getPerteDeFermeteVisage() {
        return perteDeFermeteVisage;
    }

    public void setPerteDeFermeteVisage(String perteDeFermeteVisage) {
        this.perteDeFermeteVisage = perteDeFermeteVisage;
    }

    public String getPerteDeFermeteCou() {
        return perteDeFermeteCou;
    }

    public void setPerteDeFermeteCou(String perteDeFermeteCou) {
        this.perteDeFermeteCou = perteDeFermeteCou;
    }

    public String getPerteDeFermeteDecollete() {
        return perteDeFermeteDecollete;
    }

    public void setPerteDeFermeteDecollete(String perteDeFermeteDecollete) {
        this.perteDeFermeteDecollete = perteDeFermeteDecollete;
    }

    public String getPilosite() {
        return pilosite;
    }

    public void setPilosite(String pilosite) {
        this.pilosite = pilosite;
    }

    public String getCicatrices() {
        return cicatrices;
    }

    public void setCicatrices(String cicatrices) {
        this.cicatrices = cicatrices;
    }

    public String getTatouages() {
        return tatouages;
    }

    public void setTatouages(String tatouages) {
        this.tatouages = tatouages;
    }

    public String getPiercings() {
        return piercings;
    }

    public void setPiercings(String piercings) {
        this.piercings = piercings;
    }

    public String getVergeturesJambes() {
        return vergeturesJambes;
    }

    public void setVergeturesJambes(String vergeturesJambes) {
        this.vergeturesJambes = vergeturesJambes;
    }

    public String getVergeturesFessesHanches() {
        return vergeturesFessesHanches;
    }

    public void setVergeturesFessesHanches(String vergeturesFessesHanches) {
        this.vergeturesFessesHanches = vergeturesFessesHanches;
    }

    public String getVergeturesVentreTaille() {
        return vergeturesVentreTaille;
    }

    public void setVergeturesVentreTaille(String vergeturesVentreTaille) {
        this.vergeturesVentreTaille = vergeturesVentreTaille;
    }

    public String getVergeturesPoitrineDecollete() {
        return vergeturesPoitrineDecollete;
    }

    public void setVergeturesPoitrineDecollete(String vergeturesPoitrineDecollete) {
        this.vergeturesPoitrineDecollete = vergeturesPoitrineDecollete;
    }

    public String getCelluliteJambes() {
        return celluliteJambes;
    }

    public void setCelluliteJambes(String celluliteJambes) {
        this.celluliteJambes = celluliteJambes;
    }

    public String getCelluliteFessesHanches() {
        return celluliteFessesHanches;
    }

    public void setCelluliteFessesHanches(String celluliteFessesHanches) {
        this.celluliteFessesHanches = celluliteFessesHanches;
    }

    public String getCelluliteVentreTaille() {
        return celluliteVentreTaille;
    }

    public void setCelluliteVentreTaille(String celluliteVentreTaille) {
        this.celluliteVentreTaille = celluliteVentreTaille;
    }

    public String getCelluliteBras() {
        return celluliteBras;
    }

    public void setCelluliteBras(String celluliteBras) {
        this.celluliteBras = celluliteBras;
    }

    public String getCouleurCheveux() {
        return couleurCheveux;
    }

    public void setCouleurCheveux(String couleurCheveux) {
        this.couleurCheveux = couleurCheveux;
    }

    public String getNatureCheveux() {
        return natureCheveux;
    }

    public void setNatureCheveux(String natureCheveux) {
        this.natureCheveux = natureCheveux;
    }

    public String getLongueurCheveux() {
        return longueurCheveux;
    }

    public void setLongueurCheveux(String longueurCheveux) {
        this.longueurCheveux = longueurCheveux;
    }

    public String getEpaisseurCheveux() {
        return epaisseurCheveux;
    }

    public void setEpaisseurCheveux(String epaisseurCheveux) {
        this.epaisseurCheveux = epaisseurCheveux;
    }

    public String getNatureCuirChevelu() {
        return natureCuirChevelu;
    }

    public void setNatureCuirChevelu(String natureCuirChevelu) {
        this.natureCuirChevelu = natureCuirChevelu;
    }

    public String getCheveuxAbimes() {
        return cheveuxAbimes;
    }

    public void setCheveuxAbimes(String cheveuxAbimes) {
        this.cheveuxAbimes = cheveuxAbimes;
    }

    public String getCheveuxCassants() {
        return cheveuxCassants;
    }

    public void setCheveuxCassants(String cheveuxCassants) {
        this.cheveuxCassants = cheveuxCassants;
    }

    public String getCheveuxPlats() {
        return cheveuxPlats;
    }

    public void setCheveuxPlats(String cheveuxPlats) {
        this.cheveuxPlats = cheveuxPlats;
    }

    public String getCheveuxTernes() {
        return cheveuxTernes;
    }

    public void setCheveuxTernes(String cheveuxTernes) {
        this.cheveuxTernes = cheveuxTernes;
    }

    public String getPointesFourchues() {
        return pointesFourchues;
    }

    public void setPointesFourchues(String pointesFourchues) {
        this.pointesFourchues = pointesFourchues;
    }

    public String getPellicules() {
        return pellicules;
    }

    public void setPellicules(String pellicules) {
        this.pellicules = pellicules;
    }

    public String getDemangeaisonsDuCuirChevelu() {
        return demangeaisonsDuCuirChevelu;
    }

    public void setDemangeaisonsDuCuirChevelu(String demangeaisonsDuCuirChevelu) {
        this.demangeaisonsDuCuirChevelu = demangeaisonsDuCuirChevelu;
    }

    public String getCuirCheveluSensible() {
        return cuirCheveluSensible;
    }

    public void setCuirCheveluSensible(String cuirCheveluSensible) {
        this.cuirCheveluSensible = cuirCheveluSensible;
    }

    public String getChuteDeCheveux() {
        return chuteDeCheveux;
    }

    public void setChuteDeCheveux(String chuteDeCheveux) {
        this.chuteDeCheveux = chuteDeCheveux;
    }

    public String getCalvitie() {
        return calvitie;
    }

    public void setCalvitie(String calvitie) {
        this.calvitie = calvitie;
    }

    public String getEpaisseurCils() {
        return epaisseurCils;
    }

    public void setEpaisseurCils(String epaisseurCils) {
        this.epaisseurCils = epaisseurCils;
    }

    public String getLongueurCils() {
        return longueurCils;
    }

    public void setLongueurCils(String longueurCils) {
        this.longueurCils = longueurCils;
    }

    public String getCourbureCils() {
        return courbureCils;
    }

    public void setCourbureCils(String courbureCils) {
        this.courbureCils = courbureCils;
    }

    public String getCilsAbimes() {
        return cilsAbimes;
    }

    public void setCilsAbimes(String cilsAbimes) {
        this.cilsAbimes = cilsAbimes;
    }

    public String getCilsBroussailleux() {
        return cilsBroussailleux;
    }

    public void setCilsBroussailleux(String cilsBroussailleux) {
        this.cilsBroussailleux = cilsBroussailleux;
    }

    public String getChuteDeCils() {
        return chuteDeCils;
    }

    public void setChuteDeCils(String chuteDeCils) {
        this.chuteDeCils = chuteDeCils;
    }

    public String getOnglesMous() {
        return onglesMous;
    }

    public void setOnglesMous(String onglesMous) {
        this.onglesMous = onglesMous;
    }

    public String getOnglesCassants() {
        return onglesCassants;
    }

    public void setOnglesCassants(String onglesCassants) {
        this.onglesCassants = onglesCassants;
    }

    public String getOnglesStries() {
        return onglesStries;
    }

    public void setOnglesStries(String onglesStries) {
        this.onglesStries = onglesStries;
    }

    public String getOnglesDedoubles() {
        return onglesDedoubles;
    }

    public void setOnglesDedoubles(String onglesDedoubles) {
        this.onglesDedoubles = onglesDedoubles;
    }

    public String getLesionsRetentionnelles() {
        return lesionsRetentionnelles;
    }

    public void setLesionsRetentionnelles(String lesionsRetentionnelles) {
        this.lesionsRetentionnelles = lesionsRetentionnelles;
    }

    public String getLesionsInflammatoires() {
        return lesionsInflammatoires;
    }

    public void setLesionsInflammatoires(String lesionsInflammatoires) {
        this.lesionsInflammatoires = lesionsInflammatoires;
    }

    public String getCernesPigmentaires() {
        return cernesPigmentaires;
    }

    public void setCernesPigmentaires(String cernesPigmentaires) {
        this.cernesPigmentaires = cernesPigmentaires;
    }

    public String getPoches() {
        return poches;
    }

    public void setPoches(String poches) {
        this.poches = poches;
    }

    public String getPoresVisibles() {
        return poresVisibles;
    }

    public void setPoresVisibles(String poresVisibles) {
        this.poresVisibles = poresVisibles;
    }

    public String getTeintInhomogene() {
        return teintInhomogene;
    }

    public void setTeintInhomogene(String teintInhomogene) {
        this.teintInhomogene = teintInhomogene;
    }

    public String getTeintTerne() {
        return teintTerne;
    }

    public void setTeintTerne(String teintTerne) {
        this.teintTerne = teintTerne;
    }

    public String getMenopause() {
        return menopause;
    }

    public void setMenopause(String menopause) {
        this.menopause = menopause;
    }

    public String getThs() {
        return ths;
    }

    public void setThs(String ths) {
        this.ths = ths;
    }

    public String getContraception() {
        return contraception;
    }

    public void setContraception(String contraception) {
        this.contraception = contraception;
    }

    public String getAcne() {
        return acne;
    }

    public void setAcne(String acne) {
        this.acne = acne;
    }

    public String getCouperoseRosacee() {
        return couperoseRosacee;
    }

    public void setCouperoseRosacee(String couperoseRosacee) {
        this.couperoseRosacee = couperoseRosacee;
    }

    public String getPsoriasis() {
        return psoriasis;
    }

    public void setPsoriasis(String psoriasis) {
        this.psoriasis = psoriasis;
    }

    public String getDermiteSeborrheique() {
        return dermiteSeborrheique;
    }

    public void setDermiteSeborrheique(String dermiteSeborrheique) {
        this.dermiteSeborrheique = dermiteSeborrheique;
    }

    public String getEczema() {
        return eczema;
    }

    public void setEczema(String eczema) {
        this.eczema = eczema;
    }

    public String getAngiome() {
        return angiome;
    }

    public void setAngiome(String angiome) {
        this.angiome = angiome;
    }

    public String getPityriasis() {
        return pityriasis;
    }

    public void setPityriasis(String pityriasis) {
        this.pityriasis = pityriasis;
    }

    public String getVitiligo() {
        return vitiligo;
    }

    public void setVitiligo(String vitiligo) {
        this.vitiligo = vitiligo;
    }

    public String getMelanome() {
        return melanome;
    }

    public void setMelanome(String melanome) {
        this.melanome = melanome;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getHerpes() {
        return herpes;
    }

    public void setHerpes(String herpes) {
        this.herpes = herpes;
    }

    public String getPelade() {
        return pelade;
    }

    public void setPelade(String pelade) {
        this.pelade = pelade;
    }

    public String getReactionAllergique() {
        return reactionAllergique;
    }

    public void setReactionAllergique(String reactionAllergique) {
        this.reactionAllergique = reactionAllergique;
    }

    public String getDesensibilisation() {
        return desensibilisation;
    }

    public void setDesensibilisation(String desensibilisation) {
        this.desensibilisation = desensibilisation;
    }

    public String getTerrainAtopique() {
        return terrainAtopique;
    }

    public void setTerrainAtopique(String terrainAtopique) {
        this.terrainAtopique = terrainAtopique;
    }

    public String getScorePodMin() {
        return scorePodMin;
    }

    public void setScorePodMin(String scorePodMin) {
        this.scorePodMin = scorePodMin;
    }

    public String getScorePogMin() {
        return scorePogMin;
    }

    public void setScorePogMin(String scorePogMin) {
        this.scorePogMin = scorePogMin;
    }

    public String getScoreFrontMin() {
        return scoreFrontMin;
    }

    public void setScoreFrontMin(String scoreFrontMin) {
        this.scoreFrontMin = scoreFrontMin;
    }

    public String getScoreLionMin() {
        return scoreLionMin;
    }

    public void setScoreLionMin(String scoreLionMin) {
        this.scoreLionMin = scoreLionMin;
    }

    public String getScorePpdMin() {
        return scorePpdMin;
    }

    public void setScorePpdMin(String scorePpdMin) {
        this.scorePpdMin = scorePpdMin;
    }

    public String getScorePpgMin() {
        return scorePpgMin;
    }

    public void setScorePpgMin(String scorePpgMin) {
        this.scorePpgMin = scorePpgMin;
    }

    public String getScoreDodMin() {
        return scoreDodMin;
    }

    public void setScoreDodMin(String scoreDodMin) {
        this.scoreDodMin = scoreDodMin;
    }

    public String getScoreDogMin() {
        return scoreDogMin;
    }

    public void setScoreDogMin(String scoreDogMin) {
        this.scoreDogMin = scoreDogMin;
    }

    public String getScoreSngdMin() {
        return scoreSngdMin;
    }

    public void setScoreSngdMin(String scoreSngdMin) {
        this.scoreSngdMin = scoreSngdMin;
    }

    public String getScoreSnggMin() {
        return scoreSnggMin;
    }

    public void setScoreSnggMin(String scoreSnggMin) {
        this.scoreSnggMin = scoreSnggMin;
    }

    public String getScoreLevsupMin() {
        return scoreLevsupMin;
    }

    public void setScoreLevsupMin(String scoreLevsupMin) {
        this.scoreLevsupMin = scoreLevsupMin;
    }

    public String getScoreComlevdMin() {
        return scoreComlevdMin;
    }

    public void setScoreComlevdMin(String scoreComlevdMin) {
        this.scoreComlevdMin = scoreComlevdMin;
    }

    public String getScoreComlevgMin() {
        return scoreComlevgMin;
    }

    public void setScoreComlevgMin(String scoreComlevgMin) {
        this.scoreComlevgMin = scoreComlevgMin;
    }

    public String getScorePtoseMin() {
        return scorePtoseMin;
    }

    public void setScorePtoseMin(String scorePtoseMin) {
        this.scorePtoseMin = scorePtoseMin;
    }

    public String getScorePodMax() {
        return scorePodMax;
    }

    public void setScorePodMax(String scorePodMax) {
        this.scorePodMax = scorePodMax;
    }

    public String getScorePogMax() {
        return scorePogMax;
    }

    public void setScorePogMax(String scorePogMax) {
        this.scorePogMax = scorePogMax;
    }

    public String getScoreFrontMax() {
        return scoreFrontMax;
    }

    public void setScoreFrontMax(String scoreFrontMax) {
        this.scoreFrontMax = scoreFrontMax;
    }

    public String getScoreLionMax() {
        return scoreLionMax;
    }

    public void setScoreLionMax(String scoreLionMax) {
        this.scoreLionMax = scoreLionMax;
    }

    public String getScorePpdMax() {
        return scorePpdMax;
    }

    public void setScorePpdMax(String scorePpdMax) {
        this.scorePpdMax = scorePpdMax;
    }

    public String getScorePpgMax() {
        return scorePpgMax;
    }

    public void setScorePpgMax(String scorePpgMax) {
        this.scorePpgMax = scorePpgMax;
    }

    public String getScoreDodMax() {
        return scoreDodMax;
    }

    public void setScoreDodMax(String scoreDodMax) {
        this.scoreDodMax = scoreDodMax;
    }

    public String getScoreDogMax() {
        return scoreDogMax;
    }

    public void setScoreDogMax(String scoreDogMax) {
        this.scoreDogMax = scoreDogMax;
    }

    public String getScoreSngdMax() {
        return scoreSngdMax;
    }

    public void setScoreSngdMax(String scoreSngdMax) {
        this.scoreSngdMax = scoreSngdMax;
    }

    public String getScoreSnggMax() {
        return scoreSnggMax;
    }

    public void setScoreSnggMax(String scoreSnggMax) {
        this.scoreSnggMax = scoreSnggMax;
    }

    public String getScoreLevsupMax() {
        return scoreLevsupMax;
    }

    public void setScoreLevsupMax(String scoreLevsupMax) {
        this.scoreLevsupMax = scoreLevsupMax;
    }

    public String getScoreComlevdMax() {
        return scoreComlevdMax;
    }

    public void setScoreComlevdMax(String scoreComlevdMax) {
        this.scoreComlevdMax = scoreComlevdMax;
    }

    public String getScoreComlevgMax() {
        return scoreComlevgMax;
    }

    public void setScoreComlevgMax(String scoreComlevgMax) {
        this.scoreComlevgMax = scoreComlevgMax;
    }

    public String getScorePtoseMax() {
        return scorePtoseMax;
    }

    public void setScorePtoseMax(String scorePtoseMax) {
        this.scorePtoseMax = scorePtoseMax;
    }

    public String getOriginePere() {
        return originePere;
    }

    public void setOriginePere(String originePere) {
        this.originePere = originePere;
    }

    public String getOrigineMere() {
        return origineMere;
    }

    public void setOrigineMere(String origineMere) {
        this.origineMere = origineMere;
    }

    public String getSousEthnie() {
        return sousEthnie;
    }

    public void setSousEthnie(String sousEthnie) {
        this.sousEthnie = sousEthnie;
    }

    public String getBouffeeChaleurMenaupose() {
        return bouffeeChaleurMenaupose;
    }

    public void setBouffeeChaleurMenaupose(String bouffeeChaleurMenaupose) {
        this.bouffeeChaleurMenaupose = bouffeeChaleurMenaupose;
    }

    public String getYeux() {
        return yeux;
    }

    public void setYeux(String yeux) {
        this.yeux = yeux;
    }

    public String getLevres() {
        return levres;
    }

    public void setLevres(String levres) {
        this.levres = levres;
    }

    public String getCernesVasculaires() {
        return cernesVasculaires;
    }

    public void setCernesVasculaires(String cernesVasculaires) {
        this.cernesVasculaires = cernesVasculaires;
    }

    public String getMapyeux() {
        return mapyeux;
    }

    public void setMapyeux(String mapyeux) {
        this.mapyeux = mapyeux;
    }

    public String getMaplevres() {
        return maplevres;
    }

    public void setMaplevres(String maplevres) {
        this.maplevres = maplevres;
    }

    public String getMapsourcils() {
        return mapsourcils;
    }

    public void setMapsourcils(String mapsourcils) {
        this.mapsourcils = mapsourcils;
    }

    // Ajoutez ici tous les autres getters et setters pour les attributs...
    // Pour des raisons de concision, tous les getters et setters ne sont pas inclus ici

    // Méthode toString pour faciliter le débogage

    @Override
    public String toString() {
        return "PanelDTO{" +
                "idPanel=" + idPanel +
                ", idEtude=" + idEtude +
                ", idGroupe=" + idGroupe +
                ", sexe='" + sexe + '\'' +
                ", carnation='" + carnation + '\'' +
                ", bronzage='" + bronzage + '\'' +
                ", coupsDeSoleil='" + coupsDeSoleil + '\'' +
                ", expositionSolaire='" + expositionSolaire + '\'' +
                ", phototype='" + phototype + '\'' +
                ", sensibiliteCutanee='" + sensibiliteCutanee + '\'' +
                ", typePeauVisage='" + typePeauVisage + '\'' +
                ", secheresseLevres='" + secheresseLevres + '\'' +
                ", secheresseCou='" + secheresseCou + '\'' +
                ", secheressePoitrineDecollete='" + secheressePoitrineDecollete + '\'' +
                ", secheresseVentreTaille='" + secheresseVentreTaille + '\'' +
                ", secheresseFessesHanches='" + secheresseFessesHanches + '\'' +
                ", secheresseBras='" + secheresseBras + '\'' +
                ", secheresseMains='" + secheresseMains + '\'' +
                ", secheresseJambes='" + secheresseJambes + '\'' +
                ", secheressePieds='" + secheressePieds + '\'' +
                ", tachesPigmentairesVisage='" + tachesPigmentairesVisage + '\'' +
                ", tachesPigmentairesCou='" + tachesPigmentairesCou + '\'' +
                ", tachesPigmentairesDecollete='" + tachesPigmentairesDecollete + '\'' +
                ", tachesPigmentairesMains='" + tachesPigmentairesMains + '\'' +
                ", perteDeFermeteVisage='" + perteDeFermeteVisage + '\'' +
                ", perteDeFermeteCou='" + perteDeFermeteCou + '\'' +
                ", perteDeFermeteDecollete='" + perteDeFermeteDecollete + '\'' +
                ", pilosite='" + pilosite + '\'' +
                ", cicatrices='" + cicatrices + '\'' +
                ", tatouages='" + tatouages + '\'' +
                ", piercings='" + piercings + '\'' +
                ", vergeturesJambes='" + vergeturesJambes + '\'' +
                ", vergeturesFessesHanches='" + vergeturesFessesHanches + '\'' +
                ", vergeturesVentreTaille='" + vergeturesVentreTaille + '\'' +
                ", vergeturesPoitrineDecollete='" + vergeturesPoitrineDecollete + '\'' +
                ", celluliteJambes='" + celluliteJambes + '\'' +
                ", celluliteFessesHanches='" + celluliteFessesHanches + '\'' +
                ", celluliteVentreTaille='" + celluliteVentreTaille + '\'' +
                ", celluliteBras='" + celluliteBras + '\'' +
                ", couleurCheveux='" + couleurCheveux + '\'' +
                ", natureCheveux='" + natureCheveux + '\'' +
                ", longueurCheveux='" + longueurCheveux + '\'' +
                ", epaisseurCheveux='" + epaisseurCheveux + '\'' +
                ", natureCuirChevelu='" + natureCuirChevelu + '\'' +
                ", cheveuxAbimes='" + cheveuxAbimes + '\'' +
                ", cheveuxCassants='" + cheveuxCassants + '\'' +
                ", cheveuxPlats='" + cheveuxPlats + '\'' +
                ", cheveuxTernes='" + cheveuxTernes + '\'' +
                ", pointesFourchues='" + pointesFourchues + '\'' +
                ", pellicules='" + pellicules + '\'' +
                ", demangeaisonsDuCuirChevelu='" + demangeaisonsDuCuirChevelu + '\'' +
                ", cuirCheveluSensible='" + cuirCheveluSensible + '\'' +
                ", chuteDeCheveux='" + chuteDeCheveux + '\'' +
                ", calvitie='" + calvitie + '\'' +
                ", epaisseurCils='" + epaisseurCils + '\'' +
                ", longueurCils='" + longueurCils + '\'' +
                ", courbureCils='" + courbureCils + '\'' +
                ", cilsAbimes='" + cilsAbimes + '\'' +
                ", cilsBroussailleux='" + cilsBroussailleux + '\'' +
                ", chuteDeCils='" + chuteDeCils + '\'' +
                ", onglesMous='" + onglesMous + '\'' +
                ", onglesCassants='" + onglesCassants + '\'' +
                ", onglesStries='" + onglesStries + '\'' +
                ", onglesDedoubles='" + onglesDedoubles + '\'' +
                ", lesionsRetentionnelles='" + lesionsRetentionnelles + '\'' +
                ", lesionsInflammatoires='" + lesionsInflammatoires + '\'' +
                ", cernesPigmentaires='" + cernesPigmentaires + '\'' +
                ", poches='" + poches + '\'' +
                ", poresVisibles='" + poresVisibles + '\'' +
                ", teintInhomogene='" + teintInhomogene + '\'' +
                ", teintTerne='" + teintTerne + '\'' +
                ", menopause='" + menopause + '\'' +
                ", ths='" + ths + '\'' +
                ", contraception='" + contraception + '\'' +
                ", acne='" + acne + '\'' +
                ", couperoseRosacee='" + couperoseRosacee + '\'' +
                ", psoriasis='" + psoriasis + '\'' +
                ", dermiteSeborrheique='" + dermiteSeborrheique + '\'' +
                ", eczema='" + eczema + '\'' +
                ", angiome='" + angiome + '\'' +
                ", pityriasis='" + pityriasis + '\'' +
                ", vitiligo='" + vitiligo + '\'' +
                ", melanome='" + melanome + '\'' +
                ", zona='" + zona + '\'' +
                ", herpes='" + herpes + '\'' +
                ", pelade='" + pelade + '\'' +
                ", reactionAllergique='" + reactionAllergique + '\'' +
                ", desensibilisation='" + desensibilisation + '\'' +
                ", terrainAtopique='" + terrainAtopique + '\'' +
                ", scorePodMin='" + scorePodMin + '\'' +
                ", scorePogMin='" + scorePogMin + '\'' +
                ", scoreFrontMin='" + scoreFrontMin + '\'' +
                ", scoreLionMin='" + scoreLionMin + '\'' +
                ", scorePpdMin='" + scorePpdMin + '\'' +
                ", scorePpgMin='" + scorePpgMin + '\'' +
                ", scoreDodMin='" + scoreDodMin + '\'' +
                ", scoreDogMin='" + scoreDogMin + '\'' +
                ", scoreSngdMin='" + scoreSngdMin + '\'' +
                ", scoreSnggMin='" + scoreSnggMin + '\'' +
                ", scoreLevsupMin='" + scoreLevsupMin + '\'' +
                ", scoreComlevdMin='" + scoreComlevdMin + '\'' +
                ", scoreComlevgMin='" + scoreComlevgMin + '\'' +
                ", scorePtoseMin='" + scorePtoseMin + '\'' +
                ", scorePodMax='" + scorePodMax + '\'' +
                ", scorePogMax='" + scorePogMax + '\'' +
                ", scoreFrontMax='" + scoreFrontMax + '\'' +
                ", scoreLionMax='" + scoreLionMax + '\'' +
                ", scorePpdMax='" + scorePpdMax + '\'' +
                ", scorePpgMax='" + scorePpgMax + '\'' +
                ", scoreDodMax='" + scoreDodMax + '\'' +
                ", scoreDogMax='" + scoreDogMax + '\'' +
                ", scoreSngdMax='" + scoreSngdMax + '\'' +
                ", scoreSnggMax='" + scoreSnggMax + '\'' +
                ", scoreLevsupMax='" + scoreLevsupMax + '\'' +
                ", scoreComlevdMax='" + scoreComlevdMax + '\'' +
                ", scoreComlevgMax='" + scoreComlevgMax + '\'' +
                ", scorePtoseMax='" + scorePtoseMax + '\'' +
                ", originePere='" + originePere + '\'' +
                ", origineMere='" + origineMere + '\'' +
                ", sousEthnie='" + sousEthnie + '\'' +
                ", bouffeeChaleurMenaupose='" + bouffeeChaleurMenaupose + '\'' +
                ", yeux='" + yeux + '\'' +
                ", levres='" + levres + '\'' +
                ", cernesVasculaires='" + cernesVasculaires + '\'' +
                ", mapyeux='" + mapyeux + '\'' +
                ", maplevres='" + maplevres + '\'' +
                ", mapsourcils='" + mapsourcils + '\'' +
                '}';
    }
}