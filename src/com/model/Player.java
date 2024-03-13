package com.model;
import com.model.config.Plateau;
import com.model.config.Rail;
import com.model.config.Rail.Content;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;

import java.util.ArrayList;

public class Player {
	private String name;
    private int score;
	private int niveau; //Si niveau = 0, alors c'est un joueur, si niveau = 1 = bot facile, si niveau = 2 bot moyen, si niveau = 3 bot difficile
    private ArrayList<CarteDestination> destinationsList = new ArrayList<>();//La liste de carte mission du jouer
    private ArrayList<CarteWagon.Couleur> trainList = new ArrayList<>(); //La liste de carte wagon du joueur



    public ArrayList<CarteDestination> getDestinationsList() {
        return destinationsList;
    }

    public ArrayList<CarteWagon.Couleur> getTrainCard() {
        return trainList;
    }

	public ArrayList<Couleur> getTrainList() {
		return trainList;
	}

	public int getScore() {
    	return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNiveau() {
		return niveau;
	}

	public void setDestinationsList(ArrayList<CarteDestination> destinationsList) {
        this.destinationsList = destinationsList;
    }

    public void setTrainCard(ArrayList<CarteWagon.Couleur> trainList) {
        this.trainList = trainList;
    }
    
    private int carteDuJoueur(Rail.Content color){
    	int count = 0;

    	for(int i = 0; i < this.trainList.size(); i++) {
    		if(compatibleColor(color,this.trainList.get(i)))count++;

    	}
    	
    	return count;
    }

    private void retirerLesCartes(Couleur color, int longeur) {
    	int i = 0, count = longeur;
    	
    	while(count != 0) {    		
    		if(this.trainList.get(i) == color) {
    			this.trainList.remove(i);
    			count--;
    		}else {
    			i++;
    		}
    	}   	
    }

    public boolean mettreRoute(Route r){
		if (r.getLongueur() <= this.carteDuJoueur(r.getCouleur()) && r.getProprietaire() == null){
    		this.retirerLesCartes(r.traducteurCouleur(), r.getLongueur());
    		r = new Route(r.getVille1(), r.getVille2(), r.getLongueur(), r.getCouleur(), this, r.getRailsRoute());
    		return true;
    	}
		return false;
    }
    
    //ATTENTION ! Si c'est true, passer le prochain tour du joueur.
    public boolean changerGareEnVille(int x, int y, Plateau p){
    	if(p.positionValide(x, y)){
    		if(p.estUneCaseVille(x, y) && !p.estUneCaseGare(x, y)) {
    			((Ville) p.getPlateau()[x][y]).getIsOccuped();
    			return true;
    		}
    	}
		return false;
    }

	public boolean compatibleColor(Content content, Couleur couleur){

		if(couleur == Couleur.LOC){
			return true;
		}

		return content.ordinal() == couleur.ordinal();

	}







}
