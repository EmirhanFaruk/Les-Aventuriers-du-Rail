package config;

/**
 * La classe Plateau représente le plateau de jeu.
 * Elle contient des méthodes pour gérer les cases du plateau telles que les rails, les villes et les paysages.
 */
public class Plateau {
    
    /** La longueur du plateau. */
    private int longueurP;
    
    /** La largeur du plateau. */
    private int largeurP;
    
    /** Le tableau représentant les cases du plateau. */
    private Case[][] plateau;
    
    
    public static Plateau creerPlateauDepuisFichier(String nomFichier) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(nomFichier));

        String line;

        // Lire la première ligne pour déterminer la longueur du plateau
        if ((line = reader.readLine()) != null) {
        	longueurP = line.length();
        	largeurP++;
        }

        // Lire les lignes suivantes pour déterminer la largeur du plateau
        while ((line = reader.readLine()) != null) {
        	largeurP++;
        }

        reader.close();

        Plateau plateau = new Plateau(longueurP, largeurP);
        reader = new BufferedReader(new FileReader(nomFichier));

        int x = 0;
        int y = 0;

        // Lire à nouveau le fichier pour créer les cases du plateau
        while ((line = reader.readLine()) != null) {
            for (char c : line.toCharArray()) {
                switch (c) {
                    case '1':
                        plateau.getPlateau()[x][y] = new Case(x, y);
                        break;
                    case '2':
                        plateau.getPlateau()[x][y] = new Rail(x, y, Rail.Content.NOIR); // couleur par défaut
                        break;
                    case '3':
                        plateau.getPlateau()[x][y] = new Ville(x, y, "Ville"); // Nom par défaut
                        break;
                    default:
                        throw new IllegalArgumentException("Caractère invalide dans le fichier de carte: " + c);
                }
                x++;
            }
            x = 0;
            y++;
        }

        reader.close();
        return plateau;
    }
    
    
    /**
     * Constructeur de la classe Plateau.
     * @param x La longueur du plateau.
     * @param y La largeur du plateau.
     */
    public Plateau() {
        this.plateau = creerPlateauDepuisFichier("/resources/Map.txt");
    }
    
    /**
     * Obtient la largeur du plateau. 
     * @return La largeur du plateau.
     */
    public int getLargeur() {
        return plateau.length;
    }
    
    /**
     * Obtient la longueur du plateau.
     * @return La longueur du plateau.
     */
    public int getLongueur() {
        return plateau[0].length;
    }
    
    /**
     * Obtient le tableau représentant les cases du plateau.
     * @return Le tableau représentant les cases du plateau.
     */
    public Case[][] getPlateau(){
        return plateau;
    }
    
    /**
     * Vérifie si une position donnée est valide sur le plateau.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @return true si la position est valide, sinon false.
     */
    public boolean positionValide(int x, int y) {
        return !(y >= this.getLargeur() || x >= this.getLongueur() || x < 0  || y < 0);
    }
    
    /**
     * Vérifie si la case à la position spécifiée est un rail.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @return true si la case est un rail, sinon false.
     */
    public boolean estUneCaseRail(int x, int y) {
        return plateau[x][y] instanceof Rail;
    }
    
    /**
     * Vérifie si la case à la position spécifiée est une ville.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @return true si la case est une ville, sinon false.
     */
    public boolean estUneCaseVille(int x, int y) {
        return plateau[x][y] instanceof Ville;
    }
    
    /**
     * Vérifie si la case à la position spécifiée est une gare.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @return true si la case est une gare, sinon false.
     */
    public boolean estUneCaseGare(int x, int y) {
        if (estUneCaseVille(x, y)) {
            return ((Ville) plateau[x][y]).estUneCaseGare();
        }
        return false;
    }
    
    /**
     * Vérifie si la case à la position spécifiée est un paysage.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @return true si la case est un paysage, sinon false.
     */
    public boolean estUneCasePaysage(int x, int y) {
        return !this.estUneCaseVille(x, y) && !this.estUneCaseRail(x,y);
    }
}