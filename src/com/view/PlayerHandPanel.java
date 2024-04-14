package com.view;

import com.model.Game;
import com.model.Player;
import com.model.config.carte.CarteDestination;
import com.model.config.carte.CarteWagon;
import com.model.controller.GameController;
import com.view.graphics.CardGraphics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerHandPanel extends JPanel {
    private HashMap<String, JSplitPane> playerSplitPanes; // Mapping joueur -> JSplitPane
    private HashMap<String, DrawPlayerHand> drawPlayerHands;
    private HashMap<String, DrawPlayerHand2> drawPlayerHands2;
    private int imageWidth, imageHeight;
    private Game game;
    private int width, height;
    private CardLayout cardLayout = new CardLayout();
    
    public Player getPlayer() {
        return game.getJoueurCourant();
    }

    public void initDrawPlayerHands(Game game, GameController gameController, int height) {
        drawPlayerHands = new HashMap<>();
        drawPlayerHands2 = new HashMap<>();
        playerSplitPanes = new HashMap<>();
        
        for (Player player : game.getListPlayer()) {
            DrawPlayerHand drawPlayerHand = new DrawPlayerHand(player, height, gameController, game);
            DrawPlayerHand2 drawPlayerHand2 = new DrawPlayerHand2(player, height, gameController, game);
            drawPlayerHands.put(player.getName(), drawPlayerHand);
            drawPlayerHands2.put(player.getName(), drawPlayerHand2);
            JSplitPane playerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            playerSplitPane.setResizeWeight(0.5);
            playerSplitPane.setLeftComponent(drawPlayerHand2);
            JScrollPane scrollPane = new JScrollPane(drawPlayerHand);
            scrollPane.setPreferredSize(new Dimension(width, height));
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.getHorizontalScrollBar().setBackground(Color.ORANGE);
            playerSplitPane.setRightComponent(scrollPane);
            playerSplitPane.setDividerLocation(width / 2);
            playerSplitPane.setEnabled(false);
            playerSplitPanes.put(player.getName(), playerSplitPane);
        }
    }

    public void initCardLayout() {
        this.setLayout(cardLayout);
        for (String playerName : drawPlayerHands.keySet()) {
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

    public DrawPlayerHand getDrawPlayerHand(String playerName) {
        return drawPlayerHands.get(playerName);
    }

    public DrawPlayerHand2 getDrawPlayerHand2(String playerName) {
        return drawPlayerHands2.get(playerName);
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
	            	gameController.couleurCarteAChoisir( e ,  player , PlayerHandPanel.this ,game );
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
	        int x = 30, i = 0;
	        listCardWagon = new ArrayList<>();
	        while (i < this.player.getTrainList().size()) {
	            CarteWagon.Couleur couleur = this.player.getTrainList().get(i);
	            CarteWagon carteWagon = new CarteWagon(couleur, x, hFixe);
	            listCardWagon.add(carteWagon);
	            BufferedImage image = CardGraphics.getImage(carteWagon);
	
	            if (image != null) {
	                g.drawImage(image, x, hFixe, null);
	                x += image.getWidth() + 10;
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
	            if ( x > c.getWidthInPanel() && x < widthEndCard && y > c.getHeightInPanel() && y < heightEndCard ){
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

    
    public class DrawPlayerHand2 extends JPanel {
        Player player;
        int height;
        int width;
        int hFixe;
        private HashMap<Rectangle, CarteDestination> cardAreas;

        DrawPlayerHand2(Player player, int height, GameController g, Game game) {
            this.player = player;
            this.height = height;
            this.width = 0;
            this.hFixe = 30;
            this.cardAreas = new HashMap<Rectangle, CarteDestination>();

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    CarteDestination carteClicked = getClickedCard(e.getX(), e.getY());
                    if (carteClicked != null) {
                        g.descriptionCardDestination(carteClicked, game);
                    }
                }
            });
            
            setBackground(Color.orange);
        }
        
        private CarteDestination getClickedCard(int x, int y) {
            // Parcourez toutes les zones de clic
            for (Map.Entry<Rectangle, CarteDestination> entry : cardAreas.entrySet()) {
                Rectangle area = entry.getKey();
                // Vérifiez si les coordonnées du clic se trouvent dans cette zone
                if (area.contains(x, y)) {
                    // Si oui, retournez la carte destination associée
                    return entry.getValue();
                }
            }
            // Si aucune carte n'est cliquée, retournez null
            return null;
        }

		@Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (this.player != null) {
                drawPlayerHand2(g2d); // Appel de la méthode drawPlayerHand2 ici
            }
        }

        private void drawPlayerHand2(Graphics2D g) {
            int x = 30, i = 0;

            while (i < this.player.getDestinationsList().size()) {
                BufferedImage image = CardGraphics.getCardObjectif();

                if (image != null) {
                	// Créez un rectangle représentant la zone de clic pour cette carte
                    Rectangle cardArea = new Rectangle(x, hFixe, image.getWidth(), image.getHeight());
                    
                    //Carte destination du joueur
                    CarteDestination carteJ = this.player.getDestinationsList().get(i);
                    
                    // Associez la carte destination à la zone de clic
                    cardAreas.put(cardArea, carteJ);
                	
                    g.drawImage(image, x, hFixe, null);
                    x += image.getWidth() + 10;
                    width = x;
                    // Vous pouvez également initialiser imageWidth et imageHeight ici si nécessaire
                }
                i++;
            }
        }
    }
	
}
