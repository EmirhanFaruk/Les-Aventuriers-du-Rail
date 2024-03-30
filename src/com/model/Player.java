package com.model;
import com.model.config.Plateau;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;

import java.awt.*;
import java.util.ArrayList;

public class Player {
	private String name;
    private int score;

	private final String playerCouleur ;
	private int nbrWagon ;
	private int nbrGare ;//Le nombre de gare que le joueur peut poser

	private int missionComplete ;
	private int niveau; //Si niveau = 0, alors c'est un joueur, si niveau = 1 = bot facile, si niveau = 2 bot moyen, si niveau = 3 bot difficile
    private ArrayList<CarteDestination> destinationsList = new ArrayList<>();//La liste de carte mission du jouer
    private ArrayList<CarteWagon.Couleur> trainList = new ArrayList<>(); //La liste de carte wagon du joueur
	public Couleur couleur;


	public Player ( String playerCouleur , String name , int niveau, CarteManager carteManager ){
		this.playerCouleur = playerCouleur ;
		this.name = name;
		this.niveau = niveau;
		this.score = 0 ;
		this.missionComplete = 0 ;
		this.nbrWagon = 15 ;
		this.nbrGare = 3 ;
	}
	
	
	private int carteDuJoueur(Route r){
		int count = 0;

		for(int i = 0; i < this.trainList.size(); i++) {
			if(compatibleColor(r, this.trainList.get(i)))count++;
		}

		return count;
	}
	
	public void piocheCarteInvisible() {
		CarteManager cm = new CarteManager();
		this.trainList.add(cm.drawCard());
	}
	
	public void piocheCarteVisible(CarteWagon.Couleur carte) {
		this.trainList.add(carte);
	}

    private void retirerLesCartes(Couleur color, int carteAEnlever) {
    	int i = 0; 
    	setNbrWagon(this.nbrWagon - carteAEnlever);
		while(i < this.trainList.size() && 0 < carteAEnlever) {
			this.trainList.remove(i);
			carteAEnlever--;
			i++;
    	}
    }

    public boolean mettreRoute(Route r) {
    	if(r != null) {
    		if (r.getLongueur() <= this.carteDuJoueur(r) && r.getProprietaire() == null) {
                this.retirerLesCartes(r.traducteurCouleur(), r.getLongueur());
                r.setProprietaire(this); // Met à jour le propriétaire de la route.
                //DEBUG : System.out.println("nombre de wagon : "  + this.trainList.size());
                return true;
            }
    	}
    	
    	return false;
    }

    //ATTENTION ! Si c'est true, passer le prochain tour du joueur.
    public boolean changerGareEnVille(int x, int y, Plateau p){
    	if(p.positionValide(x, y)){
    		if(p.estUneCaseVille(x, y) && !p.estUneCaseGare(x, y)) {
    			((Ville) p.getPlateau()[x][y]).setIsOccuped(this);
    			return true;
    		}
    	}
		return false;
	}

	public boolean compatibleColor(Route r, Couleur couleur){
		Rail.Content color = r.getCouleur();
		Rail.Content color2 = r.getRailsRoute().get(0).getInitialContent();
		
		if(couleur == Couleur.LOC){
			return true;
		}else if(color2 == Rail.Content.JOKERETOILEE || color2 == Rail.Content.JOKER) {
			return true;
		}

		return color.ordinal() == couleur.ordinal();
	}


	public void piocher(CarteManager cm)
	{
		trainList.add(cm.drawCard());
	}


	/**
	 * Une fonction qui verifie si le joueur a assez de gare
	 * @return si nbrGare est superieur a 0
	 */
	public boolean assezDeGare(){
		return this.nbrGare > 0 ;
	}


	public String getPlayerCouleur() {
		return playerCouleur;
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

    public int getScore() {
        return this.score;
    }

    public int getNbrWagon() {
        return nbrWagon;
    }

    public void setNbrWagon(int nbrWagon) {
        this.nbrWagon = nbrWagon;
    }

    public int getNbrGare() {
        return nbrGare;
    }

    public void setNbrGare(int nbrGare) {
        this.nbrGare = nbrGare;
    }

	public int getMissionComplete() {
		return missionComplete;
	}

	public void setMissionComplete(int missionComplete) {
		this.missionComplete = missionComplete;
	}

	public ArrayList<CarteDestination> getDestinationsList() {
        return destinationsList;
    }

    public ArrayList<CarteWagon.Couleur> getTrainCard() {
        return trainList;
    }

    public ArrayList<Couleur> getTrainList() {
        return trainList;
    }

}
