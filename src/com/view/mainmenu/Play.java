package com.view.mainmenu;

import com.view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Play extends JPanel
{
    private JLabel level_name_tag;
    private JLabel[] player_name_list_tag;
    private JLabel[] player_type_list_tag;

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

        add(makeAllPlayersPanel());
        add(makeMapListPanel(), BorderLayout.EAST);

    }



    private JPanel makeDefaultPanel()
    {
        JPanel res = new JPanel();
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }


    // START OF PLAYER PANEL FUNCTIONS

    /**
     * Gathers 4 players in 1 panel
     * @return the panel that contains 4 players
     */
    private JPanel makeAllPlayersPanel()
    {
        JPanel res = makeDefaultPanel();

        res.setLayout(new GridLayout(4, 1));

        for (int i = 0; i < 4; i++)
        {
            res.add(makeSinglePlayerPanel(i + 1));
        }

        return res;
    }



    private JPanel makePlayerNamePanel(int i)
    {
        JPanel res = makeDefaultPanel();

        JTextArea playerName = new JTextArea("Player " + i);
    }


    /**
     * Makes a single player panel.
     * @param i The number that differentiates the players
     * @return the player panel
     */
    private JPanel makeSinglePlayerPanel(int i)
    {
        JPanel res = makeDefaultPanel();

        res.setLayout(new GridLayout(1, 3));

        res.add()


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

        File directory = new File(path + s + "src" + s + "resources" + s + "maps");
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

        File directory = new File(path + s + "src" + s + "resources" + s + "maps");
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
        JPanel res = new JPanel();
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
                        frame.startGame(level_name_tag.getText(), player_name_list, player_type_list);
                    });

        res.add(play_button);

        return res;
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
