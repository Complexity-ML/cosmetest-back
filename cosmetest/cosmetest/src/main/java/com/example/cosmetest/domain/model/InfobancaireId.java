package com.example.cosmetest.domain.model;
import jakarta.persistence.Embeddable;
import java.io.Serializable;


@Embeddable // Déclare cette classe comme une clé composite
public class InfobancaireId implements Serializable {

	private String bic;
	private String iban;
	private Integer idVol;

	public InfobancaireId() {
	}

	public InfobancaireId(String bic, String iban, Integer idVol) {
		this.bic = bic;
		this.iban = iban;
		this.idVol = idVol;
	}

	public String getBic() {
		return this.bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public Integer getIdVol() {
		return this.idVol;
	}

	public void setIdVol(Integer idVol) {
		this.idVol = idVol;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof InfobancaireId))
			return false;
		InfobancaireId castOther = (InfobancaireId) other;

		return ((this.getBic() == castOther.getBic())
				|| (this.getBic() != null && castOther.getBic() != null && this.getBic().equals(castOther.getBic())))
				&& ((this.getIban() == castOther.getIban()) || (this.getIban() != null && castOther.getIban() != null
						&& this.getIban().equals(castOther.getIban())))
				&& ((this.getIdVol() == castOther.getIdVol()) || (this.getIdVol() != null
						&& castOther.getIdVol() != null && this.getIdVol().equals(castOther.getIdVol())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getBic() == null ? 0 : this.getBic().hashCode());
		result = 37 * result + (getIban() == null ? 0 : this.getIban().hashCode());
		result = 37 * result + (getIdVol() == null ? 0 : this.getIdVol().hashCode());
		return result;
	}

}
