package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.Plateau;
import com.model.config.carte.CarteWagon;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlayerHandPanel extends JPanel {

    private DrawPlayerHand drawPlayerHands1 ;
    private DrawPlayerHand drawPlayerHands2 ;
    private DrawPlayerHand drawPlayerHands3 ;
    private DrawPlayerHand drawPlayerHands4;


    private JScrollPane scrollPanes1;
    private JScrollPane scrollPanes2;
    private JScrollPane scrollPanes3;
    private JScrollPane scrollPanes4;

    private ArrayList<String> whoSHand;
    private int imageWidth , imageHeight ;
    private Game game ;
    private MapScreen mapScreen ;
    private int width , height ;
    private JPanel capsulPanel;

    private CardLayout cardLayout = new CardLayout();


    public PlayerHandPanel(GameController gameController, Game game , int width , int height , MapScreen mapScreen){
        make(gameController,game,width,height,mapScreen);
    }

    public void setPlayer(Player player) {
        cardLayout.show(this,player.getName());
    }

    public void initDrawPlayerHand(Game game, GameController gameController, int height){
        //Initialise la liste des DrawPlayerHand, pour permettre d'afficher la main du joueur qui joue
        for(int i = 0; i< game.getListPlayer().size();i++){
            DrawPlayerHand drawPlayerHand = new DrawPlayerHand(game.getListPlayer().get(i), height, gameController,game);

            switch (i){
                case 0 :
                    drawPlayerHands1 = drawPlayerHand ;
                    break;
                case 1 :
                    drawPlayerHands2 = drawPlayerHand;
                    break;
                case 2 :
                    drawPlayerHands3 = drawPlayerHand;
                    break;
                case 3 :
                    drawPlayerHands4 = drawPlayerHand;
                    break;


            }
        }
    }

    public void initScrollPane(int width , int height ){
        //Initilisation d'une liste de JScrollPane pour changer l'affichage de la main courante a chaque fois
        DrawPlayerHand[] drawPlayerHands = {drawPlayerHands1,drawPlayerHands2,drawPlayerHands3,drawPlayerHands4};

        for(int i = 0; i< drawPlayerHands.length;i++){
            JScrollPane scrollPane = new JScrollPane(drawPlayerHands[i]);
            scrollPane.setPreferredSize(new Dimension( width , height ));
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.getHorizontalScrollBar().setBackground(Color.ORANGE);

            switch (i){
                case 0 :
                    scrollPanes1 = scrollPane;
                    break;
                case 1 :
                    scrollPanes2 = scrollPane;
                    break;
                case 2 :
                    scrollPanes3 = scrollPane;
                    break;
                case 3 :
                    scrollPanes4 = scrollPane;
                    break;


            }


        }
    }

    public void initWhosHand(Game game){
        this.whoSHand = new ArrayList<>();
        //Initialisation de la liste de String pour le cardLayout (pour se déplacer entre les joueurs)
        for (int i = 0; i< game.getListPlayer().size();i++){
            this.whoSHand.add(game.getListPlayer().get(i).getName());
        }
    }

    public void initCardLayout(JPanel capsulPanel){
        //Initialisation du contenu du cardLayout

        JScrollPane[] scrollPanes = {scrollPanes1,scrollPanes2,scrollPanes3,scrollPanes4} ;

        this.setLayout(cardLayout);
        for(int i = 0; i< this.whoSHand.size();i++){

            capsulPanel.add(whoSHand.get(i),scrollPanes[i]);
        }
    }

    public void make(GameController gameController, Game game , int width , int height , MapScreen mapScreen){
        //Initialisation de tout les attributs de la classe

        this.capsulPanel = new JPanel();
        this.game = game ;
        this.mapScreen = mapScreen ;
        this.width = width ;
        this.height = height ;

        setBackground(Color.orange);
        setPreferredSize(new Dimension( this.width, this.height ));

        initDrawPlayerHand(game,gameController, this.height);
        initScrollPane(this.width, this.height);
        initWhosHand(game);
        initCardLayout(capsulPanel);
        this.add(capsulPanel, BorderLayout.SOUTH);


    }

    public void setMapScreen(MapScreen mapScreen) {
        this.mapScreen = mapScreen;
    }


    public DrawPlayerHand getDrawPlayerHand() {
        int whoIsPlaying = game.getRound().getWhoIsPlaying();
        switch (whoIsPlaying) {
            case 0:
                return drawPlayerHands1;
            case 1:
                return drawPlayerHands2;
            case 2:
                return drawPlayerHands3;
            case 3:
                return drawPlayerHands4;

        }
        return null;
    }

    public Game getGame() {
        return game;
    }

    public class DrawPlayerHand extends JPanel {
        Player player ;
        int height ;
        int width ;
        int hFixe ;
        private ArrayList< CarteWagon > listCardWagon ;
        GameController gameController ;

        DrawPlayerHand (Player player  , int height , GameController gameController, Game game) {
            this.player = player;
            this.height = height;
            this.width = 0;
            this.hFixe = 30 ;
            this.gameController = gameController ;
            this.listCardWagon = new ArrayList<>() ;
            setBackground(Color.orange);
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    gameController.couleurCarteAChoisir( e ,  player , PlayerHandPanel.this , mapScreen,game );
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (this.player != null) {
                this.drawPlayerHand(g2d);
                setPreferredSize(new Dimension(width, height));
                getParent().revalidate(); // Appel à revalidate() sur le parent (JScrollPane)
            }
            repaint();
        }

        private void drawPlayerHand(Graphics2D g) {
            int x = 30, i = 0;
            listCardWagon = new ArrayList<>();
            while (i < this.player.getTrainCard().size()) {
                CarteWagon.Couleur couleur = this.player.getTrainCard().get(i);
                CarteWagon carteWagon = new CarteWagon( couleur , x , hFixe );
                listCardWagon.add( carteWagon ) ;
                BufferedImage image = CardGraphics.getImage(carteWagon);

                if (image != null) {
                    g.drawImage(image, x , hFixe , null);
                    x += image.getWidth() + 10;
                    width = x;
                    imageWidth = image.getWidth() ;
                    imageHeight = image.getHeight() ;
                }
                i++;
            }
        }

        /**
         * Une fonction qui renvoie la couleur de la carte clicker par le joueur
         * @param x width
         * @param y height
         * @return la carte clicker
         */
        public CarteWagon CardClicked (int x , int y ){
            for ( CarteWagon c : listCardWagon ){
                int widthEndCard = c.getWidthInPanel() + imageWidth ;
                int heightEndCard = c.getHeightInPanel() + imageHeight ;
                if ( x > c.getWidthInPanel() && x < widthEndCard && y > c.getHeightInPanel() && y < heightEndCard ){
                    //DEBUG : System.err.println("La carte est de la couleur " + c.getInitialCouleur() );
                    return c ;
                }
            }
           return null ;
        }

        public void setPlayer(Player player) {
            this.player = player;
            repaint();
        }

    }

}
