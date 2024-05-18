package com.view.mainmenu;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings extends JPanel
{
    private final Menu main;

    // Resolution
    private JComboBox<String> res_box;
    private final int[][] resolutions = {{800, 500}, {800, 600}, {1000, 800}, {1280, 720}, {1920, 1080}};


    // Fullscreen
    private JCheckBox fs_cb;

    // Sound
    private JCheckBox music_cb;
    private JCheckBox click_cb;
    private static SliderWithValueLabel volume_slider;
    private JLabel valueLabel;
    private int valeur_slider = 50;


    public Settings(Menu main)
    {
        this.main = main;
        makeSettings();
    }

    private void makeSettings()
    {
        this.setLayout(new GridLayout(3, 1));
        // Maybe add only rows and put everything seperate each row?

        // Resolution setting
        add(makeResolutionSetting());
        // Fullscreen setting
        add(makeFullscreenSetting());
        // Sound management
        add(makeSoundSetting());

    }

    private JPanel makeBlackBox()
    {
        JPanel blackbox = new JPanel();
        blackbox.setBackground(Color.BLACK);
        return blackbox;
    }

    private JPanel makeResolutionSetting()
    {
        JPanel resolution_panel = new JPanel();
        resolution_panel.setLayout(new GridLayout(1, 3));
        this.res_box = makeResBox();
        JButton choose_res = makeResChooseButton();

        resolution_panel.add(res_box);
        resolution_panel.add(makeBlackBox());
        resolution_panel.add(choose_res);

        return resolution_panel;
    }

    private JComboBox<String> makeResBox()
    {
        // Making the ComboBox to choose the resolution
        JComboBox<String> res = new JComboBox<>();
        for(int[] couple : resolutions)
        {
            res.addItem(couple[0] + " x " + couple[1]);
        }
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        res.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound( "MENU" , "ChangeSizeOfTheScreen.wav");
            }
        });
        return res;
    }
    

    private JButton makeResChooseButton()
    {
        JButton res = new JButton("Choisir cette resolution");
        res.addActionListener(
                new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound( "MENU" ,"ChangeSizeOfTheScreen.wav");
                int index = res_box.getSelectedIndex();
                int[] res = resolutions[index];
                main.setAllSize(res[0], res[1]);
                main.getFrame().setLocationRelativeTo(null);
            }
        });

        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);


        return res;
    }



    private JPanel makeFullscreenSetting()
    {
        JPanel fullscreen_panel = new JPanel();
        fullscreen_panel.setLayout(new GridLayout(1, 3));

        fs_cb = makeFSCheckBox();
        fs_cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick()) main.getFrame().getSound().playSound( "MENU" ,"ChangeSizeOfTheScreen.wav");
            }
        });
        fullscreen_panel.add(fs_cb);
        fullscreen_panel.add(makeBlackBox());
        fullscreen_panel.add(makeFSButton());

        return fullscreen_panel;
    }

    private JPanel makeSoundSetting()
    {
        JPanel sound_panel = new JPanel();
        sound_panel.setLayout(new GridLayout(1, 3));

        JPanel sound_buttons = makeBlackBox();
        sound_buttons.setLayout(new GridLayout(2, 1));

        music_cb = makeMusicCheckBox();
        music_cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick()) main.getFrame().getSound().playSound("MENU" ,"ChangePageInTheMenu.wav");
            }
        });
        click_cb = makeClickSoundCheckBox();
        click_cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(main.getFrame().getSound().getclick())
                    main.getFrame().getSound().playSound("MENU" ,"ChangePageInTheMenu.wav");
            }
        });

        sound_buttons.add(music_cb);
        sound_buttons.add(click_cb);
        sound_panel.add(sound_buttons);
        sound_panel.add(makeVolumeSlider());
        sound_panel.add(makeSButton());
        volume_slider.setSize((int)(volume_slider.getSize().getWidth()*0.8),(int)(volume_slider.getSize().getHeight()*0.8));

        return sound_panel;
    }

    private JCheckBox makeFSCheckBox()
    {
        JCheckBox res = new JCheckBox("Fullscreen");
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

    private JButton makeFSButton()
    {
        JButton res = new JButton("Choisir cet option de fullscreen");
        res.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(main.getFrame().getSound().getclick()) main.getFrame().getSound().playSound("MENU" ,"ChangePageInTheMenu.wav");
                        if(fs_cb.isSelected())
                        {
                            main.getDevice().setFullScreenWindow(main.getFrame());
                            main.setAllSize(main.getFrame().getWidth(), main.getFrame().getHeight());
                            fs_cb.setSelected(true);
                        }
                        else
                        {
                            // Si pas de fullscreen il retourne a setting de resolution.
                            main.getDevice().setFullScreenWindow(null);
                            int[] res = resolutions[res_box.getSelectedIndex()];
                            main.setAllSize(res[0], res[1]);
                        }
                        if(music_cb.isSelected())
                        {
                            main.getFrame().getSound().setMusic(true);
                        }else
                        {
                            main.getFrame().getSound().setMusic(false);
                        }

                    }
                });

        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);


        return res;
    }
    private JButton makeSButton()
    {
        JButton res = new JButton("Choisir ses options de son");
        res.addActionListener(
                new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(music_cb.isSelected()){
                            main.getFrame().getSound().setMusic(true);
                        }else{
                            main.getFrame().getSound().setMusic(false);
                            main.getFrame().getSound().stop();
                        }
                        if(click_cb.isSelected()){
                            main.getFrame().getSound().setClick(true);
                        }else{
                            main.getFrame().getSound().setClick(false);
                        }

                        if(main.getFrame().getSound().getclick()) main.getFrame().getSound().playSound("MENU" ,"ChangePageInTheMenu.wav");

                        main.getFrame().getSound().setVolume((float)volume_slider.getValue()/100.0f);
                    }
                });

        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);

        return res;
    }

    private JCheckBox makeMusicCheckBox()
    {
        JCheckBox res = new JCheckBox("Music" , true );
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

    private JCheckBox makeClickSoundCheckBox()
    {
        JCheckBox res = new JCheckBox("Click Sound" , true );
        res.setBackground(Color.BLACK);
        res.setForeground(Color.GRAY);
        return res;
    }

    private JPanel makeVolumeSlider()
    {   
        JPanel volume_panel = makeBlackBox();
        volume_panel.setLayout(new BorderLayout(0,0));

        volume_slider = new SliderWithValueLabel(0, 100, valeur_slider);
        volume_slider.setBackground(Color.BLACK);
        volume_slider.setForeground(Color.GRAY);
        JPanel volume_slider_panel = makeBlackBox();
        volume_slider_panel.setLayout(new BorderLayout());

        volume_slider_panel.add(volume_slider, BorderLayout.CENTER);

        valueLabel.setForeground(Color.GRAY);
        // volume_slider.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        
        JLabel text = new JLabel("Volume  ");
        text.setBackground(Color.BLACK);
        text.setForeground(Color.GRAY);
        text.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        //text.setSize(12,12);

        volume_panel.add(text, BorderLayout.WEST);
        volume_panel.add(makeBlackBox(), BorderLayout.EAST);
        volume_panel.add(volume_slider_panel,BorderLayout.CENTER);
        
        volume_panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        // volume_panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        return volume_panel;
    }

    public class SliderWithValueLabel extends JSlider {
        public SliderWithValueLabel(int min, int max, int value) {
            super(min, max, value);
            setLayout(new BorderLayout());
            valueLabel = new JLabel(Integer.toString(value), SwingConstants.CENTER);
            valueLabel.setPreferredSize(new Dimension(40, 20));
            valueLabel.setVisible(true);
            add(valueLabel, BorderLayout.NORTH);
            
            addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int val = getValue();
                    valueLabel.setText(Integer.toString(val));
                    Rectangle thumbBounds = getThumbBounds();
                    valueLabel.setLocation(thumbBounds.x + thumbBounds.width / 2 - valueLabel.getWidth() / 2,
                                           thumbBounds.y - valueLabel.getHeight());
                }
            });
        }
    
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Rectangle thumbBounds = getThumbBounds();
            if(Integer.valueOf(valueLabel.getText())!= 100){
            valueLabel.setLocation(thumbBounds.x + thumbBounds.width / 2 - valueLabel.getWidth() / 2,
                                   thumbBounds.y - valueLabel.getHeight());
            }else{
                valueLabel.setLocation(thumbBounds.x + thumbBounds.width / 2 - valueLabel.getWidth() / 2 - 3,
                thumbBounds.y - valueLabel.getHeight());}
        }
    
        private Rectangle getThumbBounds() {
            int valuePosition = (int) ((double) (getValue() - getMinimum()) / (getMaximum() - getMinimum()) * (getWidth() - 16));
            int trackY = (getHeight() - getPreferredSize().height) / 2;
            return new Rectangle(valuePosition, trackY, 16, 16);
        }
    
    }
    public static SliderWithValueLabel getVolume_slider() {
        return volume_slider;
    }

}
