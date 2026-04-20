package com.example.cosmetest.business.service;

import com.example.cosmetest.business.dto.PaymentBatchResultDTO;

public interface PaymentBatchService {
    /**
     * Marque comme payés tous les paiements non payés d'une étude,
     * en excluant les volontaires annulés.
     */
    PaymentBatchResultDTO markAllAsPaid(int idEtude);
}

