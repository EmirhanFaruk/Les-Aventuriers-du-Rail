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

    /**
    * Gère le clic de souris sur le plateau de jeu.
    *
    * @param e              L'événement de la souris.
    * @param tileWidth      La largeur d'une tuile.
    * @param tileHeight     La hauteur d'une tuile.
    * @param game           Le jeu en cours.
    * @param joueurCourant  Le joueur qui effectue l'action.
    * @param zoomed         Indique si la carte est zoomée ou non.
    */
   public void mouseClicked(MouseEvent e, int tileWidth, int tileHeight, Game game, Player joueurCourant, boolean zoomed) {
       // Obtention des coordonnées du clic de souris
       int x = e.getX() / tileWidth;
       int y = e.getY() / tileHeight;

       // Utilisation des coordonnées x et y pour déterminer l'objet sur lequel l'utilisateur a cliqué
       try {
           Object clickedObject = game.getPlateau().getPlateau()[x][y];
           if (clickedObject != null) {
               // Traitement en fonction du type de l'objet cliqué
               if (!zoomed) {
                   if (clickedObject instanceof Rail) {
                       tenterAcquisitionRoute((Rail) clickedObject, joueurCourant, game.getRound(), game);
                       Mx = x;
                       My = y;
                   } else if (clickedObject instanceof Ville) {
                       Mx = x;
                       My = y;
                   }
               } else {
                   JOptionPane.showMessageDialog(new JFrame(),
                           "Veuillez dézoomer la map pour pouvoir poser vos wagons ou une gare.", "Instructions", JOptionPane.WARNING_MESSAGE);
               }
           }
       } catch (Exception ignored) {
           // DEBUG : System.out.println("Nope hihi");
       }
   }

	public boolean isEntreeAppuye(KeyEvent e) {
		return e.getKeyCode() == KeyEvent.VK_ENTER;
	}

	/**
	 * Une fonction qui tente s'il est possible d'occuper route qu'on a choisi
	 * @param r la rail
	 * @param player le joueur
	 * @param round le tour
	 * @param game le jeu
	 */
    public void tenterAcquisitionRoute(Rail r, Player player, Round round, Game game) {
		System.out.println("\n\n\nTenter acquisition route de " + player.getName());
        //Empêche le joueur de faire cette action s'il a déjà pris une carte destination
    	if(player.getFirstTurnOver() && game.getCarteManager().alreadyPickedACard()) {
			game.playSoundClick("INGAME" , "popUp.wav");
			JOptionPane.showMessageDialog( player.getGame().getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
					,"TU CROIS M'AVOIR SALE FOU T'AS DEJA PRIS UNE CARTE DESTINATION !","INFORMATION", JOptionPane.INFORMATION_MESSAGE ) ;
    	}else {
			System.out.println("Pas encore pris une carte. CanPlay: " + player.getCanPlay() + ", action: " + round.getAction());
    		//Force le premier tour du joueur a pioché une carte destination
    		if(player.getCanPlay()) {
				System.out.println("can play");
	    		if(round.getAction() < 2){
					System.out.println("round.getAction() < 2");
					game.playSoundClick("INGAME" , "popUp.wav");
	                JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                        "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
	                return;
	            }

	            if (r.getSaRoute() != null &&  r.getSaRoute().getProprietaire() != null && player.getNiveau() == 0) {
					System.out.println("r.getSaRoute() != null &&  r.getSaRoute().getProprietaire() != null && player.getNiveau() == 0");
	            	if(!player.checkACarteNuke()) {
						game.playSoundClick("INGAME" , "popUp.wav");
		                JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
		                        "Cette route a déja un propriétaire. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
	            	}
	                return;
	            }

	            if ( player.getNiveau() == 0 ){
					System.out.println("player.getNiveau() == 0");
					game.playSoundClick("INGAME" , "popUp.wav");
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
							game.playSoundClick("INGAME" , "popUp.wav");
                            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                                    "Vous n'avez pas assez de wagon pour posséder cette route. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
                        } else {
							game.playSoundClick("INGAME" , "popUp.wav");
	                        JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
	                                "Vous n'avez pas assez de carte pour posséder cette route. ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE );
	                    }
	                }
	            } else {
					System.out.println("player.getNiveau() != 0");
	                if (player.mettreRoute(r.getSaRoute())) {
	                    int tailleRoute = r.getSaRoute().getRailsRoute().size();
	                    ArrayList<Rail> listeRail = r.getSaRoute().getRailsRoute();

	                    for (int i = 0; i < tailleRoute; i++) {
	                        listeRail.get(i).setOccuperPar(player);
	                    }

	                    round.endRound(game);
	                }
	            }

	    	}else if ( player.getNiveau() == 0 ) {
				game.playSoundClick("INGAME" , "popUp.wav");
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
		System.out.println("\n\n\ncouleurCarteAChoisir avec player: " + player.getName());
		if ( player.getNiveau() == 0 ) {
			//Empêche le joueur de faire cette action s'il a déjà pris une carte destination
			if (player.getFirstTurnOver() && game.getCarteManager().alreadyPickedACard()) {
				System.out.println("player.getFirstTurnOver() && game.getCarteManager().alreadyPickedACard()");
					game.playSoundClick("INGAME", "popUp.wav");
				JOptionPane.showMessageDialog(player.getGame().getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
						, "Vous avez déjà pris une carte destination ! (Prenez en une autre ou terminer votre tour)", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
			} else {
				System.out.println("first else");
				//Force le premier tour du joueur a pioché une carte destination
				if (player.getCanPlay()) {
					System.out.println("player can play");
					CarteWagon source = playerHandPanel.getDrawPlayerHand(player.getName()).CardClicked(e.getX(), e.getY());

					if (source != null) {
						System.out.println("Source not null");
						// Si la source est une carte wagon
						this.carteWagon = source;

						try {
							System.out.println("Inside try");
							if (source.getInitialCouleur() != Couleur.NUKE) {
								System.out.println("Not nuke");
								Ville ville = (Ville) playerHandPanel.getGame().getPlateau().getPlateau()[Mx][My];

								if (tenterDePoserUneGare(ville, player, game)) {
									game.getRound().endRound(game);
								}
							} else {
								System.out.println("Nuke");
								try {
									System.out.println("Inside try");
									if (source.getInitialCouleur() == Couleur.NUKE) {
										actionDeNuke(e, playerHandPanel, player, game);
										game.playSoundClick("INGAME", "tactical-nuke.wav");
										game.getRound().endRound(game);
									}
								} catch (Exception exception) {
									System.out.println("Inside catch");
									if (player.getNiveau() == 0) {
										game.playSoundClick("INGAME" , "popUp.wav");
										JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
												, "Veuillez choisir une ville ou une rail avant la NUKE !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
										//DEBUG : System.err.println( "D'abord selectionner une ville" ) ;
									}
								}
							}

						} catch (Exception exception) {
							System.out.println("Inside catch");
							if (player.getNiveau() == 0) {
								game.playSoundClick("INGAME", "popUp.wav");
								JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel()
										, "Veuillez choisir une ville avant de choisir la carte !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
								//DEBUG : System.err.println( "D'abord selectionner une ville" ) ;
							}
						}


					}

				} else {
					game.playSoundClick("INGAME" , "popUp.wav");
					JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
							"Vous devez d'abord piocher 1 carte destination au minimum !", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
    }

    /**
     * Effectue une action de type "Nuke" en fonction de l'élément cliqué par le joueur.
     *
     * @param e                L'événement de la souris.
     * @param playerHandPanel  Le panneau de la main du joueur.
     * @param player           Le joueur qui effectue l'action.
     * @param game             Le jeu en cours.
     * @return                 true si l'action est validée et effectuée, sinon false.
     */
    private boolean actionDeNuke(MouseEvent e, PlayerHandPanel playerHandPanel, Player player, Game game) {
        // Element inconnu auquel on a cliqué dessus
        Object CestQuoi = playerHandPanel.getGame().getPlateau().getPlateau()[Mx][My];

        boolean actionValid = false;

        if (CestQuoi instanceof Ville) {
            Ville ville = (Ville) CestQuoi;

            // Si c'est une ville on tente de détruire la Gare
            actionValid = tenterDeDetruireGare(ville, player, game);

        } else if (CestQuoi instanceof Rail) {
            Rail rail = (Rail) CestQuoi;

            // Si c'est un rail on tente de détruire la Route
            actionValid = tenterDeDetruireRoute(rail, player, game);
        }

        if (actionValid) {
            showNukeAnimation(game); // Afficher l'animation nuke
        }
		// Dans aucun des deux cas on ne fait rien
		return actionValid;
	}


	/**
	 * Tente de détruire une route.
	 *
	 * @param rail    Le rail représentant la route à détruire.
	 * @param player  Le joueur qui tente de détruire la route.
	 * @param game    Le jeu en cours.
	 * @return        true si la route a été détruite, sinon false.
	 */
	private boolean tenterDeDetruireRoute(Rail rail, Player player, Game game) {
		//On check si son action est supérieur à 2 autrement on refuse l'action
        if(game.getRound().getAction() < 2){
			game.playSoundClick("INGAME" , "popUp.wav");
            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                    "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
			return false;
		}

		Player autrePlayer = rail.getSaRoute().getProprietaire();

		// On vérifie si le joueur a une carte Nuke, si oui on retire finalement la route
		if (player.checkACarteNuke()) {
			return autrePlayer.retirerRouteAutreJoueur(rail, player);
		}

		return false;
	}

    /**
     * Affiche une animation de "Nuke" pour représenter l'action de destruction.
     *
     * @param game  Le jeu en cours.
     */
    public void showNukeAnimation(Game game) {
        SwingUtilities.invokeLater(() -> {
            JFrame animationFrame = new JFrame("Nuke Animation");
            NukeAnimationPanel animationPanel = new NukeAnimationPanel();
            animationFrame.add(animationPanel);
            animationFrame.pack();
            animationFrame.setLocationRelativeTo(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel());
            //animationFrame.setUndecorated(true); // No title bar
            animationFrame.setVisible(true);
        });
    }



	/**
	 * Tente de détruire une gare.
	 *
	 * @param ville   La ville où se trouve la gare à détruire.
	 * @param player  Le joueur qui tente de détruire la gare.
	 * @param game    Le jeu en cours.
	 * @return        true si la gare a été détruite, sinon false.
	 */
	private boolean tenterDeDetruireGare(Ville ville, Player player, Game game) {
		//On check si son action est supérieur à 2 autrement on refuse l'action
        if(game.getRound().getAction() < 2){
			game.playSoundClick("INGAME" , "popUp.wav");
            JOptionPane.showMessageDialog(  game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                    "Vous ne pouvez que piocher des cartes, IT'S YOUR CHOICE ! ", "YU-GI-OH", JOptionPane.INFORMATION_MESSAGE );
            return false;
        }

        Player autrePlayer = ville.getIsOccuped();

        // On vérifie si le joueur a une carte Nuke, si oui on retire finalement la gare de la Ville
        if (player.checkACarteNuke() && autrePlayer != null) {
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

    /**
     * Pioche une carte visible pour le joueur.
     *
     * @param player              Le joueur qui pioche la carte.
     * @param imagePiocheVisible  La couleur de la carte visible à piocher.
     * @return                    true si la pioche a réussi, sinon false.
     */
    public boolean piocherCarteVisible(Player player, Couleur imagePiocheVisible) {
        return player.piocheCarteVisible(imagePiocheVisible);
    }

    /**
     * Pioche une carte de destination pour le joueur.
     *
     * @param player  Le joueur qui pioche la carte de destination.
     * @param cm      Le gestionnaire de cartes (CarteManager).
     * @param i       L'index de la carte de destination à piocher.
     * @return        true si la pioche a réussi, sinon false.
     */
    public boolean piocherCarteDestination(Player player, CarteManager cm, int i) {
        return player.piocheCarteDestination(cm, i);
    }

    /**
     * Pioche une carte invisible pour le joueur.
     *
     * @param player  Le joueur qui pioche la carte invisible.
     */
    public void piocherCarteInvisible(Player player) {
        player.piocheCarteInvisible();
    }

    /**
     * Affiche la description d'une carte de destination dans une boîte de dialogue.
     *
     * @param carteHover  La carte de destination dont la description doit être affichée.
     * @param game        Le jeu en cours.
     */
    public void descriptionCardDestination(CarteDestination carteHover, Game game) {
        JOptionPane.showMessageDialog(game.getGameFrame().getGameScreen().getGameManagerScreen().getGameMapPanel(),
                carteHover.getDescription(), "Carte Destination", JOptionPane.INFORMATION_MESSAGE);
    }
}