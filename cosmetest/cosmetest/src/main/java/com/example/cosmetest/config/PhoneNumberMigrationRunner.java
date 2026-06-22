package com.example.cosmetest.config;

import com.example.cosmetest.data.repository.VolontaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMigrationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PhoneNumberMigrationRunner.class);

    private final VolontaireRepository volontaireRepository;

    public PhoneNumberMigrationRunner(VolontaireRepository volontaireRepository) {
        this.volontaireRepository = volontaireRepository;
    }

    @Override
    public void run(String... args) {
        int domicileUpdated = volontaireRepository.prefixMissingZeroOnTelDomicileVol();
        int portableUpdated = volontaireRepository.prefixMissingZeroOnTelPortableVol();
        int totalUpdated = domicileUpdated + portableUpdated;

        if (totalUpdated > 0) {
            log.info("Migration telephones volontaires: {} lignes mises a jour (domicile={}, portable={})",
                    totalUpdated, domicileUpdated, portableUpdated);
        }
    }
}
