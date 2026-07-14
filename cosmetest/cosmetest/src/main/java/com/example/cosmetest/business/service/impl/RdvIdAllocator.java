package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.data.repository.RdvRepository;
import org.springframework.stereotype.Component;

@Component
public class RdvIdAllocator {

    private final RdvRepository rdvRepository;

    public RdvIdAllocator(RdvRepository rdvRepository) {
        this.rdvRepository = rdvRepository;
    }

    /**
     * Alloue le prochain identifiant d'une étude. L'appelant doit déjà être dans
     * une transaction afin que le verrou pessimiste du repository reste actif
     * jusqu'à l'insertion du rendez-vous.
     */
    public int nextForStudy(int idEtude) {
        int nextId = rdvRepository.findLastRdvForEtudeForUpdate(idEtude)
                .map(rdv -> rdv.getIdRdv() + 1)
                .orElse(1);

        while (rdvRepository.existsByIdEtudeAndIdRdv(idEtude, nextId)) {
            nextId++;
        }
        return nextId;
    }
}
