package com.model;
import com.model.config.Plateau;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;

import java.util.ArrayList;

public class Player {
	private String name;
    private int score;
    private ArrayList<CarteDestination> destinationsList = new ArrayList<>();
    private ArrayList<CarteWagon.Couleur> trainList = new ArrayList<>();
    private CarteWagon carteDestination = new CarteWagon();


    public ArrayList<CarteDestination> getDestinationsList() {
        return destinationsList;
    }

    public ArrayList<CarteWagon.Couleur> getTrainCard() {
        return trainList;
    }
    
    public int getScore() {
    	return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDestinationsList(ArrayList<CarteDestination> destinationsList) {
        this.destinationsList = destinationsList;
    }

    public void setTrainCard(ArrayList<CarteWagon.Couleur> trainList) {
        this.trainList = trainList;
    }
    
    private int carteDuJoueur(Couleur color){
    	int count = 0;

    	for(int i = 0; i < this.trainList.size(); i++) {
    		if(this.trainList.get(i) == color)count++;
    		if(this.trainList.get(i) == Couleur.LOC)count++;
    	}
    	
    	return count;
    }
    
    private void retirerLesCartes(Couleur color, int longeur) {
    	int i = 0, count = longeur;
    	
    	while(count == 0) {    		
    		if(this.trainList.get(i) == color) {
    			this.trainList.remove(i);
    			count--;
    		}else {
    			i++;
    		}
    	}   	
    }

    public boolean mettreRoute(Route r){
    	if(r.getLongueur() <= this.carteDuJoueur(r.getCouleur()) && r.getProprietaire() == null){
    		this.retirerLesCartes(r.getCouleur(), r.getLongueur());
    		r = new Route(r.getVille1(), r.getVille2(), r.getLongueur(), r.getCouleur(), this);
    		return true;
    	}
		return false;
    }
    
    //ATTENTION ! Si c'est true, passer le prochain tour du joueur.
    public boolean changerGareEnVille(int x, int y, Plateau p){
    	if(p.positionValide(x, y)){
    		if(p.estUneCaseVille(x, y) && !p.estUneCaseGare(x, y)) {
    			((Ville) p.getPlateau()[x][y]).setGare(true);
    			return true;
    		}
    	}
		return false;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
