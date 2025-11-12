package com.example.cosmetest.business.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Résumé d'une opération de mise à jour en masse des paiements
 */
public class PaymentBatchResultDTO {
    private int idEtude;
    private int processedCount;
    private int updatedCount;
    private int skippedAnnules;
    private int alreadyPaidCount;
    private int errorCount;
    private List<String> errors = new ArrayList<>();

    public int getIdEtude() {
        return idEtude;
    }

    public void setIdEtude(int idEtude) {
        this.idEtude = idEtude;
    }

    public int getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(int processedCount) {
        this.processedCount = processedCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }

    public int getSkippedAnnules() {
        return skippedAnnules;
    }

    public void setSkippedAnnules(int skippedAnnules) {
        this.skippedAnnules = skippedAnnules;
    }

    public int getAlreadyPaidCount() {
        return alreadyPaidCount;
    }

    public void setAlreadyPaidCount(int alreadyPaidCount) {
        this.alreadyPaidCount = alreadyPaidCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

