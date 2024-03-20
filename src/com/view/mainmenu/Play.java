package com.view.mainmenu;

import com.view.GameFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Play extends JPanel
{
    private JLabel level_name_tag;
    private JTextArea[] player_name_list_tag;
    private JLabel[] player_type_list_tag;
    private Color[] player_colors = {Color.red,Color.red,Color.red,Color.red};
    private JButton[] selected_color = new JButton[4];

    private final String
            PLAYER = "PLAYER",
            CPU = "CPU";

    private GameFrame frame;

    public Play(GameFrame frame)
    {
        this.frame = frame;

        makePlay();
    }

    @Override
    public void setSize(int width, int height)
    {
        makePlay();
    }

    private void makePlay()
    {
        setLayout(new BorderLayout());
        initializeTables();
        add(makeAllPlayersPanel());
        add(makeMapListPanel(), BorderLayout.EAST);

    }

    private void initializeTables()
    {
        player_name_list_tag = new JTextArea[4];
        for (int i = 0; i < 4; i++)
        {
            player_name_list_tag[i] = new JTextArea("Player " + (i + 1));
        }

        player_type_list_tag = new JLabel[4];
        for (int i = 0; i < 4; i++)
        {
            player_type_list_tag[i] = new JLabel(PLAYER);
        }
        
    }


    private JPanel makeDefaultPanel()
    {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

    private JPanel makeCenteringPanel(JComponent comp)
    {
        JPanel res = makeDefaultPanel();

        res.setLayout(new GridLayout(3, 3));

        for (int j = 0; j < 4; j++)
        {
            res.add(makeDefaultPanel());
        }

        res.add(comp);

        for (int j = 0; j < 4; j++)
        {
            res.add(makeDefaultPanel());
        }

        return res;
    }


    // START OF PLAYER PANEL FUNCTIONS

    /**
     * Gathers 4 players in 1 panel.
     * @return the panel that contains 4 players.
     */
    private JPanel makeAllPlayersPanel()
    {
        JPanel res = makeDefaultPanel();
        res.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        res.setLayout(new GridLayout(4, 1));

        for (int i = 0; i < 4; i++)
        {
            res.add(makeSinglePlayerPanel(i));
        }

        return res;
    }


    /**
     * Makes the name part of the player panel. Also adds it to the player_name_list_tag.
     * @param i the player number.
     * @return the TextArea capsulated in JPanel.
     */
    private JPanel makePlayerNamePanel(int i)
    {
        JTextArea playerName = new JTextArea("Player " + (i + 1));
        playerName.setBackground(Color.BLACK);
        playerName.setForeground(Color.GRAY);

        player_name_list_tag[i] = playerName;

        return makeCenteringPanel(playerName);
    }


    /**
     * Makes a button that toggles CPU and Player.
     * @param i player number
     * @return the panel containing the said button.
     */
    private JPanel makeCPUPlayerSelectorPanel(int i)
    {
        player_type_list_tag[i] = new JLabel(PLAYER);

        JButton button = new JButton(PLAYER);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.GRAY);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        button.addActionListener(e ->
        {
            if (player_type_list_tag[i].getText().equals(PLAYER))
            {
                player_type_list_tag[i].setText(CPU);
                button.setText(CPU);
            }
            else
            {
                player_type_list_tag[i].setText(PLAYER);
                button.setText(PLAYER);
            }
        });

        return makeCenteringPanel(button);
    }

    /**
     * Makes a liste for choosing player's coulour.
     * @param i player number
     * @return the panel containing the said button.
     */
    private JComponent makePlayerCoulourChoicePanel(int i)
    {
        JPanel colors = makeDefaultPanel();
        colors.setLayout(new GridLayout(1,5));
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
            if(selected_color[i] != red){
            if(selected_color[i] != null)
            selected_color[i].setBorder(new EmptyBorder(5,5,5,5));
            red.setBorder(new LineBorder(Color.WHITE, 5));
            selected_color[i] = red;
            player_colors[i] = Color.red;
            }
            }
        });
        redPanel.setLayout(new GridLayout(3,1));
        redPanel.setBorder(new EmptyBorder(5,5,5,5));
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
            if(selected_color[i] != blue){
            if(selected_color[i] != null)
            selected_color[i].setBorder(new EmptyBorder(5,5,5,5));
            blue.setBorder(new LineBorder(Color.WHITE, 5));
            selected_color[i] = blue;
            player_colors[i] = Color.blue;
            }
            }
        });
        bluePanel.setLayout(new GridLayout(3,1));
        bluePanel.setBorder(new EmptyBorder(5,5,5,5));
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
            if(selected_color[i] != green) {
            if(selected_color[i] != null)
            selected_color[i].setBorder(new EmptyBorder(5,5,5,5));
            green.setBorder(new LineBorder(Color.WHITE, 5));
            selected_color[i] = green;
            player_colors[i] = Color.green;
        }
            }
        });
        greenPanel.setLayout(new GridLayout(3,1));
        greenPanel.setBorder(new EmptyBorder(5,5,5,5));
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
            if(selected_color[i] != yellow){
            if(selected_color[i] != null)
            selected_color[i].setBorder(new EmptyBorder(5,5,5,5));
            yellow.setBorder(new LineBorder(Color.WHITE, 5));
            selected_color[i] = yellow;
            player_colors[i] = Color.yellow;
            }
            }
        });
        yellowPanel.setLayout(new GridLayout(3,1));
        yellowPanel.setBorder(new EmptyBorder(5,5,5,5));
        yellowPanel.add(makeDefaultPanel());
        yellowPanel.add(yellow);
        return yellowPanel;
    }

    /**
     * Makes a single player panel.
     * @param i The number that differentiates the players
     * @return the player panel
     */
    private JPanel makeSinglePlayerPanel(int i)
    {
        JPanel res = makeDefaultPanel();
        res.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        res.setLayout(new GridLayout(1, 3));

        res.add(makePlayerNamePanel(i));

        res.add(makeCPUPlayerSelectorPanel(i));

        res.add(makePlayerCoulourChoicePanel(i));


        return res;
    }
















    /*
     * START OF LEVEL LIST FUNCTIONS
     */

    private String findSlash(String p)
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

    private int getMapCount()
    {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);

        File directory = new File(path + s + "ressources" + s + "maps");
        int map_count = 0;
        if(directory.list() != null)
        {
            map_count = directory.list().length;
        }
        return map_count;
    }

    private String[] getMapNames()
    {
        String path = System.getProperty("user.dir");
        String s = findSlash(path);

        File directory = new File(path + s + "ressources" + s + "maps");
        if(directory.list() != null)
        {
            String[] res = directory.list();
            for(int i = 0; i < res.length; i++)
            {
                res[i] = res[i].substring(0, res[i].length()-4);
            }
            return res;
        }
        return new String[]{};
    }

    private JButton makeSingleLevelPanel(String level_name)
    {
        JButton res = new JButton(level_name);
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        res.addActionListener( e -> { level_name_tag.setText(level_name); });
        return res;
    }

    private JPanel makeLevelPanel()
    {
        JPanel res = makeDefaultPanel();
        int map_count = getMapCount();
        if(map_count > 0)
        {
            res.setLayout(new GridLayout(map_count, 1));
            for (String level_name : getMapNames())
            {
                res.add(makeSingleLevelPanel(level_name));
            }
        }
        return res;
    }


    /*
     * END OF LEVEL LIST FUNCTIONS
     */




    private JPanel makeShowConfigPanel()
    {
        JPanel res = makeDefaultPanel();

        JPanel lnt_capsule = makeDefaultPanel(); // level name tag capsule

        level_name_tag = new JLabel("Map1");
        level_name_tag.setHorizontalTextPosition(SwingConstants.CENTER);
        level_name_tag.setVerticalTextPosition(SwingConstants.CENTER);
        level_name_tag.setBackground(Color.BLACK);
        level_name_tag.setForeground(Color.GRAY);

        lnt_capsule.add(level_name_tag);

        res.add(lnt_capsule);

        return res;
    }


    private JPanel makeLevelNConfigPanel()
    {
        JPanel res = makeDefaultPanel();

        res.setLayout(new BorderLayout());

        res.add(makeLevelPanel(), BorderLayout.CENTER);
        res.add(makeShowConfigPanel(), BorderLayout.SOUTH);

        return res;
    }



    private JPanel makePlayButton()
    {
        JPanel res = makeDefaultPanel();

        JButton play_button = new JButton("GO!");
        play_button.setBackground(Color.BLACK);
        play_button.setForeground(Color.GRAY);

        play_button.addActionListener(e ->
                    {
                        String[] player_name_list = new String[4];
                        for (int i = 0; i < 4; i++)
                        {
                            player_name_list[i] = player_name_list_tag[i].getText();
                        }
                        String[] player_type_list = new String[4];
                        for (int i = 0; i < 4; i++)
                        {
                            player_type_list[i] = player_type_list_tag[i].getText();
                        }
                        if(!differentcolors()){
                            JOptionPane.showMessageDialog(this,"Veuillez choisir des couleurs différentes !","Warning",JOptionPane.WARNING_MESSAGE);
                        }else{
                        frame.startGame(level_name_tag.getText(), player_name_list, player_type_list,player_colors);
                    }
                    });

        res.add(play_button);

        return res;
    }

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

    private JPanel makeMapListPanel()
    {
        JPanel res = new JPanel();
        res.setLayout(new BorderLayout());
        res.add(makeLevelNConfigPanel(), BorderLayout.CENTER);

        res.add(makePlayButton(), BorderLayout.SOUTH);

        return res;
    }

}
