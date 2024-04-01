package com.model;
import com.model.config.Plateau;
import com.model.config.Rail;
import com.model.config.Rail.Content;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;
import com.view.graphics.VilleGraphics;

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
	private Round round;


	public Player ( String playerCouleur , String name , int niveau, Round round){
		this.playerCouleur = playerCouleur ;
		this.name = name;
		this.niveau = niveau;
		this.score = 0 ;
		this.missionComplete = 0 ;
		this.nbrWagon = 15 ;
		this.nbrGare = 3 ;
		this.round = round;

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
		// Premiere boucle qui enlève juste la couleur color
		while(i < this.trainList.size() && 0 < carteAEnlever) {
			if ( trainList.get(i) == color ){
				this.trainList.remove(i);
				carteAEnlever--;
			} else {
				i++ ;
			}
    	}
		// Deuxième boucle qui enlève les cartes de couleur loc pour complèter les carte à enlèver
		// si les carte de la couleur color est insuffissant
		int j = 0 ;
		while ( j < this.trainList.size() && 0 < carteAEnlever ){
			if ( trainList.get(j) == Couleur.LOC) {
				this.trainList.remove(j);
				carteAEnlever--;
			} else{
				j++ ;
			}
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

	/**
	 * Une fonction qui renvoie true si le joueur peut changer la ville en gare
	 * @param ville Ville
	 * @param couleurCarteChoisit une couleur de carte
	 */
	public void transformerEnGare( Ville ville , Couleur couleurCarteChoisit ){
		if ( assezDeGare() ){
			int nbrCarteRetirer = nombreDeCartePourPoserUneGare() ;
			if (  nbrCarteRetirer <= peutChangerAvecCetteCarte( couleurCarteChoisit ) && ville.getIsOccuped() == null ) {
					retirerCartePourGare(couleurCarteChoisit , nbrCarteRetirer );
					ville.setIsOccuped( this );
					System.out.println("LE SUIS LE NOUVEAU MAIRE DE LA VILLE ");
			} else {
				System.out.println("JE N'AI PAS ASSEZ DE VOTE wuwuwuwu");
			}
		}

	}

	/**
	 * Une fonction qui donne le bon nombre de cartes à échanger contre des gares
	 * @return le nombre de cartes à échanger
	 */
	public int nombreDeCartePourPoserUneGare(){
		if ( nbrGare == 3 )  return 1 ;
		if ( nbrGare == 2 ) return 2 ;
		if ( nbrGare == 1 ) return 3 ;
		return 0 ;
	}

	/**
	 * Une fonction qui compte le nombre de cartes de la couleur que le joueur a choisi pour changer les cartes en gare
	 * @param couleurCarteChoisit couleur choisit
	 * @return le nombre de cartes de la couleur
	 */
	public int peutChangerAvecCetteCarte ( Couleur couleurCarteChoisit ){
		int count = 0;
		for ( Couleur c : trainList ) {
			if ( couleurCarteChoisit == c ) count++ ;
		}
		return count;
	}

	/**
	 * Une fonction qui enlève une gare et les carte necessaire pour faire l'échange
	 * @param couleur Couleur de la carte
	 * @param carteAEnlever le nombre de cartes à retirer
	 */
	private void retirerCartePourGare( Couleur couleur , int carteAEnlever) {
		setNbrGare( getNbrGare() -1 );
		int restant = carteAEnlever ;
		int i = 0;
		while(i < this.trainList.size() && 0 < restant ) {
			if ( trainList.get(i) == couleur ) {
				//DEBUG : System.err.println("La couleur de la carte a enlever est : " + couleur );
				this.trainList.remove(i);
				restant-- ;
			} else {
				i++ ;
			}
		}
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



	public int scoreFinal(){
		return this.score + nbrGare*4 ;
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
