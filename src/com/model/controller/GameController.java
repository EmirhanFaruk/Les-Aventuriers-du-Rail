package com.model.controller;
import com.model.config.carte.CarteWagon;
import com.model.config.carte.CarteWagon.Couleur;
import com.model.Player;
import com.model.config.Plateau;
import com.model.config.Rail;
import com.model.config.carte.CarteDestination;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GameController {
    private String detailsCarte; // Variable pour sauvegarder les détails de la carte
    
    public void mouseClicked(MouseEvent e, int tileWidth, int tileHeight, Plateau plateau, Player joueurCourant) {
        // Obtention des coordonnées du clic de souris
    	int x = e.getX() / tileWidth;
    	int y = e.getY() / tileHeight;

        // Utilisation des coordonnées x et y pour déterminer l'objet sur lequel l'utilisateur a cliqué
        Object clickedObject = plateau.getPlateau()[x][y];
        Rail r = null;
        if(clickedObject instanceof Rail)r = (Rail) clickedObject;
        
        //DEBUG : POSITION
        /*
        System.out.println("x = " + x + " y = " + y);
        if(r!=null) {
        	System.out.println(clickedObject + " " + r.getInitialContent() + " " + r.getSaRoute());
        }else {
        	System.out.println(clickedObject);
        }*/
        

        if (clickedObject != null) {
            // Traitement en fonction du type de l'objet cliqué
            if (clickedObject instanceof Rail) {
                tenterAcquisitionRoute((Rail) clickedObject, plateau, joueurCourant);
            } 
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
     
    public void tenterAcquisitionRoute(Rail r, Plateau plateau, Player player) {
        // Vérifie si le rail a déjà un propriétaire
        if (r.getSaRoute() != null &&  r.getSaRoute().getProprietaire() != null) {
            System.out.println("Ce rail a déjà un propriétaire.");
            return;
        }

        //TODO : Bouton de confirmation      
        if(player.mettreRoute(r.getSaRoute())) {
        	int tailleRoute = r.getSaRoute().getRailsRoute().size();
        	ArrayList<Rail> listeRail = r.getSaRoute().getRailsRoute(); 
        	
        	for(int i=0; i<tailleRoute; i++) {
        		listeRail.get(i).setOccuperPar(player);
        	}
        }
    }
    
    public void piocherCarteVisible(Player player, Couleur imagePiocheVisible) {
    	player.piocheCarteVisible(imagePiocheVisible);
	}


	public void piocherCarteInvisible(Player player) {
		player.piocheCarteInvisible();
	}
}