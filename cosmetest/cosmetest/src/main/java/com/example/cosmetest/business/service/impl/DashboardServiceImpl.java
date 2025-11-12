// src/main/java/com/example/cosmetest/business/service/impl/DashboardServiceImpl.java
package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.*;
import com.example.cosmetest.business.service.*;
import com.example.cosmetest.data.repository.*;
import com.example.cosmetest.domain.model.Rdv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
// Ajouter l'import manquant en haut du fichier

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private VolontaireService volontaireService;

    @Autowired
    private EtudeService etudeService;

    @Autowired
    private RdvService rdvService;

    @Autowired
    private PreinscritService preinscritService;

    @Autowired
    private RdvRepository rdvRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setVolontairesActifs(volontaireService.countActiveVolontaires());

        int etudesEnCours = etudeService.countCurrentEtudes();
        System.out.println(" Nombre d'études en cours récupérées : " + etudesEnCours);
        stats.setEtudesEnCours(etudesEnCours);

        stats.setRdvToday(rdvService.countRdvForToday());
        stats.setPreinscrits(preinscritService.countPreinscrits());

        return stats;
    }


    @Override
    public DailyStatsDTO getDailyStats() {
        DailyStatsDTO dailyStats = new DailyStatsDTO();

        // Récupération du nombre de volontaires ajoutés aujourd'hui
        dailyStats.setVolontairesAjoutes(volontaireService.countVolontairesAddedToday());

        // Récupération du nombre de rdv effectués aujourd'hui
        dailyStats.setRdvEffectues(rdvService.countCompletedRdvToday());

        // Récupération du nombre de nouvelles préinscriptions aujourd'hui
        dailyStats.setNouvellesPreinscriptions(preinscritService.countNewPreinscritsToday());

        return dailyStats;
    }

    @Override
    public List<RdvDTO> getProchainRdvs(int limit) {
        return rdvService.getUpcomingRdvs(limit);
    }

    @Override
    public List<EtudeDTO> getRecentEtudes(int limit) {
        return etudeService.getRecentEtudes(limit);
    }

    /**
     * Add this method to your getRecentActivities method in DashboardServiceImpl
     * to handle null dates in activities
     */
    @Override
    public List<ActiviteRecenteDTO> getRecentActivities(int limit) {
        List<ActiviteRecenteDTO> activities = new ArrayList<>();

        try {
            // Récupérer les volontaires récents
            List<VolontaireDTO> recentVolontaires = volontaireService.getRecentVolontaires(limit);

            // Transformer les volontaires en activités
            for (VolontaireDTO v : recentVolontaires) {
                if (v != null) {
                    ActiviteRecenteDTO activity = new ActiviteRecenteDTO();
                    activity.setId(v.getIdVol() != null ? v.getIdVol().longValue() : null);
                    activity.setType("volontaire_ajout");
                    activity.setDate(v.getDateI());  // Use current date if null
                    activity.setUser("Admin");
                    activity.setDescription("a ajouté le volontaire " +
                            (v.getPrenomVol() != null ? v.getPrenomVol() : "") + " " +
                            (v.getNomVol() != null ? v.getNomVol() : ""));
                    activities.add(activity);
                }
            }

            // Récupérer les RDV récents
            List<RdvDTO> recentRdvs = rdvService.getRecentRdvs(limit);

            // Transformer les RDV en activités
            for (RdvDTO r : recentRdvs) {
                if (r != null) {
                    ActiviteRecenteDTO activity = new ActiviteRecenteDTO();
                    activity.setId((long) r.getIdRdv()); // Convertir int en Long
                    activity.setType("rdv_planification");

                    // Handle null date by providing a default (today)
                    LocalDate activityDate = (r.getDate() != null) ?
                            r.getDate() : LocalDate.now();
                    activity.setDate(activityDate);

                    // Comme getCreatedBy n'existe pas, utilisez une valeur par défaut
                    activity.setUser("Système");

                    // Comme getEtude().getRef() n'existe pas, utilisez directement l'ID de l'étude
                    activity.setDescription("a planifié un RDV pour l'étude #" + r.getIdEtude());

                    activities.add(activity);
                }
            }

            // Trier par date, handling null dates gracefully
            activities.sort((a, b) -> {
                // If both dates are null, consider them equal
                if (a.getDate() == null && b.getDate() == null) return 0;
                // Null dates should come last in sorting
                if (a.getDate() == null) return 1;
                if (b.getDate() == null) return -1;
                // Normal comparison if both dates are non-null
                return b.getDate().compareTo(a.getDate());
            });

            // Limiter le nombre de résultats
            if (activities.size() > limit) {
                activities = activities.subList(0, limit);
            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error in getRecentActivities: " + e.getMessage());
            e.printStackTrace();
        }

        return activities;
    }

    @Override
    public List<RdvDTO> getUpcomingRdvs(int limit) {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(0, limit);

        Page<Rdv> rdvPage = rdvRepository.findByDateGreaterThanEqualOrderByDateAscHeureAsc(today, pageable);

        return rdvPage.getContent().stream()
                .map(this::mapToDTO) // Maintenant valide
                .collect(Collectors.toList());
    }

    // Méthode de conversion ajoutée
    private RdvDTO mapToDTO(Rdv rdv) {
        RdvDTO dto = new RdvDTO();
        dto.setIdEtude(rdv.getId().getIdEtude());
        dto.setIdRdv(rdv.getId().getIdRdv());
        dto.setDate(rdv.getDate());
        dto.setHeure(rdv.getHeure());
        dto.setEtat(rdv.getEtat());
        dto.setCommentaires(rdv.getCommentaires());
        return dto;
    }
}