package com.view.mainmenu;

import com.view.GameFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * La classe Play représente la section du menu où les joueurs peuvent configurer et démarrer une partie.
 */
public class Play extends JPanel {
    private JLabel level_name_tag;
    private JButton mode_button;
    private final String[] possible_modes = {"NORMAL", "NUKE"};
    private JTextArea[] player_name_list_tag;
    private JLabel[] player_type_list_tag;
    private Color[] player_colors = {Color.red, Color.blue, Color.green, Color.yellow};
    private JButton[] selected_color = new JButton[4];

    private final String PLAYER = "PLAYER", WEAKBOT = "WEAK", NORMALBOT = "NORMAL", STRONGBOT = "STRONG", NONE = "NONE";

    private GameFrame frame;

    /**
     * Constructeur de la classe Play.
     * @param frame Le frame principal du jeu.
     */
    public Play(GameFrame frame) {
        this.frame = frame;
        makePlay();
    }

    @Override
    public void setSize(int width, int height) {
        makePlay();
    }

    /**
     * Initialise la section Play du menu en utilisant les fonctions de création de panels.
     */
    private void makePlay() {
        setLayout(new BorderLayout());
        initializeTables();
        add(makeAllPlayersPanel());
        add(makeMapListPanel(), BorderLayout.EAST);
    }

    /**
     * Initialise les listes player_name_list_tag et player_type_list_tag.
     */
    private void initializeTables() {
        player_name_list_tag = new JTextArea[4];
        for (int i = 0; i < 4; i++) {
            player_name_list_tag[i] = new JTextArea("Player " + (i + 1));
        }

        player_type_list_tag = new JLabel[4];
        for (int i = 0; i < 4; i++) {
            player_type_list_tag[i] = new JLabel(PLAYER);
        }
    }

    /**
     * Crée un JPanel avec un arrière-plan noir et un premier plan gris.
     * @return Le panel créé.
     */
    private JPanel makeDefaultPanel() {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

    /**
     * Crée un JPanel qui centre le composant donné.
     * @param comp Le composant à centrer.
     * @return Le panel contenant le composant centré.
     */
    private JPanel makeCenteringPanel(JComponent comp) {
        JPanel res = makeDefaultPanel();
        res.setLayout(new GridLayout(3, 3));
        for (int j = 0; j < 4; j++) {
            res.add(makeDefaultPanel());
        }
        res.add(comp);
        for (int j = 0; j < 4; j++) {
            res.add(makeDefaultPanel());
        }
        return res;
    }

    /**
     * Crée un JPanel qui centre verticalement le composant donné.
     * @param comp Le composant à centrer.
     * @return Le panel contenant le composant centré verticalement.
     */
    private JPanel makeVerticalCenteringPanel(JComponent comp) {
        JPanel res = makeDefaultPanel();
        res.setLayout(new GridLayout(3, 1));
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        res.add(makeDefaultPanel());
        middlePanel.add(makeDefaultPanel(), BorderLayout.WEST);
        middlePanel.add(comp, BorderLayout.CENTER);
        res.add(middlePanel);
        res.add(makeDefaultPanel());
        return res;
    }

    /**
     * Rassemble 4 joueurs dans un panel.
     * @return Le panel contenant 4 joueurs.
     */
    private JPanel makeAllPlayersPanel() {
        JPanel res = makeDefaultPanel();
        res.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        res.setLayout(new GridLayout(4, 1));
        for (int i = 0; i < 4; i++) {
            res.add(makeSinglePlayerPanel(i));
        }
        return res;
    }

    /**
     * Crée la partie nom du joueur du panel joueur. Ajoute également le nom à player_name_list_tag.
     * @param i Le numéro du joueur.
     * @return Le JTextArea encapsulé dans un JPanel.
     */
    private JPanel makePlayerNamePanel(int i) {
        JTextArea playerName = new JTextArea("Player " + (i + 1));
        playerName.setBackground(Color.GRAY);
        playerName.setForeground(Color.BLACK);
        playerName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                frame.playSoundClick("MENU" ,"click.wav"); // a changer pour l'ecriture
            }
        });
        player_name_list_tag[i] = playerName;
        return makeVerticalCenteringPanel(playerName);
    }

    /**
     * Crée un bouton qui bascule entre CPU et joueur.
     * @param i Le numéro du joueur.
     * @return Le panel contenant le bouton.
     */
    private JPanel makeCPUPlayerSelectorPanel(int i) {
        player_type_list_tag[i] = new JLabel(PLAYER);
        JButton button = new JButton(PLAYER);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.GRAY);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        if (isPlayerOne(i)) {
            button.setEnabled(false);
        } else {
            button.addActionListener(e -> {
                switch (player_type_list_tag[i].getText()) {
                    case PLAYER:
                        frame.playSoundClick("MENU" ,"WeakBot.wav");
                        player_type_list_tag[i].setText(WEAKBOT);
                        button.setText(WEAKBOT);
                        break;
                    case WEAKBOT:
                        frame.playSound("MENU" ,"NormalBot.wav");
                        player_type_list_tag[i].setText(NORMALBOT);
                        button.setText(NORMALBOT);
                        break;
                    case NORMALBOT:
                        frame.playSound("MENU" ,"StrongBot.wav");
                        player_type_list_tag[i].setText(STRONGBOT);
                        button.setText(STRONGBOT);
                        break;
                    case STRONGBOT:
                        frame.playSound("MENU" ,"None.wav");
                        player_type_list_tag[i].setText(NONE);
                        button.setText(NONE);
                        break;
                    case NONE:
                        frame.playSound("MENU" ,"Player.wav");
                        player_type_list_tag[i].setText(PLAYER);
                        button.setText(PLAYER);
                        break;
                    default:
                        break;
                }
            });
        }
        return makeCenteringPanel(button);
    }

    /**
     * Crée une liste pour choisir la couleur du joueur.
     * @param i Le numéro du joueur.
     * @return Le panel contenant la liste des couleurs.
     */
    private JComponent makePlayerCoulourChoicePanel(int i) {
        JPanel colors = makeDefaultPanel();
        colors.setLayout(new GridLayout(1, 5));
        JPanel redPanel = makeredPanel(i);
        JPanel bluePanel = makebluePanel(i);
        JPanel greenPanel = makegreenPanel(i);
        JPanel yellowPanel = makeyellowPanel(i);
        colors.add(redPanel);
        colors.add(bluePanel);
        colors.add(greenPanel);
        colors.add(yellowPanel);
        colors.add(makeDefaultPanel());
        return colors;
    }

    private JPanel makeredPanel(int i) {
        JPanel redPanel = makeDefaultPanel();
        JButton red = new JButton();
        red.setBackground(Color.red);
        red.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.playSoundClick("MENU", "Colors.wav");
                if (selected_color[i] != red) {
                    if (selected_color[i] != null) selected_color[i].setBorder(new EmptyBorder(5, 5, 5, 5));
                    red.setBorder(new LineBorder(Color.WHITE, 5));
                    selected_color[i] = red;
                    player_colors[i] = Color.red;
                }
            }
        });
        redPanel.setLayout(new GridLayout(3, 1));
        redPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        redPanel.add(makeDefaultPanel());
        redPanel.add(red);
        return redPanel;
    }

    private JPanel makebluePanel(int i) {
        JPanel bluePanel = makeDefaultPanel();
        JButton blue = new JButton();
        blue.setBackground(Color.blue);
        blue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.playSoundClick("MENU", "Colors.wav");
                if (selected_color[i] != blue) {
                    if (selected_color[i] != null)
                    {
                        selected_color[i].setBorder(new EmptyBorder(5, 5, 5, 5));
                    }
                    blue.setBorder(new LineBorder(Color.WHITE, 5));
                    selected_color[i] = blue;
                    player_colors[i] = Color.blue;
                }
            }
        });
        bluePanel.setLayout(new GridLayout(3, 1));
        bluePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        bluePanel.add(makeDefaultPanel());
        bluePanel.add(blue);
        return bluePanel;
    }

    private JPanel makegreenPanel(int i) {
        JPanel greenPanel = makeDefaultPanel();
        JButton green = new JButton();
        green.setBackground(Color.GREEN);
        green.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.playSoundClick("MENU" ,"Colors.wav");
                if (selected_color[i] != green) {
                    if (selected_color[i] != null)
                    {
                        selected_color[i].setBorder(new EmptyBorder(5, 5, 5, 5));
                    }
                    green.setBorder(new LineBorder(Color.WHITE, 5));
                    selected_color[i] = green;
                    player_colors[i] = Color.green;
                }
            }
        });
        greenPanel.setLayout(new GridLayout(3, 1));
        greenPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        greenPanel.add(makeDefaultPanel());
        greenPanel.add(green);
        return greenPanel;
    }

    private JPanel makeyellowPanel(int i) {
        JPanel yellowPanel = makeDefaultPanel();
        JButton yellow = new JButton();
        yellow.setBackground(Color.YELLOW);
        yellow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.playSoundClick("MENU" ,"Colors.wav");
                if (selected_color[i] != yellow) {
                    if (selected_color[i] != null)
                    {
                        selected_color[i].setBorder(new EmptyBorder(5, 5, 5, 5));
                    }
                    yellow.setBorder(new LineBorder(Color.WHITE, 5));
                    selected_color[i] = yellow;
                    player_colors[i] = Color.yellow;
                }
            }
        });
        yellowPanel.setLayout(new GridLayout(3, 1));
        yellowPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        yellowPanel.add(makeDefaultPanel());
        yellowPanel.add(yellow);
        return yellowPanel;
    }

    /**
     * Crée un panel pour un joueur unique.
     * @param i Le numéro qui différencie les joueurs.
     * @return Le panel du joueur.
     */
    private JPanel makeSinglePlayerPanel(int i) {
        JPanel res = makeDefaultPanel();
        res.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        res.setLayout(new GridLayout(1, 3));
        res.add(makePlayerNamePanel(i));
        res.add(makeCPUPlayerSelectorPanel(i));
        res.add(makePlayerCoulourChoicePanel(i));
        return res;
    }

    /**
     * Trouve le slash approprié pour le système d'exploitation (/ ou \\).
     * @param p Un exemple de chemin pour voir quel slash il utilise.
     * @return Le slash approprié.
     */
    private String findSlash(String p) {
        for (int i = 0; i < p.length(); i++) {
            switch (p.charAt(i)) {
                case '/':
                    return "/";
                case '\\':
                    return "\\";
            }
        }
        return "/";
    }

    /**
     * Compte combien de fichiers se trouvent dans le dossier ressources/maps.
     * @return Le nombre de fichiers.
     */
    private int getMapCount() {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);
        File directory = new File(path + s + "ressources" + s + "maps");
        int map_count = 0;
        if (directory.list() != null) {
            map_count = directory.list().length;
        }
        return map_count;
    }

    /**
     * Obtient tous les noms de cartes du dossier ressources/maps.
     * @return Un tableau des noms de cartes.
     */
    private String[] getMapNames() {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);
        File directory = new File(path + s + "ressources" + s + "maps");
        if (directory.list() != null) {
            String[] res = directory.list();
            for (int i = 0; i < res.length; i++) {
                res[i] = res[i].substring(0, res[i].length() - 4);
            }
            return res;
        }
        return new String[]{};
    }

    /**
     * Crée un panel de niveau unique, qui est un bouton avec le nom de la carte.
     * @param level_name Le nom de la carte.
     * @return Le bouton avec le nom de la carte.
     */
    private JButton makeSingleLevelPanel(String level_name) {
        JButton res = new JButton(level_name);
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        res.addActionListener( e -> {
            level_name_tag.setText(level_name);
            frame.playSoundClick("MENU" ,"SelectMap.wav");
        });
        return res;
    }

    /**
     * Obtient tous les boutons des cartes disponibles et les met dans un panel.
     * @return Le panel contenant les boutons des cartes.
     */
    private JPanel makeLevelPanel() {
        JPanel res = makeDefaultPanel();
        boolean flag = true;
        int map_count = getMapCount();
        if (map_count > 0) {
            res.setLayout(new GridLayout(map_count, 1));
            for (String level_name : getMapNames()) {
                if (flag) {
                    flag = false;
                    level_name_tag = new JLabel(level_name);
                }
                res.add(makeSingleLevelPanel(level_name));
            }
        }
        return res;
    }

    /**
     * Crée un JPanel contenant un bouton qui change le mode de jeu.
     * @return Le panel contenant le bouton de mode.
     */
    private JPanel makeModePanel() {
        JPanel res = makeDefaultPanel();
        res.setLayout(new GridLayout(1, 2));
        mode_button = new JButton("NORMAL");
        mode_button.setBackground(Color.BLACK);
        mode_button.setForeground(Color.GRAY);
        mode_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mode = mode_button.getText();
                if ( mode.equals("NORMAL")) {
                    frame.playSoundClick("MENU" ,"ModeNuke.wav");
                } else {
                    frame.playSoundClick("MENU" ,"ModeNormal.wav");
                }
                for (int i = 0; i < possible_modes.length; i++) {
                    if (possible_modes[i].equals(mode)) {
                        if (i < possible_modes.length - 1) {
                            mode_button.setText(possible_modes[i + 1]);
                        } else {
                            mode_button.setText(possible_modes[0]);
                        }
                    }
                }
            }
        });
        mode_button.setPreferredSize(new Dimension(frame.getWidth() / 8, frame.getHeight() / 10));
        JLabel modeString = new JLabel("Mode: ");
        modeString.setForeground(Color.GRAY);
        JPanel modeStringCapsule = makeCenteringPanel(modeString);
        res.add(modeStringCapsule);
        res.add(mode_button);
        return res;
    }

    /**
     * Rassemble le panel des niveaux et le panel du mode (qui est juste un panel avec un bouton) dans un JPanel.
     * @return Le JPanel contenant les deux panels.
     */
    private JPanel makeLevelNModePanel() {
        JPanel res = makeDefaultPanel();
        res.setLayout(new BorderLayout());
        res.add(makeLevelPanel(), BorderLayout.CENTER);
        res.add(makeModePanel(), BorderLayout.SOUTH);
        return res;
    }

    /**
     * Initialise level_name_tag, qui est utilisé pour voir la carte choisie (également nécessaire pour démarrer le jeu).
     * @return Le JPanel encapsulant level_name_tag.
     */
    private JPanel makeShowConfigPanel() {
        JPanel res = makeDefaultPanel();
        JPanel lnt_capsule = makeDefaultPanel(); // encapsuleur du nom de la carte
        level_name_tag.setHorizontalTextPosition(SwingConstants.CENTER);
        level_name_tag.setVerticalTextPosition(SwingConstants.CENTER);
        level_name_tag.setBackground(Color.BLACK);
        level_name_tag.setForeground(Color.GRAY);
        lnt_capsule.add(level_name_tag);
        res.add(lnt_capsule);
        return res;
    }

    /**
     * Rassemble le LevelPanel (boutons qui affichent les noms des cartes) et le config panel (affiche la carte choisie).
     * @return Le JPanel qui rassemble les deux panels ci-dessus.
     */
    private JPanel makeLevelNConfigPanel() {
        JPanel res = makeDefaultPanel();
        res.setLayout(new BorderLayout());
        res.add(makeLevelNModePanel(), BorderLayout.CENTER);
        res.add(makeShowConfigPanel(), BorderLayout.SOUTH);
        return res;
    }

    /**
     * Crée le bouton qui dit GO!, qui démarre le jeu en utilisant les paramètres sélectionnés dans le menu.
     * @return Le bouton encapsulé dans un JPanel.
     */
    private JPanel makePlayButton() {
        JPanel res = makeDefaultPanel();
        JButton play_button = new JButton("GO!");
        play_button.setBackground(Color.BLACK);
        play_button.setForeground(Color.GRAY);
        play_button.addActionListener(e -> {
            if (frame.getSound().getclick())
                frame.getSound().playSound("MENU", "StartButtonSound.wav");
            String[] player_name_list = new String[4];
            for (int i = 0; i < 4; i++) {
                player_name_list[i] = player_name_list_tag[i].getText();
            }
            String[] player_type_list = new String[4];
            for (int i = 0; i < 4; i++) {
                player_type_list[i] = player_type_list_tag[i].getText();
            }

            if(!differentcolors()){
                frame.playSoundClick("MENU" , "popUp.wav");
                JOptionPane.showMessageDialog(this,"Veuillez choisir des couleurs différentes !","Warning",JOptionPane.WARNING_MESSAGE);
            }
            else if(verifSupTwoPlayer(player_type_list)){
                frame.playSoundClick("MENU" , "popUp.wav");
                JOptionPane.showMessageDialog(this,"Il faut plus de joueur !","Robocop",JOptionPane.WARNING_MESSAGE);

            }
            else
            {
                if (!level_name_tag.getText().isEmpty())
                {
                    frame.startGame(level_name_tag.getText(), mode_button.getText(), player_name_list, player_type_list, player_colors);
                }
            }
        });
        res.add(play_button);
        return res;
    }

    /**
     * Vérifie si les couleurs des joueurs sont différentes.
     * @return True si les couleurs sont différentes, sinon False.
     */
    private boolean differentcolors() {
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (player_colors[i].equals(player_colors[j])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Rassemble le LevelNConfigPanel et le bouton play dans un JPanel.
     * @return Le panel contenant les deux éléments.
     */
    private JPanel makeMapListPanel() {
        JPanel res = new JPanel();
        res.setLayout(new BorderLayout());
        res.add(makeLevelNConfigPanel(), BorderLayout.CENTER);
        res.add(makePlayButton(), BorderLayout.SOUTH);
        return res;
    }

    /**
     * Vérifie qu'il y a au moins 2 joueurs.
     * @param listeJoueur La liste des joueurs.
     * @return True si le nombre de joueurs est supérieur à 2, sinon False.
     */
    private boolean verifSupTwoPlayer(String[] listeJoueur) {
        int compteur = 0;
        for (String s : listeJoueur) {
            if (s.equals("NONE")) {
                compteur++;
            }
        }
        return compteur > 2;
    }

    /**
     * Vérifie si le joueur est le joueur 1.
     * @param i Le numéro du joueur.
     * @return True si le joueur est le joueur 1, sinon False.
     */
    private boolean isPlayerOne(int i) {
        return i == 0;
    }
}
