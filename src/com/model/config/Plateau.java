package com.model.config;
import com.model.Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * La classe Plateau représente le plateau de jeu.
 * Elle contient des méthodes pour gérer les cases du plateau telles que les rails, les villes et les paysages.
 */
public class Plateau {

    /** Le tableau représentant les cases du plateau. */
    private final Case[][] plateau;


    /**
     * Produire un plateau depuis un nom de map.
     * @param  nomMap de fichier
     * @return le plateau depuis la carte donnee
     */
    public static Plateau makePlateau(String nomMap, Game game)
    {
        // Opening the file
        BufferedReader reader = openFile(nomMap);
        if (reader == null) { return null; }

        // Reading from the file
        ArrayList<ArrayList<String>> stville = new ArrayList<>();
        int[] size = readFile(reader, stville);

        Plateau res = new Plateau(size[0], size[1]);

        fillTab(res.getPlateau());

        // Produire les villes
        game.setVilles(produireVilles(stville, res));

        // Produire routes
        game.setRoutes(produireRoutes(game.getVilles(), stville));

        // Set leur doubles/cousins
        setRouteCousins(game.getRoutes());

        // Mettre toutes les rails(oui je sais il est ecrit double rail rails)
        putDRRails(game.getRoutes(), res);


        return res;
    }


    /**
     * Remplir les parties nulls du tableau avec des Paysages.
     * @param tab le tableau dit
     */
    private static void fillTab(Case[][] tab)
    {
        int j = 0;
        for (int i = 0; i < tab[j].length; i++)
        {
            while (j < tab.length)
            {
                tab[j][i] = new Paysage(j, i);
                j++;
            }
            j = 0;
        }
    }

    /**
     * Retourne un tableau avec chaque element d'une ligne d'un fichier csv.
     * @param csvLine the line readen from the file
     * @return le tableau des villes
     */
    private static ArrayList<String> delimit(String csvLine, char delimiter)
    {
        int len = numDelimiter(csvLine, delimiter);
        ArrayList<String> res = new ArrayList<>();

        String temp = "";
        for (int i = 0; i < csvLine.length(); i++)
        {
            if(csvLine.charAt(i) != ';')
            {
                temp = temp + csvLine.charAt(i);
            }
            else
            {
                res.add(temp);
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
     * Ouvrir un fichier et retourne le reader.
     * @param nomMap nom de fichier
     * @return the reader of the file
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
     * Retourne le type de slash de systeme d'exploitation.
     * @param p any path that contains a / or \
     * @return the type of slash in String
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
     * Lire le fichier et mettre les donnees dans le tableau donne. Retourne la taille de plateau.
     * @param reader reader of the file
     * @param stville the list of list that the data will be written on
     */
    private static int[] readFile(BufferedReader reader, ArrayList<ArrayList<String>> stville)
    {
        int[] res = new int[2];

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

        boolean sizeLine = true;
        // Reading from the file and converting it to string tables
        while(line != null)
        {
            if (!sizeLine)
            {
                stville.add(delimit(line, delimiter));
            }
            else
            {
                ArrayList<String> size = delimit(line, delimiter);
                for (int i = 0; i < 2; i++)
                {
                    int l = Integer.parseInt(size.get(i));
                    res[i] = l;
                }
                sizeLine = false;
            }

            try
            {
                line = reader.readLine();
            }
            catch (java.io.IOException e)
            {
                System.out.println("Ended reading the file.");
                line = null;
            }
        }

        return res;
    }

    /**
     * Produire des villes depuis stville data et sauvegarde les dans plateau et villes
     * @param stville la liste de la liste a lire
     * @param plateau le plateau a sauvegarder
     */
    private static ArrayList<Ville> produireVilles(ArrayList<ArrayList<String>> stville, Plateau plateau)
    {
        ArrayList<Ville> res = new ArrayList<>();
        for (int i = 0; i < stville.size(); i++)
        {
            // num ville, nom, x, y, (num de ville, type de rail, nombre de rail, angle des railes{90, 45, 0, 135}) * k
            int x = Integer.parseInt(stville.get(i).get(2));
            int y = Integer.parseInt(stville.get(i).get(3)); //CHECK
            String nom = stville.get(i).get(1);
            System.out.println(x + ", " + y);
            plateau.plateau[x][y] = new Ville(x, y, nom);
            res.add(( Ville ) plateau.plateau[x][y]) ;
        }
        return res;
    }

    /**
     * Produire des routes et retourne les dans une ArrayList
     * @param villes la liste des villes a utiliser
     * @param stville la liste de la liste a lire
     * @return la liste des routes produits depuis stville
     */
    private static ArrayList<Route> produireRoutes(ArrayList<Ville> villes, ArrayList<ArrayList<String>> stville)
    {
        ArrayList<Route> res = new ArrayList<>();

        for (ArrayList<String> villet : stville)
        {
            // Faire le taff si la longueur est plus que 4,
            // car jusqu'a 4 il y a que l'info de ville et pas ses connections
            if(villet.size() > 4)
            {
                int i = 4;
                // Avec des boucles de 4, on lit chaque connection entre les villes
                while (i + 4 < villet.size() && !villet.get(i).isEmpty())
                {
                    // num ville, nom, x, y, (num de ville, type de rail, nombre de rail, angle des railes{90, 45, 0, 135}) * k
                    // Sauvegarder les nums des villes
                    int nvil1 = Integer.parseInt(villet.get(0));
                    int nvil2 = Integer.parseInt(villet.get(i));
                    // Avoir les villes pour sauvegarder dans la route
                    Ville ville1 = villes.get(nvil1 - 1);
                    Ville ville2 = villes.get(nvil2 - 1);
                    // Garder les infos necessaires pour la route
                    int longueur = Integer.parseInt(villet.get(i + 2));
                    Rail.Content couleur = Rail.Content.values()[Integer.parseInt(villet.get(i + 1))];


                    // Faire la route
                    Route route = new Route(ville1, ville2, longueur, couleur);

                    // Ajouter la route aux villes concernés
                    ville1.getRoutes().add(route);
                    ville2.getRoutes().add(route);

                    // Ajouter la resultat dans la liste a retourner
                    res.add(route);
                    i += 4;
                }
            }
        }
        return res;
    }


    /**
     * Produire et mettre des rails dans le plateau avec les infos données
     * en appelant putRailsPos avec les positions des villes.
     * @param ville1 ville 1
     * @param ville2 ville 2
     * @param longueur nombre des rails a mettre
     * @param couleur couleur des rails
     * @param angle angle des rails
     * @param plateau le plateau a sauvegarder dans
     */
    private static void putRails(Ville ville1, Ville ville2, int longueur, Rail.Content couleur, int angle, Route route, Plateau plateau)
    {
        int[] v1pos = {ville1.getX(), ville1.getY()};
        int[] v2pos = {ville2.getX(), ville2.getY()};
        putRailsPos(v1pos, v2pos, longueur, couleur, angle, route, plateau);
    }

    /**
     * Produire et mettre des rails dans le plateau avec les infos données
     * @param v1pos position de ville 1
     * @param v2pos position de ville 2
     * @param longueur nombre des rails a mettre
     * @param couleur couleur des rails
     * @param angle angle des rails
     * @param plateau le plateau a sauvegarder dans
     */
    private static void putRailsPos(int[] v1pos, int[] v2pos, int longueur, Rail.Content couleur, int angle, Route route, Plateau plateau)
    {
        int
                x1 = v1pos[0], x2 = v2pos[0],
                y1 = v1pos[1], y2 = v2pos[1];
        int[] pos = new int[]{x1, y1};
        int[] destpos = new int[]{x2, y2};
        int[] angles = {90, 45, 0, 135};
        while(longueur > 0)
        {
            // Fix de Salim
            if (!(plateau.plateau[pos[0]][pos[1]] instanceof Ville))
            {
                Rail rail = new Rail(pos[0], pos[1], couleur, angles[angle]);
                rail.setSaRoute(route);
                route.getRailsRoute().add(rail);
                plateau.plateau[pos[0]][pos[1]] = rail;
            }

            if (pos[0] > destpos[0])
            {
                pos[0] = pos[0] - 1;
            }
            else if (pos[0] < destpos[0])
            {
                pos[0] = pos[0] + 1;
            }

            if (pos[1] > destpos[1])
            {
                pos[1] = pos[1] - 1;
            }
            else if (pos[1] < destpos[1])
            {
                pos[1] = pos[1] + 1;
            }

            longueur--;
        }
    }


    /**
     * Faire connaitre les doubles(cousins) routes
     * @param routes la liste des routes a se faire connaitre
     */
    private static void setRouteCousins(ArrayList<Route> routes)
    {
        for (int i = 0; i < routes.size(); i++)
        {
            Route route = routes.get(i);
            for (int j = 0; j < routes.size(); j++)
            {
                Route temp = routes.get(j);
                if (i != j)
                {
                    if (route.getCousin() == null)
                    {
                        boolean possibility1 = route.getVille1() == temp.getVille1() && route.getVille2() == temp.getVille2();
                        boolean possibility2 = route.getVille1() == temp.getVille2() && route.getVille2() == temp.getVille1();
                        if (possibility1 || possibility2)
                        {
                            route.setCousin(temp);
                            temp.setCousin(route);
                        }
                    }
                }
            }
        }
    }


    /**
     * Avoir angle d'une route depuis ses villes
     * @param ville1 ville 1
     * @param ville2 ville 2
     * @return angle depuis la liste
     */
    private static int getAngle(Ville ville1, Ville ville2)
    {
        // angles = {90, 45, 0, 135}
        int x1 = ville1.getX(),
                x2 = ville2.getX(),
                y1 = ville1.getY(),
                y2 = ville2.getY();

        if (x1 == x2 && y1 != y2)
        {
            return 0;
        }
        if (x1 != x2 && y1 == y2)
        {
            return 2;
        }
        if ((x1 > x2 && y1 < y2) || (x1 < x2 && y1 > y2))
        {
            return 1;
        }
        if ((x1 < x2 && y1 < y2) || (x1 > x2 && y1 > y2))
        {
            return 3;
        }

        return 0;
    }

    /**
     * Verifier si un route existe dans une array(pas avec leur proprietes, directement)
     * @param route la route a comparer
     * @param arr la liste des routes
     * @return le resultat
     */
    private static boolean exists(Route route, ArrayList<Route> arr)
    {
        for (Route i : arr)
        {
            if (route == i)
            {
                return true;
            }
        }
        return false;
    }


    /**
     * Mettre les doubles routes dans plateau
     * @param routes la liste des routes
     * @param plateau le plateau a mettre sur
     */
    private static void putDRRails(ArrayList<Route> routes, Plateau plateau)
    {
        ArrayList<Route> doubles = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++)
        {
            Ville v1 = routes.get(i).getVille1();
            Ville v2 = routes.get(i).getVille2();
            int longueur = routes.get(i).getLongueur();

            if (routes.get(i).getCousin() != null && !exists(routes.get(i), doubles))
            {
                putDoubleRails(v1, v2, routes.get(i), routes.get(i).getCousin(), plateau);
                doubles.add(routes.get(i));
                doubles.add(routes.get(i).getCousin());
            }
            else if (routes.get(i).getCousin() == null)
            {
                // Mettre des rails pour la route
                putRails(v1, v2, longueur + 1, routes.get(i).getCouleur(), getAngle(v1, v2), routes.get(i), plateau);
                // length + 1 because putting rails will start from the ville
            }
        }
    }

    /**
     * Mettre des doubles rails entre deux villes
     * @param ville1 ville 1
     * @param ville2 ville 2
     * @param route1 route 1
     * @param route2 route 2
     * @param plateau le plateau a sauvegarder dans
     */
    private static void putDoubleRails(Ville ville1, Ville ville2, Route route1, Route route2, Plateau plateau)
    {
        int x1 = ville1.getX(),
            x2 = ville2.getX(),
            y1 = ville1.getY(),
            y2 = ville2.getY();

        Rail.Content couleur1 = route1.getCouleur();
        Rail.Content couleur2 = route2.getCouleur();
        int angle = getAngle(route1.getVille1(), route1.getVille2());

        int longueur;
        if (angle == 1 || angle == 3)
        {
            longueur = route1.getLongueur() + 1; // Car on va pas utiliser la distance direct entre deux villes
        }
        else
        {
            longueur = route1.getLongueur();
        }



        // There will be lots of ifs and elses
        // 0  1  2
        // 3  V  4
        // 5  6  7

        // I know this is not the best way to do it
        int[][] positions1 =
                {
                        {x1 - 1, y1 - 1},
                        {x1, y1 - 1},
                        {x1 + 1, y1 - 1},
                        {x1 - 1, y1},
                        {x1 + 1, y1},
                        {x1 - 1, y1 + 1},
                        {x1, y1 + 1},
                        {x1 + 1, y1 + 1}
                };
        int[][] positions2 =
                {
                        {x2 - 1, y2 - 1},
                        {x2, y2 - 1},
                        {x2 + 1, y2 - 1},
                        {x2 - 1, y2},
                        {x2 + 1, y2},
                        {x2 - 1, y2 + 1},
                        {x2, y2 + 1},
                        {x2 + 1, y2 + 1}
                };

        // angles = {90, 45, 0, 135}
        if (angle == 0)
        {
            // 90 degrees, upwards and downwards
            if (ville1.getY() > ville2.getY())
            {
                // if ville1 below ville2, then put the rails starting from 0 and 2, ending at 5 and 7
                putRailsPos(positions1[0], positions2[5], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[2], positions2[7], longueur, couleur2, angle, route2, plateau);
            }
            else
            {
                // if not then the numbers are switched
                putRailsPos(positions1[5], positions2[0], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[7], positions2[2], longueur, couleur2, angle, route2, plateau);

            }
        } // I hate myself for not using switch
        else if (angle == 1)
        {
            // 45 degrees, diagonal up right or down left
            // thankfully, only need to check one of x or y this time
            if (ville1.getY() > ville2.getY())
            {
                // if ville1 below left ville2, then put the rails starting from 1 and 4, ending at 3 and 6
                putRailsPos(positions1[1], positions2[3], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[4], positions2[6], longueur, couleur2, angle, route2, plateau);
            }
            else
            {
                // if not then the numbers are switched
                putRailsPos(positions1[3], positions2[1], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[6], positions2[4], longueur, couleur2, angle, route2, plateau);
            }
        }
        else if (angle == 2)
        {
            // 0 degrees, left or right
            if (ville1.getX() > ville2.getX())
            {
                // if ville1 is at the right of ville2, then put the rails starting from 0 and 5, ending at 2 and 7
                putRailsPos(positions1[0], positions2[2], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[5], positions2[7], longueur, couleur2, angle, route2, plateau);
            }
            else
            {
                // if not then the numbers are switched
                putRailsPos(positions1[2], positions2[0], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[7], positions2[5], longueur, couleur2, angle, route2, plateau);
            }
        }
        else if (angle == 3)
        {
            // 135 degrees, diagonal up left or down right
            // thankfully, only need to check one of x or y again
            if (ville1.getY() > ville2.getY())
            {
                // if ville1 below left ville2, then put the rails starting from 3 and 1, ending at 6 and 4
                putRailsPos(positions1[3], positions2[6], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[1], positions2[4], longueur, couleur2, angle, route2, plateau);
            }
            else
            {
                // if not then the numbers are switched
                putRailsPos(positions1[6], positions2[3], longueur, couleur1, angle, route1, plateau);
                putRailsPos(positions1[4], positions2[1], longueur, couleur2, angle, route2, plateau);
            }
        }

        // Finally done with this abomination, will hopefully come back to optimize later ^^ (probably never)
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
        return !(y > this.getLargeur() || x > this.getLongueur() || x < 0 || y < 0);
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




