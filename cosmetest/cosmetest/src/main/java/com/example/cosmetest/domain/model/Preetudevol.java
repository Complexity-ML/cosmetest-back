package com.example.cosmetest.domain.model;
import jakarta.persistence.*;

@Entity
@Table(name = "preetudevol")
public class Preetudevol{

    @EmbeddedId 
	private PreetudevolId id;

	public Preetudevol() {
	}

	public Preetudevol(PreetudevolId id) {
		this.id = id;
	}

	public PreetudevolId getId() {
		return this.id;
	}

	public void setId(PreetudevolId id) {
		this.id = id;
	}

}
