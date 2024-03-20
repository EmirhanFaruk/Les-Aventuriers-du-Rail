package com.model.config;

import com.model.Player;

/**
 * La classe Rail représente une case de type rail.
 * Elle hérite de la classe Case et ajoute des fonctionnalités spécifiques
 * aux rails, tels que la couleur initiale et la possibilité d'être occupé.
 */
public class Rail extends Case {

    /**
     * L'énumération Content représente les différentes couleurs de rail possibles.
     */
    public enum Content {BLEU, VIOLET , MARRON , NOIRE , VERT , JAUNE , BLANC , ROUGE , JOKER , JOKERETOILEE}

    /** La couleur initiale du rail. */
    private Content initialContent;

    private final int angle ;
    
    /** Un indicateur indiquant si le rail est occupé ou non. */
    private Player occuperPar;

    /** Un indicateur indiquant si le rail est occupé ou non. */
    private boolean occuper;
    
    private Route saRoute;


    /**
     * Constructeur de la classe Rail.
     * @param x La position horizontale du rail sur le plateau de jeu.
     * @param y La position verticale du rail sur le plateau de jeu.
     * @param c La couleur initiale du rail.
     */
    public Rail(int x, int y, Content c, int angle) {
        super(x, y);
        this.initialContent = c;
        this.angle = angle;
        this.saRoute = null;
    }
    
    public Rail(int i, int j, Content couleur, int k, Route r) {
		super(i, j);
		this.initialContent = couleur;
		this.angle = k;
		this.saRoute = r;
	}

	/**
     * Obtient la couleur initiale du rail.
     * @return La couleur initiale du rail.
     */
    public Content getInitialContent() {
        return initialContent;
    }

    /**
     * Obtient l'état d'occupation du rail.
     * @return true si le rail est occupé, sinon false.
     */
    public boolean getOccuper() {
        return occuper;
    }
    
    /**
     * Définit l'état d'occupation du rail.
     * @param o true si le rail est occupé, sinon false.
     */
    public void setOccuper(boolean o) {
        this.occuper = o;
    }

    /**
     * Vérifie si la case est une gare.
     * @return false, car une case de rail ne peut pas être une gare, il faut qu'elle soit une ville pour être une gare.
     */
    public boolean estUneCaseGare() {
        return false;
    }

    public int getAngle() {
        return angle;
    }

	public Route getSaRoute() {
		return saRoute;
	}

	public void setSaRoute(Route r) {
		this.saRoute = r;
	}
	
	/**
     * Renvoie quelle joueur a cette rail
     * @return Player
     */
    public Player getOccuperPar() {
        return occuperPar;
    }

    /**
     * Definir le joueur a quui appartient cette rail
     * @param occuperPar Player
     */
    public void setOccuperPar(Player occuperPar) {
        if ( ! this.occuper ) {
            this.occuperPar = occuperPar;
        }
    }
}