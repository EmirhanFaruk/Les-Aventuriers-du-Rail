package com.model.config;
import com.model.Game;
import com.model.config.carte.CarteWagon.Couleur;
import com.model.config.carte.CarteDestination;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * La classe Plateau représente le plateau de jeu.
 * Elle contient des méthodes pour gérer les cases du plateau telles que les rails, les villes et les paysages.
 */
public class Plateau {

    /**
     * La longueur du plateau.
     */
    /** Le tableau représentant les cases du plateau. */
    private Case[][] plateau;

    private ArrayList<Route> routesPlateau;

    private String nomMap;


    /**
     * Produire un plateau depuis un nom de map
     * @param  nomMap de fichier
     * @return le plateau depuis la carte donnee
     */
    public static Plateau makePlateau(String nomMap, Game game)
    {
        Plateau res = new Plateau(24,24);

        fillTab(res.getPlateau());

        // Opening the file
        BufferedReader reader = openFile(nomMap);
        if (reader == null) { return null; }

        // Reading from the file
        game.setVilles(new Ville[15]);
        String[][] stville = new String[15][];
        readFile(reader, stville);

        // Produire les villes
        produireVilles(game.getVilles(), stville, res);

        // Produire routes
        game.setRoutes(produireRoutes(game.getVilles(), stville, res));

        printTab(stville);

        printVilles(stville);

        res.printPlat();

        return res;
    }


    /**
     * Remplir les parties nulls du tableau avec des Paysages.
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
     * @return le tableau des villes
     */
    private static String[] delimit(String csvLine, char delimiter)
    {
        String[] res = new String[numDelimiter(csvLine, delimiter)];
        int resIndex = 0;

        String temp = "";
        for (int i = 0; i < csvLine.length(); i++)
        {
            if(csvLine.charAt(i) != ';')
            {
                temp = temp + csvLine.charAt(i);
            }
            else
            {
                res[resIndex] = temp;
                resIndex++;
                temp = "";
            }
        }

        return res;
    }

    /**
     * Retourne le nombre de delimiteur dans le string.
     * @param csvLine le string
     * @param delimiter le delimiteur
     * @return le nombre de delimiteur dans le string
     */
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


    /**
     * Ouvrir un fichier et retourne le reader
     * @param nomMap nom de fichier
     * @return
     */
    private static BufferedReader openFile(String nomMap)
    {
        BufferedReader reader;
        String path = System.getProperty("user.dir");
        String s = findSlash(path);
        try
        {
            reader = new BufferedReader(new FileReader(path + s + "ressources" + s + "maps" + s + nomMap + ".csv"));
        }
        catch (java.io.FileNotFoundException e)
        {
            System.out.println("Could not read the file.");
            return null;
        }
        return reader;
    }

    /**
     * Retourne le type de slash de systeme d'exploitation
     * @param p
     * @return
     */
    private static String findSlash(String p)
    {
        for(int i = 0; i < p.length(); i++)
        {
            switch (p.charAt(i))
            {
                case '/' : return "/";
                case '\\' : return "\\";
            }
        }
        return "/";
    }

    /**
     * Lire le fichier et mettre les donnees dans le tableau donne
     * @param reader
     * @param stville
     */
    private static void readFile(BufferedReader reader, String[][] stville)
    {
        // Checking the file
        String line;
        try
        {
            line = reader.readLine();
        }
        catch (java.io.IOException e)
        {
            System.out.println("The file is empty.");
            line = null;
        }
        char delimiter = ';';

        int index = 0;
        // Reading from the file and converting it to string tables
        while(line != null)
        {
            stville[index] = delimit(line, delimiter);

            try
            {
                line = reader.readLine();
            }
            catch (java.io.IOException e)
            {
                System.out.println("Ended reading the file.");
                line = null;
            }
            index++;
        }
    }

    private static void produireVilles(Ville[] villes, String[][] stville, Plateau plateau)
    {
        for (int i = 0; i < villes.length; i++)
        {
            // num ville, nom, x, y, (num de ville, type de rail, nombre de rail, angle des railes{90, 45, 0, 135}) * k
            int x = Integer.parseInt(stville[i][2]);
            int y = Integer.parseInt(stville[i][3]);
            String nom = stville[i][1];
            villes[i] = new Ville(x, y, nom);
            plateau.plateau[y][x] = new Ville(x, y, nom);
        }
    }

    private static ArrayList<Route> produireRoutes(Ville[] villes, String[][] stville, Plateau plateau)
    {
        ArrayList<Route> res = new ArrayList<>();

        for (String[] villet : stville)
        {
            if(villet.length > 4)
            {
                int i = 4;
                while (i + 4 < villet.length)
                {
                    // num ville, nom, x, y, (num de ville, type de rail, nombre de rail, angle des railes{90, 45, 0, 135}) * k
                    int nvil1 = Integer.parseInt(villet[0]);
                    int nvil2 = Integer.parseInt(villet[i]);
                    Ville ville1 = villes[nvil1 - 1];
                    Ville ville2 = villes[nvil2 - 1];
                    int longueur = Integer.parseInt(villet[i + 2]);
                    Rail.Content couleur = Rail.Content.values()[Integer.parseInt(villet[i + 1])];
                    int angle = Integer.parseInt(villet[i + 3]);


                    //Route(Ville ville1, Ville ville2, int longueur, Couleur couleur)
                    Route route = new Route(ville1, ville2, longueur, couleur);

                    putRails(ville1, ville2, longueur, couleur, angle, plateau);
                    res.add(route);
                    i += 4;
                }
            }
        }
        return res;
    }


    private static void putRails(Ville ville1, Ville ville2, int longueur, Rail.Content couleur, int angle, Plateau plateau)
    {
        int[] pos = new int[]{ville1.getX(), ville1.getY()};
        int[] destpos = new int[]{ville2.getX(), ville2.getY()};
        int[] angles = {90, 45, 0, 135};
        System.out.println();
        do
        {
            if (pos[0] > ville2.getX())
            {
                pos[0] = pos[0] - 1;
            }
            else if (pos[0] < ville2.getX())
            {
                pos[0] = pos[0] + 1;
            }

            if (pos[1] > ville2.getY())
            {
                pos[1] = pos[1] - 1;
            }
            else if (pos[1] < ville2.getY())
            {
                pos[1] = pos[1] + 1;
            }

            if (!(plateau.plateau[pos[1]][pos[0]] instanceof Ville))
            {
                System.out.println("Between " + ville1.getNom() + " and " + ville2.getNom() + ", putting rail at {" + pos[0] + ", " + pos[1] + "}");
                plateau.plateau[pos[1]][pos[0]] = new Rail(pos[0], pos[1], couleur, angles[angle]);
            }
            longueur--;
        } while(longueur > 0 && !(samePos(pos, destpos)));
    }

    private static boolean samePos(int[] t1, int[] t2)
    {
        return t1[0] == t2[0] && t1[1] == t2[1];
    }


    /**
     * Constructeur de la classe Plateau.
     * @param longueur La longueur du plateau.
     * @param largeur La largeur du plateau.
     */
    public Plateau(int longueur , int largeur)
    {
        plateau = new Case[longueur][largeur];
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

    public void cmp (){
        int cmpVille = 0 ;
        int cmpRail = 0 ;
        for ( Case[] cases : this.plateau ){
            for ( Case c : cases ){
                if ( c instanceof Ville ){
                    cmpVille ++ ;
                } else if ( c instanceof Rail ){
                    cmpRail++ ;
                }
            }
        }
        System.out.println("Le nombre de ville  : "+ cmpVille );
        System.out.println("Le nombre de rail : "+ cmpRail );
    }



    private void printPlat()
    {
        for (int i = 0; i < plateau.length; i++)
        {
            System.out.println();
            for (int j = 0; j < plateau[i].length; j++)
            {
                if (plateau[i][j] instanceof Ville)
                {
                    System.out.print(" V ");
                }
                else if (plateau[i][j] instanceof Rail)
                {
                    System.out.print(" R ");
                }
                else
                {
                    System.out.print(" _ ");
                }
            }
        }
    }

    private static void printVilles(String[][] stvilles)
    {
        String[] couleurtab = new String[]{"BLEU", "VIOLET", "MARRON", "NOIRE", "VERT", "JAUNE", "BLANC", "ROUGE", "JOKER", "JOKERETOILEE"};
        for (int i = 0; i < stvilles.length; i++)
        {
            println("\n-----------------\n" + stvilles[i][1] + ":", 0);
            println("Num: " + stvilles[i][0], 2);
            println("Name: " + stvilles[i][1], 2);
            println("X: " + stvilles[i][2], 2);
            println("Y: " + stvilles[i][3], 2);
            for (int j = 4; j < stvilles[i].length - 3; j = j + 4)
            {
                // num ville, nom, x, y, (num de ville, type de rail, nombre de rail, angle des railes{90, 45, 0, 135}) * k
                print("Dest Num: " + stvilles[i][j], 4);
                // {BLEU, VIOLET , MARRON , NOIRE , VERT , JAUNE , BLANC , ROUGE , JOKER , JOKERETOILEE}
                print("Type de rail: " + couleurtab[Integer.parseInt(stvilles[i][j + 1])], 4);
                print("Nb de rail: " + stvilles[i][j + 2], 4);
                //print("Angle de rail: " + stvilles[i][j + 3], 4);
                
                switch (stvilles[i][j + 3])
                {
                    case "0": print("Angle de rail: 90", 4); break;
                    case "1": print("Angle de rail: 45", 4); break;
                    case "2": print("Angle de rail: 0", 4); break;
                    case "3": print("Angle de rail: 135", 4); break;
                }
                System.out.println();
            }

        }
    }


    private static void indent(int n)
    {
        for (int i = 0; i < n; i++)
        {
            System.out.print(" ");
        }
    }

    private static void println(String s, int i)
    {
        indent(i);
        System.out.println(s);
    }

    private static void print(String s, int i)
    {
        indent(i);
        System.out.print(s);
    }

    private static void printTab(String[][] t)
    {
        for (int i = 0; i < t.length; i++)
        {
            System.out.println();
            for (int j = 0; j < t[i].length; j++)
            {
                System.out.print(t[i][j]);
                if (j < t[i].length - 1)
                {
                    System.out.print(", ");
                }
            }
        }
    }
}




