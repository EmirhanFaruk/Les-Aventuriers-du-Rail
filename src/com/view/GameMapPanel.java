package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.Plateau;
import com.model.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GameMapPanel extends JPanel {
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
        tile_height = (int) (getHeight() * 0.8 / 24);
        tile_width = (int) (getWidth() * 0.85 / 24);
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


    public PlayerInformationBarPanel getPlayerInformationBarPanel() {
        return playerInformationBarPanel;
    }

    public void setPlayerInformationBarPanel(PlayerInformationBarPanel playerInformationBarPanel) {
        this.playerInformationBarPanel = playerInformationBarPanel;
    }

    public MapScreen getMapScreen() {
        return mapScreen;
    }

    public PlayerHandPanel getPlayerHandPanel() {
        return playerHandPanel;
    }
}
