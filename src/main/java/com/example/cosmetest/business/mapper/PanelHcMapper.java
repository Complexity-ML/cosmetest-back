package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.PanelHcDTO;
import com.example.cosmetest.domain.model.PanelHc;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité PanelHc et PanelHcDTO
 */
@Component
public class PanelHcMapper {

    /**
     * Convertit une entité PanelHc en PanelHcDTO
     *
     * @param panelHc l'entité à convertir
     * @return le DTO correspondant
     */
    public PanelHcDTO toDTO(PanelHc panelHc) {
        if (panelHc == null) {
            return null;
        }

        PanelHcDTO dto = new PanelHcDTO();
        dto.setIdPanel(panelHc.getIdPanel());
        dto.setCire(panelHc.getCire());
        dto.setCremeDepilatoire(panelHc.getCremeDepilatoire());
        dto.setEpilateurElectrique(panelHc.getEpilateurElectrique());
        dto.setRasoir(panelHc.getRasoir());
        dto.setInstitut(panelHc.getInstitut());
        dto.setEpilationDefinitive(panelHc.getEpilationDefinitive());
        dto.setAchatInstitutParfumerie(panelHc.getAchatInstitutParfumerie());
        dto.setAchatGrandesSurfaces(panelHc.getAchatGrandesSurfaces());
        dto.setAchatPharmacieParapharmacie(panelHc.getAchatPharmacieParapharmacie());
        dto.setAchatInternet(panelHc.getAchatInternet());
        dto.setProduitsBio(panelHc.getProduitsBio());
        dto.setSavon(panelHc.getSavon());
        dto.setGelDouche(panelHc.getGelDouche());
        dto.setLaitDouche(panelHc.getLaitDouche());
        dto.setNettoyantIntime(panelHc.getNettoyantIntime());
        dto.setProduitsBain(panelHc.getProduitsBain());
        dto.setDeodorant(panelHc.getDeodorant());
        dto.setAntiTranspirant(panelHc.getAntiTranspirant());
        dto.setEauDeToilette(panelHc.getEauDeToilette());
        dto.setParfum(panelHc.getParfum());
        dto.setGelNettoyant(panelHc.getGelNettoyant());
        dto.setDemaquillantVisage(panelHc.getDemaquillantVisage());
        dto.setTonique(panelHc.getTonique());
        dto.setLotionMicellaire(panelHc.getLotionMicellaire());
        dto.setDemaquillantYeux(panelHc.getDemaquillantYeux());
        dto.setDemaquillantWaterproof(panelHc.getDemaquillantWaterproof());
        dto.setDissolvantOngles(panelHc.getDissolvantOngles());
        dto.setSoinHydratantVisage(panelHc.getSoinHydratantVisage());
        dto.setSoinNourissantVisage(panelHc.getSoinNourissantVisage());
        dto.setSoinAntiRidesVisage(panelHc.getSoinAntiRidesVisage());
        dto.setSoinAntiAgeVisage(panelHc.getSoinAntiAgeVisage());
        dto.setSoinRaffermissantVisage(panelHc.getSoinRaffermissantVisage());
        dto.setGommageVisage(panelHc.getGommageVisage());
        dto.setMasqueVisage(panelHc.getMasqueVisage());
        dto.setSoinAntiTachesVisage(panelHc.getSoinAntiTachesVisage());
        dto.setSoinAntiRougeursVisage(panelHc.getSoinAntiRougeursVisage());
        dto.setSoinMatifiantVisage(panelHc.getSoinMatifiantVisage());
        dto.setSoinEclatDuTeint(panelHc.getSoinEclatDuTeint());
        dto.setSoinContourDesYeux(panelHc.getSoinContourDesYeux());
        dto.setSoinContourDesLevres(panelHc.getSoinContourDesLevres());
        dto.setSoinHydratantCorps(panelHc.getSoinHydratantCorps());
        dto.setSoinNourrissantCorps(panelHc.getSoinNourrissantCorps());
        dto.setSoinAntiAgeCorps(panelHc.getSoinAntiAgeCorps());
        dto.setSoinRaffermissantCorps(panelHc.getSoinRaffermissantCorps());
        dto.setGommageCorps(panelHc.getGommageCorps());
        dto.setMasqueCorps(panelHc.getMasqueCorps());
        dto.setSoinAntiTachesDecollete(panelHc.getSoinAntiTachesDecollete());
        dto.setSoinAmincissant(panelHc.getSoinAmincissant());
        dto.setSoinAntiCellulite(panelHc.getSoinAntiCellulite());
        dto.setSoinAntiVergetures(panelHc.getSoinAntiVergetures());
        dto.setSoinHydratantMains(panelHc.getSoinHydratantMains());
        dto.setSoinNourrissantMains(panelHc.getSoinNourrissantMains());
        dto.setSoinAntiAgeMains(panelHc.getSoinAntiAgeMains());
        dto.setSoinAntiTachesMains(panelHc.getSoinAntiTachesMains());
        dto.setSoinPieds(panelHc.getSoinPieds());
        dto.setSoinOngles(panelHc.getSoinOngles());
        dto.setManucures(panelHc.getManucures());
        dto.setCremeTeintee(panelHc.getCremeTeintee());
        dto.setBaseMaquillage(panelHc.getBaseMaquillage());
        dto.setCorrecteurTeint(panelHc.getCorrecteurTeint());
        dto.setAnticerne(panelHc.getAnticerne());
        dto.setFondDeTeint(panelHc.getFondDeTeint());
        dto.setPoudreLibre(panelHc.getPoudreLibre());
        dto.setBlushFardAJoues(panelHc.getBlushFardAJoues());
        dto.setMascara(panelHc.getMascara());
        dto.setMascaraWaterproof(panelHc.getMascaraWaterproof());
        dto.setFardAPaupieres(panelHc.getFardAPaupieres());
        dto.setCrayonsYeux(panelHc.getCrayonsYeux());
        dto.setEyeliner(panelHc.getEyeliner());
        dto.setFauxCils(panelHc.getFauxCils());
        dto.setMaquillageDesSourcils(panelHc.getMaquillageDesSourcils());
        dto.setRougeALevres(panelHc.getRougeALevres());
        dto.setGloss(panelHc.getGloss());
        dto.setCrayonLevres(panelHc.getCrayonLevres());
        dto.setVernisAOngles(panelHc.getVernisAOngles());
        dto.setFauxOngles(panelHc.getFauxOngles());
        dto.setMaquillagePermanentYeux(panelHc.getMaquillagePermanentYeux());
        dto.setMaquillagePermanentSourcils(panelHc.getMaquillagePermanentSourcils());
        dto.setMaquillagePermanentLevres(panelHc.getMaquillagePermanentLevres());
        dto.setShampoing(panelHc.getShampoing());
        dto.setApresShampoing(panelHc.getApresShampoing());
        dto.setMasqueCapillaire(panelHc.getMasqueCapillaire());
        dto.setColorationMeches(panelHc.getColorationMeches());
        dto.setProduitCoiffantFixant(panelHc.getProduitCoiffantFixant());
        dto.setPermanente(panelHc.getPermanente());
        dto.setLissageDefrisage(panelHc.getLissageDefrisage());
        dto.setExtensionsCapillaires(panelHc.getExtensionsCapillaires());
        dto.setProtecteurSolaireVisage(panelHc.getProtecteurSolaireVisage());
        dto.setProtecteurSolaireCorps(panelHc.getProtecteurSolaireCorps());
        dto.setProtecteurSolaireLevres(panelHc.getProtecteurSolaireLevres());
        dto.setSoinApresSoleil(panelHc.getSoinApresSoleil());
        dto.setAutobronzant(panelHc.getAutobronzant());

        return dto;
    }

    /**
     * Convertit un PanelHcDTO en entité PanelHc
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public PanelHc toEntity(PanelHcDTO dto) {
        if (dto == null) {
            return null;
        }

        PanelHc panelHc = new PanelHc();

        // Ne pas définir l'ID si c'est une création (ID = 0)
        if (dto.getIdPanel() != 0) {
            panelHc.setIdPanel(dto.getIdPanel());
        }

        panelHc.setCire(dto.getCire());
        panelHc.setCremeDepilatoire(dto.getCremeDepilatoire());
        panelHc.setEpilateurElectrique(dto.getEpilateurElectrique());
        panelHc.setRasoir(dto.getRasoir());
        panelHc.setInstitut(dto.getInstitut());
        panelHc.setEpilationDefinitive(dto.getEpilationDefinitive());
        panelHc.setAchatInstitutParfumerie(dto.getAchatInstitutParfumerie());
        panelHc.setAchatGrandesSurfaces(dto.getAchatGrandesSurfaces());
        panelHc.setAchatPharmacieParapharmacie(dto.getAchatPharmacieParapharmacie());
        panelHc.setAchatInternet(dto.getAchatInternet());
        panelHc.setProduitsBio(dto.getProduitsBio());
        panelHc.setSavon(dto.getSavon());
        panelHc.setGelDouche(dto.getGelDouche());
        panelHc.setLaitDouche(dto.getLaitDouche());
        panelHc.setNettoyantIntime(dto.getNettoyantIntime());
        panelHc.setProduitsBain(dto.getProduitsBain());
        panelHc.setDeodorant(dto.getDeodorant());
        panelHc.setAntiTranspirant(dto.getAntiTranspirant());
        panelHc.setEauDeToilette(dto.getEauDeToilette());
        panelHc.setParfum(dto.getParfum());
        panelHc.setGelNettoyant(dto.getGelNettoyant());
        panelHc.setDemaquillantVisage(dto.getDemaquillantVisage());
        panelHc.setTonique(dto.getTonique());
        panelHc.setLotionMicellaire(dto.getLotionMicellaire());
        panelHc.setDemaquillantYeux(dto.getDemaquillantYeux());
        panelHc.setDemaquillantWaterproof(dto.getDemaquillantWaterproof());
        panelHc.setDissolvantOngles(dto.getDissolvantOngles());
        panelHc.setSoinHydratantVisage(dto.getSoinHydratantVisage());
        panelHc.setSoinNourissantVisage(dto.getSoinNourissantVisage());
        panelHc.setSoinAntiRidesVisage(dto.getSoinAntiRidesVisage());
        panelHc.setSoinAntiAgeVisage(dto.getSoinAntiAgeVisage());
        panelHc.setSoinRaffermissantVisage(dto.getSoinRaffermissantVisage());
        panelHc.setGommageVisage(dto.getGommageVisage());
        panelHc.setMasqueVisage(dto.getMasqueVisage());
        panelHc.setSoinAntiTachesVisage(dto.getSoinAntiTachesVisage());
        panelHc.setSoinAntiRougeursVisage(dto.getSoinAntiRougeursVisage());
        panelHc.setSoinMatifiantVisage(dto.getSoinMatifiantVisage());
        panelHc.setSoinEclatDuTeint(dto.getSoinEclatDuTeint());
        panelHc.setSoinContourDesYeux(dto.getSoinContourDesYeux());
        panelHc.setSoinContourDesLevres(dto.getSoinContourDesLevres());
        panelHc.setSoinHydratantCorps(dto.getSoinHydratantCorps());
        panelHc.setSoinNourrissantCorps(dto.getSoinNourrissantCorps());
        panelHc.setSoinAntiAgeCorps(dto.getSoinAntiAgeCorps());
        panelHc.setSoinRaffermissantCorps(dto.getSoinRaffermissantCorps());
        panelHc.setGommageCorps(dto.getGommageCorps());
        panelHc.setMasqueCorps(dto.getMasqueCorps());
        panelHc.setSoinAntiTachesDecollete(dto.getSoinAntiTachesDecollete());
        panelHc.setSoinAmincissant(dto.getSoinAmincissant());
        panelHc.setSoinAntiCellulite(dto.getSoinAntiCellulite());
        panelHc.setSoinAntiVergetures(dto.getSoinAntiVergetures());
        panelHc.setSoinHydratantMains(dto.getSoinHydratantMains());
        panelHc.setSoinNourrissantMains(dto.getSoinNourrissantMains());
        panelHc.setSoinAntiAgeMains(dto.getSoinAntiAgeMains());
        panelHc.setSoinAntiTachesMains(dto.getSoinAntiTachesMains());
        panelHc.setSoinPieds(dto.getSoinPieds());
        panelHc.setSoinOngles(dto.getSoinOngles());
        panelHc.setManucures(dto.getManucures());
        panelHc.setCremeTeintee(dto.getCremeTeintee());
        panelHc.setBaseMaquillage(dto.getBaseMaquillage());
        panelHc.setCorrecteurTeint(dto.getCorrecteurTeint());
        panelHc.setAnticerne(dto.getAnticerne());
        panelHc.setFondDeTeint(dto.getFondDeTeint());
        panelHc.setPoudreLibre(dto.getPoudreLibre());
        panelHc.setBlushFardAJoues(dto.getBlushFardAJoues());
        panelHc.setMascara(dto.getMascara());
        panelHc.setMascaraWaterproof(dto.getMascaraWaterproof());
        panelHc.setFardAPaupieres(dto.getFardAPaupieres());
        panelHc.setCrayonsYeux(dto.getCrayonsYeux());
        panelHc.setEyeliner(dto.getEyeliner());
        panelHc.setFauxCils(dto.getFauxCils());
        panelHc.setMaquillageDesSourcils(dto.getMaquillageDesSourcils());
        panelHc.setRougeALevres(dto.getRougeALevres());
        panelHc.setGloss(dto.getGloss());
        panelHc.setCrayonLevres(dto.getCrayonLevres());
        panelHc.setVernisAOngles(dto.getVernisAOngles());
        panelHc.setFauxOngles(dto.getFauxOngles());
        panelHc.setMaquillagePermanentYeux(dto.getMaquillagePermanentYeux());
        panelHc.setMaquillagePermanentSourcils(dto.getMaquillagePermanentSourcils());
        panelHc.setMaquillagePermanentLevres(dto.getMaquillagePermanentLevres());
        panelHc.setShampoing(dto.getShampoing());
        panelHc.setApresShampoing(dto.getApresShampoing());
        panelHc.setMasqueCapillaire(dto.getMasqueCapillaire());
        panelHc.setColorationMeches(dto.getColorationMeches());
        panelHc.setProduitCoiffantFixant(dto.getProduitCoiffantFixant());
        panelHc.setPermanente(dto.getPermanente());
        panelHc.setLissageDefrisage(dto.getLissageDefrisage());
        panelHc.setExtensionsCapillaires(dto.getExtensionsCapillaires());
        panelHc.setProtecteurSolaireVisage(dto.getProtecteurSolaireVisage());
        panelHc.setProtecteurSolaireCorps(dto.getProtecteurSolaireCorps());
        panelHc.setProtecteurSolaireLevres(dto.getProtecteurSolaireLevres());
        panelHc.setSoinApresSoleil(dto.getSoinApresSoleil());
        panelHc.setAutobronzant(dto.getAutobronzant());

        return panelHc;
    }

    /**
     * Convertit une liste d'entités PanelHc en liste de PanelHcDTO
     *
     * @param panelHcs la liste d'entités
     * @return la liste de DTOs
     */
    public List<PanelHcDTO> toDTOList(List<PanelHc> panelHcs) {
        if (panelHcs == null) {
            return null;
        }

        return panelHcs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de PanelHcDTO en liste d'entités PanelHc
     *
     * @param dtos la liste de DTOs
     * @return la liste d'entités
     */
    public List<PanelHc> toEntityList(List<PanelHcDTO> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité PanelHc existante avec les données d'un PanelHcDTO
     *
     * @param entity l'entité à mettre à jour
     * @param dto    les données à appliquer
     * @return l'entité mise à jour
     */
    public PanelHc updateEntityFromDTO(PanelHc entity, PanelHcDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        // Conserver l'ID existant

        entity.setCire(dto.getCire());
        entity.setCremeDepilatoire(dto.getCremeDepilatoire());
        entity.setEpilateurElectrique(dto.getEpilateurElectrique());
        entity.setRasoir(dto.getRasoir());
        entity.setInstitut(dto.getInstitut());
        entity.setEpilationDefinitive(dto.getEpilationDefinitive());
        entity.setAchatInstitutParfumerie(dto.getAchatInstitutParfumerie());
        entity.setAchatGrandesSurfaces(dto.getAchatGrandesSurfaces());
        entity.setAchatPharmacieParapharmacie(dto.getAchatPharmacieParapharmacie());
        entity.setAchatInternet(dto.getAchatInternet());
        entity.setProduitsBio(dto.getProduitsBio());
        entity.setSavon(dto.getSavon());
        entity.setGelDouche(dto.getGelDouche());
        entity.setLaitDouche(dto.getLaitDouche());
        entity.setNettoyantIntime(dto.getNettoyantIntime());
        entity.setProduitsBain(dto.getProduitsBain());
        entity.setDeodorant(dto.getDeodorant());
        entity.setAntiTranspirant(dto.getAntiTranspirant());
        entity.setEauDeToilette(dto.getEauDeToilette());
        entity.setParfum(dto.getParfum());
        entity.setGelNettoyant(dto.getGelNettoyant());
        entity.setDemaquillantVisage(dto.getDemaquillantVisage());
        entity.setTonique(dto.getTonique());
        entity.setLotionMicellaire(dto.getLotionMicellaire());
        entity.setDemaquillantYeux(dto.getDemaquillantYeux());
        entity.setDemaquillantWaterproof(dto.getDemaquillantWaterproof());
        entity.setDissolvantOngles(dto.getDissolvantOngles());
        entity.setSoinHydratantVisage(dto.getSoinHydratantVisage());
        entity.setSoinNourissantVisage(dto.getSoinNourissantVisage());
        entity.setSoinAntiRidesVisage(dto.getSoinAntiRidesVisage());
        entity.setSoinAntiAgeVisage(dto.getSoinAntiAgeVisage());
        entity.setSoinRaffermissantVisage(dto.getSoinRaffermissantVisage());
        entity.setGommageVisage(dto.getGommageVisage());
        entity.setMasqueVisage(dto.getMasqueVisage());
        entity.setSoinAntiTachesVisage(dto.getSoinAntiTachesVisage());
        entity.setSoinAntiRougeursVisage(dto.getSoinAntiRougeursVisage());
        entity.setSoinMatifiantVisage(dto.getSoinMatifiantVisage());
        entity.setSoinEclatDuTeint(dto.getSoinEclatDuTeint());
        entity.setSoinContourDesYeux(dto.getSoinContourDesYeux());
        entity.setSoinContourDesLevres(dto.getSoinContourDesLevres());
        entity.setSoinHydratantCorps(dto.getSoinHydratantCorps());
        entity.setSoinNourrissantCorps(dto.getSoinNourrissantCorps());
        entity.setSoinAntiAgeCorps(dto.getSoinAntiAgeCorps());
        entity.setSoinRaffermissantCorps(dto.getSoinRaffermissantCorps());
        entity.setGommageCorps(dto.getGommageCorps());
        entity.setMasqueCorps(dto.getMasqueCorps());
        entity.setSoinAntiTachesDecollete(dto.getSoinAntiTachesDecollete());
        entity.setSoinAmincissant(dto.getSoinAmincissant());
        entity.setSoinAntiCellulite(dto.getSoinAntiCellulite());
        entity.setSoinAntiVergetures(dto.getSoinAntiVergetures());
        entity.setSoinHydratantMains(dto.getSoinHydratantMains());
        entity.setSoinNourrissantMains(dto.getSoinNourrissantMains());
        entity.setSoinAntiAgeMains(dto.getSoinAntiAgeMains());
        entity.setSoinAntiTachesMains(dto.getSoinAntiTachesMains());
        entity.setSoinPieds(dto.getSoinPieds());
        entity.setSoinOngles(dto.getSoinOngles());
        entity.setManucures(dto.getManucures());
        entity.setCremeTeintee(dto.getCremeTeintee());
        entity.setBaseMaquillage(dto.getBaseMaquillage());
        entity.setCorrecteurTeint(dto.getCorrecteurTeint());
        entity.setAnticerne(dto.getAnticerne());
        entity.setFondDeTeint(dto.getFondDeTeint());
        entity.setPoudreLibre(dto.getPoudreLibre());
        entity.setBlushFardAJoues(dto.getBlushFardAJoues());
        entity.setMascara(dto.getMascara());
        entity.setMascaraWaterproof(dto.getMascaraWaterproof());
        entity.setFardAPaupieres(dto.getFardAPaupieres());
        entity.setCrayonsYeux(dto.getCrayonsYeux());
        entity.setEyeliner(dto.getEyeliner());
        entity.setFauxCils(dto.getFauxCils());
        entity.setMaquillageDesSourcils(dto.getMaquillageDesSourcils());
        entity.setRougeALevres(dto.getRougeALevres());
        entity.setGloss(dto.getGloss());
        entity.setCrayonLevres(dto.getCrayonLevres());
        entity.setVernisAOngles(dto.getVernisAOngles());
        entity.setFauxOngles(dto.getFauxOngles());
        entity.setMaquillagePermanentYeux(dto.getMaquillagePermanentYeux());
        entity.setMaquillagePermanentSourcils(dto.getMaquillagePermanentSourcils());
        entity.setMaquillagePermanentLevres(dto.getMaquillagePermanentLevres());
        entity.setShampoing(dto.getShampoing());
        entity.setApresShampoing(dto.getApresShampoing());
        entity.setMasqueCapillaire(dto.getMasqueCapillaire());
        entity.setColorationMeches(dto.getColorationMeches());
        entity.setProduitCoiffantFixant(dto.getProduitCoiffantFixant());
        entity.setPermanente(dto.getPermanente());
        entity.setLissageDefrisage(dto.getLissageDefrisage());
        entity.setExtensionsCapillaires(dto.getExtensionsCapillaires());
        entity.setProtecteurSolaireVisage(dto.getProtecteurSolaireVisage());
        entity.setProtecteurSolaireCorps(dto.getProtecteurSolaireCorps());
        entity.setProtecteurSolaireLevres(dto.getProtecteurSolaireLevres());
        entity.setSoinApresSoleil(dto.getSoinApresSoleil());
        entity.setAutobronzant(dto.getAutobronzant());

        return entity;
    }
}