package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.DocCheckDuplicateCandidateDTO;
import com.example.cosmetest.business.dto.DocCheckFieldDTO;
import com.example.cosmetest.business.dto.DocCheckImportPreviewDTO;
import com.example.cosmetest.business.dto.DocCheckImportRequestDTO;
import com.example.cosmetest.business.dto.VolontaireDTO;
import com.example.cosmetest.business.dto.VolontaireDetailDTO;
import com.example.cosmetest.business.service.DocCheckImportService;
import com.example.cosmetest.business.service.VolontaireService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class DocCheckImportServiceImpl implements DocCheckImportService {
    private final VolontaireService volontaireService;

    public DocCheckImportServiceImpl(VolontaireService volontaireService) {
        this.volontaireService = volontaireService;
    }

    @Override
    @Transactional(readOnly = true)
    public DocCheckImportPreviewDTO preview(DocCheckImportRequestDTO request) {
        VolontaireDetailDTO volontaire = mapToVolontaire(request);
        List<String> warnings = validateForImport(request, volontaire);
        List<DocCheckDuplicateCandidateDTO> duplicates = findDuplicateCandidates(volontaire);

        DocCheckImportPreviewDTO response = new DocCheckImportPreviewDTO();
        response.setDocumentId(request != null ? request.getDocumentId() : null);
        response.setVolontaire(volontaire);
        response.setWarnings(warnings);
        response.setDuplicateCandidates(duplicates);
        response.setCanImport(warnings.stream().noneMatch(w -> w.startsWith("Champ obligatoire"))
                && duplicates.isEmpty());
        return response;
    }

    @Override
    @Transactional
    public VolontaireDetailDTO confirm(DocCheckImportRequestDTO request) {
        DocCheckImportPreviewDTO preview = preview(request);
        if (!preview.isCanImport()) {
            throw new IllegalArgumentException("Import bloque: corriger les alertes ou verifier les doublons avant insertion.");
        }
        return volontaireService.createVolontaireDetail(preview.getVolontaire());
    }

    private VolontaireDetailDTO mapToVolontaire(DocCheckImportRequestDTO request) {
        VolontaireDetailDTO volontaire = new VolontaireDetailDTO();
        if (request == null || request.getFields() == null) {
            return volontaire;
        }

        Map<String, String> values = new LinkedHashMap<>();
        for (DocCheckFieldDTO field : request.getFields()) {
            if (field == null || isBlank(field.getKey()) || isBlank(field.getValue())) {
                continue;
            }
            values.put(field.getKey(), field.getValue().trim());
        }

        volontaire.setNomVol(values.get("nom"));
        volontaire.setPrenomVol(values.get("prenom"));
        volontaire.setTitreVol(values.get("titre"));
        volontaire.setEmailVol(values.get("email"));
        volontaire.setTelPortableVol(normalizePhone(values.get("telephone")));
        volontaire.setTelDomicileVol(normalizePhone(values.get("telephoneDomicile")));
        volontaire.setAdresseVol(values.get("adresse"));
        volontaire.setCpVol(values.get("codePostal"));
        volontaire.setVilleVol(values.get("ville"));
        volontaire.setSexe(values.get("sexe"));
        volontaire.setDateNaissance(parseLocalDate(values.get("dateNaissance")));
        volontaire.setTaille(parseInteger(values.get("taille")));
        volontaire.setPoids(parseInteger(values.get("poids")));
        volontaire.setEthnie(values.get("ethnie"));
        volontaire.setSousEthnie(values.get("sousEthnie"));
        volontaire.setPhototype(values.get("phototype"));
        volontaire.setTypePeauVisage(values.get("typePeauVisage"));
        volontaire.setSanteCompatible(values.get("santeCompatible"));
        volontaire.setObservations(firstNonBlank(values.get("observations"), values.get("commentaire")));

        values.forEach((key, value) -> applyDetailProperty(volontaire, key, value));
        return volontaire;
    }

    private List<String> validateForImport(DocCheckImportRequestDTO request, VolontaireDetailDTO volontaire) {
        List<String> warnings = new ArrayList<>();
        if (request == null || isBlank(request.getDocumentId())) {
            warnings.add("Document source absent.");
        }
        if (isBlank(volontaire.getNomVol())) {
            warnings.add("Champ obligatoire manquant: nom.");
        }
        if (isBlank(volontaire.getPrenomVol())) {
            warnings.add("Champ obligatoire manquant: prenom.");
        }
        if (isBlank(volontaire.getTelPortableVol())) {
            warnings.add("Champ obligatoire manquant: telephone.");
        }
        if (volontaire.getDateNaissance() == null) {
            warnings.add("Champ obligatoire manquant: date de naissance.");
        }

        if (request != null && request.getFields() != null) {
            request.getFields().stream()
                    .filter(Objects::nonNull)
                    .filter(field -> field.getConfidence() != null && field.getConfidence() < 0.80)
                    .map(field -> "Champ a verifier: " + readableFieldName(field))
                    .forEach(warnings::add);
        }

        return warnings;
    }

    private List<DocCheckDuplicateCandidateDTO> findDuplicateCandidates(VolontaireDetailDTO volontaire) {
        Map<Integer, DocCheckDuplicateCandidateDTO> candidates = new LinkedHashMap<>();

        if (!isBlank(volontaire.getEmailVol())) {
            volontaireService.findByEmail(volontaire.getEmailVol().trim())
                    .ifPresent(v -> candidates.put(v.getIdVol(), new DocCheckDuplicateCandidateDTO(v, "email")));
        }

        if (!isBlank(volontaire.getNomVol()) || !isBlank(volontaire.getPrenomVol()) || !isBlank(volontaire.getTelPortableVol())) {
            Page<VolontaireDTO> page = volontaireService.searchByMultipleFields(
                    volontaire.getNomVol(),
                    volontaire.getPrenomVol(),
                    null,
                    volontaire.getTelPortableVol(),
                    null,
                    null,
                    null,
                    true,
                    0,
                    10);
            page.getContent().forEach(v -> candidates.putIfAbsent(
                    v.getIdVol(),
                    new DocCheckDuplicateCandidateDTO(v, "nom/prenom/telephone")));
        }

        return new ArrayList<>(candidates.values());
    }

    private void applyDetailProperty(VolontaireDetailDTO volontaire, String key, String value) {
        try {
            for (PropertyDescriptor descriptor : Introspector.getBeanInfo(VolontaireDetailDTO.class).getPropertyDescriptors()) {
                if (!descriptor.getName().equals(key) || descriptor.getWriteMethod() == null) {
                    continue;
                }
                Method setter = descriptor.getWriteMethod();
                Class<?> propertyType = descriptor.getPropertyType();
                Object converted = convertValue(value, propertyType);
                if (converted != null) {
                    setter.invoke(volontaire, converted);
                }
                return;
            }
        } catch (Exception ignored) {
            // Le champ reste editable dans doc-check; on ignore seulement les champs inconnus du DTO actuel.
        }
    }

    private Object convertValue(String value, Class<?> propertyType) {
        if (isBlank(value)) {
            return null;
        }
        if (String.class.equals(propertyType)) {
            return value.trim();
        }
        if (Integer.class.equals(propertyType) || int.class.equals(propertyType)) {
            return parseInteger(value);
        }
        if (Float.class.equals(propertyType) || float.class.equals(propertyType)) {
            return Float.parseFloat(value.trim().replace(",", "."));
        }
        if (Double.class.equals(propertyType) || double.class.equals(propertyType)) {
            return Double.parseDouble(value.trim().replace(",", "."));
        }
        if (LocalDate.class.equals(propertyType)) {
            return parseLocalDate(value);
        }
        if (Date.class.equals(propertyType)) {
            LocalDate localDate = parseLocalDate(value);
            return localDate != null ? Date.valueOf(localDate) : null;
        }
        return null;
    }

    private Integer parseInteger(String value) {
        if (isBlank(value)) {
            return null;
        }
        return Integer.parseInt(value.trim().replaceAll("[^0-9-]", ""));
    }

    private LocalDate parseLocalDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        String cleaned = value.trim();
        if (cleaned.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] parts = cleaned.split("/");
            return LocalDate.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0]));
        }
        return LocalDate.parse(cleaned);
    }

    private String normalizePhone(String value) {
        if (isBlank(value)) {
            return null;
        }
        String digits = value.replaceAll("\\D", "");
        if (digits.length() == 9 && !digits.startsWith("0")) {
            return "0" + digits;
        }
        return digits;
    }

    private String readableFieldName(DocCheckFieldDTO field) {
        if (!isBlank(field.getLabel())) {
            return field.getLabel();
        }
        return field.getKey();
    }

    private String firstNonBlank(String first, String second) {
        return !isBlank(first) ? first : second;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
