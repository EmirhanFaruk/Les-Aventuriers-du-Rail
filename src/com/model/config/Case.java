package src.com.model.config;

/**
 * La classe Case représente une case générique.
 * Cette classe est une classe de base pour d'autres types de cases spécifiques.
 */
public abstract class Case {

    /** La position horizontale de la case sur le plateau de jeu. */
    private final int x;

    /** La position verticale de la case sur le plateau de jeu. */
    private final int y;

    /**
     * Constructeur de la classe Case.
     * @param x La position horizontale de la case sur le plateau de jeu.
     * @param y La position verticale de la case sur le plateau de jeu.
     */
    public Case(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Obtient la position horizontale de la case sur le plateau de jeu.
     * @return La position horizontale de la case.
     */
    public int getX() {
        return x;
    }

    /**
     * Obtient la position verticale de la case sur le plateau de jeu.
     * @return La position verticale de la case.
     */
    public int getY() {
        return y;
    }

    /**
     * Méthode abstraite pour déterminer si la case est une gare ou non.
     * @return true si la case est une gare, sinon false.
     */
    public abstract boolean estUneCaseGare();
}