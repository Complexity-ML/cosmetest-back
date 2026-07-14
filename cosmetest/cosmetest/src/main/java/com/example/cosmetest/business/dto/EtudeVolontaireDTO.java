package com.example.cosmetest.business.dto;

public class EtudeVolontaireDTO {
    private Long id;
    private int idEtude;
    private int idGroupe;
    private int idVolontaire;
    private int iv;
    private int numsujet;
    private int paye;
    private String statut;

    public EtudeVolontaireDTO() {}
    public EtudeVolontaireDTO(int idEtude, int idGroupe, int idVolontaire, int iv, int numsujet, int paye, String statut) {
        this(null, idEtude, idGroupe, idVolontaire, iv, numsujet, paye, statut);
    }
    public EtudeVolontaireDTO(Long id, int idEtude, int idGroupe, int idVolontaire, int iv, int numsujet, int paye, String statut) {
        this.id=id; this.idEtude=idEtude; this.idGroupe=idGroupe; this.idVolontaire=idVolontaire;
        this.iv=iv; this.numsujet=numsujet; this.paye=paye; this.statut=statut;
    }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public int getIdEtude(){return idEtude;} public void setIdEtude(int v){idEtude=v;}
    public int getIdGroupe(){return idGroupe;} public void setIdGroupe(int v){idGroupe=v;}
    public int getIdVolontaire(){return idVolontaire;} public void setIdVolontaire(int v){idVolontaire=v;}
    public int getIv(){return iv;} public void setIv(int v){iv=v;}
    public int getNumsujet(){return numsujet;} public void setNumsujet(int v){numsujet=v;}
    public int getPaye(){return paye;} public void setPaye(int v){paye=v;}
    public String getStatut(){return statut;} public void setStatut(String v){statut=v;}
}
