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
    public enum Content {BLEU, VIOLET, MARRON, NOIRE, VERT, JAUNE, BLANC, ROUGE, JOKER, JOKERETOILEE}

    /** La couleur initiale du rail. */
    private Content initialContent;

    /** L'angle du rail. */
    private final int angle;

    /** Un indicateur indiquant si le rail est occupé ou non. */
    private Player occuperPar;

    /** La route à laquelle le rail appartient. */
    private Route saRoute;

    /**
     * Constructeur de la classe Rail.
     *
     * @param x     La position horizontale du rail sur le plateau de jeu.
     * @param y     La position verticale du rail sur le plateau de jeu.
     * @param c     La couleur initiale du rail.
     * @param angle L'angle du rail.
     */
    public Rail(int x, int y, Content c, int angle) {
        super(x, y);
        this.initialContent = c;
        this.angle = angle;
        this.saRoute = null;
    }

    /**
     * Constructeur de la classe Rail avec une route associée.
     *
     * @param i      La position horizontale du rail sur le plateau de jeu.
     * @param j      La position verticale du rail sur le plateau de jeu.
     * @param couleur La couleur initiale du rail.
     * @param k      L'angle du rail.
     * @param r      La route à laquelle le rail appartient.
     */
    public Rail(int i, int j, Content couleur, int k, Route r) {
        super(i, j);
        this.initialContent = couleur;
        this.angle = k;
        this.saRoute = r;
    }

    /* Getters et Setters */

    /**
     * Obtient la couleur initiale du rail.
     *
     * @return La couleur initiale du rail.
     */
    public Content getInitialContent() {
        return initialContent;
    }

    /**
     * Obtient l'état d'occupation du rail.
     *
     * @return true si le rail est occupé, sinon false.
     */
    public boolean getOccuper() {
        return occuperPar != null;
    }

    /**
     * Vérifie si la case est une gare.
     *
     * @return false, car une case de rail ne peut pas être une gare, il faut qu'elle soit une ville pour être une gare.
     */
    @Override
    public boolean estUneCaseGare() {
        return false;
    }

    /**
     * Obtient l'angle du rail.
     *
     * @return L'angle du rail.
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Obtient la route à laquelle le rail appartient.
     *
     * @return La route à laquelle le rail appartient.
     */
    public Route getSaRoute() {
        return saRoute;
    }

    /**
     * Définit la route à laquelle le rail appartient.
     *
     * @param r La route à laquelle le rail appartient.
     */
    public void setSaRoute(Route r) {
        this.saRoute = r;
    }

    /**
     * Définit l'occupant du rail.
     *
     * @param o L'occupant du rail.
     */
    public void setOccuperPar2(Player o) {
        this.occuperPar = o;
    }

    /**
     * Obtient l'occupant du rail.
     *
     * @return L'occupant du rail.
     */
    public Player getOccuperPar() {
        return occuperPar;
    }

    /**
     * Définit l'occupant du rail si le rail n'est pas déjà occupé.
     *
     * @param occuperPar L'occupant du rail.
     */
    public void setOccuperPar(Player occuperPar) {
        if (!getOccuper()) {
            this.occuperPar = occuperPar;
        }
    }
}
