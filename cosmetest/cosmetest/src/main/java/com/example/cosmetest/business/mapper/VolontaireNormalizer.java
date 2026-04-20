package com.example.cosmetest.business.mapper;

import java.text.Normalizer;
import java.util.Map;

/**
 * Normalise les valeurs lues en BDD vers le format attendu par le frontend.
 * Ne modifie PAS les noms de champs/colonnes.
 */
public class VolontaireNormalizer {

    private static final Map<String, String> SEXE_MAP = Map.of(
            "MASCULIN", "Masculin",
            "FEMININ", "Féminin",
            "FÉMININ", "Féminin",
            "M", "Masculin",
            "F", "Féminin",
            "AUTRE", "O",
            "O", "O"
    );

    private static final Map<String, String> TYPE_PEAU_MAP = Map.ofEntries(
            Map.entry("NORMALE", "Normale"),
            Map.entry("SECHE", "Sèche"),
            Map.entry("SÈCHE", "Sèche"),
            Map.entry("GRASSE", "Grasse"),
            Map.entry("MIXTE", "Mixte"),
            Map.entry("MIXTE TENDANCE GRASSE", "Mixte à tendance grasse"),
            Map.entry("MIXTE À TENDANCE GRASSE", "Mixte à tendance grasse"),
            Map.entry("MIXTE A TENDANCE GRASSE", "Mixte à tendance grasse"),
            Map.entry("MIXTE TENDANCE SECHE", "Mixte à tendance sèche"),
            Map.entry("MIXTE TENDANCE SÈCHE", "Mixte à tendance sèche"),
            Map.entry("MIXTE À TENDANCE SÈCHE", "Mixte à tendance sèche"),
            Map.entry("MIXTE A TENDANCE SECHE", "Mixte à tendance sèche"),
            Map.entry("N", "Normale"),
            Map.entry("S", "Sèche"),
            Map.entry("G", "Grasse")
    );

    private static final Map<String, String> PHOTOTYPE_ROMAN_MAP = Map.of(
            "I", "1",
            "II", "2",
            "III", "3",
            "IV", "4",
            "V", "5",
            "VI", "6"
    );

    public static String normalizeSexe(String raw) {
        if (raw == null || raw.isBlank()) return raw;
        String key = stripAccents(raw.trim()).toUpperCase();
        // Essayer d'abord la valeur brute (avec accents)
        String result = SEXE_MAP.get(raw.trim());
        if (result != null) return result;
        // Puis sans accents en uppercase
        result = SEXE_MAP.get(key);
        return result != null ? result : raw;
    }

    public static String normalizeTypePeauVisage(String raw) {
        if (raw == null || raw.isBlank()) return raw;
        String key = stripAccents(raw.trim()).toUpperCase();
        // Essayer d'abord la valeur brute
        String result = TYPE_PEAU_MAP.get(raw.trim());
        if (result != null) return result;
        // Puis sans accents en uppercase
        result = TYPE_PEAU_MAP.get(key);
        return result != null ? result : raw;
    }

    public static String normalizePhototype(String raw) {
        if (raw == null || raw.isBlank()) return raw;
        String s = raw.trim();

        // Déjà au format "Phototype X" ?
        if (s.matches("(?i)phototype\\s+\\d")) return s;

        // Chiffre romain seul (I, II, III, IV, V, VI)
        String num = PHOTOTYPE_ROMAN_MAP.get(s.toUpperCase());
        if (num != null) return "Phototype " + num;

        // Chiffre romain avec description (ex: "III - Peau claire à mate")
        String prefix = s.split("\\s*-\\s*")[0].trim().toUpperCase();
        num = PHOTOTYPE_ROMAN_MAP.get(prefix);
        if (num != null) return "Phototype " + num;

        // Chiffre arabe seul
        if (s.matches("\\d")) return "Phototype " + s;

        return raw;
    }

    private static String stripAccents(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
