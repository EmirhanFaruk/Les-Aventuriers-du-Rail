package com.model.controller;

import com.model.Game;
import com.model.Round;
import com.model.config.carte.*;
import com.model.config.carte.CarteWagon.Couleur;
import com.model.Player;
import com.model.config.*;
import com.view.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameController {
    private CarteWagon carteWagon ;
    private int Mx , My  ;

    public void mouseClicked(MouseEvent e, int tileWidth, int tileHeight, Game game, Player joueurCourant) {
        // Obtention des coordonnées du clic de souris
    	int x = e.getX() / tileWidth;
    	int y = e.getY() / tileHeight;

        // Utilisation des coordonnées x et y pour déterminer l'objet sur lequel l'utilisateur a cliqué
        try {
            Object clickedObject = game.getPlateau().getPlateau()[x][y];
            if (clickedObject != null) {
                // Traitement en fonction du type de l'objet cliqué
                if (clickedObject instanceof Rail) {
                    tenterAcquisitionRoute((Rail) clickedObject, joueurCourant, game.getRound(), game);
                    Mx = x;
                    My = y;
                } else if (clickedObject instanceof Ville) {
                    Mx = x;
                    My = y;
                }
            }
        } catch ( Exception ignored ){
            // DEBUG : System.out.println("Nope hihi");
        }
    }

	public boolean isEntreeAppuye(KeyEvent e) {
		return e.getKeyCode() == KeyEvent.VK_ENTER;
	}

	/**
	 * Ue fonction qui tente s'il est possible d'occuper route qu'on a choisi
	 * @param r la rail
	 * @param player le joueur
	 * @param round le tour
	 * @param game le jeu
	 */
    public void tenterAcquisitionRoute(Rail r, Player player, Round round, Game game) {
        //Empêche le joueur de faire cette action s'il a déjà pris une carte destination
    	if(player.getFirstTurnOver() && game.getCarteManager().alreadyPickedACard()) {
   		 JOptionPane.showMessageDialog( player.getGame().getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
                    ,"TU CROIS M'AVOIR SALE FOU T'AS DEJA PRIS UNE CARTE DESTINATION !","INFORMATION", JOptionPane.INFORMATION_MESSAGE ) ;
    	}else {
    		//Force le premier tour du joueur a pioché une carte destination
    		if(player.getCanPlay()) {

	    		if(round.getAction() < 2){
	                JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                        "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
	                return;
	            }

	            if (r.getSaRoute() != null &&  r.getSaRoute().getProprietaire() != null && player.getNiveau() == 0) {
	            	if(!player.checkACarteNuke()) {
		                JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
		                        "Cette route a déja un propriétaire. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
	            	}
	                return;
	            }

	            if ( player.getNiveau() == 0 ){
	                int choixUtilisateur = JOptionPane.showConfirmDialog(
	                        game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                        "Êtes-vous sûr de vouloir poser votre rail ici ?", "CONFIRMATION", JOptionPane.YES_NO_OPTION);

	                if (choixUtilisateur == JOptionPane.YES_OPTION) {
	                    if (player.mettreRoute(r.getSaRoute())) {
	                        int tailleRoute = r.getSaRoute().getRailsRoute().size();
	                        ArrayList<Rail> listeRail = r.getSaRoute().getRailsRoute();

	                        for (int i = 0; i < tailleRoute; i++) {
	                            listeRail.get(i).setOccuperPar(player);
	                        }

	                        round.endRound(game);
	                    } else if ( r.getSaRoute().getLongueur() > player.getNbrWagon() ) {
                            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                                    "Vous n'avez pas assez de wagon pour posséder cette route. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
                        } else {
	                        JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                                "Vous n'avez pas assez de carte pour posséder cette route. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
	                    }
	                }
	            } else {
	                if (player.mettreRoute(r.getSaRoute())) {
	                    int tailleRoute = r.getSaRoute().getRailsRoute().size();
	                    ArrayList<Rail> listeRail = r.getSaRoute().getRailsRoute();

	                    for (int i = 0; i < tailleRoute; i++) {
	                        listeRail.get(i).setOccuperPar(player);
	                    }

	                    round.endRound(game);
	                }
	            }

	    	}else {
	    		JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                    "Vous devez d'abord piocher 1 carte destination au minimum !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
	    	}
    	}
    }

	/**
	 * Une fonction qui permet de choisir quelle carte on veut utiliser pour transformer une ville en gare
	 * @param e le lectrue de souris
	 * @param player le joueur
	 * @param playerHandPanel la main du joueur
	 * @param game le jeu
	 */
    public void couleurCarteAChoisir(MouseEvent e , Player player , PlayerHandPanel playerHandPanel, Game game){
    	//Empêche le joueur de faire cette action s'il a déjà pris une carte destination
    	if(player.getFirstTurnOver() && game.getCarteManager().alreadyPickedACard()) {
      		 JOptionPane.showMessageDialog( player.getGame().getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
                       ,"TU CROIS M'AVOIR SALE FOU T'AS DEJA PRIS UNE CARTE DESTINATION !","INFORMATION", JOptionPane.INFORMATION_MESSAGE ) ;
       	}else {
    		//Force le premier tour du joueur a pioché une carte destination
	    	if(player.getCanPlay()) {
	    		CarteWagon source = playerHandPanel.getDrawPlayerHand(player.getName()).CardClicked( e.getX() , e.getY() );
	    		
	    		if ( source != null ) {
	                // Si la source est une carte wagon
	                this.carteWagon = source;
	                    
	                try {	                
	                    if(source.getInitialCouleur() != Couleur.NUKE){
		                    Ville ville = (Ville) playerHandPanel.getGame().getPlateau().getPlateau()[Mx][My];
	                    	
	                    	if(tenterDePoserUneGare(ville, player, game)) {
		                        game.getRound().endRound(game);
	                    	}
	                    }else {
	                    	
	                    	 try {
	                    		 if(source.getInitialCouleur() == Couleur.NUKE) {
	     	                    	actionDeNuke(e, playerHandPanel, player, game);
	     	                    	game.getRound().endRound(game);
	     	                	}
	                    	}catch ( Exception exception ){
	    	                    if ( player.getNiveau() == 0 ) {
	    	                        JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
	    	                                , "Veuillez choisir une ville ou une rail avant la NUKE !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
	    	                        //DEBUG : System.err.println( "D'abord selectionner une ville" ) ;
	    	                    }
	                    	}
	                    }
	                    
	                } catch ( Exception exception ){
	                    if ( player.getNiveau() == 0 ) {
	                        JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
	                                , "Veuillez choisir une ville avant de choisir la carte !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
	                        //DEBUG : System.err.println( "D'abord selectionner une ville" ) ;
	                    }
	                }

	                    
	            }

	    	}else {
	    		JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                    "Vous devez d'abord piocher 1 carte destination au minimum !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
	    	}
    	}
    }
    
    //Action de la Nuke pour voir ce qu'elle doit faire
	private boolean actionDeNuke(MouseEvent e, PlayerHandPanel playerHandPanel, Player player, Game game) {	
		
		//Element inconnu auquel on a cliqué dessus
		Object CestQuoi = playerHandPanel.getGame().getPlateau().getPlateau()[Mx][My];
		
        if(CestQuoi instanceof Ville){
        	
        	Ville ville = (Ville) CestQuoi;
        	
        	//Si c'est une ville on tente de détruire la Gare
        	if(tenterDeDetruireGare(ville, player, game)){
                return true;
        	}
        	
        }else if(CestQuoi instanceof Rail) {
        	
        	Rail rail = (Rail) CestQuoi;
          	
        	//Si c'est une rail on tente de détruire la Route
       	 	if(tenterDeDetruireRoute(rail, player, game)) {
       	 		return true;
       	 	}
        }
		
        //Dans aucun des deux cas on ne fait rien
		return false;
	}
	
	//Méthode pour détruire une Route
	private boolean tenterDeDetruireRoute(Rail rail, Player player, Game game) {
		//On check si son action est supérieur à 2 autrement on refuse l'action
        if(game.getRound().getAction() < 2){
            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                    "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
            return false;
        }
        
        Player autrePlayer = rail.getSaRoute().getProprietaire();
        
        //On vérifie si le joueur à une carte Nuke, si oui on retire finalement la route
        if(player.checkACarteNuke()) {
        	return autrePlayer.retirerRouteAutreJoueur(rail, player);
        }
        
		return false;
	}
	
	//Méthode pour détruire une Gare
	private boolean tenterDeDetruireGare(Ville ville, Player player, Game game) {
		//On check si son action est supérieur à 2 autrement on refuse l'action
        if(game.getRound().getAction() < 2){
            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                    "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
            return false;
        }
        
        Player autrePlayer = ville.getIsOccuped();
        
        //On vérifie si le joueur à une carte Nuke, si oui on retire finalement la gare de la Ville
        if(player.checkACarteNuke() && autrePlayer != null){ 
        	autrePlayer.retirerGareAutrePlayer(ville, player);
        	return true;
        }
        
		return false;
	}

	/**
	 * Une fonction qui tente de poser une gare
	 * @param ville la ville que l'in veut transformer en gare
	 * @param player le joueur
	 * @param game le jeu
	 * @return renvoie true si la gare est posé
	 */
    public boolean tenterDePoserUneGare(Ville ville , Player player, Game game ){
        if(game.getRound().getAction() < 2){
            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                    "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
            return false;
        }

        boolean didIt = player.transformerEnGare( ville , carteWagon.getInitialCouleur() ) ;
        Mx = -1;
        My = -1;

        return didIt;
    }

    public boolean piocherCarteVisible(Player player, Couleur imagePiocheVisible) {
    	return player.piocheCarteVisible(imagePiocheVisible);
	}

    public boolean piocherCarteDestination(Player player, CarteManager cm, int i) {
    	return player.piocheCarteDestination(cm, i);
	}

	public void piocherCarteInvisible(Player player) {
		player.piocheCarteInvisible();
	}


	public void descriptionCardDestination(CarteDestination carteHover, Game game) {
		JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                carteHover.getDescription(), "Carte Destination", JOptionPane.INFORMATION_MESSAGE );
	}
}