package com.example.cosmetest.domain.model;
// Generated 15 d�c. 2024, 15:56:41 by Hibernate Tools 6.5.1.Final

import jakarta.persistence.*;

@Entity // Indique que cette classe est une entité JPA
@Table(name = "identifiant") // Permet de spécifier le nom de la table
public class Identifiant implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_IDENTIFIANT")
	private Integer idIdentifiant;
	private String identifiant;
	private String mdpIdentifiant;
	private String mailIdentifiant;
	private String description;
	private String role;

	public Identifiant() {
	}

	public Identifiant(String identifiant, String mdpIdentifiant, String mailIdentifiant, String role, String description) {
		this.identifiant = identifiant;
		this.mdpIdentifiant = mdpIdentifiant;
		this.mailIdentifiant = mailIdentifiant;
		this.description = description;
		this.role = role;
	}

	public Integer getIdIdentifiant() {
		return this.idIdentifiant;
	}

	public void setIdIdentifiant(Integer idIdentifiant) {
		this.idIdentifiant = idIdentifiant;
	}

	public String getIdentifiant() {
		return this.identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public String getMdpIdentifiant() {
		return this.mdpIdentifiant;
	}

	public void setMdpIdentifiant(String mdpIdentifiant) {
		this.mdpIdentifiant = mdpIdentifiant;
	}

	public String getMailIdentifiant() {
		return this.mailIdentifiant;
	}

	public void setMailIdentifiant(String mailIdentifiant) {
		this.mailIdentifiant = mailIdentifiant;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
