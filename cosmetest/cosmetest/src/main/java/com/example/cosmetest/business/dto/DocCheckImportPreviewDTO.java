package com.example.cosmetest.business.dto;

import java.util.ArrayList;
import java.util.List;

public class DocCheckImportPreviewDTO {
    private String documentId;
    private VolontaireDetailDTO volontaire;
    private List<String> warnings = new ArrayList<>();
    private List<DocCheckDuplicateCandidateDTO> duplicateCandidates = new ArrayList<>();
    private boolean canImport;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public VolontaireDetailDTO getVolontaire() {
        return volontaire;
    }

    public void setVolontaire(VolontaireDetailDTO volontaire) {
        this.volontaire = volontaire;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<DocCheckDuplicateCandidateDTO> getDuplicateCandidates() {
        return duplicateCandidates;
    }

    public void setDuplicateCandidates(List<DocCheckDuplicateCandidateDTO> duplicateCandidates) {
        this.duplicateCandidates = duplicateCandidates;
    }

    public boolean isCanImport() {
        return canImport;
    }

    public void setCanImport(boolean canImport) {
        this.canImport = canImport;
    }
}
