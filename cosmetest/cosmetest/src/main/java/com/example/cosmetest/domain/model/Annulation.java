package com.example.cosmetest.domain.model;
import jakarta.persistence.*;

@Entity // Indique que cette classe est une entité JPA
@Table(name="annulation") // Permet de spécifier le nom de la table
public class Annulation implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_annuler")
	private Integer idAnnuler;
	private int idVol;
	private int idEtude;
	private String dateAnnulation;
	private String commentaire;

	public Annulation() {
	}

	public Annulation(int idVol, int idEtude, String dateAnnulation) {
		this.idVol = idVol;
		this.idEtude = idEtude;
		this.dateAnnulation = dateAnnulation;
	}

	public Annulation(int idVol, int idEtude, String dateAnnulation, String commentaire) {
		this.idVol = idVol;
		this.idEtude = idEtude;
		this.dateAnnulation = dateAnnulation;
		this.commentaire = commentaire;
	}

	public Integer getIdAnnuler() {
		return this.idAnnuler;
	}

	public void setIdAnnuler(Integer idAnnuler) {
		this.idAnnuler = idAnnuler;
	}

	public int getIdVol() {
		return this.idVol;
	}

	public void setIdVol(int idVol) {
		this.idVol = idVol;
	}

	public int getIdEtude() {
		return this.idEtude;
	}

	public void setIdEtude(int idEtude) {
		this.idEtude = idEtude;
	}

	public String getDateAnnulation() {
		return this.dateAnnulation;
	}

	public void setDateAnnulation(String dateAnnulation) {
		this.dateAnnulation = dateAnnulation;
	}

	public String getCommentaire() {
		return this.commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
