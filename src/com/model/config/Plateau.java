package com.model.config;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

        int longueur = 0;
        int largeur = 0;

        // Lire la première ligne pour déterminer la longueur du plateau
        if ((line = reader.readLine()) != null) {
            longueur = line.length();
        }

        // Lire les lignes suivantes pour déterminer la largeur du plateau
        while ((line = reader.readLine()) != null) {
            largeur++;
        }

        reader.close();
        
        this.longueurP = longueur;
        this.largeurP = largeur;
        Plateau plateau = new Plateau(longueur, largeur);
        reader = new BufferedReader(new FileReader(nomFichier));

        int x = 0;
        int y = 0;

        // Lire à nouveau le fichier pour créer les cases du plateau
        /*LEGENDE : V = ville, C = case vide, R = rail + {Bleu = B, Jaune = J, Blanc = W,
        Marron = M, Vert = G, Noir = N, Violet = P et Rouge = K} + {_ = 0°, \ = 45°, | = 90° et / = 135°}*/
        while ((line = reader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                switch (c) {
                    case 'C':
                        plateau.getPlateau()[x][y] = new Case(x, y);
                        break;
                    case 'R':
                    	char couleur = line.charAt(i+1); // Lire le caractère suivant pour obtenir la couleur
                        char angle = line.charAt(i+2); // Lire le deuxième caractère suivant pour obtenir l'angle
                        plateau.getPlateau()[x][y] = new Rail(x, y, raiLCouleur(couleur), raiLAngle(angle));
                        i += 2; // Avancer de deux caractères supplémentaires
                        break;
                    case 'V':
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
    
    //METHODE COULEUR
    Content raiLCouleur(char c) {
    	if(c == 'B') {
    		return BLEU;
        }else if(c == 'J') {
        	return JAUNE;
        }else if(c == 'W') {
        	return BLANC;
        }else if(c == 'M') {
        	return MARRON;
        }else if(c == 'G') {
        	return VERT;
        }else if(c == 'N') {
        	return NOIR;
        }else if(c == 'P') {
        	return VIOLET;
        }else{
        	return ROUGE;
        }
    }
    
    //METHODE ANGLE
    int raiLAngle(char angle) {
    	if(angle == '_') {
        	return 0;
        }else if(angle == '\'){
        	return 45;
		}else if(angle == '|') {
			return 90
		}else {
			return 135;
		}
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