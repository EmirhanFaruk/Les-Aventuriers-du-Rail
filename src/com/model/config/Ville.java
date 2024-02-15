package com.model.config;

public class Ville extends Case{
	private String nom;
	private boolean gare;
	
	public Ville(int x, int y, String nom) {
		super(x, y);
		this.nom = nom;
	}
	
	public boolean getGare() {
    	return gare;
    }
	
	public void setGare(boolean estUneGare) {
    	this.gare = estUneGare;
    }

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
