package com.example.cosmetest.business.dto;

/**
 * DTO détaillé pour l'entité Volontaire
 * Contient tous les champs de l'entité pour les opérations nécessitant des
 * informations complètes
 */
public class VolontaireDetailDTO extends VolontaireDTO {

    // Informations sur la peau
    private String carnation;
    private String bronzage;
    private String coupsDeSoleil;
    private String expositionSolaire;
    private String sensibiliteCutanee;

    // Zones de sécheresse
    private String secheresseLevres;
    private String secheresseCou;
    private String secheressePoitrineDecollete;
    private String secheresseVentreTaille;
    private String secheresseFessesHanches;
    private String secheresseBras;
    private String secheresseMains;
    private String secheresseJambes;
    private String secheressePieds;

    // Taches pigmentaires
    private String tachesPigmentairesVisage;
    private String tachesPigmentairesCou;
    private String tachesPigmentairesDecollete;
    private String tachesPigmentairesMains;

    // Perte de fermeté
    private String perteDeFermeteVisage;
    private String perteDeFermeteCou;
    private String perteDeFermeteDecollete;
    private String perteDeFermeteAvantBras;

    // Caractéristiques particulières
    private String pilosite;
    private String cicatrices;
    private String tatouages;
    private String piercings;

    // Vergetures
    private String vergeturesJambes;
    private String vergeturesFessesHanches;
    private String vergeturesVentreTaille;
    private String vergeturesPoitrineDecollete;

    // Cellulite
    private String celluliteJambes;
    private String celluliteFessesHanches;
    private String celluliteVentreTaille;
    private String celluliteBras;

    // Mesures
    private Double ihBrasDroit;
    private Double ihBrasGauche;

    // Cheveux
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

    // Cils
    private String epaisseurCils;
    private String longueurCils;
    private String courbureCils;
    private String cilsAbimes;
    private String cilsBroussailleux;
    private String chuteDeCils;
    private String cils;
    private String caracteristiqueSourcils;

    // Ongles
    private String onglesMous;
    private String onglesCassants;
    private String onglesStries;
    private String onglesDedoubles;

    // Visage
    private String lesionsRetentionnelles;
    private String lesionsInflammatoires;
    private String cernesPigmentaires;
    private String cernesVasculaires;
    private String poches;
    private String poresVisibles;
    private String teintInhomogene;
    private String teintTerne;
    private String yeux;
    private String levres;

    // Santé féminine
    private String menopause;
    private String ths;
    private String contraception;
    private String bouffeeChaleurMenaupose;

    // Antécédents médicaux
    private String anamnese;
    private String traitement;
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

    // Allergies
    private String reactionAllergique;
    private String allergiesCommentaires;
    private String desensibilisation;
    private String terrainAtopique;

    // Scores
    private Float scorePod;
    private Float scorePog;
    private Float scoreFront;
    private Float scoreLion;
    private Float scorePpd;
    private Float scorePpg;
    private Float scoreDod;
    private Float scoreDog;
    private Float scoreSngd;
    private Float scoreSngg;
    private Float scoreLevsup;
    private Float scoreComlevd;
    private Float scoreComlevg;
    private Float scorePtose;
    private Float ita;

    // Divers
    private String originePere;
    private String origineMere;
    private String nbCigarettesJour;
    private long hauteurSiege;
    private String mapyeux;
    private String maplevres;
    private String mapsourcils;

    // notes
    private Integer notes;
    private Integer notesYeux;
    private Integer notesLevres;
    private Integer notesTeint;
    private Integer notesCynetique;

    // Constructeurs
    public VolontaireDetailDTO() {
        super();
    }

    // Getters et Setters
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

    public String getSensibiliteCutanee() {
        return sensibiliteCutanee;
    }

    public void setSensibiliteCutanee(String sensibiliteCutanee) {
        this.sensibiliteCutanee = sensibiliteCutanee;
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

    public String getPerteDeFermeteAvantBras() {
        return perteDeFermeteAvantBras;
    }

    public void setPerteDeFermeteAvantBras(String perteDeFermeteAvantBras) {
        this.perteDeFermeteAvantBras = perteDeFermeteAvantBras;
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

    public Double getIhBrasDroit() {
        return ihBrasDroit;
    }

    public void setIhBrasDroit(Double ihBrasDroit) {
        this.ihBrasDroit = ihBrasDroit;
    }

    public Double getIhBrasGauche() {
        return ihBrasGauche;
    }

    public void setIhBrasGauche(Double ihBrasGauche) {
        this.ihBrasGauche = ihBrasGauche;
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

    public String getCils() {
        return cils;
    }

    public void setCils(String cils) {
        this.cils = cils;
    }

    public String getCaracteristiqueSourcils() {
        return caracteristiqueSourcils;
    }

    public void setCaracteristiqueSourcils(String caracteristiqueSourcils) {
        this.caracteristiqueSourcils = caracteristiqueSourcils;
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

    public String getCernesVasculaires() {
        return cernesVasculaires;
    }

    public void setCernesVasculaires(String cernesVasculaires) {
        this.cernesVasculaires = cernesVasculaires;
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

    public String getBouffeeChaleurMenaupose() {
        return bouffeeChaleurMenaupose;
    }

    public void setBouffeeChaleurMenaupose(String bouffeeChaleurMenaupose) {
        this.bouffeeChaleurMenaupose = bouffeeChaleurMenaupose;
    }

    public String getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(String anamnese) {
        this.anamnese = anamnese;
    }

    public String getTraitement() {
        return traitement;
    }

    public void setTraitement(String traitement) {
        this.traitement = traitement;
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

    public String getAllergiesCommentaires() {
        return allergiesCommentaires;
    }

    public void setAllergiesCommentaires(String allergiesCommentaires) {
        this.allergiesCommentaires = allergiesCommentaires;
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

    public Float getScorePod() {
        return scorePod;
    }

    public void setScorePod(Float scorePod) {
        this.scorePod = scorePod;
    }

    public Float getScorePog() {
        return scorePog;
    }

    public void setScorePog(Float scorePog) {
        this.scorePog = scorePog;
    }

    public Float getScoreFront() {
        return scoreFront;
    }

    public void setScoreFront(Float scoreFront) {
        this.scoreFront = scoreFront;
    }

    public Float getScoreLion() {
        return scoreLion;
    }

    public void setScoreLion(Float scoreLion) {
        this.scoreLion = scoreLion;
    }

    public Float getScorePpd() {
        return scorePpd;
    }

    public void setScorePpd(Float scorePpd) {
        this.scorePpd = scorePpd;
    }

    public Float getScorePpg() {
        return scorePpg;
    }

    public void setScorePpg(Float scorePpg) {
        this.scorePpg = scorePpg;
    }

    public Float getScoreDod() {
        return scoreDod;
    }

    public void setScoreDod(Float scoreDod) {
        this.scoreDod = scoreDod;
    }

    public Float getScoreDog() {
        return scoreDog;
    }

    public void setScoreDog(Float scoreDog) {
        this.scoreDog = scoreDog;
    }

    public Float getScoreSngd() {
        return scoreSngd;
    }

    public void setScoreSngd(Float scoreSngd) {
        this.scoreSngd = scoreSngd;
    }

    public Float getScoreSngg() {
        return scoreSngg;
    }

    public void setScoreSngg(Float scoreSngg) {
        this.scoreSngg = scoreSngg;
    }

    public Float getScoreLevsup() {
        return scoreLevsup;
    }

    public void setScoreLevsup(Float scoreLevsup) {
        this.scoreLevsup = scoreLevsup;
    }

    public Float getScoreComlevd() {
        return scoreComlevd;
    }

    public void setScoreComlevd(Float scoreComlevd) {
        this.scoreComlevd = scoreComlevd;
    }

    public Float getScoreComlevg() {
        return scoreComlevg;
    }

    public void setScoreComlevg(Float scoreComlevg) {
        this.scoreComlevg = scoreComlevg;
    }

    public Float getScorePtose() {
        return scorePtose;
    }

    public void setScorePtose(Float scorePtose) {
        this.scorePtose = scorePtose;
    }

    public Float getIta() {
        return ita;
    }

    public void setIta(Float ita) {
        this.ita = ita;
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

    public String getNbCigarettesJour() {
        return nbCigarettesJour;
    }

    public void setNbCigarettesJour(String nbCigarettesJour) {
        this.nbCigarettesJour = nbCigarettesJour;
    }

    public long getHauteurSiege() {
        return hauteurSiege;
    }

    public void setHauteurSiege(long hauteurSiege) {
        this.hauteurSiege = hauteurSiege;
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

    // Notes
    public Integer getNotes() {
        return notes;
    }

    public void setNotes(Integer notes) {
        this.notes = notes;
    }

    public Integer getNotesYeux() {
        return notesYeux;
    }

    public void setNotesYeux(Integer notesYeux) {
        this.notesYeux = notesYeux;
    }

    public Integer getNotesLevres() {
        return notesLevres;
    }

    public void setNotesLevres(Integer notesLevres) {
        this.notesLevres = notesLevres;
    }

    public Integer getNotesTeint() {
        return notesTeint;
    }

    public void setNotesTeint(Integer notesTeint) {
        this.notesTeint = notesTeint;
    }

    public Integer getNotesCynetique() {
        return notesCynetique;
    }

    public void setNotesCynetique(Integer notesCynetique) {
        this.notesCynetique = notesCynetique;
    }

}