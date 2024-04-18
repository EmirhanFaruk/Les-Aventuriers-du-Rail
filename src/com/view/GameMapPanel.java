package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.Plateau;
import com.model.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.util.ArrayList;

public class GameMapPanel extends JPanel {
    private final int height, width;
    GameFrame frame ;
    private MapScreen mapScreen ;
    private PlayerHandPanel playerHandPanel ;
    private PlayerInformationBarPanel playerInformationBarPanel ;
    private PiochePanel pioche;
    private CarteDestinationPanel cdPanel;
    private static int tile_width , tile_height ;
    private GameController gameController = new GameController();


    /**
     * Constructeur de la classe GameManagerScreen
     * @param frame
     * @param map
     * @param width
     * @param height
     */
    public GameMapPanel (GameFrame frame , String map , int width , int height , Player player , Game game ){
        this.frame = frame ;
        setSize(width , height );
        this.height = height ;
        this.width = width ;

        int[] tileCount = getTileCount(map);

        tile_width = (int) (getWidth() * 0.85 / tileCount[0]);
        tile_height = (int) (getHeight() * 0.8 / tileCount[1]);


        this.playerHandPanel = new PlayerHandPanel() ;
        this.mapScreen = new MapScreen( map , (int) (width*0.85), (int) (height*0.8),tile_width , tile_height  , player , game, this.playerHandPanel , gameController ) ;
        this.playerHandPanel.make(gameController,game,width,(int) (height * 0.2));
        this.pioche = new PiochePanel(width, height, this.playerHandPanel, game);
        this.cdPanel = new CarteDestinationPanel(width, height, this.playerHandPanel, game);
        this.playerInformationBarPanel = new PlayerInformationBarPanel(  game.getListPlayer() , player  , width , ( int ) ( height * 0.05 )) ;

        setLayout(new BorderLayout());

        add( mapScreen , BorderLayout.CENTER ) ;
        add(pioche, BorderLayout.EAST);
        add(cdPanel, BorderLayout.WEST);
        add( playerHandPanel , BorderLayout.SOUTH ) ;
        add(playerInformationBarPanel , BorderLayout.NORTH ) ;

    }

    private int[] getTileCount(String map) {
        //Retourne la taille du scale de la map (heigth et weight)
        BufferedReader reader = Plateau.openFile(map);

        // 0 = heigth, 1 = weight
        int[] size = Plateau.readFile(reader,new ArrayList<>());


        return size;

    }


    /**
     * Une fonction qui permet de faire la map à partir du plateau
     * @param plateau Plateau
     */
    public void make( Plateau plateau ){
        mapScreen.makeMap( plateau );
    }


    public void setPlayerCourant(Player playerCourant) {
        this.playerHandPanel.setPlayer(playerCourant);
        this.cdPanel.setPlayer(playerCourant);
        this.cdPanel.setAllDefault(); //Remet tout à false (pour afficher la bonne couleur)
        this.cdPanel.repaint();
        this.pioche.setPlayer(playerCourant);
        this.playerInformationBarPanel.setPlayerCourant(playerCourant);
        this.mapScreen.setPlayer( playerCourant );
    }

    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public PlayerHandPanel getPlayerHandPanel() {
        return playerHandPanel;
    }
}
