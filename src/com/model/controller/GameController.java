package com.model.controller;
import com.model.config.carte.CarteWagon;
import com.model.Player;
import com.model.config.Plateau;
import com.model.config.Rail;
import com.model.config.carte.CarteDestination;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameController {
    private String detailsCarte; // Variable pour sauvegarder les détails de la carte
    
    public void mouseClicked(MouseEvent e, Plateau plateau, Player joueurCourant) {
        // Obtention des coordonnées du clic de souris
        int x = e.getX();
        int y = e.getY();

        // Utilisation des coordonnées x et y pour déterminer l'objet sur lequel l'utilisateur a cliqué
        Object clickedObject = plateau.getPlateau()[x][y];

        if (clickedObject != null) {
            // Traitement en fonction du type de l'objet cliqué
            if (clickedObject instanceof Rail) {
                tenterAcquisitionRoute((Rail) clickedObject, plateau, joueurCourant);
            } else if (clickedObject instanceof CarteDestination) {
                // Pour une CarteDestination est cliquée
            } else if (clickedObject instanceof CarteWagon) {
                // Pour une CarteWagon est cliquée
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
        if (r.getSaRoute().getProprietaire() != null) {
            System.out.println("Ce rail a déjà un propriétaire.");
            return;
        }

        //TODO : Bouton de confirmation      
        player.mettreRoute(r.getSaRoute());
    }
}