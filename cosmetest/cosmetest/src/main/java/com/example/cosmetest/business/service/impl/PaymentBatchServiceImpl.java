package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.AnnulationDTO;
import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.dto.PaymentBatchResultDTO;
import com.example.cosmetest.business.service.AnnulationService;
import com.example.cosmetest.business.service.EtudeService;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.business.service.PaymentBatchService;
import com.example.cosmetest.domain.model.EtudeVolontaireId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PaymentBatchServiceImpl implements PaymentBatchService {

    private static final Logger log = LoggerFactory.getLogger(PaymentBatchServiceImpl.class);

    private final EtudeVolontaireService etudeVolontaireService;
    private final AnnulationService annulationService;
    private final EtudeService etudeService;

    public PaymentBatchServiceImpl(EtudeVolontaireService etudeVolontaireService,
                                   AnnulationService annulationService,
                                   EtudeService etudeService) {
        this.etudeVolontaireService = etudeVolontaireService;
        this.annulationService = annulationService;
        this.etudeService = etudeService;
    }

    @Override
    @Transactional
    public PaymentBatchResultDTO markAllAsPaid(int idEtude) {
        PaymentBatchResultDTO result = new PaymentBatchResultDTO();
        result.setIdEtude(idEtude);

        // Charger les paiements de l'étude
        List<EtudeVolontaireDTO> associations = etudeVolontaireService.getEtudeVolontairesByEtude(idEtude);
        result.setProcessedCount(associations != null ? associations.size() : 0);

        if (associations == null || associations.isEmpty()) {
            log.info("Aucune association trouvée pour l'étude {}", idEtude);
            etudeService.updatePayeStatus(idEtude, 0);
            return result;
        }

        // Précharger les annulations pour l'étude
        Set<Integer> cancelledVolunteers = new HashSet<>();
        List<AnnulationDTO> annulations = annulationService.getAnnulationsByEtude(idEtude);
        if (annulations != null) {
            for (AnnulationDTO a : annulations) {
                cancelledVolunteers.add(a.getIdVol());
            }
        }

        int updated = 0;
        int skippedAnnules = 0;
        int alreadyPaid = 0;
        int errors = 0;

        for (EtudeVolontaireDTO dto : associations) {
            // Déjà payé
            if (dto.getPaye() == 1) {
                alreadyPaid++;
                continue;
            }

            // Annulé ⇒ ignorer
            if (cancelledVolunteers.contains(dto.getIdVolontaire())) {
                skippedAnnules++;
                continue;
            }

            try {
                EtudeVolontaireId id = new EtudeVolontaireId(
                        dto.getIdEtude(),
                        dto.getIdGroupe(),
                        dto.getIdVolontaire(),
                        dto.getIv(),
                        dto.getNumsujet(),
                        dto.getPaye(),
                        dto.getStatut()
                );
                etudeVolontaireService.updatePaye(id, 1);
                updated++;
            } catch (Exception e) {
                errors++;
                result.getErrors().add("Volontaire " + dto.getIdVolontaire() + ": " + e.getMessage());
                log.error("Erreur MAJ paiement pour vol {} etude {}: {}", dto.getIdVolontaire(), idEtude, e.getMessage());
            }
        }

        result.setUpdatedCount(updated);
        result.setSkippedAnnules(skippedAnnules);
        result.setAlreadyPaidCount(alreadyPaid);
        result.setErrorCount(errors);

        // Mettre à jour le statut PAYE de l'étude (0 ou 2)
        try {
            boolean allPaid = associations.stream()
                    .filter(dto -> !cancelledVolunteers.contains(dto.getIdVolontaire()))
                    .allMatch(dto -> dto.getPaye() == 1 || // déjà payé
                            (dto.getPaye() != 1 && dto.getIdVolontaire() == 0)); // tolérer ID=0 si besoin

            etudeService.updatePayeStatus(idEtude, allPaid ? 2 : 0);
        } catch (Exception e) {
            log.warn("Impossible de mettre à jour le statut PAYE de l'étude {}: {}", idEtude, e.getMessage());
        }

        return result;
    }
}

