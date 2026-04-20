package com.example.cosmetest.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RdvId implements Serializable {

	@Column(name = "ID_ETUDE")
	private Integer idEtude;

	@Column(name = "ID_RDV")
	private Integer idRdv;

	//@Column(name = "sequence")
	//private Integer sequence;

	// Constructeur par défaut
	public RdvId() {
	}

	// Constructeur avec paramètres
	public RdvId(Integer idEtude, Integer idRdv, Integer sequence) {
		this.idEtude = idEtude;
		this.idRdv = idRdv;
		//this.sequence = sequence;
	}

	public RdvId(Integer idEtude, Integer idRdv) {
		this.idEtude = idEtude;
		this.idRdv = idRdv;
	}

	// Getters et Setters
	public Integer getIdEtude() {
		return this.idEtude;
	}

	public void setIdEtude(Integer idEtude) {
		this.idEtude = idEtude;
	}

	public Integer getIdRdv() {
		return this.idRdv;
	}

	public void setIdRdv(Integer idRdv) {
		this.idRdv = idRdv;
	}

	//public Integer getSequence() {
	//	return sequence;
	//}

	//public void setSequence(Integer sequence) {
	//	this.sequence = sequence;
	//}

	// Méthode equals
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof RdvId other))
			return false;
		return Objects.equals(idEtude, other.idEtude) && Objects.equals(idRdv, other.idRdv);
	}

	public int hashCode() {
		return Objects.hash(idEtude, idRdv);
	}

}
