package com.example.cosmetest.domain.model;
// Generated 15 d�c. 2024, 15:56:41 by Hibernate Tools 6.5.1.Final
import jakarta.persistence.*;

@Entity // Indique que cette classe est une entité JPA
@Table(name = "volbug") // Permet de spécifier le nom de la table
public class Volbug implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_vol")
	private int idVol;

	public Volbug() {
	}

	public Volbug(int idVol) {
		this.idVol = idVol;
	}

	public int getIdVol() {
		return this.idVol;
	}

	public void setIdVol(int idVol) {
		this.idVol = idVol;
	}

}
