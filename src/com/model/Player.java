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

/**
 * Classe représentant un joueur dans le jeu.
 */
public class Player {
    private String name;
    private int score;
    private boolean firstTurnOver = false;
    private boolean canPlay;
    private final String playerCouleur;
    private int nbrWagon;
    private int nbrGare; // Le nombre de gare que le joueur peut poser
    private ArrayList<Route> playerRoutes = new ArrayList<>();
    private int nbMissionComplete;
    private int niveau; // 0: joueur, 1: bot facile, 2: bot moyen, 3: bot difficile
    private ArrayList<CarteDestination> destinationsList = new ArrayList<>(); // Liste des cartes mission du joueur
    private ArrayList<CarteWagon.Couleur> trainList = new ArrayList<>(); // Liste des cartes wagon du joueur
    public Couleur couleur;
    private Game game;

    /**
     * Constructeur de Player.
     *
     * @param playerCouleur La couleur du joueur.
     * @param name          Le nom du joueur.
     * @param niveau        Le type du joueur (personne ou bot de différents niveaux).
     * @param game          Le jeu.
     */
    public Player(String playerCouleur, String name, int niveau, Game game) {
        this.playerCouleur = playerCouleur;
        this.name = name;
        this.niveau = niveau;
        this.game = game;
        this.score = 0;
        this.nbMissionComplete = 0;
        this.nbrWagon = initNbragon();
        this.nbrGare = 4;
    }
    
    /**
     * Compte le nombre de wagon du joueur similaire à la couleur de la route
     * @param r Route ciblé par le joueur
     * @return Retourne le nombre de carte compatible
     */
    private int carteDuJoueur(Route r){
		int count = 0;

		for(int i = 0; i < this.trainList.size(); i++) {
			if(compatibleColor(r, this.trainList.get(i)))count++;
		}

		return count;
	}


    /**
     * Initialise le nombre de wagons en fonction de la taille de la carte.
     *
     * @return Le nombre de wagons.
     */
    public int initNbragon() {
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

    /**
     * Pioche une carte de destination pour le joueur.
     *
     * @param cm Le gestionnaire de cartes (CarteManager).
     * @param i  L'index de la carte de destination à piocher.
     * @return true si la pioche a réussi, sinon false.
     */
    public boolean piocheCarteDestination(CarteManager cm, int i) {
        //vérifie s'il ne possède pas déjà la carte destination
        if(cm.getDestinationsCards()[i] != null) {
            //donne la carte destination et mets à null pour remplacer
            this.destinationsList.add(cm.getDestinationsCards()[i]);

            cm.getDestinationsCards()[i] = null;

            if( !this.destinationsList.isEmpty() )
            {
                this.canPlay = true;
            }

            game.playSoundClick("INGAME", "carte-dest.wav");

            return true;
        }else {
            game.playSoundClick("INGAME", "popUp.wav");

            JOptionPane.showMessageDialog(new JFrame(),"Vous avez déjà pioché cette carte !","Instructions",JOptionPane.WARNING_MESSAGE);
        }

        return false;
    }

    /**
     * Pioche une carte invisible pour le joueur.
     */
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
        game.playSoundClick("INGAME" ,"carte-wagon.wav");
    }


    /**
     * Pioche une carte visible de type CarteWagon pour le joueur.
     *
     * @param carte La couleur de la carte visible à piocher.
     * @return true si la pioche a réussi, sinon false.
     */
    public boolean piocheCarteVisible(CarteWagon.Couleur carte) {
        //On regarde si le joueur a 2 actions ou non
        if(game.getRound().getAction() > 1){

            //Si oui alors on regarde si c'est une carte locomotive ou non
            if(carte == Couleur.LOC){
                //Si c'est une locomotive on fini le tour du joueur
                game.playSoundClick("INGAME" ,"carte-wagon.wav");
                insertCarte(carte);
                game.getRound().endRound(game);
                return true;
            }
            else{
                //Sinon on enleve une action au joueur
                game.playSoundClick("INGAME" ,"carte-wagon.wav");
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
                game.playSoundClick("INGAME" ,"carte-wagon.wav");
                insertCarte(carte);
                game.getRound().endRound(game);
                return true;
            }
        }
    }

    /**
     * Insère une carte dans la liste de cartes du joueur.
     *
     * @param carte La carte à insérer.
     */
    private void insertCarte(Couleur carte) {
        int i = findCarteIndex(carte);
        this.trainList.add(i, carte);
    }

    /**
     * Trouve le bon index pour insérer la carte obtenue.
     *
     * @param carte La carte à insérer.
     * @return Le bon index.
     */
    private int findCarteIndex(Couleur carte) {
        int res = this.trainList.size();

        if (carte != Couleur.LOC) {
            int i = 0;
            while (i < this.trainList.size()) {
                if (carte.ordinal() > this.trainList.get(i).ordinal()) {
                    i++;
                } else {
                    break;
                }
            }
            res = i;
        }

        return res;
    }

    /**
     * Retire des cartes de couleur non locomotive de la liste du joueur.
     *
     * @param color          La couleur des cartes à retirer.
     * @param carteAEnlever  Le nombre de cartes à retirer.
     */
    public void retirerCarteNonLoc(Couleur color, int carteAEnlever) {
        int i = 0;
        setNbrWagon(this.nbrWagon - carteAEnlever);
        // Première boucle qui enlève juste la couleur color
        while (i < this.trainList.size() && 0 < carteAEnlever) {
            if (trainList.get(i) == color) {
                this.trainList.remove(i);
                carteAEnlever--;
            } else {
                i++;
            }
        }
        // Deuxième boucle qui enlève les cartes de couleur loc pour compléter les cartes à enlever
        int j = 0;
        while (j < this.trainList.size() && 0 < carteAEnlever) {
            if (trainList.get(j) == Couleur.LOC) {
                this.trainList.remove(j);
                carteAEnlever--;
            } else {
                j++;
            }
        }
    }

    /**
     * Retire des cartes locomotive de la liste du joueur.
     *
     * @param carteAEnlever Le nombre de cartes à retirer.
     */
    public void retirerCarteLoc(int carteAEnlever) {
        int i = 0;
        setNbrWagon(this.nbrWagon - carteAEnlever);
        while (i < this.trainList.size() && 0 < carteAEnlever) {
            this.trainList.remove(i);
            carteAEnlever--;
        }
    }

    public boolean retirerRouteAutreJoueurBot(Route r, Player p) {
        // Si le joueur n'est pas null
        if (this != null) {
            for (int i = 0; i < this.getPlayerRoutes().size(); i++) {
                // Si la Route correspond dans l'inventaire du joueur
                if (r == this.getPlayerRoutes().get(i)) {
                    // son
                    game.playSoundClick("INGAME", "tactical-nuke.wav");
                    // Retire la carte nuke de son inventaire
                    p.retirerCarteNuke();
                    // Enlève le proprio de la route et des rails
                    r.enleverProprio();
                    // Enlève la route de l'inventaire du joueur
                    this.getPlayerRoutes().remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retire une route d'un autre joueur.
     *
     * @param r  La route à retirer.
     * @param p  Le joueur qui effectue l'action de retrait.
     * @return true si la route a été retirée avec succès, sinon false.
     */
    public boolean retirerRouteAutreJoueur(Rail r, Player p) {
        if (this != null) {
            for (int i = 0; i < this.getPlayerRoutes().size(); i++) {
                if (r.getSaRoute() == this.getPlayerRoutes().get(i)) {
                    // son
                    game.playSoundClick("INGAME", "tactical-nuke.wav");
                    // Retire la carte nuke de son inventaire
                    p.retirerCarteNuke();
                    // Enlève le proprio de la route et des rails
                    r.getSaRoute().enleverProprio();
                    // Enlève la route de l'inventaire du joueur
                    this.getPlayerRoutes().remove(i);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Retire la gare d'un autre joueur.
     *
     * @param ville   La ville dont la gare doit être retirée.
     * @param player  Le joueur qui effectue l'action de retrait.
     */
    public void retirerGareAutrePlayer(Ville ville, Player player) {
        ville.setIsOccuped(null); // Enlève le proprio de la ville
        player.retirerCarteNuke(); // Retire la carte nuke de l'inventaire
        this.nbrGare += 1; // Rajoute une gare disponible
    }


    /**
     * Vérifie si le joueur possède une carte nuke.
     *
     * @return true si le joueur possède une carte nuke, sinon false.
     */
    public boolean checkACarteNuke() {
        for (Couleur couleur : this.trainList) {
            if (couleur == Couleur.NUKE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retire une carte nuke de l'inventaire du joueur.
     */
    private void retirerCarteNuke() {
        for (int i = 0; i < this.trainList.size(); i++) {
            if (this.trainList.get(i) == Couleur.NUKE) {
                this.trainList.remove(i);
                return;
            }
        }
    }

    /**
     * Retire les cartes nécessaires pour poser une gare.
     *
     * @param couleur        La couleur des cartes à retirer.
     * @param carteAEnlever  Le nombre de cartes à retirer.
     */
    public void retirerLesCartes(Couleur couleur, int carteAEnlever) {
        if (couleur != Couleur.LOC) {
            retirerCarteNonLoc(couleur, carteAEnlever);
        } else {
            retirerCarteLoc(carteAEnlever);
        }
    }

    /**
     * Pose une route pour le joueur.
     *
     * @param r  La route à poser.
     * @return true si la route a été posée avec succès, sinon false.
     */
    public boolean mettreRoute(Route r) {
    	if(r != null) {
    		if (r.getLongueur() <= this.carteDuJoueur(r) && r.getProprietaire() == null  && r.getLongueur() <= this.nbrWagon ) {
				game.playSoundClick("INGAME" ,"mettreRoute.wav");

				this.retirerLesCartes(r.traducteurCouleur(), r.getLongueur());
                r.setProprietaire(this); // Met à jour le propriétaire de la route.
                this.score += r.getNombrePoint();
                playerRoutes.add(r);
                score += aCompleterUneMission(); // Vérifie si une mission est complétée et ajoute les points le cas échéant
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si une mission a été complétée.
     *
     * @return Le nombre de points cumulés pour les missions complétées.
     */
    private int aCompleterUneMission() {
        if (destinationsList.isEmpty()) return 0;
        int cumulPoints = 0;
        for (CarteDestination c : destinationsList) {
            if (c.getComplete()) continue; // Éviter les cartes déjà complétées.

            Ville ville1 = c.getPremiereVille();
            Ville ville2 = c.getDeuxiemeVille();
            if (LongestFinder.wayExists(ville1, ville2, this)) {
                cumulPoints += c.getNombrePoints();  // Cumule les points pour les missions complétées
                c.setComplete();
                this.setMissionComplete(this.nbMissionComplete + 1);
            }
        }
        return cumulPoints;
    }

    /**
     * Transforme une ville en gare pour le joueur.
     *
     * @param ville                La ville à transformer.
     * @param couleurCarteChoisit  La couleur de la carte choisie.
     * @return true si la transformation a réussi, sinon false.
     */
    public boolean transformerEnGare( Ville ville , Couleur couleurCarteChoisit ){

        if ( assezDeGare() && ville.getIsOccuped() == null){
            int nbrCarteRetirer = nombreDeCartePourPoserUneGare() ;


            //Si c'est un joueur alors on fait la demande, sinon pour les bots on fait directement le procédé
            if(this.niveau == 0){
                game.playSoundClick("INGAME" , "popUp.wav");

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
                        game.playSoundClick("INGAME" , "popUp.wav");
                        JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                                "Cette ville possède déja un propriétaire. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
                        return false;

                    } else {
                        game.playSoundClick("INGAME" , "popUp.wav");
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
            game.playSoundClick("INGAME", "popUp.wav");
            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                    "Vous n'avez plus assez de gare pour pour posséder cette ville. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
            return false;
        }

        return false;
    }



    /**
     * Retourne le nombre de cartes nécessaires pour poser une gare.
     *
     * @return Le nombre de cartes nécessaires pour poser une gare.
     */
    public int nombreDeCartePourPoserUneGare() {
        switch (nbrGare) {
            case 4:
                return 1;
            case 3:
                return 2;
            case 2:
                return 3;
            case 1:
                return 4;
            default:
                return 0;
        }
    }

    /**
     * Compte le nombre de cartes de la couleur que le joueur a choisi pour changer les cartes en gare.
     *
     * @param couleurCarteChoisit La couleur choisie.
     * @return Le nombre de cartes de la couleur choisie.
     */
    public int peutChangerAvecCetteCarte(Couleur couleurCarteChoisit) {
        int count = 0;
        for (Couleur c : trainList) {
            if (couleurCarteChoisit == c) count++;
        }
        return count;
    }


    /**
     * Enlève une gare et les cartes nécessaires pour faire l'échange.
     *
     * @param couleur        La couleur des cartes.
     * @param carteAEnlever  Le nombre de cartes à retirer.
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


    /**
     * Change une gare en ville si applicable.
     *
     * @param x La coordonnée x.
     * @param y La coordonnée y.
     * @param p Le plateau.
     * @return true si la transformation a réussi, sinon false.
     */
    public boolean changerGareEnVille(int x, int y, Plateau p) {
        if (p.positionValide(x, y)) {
            if (p.estUneCaseVille(x, y) && !p.estUneCaseGare(x, y)) {
                ((Ville) p.getPlateau()[x][y]).setIsOccuped(this);
                return true;
            }
        }
        return false;
    }


    /**
     * Vérifie si la couleur de la carte est compatible avec la route.
     *
     * @param r        La route.
     * @param couleur  La couleur de la carte.
     * @return true si la couleur est compatible, sinon false.
     */
    public boolean compatibleColor(Route r, Couleur couleur) {
        Rail.Content color = r.getCouleur();
        Rail.Content color2 = r.getRailsRoute().get(0).getInitialContent();

        if (couleur == Couleur.LOC) {
            return true;
        } else if (color2 == Rail.Content.JOKERETOILEE || color2 == Rail.Content.JOKER) {
            return true;
        }

        return color.ordinal() == couleur.ordinal();
    }

    /**
     * Pioche une carte de wagon pour le joueur.
     *
     * @param cm Le gestionnaire de cartes.
     */
    public void piocher(CarteManager cm) {
        insertCarte(cm.drawCard());
    }

    /**
     * Retourne le score final avec le nombre de gares restantes.
     *
     * @return Le score final.
     */
    public int scoreFinal() {
        return this.score + nbrGare * 4;
    }

    /**
     * Vérifie si le joueur a assez de gares.
     *
     * @return true si le joueur a au moins une gare, sinon false.
     */
    public boolean assezDeGare() {
        return this.nbrGare > 0;
    }

    /**
     * Retourne la liste des routes du joueur.
     *
     * @return La liste des routes.
     */
    public ArrayList<Route> getPlayerRoutes() {
        return this.playerRoutes;
    }

    /* Getters et Setters */

    /**
     * Retourne la couleur du joueur.
     *
     * @return La couleur du joueur.
     */
    public String getPlayerCouleur() {
        return playerCouleur;
    }


    /**
     * Retourne le nom du joueur.
     *
     * @return Le nom du joueur.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne le niveau du joueur.
     *
     * @return Le niveau du joueur.
     */
    public int getNiveau() {
        return niveau;
    }

    /**
     * Retourne le jeu associé au joueur.
     *
     * @return Le jeu.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Retourne le score du joueur.
     *
     * @return Le score du joueur.
     */
    public int getScore() {
        return this.score;
    }


    /**
     * Ajouter 10 points au joueur.
     */
	public void addLongestWayScore()
	{
		this.score += 10;
	}

    /**
     * Retourne le nombre de wagons du joueur.
     *
     * @return Le nombre de wagons.
     */
    public int getNbrWagon() {
        return nbrWagon;
    }

    /**
     * Définit le nombre de wagons du joueur.
     *
     * @param nbrWagon Le nombre de wagons.
     */
    public void setNbrWagon(int nbrWagon) {
        this.nbrWagon = nbrWagon;
    }

    /**
     * Retourne le nombre de gares du joueur.
     *
     * @return Le nombre de gares.
     */
    public int getNbrGare() {
        return nbrGare;
    }

    /**
     * Définit le nombre de gares du joueur.
     *
     * @param nbrGare Le nombre de gares.
     */
    public void setNbrGare(int nbrGare) {
        this.nbrGare = nbrGare;
    }

    /**
     * Retourne le nombre de missions complétées par le joueur.
     *
     * @return Le nombre de missions complétées.
     */
    public int getMissionComplete() {
        return nbMissionComplete;
    }

    /**
     * Définit le nombre de missions complétées par le joueur.
     *
     * @param missionComplete Le nombre de missions complétées.
     */
    public void setMissionComplete(int missionComplete) {
        this.nbMissionComplete = missionComplete;
    }

    /**
     * Retourne la liste des cartes de destination du joueur.
     *
     * @return La liste des cartes de destination.
     */
    public ArrayList<CarteDestination> getDestinationsList() {
        return destinationsList;
    }

    /**
     * Retourne la liste des cartes wagon du joueur.
     *
     * @return La liste des cartes wagon.
     */
    public ArrayList<Couleur> getTrainList() {
        return trainList;
    }

    /**
     * Retourne si le joueur peut jouer ou non.
     *
     * @return true si le joueur peut jouer, sinon false.
     */
    public boolean getCanPlay() {
        return canPlay;
    }

    /**
     * Retourne si le premier tour du joueur est terminé.
     *
     * @return true si le premier tour est terminé, sinon false.
     */
    public boolean getFirstTurnOver() {
        return firstTurnOver;
    }

    /**
     * Définit si le premier tour du joueur est terminé.
     *
     * @param firstTurnOver true si le premier tour est terminé, sinon false.
     */
    public void setFirstTurnOver(boolean firstTurnOver) {
        this.firstTurnOver = firstTurnOver;
    }
}
