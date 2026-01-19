package com.example.cosmetest.domain.model;
// Generated 15 déc. 2024, 15:56:41 by Hibernate Tools 6.5.1.Final

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity // Indique que cette classe est une entité JPA
@Table(name = "volontaire", indexes = {
		@Index(name = "idx_fulltext_vol", columnList = "nom_vol, prenom_vol, tel_domicile_vol, tel_portable_vol, email_vol")
}) // Permet de spécifier le nom de la table
public class Volontaire implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VOL")
	private Integer idVol;

	// Personal information fields
	@Column(name = "TITRE_VOL", length = 20)
	private String titreVol;

	@Column(name = "NOM_VOL", nullable = false, length = 50)
	private String nomVol;

	@Column(name = "PRENOM_VOL", nullable = false, length = 50)
	private String prenomVol;

	@Column(name = "ADRESSE_VOL", length = 200)
	private String adresseVol;

	@Column(name = "CP_VOL", columnDefinition = "CHAR(5)")
	private String cpVol;

	@Column(name = "VILLE_VOL", length = 50)
	private String villeVol;

	@Column(name = "TEL_DOMICILE_VOL", length = 20)
	private String telDomicileVol;

	@Column(name = "TEL_PORTABLE_VOL", length = 20)
	private String telPortableVol;

	@Column(name = "EMAIL_VOL", length = 100)
	private String emailVol;

	@Column(name = "SEXE", length = 10)
	private String sexe;

	@Column(name = "DATE_NAISSANCE")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Date dateNaissance;

	@Column(name = "ARCHIVE")
	private Boolean archive;

	// Comments field - using TEXT for longer content
	@Column(columnDefinition = "TEXT")
	private String commentairesVol;

	// Ethnicity and appearance characteristics
	@Column(length = 30)
	private String ethnie;

	@Column(length = 20)
	private String carnation;

	@Column(length = 20)
	private String bronzage;

	@Column(length = 30)
	private String coupsDeSoleil;

	@Column(length = 30)
	private String expositionSolaire;

	@Column(length = 15)
	private String phototype;

	@Column(length = 30)
	private String sensibiliteCutanee;

	@Column(length = 30)
	private String typePeauVisage;

	// Dryness characteristics
	@Column(length = 20)
	private String secheresseLevres;

	@Column(length = 20)
	private String secheresseCou;

	@Column(length = 20)
	private String secheressePoitrineDecollete;

	@Column(length = 20)
	private String secheresseVentreTaille;

	@Column(length = 20)
	private String secheresseFessesHanches;

	@Column(length = 20)
	private String secheresseBras;

	@Column(length = 20)
	private String secheresseMains;

	@Column(length = 20)
	private String secheresseJambes;

	@Column(length = 20)
	private String secheressePieds;

	// Pigmentation spots
	@Column(length = 20)
	private String tachesPigmentairesVisage;

	@Column(length = 20)
	private String tachesPigmentairesCou;

	@Column(length = 20)
	private String tachesPigmentairesDecollete;

	@Column(length = 20)
	private String tachesPigmentairesMains;

	// Firmness loss
	@Column(length = 20)
	private String perteDeFermeteVisage;

	@Column(length = 20)
	private String perteDeFermeteCou;

	@Column(length = 20)
	private String perteDeFermeteDecollete;

	// Physical characteristics
	@Column(length = 20)
	private String pilosite;

	@Column(columnDefinition = "TEXT")
	private String cicatrices;

	@Column(columnDefinition = "TEXT")
	private String tatouages;

	@Column(columnDefinition = "TEXT")
	private String piercings;

	// Stretch marks
	@Column(length = 20)
	private String vergeturesJambes;

	@Column(length = 20)
	private String vergeturesFessesHanches;

	@Column(length = 20)
	private String vergeturesVentreTaille;

	@Column(length = 20)
	private String vergeturesPoitrineDecollete;

	// Cellulite
	@Column(length = 20)
	private String celluliteJambes;

	@Column(length = 20)
	private String celluliteFessesHanches;

	@Column(length = 20)
	private String celluliteVentreTaille;

	@Column(length = 20)
	private String celluliteBras;

	// Numeric measurements (no length needed)
	private Double ihBrasDroit;
	private Double ihBrasGauche;

	// Hair characteristics
	@Column(length = 30)
	private String couleurCheveux;

	@Column(length = 20)
	private String natureCheveux;

	@Column(length = 15)
	private String longueurCheveux;

	@Column(length = 15)
	private String epaisseurCheveux;

	@Column(length = 20)
	private String natureCuirChevelu;

	@Column(length = 20)
	private String cheveuxAbimes;

	@Column(length = 20)
	private String cheveuxCassants;

	@Column(length = 20)
	private String cheveuxPlats;

	@Column(length = 20)
	private String cheveuxTernes;

	@Column(length = 20)
	private String pointesFourchues;

	@Column(length = 20)
	private String pellicules;

	@Column(length = 20)
	private String demangeaisonsDuCuirChevelu;

	@Column(length = 20)
	private String cuirCheveluSensible;

	@Column(length = 20)
	private String chuteDeCheveux;

	@Column(length = 20)
	private String calvitie;

	// Eyelash characteristics
	@Column(length = 15)
	private String epaisseurCils;

	@Column(length = 15)
	private String longueurCils;

	@Column(length = 15)
	private String courbureCils;

	@Column(length = 20)
	private String cilsAbimes;

	@Column(length = 20)
	private String cilsBroussailleux;

	@Column(length = 20)
	private String chuteDeCils;

	// Nail characteristics
	@Column(length = 20)
	private String onglesMous;

	@Column(length = 20)
	private String onglesCassants;

	@Column(length = 20)
	private String onglesStries;

	@Column(length = 20)
	private String onglesDedoubles;

	// Skin conditions
	@Column(length = 20)
	private String lesionsRetentionnelles;

	@Column(length = 20)
	private String lesionsInflammatoires;

	@Column(length = 20)
	private String cernesPigmentaires;

	@Column(length = 20)
	private String poches;

	@Column(length = 20)
	private String poresVisibles;

	@Column(length = 20)
	private String teintInhomogene;

	@Column(length = 20)
	private String teintTerne;

	// Hormonal/Medical information
	@Column(length = 15)
	private String menopause;

	@Column(columnDefinition = "TEXT")
	private String ths;

	@Column(columnDefinition = "TEXT")
	private String contraception;

	// Medical fields that might need more space
	@Column(columnDefinition = "TEXT")
	private String anamnese;

	@Column(length = 200)
	private String traitement;

	// Medical conditions
	@Column(length = 15)
	private String acne;

	@Column(length = 20)
	private String couperoseRosacee;

	@Column(length = 15)
	private String psoriasis;

	@Column(length = 20)
	private String dermiteSeborrheique;

	@Column(length = 15)
	private String eczema;

	@Column(length = 15)
	private String angiome;

	@Column(length = 15)
	private String pityriasis;

	@Column(length = 15)
	private String vitiligo;

	@Column(length = 15)
	private String melanome;

	@Column(length = 15)
	private String zona;

	@Column(length = 15)
	private String herpes;

	@Column(length = 15)
	private String pelade;

	@Column(columnDefinition = "TEXT")
	private String reactionAllergique;

	@Column(columnDefinition = "TEXT")
	private String allergiesCommentaires;

	@Column(length = 20)
	private String desensibilisation;

	@Column(length = 20)
	private String terrainAtopique;

	@Column(length = 20)
	private String santeCompatible;

	// Score fields (Float/Double - no length needed)
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

	// Origin information
	@Column(length = 50)
	private String originePere;

	@Column(length = 50)
	private String origineMere;

	@Column(length = 20)
	private String bouffeeChaleurMenaupose;

	// Numeric measurements (no length needed)
	private Integer poids;
	private Integer taille;

	@Column(length = 30)
	private String sousEthnie;

	@Column(name = "DATE_I")
	private Date dateI;

	// Additional characteristics
	@Column(length = 20)
	private String levres;

	@Column(length = 20)
	private String yeux;

	@Column(length = 20)
	private String cernesVasculaires;

	@Column(length = 20)
	private String cils;

	@Column(length = 10)
	private String nbCigarettesJour;

	@Column(length = 30)
	private String caracteristiqueSourcils;

	// Numeric measurement (no length needed)
	private long hauteurSiege;

	// Mapping fields
	@Column(length = 30)
	private String mapyeux;

	@Column(length = 30)
	private String maplevres;

	@Column(length = 30)
	private String mapsourcils;

	@Column(name = "notes", columnDefinition = "TINYINT" + " DEFAULT 4")
	private Integer notes;
	@Column(name = "notes_yeux", columnDefinition = "TINYINT" + " DEFAULT 4")
	private Integer notesYeux;
	@Column(name = "notes_levres", columnDefinition = "TINYINT" + " DEFAULT 4")
	private Integer notesLevres;
	@Column(name = "notes_teint", columnDefinition = "TINYINT" + " DEFAULT 4")
	private Integer notesTeint;
	@Column(name = "notes_cynetique", columnDefinition = "TINYINT" + " DEFAULT 4")
	private Integer notesCynetique;

	@OneToMany(mappedBy = "volontaire", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EtudeVolontaire> etudeVolontaires = new ArrayList<>();

	public Volontaire() {
	}

	public Volontaire(String nomVol, String prenomVol, Date dateNaissance, String cils, String caracteristiqueSourcils,
			long hauteurSiege) {
		this.nomVol = nomVol;
		this.prenomVol = prenomVol;
		this.dateNaissance = dateNaissance;
		this.cils = cils;
		this.caracteristiqueSourcils = caracteristiqueSourcils;
		this.hauteurSiege = hauteurSiege;
	}

	public Volontaire(String titreVol, String nomVol, String prenomVol, String adresseVol, String cpVol,
			String villeVol, String telDomicileVol, String telPortableVol, String emailVol, String sexe,
			Date dateNaissance, Boolean archive, String commentairesVol, String ethnie, String carnation,
			String bronzage, String coupsDeSoleil, String expositionSolaire, String phototype,
			String sensibiliteCutanee, String typePeauVisage, String secheresseLevres, String secheresseCou,
			String secheressePoitrineDecollete, String secheresseVentreTaille, String secheresseFessesHanches,
			String secheresseBras, String secheresseMains, String secheresseJambes, String secheressePieds,
			String tachesPigmentairesVisage, String tachesPigmentairesCou, String tachesPigmentairesDecollete,
			String tachesPigmentairesMains, String perteDeFermeteVisage, String perteDeFermeteCou,
			String perteDeFermeteDecollete, String pilosite, String cicatrices, String tatouages, String piercings,
			String vergeturesJambes, String vergeturesFessesHanches, String vergeturesVentreTaille,
			String vergeturesPoitrineDecollete, String celluliteJambes, String celluliteFessesHanches,
			String celluliteVentreTaille, String celluliteBras, Double ihBrasDroit, Double ihBrasGauche,
			String couleurCheveux, String natureCheveux, String longueurCheveux, String epaisseurCheveux,
			String natureCuirChevelu, String cheveuxAbimes, String cheveuxCassants, String cheveuxPlats,
			String cheveuxTernes, String pointesFourchues, String pellicules, String demangeaisonsDuCuirChevelu,
			String cuirCheveluSensible, String chuteDeCheveux, String calvitie, String epaisseurCils,
			String longueurCils, String courbureCils, String cilsAbimes, String cilsBroussailleux, String chuteDeCils,
			String onglesMous, String onglesCassants, String onglesStries, String onglesDedoubles,
			String lesionsRetentionnelles, String lesionsInflammatoires, String cernesPigmentaires, String poches,
			String poresVisibles, String teintInhomogene, String teintTerne, String menopause, String ths,
			String contraception, String anamnese, String traitement, String acne, String couperoseRosacee,
			String psoriasis, String dermiteSeborrheique, String eczema, String angiome, String pityriasis,
			String vitiligo, String melanome, String zona, String herpes, String pelade, String reactionAllergique,
			String allergiesCommentaires, String desensibilisation, String terrainAtopique, String santeCompatible,
			Float scorePod, Float scorePog, Float scoreFront, Float scoreLion, Float scorePpd, Float scorePpg,
			Float scoreDod, Float scoreDog, Float scoreSngd, Float scoreSngg, Float scoreLevsup, Float scoreComlevd,
			Float scoreComlevg, Float scorePtose, Float ita, String originePere, String origineMere,
			String bouffeeChaleurMenaupose, Integer poids, Integer taille, String sousEthnie, Date dateI, String levres,
			String yeux, String cernesVasculaires, String cils, String nbCigarettesJour, String caracteristiqueSourcils,
			long hauteurSiege, String mapyeux, String maplevres, String mapsourcils, Integer notes, Integer notesYeux,
			Integer notesLevres, Integer notesSourcils, Integer notesTeint, Integer notesCynetique) {
		this.titreVol = titreVol;
		this.nomVol = nomVol;
		this.prenomVol = prenomVol;
		this.adresseVol = adresseVol;
		this.cpVol = cpVol;
		this.villeVol = villeVol;
		this.telDomicileVol = telDomicileVol;
		this.telPortableVol = telPortableVol;
		this.emailVol = emailVol;
		this.sexe = sexe;
		this.dateNaissance = dateNaissance;
		this.archive = archive;
		this.commentairesVol = commentairesVol;
		this.ethnie = ethnie;
		this.carnation = carnation;
		this.bronzage = bronzage;
		this.coupsDeSoleil = coupsDeSoleil;
		this.expositionSolaire = expositionSolaire;
		this.phototype = phototype;
		this.sensibiliteCutanee = sensibiliteCutanee;
		this.typePeauVisage = typePeauVisage;
		this.secheresseLevres = secheresseLevres;
		this.secheresseCou = secheresseCou;
		this.secheressePoitrineDecollete = secheressePoitrineDecollete;
		this.secheresseVentreTaille = secheresseVentreTaille;
		this.secheresseFessesHanches = secheresseFessesHanches;
		this.secheresseBras = secheresseBras;
		this.secheresseMains = secheresseMains;
		this.secheresseJambes = secheresseJambes;
		this.secheressePieds = secheressePieds;
		this.tachesPigmentairesVisage = tachesPigmentairesVisage;
		this.tachesPigmentairesCou = tachesPigmentairesCou;
		this.tachesPigmentairesDecollete = tachesPigmentairesDecollete;
		this.tachesPigmentairesMains = tachesPigmentairesMains;
		this.perteDeFermeteVisage = perteDeFermeteVisage;
		this.perteDeFermeteCou = perteDeFermeteCou;
		this.perteDeFermeteDecollete = perteDeFermeteDecollete;
		this.pilosite = pilosite;
		this.cicatrices = cicatrices;
		this.tatouages = tatouages;
		this.piercings = piercings;
		this.vergeturesJambes = vergeturesJambes;
		this.vergeturesFessesHanches = vergeturesFessesHanches;
		this.vergeturesVentreTaille = vergeturesVentreTaille;
		this.vergeturesPoitrineDecollete = vergeturesPoitrineDecollete;
		this.celluliteJambes = celluliteJambes;
		this.celluliteFessesHanches = celluliteFessesHanches;
		this.celluliteVentreTaille = celluliteVentreTaille;
		this.celluliteBras = celluliteBras;
		this.ihBrasDroit = ihBrasDroit;
		this.ihBrasGauche = ihBrasGauche;
		this.couleurCheveux = couleurCheveux;
		this.natureCheveux = natureCheveux;
		this.longueurCheveux = longueurCheveux;
		this.epaisseurCheveux = epaisseurCheveux;
		this.natureCuirChevelu = natureCuirChevelu;
		this.cheveuxAbimes = cheveuxAbimes;
		this.cheveuxCassants = cheveuxCassants;
		this.cheveuxPlats = cheveuxPlats;
		this.cheveuxTernes = cheveuxTernes;
		this.pointesFourchues = pointesFourchues;
		this.pellicules = pellicules;
		this.demangeaisonsDuCuirChevelu = demangeaisonsDuCuirChevelu;
		this.cuirCheveluSensible = cuirCheveluSensible;
		this.chuteDeCheveux = chuteDeCheveux;
		this.calvitie = calvitie;
		this.epaisseurCils = epaisseurCils;
		this.longueurCils = longueurCils;
		this.courbureCils = courbureCils;
		this.cilsAbimes = cilsAbimes;
		this.cilsBroussailleux = cilsBroussailleux;
		this.chuteDeCils = chuteDeCils;
		this.onglesMous = onglesMous;
		this.onglesCassants = onglesCassants;
		this.onglesStries = onglesStries;
		this.onglesDedoubles = onglesDedoubles;
		this.lesionsRetentionnelles = lesionsRetentionnelles;
		this.lesionsInflammatoires = lesionsInflammatoires;
		this.cernesPigmentaires = cernesPigmentaires;
		this.poches = poches;
		this.poresVisibles = poresVisibles;
		this.teintInhomogene = teintInhomogene;
		this.teintTerne = teintTerne;
		this.menopause = menopause;
		this.ths = ths;
		this.contraception = contraception;
		this.anamnese = anamnese;
		this.traitement = traitement;
		this.acne = acne;
		this.couperoseRosacee = couperoseRosacee;
		this.psoriasis = psoriasis;
		this.dermiteSeborrheique = dermiteSeborrheique;
		this.eczema = eczema;
		this.angiome = angiome;
		this.pityriasis = pityriasis;
		this.vitiligo = vitiligo;
		this.melanome = melanome;
		this.zona = zona;
		this.herpes = herpes;
		this.pelade = pelade;
		this.reactionAllergique = reactionAllergique;
		this.allergiesCommentaires = allergiesCommentaires;
		this.desensibilisation = desensibilisation;
		this.terrainAtopique = terrainAtopique;
		this.santeCompatible = santeCompatible;
		this.scorePod = scorePod;
		this.scorePog = scorePog;
		this.scoreFront = scoreFront;
		this.scoreLion = scoreLion;
		this.scorePpd = scorePpd;
		this.scorePpg = scorePpg;
		this.scoreDod = scoreDod;
		this.scoreDog = scoreDog;
		this.scoreSngd = scoreSngd;
		this.scoreSngg = scoreSngg;
		this.scoreLevsup = scoreLevsup;
		this.scoreComlevd = scoreComlevd;
		this.scoreComlevg = scoreComlevg;
		this.scorePtose = scorePtose;
		this.ita = ita;
		this.originePere = originePere;
		this.origineMere = origineMere;
		this.bouffeeChaleurMenaupose = bouffeeChaleurMenaupose;
		this.poids = poids;
		this.taille = taille;
		this.sousEthnie = sousEthnie;
		this.dateI = dateI;
		this.levres = levres;
		this.yeux = yeux;
		this.cernesVasculaires = cernesVasculaires;
		this.cils = cils;
		this.nbCigarettesJour = nbCigarettesJour;
		this.caracteristiqueSourcils = caracteristiqueSourcils;
		this.hauteurSiege = hauteurSiege;
		this.mapyeux = mapyeux;
		this.maplevres = maplevres;
		this.mapsourcils = mapsourcils;
		this.notes = notes;
		this.notesYeux = notesYeux;
		this.notesLevres = notesLevres;
		this.notesTeint = notesTeint;
		this.notesCynetique = notesCynetique;
	}

	public Integer getIdVol() {
		return this.idVol;
	}

	public void setIdVol(Integer idVol) {
		this.idVol = idVol;
	}

	public String getTitreVol() {
		return this.titreVol;
	}

	public void setTitreVol(String titreVol) {
		this.titreVol = titreVol;
	}

	public String getNomVol() {
		return this.nomVol;
	}

	public void setNomVol(String nomVol) {
		this.nomVol = nomVol;
	}

	public String getPrenomVol() {
		return this.prenomVol;
	}

	public void setPrenomVol(String prenomVol) {
		this.prenomVol = prenomVol;
	}

	public String getAdresseVol() {
		return this.adresseVol;
	}

	public void setAdresseVol(String adresseVol) {
		this.adresseVol = adresseVol;
	}

	public String getCpVol() {
		return this.cpVol;
	}

	public void setCpVol(String cpVol) {
		this.cpVol = cpVol;
	}

	public String getVilleVol() {
		return this.villeVol;
	}

	public void setVilleVol(String villeVol) {
		this.villeVol = villeVol;
	}

	public String getTelDomicileVol() {
		return this.telDomicileVol;
	}

	public void setTelDomicileVol(String telDomicileVol) {
		this.telDomicileVol = telDomicileVol;
	}

	public String getTelPortableVol() {
		return this.telPortableVol;
	}

	public void setTelPortableVol(String telPortableVol) {
		this.telPortableVol = telPortableVol;
	}

	public String getEmailVol() {
		return this.emailVol;
	}

	public void setEmailVol(String emailVol) {
		this.emailVol = emailVol;
	}

	public String getSexe() {
		return this.sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	/**
	 * Convertit et retourne la date de naissance en LocalDate
	 *
	 * @return la date de naissance au format LocalDate
	 */
	// Si vous avez besoin de convertir java.sql.Date en java.time.LocalDate
	public LocalDate getDateNaissance() {
		if (this.dateNaissance == null) {
			return null;
		}
		return this.dateNaissance.toLocalDate(); // Utilisez toLocalDate() au lieu de toInstant()
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Boolean getArchive() {
		return this.archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public String getCommentairesVol() {
		return this.commentairesVol;
	}

	public void setCommentairesVol(String commentairesVol) {
		this.commentairesVol = commentairesVol;
	}

	public String getEthnie() {
		return this.ethnie;
	}

	public void setEthnie(String ethnie) {
		this.ethnie = ethnie;
	}

	public String getCarnation() {
		return this.carnation;
	}

	public void setCarnation(String carnation) {
		this.carnation = carnation;
	}

	public String getBronzage() {
		return this.bronzage;
	}

	public void setBronzage(String bronzage) {
		this.bronzage = bronzage;
	}

	public String getCoupsDeSoleil() {
		return this.coupsDeSoleil;
	}

	public void setCoupsDeSoleil(String coupsDeSoleil) {
		this.coupsDeSoleil = coupsDeSoleil;
	}

	public String getExpositionSolaire() {
		return this.expositionSolaire;
	}

	public void setExpositionSolaire(String expositionSolaire) {
		this.expositionSolaire = expositionSolaire;
	}

	public String getPhototype() {
		return this.phototype;
	}

	public void setPhototype(String phototype) {
		this.phototype = phototype;
	}

	public String getSensibiliteCutanee() {
		return this.sensibiliteCutanee;
	}

	public void setSensibiliteCutanee(String sensibiliteCutanee) {
		this.sensibiliteCutanee = sensibiliteCutanee;
	}

	public String getTypePeauVisage() {
		return this.typePeauVisage;
	}

	public void setTypePeauVisage(String typePeauVisage) {
		this.typePeauVisage = typePeauVisage;
	}

	public String getSecheresseLevres() {
		return this.secheresseLevres;
	}

	public void setSecheresseLevres(String secheresseLevres) {
		this.secheresseLevres = secheresseLevres;
	}

	public String getSecheresseCou() {
		return this.secheresseCou;
	}

	public void setSecheresseCou(String secheresseCou) {
		this.secheresseCou = secheresseCou;
	}

	public String getSecheressePoitrineDecollete() {
		return this.secheressePoitrineDecollete;
	}

	public void setSecheressePoitrineDecollete(String secheressePoitrineDecollete) {
		this.secheressePoitrineDecollete = secheressePoitrineDecollete;
	}

	public String getSecheresseVentreTaille() {
		return this.secheresseVentreTaille;
	}

	public void setSecheresseVentreTaille(String secheresseVentreTaille) {
		this.secheresseVentreTaille = secheresseVentreTaille;
	}

	public String getSecheresseFessesHanches() {
		return this.secheresseFessesHanches;
	}

	public void setSecheresseFessesHanches(String secheresseFessesHanches) {
		this.secheresseFessesHanches = secheresseFessesHanches;
	}

	public String getSecheresseBras() {
		return this.secheresseBras;
	}

	public void setSecheresseBras(String secheresseBras) {
		this.secheresseBras = secheresseBras;
	}

	public String getSecheresseMains() {
		return this.secheresseMains;
	}

	public void setSecheresseMains(String secheresseMains) {
		this.secheresseMains = secheresseMains;
	}

	public String getSecheresseJambes() {
		return this.secheresseJambes;
	}

	public void setSecheresseJambes(String secheresseJambes) {
		this.secheresseJambes = secheresseJambes;
	}

	public String getSecheressePieds() {
		return this.secheressePieds;
	}

	public void setSecheressePieds(String secheressePieds) {
		this.secheressePieds = secheressePieds;
	}

	public String getTachesPigmentairesVisage() {
		return this.tachesPigmentairesVisage;
	}

	public void setTachesPigmentairesVisage(String tachesPigmentairesVisage) {
		this.tachesPigmentairesVisage = tachesPigmentairesVisage;
	}

	public String getTachesPigmentairesCou() {
		return this.tachesPigmentairesCou;
	}

	public void setTachesPigmentairesCou(String tachesPigmentairesCou) {
		this.tachesPigmentairesCou = tachesPigmentairesCou;
	}

	public String getTachesPigmentairesDecollete() {
		return this.tachesPigmentairesDecollete;
	}

	public void setTachesPigmentairesDecollete(String tachesPigmentairesDecollete) {
		this.tachesPigmentairesDecollete = tachesPigmentairesDecollete;
	}

	public String getTachesPigmentairesMains() {
		return this.tachesPigmentairesMains;
	}

	public void setTachesPigmentairesMains(String tachesPigmentairesMains) {
		this.tachesPigmentairesMains = tachesPigmentairesMains;
	}

	public String getPerteDeFermeteVisage() {
		return this.perteDeFermeteVisage;
	}

	public void setPerteDeFermeteVisage(String perteDeFermeteVisage) {
		this.perteDeFermeteVisage = perteDeFermeteVisage;
	}

	public String getPerteDeFermeteCou() {
		return this.perteDeFermeteCou;
	}

	public void setPerteDeFermeteCou(String perteDeFermeteCou) {
		this.perteDeFermeteCou = perteDeFermeteCou;
	}

	public String getPerteDeFermeteDecollete() {
		return this.perteDeFermeteDecollete;
	}

	public void setPerteDeFermeteDecollete(String perteDeFermeteDecollete) {
		this.perteDeFermeteDecollete = perteDeFermeteDecollete;
	}

	public String getPilosite() {
		return this.pilosite;
	}

	public void setPilosite(String pilosite) {
		this.pilosite = pilosite;
	}

	public String getCicatrices() {
		return this.cicatrices;
	}

	public void setCicatrices(String cicatrices) {
		this.cicatrices = cicatrices;
	}

	public String getTatouages() {
		return this.tatouages;
	}

	public void setTatouages(String tatouages) {
		this.tatouages = tatouages;
	}

	public String getPiercings() {
		return this.piercings;
	}

	public void setPiercings(String piercings) {
		this.piercings = piercings;
	}

	public String getVergeturesJambes() {
		return this.vergeturesJambes;
	}

	public void setVergeturesJambes(String vergeturesJambes) {
		this.vergeturesJambes = vergeturesJambes;
	}

	public String getVergeturesFessesHanches() {
		return this.vergeturesFessesHanches;
	}

	public void setVergeturesFessesHanches(String vergeturesFessesHanches) {
		this.vergeturesFessesHanches = vergeturesFessesHanches;
	}

	public String getVergeturesVentreTaille() {
		return this.vergeturesVentreTaille;
	}

	public void setVergeturesVentreTaille(String vergeturesVentreTaille) {
		this.vergeturesVentreTaille = vergeturesVentreTaille;
	}

	public String getVergeturesPoitrineDecollete() {
		return this.vergeturesPoitrineDecollete;
	}

	public void setVergeturesPoitrineDecollete(String vergeturesPoitrineDecollete) {
		this.vergeturesPoitrineDecollete = vergeturesPoitrineDecollete;
	}

	public String getCelluliteJambes() {
		return this.celluliteJambes;
	}

	public void setCelluliteJambes(String celluliteJambes) {
		this.celluliteJambes = celluliteJambes;
	}

	public String getCelluliteFessesHanches() {
		return this.celluliteFessesHanches;
	}

	public void setCelluliteFessesHanches(String celluliteFessesHanches) {
		this.celluliteFessesHanches = celluliteFessesHanches;
	}

	public String getCelluliteVentreTaille() {
		return this.celluliteVentreTaille;
	}

	public void setCelluliteVentreTaille(String celluliteVentreTaille) {
		this.celluliteVentreTaille = celluliteVentreTaille;
	}

	public String getCelluliteBras() {
		return this.celluliteBras;
	}

	public void setCelluliteBras(String celluliteBras) {
		this.celluliteBras = celluliteBras;
	}

	public Double getIhBrasDroit() {
		return this.ihBrasDroit;
	}

	public void setIhBrasDroit(Double ihBrasDroit) {
		this.ihBrasDroit = ihBrasDroit;
	}

	public Double getIhBrasGauche() {
		return this.ihBrasGauche;
	}

	public void setIhBrasGauche(Double ihBrasGauche) {
		this.ihBrasGauche = ihBrasGauche;
	}

	public String getCouleurCheveux() {
		return this.couleurCheveux;
	}

	public void setCouleurCheveux(String couleurCheveux) {
		this.couleurCheveux = couleurCheveux;
	}

	public String getNatureCheveux() {
		return this.natureCheveux;
	}

	public void setNatureCheveux(String natureCheveux) {
		this.natureCheveux = natureCheveux;
	}

	public String getLongueurCheveux() {
		return this.longueurCheveux;
	}

	public void setLongueurCheveux(String longueurCheveux) {
		this.longueurCheveux = longueurCheveux;
	}

	public String getEpaisseurCheveux() {
		return this.epaisseurCheveux;
	}

	public void setEpaisseurCheveux(String epaisseurCheveux) {
		this.epaisseurCheveux = epaisseurCheveux;
	}

	public String getNatureCuirChevelu() {
		return this.natureCuirChevelu;
	}

	public void setNatureCuirChevelu(String natureCuirChevelu) {
		this.natureCuirChevelu = natureCuirChevelu;
	}

	public String getCheveuxAbimes() {
		return this.cheveuxAbimes;
	}

	public void setCheveuxAbimes(String cheveuxAbimes) {
		this.cheveuxAbimes = cheveuxAbimes;
	}

	public String getCheveuxCassants() {
		return this.cheveuxCassants;
	}

	public void setCheveuxCassants(String cheveuxCassants) {
		this.cheveuxCassants = cheveuxCassants;
	}

	public String getCheveuxPlats() {
		return this.cheveuxPlats;
	}

	public void setCheveuxPlats(String cheveuxPlats) {
		this.cheveuxPlats = cheveuxPlats;
	}

	public String getCheveuxTernes() {
		return this.cheveuxTernes;
	}

	public void setCheveuxTernes(String cheveuxTernes) {
		this.cheveuxTernes = cheveuxTernes;
	}

	public String getPointesFourchues() {
		return this.pointesFourchues;
	}

	public void setPointesFourchues(String pointesFourchues) {
		this.pointesFourchues = pointesFourchues;
	}

	public String getPellicules() {
		return this.pellicules;
	}

	public void setPellicules(String pellicules) {
		this.pellicules = pellicules;
	}

	public String getDemangeaisonsDuCuirChevelu() {
		return this.demangeaisonsDuCuirChevelu;
	}

	public void setDemangeaisonsDuCuirChevelu(String demangeaisonsDuCuirChevelu) {
		this.demangeaisonsDuCuirChevelu = demangeaisonsDuCuirChevelu;
	}

	public String getCuirCheveluSensible() {
		return this.cuirCheveluSensible;
	}

	public void setCuirCheveluSensible(String cuirCheveluSensible) {
		this.cuirCheveluSensible = cuirCheveluSensible;
	}

	public String getChuteDeCheveux() {
		return this.chuteDeCheveux;
	}

	public void setChuteDeCheveux(String chuteDeCheveux) {
		this.chuteDeCheveux = chuteDeCheveux;
	}

	public String getCalvitie() {
		return this.calvitie;
	}

	public void setCalvitie(String calvitie) {
		this.calvitie = calvitie;
	}

	public String getEpaisseurCils() {
		return this.epaisseurCils;
	}

	public void setEpaisseurCils(String epaisseurCils) {
		this.epaisseurCils = epaisseurCils;
	}

	public String getLongueurCils() {
		return this.longueurCils;
	}

	public void setLongueurCils(String longueurCils) {
		this.longueurCils = longueurCils;
	}

	public String getCourbureCils() {
		return this.courbureCils;
	}

	public void setCourbureCils(String courbureCils) {
		this.courbureCils = courbureCils;
	}

	public String getCilsAbimes() {
		return this.cilsAbimes;
	}

	public void setCilsAbimes(String cilsAbimes) {
		this.cilsAbimes = cilsAbimes;
	}

	public String getCilsBroussailleux() {
		return this.cilsBroussailleux;
	}

	public void setCilsBroussailleux(String cilsBroussailleux) {
		this.cilsBroussailleux = cilsBroussailleux;
	}

	public String getChuteDeCils() {
		return this.chuteDeCils;
	}

	public void setChuteDeCils(String chuteDeCils) {
		this.chuteDeCils = chuteDeCils;
	}

	public String getOnglesMous() {
		return this.onglesMous;
	}

	public void setOnglesMous(String onglesMous) {
		this.onglesMous = onglesMous;
	}

	public String getOnglesCassants() {
		return this.onglesCassants;
	}

	public void setOnglesCassants(String onglesCassants) {
		this.onglesCassants = onglesCassants;
	}

	public String getOnglesStries() {
		return this.onglesStries;
	}

	public void setOnglesStries(String onglesStries) {
		this.onglesStries = onglesStries;
	}

	public String getOnglesDedoubles() {
		return this.onglesDedoubles;
	}

	public void setOnglesDedoubles(String onglesDedoubles) {
		this.onglesDedoubles = onglesDedoubles;
	}

	public String getLesionsRetentionnelles() {
		return this.lesionsRetentionnelles;
	}

	public void setLesionsRetentionnelles(String lesionsRetentionnelles) {
		this.lesionsRetentionnelles = lesionsRetentionnelles;
	}

	public String getLesionsInflammatoires() {
		return this.lesionsInflammatoires;
	}

	public void setLesionsInflammatoires(String lesionsInflammatoires) {
		this.lesionsInflammatoires = lesionsInflammatoires;
	}

	public String getCernesPigmentaires() {
		return this.cernesPigmentaires;
	}

	public void setCernesPigmentaires(String cernesPigmentaires) {
		this.cernesPigmentaires = cernesPigmentaires;
	}

	public String getPoches() {
		return this.poches;
	}

	public void setPoches(String poches) {
		this.poches = poches;
	}

	public String getPoresVisibles() {
		return this.poresVisibles;
	}

	public void setPoresVisibles(String poresVisibles) {
		this.poresVisibles = poresVisibles;
	}

	public String getTeintInhomogene() {
		return this.teintInhomogene;
	}

	public void setTeintInhomogene(String teintInhomogene) {
		this.teintInhomogene = teintInhomogene;
	}

	public String getTeintTerne() {
		return this.teintTerne;
	}

	public void setTeintTerne(String teintTerne) {
		this.teintTerne = teintTerne;
	}

	public String getMenopause() {
		return this.menopause;
	}

	public void setMenopause(String menopause) {
		this.menopause = menopause;
	}

	public String getThs() {
		return this.ths;
	}

	public void setThs(String ths) {
		this.ths = ths;
	}

	public String getContraception() {
		return this.contraception;
	}

	public void setContraception(String contraception) {
		this.contraception = contraception;
	}

	public String getAnamnese() {
		return this.anamnese;
	}

	public void setAnamnese(String anamnese) {
		this.anamnese = anamnese;
	}

	public String getTraitement() {
		return this.traitement;
	}

	public void setTraitement(String traitement) {
		this.traitement = traitement;
	}

	public String getAcne() {
		return this.acne;
	}

	public void setAcne(String acne) {
		this.acne = acne;
	}

	public String getCouperoseRosacee() {
		return this.couperoseRosacee;
	}

	public void setCouperoseRosacee(String couperoseRosacee) {
		this.couperoseRosacee = couperoseRosacee;
	}

	public String getPsoriasis() {
		return this.psoriasis;
	}

	public void setPsoriasis(String psoriasis) {
		this.psoriasis = psoriasis;
	}

	public String getDermiteSeborrheique() {
		return this.dermiteSeborrheique;
	}

	public void setDermiteSeborrheique(String dermiteSeborrheique) {
		this.dermiteSeborrheique = dermiteSeborrheique;
	}

	public String getEczema() {
		return this.eczema;
	}

	public void setEczema(String eczema) {
		this.eczema = eczema;
	}

	public String getAngiome() {
		return this.angiome;
	}

	public void setAngiome(String angiome) {
		this.angiome = angiome;
	}

	public String getPityriasis() {
		return this.pityriasis;
	}

	public void setPityriasis(String pityriasis) {
		this.pityriasis = pityriasis;
	}

	public String getVitiligo() {
		return this.vitiligo;
	}

	public void setVitiligo(String vitiligo) {
		this.vitiligo = vitiligo;
	}

	public String getMelanome() {
		return this.melanome;
	}

	public void setMelanome(String melanome) {
		this.melanome = melanome;
	}

	public String getZona() {
		return this.zona;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getHerpes() {
		return this.herpes;
	}

	public void setHerpes(String herpes) {
		this.herpes = herpes;
	}

	public String getPelade() {
		return this.pelade;
	}

	public void setPelade(String pelade) {
		this.pelade = pelade;
	}

	public String getReactionAllergique() {
		return this.reactionAllergique;
	}

	public void setReactionAllergique(String reactionAllergique) {
		this.reactionAllergique = reactionAllergique;
	}

	public String getAllergiesCommentaires() {
		return this.allergiesCommentaires;
	}

	public void setAllergiesCommentaires(String allergiesCommentaires) {
		this.allergiesCommentaires = allergiesCommentaires;
	}

	public String getDesensibilisation() {
		return this.desensibilisation;
	}

	public void setDesensibilisation(String desensibilisation) {
		this.desensibilisation = desensibilisation;
	}

	public String getTerrainAtopique() {
		return this.terrainAtopique;
	}

	public void setTerrainAtopique(String terrainAtopique) {
		this.terrainAtopique = terrainAtopique;
	}

	public String getSanteCompatible() {
		return this.santeCompatible;
	}

	public void setSanteCompatible(String santeCompatible) {
		this.santeCompatible = santeCompatible;
	}

	public Float getScorePod() {
		return this.scorePod;
	}

	public void setScorePod(Float scorePod) {
		this.scorePod = scorePod;
	}

	public Float getScorePog() {
		return this.scorePog;
	}

	public void setScorePog(Float scorePog) {
		this.scorePog = scorePog;
	}

	public Float getScoreFront() {
		return this.scoreFront;
	}

	public void setScoreFront(Float scoreFront) {
		this.scoreFront = scoreFront;
	}

	public Float getScoreLion() {
		return this.scoreLion;
	}

	public void setScoreLion(Float scoreLion) {
		this.scoreLion = scoreLion;
	}

	public Float getScorePpd() {
		return this.scorePpd;
	}

	public void setScorePpd(Float scorePpd) {
		this.scorePpd = scorePpd;
	}

	public Float getScorePpg() {
		return this.scorePpg;
	}

	public void setScorePpg(Float scorePpg) {
		this.scorePpg = scorePpg;
	}

	public Float getScoreDod() {
		return this.scoreDod;
	}

	public void setScoreDod(Float scoreDod) {
		this.scoreDod = scoreDod;
	}

	public Float getScoreDog() {
		return this.scoreDog;
	}

	public void setScoreDog(Float scoreDog) {
		this.scoreDog = scoreDog;
	}

	public Float getScoreSngd() {
		return this.scoreSngd;
	}

	public void setScoreSngd(Float scoreSngd) {
		this.scoreSngd = scoreSngd;
	}

	public Float getScoreSngg() {
		return this.scoreSngg;
	}

	public void setScoreSngg(Float scoreSngg) {
		this.scoreSngg = scoreSngg;
	}

	public Float getScoreLevsup() {
		return this.scoreLevsup;
	}

	public void setScoreLevsup(Float scoreLevsup) {
		this.scoreLevsup = scoreLevsup;
	}

	public Float getScoreComlevd() {
		return this.scoreComlevd;
	}

	public void setScoreComlevd(Float scoreComlevd) {
		this.scoreComlevd = scoreComlevd;
	}

	public Float getScoreComlevg() {
		return this.scoreComlevg;
	}

	public void setScoreComlevg(Float scoreComlevg) {
		this.scoreComlevg = scoreComlevg;
	}

	public Float getScorePtose() {
		return this.scorePtose;
	}

	public void setScorePtose(Float scorePtose) {
		this.scorePtose = scorePtose;
	}

	public Float getIta() {
		return this.ita;
	}

	public void setIta(Float ita) {
		this.ita = ita;
	}

	public String getOriginePere() {
		return this.originePere;
	}

	public void setOriginePere(String originePere) {
		this.originePere = originePere;
	}

	public String getOrigineMere() {
		return this.origineMere;
	}

	public void setOrigineMere(String origineMere) {
		this.origineMere = origineMere;
	}

	public String getBouffeeChaleurMenaupose() {
		return this.bouffeeChaleurMenaupose;
	}

	public void setBouffeeChaleurMenaupose(String bouffeeChaleurMenaupose) {
		this.bouffeeChaleurMenaupose = bouffeeChaleurMenaupose;
	}

	public Integer getPoids() {
		return this.poids;
	}

	public void setPoids(Integer poids) {
		this.poids = poids;
	}

	public Integer getTaille() {
		return this.taille;
	}

	public void setTaille(Integer taille) {
		this.taille = taille;
	}

	public String getSousEthnie() {
		return this.sousEthnie;
	}

	public void setSousEthnie(String sousEthnie) {
		this.sousEthnie = sousEthnie;
	}

	public LocalDate getDateI() {
		if (this.dateI == null) {
			return null;
		}
		return this.dateI.toLocalDate();
	}

	public void setDateI(Date dateI) {
		this.dateI = dateI;
	}

	public String getLevres() {
		return this.levres;
	}

	public void setLevres(String levres) {
		this.levres = levres;
	}

	public String getYeux() {
		return this.yeux;
	}

	public void setYeux(String yeux) {
		this.yeux = yeux;
	}

	public String getCernesVasculaires() {
		return this.cernesVasculaires;
	}

	public void setCernesVasculaires(String cernesVasculaires) {
		this.cernesVasculaires = cernesVasculaires;
	}

	public String getCils() {
		return this.cils;
	}

	public void setCils(String cils) {
		this.cils = cils;
	}

	public String getNbCigarettesJour() {
		return this.nbCigarettesJour;
	}

	public void setNbCigarettesJour(String nbCigarettesJour) {
		this.nbCigarettesJour = nbCigarettesJour;
	}

	public String getCaracteristiqueSourcils() {
		return this.caracteristiqueSourcils;
	}

	public void setCaracteristiqueSourcils(String caracteristiqueSourcils) {
		this.caracteristiqueSourcils = caracteristiqueSourcils;
	}

	public long getHauteurSiege() {
		return this.hauteurSiege;
	}

	public void setHauteurSiege(long hauteurSiege) {
		this.hauteurSiege = hauteurSiege;
	}

	public String getMapyeux() {
		return this.mapyeux;
	}

	public void setMapyeux(String mapyeux) {
		this.mapyeux = mapyeux;
	}

	public String getMaplevres() {
		return this.maplevres;
	}

	public void setMaplevres(String maplevres) {
		this.maplevres = maplevres;
	}

	public String getMapsourcils() {
		return this.mapsourcils;
	}

	public void setMapsourcils(String mapsourcils) {
		this.mapsourcils = mapsourcils;
	}

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

	public List<EtudeVolontaire> getEtudeVolontaires() {
		return etudeVolontaires;
	}

	public void setEtudeVolontaires(List<EtudeVolontaire> etudeVolontaires) {
		this.etudeVolontaires = etudeVolontaires;
	}

}
