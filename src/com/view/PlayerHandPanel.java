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

/**
 * Le panneau représentant la main du joueur.
 */
public class PlayerHandPanel extends JPanel {
    private HashMap<String, JSplitPane> playerSplitPanes; // Mapping joueur -> JSplitPane
    private HashMap<String, DrawPlayerHandCardWagon> drawPlayerHandsWagon;
    private HashMap<String, DrawPlayerHandCardDes> drawPlayerHandsDes;
    private int imageWidth, imageHeight;
    private Game game;
    private int width, height;
    private CardLayout cardLayout = new CardLayout();
    JScrollPane scrollPaneCardWagon;
    JScrollPane scrollPaneCardDes;

    /**
     * Obtient le joueur courant.
     *
     * @return Le joueur courant.
     */
    public Player getPlayer() {
        return game.getJoueurCourant();
    }

    /**
     * Initialise les panneaux de dessin des mains des joueurs.
     *
     * @param game Le jeu en cours.
     * @param gameController Le contrôleur du jeu.
     * @param height La hauteur des panneaux.
     */
    public void initDrawPlayerHands(Game game, GameController gameController, int height) {
        drawPlayerHandsWagon = new HashMap<>();
        drawPlayerHandsDes = new HashMap<>();
        playerSplitPanes = new HashMap<>();

        for (Player player : game.getListPlayer()) {
            DrawPlayerHandCardWagon drawPlayerHand = new DrawPlayerHandCardWagon(player, height, gameController, game);
            DrawPlayerHandCardDes drawPlayerHand2 = new DrawPlayerHandCardDes(player, height, gameController, game);
            drawPlayerHandsWagon.put(player.getName(), drawPlayerHand);
            drawPlayerHandsDes.put(player.getName(), drawPlayerHand2);

            // scrollPane pour les cartes destinations
            scrollPaneCardDes = new JScrollPane(drawPlayerHand2);
            scrollPaneCardDes.setPreferredSize(new Dimension(width, height));
            scrollPaneCardDes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPaneCardDes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPaneCardDes.getHorizontalScrollBar().setBackground(Color.ORANGE);

            // scrollPane pour les cartes wagons
            scrollPaneCardWagon = new JScrollPane(drawPlayerHand);
            scrollPaneCardWagon.setPreferredSize(new Dimension(width, height));
            scrollPaneCardWagon.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPaneCardWagon.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPaneCardWagon.getHorizontalScrollBar().setBackground(Color.ORANGE);

            // playerSplitPane
            JSplitPane playerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            playerSplitPane.setResizeWeight(0.5);
            playerSplitPane.setLeftComponent(scrollPaneCardDes);
            playerSplitPane.setRightComponent(scrollPaneCardWagon);
            playerSplitPane.setDividerLocation(width / 2);
            playerSplitPane.setEnabled(false);
            playerSplitPanes.put(player.getName(), playerSplitPane);
        }
    }

    /**
     * Initialise le CardLayout pour gérer les mains des joueurs.
     */
    public void initCardLayout() {
        this.setLayout(cardLayout);
        for (String playerName : drawPlayerHandsWagon.keySet()) {
            JSplitPane playerSplitPane = playerSplitPanes.get(playerName);
            this.add(playerName, playerSplitPane);
        }
    }

    /**
     * Définit le joueur dont la main doit être affichée.
     *
     * @param player Le joueur dont la main doit être affichée.
     */
    public void setPlayer(Player player) {
        cardLayout.show(this, player.getName());
    }

    /**
     * Initialise le panneau de la main du joueur.
     *
     * @param gameController Le contrôleur du jeu.
     * @param game Le jeu en cours.
     * @param width La largeur du panneau.
     * @param height La hauteur du panneau.
     */
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

    /**
     * Classe représentant le panneau de dessin des cartes wagon de la main du joueur.
     */
    public class DrawPlayerHandCardWagon extends JPanel {
        Player player;
        int height;
        int width;
        int hFixe;
        private ArrayList<CarteWagon> listCardWagon;
        GameController gameController;

        /**
         * Constructeur de DrawPlayerHandCardWagon.
         *
         * @param player Le joueur associé à ce panneau.
         * @param height La hauteur du panneau.
         * @param gameController Le contrôleur du jeu.
         * @param game Le jeu en cours.
         */
        DrawPlayerHandCardWagon(Player player, int height, GameController gameController, Game game) {
            this.player = player;
            this.height = height;
            this.width = 0;
            this.hFixe = 10;
            this.gameController = gameController;
            this.listCardWagon = new ArrayList<>();
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
            int cardheight = this.getHeight() - scrollPaneCardWagon.getHorizontalScrollBar().getHeight() - 5 - 2 * hFixe;
            // On soustrait le height du scrollpane et on enleve 2 fois hFix pour centrer les cartes
            int cardwidth = (this.getHeight() - scrollPaneCardWagon.getHorizontalScrollBar().getHeight() - 5 - 2 * hFixe) * 2;
            // l'aspect ratio des cartes est de 2
            listCardWagon = new ArrayList<>();
            while (i < this.player.getTrainList().size()) {
                CarteWagon carteWagon;
                if (player.getNiveau() == 0) {
                    CarteWagon.Couleur couleur = this.player.getTrainList().get(i);
                    carteWagon = new CarteWagon(couleur, x, hFixe);
                    listCardWagon.add(carteWagon);
                } else {
                    CarteWagon.Couleur couleur = CarteWagon.Couleur.BACK;
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
         * Renvoie la carte wagon cliquée par le joueur.
         *
         * @param x La coordonnée x du clic.
         * @param y La coordonnée y du clic.
         * @return La carte wagon cliquée.
         */
        public CarteWagon CardClicked(int x, int y) {
            for (CarteWagon c : listCardWagon) {
                int widthEndCard = c.getWidthInPanel() + imageWidth;
                int heightEndCard = c.getHeightInPanel() + imageHeight;
                if (x > c.getWidthInPanel() && x < widthEndCard && y > c.getHeightInPanel() && y < heightEndCard) {
                    // DEBUG : System.err.println("La carte est de la couleur " + c.getInitialCouleur());
                    return c;
                }
            }
            return null;
        }

        /**
         * Obtient le joueur associé à ce panneau.
         *
         * @return Le joueur associé.
         */
        public Player getPlayer() {
            return this.player;
        }
    }

    /**
     * Classe représentant le panneau de dessin des cartes destination de la main du joueur.
     */
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

        /**
         * Constructeur de DrawPlayerHandCardDes.
         *
         * @param player Le joueur associé à ce panneau.
         * @param height La hauteur du panneau.
         * @param g Le contrôleur du jeu.
         * @param game Le jeu en cours.
         */
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
         * Ajoute un MouseMotionListener pour gérer le survol des cartes.
         */
        public void addMouseMotionListener() {
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
                                    } catch (Exception ignored) { }
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
                    repaint(); // Force repaint for color update
                }
            });
        }

        /**
         * Ajoute un MouseListener pour gérer les actions de la souris.
         */
        public void addMouseListener() {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    if (currentHoverCard != null) {
                        currentHoverCard = null;
                        repaint();
                    }
                }
            });
        }

        /**
         * Obtient la carte destination survolée par la souris.
         *
         * @param x La coordonnée x de la souris.
         * @param y La coordonnée y de la souris.
         * @return La carte destination survolée.
         */
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
            int cardheight = this.getHeight() - scrollPaneCardDes.getHorizontalScrollBar().getHeight() - 5 - 2 * hFixe;
            // On soustrait le height du scrollpane et on enlève 2 fois hFix pour centrer les cartes
            int cardwidth = (this.getHeight() - scrollPaneCardDes.getHorizontalScrollBar().getHeight() - 5 - 2 * hFixe) * 2;
            // l'aspect ratio des cartes est de 2
            while (i < this.player.getDestinationsList().size()) {
                BufferedImage image = CardGraphics.getCardObjectif();
                if (image != null) {
                    Rectangle cardArea = new Rectangle(x, hFixe, cardwidth, cardheight);
                    CarteDestination carteJ = this.player.getDestinationsList().get(i);
                    cardAreas.put(cardArea, carteJ);

                    g.drawImage(image, x, hFixe, cardwidth, cardheight, null);

                    if (carteJ == currentHoverCard && player.getNiveau() == 0) {
                        // Appliquer une couleur jaune semi-transparente
                        g.setColor(new Color(255, 255, 0, 128)); // Jaune semi-transparent
                        g.fillRect(x, hFixe, cardwidth, cardheight);
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

    /**
     * Obtient le panneau de dessin des cartes wagon pour le joueur spécifié.
     *
     * @param playerName Le nom du joueur.
     * @return Le panneau de dessin des cartes wagon du joueur.
     */
    public DrawPlayerHandCardWagon getDrawPlayerHand(String playerName) {
        return drawPlayerHandsWagon.get(playerName);
    }

    /**
     * Obtient le jeu en cours.
     *
     * @return Le jeu en cours.
     */
    public Game getGame() {
        return game;
    }
}
