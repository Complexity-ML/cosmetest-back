package com.example.cosmetest.business.mapper;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.domain.model.VolontaireHc;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir entre l'entité VolontaireHc et VolontaireHcDTO
 */
@Component
public class VolontaireHcMapper {

    /**
     * Convertit une entité VolontaireHc en VolontaireHcDTO
     *
     * @param volontaireHc l'entité à convertir
     * @return le DTO correspondant
     */
    public VolontaireHcDTO toDTO(VolontaireHc volontaireHc) {
        if (volontaireHc == null) {
            return null;
        }
        VolontaireHcDTO dto = new VolontaireHcDTO();

        // ID du volontaire
        dto.setIdVol(volontaireHc.getIdVol());

        // Lieux d'achat
        dto.setAchatGrandesSurfaces(volontaireHc.getAchatGrandesSurfaces());
        dto.setAchatInstitutParfumerie(volontaireHc.getAchatInstitutParfumerie());
        dto.setAchatInternet(volontaireHc.getAchatInternet());
        dto.setAchatPharmacieParapharmacie(volontaireHc.getAchatPharmacieParapharmacie());

        // Produits d'hygiène et soins
        dto.setAntiTranspirant(volontaireHc.getAntiTranspirant());
        dto.setApresRasage(volontaireHc.getApresRasage());
        dto.setApresShampoing(volontaireHc.getApresShampoing());
        dto.setAutobronzant(volontaireHc.getAutobronzant());
        dto.setCire(volontaireHc.getCire());
        dto.setCremeDepilatoire(volontaireHc.getCremeDepilatoire());
        dto.setDeodorant(volontaireHc.getDeodorant());
        dto.setDissolvantOngles(volontaireHc.getDissolvantOngles());
        dto.setEauDeToilette(volontaireHc.getEauDeToilette());
        dto.setEpilateurElectrique(volontaireHc.getEpilateurElectrique());
        dto.setEpilationDefinitive(volontaireHc.getEpilationDefinitive());
        dto.setExtensionsCapillaires(volontaireHc.getExtensionsCapillaires());
        dto.setGelARaser(volontaireHc.getGelARaser());
        dto.setGelDouche(volontaireHc.getGelDouche());
        dto.setGelNettoyant(volontaireHc.getGelNettoyant());
        dto.setGommageCorps(volontaireHc.getGommageCorps());
        dto.setGommageVisage(volontaireHc.getGommageVisage());
        dto.setInstitut(volontaireHc.getInstitut());
        dto.setLaitDouche(volontaireHc.getLaitDouche());
        dto.setLissageDefrisage(volontaireHc.getLissageDefrisage());
        dto.setLotionMicellaire(volontaireHc.getLotionMicellaire());
        dto.setManucures(volontaireHc.getManucures());
        dto.setMasqueCapillaire(volontaireHc.getMasqueCapillaire());
        dto.setMasqueCorps(volontaireHc.getMasqueCorps());
        dto.setMasqueVisage(volontaireHc.getMasqueVisage());
        dto.setMousseARaser(volontaireHc.getMousseARaser());
        dto.setNettoyantIntime(volontaireHc.getNettoyantIntime());
        dto.setOmbreBarbe(volontaireHc.getOmbreBarbe());
        dto.setParfum(volontaireHc.getParfum());
        dto.setPermanente(volontaireHc.getPermanente());
        dto.setProduitCoiffantFixant(volontaireHc.getProduitCoiffantFixant());
        dto.setProduitsBain(volontaireHc.getProduitsBain());
        dto.setProduitsBio(volontaireHc.getProduitsBio());
        dto.setProtecteurSolaireCorps(volontaireHc.getProtecteurSolaireCorps());
        dto.setProtecteurSolaireLevres(volontaireHc.getProtecteurSolaireLevres());
        dto.setProtecteurSolaireVisage(volontaireHc.getProtecteurSolaireVisage());
        dto.setRasoir(volontaireHc.getRasoir());
        dto.setRasoirElectrique(volontaireHc.getRasoirElectrique());
        dto.setRasoirMecanique(volontaireHc.getRasoirMecanique());
        dto.setSavon(volontaireHc.getSavon());
        dto.setShampoing(volontaireHc.getShampoing());
        dto.setTondeuseBarbe(volontaireHc.getTondeuseBarbe());
        dto.setTonique(volontaireHc.getTonique());

        // Produits de maquillage
        dto.setAnticerne(volontaireHc.getAnticerne());
        dto.setBaseMaquillage(volontaireHc.getBaseMaquillage());
        dto.setBlushFardAJoues(volontaireHc.getBlushFardAJoues());
        dto.setCorrecteurTeint(volontaireHc.getCorrecteurTeint());
        dto.setCrayonLevres(volontaireHc.getCrayonLevres());
        dto.setCrayonsYeux(volontaireHc.getCrayonsYeux());
        dto.setCremeTeintee(volontaireHc.getCremeTeintee());
        dto.setDemaquillantVisage(volontaireHc.getDemaquillantVisage());
        dto.setDemaquillantWaterproof(volontaireHc.getDemaquillantWaterproof());
        dto.setDemaquillantYeux(volontaireHc.getDemaquillantYeux());
        dto.setEyeliner(volontaireHc.getEyeliner());
        dto.setFardAPaupieres(volontaireHc.getFardAPaupieres());
        dto.setFauxCils(volontaireHc.getFauxCils());
        dto.setFauxOngles(volontaireHc.getFauxOngles());
        dto.setFondDeTeint(volontaireHc.getFondDeTeint());
        dto.setGloss(volontaireHc.getGloss());
        dto.setMaquillageDesSourcils(volontaireHc.getMaquillageDesSourcils());
        dto.setMaquillagePermanentLevres(volontaireHc.getMaquillagePermanentLevres());
        dto.setMaquillagePermanentSourcils(volontaireHc.getMaquillagePermanentSourcils());
        dto.setMaquillagePermanentYeux(volontaireHc.getMaquillagePermanentYeux());
        dto.setMascara(volontaireHc.getMascara());
        dto.setMascaraWaterproof(volontaireHc.getMascaraWaterproof());
        dto.setPoudreLibre(volontaireHc.getPoudreLibre());
        dto.setRougeALevres(volontaireHc.getRougeALevres());
        dto.setVernisAOngles(volontaireHc.getVernisAOngles());

        // Produits de soin
        dto.setColorationMeches(volontaireHc.getColorationMeches());
        dto.setSoinAmincissant(volontaireHc.getSoinAmincissant());
        dto.setSoinAntiAgeCorps(volontaireHc.getSoinAntiAgeCorps());
        dto.setSoinAntiAgeMains(volontaireHc.getSoinAntiAgeMains());
        dto.setSoinAntiAgeVisage(volontaireHc.getSoinAntiAgeVisage());
        dto.setSoinAntiCellulite(volontaireHc.getSoinAntiCellulite());
        dto.setSoinAntiRidesVisage(volontaireHc.getSoinAntiRidesVisage());
        dto.setSoinAntiRougeursVisage(volontaireHc.getSoinAntiRougeursVisage());
        dto.setSoinAntiTachesDecollete(volontaireHc.getSoinAntiTachesDecollete());
        dto.setSoinAntiTachesMains(volontaireHc.getSoinAntiTachesMains());
        dto.setSoinAntiTachesVisage(volontaireHc.getSoinAntiTachesVisage());
        dto.setSoinAntiVergetures(volontaireHc.getSoinAntiVergetures());
        dto.setSoinApresSoleil(volontaireHc.getSoinApresSoleil());
        dto.setSoinContourDesLevres(volontaireHc.getSoinContourDesLevres());
        dto.setSoinContourDesYeux(volontaireHc.getSoinContourDesYeux());
        dto.setSoinEclatDuTeint(volontaireHc.getSoinEclatDuTeint());
        dto.setSoinHydratantCorps(volontaireHc.getSoinHydratantCorps());
        dto.setSoinHydratantMains(volontaireHc.getSoinHydratantMains());
        dto.setSoinHydratantVisage(volontaireHc.getSoinHydratantVisage());
        dto.setSoinMatifiantVisage(volontaireHc.getSoinMatifiantVisage());
        dto.setSoinNourissantVisage(volontaireHc.getSoinNourissantVisage());
        dto.setSoinNourrissantCorps(volontaireHc.getSoinNourrissantCorps());
        dto.setSoinNourrissantMains(volontaireHc.getSoinNourrissantMains());
        dto.setSoinOngles(volontaireHc.getSoinOngles());
        dto.setSoinPieds(volontaireHc.getSoinPieds());
        dto.setSoinRaffermissantCorps(volontaireHc.getSoinRaffermissantCorps());
        dto.setSoinRaffermissantVisage(volontaireHc.getSoinRaffermissantVisage());

        return dto;
    }

    /**
     * Convertit un VolontaireHcDTO en entité VolontaireHc
     *
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public VolontaireHc toEntity(VolontaireHcDTO dto) {
        if (dto == null) {
            return null;
        }

        VolontaireHc entity = new VolontaireHc();
        entity.setIdVol(dto.getIdVol());

        // Lieux d'achat
        entity.setAchatGrandesSurfaces(dto.getAchatGrandesSurfaces());
        entity.setAchatInstitutParfumerie(dto.getAchatInstitutParfumerie());
        entity.setAchatInternet(dto.getAchatInternet());
        entity.setAchatPharmacieParapharmacie(dto.getAchatPharmacieParapharmacie());

        // Produits d'hygiène et soins
        entity.setAntiTranspirant(dto.getAntiTranspirant());
        entity.setApresRasage(dto.getApresRasage());
        entity.setApresShampoing(dto.getApresShampoing());
        entity.setAutobronzant(dto.getAutobronzant());
        entity.setCire(dto.getCire());
        entity.setCremeDepilatoire(dto.getCremeDepilatoire());
        entity.setDeodorant(dto.getDeodorant());
        entity.setDissolvantOngles(dto.getDissolvantOngles());
        entity.setEauDeToilette(dto.getEauDeToilette());
        entity.setEpilateurElectrique(dto.getEpilateurElectrique());
        entity.setEpilationDefinitive(dto.getEpilationDefinitive());
        entity.setExtensionsCapillaires(dto.getExtensionsCapillaires());
        entity.setGelARaser(dto.getGelARaser());
        entity.setGelDouche(dto.getGelDouche());
        entity.setGelNettoyant(dto.getGelNettoyant());
        entity.setGommageCorps(dto.getGommageCorps());
        entity.setGommageVisage(dto.getGommageVisage());
        entity.setInstitut(dto.getInstitut());
        entity.setLaitDouche(dto.getLaitDouche());
        entity.setLissageDefrisage(dto.getLissageDefrisage());
        entity.setLotionMicellaire(dto.getLotionMicellaire());
        entity.setManucures(dto.getManucures());
        entity.setMasqueCapillaire(dto.getMasqueCapillaire());
        entity.setMasqueCorps(dto.getMasqueCorps());
        entity.setMasqueVisage(dto.getMasqueVisage());
        entity.setMousseARaser(dto.getMousseARaser());
        entity.setNettoyantIntime(dto.getNettoyantIntime());
        entity.setOmbreBarbe(dto.getOmbreBarbe());
        entity.setParfum(dto.getParfum());
        entity.setPermanente(dto.getPermanente());
        entity.setProduitCoiffantFixant(dto.getProduitCoiffantFixant());
        entity.setProduitsBain(dto.getProduitsBain());
        entity.setProduitsBio(dto.getProduitsBio());
        entity.setProtecteurSolaireCorps(dto.getProtecteurSolaireCorps());
        entity.setProtecteurSolaireLevres(dto.getProtecteurSolaireLevres());
        entity.setProtecteurSolaireVisage(dto.getProtecteurSolaireVisage());
        entity.setRasoir(dto.getRasoir());
        entity.setRasoirElectrique(dto.getRasoirElectrique());
        entity.setRasoirMecanique(dto.getRasoirMecanique());
        entity.setSavon(dto.getSavon());
        entity.setShampoing(dto.getShampoing());
        entity.setTondeuseBarbe(dto.getTondeuseBarbe());
        entity.setTonique(dto.getTonique());

        // Produits de maquillage
        entity.setAnticerne(dto.getAnticerne());
        entity.setBaseMaquillage(dto.getBaseMaquillage());
        entity.setBlushFardAJoues(dto.getBlushFardAJoues());
        entity.setCorrecteurTeint(dto.getCorrecteurTeint());
        entity.setCrayonLevres(dto.getCrayonLevres());
        entity.setCrayonsYeux(dto.getCrayonsYeux());
        entity.setCremeTeintee(dto.getCremeTeintee());
        entity.setDemaquillantVisage(dto.getDemaquillantVisage());
        entity.setDemaquillantWaterproof(dto.getDemaquillantWaterproof());
        entity.setDemaquillantYeux(dto.getDemaquillantYeux());
        entity.setEyeliner(dto.getEyeliner());
        entity.setFardAPaupieres(dto.getFardAPaupieres());
        entity.setFauxCils(dto.getFauxCils());
        entity.setFauxOngles(dto.getFauxOngles());
        entity.setFondDeTeint(dto.getFondDeTeint());
        entity.setGloss(dto.getGloss());
        entity.setMaquillageDesSourcils(dto.getMaquillageDesSourcils());
        entity.setMaquillagePermanentLevres(dto.getMaquillagePermanentLevres());
        entity.setMaquillagePermanentSourcils(dto.getMaquillagePermanentSourcils());
        entity.setMaquillagePermanentYeux(dto.getMaquillagePermanentYeux());
        entity.setMascara(dto.getMascara());
        entity.setMascaraWaterproof(dto.getMascaraWaterproof());
        entity.setPoudreLibre(dto.getPoudreLibre());
        entity.setRougeALevres(dto.getRougeALevres());
        entity.setVernisAOngles(dto.getVernisAOngles());

        // Produits de soin
        entity.setColorationMeches(dto.getColorationMeches());
        entity.setSoinAmincissant(dto.getSoinAmincissant());
        entity.setSoinAntiAgeCorps(dto.getSoinAntiAgeCorps());
        entity.setSoinAntiAgeMains(dto.getSoinAntiAgeMains());
        entity.setSoinAntiAgeVisage(dto.getSoinAntiAgeVisage());
        entity.setSoinAntiCellulite(dto.getSoinAntiCellulite());
        entity.setSoinAntiRidesVisage(dto.getSoinAntiRidesVisage());
        entity.setSoinAntiRougeursVisage(dto.getSoinAntiRougeursVisage());
        entity.setSoinAntiTachesDecollete(dto.getSoinAntiTachesDecollete());
        entity.setSoinAntiTachesMains(dto.getSoinAntiTachesMains());
        entity.setSoinAntiTachesVisage(dto.getSoinAntiTachesVisage());
        entity.setSoinAntiVergetures(dto.getSoinAntiVergetures());
        entity.setSoinApresSoleil(dto.getSoinApresSoleil());
        entity.setSoinContourDesLevres(dto.getSoinContourDesLevres());
        entity.setSoinContourDesYeux(dto.getSoinContourDesYeux());
        entity.setSoinEclatDuTeint(dto.getSoinEclatDuTeint());
        entity.setSoinHydratantCorps(dto.getSoinHydratantCorps());
        entity.setSoinHydratantMains(dto.getSoinHydratantMains());
        entity.setSoinHydratantVisage(dto.getSoinHydratantVisage());
        entity.setSoinMatifiantVisage(dto.getSoinMatifiantVisage());
        entity.setSoinNourissantVisage(dto.getSoinNourissantVisage());
        entity.setSoinNourrissantCorps(dto.getSoinNourrissantCorps());
        entity.setSoinNourrissantMains(dto.getSoinNourrissantMains());
        entity.setSoinOngles(dto.getSoinOngles());
        entity.setSoinPieds(dto.getSoinPieds());
        entity.setSoinRaffermissantCorps(dto.getSoinRaffermissantCorps());
        entity.setSoinRaffermissantVisage(dto.getSoinRaffermissantVisage());

        return entity;
    }

    /**
     * Convertit une liste d'entités VolontaireHc en liste de VolontaireHcDTO
     *
     * @param volontaireHcs la liste d'entités
     * @return la liste de DTOs
     */
    public List<VolontaireHcDTO> toDTOList(List<VolontaireHc> volontaireHcs) {
        return volontaireHcs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité VolontaireHc existante avec les données d'un
     * VolontaireHcDTO
     * Pour VolontaireHc, comme l'identifiant contient toutes les données, c'est une
     * opération de remplacement complet
     *
     * @param entity l'entité à mettre à jour
     * @param dto    les données à appliquer
     * @return l'entité mise à jour
     */
    public VolontaireHc updateEntityFromDTO(VolontaireHc entity, VolontaireHcDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }

        // Mettre à jour toutes les valeurs directement sur l'entité
        entity.setIdVol(dto.getIdVol());
        entity.setAchatGrandesSurfaces(dto.getAchatGrandesSurfaces());
        entity.setAchatInstitutParfumerie(dto.getAchatInstitutParfumerie());
        entity.setAchatInternet(dto.getAchatInternet());
        entity.setAchatPharmacieParapharmacie(dto.getAchatPharmacieParapharmacie());
        entity.setAntiTranspirant(dto.getAntiTranspirant());
        entity.setApresRasage(dto.getApresRasage());
        entity.setApresShampoing(dto.getApresShampoing());
        entity.setAutobronzant(dto.getAutobronzant());
        entity.setCire(dto.getCire());
        entity.setCremeDepilatoire(dto.getCremeDepilatoire());
        entity.setDeodorant(dto.getDeodorant());
        entity.setDissolvantOngles(dto.getDissolvantOngles());
        entity.setEauDeToilette(dto.getEauDeToilette());
        entity.setEpilateurElectrique(dto.getEpilateurElectrique());
        entity.setEpilationDefinitive(dto.getEpilationDefinitive());
        entity.setExtensionsCapillaires(dto.getExtensionsCapillaires());
        entity.setGelARaser(dto.getGelARaser());
        entity.setGelDouche(dto.getGelDouche());
        entity.setGelNettoyant(dto.getGelNettoyant());
        entity.setGommageCorps(dto.getGommageCorps());
        entity.setGommageVisage(dto.getGommageVisage());
        entity.setInstitut(dto.getInstitut());
        entity.setLaitDouche(dto.getLaitDouche());
        entity.setLissageDefrisage(dto.getLissageDefrisage());
        entity.setLotionMicellaire(dto.getLotionMicellaire());
        entity.setManucures(dto.getManucures());
        entity.setMasqueCapillaire(dto.getMasqueCapillaire());
        entity.setMasqueCorps(dto.getMasqueCorps());
        entity.setMasqueVisage(dto.getMasqueVisage());
        entity.setMousseARaser(dto.getMousseARaser());
        entity.setNettoyantIntime(dto.getNettoyantIntime());
        entity.setOmbreBarbe(dto.getOmbreBarbe());
        entity.setParfum(dto.getParfum());
        entity.setPermanente(dto.getPermanente());
        entity.setProduitCoiffantFixant(dto.getProduitCoiffantFixant());
        entity.setProduitsBain(dto.getProduitsBain());
        entity.setProduitsBio(dto.getProduitsBio());
        entity.setProtecteurSolaireCorps(dto.getProtecteurSolaireCorps());
        entity.setProtecteurSolaireLevres(dto.getProtecteurSolaireLevres());
        entity.setProtecteurSolaireVisage(dto.getProtecteurSolaireVisage());
        entity.setRasoir(dto.getRasoir());
        entity.setRasoirElectrique(dto.getRasoirElectrique());
        entity.setRasoirMecanique(dto.getRasoirMecanique());
        entity.setSavon(dto.getSavon());
        entity.setShampoing(dto.getShampoing());
        entity.setTondeuseBarbe(dto.getTondeuseBarbe());
        entity.setTonique(dto.getTonique());
        entity.setAnticerne(dto.getAnticerne());
        entity.setBaseMaquillage(dto.getBaseMaquillage());
        entity.setBlushFardAJoues(dto.getBlushFardAJoues());
        entity.setCorrecteurTeint(dto.getCorrecteurTeint());
        entity.setCrayonLevres(dto.getCrayonLevres());
        entity.setCrayonsYeux(dto.getCrayonsYeux());
        entity.setCremeTeintee(dto.getCremeTeintee());
        entity.setDemaquillantVisage(dto.getDemaquillantVisage());
        entity.setDemaquillantWaterproof(dto.getDemaquillantWaterproof());
        entity.setDemaquillantYeux(dto.getDemaquillantYeux());
        entity.setEyeliner(dto.getEyeliner());
        entity.setFardAPaupieres(dto.getFardAPaupieres());
        entity.setFauxCils(dto.getFauxCils());
        entity.setFauxOngles(dto.getFauxOngles());
        entity.setFondDeTeint(dto.getFondDeTeint());
        entity.setGloss(dto.getGloss());
        entity.setMaquillageDesSourcils(dto.getMaquillageDesSourcils());
        entity.setMaquillagePermanentLevres(dto.getMaquillagePermanentLevres());
        entity.setMaquillagePermanentSourcils(dto.getMaquillagePermanentSourcils());
        entity.setMaquillagePermanentYeux(dto.getMaquillagePermanentYeux());
        entity.setMascara(dto.getMascara());
        entity.setMascaraWaterproof(dto.getMascaraWaterproof());
        entity.setPoudreLibre(dto.getPoudreLibre());
        entity.setRougeALevres(dto.getRougeALevres());
        entity.setVernisAOngles(dto.getVernisAOngles());
        entity.setColorationMeches(dto.getColorationMeches());
        entity.setSoinAmincissant(dto.getSoinAmincissant());
        entity.setSoinAntiAgeCorps(dto.getSoinAntiAgeCorps());
        entity.setSoinAntiAgeMains(dto.getSoinAntiAgeMains());
        entity.setSoinAntiAgeVisage(dto.getSoinAntiAgeVisage());
        entity.setSoinAntiCellulite(dto.getSoinAntiCellulite());
        entity.setSoinAntiRidesVisage(dto.getSoinAntiRidesVisage());
        entity.setSoinAntiRougeursVisage(dto.getSoinAntiRougeursVisage());
        entity.setSoinAntiTachesDecollete(dto.getSoinAntiTachesDecollete());
        entity.setSoinAntiTachesMains(dto.getSoinAntiTachesMains());
        entity.setSoinAntiTachesVisage(dto.getSoinAntiTachesVisage());
        entity.setSoinAntiVergetures(dto.getSoinAntiVergetures());
        entity.setSoinApresSoleil(dto.getSoinApresSoleil());
        entity.setSoinContourDesLevres(dto.getSoinContourDesLevres());
        entity.setSoinContourDesYeux(dto.getSoinContourDesYeux());
        entity.setSoinEclatDuTeint(dto.getSoinEclatDuTeint());
        entity.setSoinHydratantCorps(dto.getSoinHydratantCorps());
        entity.setSoinHydratantMains(dto.getSoinHydratantMains());
        entity.setSoinHydratantVisage(dto.getSoinHydratantVisage());
        entity.setSoinMatifiantVisage(dto.getSoinMatifiantVisage());
        entity.setSoinNourissantVisage(dto.getSoinNourissantVisage());
        entity.setSoinNourrissantCorps(dto.getSoinNourrissantCorps());
        entity.setSoinNourrissantMains(dto.getSoinNourrissantMains());
        entity.setSoinOngles(dto.getSoinOngles());
        entity.setSoinPieds(dto.getSoinPieds());
        entity.setSoinRaffermissantCorps(dto.getSoinRaffermissantCorps());
        entity.setSoinRaffermissantVisage(dto.getSoinRaffermissantVisage());

        return entity;
    }
}
