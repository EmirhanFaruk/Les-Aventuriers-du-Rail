# Les aventuriers du rail

--------------------------------------

## Présentation
**Aventuriers du Rail** est un jeu de société où les joueurs construisent des réseaux ferroviaires à travers
différents paysages. Utilisez des cartes pour relier les villes et gagnez des points en complétant des routes. 
Avec des règles simples, mais une stratégie profonde, ce jeu offre une expérience captivante pour les joueurs 
de tous niveaux.

## Règle du Jeu
Il vous faut construire un réseau ferroviaire rentable en reliant les villes sur le plateau de jeu.
Les joueurs gagnent des points en complétant des routes entre les villes et en atteignant les
destinations spécifiées sur leurs cartes Destination. À la fin de la partie, le joueur avec
le plus de points est déclaré vainqueur.

## Comment Jouer
Tout d'abord, le nombre de joueurs et de bot. 
Ensuite, il vous faudra choisir une map et le mode de jeu.
Voici un guide pour jouer à "Aventuriers du Rail" :

1. **Mise en place** :
    - Disposez le plateau de jeu au centre de la table.
    - Distribuez à chaque joueur des wagons de couleur et des cartes Destination.
    - Mélangez les cartes Wagon et placez-en 5 face visible à côté du plateau.
    - Chaque joueur reçoit également 4 cartes Wagon et 3 cartes Destination.

2. **Tour de jeu** :
    - À son tour, un joueur peut réaliser une des actions suivantes :
        - Piocher 2 cartes Wagon (ou une carte Wagon face visible et une face cachée).
        - Réclamer une route en jouant un ensemble de cartes Wagon de même couleur correspondant à la longueur de la route.
        - Piocher 3 nouvelles cartes Destination et en garder au moins une.

3. **Construire des routes** :
    - Pour relier deux villes sur le plateau, le joueur doit poser ses wagons de couleur sur les cases de même couleur le long du trajet, en utilisant les cartes Wagon correspondantes.
    - Les routes ont des longueurs différentes, nécessitant un nombre variable de cartes Wagon pour les réclamer.

4. **Objectifs** :
    - Les joueurs gagnent des points en complétant des routes et en atteignant les destinations spécifiées sur leurs cartes Destination.
    - Des points sont perdus pour chaque destination non atteinte à la fin de la partie.

5. **Fin de partie** :
    - La partie se termine lorsque un joueur n'a plus que 2 ou moins de wagons restants.
    - Les joueurs comptent leurs points en fonction des routes construites et des destinations atteintes.
    - Le joueur avec le plus de points remporte la partie.

"Aventuriers du Rail" est un jeu facile à apprendre mais offre de nombreuses possibilités stratégiques. Expérimentez avec différentes tactiques pour maximiser vos chances de victoire !


## Les Modes de Jeu
Un mode original et un mode nuke

## Lancer Le Jeu ##
Tout d'abord, il faut se mettre dans la repertoire `tchu-tchu`. Puis il faut produire une repertoire out pour les fichiers de compliation. On peut le faire en utilisant le command:

```bash
$ mkdir -p out
```

Maintenant on peut compiler les fichiers java du jeu. Pour compiler tous les fichiers .java:

```bash
$ find src/java/com/ -name "*.java" -print | xargs javac -d out
```

Enfin notre jeu est prêt a lancer. Une fois la compilation fait, on peut lancer le jeu comme on veut plusieurs fois. Le jeu est lancé avec la commande:

```bash
$ java -cp out com.gui.App
```

**A vous de jouer**