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
    private ArrayList<CarteDestination> destinationsList = new ArrayList<>();
    private ArrayList<CarteWagon.Couleur> trainList = new ArrayList<>();


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	//TODO, Faire une fonction de Comparaison entre /!\ CONTENT /!\ et /!\COULEUR/!\
    private void retirerLesCartes(Content color, int longeur) {
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
    	String a = "Rouge";
		String b = "JSP";

		System.out.println(a.equals(b));

		if (r.getLongueur() <= this.carteDuJoueur(r.getCouleur()) && r.getProprietaire() == null){
    		this.retirerLesCartes(r.getCouleur(), r.getLongueur());
    		r = new Route(r.getVille1(), r.getVille2(), r.getLongueur(), r.getCouleur());
			r.setProprietaire(this);
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

	LOC, BLEU, VIOLET, MARRON, BLANC, VERT, JAUNE, NOIRE, ROUGE
	BLEU,VIOLET , MARRON , NOIR , VERT , JAUNE , ROUGE , BLANC , JOKER , JOKERETOILEE
	public CarteWagon.Couleur compatibleColor(Content content){

		switch (content){

			case BLEU :
				return Couleur.BLEU;



		}



	}





}
