package com.example.cosmetest.domain.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "etude_volontaire")
public class EtudeVolontaire {

    @EmbeddedId
    private EtudeVolontaireId id = new EtudeVolontaireId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idEtude")
    @JoinColumn(name = "ID_ETUDE", referencedColumnName = "ID_ETUDE")
    private Etude etude;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idVolontaire")
    @JoinColumn(name = "ID_VOLONTAIRE", referencedColumnName = "ID_VOL")
    private Volontaire volontaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GROUPE", referencedColumnName = "ID_GROUPE", insertable = false, updatable = false)
    private Groupe groupe;

	public EtudeVolontaire() {
	}

	public EtudeVolontaire(EtudeVolontaireId id) {
		this.id = id;
	}

    public EtudeVolontaire(Etude etude, Groupe groupe, Volontaire volontaire) {
        this.id = new EtudeVolontaireId(
            etude != null ? etude.getIdEtude() : null,
            groupe != null ? groupe.getIdGroupe() : null,
            volontaire != null ? volontaire.getIdVol() : null,
            null,
            null,
            null,
            null);
        this.etude = etude;
        this.groupe = groupe;
        this.volontaire = volontaire;
    }

	public EtudeVolontaireId getId() {
		return this.id;
	}

	public void setId(EtudeVolontaireId id) {
		this.id = id;
	}

	public Etude getEtude() {
		return etude;
	}

    public void setEtude(Etude etude) {
        this.etude = etude;
        if (etude != null) {
            if (this.id == null) {
                this.id = new EtudeVolontaireId();
            }
            this.id.setIdEtude(etude.getIdEtude());
        }
    }

	public Volontaire getVolontaire() {
		return volontaire;
	}

    public void setVolontaire(Volontaire volontaire) {
        this.volontaire = volontaire;
        if (volontaire != null) {
            if (this.id == null) {
                this.id = new EtudeVolontaireId();
            }
            this.id.setIdVolontaire(volontaire.getIdVol());
        }
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
        if (groupe != null) {
            if (this.id == null) {
                this.id = new EtudeVolontaireId();
            }
            this.id.setIdGroupe(groupe.getIdGroupe());
        }
    }

    public Integer getIv() {
        return id != null ? id.getIv() : null;
    }

    public void setIv(Integer iv) {
        ensureId();
        this.id.setIv(iv);
    }

    public Integer getNumSujet() {
        return id != null ? id.getNumsujet() : null;
    }

    public void setNumSujet(Integer numSujet) {
        ensureId();
        this.id.setNumsujet(numSujet);
    }

    public Integer getPaye() {
        return id != null ? id.getPaye() : null;
    }

    public void setPaye(Integer paye) {
        ensureId();
        this.id.setPaye(paye);
    }

    public String getStatut() {
        return id != null ? id.getStatut() : null;
    }

    public void setStatut(String statut) {
        ensureId();
        this.id.setStatut(statut);
    }

    private void ensureId() {
        if (this.id == null) {
            this.id = new EtudeVolontaireId();
        }
    }

}
