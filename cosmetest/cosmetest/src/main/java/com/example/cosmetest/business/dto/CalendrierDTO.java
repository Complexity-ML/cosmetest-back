package com.example.cosmetest.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO optimisé pour les données du calendrier des rendez-vous
 * Encapsule toutes les informations nécessaires à l'affichage du calendrier
 * en une seule structure pour minimiser les appels API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CalendrierDTO {

    // Informations de période
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

    // RDV enrichis avec toutes les informations nécessaires
    private List<RendezVousEnrichiDTO> rdvs;

    // Études de la période (même sans RDV)
    private List<EtudeCalendrierDTO> etudes;

    // Statistiques de la période
    private StatistiquesCalendrierDTO statistiques;

    // Métadonnées pour optimisation frontend
    private MetaDonneesCalendrierDTO metaDonnees;

    // Constructeurs
    public CalendrierDTO() {
    }

    public CalendrierDTO(LocalDate dateDebut, LocalDate dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et Setters
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public List<RendezVousEnrichiDTO> getRdvs() {
        return rdvs;
    }

    public void setRdvs(List<RendezVousEnrichiDTO> rdvs) {
        this.rdvs = rdvs;
    }

    public List<EtudeCalendrierDTO> getEtudes() {
        return etudes;
    }

    public void setEtudes(List<EtudeCalendrierDTO> etudes) {
        this.etudes = etudes;
    }

    public StatistiquesCalendrierDTO getStatistiques() {
        return statistiques;
    }

    public void setStatistiques(StatistiquesCalendrierDTO statistiques) {
        this.statistiques = statistiques;
    }

    public MetaDonneesCalendrierDTO getMetaDonnees() {
        return metaDonnees;
    }

    public void setMetaDonnees(MetaDonneesCalendrierDTO metaDonnees) {
        this.metaDonnees = metaDonnees;
    }

    /**
     * DTO pour un RDV enrichi avec toutes les informations nécessaires
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RendezVousEnrichiDTO {
        // Identifiants
        private Integer idRdv;
        private Integer idEtude;
        private Integer idVolontaire;
        private Integer idGroupe;

        // Informations temporelles
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        private String heure;
        private String etat;
        private String commentaires;

        // Informations enrichies de l'étude
        private EtudeMinimalDTO etude;

        // Informations enrichies du volontaire
        private VolontaireMinimalDTO volontaire;

        // Statut temporel calculé (past, today, upcoming)
        private String statutTemporel;

        // Constructeur par défaut
        public RendezVousEnrichiDTO() {
        }

        // Getters et Setters
        public Integer getIdRdv() {
            return idRdv;
        }

        public void setIdRdv(Integer idRdv) {
            this.idRdv = idRdv;
        }

        public Integer getIdEtude() {
            return idEtude;
        }

        public void setIdEtude(Integer idEtude) {
            this.idEtude = idEtude;
        }

        public Integer getIdVolontaire() {
            return idVolontaire;
        }

        public void setIdVolontaire(Integer idVolontaire) {
            this.idVolontaire = idVolontaire;
        }

        public Integer getIdGroupe() {
            return idGroupe;
        }

        public void setIdGroupe(Integer idGroupe) {
            this.idGroupe = idGroupe;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getHeure() {
            return heure;
        }

        public void setHeure(String heure) {
            this.heure = heure;
        }

        public String getEtat() {
            return etat;
        }

        public void setEtat(String etat) {
            this.etat = etat;
        }

        public String getCommentaires() {
            return commentaires;
        }

        public void setCommentaires(String commentaires) {
            this.commentaires = commentaires;
        }

        public EtudeMinimalDTO getEtude() {
            return etude;
        }

        public void setEtude(EtudeMinimalDTO etude) {
            this.etude = etude;
        }

        public VolontaireMinimalDTO getVolontaire() {
            return volontaire;
        }

        public void setVolontaire(VolontaireMinimalDTO volontaire) {
            this.volontaire = volontaire;
        }

        public String getStatutTemporel() {
            return statutTemporel;
        }

        public void setStatutTemporel(String statutTemporel) {
            this.statutTemporel = statutTemporel;
        }
    }

    /**
     * DTO minimal pour les informations d'étude dans le calendrier
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EtudeMinimalDTO {
        private Integer id;
        private String ref;
        private String titre;
        private String type;
        private Integer nbSujets;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateDebut;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateFin;

        // Constructeur
        public EtudeMinimalDTO() {
        }

        // Getters et Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getNbSujets() {
            return nbSujets;
        }

        public void setNbSujets(Integer nbSujets) {
            this.nbSujets = nbSujets;
        }

        public LocalDate getDateDebut() {
            return dateDebut;
        }

        public void setDateDebut(LocalDate dateDebut) {
            this.dateDebut = dateDebut;
        }

        public LocalDate getDateFin() {
            return dateFin;
        }

        public void setDateFin(LocalDate dateFin) {
            this.dateFin = dateFin;
        }
    }

    /**
     * DTO minimal pour les informations de volontaire dans le calendrier
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VolontaireMinimalDTO {
        private Integer id;
        private String nom;
        private String prenom;
        private String titre;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateNaissance;

        // Constructeur
        public VolontaireMinimalDTO() {
        }

        // Getters et Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public LocalDate getDateNaissance() {
            return dateNaissance;
        }

        public void setDateNaissance(LocalDate dateNaissance) {
            this.dateNaissance = dateNaissance;
        }
    }

    /**
     * DTO pour les statistiques du calendrier
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StatistiquesCalendrierDTO {
        private int totalRdv;
        private int rdvConfirmes;
        private int rdvEnAttente;
        private int rdvAnnules;
        private int rdvCompletes;

        private int totalEtudes;
        private int etudesActives;

        private Map<String, Integer> repartitionParJour;
        private Map<String, Integer> repartitionParHeure;
        private Map<String, Integer> repartitionParEtat;

        // Constructeur
        public StatistiquesCalendrierDTO() {
        }

        // Getters et Setters
        public int getTotalRdv() {
            return totalRdv;
        }

        public void setTotalRdv(int totalRdv) {
            this.totalRdv = totalRdv;
        }

        public int getRdvConfirmes() {
            return rdvConfirmes;
        }

        public void setRdvConfirmes(int rdvConfirmes) {
            this.rdvConfirmes = rdvConfirmes;
        }

        public int getRdvEnAttente() {
            return rdvEnAttente;
        }

        public void setRdvEnAttente(int rdvEnAttente) {
            this.rdvEnAttente = rdvEnAttente;
        }

        public int getRdvAnnules() {
            return rdvAnnules;
        }

        public void setRdvAnnules(int rdvAnnules) {
            this.rdvAnnules = rdvAnnules;
        }

        public int getRdvCompletes() {
            return rdvCompletes;
        }

        public void setRdvCompletes(int rdvCompletes) {
            this.rdvCompletes = rdvCompletes;
        }

        public int getTotalEtudes() {
            return totalEtudes;
        }

        public void setTotalEtudes(int totalEtudes) {
            this.totalEtudes = totalEtudes;
        }

        public int getEtudesActives() {
            return etudesActives;
        }

        public void setEtudesActives(int etudesActives) {
            this.etudesActives = etudesActives;
        }

        public Map<String, Integer> getRepartitionParJour() {
            return repartitionParJour;
        }

        public void setRepartitionParJour(Map<String, Integer> repartitionParJour) {
            this.repartitionParJour = repartitionParJour;
        }

        public Map<String, Integer> getRepartitionParHeure() {
            return repartitionParHeure;
        }

        public void setRepartitionParHeure(Map<String, Integer> repartitionParHeure) {
            this.repartitionParHeure = repartitionParHeure;
        }

        public Map<String, Integer> getRepartitionParEtat() {
            return repartitionParEtat;
        }

        public void setRepartitionParEtat(Map<String, Integer> repartitionParEtat) {
            this.repartitionParEtat = repartitionParEtat;
        }
    }

    /**
     * DTO pour les métadonnées d'optimisation
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MetaDonneesCalendrierDTO {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime horodatageGeneration;

        private int dureeGenerationMs;
        private boolean donneesCache;
        private String versionCache;

        // Constructeur
        public MetaDonneesCalendrierDTO() {
        }

        // Getters et Setters
        public LocalDateTime getHorodatageGeneration() {
            return horodatageGeneration;
        }

        public void setHorodatageGeneration(LocalDateTime horodatageGeneration) {
            this.horodatageGeneration = horodatageGeneration;
        }

        public int getDureeGenerationMs() {
            return dureeGenerationMs;
        }

        public void setDureeGenerationMs(int dureeGenerationMs) {
            this.dureeGenerationMs = dureeGenerationMs;
        }

        public boolean isDonneesCache() {
            return donneesCache;
        }

        public void setDonneesCache(boolean donneesCache) {
            this.donneesCache = donneesCache;
        }

        public String getVersionCache() {
            return versionCache;
        }

        public void setVersionCache(String versionCache) {
            this.versionCache = versionCache;
        }
    }
    // Ajouter cette méthode dans CalendrierDTO.EtudeCalendrierDTO

    /**
     * DTO pour les données d'étude dans le contexte du calendrier
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EtudeCalendrierDTO extends EtudeMinimalDTO {
        private Integer nombreRdv;
        private boolean aRdvDansPeriode;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dernierRdv;

        // *** CHAMP IMPORTANT : Affichage des dates avec RDV ***
        private String datesAvecRdvDisplay;

        // *** NOUVEAU : Liste des dates effectives avec RDV ***
        @JsonFormat(pattern = "yyyy-MM-dd")
        private List<LocalDate> datesEffectivesAvecRdv;

        // Constructeur
        public EtudeCalendrierDTO() {
        }

        // Getters et Setters existants...
        public Integer getNombreRdv() {
            return nombreRdv;
        }

        public void setNombreRdv(Integer nombreRdv) {
            this.nombreRdv = nombreRdv;
        }

        public boolean isARdvDansPeriode() {
            return aRdvDansPeriode;
        }

        public void setARdvDansPeriode(boolean aRdvDansPeriode) {
            this.aRdvDansPeriode = aRdvDansPeriode;
        }

        public LocalDateTime getDernierRdv() {
            return dernierRdv;
        }

        public void setDernierRdv(LocalDateTime dernierRdv) {
            this.dernierRdv = dernierRdv;
        }

        public String getDatesAvecRdvDisplay() {
            return datesAvecRdvDisplay;
        }

        public void setDatesAvecRdvDisplay(String datesAvecRdvDisplay) {
            this.datesAvecRdvDisplay = datesAvecRdvDisplay;
        }

        // *** NOUVEAU GETTER/SETTER ***
        public List<LocalDate> getDatesEffectivesAvecRdv() {
            return datesEffectivesAvecRdv;
        }

        public void setDatesEffectivesAvecRdv(List<LocalDate> datesEffectivesAvecRdv) {
            this.datesEffectivesAvecRdv = datesEffectivesAvecRdv;
        }
    }
}