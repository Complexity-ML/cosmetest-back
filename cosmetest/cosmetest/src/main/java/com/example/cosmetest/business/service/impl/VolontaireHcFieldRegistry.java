package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.VolontaireHcDTO;
import com.example.cosmetest.domain.model.VolontaireHc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/** Explicit allowlist for dynamic cosmetic-habit field operations. */
final class VolontaireHcFieldRegistry {
    private static final Map<String, FieldAccess> FIELDS;
    static {
        Map<String, FieldAccess> fields = new LinkedHashMap<>();
        fields.put("achatGrandesSurfaces", new FieldAccess("achat_grandes_surfaces", VolontaireHc::getAchatGrandesSurfaces, VolontaireHc::setAchatGrandesSurfaces, VolontaireHcDTO::getAchatGrandesSurfaces));
        fields.put("achatInstitutParfumerie", new FieldAccess("achat_institut_parfumerie", VolontaireHc::getAchatInstitutParfumerie, VolontaireHc::setAchatInstitutParfumerie, VolontaireHcDTO::getAchatInstitutParfumerie));
        fields.put("achatInternet", new FieldAccess("achat_internet", VolontaireHc::getAchatInternet, VolontaireHc::setAchatInternet, VolontaireHcDTO::getAchatInternet));
        fields.put("achatPharmacieParapharmacie", new FieldAccess("achat_pharmacie_parapharmacie", VolontaireHc::getAchatPharmacieParapharmacie, VolontaireHc::setAchatPharmacieParapharmacie, VolontaireHcDTO::getAchatPharmacieParapharmacie));
        fields.put("antiTranspirant", new FieldAccess("anti_transpirant", VolontaireHc::getAntiTranspirant, VolontaireHc::setAntiTranspirant, VolontaireHcDTO::getAntiTranspirant));
        fields.put("anticerne", new FieldAccess("anti_cerne", VolontaireHc::getAnticerne, VolontaireHc::setAnticerne, VolontaireHcDTO::getAnticerne));
        fields.put("apresRasage", new FieldAccess("apres_rasage", VolontaireHc::getApresRasage, VolontaireHc::setApresRasage, VolontaireHcDTO::getApresRasage));
        fields.put("apresShampoing", new FieldAccess("apres_shampoing", VolontaireHc::getApresShampoing, VolontaireHc::setApresShampoing, VolontaireHcDTO::getApresShampoing));
        fields.put("autobronzant", new FieldAccess("autobronzant", VolontaireHc::getAutobronzant, VolontaireHc::setAutobronzant, VolontaireHcDTO::getAutobronzant));
        fields.put("baseMaquillage", new FieldAccess("base_maquillage", VolontaireHc::getBaseMaquillage, VolontaireHc::setBaseMaquillage, VolontaireHcDTO::getBaseMaquillage));
        fields.put("blushFardAJoues", new FieldAccess("blush_fard_a_joues", VolontaireHc::getBlushFardAJoues, VolontaireHc::setBlushFardAJoues, VolontaireHcDTO::getBlushFardAJoues));
        fields.put("cire", new FieldAccess("cire", VolontaireHc::getCire, VolontaireHc::setCire, VolontaireHcDTO::getCire));
        fields.put("colorationMeches", new FieldAccess("coloration_meches", VolontaireHc::getColorationMeches, VolontaireHc::setColorationMeches, VolontaireHcDTO::getColorationMeches));
        fields.put("correcteurTeint", new FieldAccess("correcteur_teint", VolontaireHc::getCorrecteurTeint, VolontaireHc::setCorrecteurTeint, VolontaireHcDTO::getCorrecteurTeint));
        fields.put("crayonLevres", new FieldAccess("crayon_levres", VolontaireHc::getCrayonLevres, VolontaireHc::setCrayonLevres, VolontaireHcDTO::getCrayonLevres));
        fields.put("crayonsYeux", new FieldAccess("crayons_yeux", VolontaireHc::getCrayonsYeux, VolontaireHc::setCrayonsYeux, VolontaireHcDTO::getCrayonsYeux));
        fields.put("cremeDepilatoire", new FieldAccess("creme_depilatoire", VolontaireHc::getCremeDepilatoire, VolontaireHc::setCremeDepilatoire, VolontaireHcDTO::getCremeDepilatoire));
        fields.put("cremeTeintee", new FieldAccess("creme_teintee", VolontaireHc::getCremeTeintee, VolontaireHc::setCremeTeintee, VolontaireHcDTO::getCremeTeintee));
        fields.put("demaquillantVisage", new FieldAccess("demaquillant_visage", VolontaireHc::getDemaquillantVisage, VolontaireHc::setDemaquillantVisage, VolontaireHcDTO::getDemaquillantVisage));
        fields.put("demaquillantWaterproof", new FieldAccess("demaquillant_waterproof", VolontaireHc::getDemaquillantWaterproof, VolontaireHc::setDemaquillantWaterproof, VolontaireHcDTO::getDemaquillantWaterproof));
        fields.put("demaquillantYeux", new FieldAccess("demaquillant_yeux", VolontaireHc::getDemaquillantYeux, VolontaireHc::setDemaquillantYeux, VolontaireHcDTO::getDemaquillantYeux));
        fields.put("deodorant", new FieldAccess("deodorant", VolontaireHc::getDeodorant, VolontaireHc::setDeodorant, VolontaireHcDTO::getDeodorant));
        fields.put("dissolvantOngles", new FieldAccess("dissolvant_ongles", VolontaireHc::getDissolvantOngles, VolontaireHc::setDissolvantOngles, VolontaireHcDTO::getDissolvantOngles));
        fields.put("eauDeToilette", new FieldAccess("eau_de_toilette", VolontaireHc::getEauDeToilette, VolontaireHc::setEauDeToilette, VolontaireHcDTO::getEauDeToilette));
        fields.put("epilateurElectrique", new FieldAccess("epilateur_electrique", VolontaireHc::getEpilateurElectrique, VolontaireHc::setEpilateurElectrique, VolontaireHcDTO::getEpilateurElectrique));
        fields.put("epilationDefinitive", new FieldAccess("epilation_definitive", VolontaireHc::getEpilationDefinitive, VolontaireHc::setEpilationDefinitive, VolontaireHcDTO::getEpilationDefinitive));
        fields.put("extensionsCapillaires", new FieldAccess("extensions_capillaires", VolontaireHc::getExtensionsCapillaires, VolontaireHc::setExtensionsCapillaires, VolontaireHcDTO::getExtensionsCapillaires));
        fields.put("eyeliner", new FieldAccess("eyeliner", VolontaireHc::getEyeliner, VolontaireHc::setEyeliner, VolontaireHcDTO::getEyeliner));
        fields.put("fardAPaupieres", new FieldAccess("fard_a_paupieres", VolontaireHc::getFardAPaupieres, VolontaireHc::setFardAPaupieres, VolontaireHcDTO::getFardAPaupieres));
        fields.put("fauxCils", new FieldAccess("faux_cils", VolontaireHc::getFauxCils, VolontaireHc::setFauxCils, VolontaireHcDTO::getFauxCils));
        fields.put("fauxOngles", new FieldAccess("faux_ongles", VolontaireHc::getFauxOngles, VolontaireHc::setFauxOngles, VolontaireHcDTO::getFauxOngles));
        fields.put("fondDeTeint", new FieldAccess("fond_de_teint", VolontaireHc::getFondDeTeint, VolontaireHc::setFondDeTeint, VolontaireHcDTO::getFondDeTeint));
        fields.put("gelARaser", new FieldAccess("gel_a_raser", VolontaireHc::getGelARaser, VolontaireHc::setGelARaser, VolontaireHcDTO::getGelARaser));
        fields.put("gelDouche", new FieldAccess("gel_douche", VolontaireHc::getGelDouche, VolontaireHc::setGelDouche, VolontaireHcDTO::getGelDouche));
        fields.put("gelNettoyant", new FieldAccess("gel_nettoyant", VolontaireHc::getGelNettoyant, VolontaireHc::setGelNettoyant, VolontaireHcDTO::getGelNettoyant));
        fields.put("gloss", new FieldAccess("gloss", VolontaireHc::getGloss, VolontaireHc::setGloss, VolontaireHcDTO::getGloss));
        fields.put("gommageCorps", new FieldAccess("gommage_corps", VolontaireHc::getGommageCorps, VolontaireHc::setGommageCorps, VolontaireHcDTO::getGommageCorps));
        fields.put("gommageVisage", new FieldAccess("gommage_visage", VolontaireHc::getGommageVisage, VolontaireHc::setGommageVisage, VolontaireHcDTO::getGommageVisage));
        fields.put("institut", new FieldAccess("id_vol", VolontaireHc::getInstitut, VolontaireHc::setInstitut, VolontaireHcDTO::getInstitut));
        fields.put("laitDouche", new FieldAccess("lait_douche", VolontaireHc::getLaitDouche, VolontaireHc::setLaitDouche, VolontaireHcDTO::getLaitDouche));
        fields.put("lissageDefrisage", new FieldAccess("lissage_defrisage", VolontaireHc::getLissageDefrisage, VolontaireHc::setLissageDefrisage, VolontaireHcDTO::getLissageDefrisage));
        fields.put("lotionMicellaire", new FieldAccess("lotion_micellaire", VolontaireHc::getLotionMicellaire, VolontaireHc::setLotionMicellaire, VolontaireHcDTO::getLotionMicellaire));
        fields.put("manucures", new FieldAccess("manucures", VolontaireHc::getManucures, VolontaireHc::setManucures, VolontaireHcDTO::getManucures));
        fields.put("maquillageDesSourcils", new FieldAccess("maquillage_des_sourcils", VolontaireHc::getMaquillageDesSourcils, VolontaireHc::setMaquillageDesSourcils, VolontaireHcDTO::getMaquillageDesSourcils));
        fields.put("maquillagePermanentLevres", new FieldAccess("maquillage_permanent_levres", VolontaireHc::getMaquillagePermanentLevres, VolontaireHc::setMaquillagePermanentLevres, VolontaireHcDTO::getMaquillagePermanentLevres));
        fields.put("maquillagePermanentSourcils", new FieldAccess("maquillage_permanent_sourcils", VolontaireHc::getMaquillagePermanentSourcils, VolontaireHc::setMaquillagePermanentSourcils, VolontaireHcDTO::getMaquillagePermanentSourcils));
        fields.put("maquillagePermanentYeux", new FieldAccess("maquillage_permanent_yeux", VolontaireHc::getMaquillagePermanentYeux, VolontaireHc::setMaquillagePermanentYeux, VolontaireHcDTO::getMaquillagePermanentYeux));
        fields.put("mascara", new FieldAccess("mascara", VolontaireHc::getMascara, VolontaireHc::setMascara, VolontaireHcDTO::getMascara));
        fields.put("mascaraWaterproof", new FieldAccess("mascara_waterproof", VolontaireHc::getMascaraWaterproof, VolontaireHc::setMascaraWaterproof, VolontaireHcDTO::getMascaraWaterproof));
        fields.put("masqueCapillaire", new FieldAccess("masque_capillaire", VolontaireHc::getMasqueCapillaire, VolontaireHc::setMasqueCapillaire, VolontaireHcDTO::getMasqueCapillaire));
        fields.put("masqueCorps", new FieldAccess("masque_corps", VolontaireHc::getMasqueCorps, VolontaireHc::setMasqueCorps, VolontaireHcDTO::getMasqueCorps));
        fields.put("masqueVisage", new FieldAccess("masque_visage", VolontaireHc::getMasqueVisage, VolontaireHc::setMasqueVisage, VolontaireHcDTO::getMasqueVisage));
        fields.put("mousseARaser", new FieldAccess("mousse_a_raser", VolontaireHc::getMousseARaser, VolontaireHc::setMousseARaser, VolontaireHcDTO::getMousseARaser));
        fields.put("nettoyantIntime", new FieldAccess("nettoyant_intime", VolontaireHc::getNettoyantIntime, VolontaireHc::setNettoyantIntime, VolontaireHcDTO::getNettoyantIntime));
        fields.put("ombreBarbe", new FieldAccess("ombre_barbe", VolontaireHc::getOmbreBarbe, VolontaireHc::setOmbreBarbe, VolontaireHcDTO::getOmbreBarbe));
        fields.put("parfum", new FieldAccess("parfum", VolontaireHc::getParfum, VolontaireHc::setParfum, VolontaireHcDTO::getParfum));
        fields.put("permanente", new FieldAccess("permanente", VolontaireHc::getPermanente, VolontaireHc::setPermanente, VolontaireHcDTO::getPermanente));
        fields.put("poudreLibre", new FieldAccess("poudre_libre", VolontaireHc::getPoudreLibre, VolontaireHc::setPoudreLibre, VolontaireHcDTO::getPoudreLibre));
        fields.put("produitCoiffantFixant", new FieldAccess("produit_coiffant_fixant", VolontaireHc::getProduitCoiffantFixant, VolontaireHc::setProduitCoiffantFixant, VolontaireHcDTO::getProduitCoiffantFixant));
        fields.put("produitsBain", new FieldAccess("produits_bain", VolontaireHc::getProduitsBain, VolontaireHc::setProduitsBain, VolontaireHcDTO::getProduitsBain));
        fields.put("produitsBio", new FieldAccess("produits_bio", VolontaireHc::getProduitsBio, VolontaireHc::setProduitsBio, VolontaireHcDTO::getProduitsBio));
        fields.put("protecteurSolaireCorps", new FieldAccess("protecteur_solaire_corps", VolontaireHc::getProtecteurSolaireCorps, VolontaireHc::setProtecteurSolaireCorps, VolontaireHcDTO::getProtecteurSolaireCorps));
        fields.put("protecteurSolaireLevres", new FieldAccess("protecteur_solaire_levres", VolontaireHc::getProtecteurSolaireLevres, VolontaireHc::setProtecteurSolaireLevres, VolontaireHcDTO::getProtecteurSolaireLevres));
        fields.put("protecteurSolaireVisage", new FieldAccess("protecteur_solaire_visage", VolontaireHc::getProtecteurSolaireVisage, VolontaireHc::setProtecteurSolaireVisage, VolontaireHcDTO::getProtecteurSolaireVisage));
        fields.put("rasoir", new FieldAccess("rasoir", VolontaireHc::getRasoir, VolontaireHc::setRasoir, VolontaireHcDTO::getRasoir));
        fields.put("rasoirElectrique", new FieldAccess("rasoir_electrique", VolontaireHc::getRasoirElectrique, VolontaireHc::setRasoirElectrique, VolontaireHcDTO::getRasoirElectrique));
        fields.put("rasoirMecanique", new FieldAccess("rasoir_mecanique", VolontaireHc::getRasoirMecanique, VolontaireHc::setRasoirMecanique, VolontaireHcDTO::getRasoirMecanique));
        fields.put("rougeALevres", new FieldAccess("rouge_a_levres", VolontaireHc::getRougeALevres, VolontaireHc::setRougeALevres, VolontaireHcDTO::getRougeALevres));
        fields.put("savon", new FieldAccess("savon", VolontaireHc::getSavon, VolontaireHc::setSavon, VolontaireHcDTO::getSavon));
        fields.put("shampoing", new FieldAccess("shampoing", VolontaireHc::getShampoing, VolontaireHc::setShampoing, VolontaireHcDTO::getShampoing));
        fields.put("soinAmincissant", new FieldAccess("soin_amincissant", VolontaireHc::getSoinAmincissant, VolontaireHc::setSoinAmincissant, VolontaireHcDTO::getSoinAmincissant));
        fields.put("soinAntiAgeCorps", new FieldAccess("soin_anti_age_corps", VolontaireHc::getSoinAntiAgeCorps, VolontaireHc::setSoinAntiAgeCorps, VolontaireHcDTO::getSoinAntiAgeCorps));
        fields.put("soinAntiAgeMains", new FieldAccess("soin_anti_age_mains", VolontaireHc::getSoinAntiAgeMains, VolontaireHc::setSoinAntiAgeMains, VolontaireHcDTO::getSoinAntiAgeMains));
        fields.put("soinAntiAgeVisage", new FieldAccess("soin_anti_age_visage", VolontaireHc::getSoinAntiAgeVisage, VolontaireHc::setSoinAntiAgeVisage, VolontaireHcDTO::getSoinAntiAgeVisage));
        fields.put("soinAntiCellulite", new FieldAccess("soin_anti_cellulite", VolontaireHc::getSoinAntiCellulite, VolontaireHc::setSoinAntiCellulite, VolontaireHcDTO::getSoinAntiCellulite));
        fields.put("soinAntiRidesVisage", new FieldAccess("soin_anti_rides_visage", VolontaireHc::getSoinAntiRidesVisage, VolontaireHc::setSoinAntiRidesVisage, VolontaireHcDTO::getSoinAntiRidesVisage));
        fields.put("soinAntiRougeursVisage", new FieldAccess("soin_anti_rougeurs_visage", VolontaireHc::getSoinAntiRougeursVisage, VolontaireHc::setSoinAntiRougeursVisage, VolontaireHcDTO::getSoinAntiRougeursVisage));
        fields.put("soinAntiTachesDecollete", new FieldAccess("soin_anti_taches_decollete", VolontaireHc::getSoinAntiTachesDecollete, VolontaireHc::setSoinAntiTachesDecollete, VolontaireHcDTO::getSoinAntiTachesDecollete));
        fields.put("soinAntiTachesMains", new FieldAccess("soin_anti_taches_mains", VolontaireHc::getSoinAntiTachesMains, VolontaireHc::setSoinAntiTachesMains, VolontaireHcDTO::getSoinAntiTachesMains));
        fields.put("soinAntiTachesVisage", new FieldAccess("soin_anti_taches_visage", VolontaireHc::getSoinAntiTachesVisage, VolontaireHc::setSoinAntiTachesVisage, VolontaireHcDTO::getSoinAntiTachesVisage));
        fields.put("soinAntiVergetures", new FieldAccess("soin_anti_vergetures", VolontaireHc::getSoinAntiVergetures, VolontaireHc::setSoinAntiVergetures, VolontaireHcDTO::getSoinAntiVergetures));
        fields.put("soinApresSoleil", new FieldAccess("soin_apres_soleil", VolontaireHc::getSoinApresSoleil, VolontaireHc::setSoinApresSoleil, VolontaireHcDTO::getSoinApresSoleil));
        fields.put("soinContourDesLevres", new FieldAccess("soin_contour_des_levres", VolontaireHc::getSoinContourDesLevres, VolontaireHc::setSoinContourDesLevres, VolontaireHcDTO::getSoinContourDesLevres));
        fields.put("soinContourDesYeux", new FieldAccess("soin_contour_des_yeux", VolontaireHc::getSoinContourDesYeux, VolontaireHc::setSoinContourDesYeux, VolontaireHcDTO::getSoinContourDesYeux));
        fields.put("soinEclatDuTeint", new FieldAccess("soin_eclat_du_teint", VolontaireHc::getSoinEclatDuTeint, VolontaireHc::setSoinEclatDuTeint, VolontaireHcDTO::getSoinEclatDuTeint));
        fields.put("soinHydratantCorps", new FieldAccess("soin_hydratant_corps", VolontaireHc::getSoinHydratantCorps, VolontaireHc::setSoinHydratantCorps, VolontaireHcDTO::getSoinHydratantCorps));
        fields.put("soinHydratantMains", new FieldAccess("soin_hydratant_mains", VolontaireHc::getSoinHydratantMains, VolontaireHc::setSoinHydratantMains, VolontaireHcDTO::getSoinHydratantMains));
        fields.put("soinHydratantVisage", new FieldAccess("soin_hydratant_visage", VolontaireHc::getSoinHydratantVisage, VolontaireHc::setSoinHydratantVisage, VolontaireHcDTO::getSoinHydratantVisage));
        fields.put("soinMatifiantVisage", new FieldAccess("soin_matifiant_visage", VolontaireHc::getSoinMatifiantVisage, VolontaireHc::setSoinMatifiantVisage, VolontaireHcDTO::getSoinMatifiantVisage));
        fields.put("soinNourissantVisage", new FieldAccess("soin_nourissant_visage", VolontaireHc::getSoinNourissantVisage, VolontaireHc::setSoinNourissantVisage, VolontaireHcDTO::getSoinNourissantVisage));
        fields.put("soinNourrissantCorps", new FieldAccess("soin_nourrissant_corps", VolontaireHc::getSoinNourrissantCorps, VolontaireHc::setSoinNourrissantCorps, VolontaireHcDTO::getSoinNourrissantCorps));
        fields.put("soinNourrissantMains", new FieldAccess("soin_nourrissant_mains", VolontaireHc::getSoinNourrissantMains, VolontaireHc::setSoinNourrissantMains, VolontaireHcDTO::getSoinNourrissantMains));
        fields.put("soinOngles", new FieldAccess("soin_ongles", VolontaireHc::getSoinOngles, VolontaireHc::setSoinOngles, VolontaireHcDTO::getSoinOngles));
        fields.put("soinPieds", new FieldAccess("soin_pieds", VolontaireHc::getSoinPieds, VolontaireHc::setSoinPieds, VolontaireHcDTO::getSoinPieds));
        fields.put("soinRaffermissantCorps", new FieldAccess("soin_raffermissant_corps", VolontaireHc::getSoinRaffermissantCorps, VolontaireHc::setSoinRaffermissantCorps, VolontaireHcDTO::getSoinRaffermissantCorps));
        fields.put("soinRaffermissantVisage", new FieldAccess("soin_raffermissant_visage", VolontaireHc::getSoinRaffermissantVisage, VolontaireHc::setSoinRaffermissantVisage, VolontaireHcDTO::getSoinRaffermissantVisage));
        fields.put("tondeuseBarbe", new FieldAccess("tondeuse_barbe", VolontaireHc::getTondeuseBarbe, VolontaireHc::setTondeuseBarbe, VolontaireHcDTO::getTondeuseBarbe));
        fields.put("tonique", new FieldAccess("tonique", VolontaireHc::getTonique, VolontaireHc::setTonique, VolontaireHcDTO::getTonique));
        fields.put("vernisAOngles", new FieldAccess("vernis_a_ongles", VolontaireHc::getVernisAOngles, VolontaireHc::setVernisAOngles, VolontaireHcDTO::getVernisAOngles));
        FIELDS = Collections.unmodifiableMap(fields);
    }
    private VolontaireHcFieldRegistry() {}
    static FieldAccess require(String name) {
        FieldAccess access = FIELDS.get(name);
        if (access == null) throw new IllegalArgumentException("Produit non reconnu: " + name);
        return access;
    }
    static Set<String> names() { return FIELDS.keySet(); }
    record FieldAccess(String columnName, Function<VolontaireHc, String> getter,
                       BiConsumer<VolontaireHc, String> setter,
                       Function<VolontaireHcDTO, String> dtoGetter) {
        String get(VolontaireHc entity) { return getter.apply(entity); }
        String get(VolontaireHcDTO dto) { return dtoGetter.apply(dto); }
        void set(VolontaireHc entity, String value) { setter.accept(entity, value); }
    }
}
