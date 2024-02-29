package com.model.config;

import com.model.config.Rail.Content;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * La classe Plateau représente le plateau de jeu.
 * Elle contient des méthodes pour gérer les cases du plateau telles que les rails, les villes et les paysages.
 */
public class Plateau {

    /** Le tableau représentant les cases du plateau. */
    private Case[][] plateau;


    /**
     * Produire un plateau depuis un nom de map
     * @param nomMap nom de fichier
     * @return
     * @throws FileNotFoundException
     */
    private static Plateau makePlateau(String nomMap)
    {
        Plateau res = new Plateau(24,24);

        fillTab(res.getPlateau());

        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(nomMap + ".csv"));
        }
        catch (java.io.FileNotFoundException e)
        {
            System.out.println("Could not read the file.");
            return null;
        }

        String line;
        try
        {
            line = reader.readLine();
        }
        catch (java.io.IOException e)
        {
            System.out.println(nomMap + ".csv is empty.");
            line = null;
        }
        char delimiter = ';';
        Ville[] villes = new Ville[15];


        while(line != null)
        {



            try
            {
                line = reader.readLine();
            }
            catch (java.io.IOException e)
            {
                System.out.println("Ended reading " + nomMap + ".csv.");
                line = null;
            }
        }


        return res;
    }


    /**
     * remplir le tableau avec des Paysages.
     * @param tab le tableau dit
     */
    private static void fillTab(Case[][] tab)
    {
        for (int i = 0; i < tab.length; i++)
        {
            for (int j = 0; j < tab[i].length; j++)
            {
                tab[i][j] = new Paysage(j, i);
            }
        }
    }

    /**
     * Retourne un tableau avec chaque element d'une ligne d'un fichier csv
     * @param csvLine
     * @return
     */
    private static String[] delimit(String csvLine, char delimiter)
    {

        return null;
    }

    private static int numDelimiter(String csvLine, char delimiter)
    {
        int res = 0;
        for (int i = 0; i < csvLine.length(); i++)
        {
            if (csvLine.charAt(i) == delimiter)
            {
                res++;
            }
        }

        return res;
    }




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

        Plateau plateau = new Plateau(longueur, largeur);
        reader = new BufferedReader(new FileReader(nomFichier));

        int x = 0;
        int y = 0;

        // Lire à nouveau le fichier pour créer les cases du plateau
        while ((line = reader.readLine()) != null) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                switch (c) {
                    case 'C':
                        plateau.plateau[x][y] = new Paysage(x, y);
                        break;
                    case 'R':
                        char couleur = line.charAt(i + 1); // Lire le caractère suivant pour obtenir la couleur
                        char angle = line.charAt(i + 2); // Lire le deuxième caractère suivant pour obtenir l'angle
                        plateau.plateau[x][y] = new Rail(x, y, raiLCouleur(couleur), raiLAngle(angle));
                        i += 2; // Avancer de deux caractères supplémentaires
                        break;
                    case 'V':
                        plateau.plateau[x][y] = new Ville(x, y, "Ville"); // Nom par défaut
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
    static Content raiLCouleur(char c) {
        if (c == 'B') {
            return Content.BLEU;
        } else if (c == 'J') {
            return Content.JAUNE;
        } else if (c == 'W') {
            return Content.BLANC;
        } else if (c == 'M') {
            return Content.MARRON;
        } else if (c == 'G') {
            return Content.VERT;
        } else if (c == 'N') {
            return Content.NOIR;
        } else if (c == 'P') {
            return Content.VIOLET;
        } else {
            return Content.ROUGE;
        }
    }

    //METHODE ANGLE
    static int raiLAngle(char angle) {
        if (angle == '-') {
            return 0;
        } else if (angle == '\\') {
            return 45;
        } else if (angle == '|') {
            return 90;
        } else {
            return 135;
        }
    }

    /**
     * Constructeur de la classe Plateau.
     * @param longueur La longueur du plateau.
     * @param largeur La largeur du plateau.
     */
    public Plateau(int longueur, int largeur) {
        this.plateau = new Case[longueur][largeur];
    }

    /**
     * Obtient la largeur du plateau.
     * @return La largeur du plateau.
     */
    public int getLargeur() {
        return plateau[0].length;
    }

    /**
     * Obtient la longueur du plateau.
     * @return La longueur du plateau.
     */
    public int getLongueur() {
        return plateau.length;
    }

    /**
     * Obtient le tableau représentant les cases du plateau.
     * @return Le tableau représentant les cases du plateau.
     */
    public Case[][] getPlateau() {
        return plateau;
    }

    /**
     * Vérifie si une position donnée est valide sur le plateau.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @return true si la position est valide, sinon false.
     */
    public boolean positionValide(int x, int y) {
        return !(y >= this.getLargeur() || x >= this.getLongueur() || x < 0 || y < 0);
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
        return !this.estUneCaseVille(x, y) && !this.estUneCaseRail(x, y);
    }
}