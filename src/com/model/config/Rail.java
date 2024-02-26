package src.com.model.config;

/**
 * La classe Rail représente une case de type rail.
 * Elle hérite de la classe Case et ajoute des fonctionnalités spécifiques
 * aux rails, tels que la couleur initiale et la possibilité d'être occupé.
 */
public class Rail extends Case {

    /**
     * L'énumération Content représente les différentes couleurs de rail possibles.
     */
    public enum Content {BLEU, VIOLET , MARRON , NOIR , VERT , JAUNE , ROUGE , BLANC , JOKER , JOKERETOILEE}

    /** La couleur initiale du rail. */
    private Content initialContent;

    /** Un indicateur indiquant si le rail est occupé ou non. */
    private boolean occuper;
    
    private final int angle;

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
    }
    
    public int getAngle() {
        return angle;
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
}