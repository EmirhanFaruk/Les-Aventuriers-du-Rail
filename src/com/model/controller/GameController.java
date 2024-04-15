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
    private String detailsCarte; // Variable pour sauvegarder les détails de la carte
    private CarteWagon carteWagon ;
    private int Mx , My  ;

    public void mouseClicked(MouseEvent e, int tileWidth, int tileHeight, Game game, Player joueurCourant) {
        // Obtention des coordonnées du clic de souris
    	int x = e.getX() / tileWidth;
    	int y = e.getY() / tileHeight;

        // Utilisation des coordonnées x et y pour déterminer l'objet sur lequel l'utilisateur a cliqué
        try {
            Object clickedObject = game.getPlateau().getPlateau()[x][y];
            Rail r = null;
            if (clickedObject instanceof Rail) r = (Rail) clickedObject;

            //DEBUG : POSITION
        /*
        System.out.println("x = " + x + " y = " + y);
        if(r!=null) {
        	System.out.println(clickedObject + " " + r.getInitialContent() + " " + r.getSaRoute());
        }else {
        	System.out.println(clickedObject);
        }
        }*/


            if (clickedObject != null) {
                // Traitement en fonction du type de l'objet cliqué
                if (clickedObject instanceof Rail) {
                    tenterAcquisitionRoute((Rail) clickedObject, joueurCourant, game.getRound(), game);
                } else if (clickedObject instanceof Ville) {
                    Mx = x;
                    My = y;
                }
            }
        } catch ( Exception exception ){
            // DEBUG : System.out.println("Nope hihi");
        }
    }


	public String obtenirDetailsCarte(MouseEvent e) {
        Object source = e.getSource(); // Obtenir la source de l'événement
        if (source instanceof CarteDestination) { // Si la source est une carte destination
            CarteDestination carteDestination = (CarteDestination) source;
            detailsCarte = "Destination: " + carteDestination.getPremiereVille() + " - " + carteDestination.getDeuxiemeVille();
        } else if (source instanceof CarteWagon) { // Si la source est une carte wagon
            CarteWagon carteWagon = (CarteWagon) source;
            detailsCarte = "Couleur du wagon: " + carteWagon.getInitialCouleur();
        }
        return detailsCarte;
    }


	public boolean isEntreeAppuye(KeyEvent e) {
		return e.getKeyCode() == KeyEvent.VK_ENTER;
	}
	
	
    public boolean isEspaceAppuye(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_SPACE; // Renvoie true si la touche "Espace" est appuyée
    }
     
    // Vérifie si le rail a déjà un propriétaire
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
	
	            if (r.getSaRoute() != null &&  r.getSaRoute().getProprietaire() != null && player.getNiveau() == 0 ) {
	                JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                        "Cette route a déja un propriétaire. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
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
	                    Ville ville = (Ville) playerHandPanel.getGame().getPlateau().getPlateau()[Mx][My];
	                    if(tenterDePoserUneGare( ville , player, game )){
	                        game.getRound().endRound(game);
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

    public boolean tenterDePoserUneGare(Ville ville , Player player, Game game ){  	
        if(game.getRound().getAction() < 2){
            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                    "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
            return false;
        }

        boolean didIt = player.transformerEnGare( ville , carteWagon.getInitialCouleur() ) ;
        Mx = -1 ;
        My = -1 ;

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