package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerHandPanel extends JPanel {
    private HashMap<String, JSplitPane> playerSplitPanes; // Mapping joueur -> JSplitPane
    private HashMap<String, DrawPlayerHandCardWagon> drawPlayerHandsWagon;
    private HashMap<String, DrawPlayerHandCardDes> drawPlayerHandsDes;
    private int imageWidth, imageHeight;
    private Game game;
    private int width, height;
    private CardLayout cardLayout = new CardLayout();
    JScrollPane scrollPaneCardWagon ;
    JScrollPane scrollPaneCardDes ;

    public Player getPlayer() {
        return game.getJoueurCourant();
    }

    public void initDrawPlayerHands(Game game, GameController gameController, int height) {
        drawPlayerHandsWagon = new HashMap<>();
        drawPlayerHandsDes = new HashMap<>();
        playerSplitPanes = new HashMap<>();

        for (Player player : game.getListPlayer()) {
            DrawPlayerHandCardWagon drawPlayerHand = new DrawPlayerHandCardWagon(player, height, gameController, game);
            DrawPlayerHandCardDes drawPlayerHand2 = new DrawPlayerHandCardDes(player, height, gameController, game);
            drawPlayerHandsWagon.put(player.getName(), drawPlayerHand);
            drawPlayerHandsDes.put(player.getName(), drawPlayerHand2);

            //scrollPane pour les cartes destinations
            scrollPaneCardDes = new JScrollPane(drawPlayerHand2);
            scrollPaneCardDes.setPreferredSize(new Dimension(width, height));
            scrollPaneCardDes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPaneCardDes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPaneCardDes.getHorizontalScrollBar().setBackground(Color.ORANGE);

            //scrollPane pour les cartes wagons
            scrollPaneCardWagon = new JScrollPane(drawPlayerHand);
            scrollPaneCardWagon.setPreferredSize(new Dimension(width, height));
            scrollPaneCardWagon.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPaneCardWagon.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPaneCardWagon.getHorizontalScrollBar().setBackground(Color.ORANGE);

            //playerSplitPane
            JSplitPane playerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            playerSplitPane.setResizeWeight(0.5);
            playerSplitPane.setLeftComponent(scrollPaneCardDes);
            playerSplitPane.setRightComponent(scrollPaneCardWagon);
            playerSplitPane.setDividerLocation(width / 2);
            playerSplitPane.setEnabled(false);
            playerSplitPanes.put(player.getName(), playerSplitPane);
        }
    }

    public void initCardLayout() {
        this.setLayout(cardLayout);
        for (String playerName : drawPlayerHandsWagon.keySet()) {
            JSplitPane playerSplitPane = playerSplitPanes.get(playerName);
            this.add(playerName, playerSplitPane);
        }
    }

    public void setPlayer(Player player) {
        cardLayout.show(this, player.getName());
    }

    public void make(GameController gameController, Game game, int width, int height) {
        this.game = game;
        this.width = width;
        this.height = height;
        setBackground(Color.orange);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(this.width, this.height));
        initDrawPlayerHands(game, gameController, this.height);
        initCardLayout();
        this.setPlayer(game.getJoueurCourant());
    }


    public class DrawPlayerHandCardWagon extends JPanel {
    	Player player ;
    	int height ;
    	int width ;
    	int hFixe ;
    	private ArrayList< CarteWagon > listCardWagon ;
    	GameController gameController ;

    	DrawPlayerHandCardWagon (Player player  , int height , GameController gameController, Game game) {
	        this.player = player;
	        this.height = height;
	        this.width = 0;
	        this.hFixe = 10 ;
	        this.gameController = gameController ;
	        this.listCardWagon = new ArrayList<>() ;
	        setBackground(Color.orange);
	        this.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	            	gameController.couleurCarteAChoisir(e, player, PlayerHandPanel.this, game);
	            }
	        });
    	}
	
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;
	        if (this.player != null) {
	            this.drawPlayerHand(g2d);
	        }
	    }
	
	    private void drawPlayerHand(Graphics2D g) {
	        int x = 15, i = 0;
            int cardheight = this.getHeight()-scrollPaneCardWagon.getHorizontalScrollBar().getHeight()-5-2*hFixe; // on soustrait le height du scrollpane et on enleve 2 fois hfix pour center les cartes
            int cardwidth = (this.getHeight()-scrollPaneCardWagon.getHorizontalScrollBar().getHeight()-5-2*hFixe)*2; //l'aspect ratio des cartes est de 2
	        listCardWagon = new ArrayList<>();
	        while (i < this.player.getTrainList().size()) {
                CarteWagon carteWagon ;
                if ( player.getNiveau() == 0 ) {
                    CarteWagon.Couleur couleur = this.player.getTrainList().get(i);
                    carteWagon = new CarteWagon(couleur, x, hFixe);
                    listCardWagon.add(carteWagon);
                } else {
                    CarteWagon.Couleur couleur = CarteWagon.Couleur.BACK ;
                    carteWagon = new CarteWagon(couleur, x, hFixe);
                }
                    BufferedImage image = CardGraphics.getImage(carteWagon);
                    if (image != null) {
                        g.drawImage(image, x, hFixe, cardwidth, cardheight, null);
                        x += cardwidth + 15;
                        width = x;
                        imageWidth = image.getWidth();
                        imageHeight = image.getHeight();
                    }
	            i++;
	        }
	        
	        // Mettre à jour les dimensions du panneau
	        setPreferredSize(new Dimension(width, height));
	        revalidate(); // Mettre à jour la mise en page
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
	            if ( x > c.getWidthInPanel() && x < widthEndCard && y > c.getHeightInPanel() && y < heightEndCard){
	                //DEBUG : System.err.println("La carte est de la couleur " + c.getInitialCouleur() );
	                return c ;
	            }
	        }
	        return null ;
	    }

	    public Player getPlayer() {
	        return this.player;
	    }
    }

    
    public class DrawPlayerHandCardDes extends JPanel {
        Player player;
        int height;
        int width;
        int hFixe;
        HashMap<Rectangle, CarteDestination> cardAreas;
        Timer hoverTimer;
        CarteDestination currentHoverCard;
        GameController gameController;
        Game game;

        DrawPlayerHandCardDes(Player player, int height, GameController g, Game game) {
            this.player = player;
            this.height = height;
            this.width = 0;
            this.hFixe = 10;
            this.cardAreas = new HashMap<>();
            this.gameController = g;
            this.game = game;

            addMouseMotionListener();
            addMouseListener();

            setBackground(Color.orange);
        }

        /**
         * Une fonction qui ajoute un Mouse Motion Listener
         */
        public void addMouseMotionListener(){
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    CarteDestination carteHovered = getHoverCard(e.getX(), e.getY());
                    if (carteHovered != null) {
                        if (currentHoverCard != carteHovered) {
                            if (hoverTimer != null) {
                                hoverTimer.stop();
                            }
                            currentHoverCard = carteHovered;
                            hoverTimer = new Timer(2000, new ActionListener() {
                                public void actionPerformed(ActionEvent ae) {
                                    try {
                                        gameController.descriptionCardDestination(currentHoverCard, game);
                                    } catch (Exception ignored ) { }
                                }
                            });
                            hoverTimer.setRepeats(false);
                            hoverTimer.start();
                        }
                    } else {
                        if (hoverTimer != null) {
                            hoverTimer.stop();
                            hoverTimer = null;
                            currentHoverCard = null;
                        }
                    }
                    repaint();  // Force repaint for color update
                }
            });
        }

        /**
         * Une fonction qui ajoute un Mouse Listener
         */
        public void addMouseListener () {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    if (currentHoverCard != null) {
                        currentHoverCard = null ;
                        repaint();
                    }

                }
            });
        }

        private CarteDestination getHoverCard(int x, int y) {
            for (Map.Entry<Rectangle, CarteDestination> entry : cardAreas.entrySet()) {
                Rectangle area = entry.getKey();
                if (area.contains(x, y)) {
                    return entry.getValue();
                }
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (this.player != null) {
                drawPlayerHand2(g2d);
            }
        }

        private void drawPlayerHand2(Graphics2D g) {
            int x = 25, i = 0;
            int cardheight = this.getHeight()-scrollPaneCardDes.getHorizontalScrollBar().getHeight()-5-2*hFixe; // on soustrait le height du scrollpane et on enleve 2 fois hfix pour center les cartes
            int cardwidth = (this.getHeight()-scrollPaneCardDes.getHorizontalScrollBar().getHeight()-5-2*hFixe)*2; //l'aspect ratio des cartes est de 2
            while (i < this.player.getDestinationsList().size()) {
                BufferedImage image = CardGraphics.getCardObjectif();
                if (image != null) {
                    Rectangle cardArea = new Rectangle(x, hFixe, cardwidth , cardheight);
                    CarteDestination carteJ = this.player.getDestinationsList().get(i);
                    cardAreas.put(cardArea, carteJ);

                    g.drawImage(image, x, hFixe,cardwidth,cardheight,null);

                    if (carteJ == currentHoverCard && player.getNiveau() == 0 ) {
                        // Appliquer une couleur jaune semi-transparente
                        g.setColor(new Color(255, 255, 0, 128)); // Jaune semi-transparent
                        g.fillRect(x, hFixe, cardwidth , cardheight );
                    }

                    x += cardwidth + 30;
                    width = x;
                }
                i++;
            }
            setPreferredSize(new Dimension(width, height));
            revalidate(); // Mettre à jour la mise en page
        }
    }


    /* getters et setters */

    public DrawPlayerHandCardWagon getDrawPlayerHand(String playerName) {
        return drawPlayerHandsWagon.get(playerName);
    }

    public Game getGame() {
        return game;
    }

}
