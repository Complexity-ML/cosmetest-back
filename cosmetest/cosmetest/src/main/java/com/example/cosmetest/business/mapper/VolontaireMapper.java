package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.VolontaireDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.domain.model.Volontaire;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité Volontaire et ses DTOs
 */
@Component
public class VolontaireMapper {

    /**
     * Convertit une entité Volontaire en VolontaireDTO (version simplifiée)
     *
     * @param volontaire l'entité à convertir
     * @return le DTO correspondant
     */
    public VolontaireDTO toDTO(Volontaire volontaire) {
        if (volontaire == null) {
            return null;
        }

        VolontaireDTO dto = new VolontaireDTO();
        dto.setIdVol(volontaire.getIdVol());
        dto.setTitreVol(volontaire.getTitreVol());
        dto.setNomVol(volontaire.getNomVol());
        dto.setPrenomVol(volontaire.getPrenomVol());
        dto.setAdresseVol(volontaire.getAdresseVol());
        dto.setCpVol(volontaire.getCpVol());
        dto.setVilleVol(volontaire.getVilleVol());
        dto.setTelDomicileVol(volontaire.getTelDomicileVol());
        dto.setTelPortableVol(volontaire.getTelPortableVol());
        dto.setEmailVol(volontaire.getEmailVol());
        dto.setSexe(volontaire.getSexe());
        dto.setDateNaissance(volontaire.getDateNaissance());
        dto.setArchive(volontaire.getArchive());
        dto.setCommentairesVol(volontaire.getCommentairesVol());
        dto.setEthnie(volontaire.getEthnie());
        dto.setSousEthnie(volontaire.getSousEthnie());
        dto.setPhototype(volontaire.getPhototype());
        dto.setTypePeauVisage(volontaire.getTypePeauVisage());
        dto.setPoids(volontaire.getPoids());
        dto.setTaille(volontaire.getTaille());

        // If dateI is already a LocalDate
        if (volontaire.getDateI() != null) {
            dto.setDateI(volontaire.getDateI());
        }

        dto.setSanteCompatible(volontaire.getSanteCompatible());

        return dto;
    }

    /**
     * Convertit une entité Volontaire en VolontaireDetailDTO (version complète)
     *
     * @param volontaire l'entité à convertir
     * @return le DTO détaillé correspondant
     */
    public VolontaireDetailDTO toDetailDTO(Volontaire volontaire) {
        if (volontaire == null) {
            return null;
        }

        VolontaireDetailDTO dto = new VolontaireDetailDTO();

        // Copier d'abord les champs de base
        dto.setIdVol(volontaire.getIdVol());
        dto.setTitreVol(volontaire.getTitreVol());
        dto.setNomVol(volontaire.getNomVol());
        dto.setPrenomVol(volontaire.getPrenomVol());
        dto.setAdresseVol(volontaire.getAdresseVol());
        dto.setCpVol(volontaire.getCpVol());
        dto.setVilleVol(volontaire.getVilleVol());
        dto.setTelDomicileVol(volontaire.getTelDomicileVol());
        dto.setTelPortableVol(volontaire.getTelPortableVol());
        dto.setEmailVol(volontaire.getEmailVol());
        dto.setSexe(volontaire.getSexe());
        dto.setDateNaissance(volontaire.getDateNaissance());
        dto.setArchive(volontaire.getArchive());
        dto.setCommentairesVol(volontaire.getCommentairesVol());
        dto.setEthnie(volontaire.getEthnie());
        dto.setSousEthnie(volontaire.getSousEthnie());
        dto.setPhototype(volontaire.getPhototype());
        dto.setTypePeauVisage(volontaire.getTypePeauVisage());
        dto.setPoids(volontaire.getPoids());
        dto.setTaille(volontaire.getTaille());

        // If dateI is already a LocalDate
        if (volontaire.getDateI() != null) {
            dto.setDateI(volontaire.getDateI());
        }

        dto.setSanteCompatible(volontaire.getSanteCompatible());

        // Ajout des champs spécifiques au DTO détaillé
        dto.setCarnation(volontaire.getCarnation());
        dto.setBronzage(volontaire.getBronzage());
        dto.setCoupsDeSoleil(volontaire.getCoupsDeSoleil());
        dto.setExpositionSolaire(volontaire.getExpositionSolaire());
        dto.setSensibiliteCutanee(volontaire.getSensibiliteCutanee());

        // Zones de sécheresse
        dto.setSecheresseLevres(volontaire.getSecheresseLevres());
        dto.setSecheresseCou(volontaire.getSecheresseCou());
        dto.setSecheressePoitrineDecollete(volontaire.getSecheressePoitrineDecollete());
        dto.setSecheresseVentreTaille(volontaire.getSecheresseVentreTaille());
        dto.setSecheresseFessesHanches(volontaire.getSecheresseFessesHanches());
        dto.setSecheresseBras(volontaire.getSecheresseBras());
        dto.setSecheresseMains(volontaire.getSecheresseMains());
        dto.setSecheresseJambes(volontaire.getSecheresseJambes());
        dto.setSecheressePieds(volontaire.getSecheressePieds());

        // Taches pigmentaires
        dto.setTachesPigmentairesVisage(volontaire.getTachesPigmentairesVisage());
        dto.setTachesPigmentairesCou(volontaire.getTachesPigmentairesCou());
        dto.setTachesPigmentairesDecollete(volontaire.getTachesPigmentairesDecollete());
        dto.setTachesPigmentairesMains(volontaire.getTachesPigmentairesMains());

        // Perte de fermeté
        dto.setPerteDeFermeteVisage(volontaire.getPerteDeFermeteVisage());
        dto.setPerteDeFermeteCou(volontaire.getPerteDeFermeteCou());
        dto.setPerteDeFermeteDecollete(volontaire.getPerteDeFermeteDecollete());
        dto.setPerteDeFermeteAvantBras(volontaire.getPerteDeFermeteAvantBras());

        // Caractéristiques particulières
        dto.setPilosite(volontaire.getPilosite());
        dto.setCicatrices(volontaire.getCicatrices());
        dto.setTatouages(volontaire.getTatouages());
        dto.setPiercings(volontaire.getPiercings());

        // Vergetures
        dto.setVergeturesJambes(volontaire.getVergeturesJambes());
        dto.setVergeturesFessesHanches(volontaire.getVergeturesFessesHanches());
        dto.setVergeturesVentreTaille(volontaire.getVergeturesVentreTaille());
        dto.setVergeturesPoitrineDecollete(volontaire.getVergeturesPoitrineDecollete());

        // Cellulite
        dto.setCelluliteJambes(volontaire.getCelluliteJambes());
        dto.setCelluliteFessesHanches(volontaire.getCelluliteFessesHanches());
        dto.setCelluliteVentreTaille(volontaire.getCelluliteVentreTaille());
        dto.setCelluliteBras(volontaire.getCelluliteBras());

        // Mesures
        dto.setIhBrasDroit(volontaire.getIhBrasDroit());
        dto.setIhBrasGauche(volontaire.getIhBrasGauche());

        // Cheveux
        dto.setCouleurCheveux(volontaire.getCouleurCheveux());
        dto.setNatureCheveux(volontaire.getNatureCheveux());
        dto.setLongueurCheveux(volontaire.getLongueurCheveux());
        dto.setEpaisseurCheveux(volontaire.getEpaisseurCheveux());
        dto.setNatureCuirChevelu(volontaire.getNatureCuirChevelu());
        dto.setCheveuxAbimes(volontaire.getCheveuxAbimes());
        dto.setCheveuxCassants(volontaire.getCheveuxCassants());
        dto.setCheveuxPlats(volontaire.getCheveuxPlats());
        dto.setCheveuxTernes(volontaire.getCheveuxTernes());
        dto.setPointesFourchues(volontaire.getPointesFourchues());
        dto.setPellicules(volontaire.getPellicules());
        dto.setDemangeaisonsDuCuirChevelu(volontaire.getDemangeaisonsDuCuirChevelu());
        dto.setCuirCheveluSensible(volontaire.getCuirCheveluSensible());
        dto.setChuteDeCheveux(volontaire.getChuteDeCheveux());
        dto.setCalvitie(volontaire.getCalvitie());

        // Cils
        dto.setEpaisseurCils(volontaire.getEpaisseurCils());
        dto.setLongueurCils(volontaire.getLongueurCils());
        dto.setCourbureCils(volontaire.getCourbureCils());
        dto.setCilsAbimes(volontaire.getCilsAbimes());
        dto.setCilsBroussailleux(volontaire.getCilsBroussailleux());
        dto.setChuteDeCils(volontaire.getChuteDeCils());
        dto.setCils(volontaire.getCils());
        dto.setCaracteristiqueSourcils(volontaire.getCaracteristiqueSourcils());

        // Ongles
        dto.setOnglesMous(volontaire.getOnglesMous());
        dto.setOnglesCassants(volontaire.getOnglesCassants());
        dto.setOnglesStries(volontaire.getOnglesStries());
        dto.setOnglesDedoubles(volontaire.getOnglesDedoubles());

        // Visage
        dto.setLesionsRetentionnelles(volontaire.getLesionsRetentionnelles());
        dto.setLesionsInflammatoires(volontaire.getLesionsInflammatoires());
        dto.setCernesPigmentaires(volontaire.getCernesPigmentaires());
        dto.setCernesVasculaires(volontaire.getCernesVasculaires());
        dto.setPoches(volontaire.getPoches());
        dto.setPoresVisibles(volontaire.getPoresVisibles());
        dto.setTeintInhomogene(volontaire.getTeintInhomogene());
        dto.setTeintTerne(volontaire.getTeintTerne());
        dto.setYeux(volontaire.getYeux());
        dto.setLevres(volontaire.getLevres());

        // Santé féminine
        dto.setMenopause(volontaire.getMenopause());
        dto.setThs(volontaire.getThs());
        dto.setContraception(volontaire.getContraception());
        dto.setBouffeeChaleurMenaupose(volontaire.getBouffeeChaleurMenaupose());

        // Antécédents médicaux
        dto.setAnamnese(volontaire.getAnamnese());
        dto.setTraitement(volontaire.getTraitement());
        dto.setAcne(volontaire.getAcne());
        dto.setCouperoseRosacee(volontaire.getCouperoseRosacee());
        dto.setPsoriasis(volontaire.getPsoriasis());
        dto.setDermiteSeborrheique(volontaire.getDermiteSeborrheique());
        dto.setEczema(volontaire.getEczema());
        dto.setAngiome(volontaire.getAngiome());
        dto.setPityriasis(volontaire.getPityriasis());
        dto.setVitiligo(volontaire.getVitiligo());
        dto.setMelanome(volontaire.getMelanome());
        dto.setZona(volontaire.getZona());
        dto.setHerpes(volontaire.getHerpes());
        dto.setPelade(volontaire.getPelade());

        // Allergies
        dto.setReactionAllergique(volontaire.getReactionAllergique());
        dto.setAllergiesCommentaires(volontaire.getAllergiesCommentaires());
        dto.setDesensibilisation(volontaire.getDesensibilisation());
        dto.setTerrainAtopique(volontaire.getTerrainAtopique());

        // Scores
        dto.setScorePod(volontaire.getScorePod());
        dto.setScorePog(volontaire.getScorePog());
        dto.setScoreFront(volontaire.getScoreFront());
        dto.setScoreLion(volontaire.getScoreLion());
        dto.setScorePpd(volontaire.getScorePpd());
        dto.setScorePpg(volontaire.getScorePpg());
        dto.setScoreDod(volontaire.getScoreDod());
        dto.setScoreDog(volontaire.getScoreDog());
        dto.setScoreSngd(volontaire.getScoreSngd());
        dto.setScoreSngg(volontaire.getScoreSngg());
        dto.setScoreLevsup(volontaire.getScoreLevsup());
        dto.setScoreComlevd(volontaire.getScoreComlevd());
        dto.setScoreComlevg(volontaire.getScoreComlevg());
        dto.setScorePtose(volontaire.getScorePtose());
        dto.setIta(volontaire.getIta());

        // Divers
        dto.setOriginePere(volontaire.getOriginePere());
        dto.setOrigineMere(volontaire.getOrigineMere());
        dto.setNbCigarettesJour(volontaire.getNbCigarettesJour());
        dto.setHauteurSiege(volontaire.getHauteurSiege());
        dto.setMapyeux(volontaire.getMapyeux());
        dto.setMaplevres(volontaire.getMaplevres());
        dto.setMapsourcils(volontaire.getMapsourcils());
        dto.setNotes(volontaire.getNotes());
        dto.setNotesYeux(volontaire.getNotesYeux());
        dto.setNotesLevres(volontaire.getNotesLevres());
        dto.setNotesTeint(volontaire.getNotesTeint());
        dto.setNotesCynetique(volontaire.getNotesCynetique());

        return dto;
    }

    /**
     * Convertit un VolontaireDTO en entité Volontaire
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Volontaire toEntity(VolontaireDTO dto) {
        if (dto == null) {
            return null;
        }

        Volontaire volontaire = new Volontaire();
        volontaire.setIdVol(dto.getIdVol());
        volontaire.setTitreVol(dto.getTitreVol());
        volontaire.setNomVol(dto.getNomVol());
        volontaire.setPrenomVol(dto.getPrenomVol());
        volontaire.setAdresseVol(dto.getAdresseVol());
        volontaire.setCpVol(dto.getCpVol());
        volontaire.setVilleVol(dto.getVilleVol());
        volontaire.setTelDomicileVol(dto.getTelDomicileVol());
        volontaire.setTelPortableVol(dto.getTelPortableVol());
        volontaire.setEmailVol(dto.getEmailVol());
        volontaire.setSexe(dto.getSexe());

        // Conversion de LocalDate en Date SQL
        if (dto.getDateNaissance() != null) {
            volontaire.setDateNaissance(Date.valueOf(dto.getDateNaissance()));
        }

        volontaire.setArchive(dto.getArchive());
        volontaire.setCommentairesVol(dto.getCommentairesVol());
        volontaire.setEthnie(dto.getEthnie());
        volontaire.setSousEthnie(dto.getSousEthnie());
        volontaire.setPhototype(dto.getPhototype());
        volontaire.setTypePeauVisage(dto.getTypePeauVisage());
        volontaire.setPoids(dto.getPoids());
        volontaire.setTaille(dto.getTaille());

        // Conversion de LocalDate en Date SQL
        if (dto.getDateI() != null) {
            volontaire.setDateI(Date.valueOf(dto.getDateI()));
        }

        volontaire.setSanteCompatible(dto.getSanteCompatible());

        return volontaire;
    }

    /**
     * Convertit un VolontaireDetailDTO en entité Volontaire
     *
     * @param dto le DTO détaillé à convertir
     * @return l'entité correspondante
     */
    public Volontaire toEntity(VolontaireDetailDTO dto) {
        if (dto == null) {
            return null;
        }

        Volontaire volontaire = toEntity((VolontaireDTO) dto);

        // Ajout des champs spécifiques au DTO détaillé
        volontaire.setCarnation(dto.getCarnation());
        volontaire.setBronzage(dto.getBronzage());
        volontaire.setCoupsDeSoleil(dto.getCoupsDeSoleil());
        volontaire.setExpositionSolaire(dto.getExpositionSolaire());
        volontaire.setSensibiliteCutanee(dto.getSensibiliteCutanee());

        // Zones de sécheresse
        volontaire.setSecheresseLevres(dto.getSecheresseLevres());
        volontaire.setSecheresseCou(dto.getSecheresseCou());
        volontaire.setSecheressePoitrineDecollete(dto.getSecheressePoitrineDecollete());
        volontaire.setSecheresseVentreTaille(dto.getSecheresseVentreTaille());
        volontaire.setSecheresseFessesHanches(dto.getSecheresseFessesHanches());
        volontaire.setSecheresseBras(dto.getSecheresseBras());
        volontaire.setSecheresseMains(dto.getSecheresseMains());
        volontaire.setSecheresseJambes(dto.getSecheresseJambes());
        volontaire.setSecheressePieds(dto.getSecheressePieds());

        // Taches pigmentaires
        volontaire.setTachesPigmentairesVisage(dto.getTachesPigmentairesVisage());
        volontaire.setTachesPigmentairesCou(dto.getTachesPigmentairesCou());
        volontaire.setTachesPigmentairesDecollete(dto.getTachesPigmentairesDecollete());
        volontaire.setTachesPigmentairesMains(dto.getTachesPigmentairesMains());

        // Perte de fermeté
        volontaire.setPerteDeFermeteVisage(dto.getPerteDeFermeteVisage());
        volontaire.setPerteDeFermeteCou(dto.getPerteDeFermeteCou());
        volontaire.setPerteDeFermeteDecollete(dto.getPerteDeFermeteDecollete());
        volontaire.setPerteDeFermeteAvantBras(dto.getPerteDeFermeteAvantBras());

        // Caractéristiques particulières
        volontaire.setPilosite(dto.getPilosite());
        volontaire.setCicatrices(dto.getCicatrices());
        volontaire.setTatouages(dto.getTatouages());
        volontaire.setPiercings(dto.getPiercings());

        // Vergetures
        volontaire.setVergeturesJambes(dto.getVergeturesJambes());
        volontaire.setVergeturesFessesHanches(dto.getVergeturesFessesHanches());
        volontaire.setVergeturesVentreTaille(dto.getVergeturesVentreTaille());
        volontaire.setVergeturesPoitrineDecollete(dto.getVergeturesPoitrineDecollete());

        // Cellulite
        volontaire.setCelluliteJambes(dto.getCelluliteJambes());
        volontaire.setCelluliteFessesHanches(dto.getCelluliteFessesHanches());
        volontaire.setCelluliteVentreTaille(dto.getCelluliteVentreTaille());
        volontaire.setCelluliteBras(dto.getCelluliteBras());

        // Mesures
        volontaire.setIhBrasDroit(dto.getIhBrasDroit());
        volontaire.setIhBrasGauche(dto.getIhBrasGauche());

        // Cheveux
        volontaire.setCouleurCheveux(dto.getCouleurCheveux());
        volontaire.setNatureCheveux(dto.getNatureCheveux());
        volontaire.setLongueurCheveux(dto.getLongueurCheveux());
        volontaire.setEpaisseurCheveux(dto.getEpaisseurCheveux());
        volontaire.setNatureCuirChevelu(dto.getNatureCuirChevelu());
        volontaire.setCheveuxAbimes(dto.getCheveuxAbimes());
        volontaire.setCheveuxCassants(dto.getCheveuxCassants());
        volontaire.setCheveuxPlats(dto.getCheveuxPlats());
        volontaire.setCheveuxTernes(dto.getCheveuxTernes());
        volontaire.setPointesFourchues(dto.getPointesFourchues());
        volontaire.setPellicules(dto.getPellicules());
        volontaire.setDemangeaisonsDuCuirChevelu(dto.getDemangeaisonsDuCuirChevelu());
        volontaire.setCuirCheveluSensible(dto.getCuirCheveluSensible());
        volontaire.setChuteDeCheveux(dto.getChuteDeCheveux());
        volontaire.setCalvitie(dto.getCalvitie());

        // Cils
        volontaire.setEpaisseurCils(dto.getEpaisseurCils());
        volontaire.setLongueurCils(dto.getLongueurCils());
        volontaire.setCourbureCils(dto.getCourbureCils());
        volontaire.setCilsAbimes(dto.getCilsAbimes());
        volontaire.setCilsBroussailleux(dto.getCilsBroussailleux());
        volontaire.setChuteDeCils(dto.getChuteDeCils());
        volontaire.setCils(dto.getCils());
        volontaire.setCaracteristiqueSourcils(dto.getCaracteristiqueSourcils());

        // Ongles
        volontaire.setOnglesMous(dto.getOnglesMous());
        volontaire.setOnglesCassants(dto.getOnglesCassants());
        volontaire.setOnglesStries(dto.getOnglesStries());
        volontaire.setOnglesDedoubles(dto.getOnglesDedoubles());

        // Visage
        volontaire.setLesionsRetentionnelles(dto.getLesionsRetentionnelles());
        volontaire.setLesionsInflammatoires(dto.getLesionsInflammatoires());
        volontaire.setCernesPigmentaires(dto.getCernesPigmentaires());
        volontaire.setCernesVasculaires(dto.getCernesVasculaires());
        volontaire.setPoches(dto.getPoches());
        volontaire.setPoresVisibles(dto.getPoresVisibles());
        volontaire.setTeintInhomogene(dto.getTeintInhomogene());
        volontaire.setTeintTerne(dto.getTeintTerne());
        volontaire.setYeux(dto.getYeux());
        volontaire.setLevres(dto.getLevres());

        // Santé féminine
        volontaire.setMenopause(dto.getMenopause());
        volontaire.setThs(dto.getThs());
        volontaire.setContraception(dto.getContraception());
        volontaire.setBouffeeChaleurMenaupose(dto.getBouffeeChaleurMenaupose());

        // Antécédents médicaux
        volontaire.setAnamnese(dto.getAnamnese());
        volontaire.setTraitement(dto.getTraitement());
        volontaire.setAcne(dto.getAcne());
        volontaire.setCouperoseRosacee(dto.getCouperoseRosacee());
        volontaire.setPsoriasis(dto.getPsoriasis());
        volontaire.setDermiteSeborrheique(dto.getDermiteSeborrheique());
        volontaire.setEczema(dto.getEczema());
        volontaire.setAngiome(dto.getAngiome());
        volontaire.setPityriasis(dto.getPityriasis());
        volontaire.setVitiligo(dto.getVitiligo());
        volontaire.setMelanome(dto.getMelanome());
        volontaire.setZona(dto.getZona());
        volontaire.setHerpes(dto.getHerpes());
        volontaire.setPelade(dto.getPelade());

        // Allergies
        volontaire.setReactionAllergique(dto.getReactionAllergique());
        volontaire.setAllergiesCommentaires(dto.getAllergiesCommentaires());
        volontaire.setDesensibilisation(dto.getDesensibilisation());
        volontaire.setTerrainAtopique(dto.getTerrainAtopique());

        // Scores
        volontaire.setScorePod(dto.getScorePod());
        volontaire.setScorePog(dto.getScorePog());
        volontaire.setScoreFront(dto.getScoreFront());
        volontaire.setScoreLion(dto.getScoreLion());
        volontaire.setScorePpd(dto.getScorePpd());
        volontaire.setScorePpg(dto.getScorePpg());
        volontaire.setScoreDod(dto.getScoreDod());
        volontaire.setScoreDog(dto.getScoreDog());
        volontaire.setScoreSngd(dto.getScoreSngd());
        volontaire.setScoreSngg(dto.getScoreSngg());
        volontaire.setScoreLevsup(dto.getScoreLevsup());
        volontaire.setScoreComlevd(dto.getScoreComlevd());
        volontaire.setScoreComlevg(dto.getScoreComlevg());
        volontaire.setScorePtose(dto.getScorePtose());
        volontaire.setIta(dto.getIta());

        // Divers
        volontaire.setOriginePere(dto.getOriginePere());
        volontaire.setOrigineMere(dto.getOrigineMere());
        volontaire.setNbCigarettesJour(dto.getNbCigarettesJour());
        volontaire.setHauteurSiege(dto.getHauteurSiege());
        volontaire.setMapyeux(dto.getMapyeux());
        volontaire.setMaplevres(dto.getMaplevres());
        volontaire.setMapsourcils(dto.getMapsourcils());
        volontaire.setNotesYeux(dto.getNotesYeux());
        volontaire.setNotesLevres(dto.getNotesLevres());
        volontaire.setNotesTeint(dto.getNotesTeint());
        volontaire.setNotesCynetique(dto.getNotesCynetique());

        return volontaire;
    }

    /**
     * Convertit une liste d'entités Volontaire en liste de VolontaireDTO
     *
     * @param volontaires la liste d'entités
     * @return la liste de DTOs
     */
    public List<VolontaireDTO> toDTOList(List<Volontaire> volontaires) {
        return volontaires.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'entités Volontaire en liste de VolontaireDetailDTO
     *
     * @param volontaires la liste d'entités
     * @return la liste de DTOs détaillés
     */
    public List<VolontaireDetailDTO> toDetailDTOList(List<Volontaire> volontaires) {
        return volontaires.stream()
                .map(this::toDetailDTO)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Volontaire existante avec les données d'un
     * VolontaireDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto    les données à appliquer
     * @return l'entité mise à jour
     */
    public Volontaire updateEntityFromDTO(Volontaire entity, VolontaireDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        if (dto.getTitreVol() != null)
            entity.setTitreVol(dto.getTitreVol());
        if (dto.getNomVol() != null)
            entity.setNomVol(dto.getNomVol());
        if (dto.getPrenomVol() != null)
            entity.setPrenomVol(dto.getPrenomVol());
        if (dto.getAdresseVol() != null)
            entity.setAdresseVol(dto.getAdresseVol());
        if (dto.getCpVol() != null)
            entity.setCpVol(dto.getCpVol());
        if (dto.getVilleVol() != null)
            entity.setVilleVol(dto.getVilleVol());
        if (dto.getTelDomicileVol() != null)
            entity.setTelDomicileVol(dto.getTelDomicileVol());
        if (dto.getTelPortableVol() != null)
            entity.setTelPortableVol(dto.getTelPortableVol());
        if (dto.getEmailVol() != null)
            entity.setEmailVol(dto.getEmailVol());
        if (dto.getSexe() != null)
            entity.setSexe(dto.getSexe());

        // Conversion de LocalDate en Date SQL
        if (dto.getDateNaissance() != null) {
            entity.setDateNaissance(Date.valueOf(dto.getDateNaissance()));
        }

        if (dto.getArchive() != null)
            entity.setArchive(dto.getArchive());
        if (dto.getCommentairesVol() != null)
            entity.setCommentairesVol(dto.getCommentairesVol());
        if (dto.getEthnie() != null)
            entity.setEthnie(dto.getEthnie());
        if (dto.getSousEthnie() != null)
            entity.setSousEthnie(dto.getSousEthnie());
        if (dto.getPhototype() != null)
            entity.setPhototype(dto.getPhototype());
        if (dto.getTypePeauVisage() != null)
            entity.setTypePeauVisage(dto.getTypePeauVisage());
        if (dto.getPoids() != null)
            entity.setPoids(dto.getPoids());
        if (dto.getTaille() != null)
            entity.setTaille(dto.getTaille());

        // Conversion de LocalDate en Date SQL
        if (dto.getDateI() != null) {
            entity.setDateI(Date.valueOf(dto.getDateI()));
        }

        if (dto.getSanteCompatible() != null)
            entity.setSanteCompatible(dto.getSanteCompatible());

        return entity;
    }

    /**
     * Met à jour une entité Volontaire existante avec les données d'un
     * VolontaireDetailDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto    les données détaillées à appliquer
     * @return l'entité mise à jour
     */
    public Volontaire updateEntityFromDetailDTO(Volontaire entity, VolontaireDetailDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        // Mettre à jour les champs de base
        entity = updateEntityFromDTO(entity, dto);

        // Mettre à jour les champs spécifiques au DTO détaillé
        if (dto.getCarnation() != null)
            entity.setCarnation(dto.getCarnation());
        if (dto.getBronzage() != null)
            entity.setBronzage(dto.getBronzage());
        if (dto.getCoupsDeSoleil() != null)
            entity.setCoupsDeSoleil(dto.getCoupsDeSoleil());
        if (dto.getExpositionSolaire() != null)
            entity.setExpositionSolaire(dto.getExpositionSolaire());
        if (dto.getSensibiliteCutanee() != null)
            entity.setSensibiliteCutanee(dto.getSensibiliteCutanee());

        // Zones de sécheresse
        if (dto.getSecheresseLevres() != null)
            entity.setSecheresseLevres(dto.getSecheresseLevres());
        if (dto.getSecheresseCou() != null)
            entity.setSecheresseCou(dto.getSecheresseCou());
        if (dto.getSecheressePoitrineDecollete() != null)
            entity.setSecheressePoitrineDecollete(dto.getSecheressePoitrineDecollete());
        if (dto.getSecheresseVentreTaille() != null)
            entity.setSecheresseVentreTaille(dto.getSecheresseVentreTaille());
        if (dto.getSecheresseFessesHanches() != null)
            entity.setSecheresseFessesHanches(dto.getSecheresseFessesHanches());
        if (dto.getSecheresseBras() != null)
            entity.setSecheresseBras(dto.getSecheresseBras());
        if (dto.getSecheresseMains() != null)
            entity.setSecheresseMains(dto.getSecheresseMains());
        if (dto.getSecheresseJambes() != null)
            entity.setSecheresseJambes(dto.getSecheresseJambes());
        if (dto.getSecheressePieds() != null)
            entity.setSecheressePieds(dto.getSecheressePieds());

        // Taches pigmentaires
        if (dto.getTachesPigmentairesVisage() != null)
            entity.setTachesPigmentairesVisage(dto.getTachesPigmentairesVisage());
        if (dto.getTachesPigmentairesCou() != null)
            entity.setTachesPigmentairesCou(dto.getTachesPigmentairesCou());
        if (dto.getTachesPigmentairesDecollete() != null)
            entity.setTachesPigmentairesDecollete(dto.getTachesPigmentairesDecollete());
        if (dto.getTachesPigmentairesMains() != null)
            entity.setTachesPigmentairesMains(dto.getTachesPigmentairesMains());

        // Perte de fermeté
        if (dto.getPerteDeFermeteVisage() != null)
            entity.setPerteDeFermeteVisage(dto.getPerteDeFermeteVisage());
        if (dto.getPerteDeFermeteCou() != null)
            entity.setPerteDeFermeteCou(dto.getPerteDeFermeteCou());
        if (dto.getPerteDeFermeteDecollete() != null)
            entity.setPerteDeFermeteDecollete(dto.getPerteDeFermeteDecollete());
        if (dto.getPerteDeFermeteAvantBras() != null)
            entity.setPerteDeFermeteAvantBras(dto.getPerteDeFermeteAvantBras());

        // Caractéristiques particulières
        if (dto.getPilosite() != null)
            entity.setPilosite(dto.getPilosite());
        if (dto.getCicatrices() != null)
            entity.setCicatrices(dto.getCicatrices());
        if (dto.getTatouages() != null)
            entity.setTatouages(dto.getTatouages());
        if (dto.getPiercings() != null)
            entity.setPiercings(dto.getPiercings());

        // Vergetures
        if (dto.getVergeturesJambes() != null)
            entity.setVergeturesJambes(dto.getVergeturesJambes());
        if (dto.getVergeturesFessesHanches() != null)
            entity.setVergeturesFessesHanches(dto.getVergeturesFessesHanches());
        if (dto.getVergeturesVentreTaille() != null)
            entity.setVergeturesVentreTaille(dto.getVergeturesVentreTaille());
        if (dto.getVergeturesPoitrineDecollete() != null)
            entity.setVergeturesPoitrineDecollete(dto.getVergeturesPoitrineDecollete());

        // Cellulite
        if (dto.getCelluliteJambes() != null)
            entity.setCelluliteJambes(dto.getCelluliteJambes());
        if (dto.getCelluliteFessesHanches() != null)
            entity.setCelluliteFessesHanches(dto.getCelluliteFessesHanches());
        if (dto.getCelluliteVentreTaille() != null)
            entity.setCelluliteVentreTaille(dto.getCelluliteVentreTaille());
        if (dto.getCelluliteBras() != null)
            entity.setCelluliteBras(dto.getCelluliteBras());

        // Mesures
        if (dto.getIhBrasDroit() != null)
            entity.setIhBrasDroit(dto.getIhBrasDroit());
        if (dto.getIhBrasGauche() != null)
            entity.setIhBrasGauche(dto.getIhBrasGauche());

        // Cheveux
        if (dto.getCouleurCheveux() != null)
            entity.setCouleurCheveux(dto.getCouleurCheveux());
        if (dto.getNatureCheveux() != null)
            entity.setNatureCheveux(dto.getNatureCheveux());
        if (dto.getLongueurCheveux() != null)
            entity.setLongueurCheveux(dto.getLongueurCheveux());
        if (dto.getEpaisseurCheveux() != null)
            entity.setEpaisseurCheveux(dto.getEpaisseurCheveux());
        if (dto.getNatureCuirChevelu() != null)
            entity.setNatureCuirChevelu(dto.getNatureCuirChevelu());
        if (dto.getCheveuxAbimes() != null)
            entity.setCheveuxAbimes(dto.getCheveuxAbimes());
        if (dto.getCheveuxCassants() != null)
            entity.setCheveuxCassants(dto.getCheveuxCassants());
        if (dto.getCheveuxPlats() != null)
            entity.setCheveuxPlats(dto.getCheveuxPlats());
        if (dto.getCheveuxTernes() != null)
            entity.setCheveuxTernes(dto.getCheveuxTernes());
        if (dto.getPointesFourchues() != null)
            entity.setPointesFourchues(dto.getPointesFourchues());
        if (dto.getPellicules() != null)
            entity.setPellicules(dto.getPellicules());
        if (dto.getDemangeaisonsDuCuirChevelu() != null)
            entity.setDemangeaisonsDuCuirChevelu(dto.getDemangeaisonsDuCuirChevelu());
        if (dto.getCuirCheveluSensible() != null)
            entity.setCuirCheveluSensible(dto.getCuirCheveluSensible());
        if (dto.getChuteDeCheveux() != null)
            entity.setChuteDeCheveux(dto.getChuteDeCheveux());
        if (dto.getCalvitie() != null)
            entity.setCalvitie(dto.getCalvitie());

        // Cils
        if (dto.getEpaisseurCils() != null)
            entity.setEpaisseurCils(dto.getEpaisseurCils());
        if (dto.getLongueurCils() != null)
            entity.setLongueurCils(dto.getLongueurCils());
        if (dto.getCourbureCils() != null)
            entity.setCourbureCils(dto.getCourbureCils());
        if (dto.getCilsAbimes() != null)
            entity.setCilsAbimes(dto.getCilsAbimes());
        if (dto.getCilsBroussailleux() != null)
            entity.setCilsBroussailleux(dto.getCilsBroussailleux());
        if (dto.getChuteDeCils() != null)
            entity.setChuteDeCils(dto.getChuteDeCils());
        if (dto.getCils() != null)
            entity.setCils(dto.getCils());
        if (dto.getCaracteristiqueSourcils() != null)
            entity.setCaracteristiqueSourcils(dto.getCaracteristiqueSourcils());

        // Ongles
        if (dto.getOnglesMous() != null)
            entity.setOnglesMous(dto.getOnglesMous());
        if (dto.getOnglesCassants() != null)
            entity.setOnglesCassants(dto.getOnglesCassants());
        if (dto.getOnglesStries() != null)
            entity.setOnglesStries(dto.getOnglesStries());
        if (dto.getOnglesDedoubles() != null)
            entity.setOnglesDedoubles(dto.getOnglesDedoubles());

        // Visage
        if (dto.getLesionsRetentionnelles() != null)
            entity.setLesionsRetentionnelles(dto.getLesionsRetentionnelles());
        if (dto.getLesionsInflammatoires() != null)
            entity.setLesionsInflammatoires(dto.getLesionsInflammatoires());
        if (dto.getCernesPigmentaires() != null)
            entity.setCernesPigmentaires(dto.getCernesPigmentaires());
        if (dto.getCernesVasculaires() != null)
            entity.setCernesVasculaires(dto.getCernesVasculaires());
        if (dto.getPoches() != null)
            entity.setPoches(dto.getPoches());
        if (dto.getPoresVisibles() != null)
            entity.setPoresVisibles(dto.getPoresVisibles());
        if (dto.getTeintInhomogene() != null)
            entity.setTeintInhomogene(dto.getTeintInhomogene());
        if (dto.getTeintTerne() != null)
            entity.setTeintTerne(dto.getTeintTerne());
        if (dto.getYeux() != null)
            entity.setYeux(dto.getYeux());
        if (dto.getLevres() != null)
            entity.setLevres(dto.getLevres());

        // Santé féminine
        if (dto.getMenopause() != null)
            entity.setMenopause(dto.getMenopause());
        if (dto.getThs() != null)
            entity.setThs(dto.getThs());
        if (dto.getContraception() != null)
            entity.setContraception(dto.getContraception());
        if (dto.getBouffeeChaleurMenaupose() != null)
            entity.setBouffeeChaleurMenaupose(dto.getBouffeeChaleurMenaupose());

        // Antécédents médicaux
        if (dto.getAnamnese() != null)
            entity.setAnamnese(dto.getAnamnese());
        if (dto.getTraitement() != null)
            entity.setTraitement(dto.getTraitement());
        if (dto.getAcne() != null)
            entity.setAcne(dto.getAcne());
        if (dto.getCouperoseRosacee() != null)
            entity.setCouperoseRosacee(dto.getCouperoseRosacee());
        if (dto.getPsoriasis() != null)
            entity.setPsoriasis(dto.getPsoriasis());
        if (dto.getDermiteSeborrheique() != null)
            entity.setDermiteSeborrheique(dto.getDermiteSeborrheique());
        if (dto.getEczema() != null)
            entity.setEczema(dto.getEczema());
        if (dto.getAngiome() != null)
            entity.setAngiome(dto.getAngiome());
        if (dto.getPityriasis() != null)
            entity.setPityriasis(dto.getPityriasis());
        if (dto.getVitiligo() != null)
            entity.setVitiligo(dto.getVitiligo());
        if (dto.getMelanome() != null)
            entity.setMelanome(dto.getMelanome());
        if (dto.getZona() != null)
            entity.setZona(dto.getZona());
        if (dto.getHerpes() != null)
            entity.setHerpes(dto.getHerpes());
        if (dto.getPelade() != null)
            entity.setPelade(dto.getPelade());

        // Allergies
        if (dto.getReactionAllergique() != null)
            entity.setReactionAllergique(dto.getReactionAllergique());
        if (dto.getAllergiesCommentaires() != null)
            entity.setAllergiesCommentaires(dto.getAllergiesCommentaires());
        if (dto.getDesensibilisation() != null)
            entity.setDesensibilisation(dto.getDesensibilisation());
        if (dto.getTerrainAtopique() != null)
            entity.setTerrainAtopique(dto.getTerrainAtopique());

        // Scores
        if (dto.getScorePod() != null)
            entity.setScorePod(dto.getScorePod());
        if (dto.getScorePog() != null)
            entity.setScorePog(dto.getScorePog());
        if (dto.getScoreFront() != null)
            entity.setScoreFront(dto.getScoreFront());
        if (dto.getScoreLion() != null)
            entity.setScoreLion(dto.getScoreLion());
        if (dto.getScorePpd() != null)
            entity.setScorePpd(dto.getScorePpd());
        if (dto.getScorePpg() != null)
            entity.setScorePpg(dto.getScorePpg());
        if (dto.getScoreDod() != null)
            entity.setScoreDod(dto.getScoreDod());
        if (dto.getScoreDog() != null)
            entity.setScoreDog(dto.getScoreDog());
        if (dto.getScoreSngd() != null)
            entity.setScoreSngd(dto.getScoreSngd());
        if (dto.getScoreSngg() != null)
            entity.setScoreSngg(dto.getScoreSngg());
        if (dto.getScoreLevsup() != null)
            entity.setScoreLevsup(dto.getScoreLevsup());
        if (dto.getScoreComlevd() != null)
            entity.setScoreComlevd(dto.getScoreComlevd());
        if (dto.getScoreComlevg() != null)
            entity.setScoreComlevg(dto.getScoreComlevg());
        if (dto.getScorePtose() != null)
            entity.setScorePtose(dto.getScorePtose());
        if (dto.getIta() != null)
            entity.setIta(dto.getIta());

        // Divers
        if (dto.getOriginePere() != null)
            entity.setOriginePere(dto.getOriginePere());
        if (dto.getOrigineMere() != null)
            entity.setOrigineMere(dto.getOrigineMere());
        if (dto.getNbCigarettesJour() != null)
            entity.setNbCigarettesJour(dto.getNbCigarettesJour());
        entity.setHauteurSiege(dto.getHauteurSiege());
        if (dto.getMapyeux() != null)
            entity.setMapyeux(dto.getMapyeux());
        if (dto.getMaplevres() != null)
            entity.setMaplevres(dto.getMaplevres());
        if (dto.getMapsourcils() != null)
            entity.setMapsourcils(dto.getMapsourcils());

        // Notes et évaluations
        if (dto.getNotes() != null)
            entity.setNotes(dto.getNotes());
        if (dto.getNotesYeux() != null)
            entity.setNotesYeux(dto.getNotesYeux());
        if (dto.getNotesLevres() != null)
            entity.setNotesLevres(dto.getNotesLevres());
        if (dto.getNotesTeint() != null)
            entity.setNotesTeint(dto.getNotesTeint());
        if (dto.getNotesCynetique() != null)
            entity.setNotesCynetique(dto.getNotesCynetique());

        return entity;
    }
}