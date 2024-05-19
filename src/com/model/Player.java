package com.model;
import com.model.ai.LongestFinder;
import com.model.config.Plateau;
import com.model.config.Rail;
import com.model.config.Route;
import com.model.config.Ville;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteManager;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;

import javax.swing.*;
import java.util.ArrayList;

public class Player {
	private String name;
    private int score;
    private boolean firstTurnOver = false;
    private boolean canPlay;
	private final String playerCouleur ;
	private int nbrWagon ;
	private int nbrGare ;//Le nombre de gare que le joueur peut poser
	private ArrayList<Route> playerRoutes = new ArrayList<>();
	private int nbMissionComplete ;
	private int niveau; //Si niveau = 0, alors c'est un joueur, si niveau = 1 = bot facile, si niveau = 2 bot moyen, si niveau = 3 bot difficile
    private ArrayList<CarteDestination> destinationsList = new ArrayList<>();//La liste de carte mission du jouer
    private ArrayList<CarteWagon.Couleur> trainList = new ArrayList<>(); //La liste de carte wagon du joueur
	public Couleur couleur;
	private Game game;


	/**
	 * Constructeur de Player
	 * @param playerCouleur la couleur du joueur
	 * @param name le nom du joueur
	 * @param niveau le type du joueur (personne ou bot de différent niveau)
	 * @param game le jeu
	 */
	public Player ( String playerCouleur , String name , int niveau, Game game){
		this.playerCouleur = playerCouleur ;
		this.name = name;
		this.niveau = niveau;
		this.game = game;
		this.score = 0 ;
		this.nbMissionComplete = 0 ;
		this.nbrWagon = initNbragon() ;
		this.nbrGare = 4 ;
	}


	private int carteDuJoueur(Route r){
		int count = 0;

		for(int i = 0; i < this.trainList.size(); i++) {
			if(compatibleColor(r, this.trainList.get(i)))count++;
		}

		return count;
	}

	/**
	 * Une fonction qui initialise le nombre de wagons en fonction de la taille de la map
	 * @return le nombre de wagons
	 */
	public int initNbragon (){
		String map  = game.getGameFrame().getMain().getMap() ;
		if ( map.equals("LongMap") ) {
			return 30 ;
		} else if ( map.equals("NormalMap") ) {
			return 20 ;
		} else if ( map.equals("QuickMap") ) {
			return 15 ;
		}
		//DEBUG : System.out.println("Pas le bon nom de map");
		return 0 ;
	}
	
	public boolean piocheCarteDestination(CarteManager cm, int i) {
		//vérifie s'il ne possède pas déjà la carte destination
		if(cm.getDestinationsCards()[i] != null) {
			//donne la carte destination et mets à null pour remplacer
			this.destinationsList.add(cm.getDestinationsCards()[i]);
			cm.getDestinationsCards()[i] = null;
			if( !this.destinationsList.isEmpty() )this.canPlay = true;
			if(game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound( "INGAME" ,"carte-dest.wav" );
			return true;
		}else {
			if (game.getGameFrame().getSound().getclick() ) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
			JOptionPane.showMessageDialog(new JFrame(),"Vous avez déjà pioché cette carte !","Instructions",JOptionPane.WARNING_MESSAGE);
		}
		
		return false;
	}

	public void piocheCarteInvisible() {
		CarteManager cm = game.getCarteManager();

		//Si le nombre d'action est égal a 2 alors on pioche une fois et on enleve le nombre d'action -1
		if(game.getRound().getAction() >1){
			insertCarte(cm.drawCard());
			game.getRound().setAction(game.getRound().getAction() - 1);

		}else{
			//Si on a plus que une action alors on pioche puis on fini le tour
			insertCarte(cm.drawCard());
			game.getRound().endRound(game);
		}
		if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" ,"carte-wagon.wav");
	}

	public boolean piocheCarteVisible(CarteWagon.Couleur carte) {

		//On regarde si le joueur a 2 actions ou non
		if(game.getRound().getAction() > 1){

			//Si oui alors on regarde si c'est une carte locomotive ou non
			if(carte == Couleur.LOC){
				//Si c'est une locomotive on fini le tour du joueur
				if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" ,"carte-wagon.wav");
				insertCarte(carte);
				game.getRound().endRound(game);
				return true;
			}
			else{
				//Sinon on enleve une action au joueur
				if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" ,"carte-wagon.wav");
				insertCarte(carte);
				game.getRound().setAction(game.getRound().getAction() - 1);
				return true;

			}

		}else{
			//On verifie que c'est une carte locomotive ou non
			if(carte == Couleur.LOC){
				//Si c'est le cas alors on dit qu'on ne peut pas
				return false;
			}
			else{
				//Sinon on pioche la carte et on passe au joueur suivant
				if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" ,"carte-wagon.wav" );
				insertCarte(carte);
				game.getRound().endRound(game);
				return true;
			}
		}
	}



	private void insertCarte(Couleur carte)
	{
		int i = findCarteIndex(carte);

		this.trainList.add(i, carte);
	}


	/**
	 * Trouver le bon index pour inserer la carte obtenu.
	 * @param carte carte a inserer
	 * @return le bon index
	 */
	private int findCarteIndex(Couleur carte)
	{
		int res = this.trainList.size();

		if (carte != Couleur.LOC)
		{
			int i = 0;
			while (i < this.trainList.size())
			{
				if (carte.ordinal() > this.trainList.get(i).ordinal())
				{
					i++;
				}
				else
				{
					break;
				}
			}

			res = i;
		}

		return res;
	}


	public void retirerCarteNonLoc(Couleur color ,int carteAEnlever){
		//Fonction qui enleve les cartes si c'est pas multicolor

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
	

	public void retirerCarteLoc(int carteAEnlever){
		//Fonction qui enleve les cartes si c'est une route multicolor
		int i = 0;
		setNbrWagon(this.nbrWagon - carteAEnlever);
		// Premiere boucle qui enlève juste la couleur color
		while(i < this.trainList.size() && 0 < carteAEnlever) {
			this.trainList.remove(i);
			carteAEnlever--;

		}

	}
	
	//Méthode pour retirer la route d'un autre joueur
	public boolean retirerRouteAutreJoueur(Rail r, Player p) {
		
		//Si le joueur n'est pas null
		if(this != null) {			
			for(int i = 0; i < this.getPlayerRoutes().size(); i++) {	
				//Si la Route correspond dans l'inventaire du joueur
				if(r.getSaRoute() == this.getPlayerRoutes().get(i)) {
					
					//Debug : System.out.print("SA PASSE");
					if (game.getGameFrame().getSound().getclick())
						game.getGameFrame().getSound().playSound("INGAME", "tactical-nuke.wav");
					p.retirerCarteNuke(); //Retire la carte nuke de son inventaire
					r.getSaRoute().enleverProprio(); //Enlève le proprio de la route et des rails
					this.getPlayerRoutes().remove(i); //Enlève la route de l'inventaire du joueur
					
					//Debug : System.out.println(r.getSaRoute().getProprietaire());
					
					return true;
				}				
			}			
		}
		
		return false;
	}
	
	//Méthode pour retirer la gare d'un autre joueur
	public void retirerGareAutrePlayer(Ville ville, Player player) {
		ville.setIsOccuped(null); //Enlève le proprio de la ville
		player.retirerCarteNuke(); //Retire la carte nuke de l'inventaire
		this.nbrGare += 1;  //Rajoute une Gare dispo
	}


	public boolean checkACarteNuke() {
		for(int i = 0; i < this.trainList.size(); i++) {
			if(this.trainList.get(i) == Couleur.NUKE) {
				return true;
			}
		}
		
		return false;	
	}
	
	//Méthode pour retirer la carte nuke de l'inventaire d'un joueur
	private void retirerCarteNuke() {
		for(int i = 0; i < this.trainList.size(); i++) {
			if(this.trainList.get(i) == Couleur.NUKE) {
				this.trainList.remove(i);
				return;
			}
		}
	}


    public void retirerLesCartes(Couleur color, int carteAEnlever) {

		//Si la route n'est pas une route multicolor
		if(color != Couleur.LOC){

			retirerCarteNonLoc(color,carteAEnlever);

		}else{
			//Sinon on enleve avec une autre fonction
			retirerCarteLoc(carteAEnlever);
		}
    }

    public boolean mettreRoute(Route r) {
    	if(r != null) {
    		if (r.getLongueur() <= this.carteDuJoueur(r) && r.getProprietaire() == null  && r.getLongueur() <= this.nbrWagon ) {
				if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" ,"mettreRoute.wav");
                this.retirerLesCartes(r.traducteurCouleur(), r.getLongueur());
                r.setProprietaire(this); // Met à jour le propriétaire de la route.
                //DEBUG : System.out.println("nombre de wagon : "  + this.trainList.size());
				this.score += r.getNombrePoint();
				playerRoutes.add(r);
				score += aCompleterUneMission(); // on vérifie si on a completer une mission et on rajoute les points le cas échéant
				return true;
            }
    	}
    	
    	return false;
    }

	private int aCompleterUneMission() {
		if( destinationsList.isEmpty() ) return 0;
		int cumulPoints=0;
		for(CarteDestination c : destinationsList){
			if(c.getComplete()) continue; // éviter les cartes dèja comptlétées.

			Ville ville1 = c.getPremiereVille();
			Ville ville2 = c.getDeuxiemeVille();
			if (LongestFinder.wayExists(ville1, ville2, this)){
				cumulPoints += c.getNombrePoints();  // si il a completer une ou plusieurs missions on cumule les points
				c.setComplete();
				this.setMissionComplete( this.nbMissionComplete + 1 );
			} 
		}
		return cumulPoints;
	}



	/**
	 * Une fonction qui renvoie true si le joueur peut changer la ville en gare
	 * @param ville Ville
	 * @param couleurCarteChoisit une couleur de carte
	 */
	public boolean transformerEnGare( Ville ville , Couleur couleurCarteChoisit ){

		if ( assezDeGare() && ville.getIsOccuped() == null){
			int nbrCarteRetirer = nombreDeCartePourPoserUneGare() ;


			//Si c'est un joueur alors on fait la demande, sinon pour les bots on fait directement le procédé
			if(this.niveau == 0){
				if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
				int choixUtilisateur = JOptionPane.showConfirmDialog(
						game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
						"Êtes-vous sûr de vouloir poser votre gare ici ?", "CONFIRMATION", JOptionPane.YES_NO_OPTION);

				if (choixUtilisateur == JOptionPane.YES_OPTION) {
					if (nbrCarteRetirer <= peutChangerAvecCetteCarte(couleurCarteChoisit) && ville.getIsOccuped() == null ) {
						retirerCartePourGare(couleurCarteChoisit, nbrCarteRetirer);
						ville.setIsOccuped(this);
						aCompleterUneMission();
						// DEBUG : System.out.println("LE SUIS LE NOUVEAU MAIRE DE LA VILLE ");
						return true;

					} else if ( ville.getIsOccuped() != null ) {
						if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
						JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
								"Cette ville possède déja un propriétaire. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
						return false;


					} else {
						if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
						JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
								"Vous n'avez pas assez de carte pour pour posséder cette ville. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
						// DEBUG : System.out.println("JE N'AI PAS ASSEZ DE VOTE wuwuwuwu");

						return false;

					}

				}

			} else {

				retirerCartePourGare(couleurCarteChoisit, nbrCarteRetirer);
				ville.setIsOccuped(this);
				aCompleterUneMission();
				return true;

			}

		} else {
			if ( game.getGameFrame().getSound().getclick()) game.getGameFrame().getSound().playSound("INGAME" , "popUp.wav");
			JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
					"Vous n'avez plus assez de gare pour pour posséder cette ville. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
			return false;
		}

		return false;
	}

	/**
	 * Une fonction qui donne le bon nombre de cartes à échanger contre des gares
	 * @return le nombre de cartes à échanger
	 */
	public int nombreDeCartePourPoserUneGare(){
		if ( nbrGare == 4 ) return 1 ;
		if ( nbrGare == 3 )  return 2 ;
		if ( nbrGare == 2 ) return 3 ;
		if ( nbrGare == 1 ) return 4 ;
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
		insertCarte(cm.drawCard());
	}

	/**
	 * Une fonction qui renvoie le score final avec le nombre de gares restant
	 * @return le score final
	 */
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
	
	public ArrayList<Route> getPlayerRoutes(){
		return this.playerRoutes;
	}


	/* getters et setters */
	public String getPlayerCouleur() {
		return playerCouleur;
	}

	public String getName() {
		return name;
	}

	public int getNiveau() {
		return niveau;
	}

	public Game getGame() {
		return game;
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
		return nbMissionComplete;
	}

	public void setMissionComplete(int missionComplete) {
		this.nbMissionComplete = missionComplete;
	}

	public ArrayList<CarteDestination> getDestinationsList() {
        return destinationsList;
    }
    public ArrayList<Couleur> getTrainList() {
        return trainList;
    }

	public boolean getCanPlay() {
		return canPlay;
	}

	public boolean getFirstTurnOver() {
		return firstTurnOver;
	}

	public void setFirstTurnOver(boolean firstTurnOver) {
		this.firstTurnOver = firstTurnOver;
	}


}
