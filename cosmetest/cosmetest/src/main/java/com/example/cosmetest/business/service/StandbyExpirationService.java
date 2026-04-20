package com.example.cosmetest.business.service;

import com.example.cosmetest.data.repository.VolontaireRepository;
import com.example.cosmetest.domain.model.Volontaire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service planifié qui retire automatiquement le stand-by des volontaires
 * dont la date de fin est dépassée.
 * S'exécute tous les jours à 2h du matin.
 */
@Service
public class StandbyExpirationService {

    private static final Logger logger = LoggerFactory.getLogger(StandbyExpirationService.class);

    private final VolontaireRepository volontaireRepository;

    public StandbyExpirationService(VolontaireRepository volontaireRepository) {
        this.volontaireRepository = volontaireRepository;
    }

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void removeExpiredStandby() {
        String today = LocalDate.now().toString();
        List<Volontaire> expired = volontaireRepository.findExpiredStandby(today);

        if (expired.isEmpty()) {
            logger.debug("Aucun stand-by expiré trouvé.");
            return;
        }

        logger.info("{} volontaire(s) en stand-by expiré(s) trouvé(s), retrait en cours...", expired.size());

        for (Volontaire v : expired) {
            v.setArchive(false);
            v.setStandby(false);
            v.setDateFinStandby(null);
            volontaireRepository.save(v);
            logger.info("Stand-by retiré pour le volontaire {} {} (ID: {})",
                    v.getNomVol(), v.getPrenomVol(), v.getIdVol());
        }
    }
}
