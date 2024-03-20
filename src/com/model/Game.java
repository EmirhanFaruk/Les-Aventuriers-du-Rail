package com.model;

import com.model.config.Plateau;
import com.model.config.Route;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;
import com.view.GameFrame;
import com.view.GameScreen;
import com.model.config.Ville;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private Plateau plateau;
    private ArrayList<Player> listPlayer;
    private Player joueurCourant;
    private Ville[] villes;
    private ArrayList<Route> routes;
    private CarteManager cm;
    private Round round;

    public Game() {}

    public void makeGame(String nomMap)
    {
        this.cm = new CarteManager();
        this.plateau = Plateau.makePlateau(nomMap, this);
        this.round = new Round();
        this.listPlayer = new ArrayList<Player>();
        this.listPlayer.add(new Player("Salim"));
        this.listPlayer.add(new Player("Alexis"));
        this.listPlayer.add(new Player("Emirhan"));
        this.listPlayer.add(new Player("Mina"));
    }

    /*
    getteurs et setteurs
     */
    public Plateau getPlateau() {
        return plateau;
    }

    public Ville[] getVilles() {
        return villes;
    }

    public void setVilles(Ville[] villes) { this.villes = villes; }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) { this.routes = routes; }

    public ArrayList<Player> getListPlayer() {
        return listPlayer;
    }

    public void setListPlayer(ArrayList<Player> listPlayer) {
        this.listPlayer = listPlayer;
    }

    private void initBoard(){
        //Fonction qui initialise le jeu


        //Initialisation des cartes wagon sur le board
        for(int i = 0; i< cm.getTrainCards().length; i++){
            cm.getTrainCards()[i] = cm.drawCard();
        }

        //Initialisation des cartes destination du premier tour du board que le joueur choisit
        for(int y = 0; y < cm.getDestinationsCards().length;y++){
            cm.getDestinationsCards()[y] = cm.getDestination(this);
        }

    }


    /**
     * Verifie s'il y a un joueur qui a moins de 3 wagons
     * @return true si nbrWagon est inferieur a 3
     */
    public boolean endGame( ){
        for (Player p : listPlayer){
            if ( p.getNbrWagon() <=2 ){
                return true ;
            }
        }
        return false ;
    }


    public void updateGame(double deltaTime, GameFrame gameFrame)
    {	
    	Player p = new Player("Silver");
    	CarteWagon.Couleur c = Couleur.BLEU;
    	CarteWagon.Couleur c1 = Couleur.MARRON;
    	CarteWagon.Couleur c2 = Couleur.VERT;
    	CarteWagon.Couleur c3 = Couleur.ROUGE;
    	CarteWagon.Couleur c4 = Couleur.NOIRE;
    	p.getTrainCard().add(c);
    	p.getTrainCard().add(c1);
    	p.getTrainCard().add(c2);
    	p.getTrainCard().add(c3);
    	p.getTrainCard().add(c4);
    	//gameFrame.getGameScreen().setPlayerCourant(p);
        /*if(round.roundFinished()){
        	
            round.round(this,cm);
            
        }*/
    }

	public Player getJoueurCourant() {
		return joueurCourant;
	}

	public void setJoueurCourant(Player joueurCourant) {
		this.joueurCourant = joueurCourant;
	}
}
