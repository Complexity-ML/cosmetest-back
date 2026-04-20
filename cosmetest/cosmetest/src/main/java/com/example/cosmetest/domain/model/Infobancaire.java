package com.example.cosmetest.domain.model;
import jakarta.persistence.*;

@Entity // Déclare cette classe comme une entité JPA
@Table(name = "infobancaire") // Nom de la table dans la base de données
public class Infobancaire {

	
    @EmbeddedId 
	private InfobancaireId id;

	public Infobancaire() {
	}

	public Infobancaire(InfobancaireId id) {
		this.id = id;
	}

	public InfobancaireId getId() {
		return this.id;
	}

	public void setId(InfobancaireId id) {
		this.id = id;
	}

}
