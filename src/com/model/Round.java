package com.model;

import com.model.bot.NormalBot;
import com.model.bot.StrongBot;
import com.model.bot.WeakBot;
import com.view.GameMapPanel;
import com.view.PlayerHandPanel;

/**
 * Classe représentant un tour de jeu.
 */
public class Round {
    private int whoIsPlaying = 0; // Le joueur actuellement en train de jouer
    private boolean endTurn = false; // Indique si le tour est terminé
    private int action = 2; // Le nombre d'actions restantes pour piocher une carte wagon
    private WeakBot weakBotPlay = new WeakBot();
    private NormalBot normalBotPlay = new NormalBot();
    private StrongBot strongBotPlay = new StrongBot();

    private final double betweenRoundTimerMax = 0.5; // Valeur maximale du timer
    private double betweenRoundTimer = 0; // Timer pour montrer ce qui se passe quand les bots jouent

    /**
     * Retourne l'index du joueur actuellement en train de jouer.
     *
     * @return L'index du joueur actuel.
     */
    public int getWhoIsPlaying() {
        return whoIsPlaying;
    }

    /**
     * Retourne le nombre d'actions restantes.
     *
     * @return Le nombre d'actions restantes.
     */
    public int getAction() {
        return action;
    }

    /**
     * Définit le nombre d'actions restantes.
     *
     * @param action Le nombre d'actions restantes.
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * Définit si le tour est terminé.
     *
     * @param endTurn true si le tour est terminé, sinon false.
     */
    public void setEndTurn(boolean endTurn) {
        this.endTurn = endTurn;
    }

    /**
     * Vérifie si le tour est terminé.
     *
     * @return true si le tour est terminé, sinon false.
     */
    public boolean roundFinished() {
        return this.endTurn;
    }

    /**
     * Termine le tour en cours et passe au joueur suivant.
     *
     * @param game Le jeu en cours.
     */
    public void endRound(Game game) {
        // Fonction qui finit le tour du bot
        setEndTurn(true);
        
        // Piocher une carte destination comptera comme une action maintenant
        game.getJoueurCourant().setFirstTurnOver(true);
        
        // Détermine le prochain joueur
        whosNext(game);
        Player joueur = game.getJoueurCourant();

        // Accès au GameMapPanel
        GameMapPanel gameMapPanel = game.getGameMapPanel();

        // Accès au PlayerHandPanel
        PlayerHandPanel playerHandPanel = gameMapPanel.getPlayerHandPanel();
               
        // Reroll les cartes destinations pour le prochain joueur
        game.getCarteManager().rerollDestination();

        // Change le joueur courant
        gameMapPanel.setPlayerCourant(joueur);
        
        // Change toutes les images pour le nouveau joueur
        gameMapPanel.getMapScreen().repaintAll(playerHandPanel);

        // Repaint la map à chaque fois
        game.getMapScreen().repaint();

        betweenRoundTimer = betweenRoundTimerMax;
        // DEBUG : System.out.println(whoIsPlaying);
    }

    /**
     * Détermine qui est le prochain joueur à jouer.
     *
     * @param game Le jeu en cours.
     */
    public void whosNext(Game game) {
        // Passe au prochain joueur

        // Réinitialise le tour
        this.endTurn = false;
        this.action = 2;

        // Change de joueur
        if (whoIsPlaying == game.getListPlayer().size() - 1) {
            whoIsPlaying = 0;
        } else {
            whoIsPlaying++;
        }
    }

    /**
     * Gère le déroulement du tour de jeu.
     *
     * @param game      Le jeu en cours.
     * @param deltaTime Le temps écoulé depuis la dernière mise à jour.
     */
    public void round(Game game, double deltaTime) {
        Player joueur = game.getListPlayer().get(whoIsPlaying);
        if (betweenRoundTimer <= 0) {
            switch (joueur.getNiveau()) {
                case 1:
                    weakBotPlay.play(game);
                    break;
                case 2:
                    normalBotPlay.play(game);
                    break;
                case 3:
                    strongBotPlay.play(game);
                    break;
                default:
                    break;
            }
        } else {
            if (joueur.getNiveau() != 0) {
                betweenRoundTimer -= deltaTime;
            } else {
                betweenRoundTimer = betweenRoundTimerMax;
            }
        }
    }
}
