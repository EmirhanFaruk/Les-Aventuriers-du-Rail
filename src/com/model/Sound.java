package com.model;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Classe gérant les effets sonores et la musique en arrière-plan du jeu.
 */
public class Sound {
    static Clip c;
    private static Clip musicClip;
    ArrayList<URL> soundURLInGame = new ArrayList<>();
    ArrayList<URL> soundURLMenu = new ArrayList<>();
    ArrayList<URL> soundURLPause = new ArrayList<>();
    ArrayList<URL> soundURLEnd = new ArrayList<>();
    static String path = System.getProperty("user.dir");
    static String s = findSlash(path);
    private boolean music = true;
    private boolean click = true;
    static float volume = 0.5f;
    private static final String[] soundsMenu = {
        "tchu-tchu-song.wav", "ChangePageInTheMenu.wav", "ChangeSizeOfTheScreen.wav", "Colors.wav",
        "ModeNormal.wav", "ModeNuke.wav", "NormalBot.wav", "RulesSelection.wav", "SelectMap.wav", "StartButtonSound.wav",
        "StrongBot.wav", "WeakBot.wav", "Player.wav", "None.wav", "click.wav", "popUp.wav"
    };
    private static final String[] soundsInGame = {
        "mettreRoute.wav", "carte-dest.wav", "carte-wagon.wav", "inGame.wav", "tactical-nuke.wav",
        "click.wav", "popUp.wav"
    };
    private static final String[] soundsPause = {"pause.wav", "click.wav"};
    private static final String[] soundsEnd = {"end.wav", "click.wav"};
    private String currentScreen;
    private String currentMusic;

    /**
     * Constructeur de la classe Sound.
     */
    public Sound() {
        initsoundURL();
    }

    /**
     * Initialise tous les sons.
     */
    private void initsoundURL() {
        initSoundList(soundsMenu, soundURLMenu);
        initSoundList(soundsInGame, soundURLInGame);
        initSoundList(soundsPause, soundURLPause);
        initSoundList(soundsEnd, soundURLEnd);
    }

    /**
     * Ajoute les URL des sons dans la liste.
     *
     * @param sounds       Les noms des sons.
     * @param soundURLList La liste des URL des sons.
     */
    private void initSoundList(String[] sounds, ArrayList<URL> soundURLList) {
        for (String sound : sounds) {
            File file = new File(prepath() + sound);
            try {
                soundURLList.add(file.toURI().toURL());
            } catch (Exception ignored) {
                // DEBUG : System.out.println("Le son " + sound + " est mal initialisé");
            }
        }
    }

    /**
     * Trouve le caractère slash correct en fonction du système d'exploitation.
     *
     * @param p Le chemin.
     * @return Le caractère slash correct.
     */
    private static String findSlash(String p) {
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
     * Prépare le chemin pour accéder aux fichiers de sons.
     *
     * @return Le chemin complet.
     */
    private static String prepath() {
        return s + path + s + "ressources" + s + "Sounds" + s;
    }

    /**
     * Retourne la liste des URL des sons en fonction de l'écran.
     *
     * @param screen L'écran actuel.
     * @return La liste des URL des sons.
     */
    private ArrayList<URL> arrayMusic (String screen) {
         switch (screen) {
             case "MENU":
                 return soundURLMenu;
             case "INGAME" :
                 return soundURLInGame;
             case "PAUSE" :
                 return soundURLPause;
             case "END" :
                 return soundURLEnd;
             default :
                 return null;


        }
    }

    /**
     * Retourne la liste des noms des sons en fonction de l'écran.
     *
     * @param screen L'écran actuel.
     * @return La liste des noms des sons.
     */
    private String[] listMusic ( String screen) {
        switch (screen) {
            case "MENU" :
                return soundsMenu;
            case "INGAME" :
                return soundsInGame;
            case "PAUSE" :
                return soundsPause;
            case "END" :
                return soundsEnd;
            default :
                return null;
        }


    }

    /**
     * Trouve la position de la musique dans la liste.
     *
     * @param listMusic La liste des noms des sons.
     * @param nameMusic Le nom du son.
     * @return La position du son dans la liste.
     */
    private int searchList(String[] listMusic, String nameMusic) {
        for (int i = 0; i < listMusic.length; i++) {
            if (listMusic[i].equals(nameMusic)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Lance les petits sons.
     *
     * @param listMusic La liste des URL des sons.
     * @param i         La position du son dans la liste.
     */
    public void setFileInGame(ArrayList<URL> listMusic, int i) {
        try {
            c = AudioSystem.getClip();
            AudioInputStream aud = AudioSystem.getAudioInputStream(listMusic.get(i));
            c.open(aud);
        } catch (Exception ignored) {
        }
    }

    /**
     * Lance le son de la musique en arrière-plan.
     *
     * @param listMusic La liste des URL des sons.
     * @param i         La position du son dans la liste.
     */
    public void setFileMusic(ArrayList<URL> listMusic, int i) {
        try {
            if (musicClip != null && musicClip.isOpen()) {
                musicClip.close();
            }
            musicClip = AudioSystem.getClip();
            AudioInputStream aud = AudioSystem.getAudioInputStream(listMusic.get(i));
            musicClip.open(aud);
            musicClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    musicClip.close();
                    if (music) {
                        playMusic(currentScreen, currentMusic); // Relance la musique
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * Change la musique en arrière-plan.
     *
     * @param newScreen Le nouvel écran.
     * @param newMusic  Le nom du son.
     */
    public void changeMusic(String newScreen, String newMusic) {
        stopMusic();
        playMusic(newScreen, newMusic);
    }

    /**
     * Joue les longs sons.
     *
     * @param screen L'écran actuel.
     * @param music  Le nom du son.
     */
    public void playMusic(String screen, String music) {
        this.currentScreen = screen;
        this.currentMusic = music;
        int i = searchList(listMusic(screen), music);
        setFileMusic(arrayMusic(screen), i);
        setVolume(volume);
        musicClip.start();
    }

    /**
     * Joue les courts sons.
     *
     * @param screen L'écran actuel.
     * @param music  Le nom du son.
     */
    public void playSound(String screen, String music) {
        int i = searchList(listMusic(screen), music);
        setFileInGame(arrayMusic(screen), i);
        setVolume(volume);
        c.start();
    }

    /**
     * Arrête la lecture du son actuel.
     */
    public void stop() {
        c.stop();
    }

    /**
     * Arrête la musique en arrière-plan.
     */
    public void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }

    /**
     * Définit si la musique doit être jouée.
     *
     * @param music true si la musique doit être jouée, sinon false.
     */
    public void setMusic(boolean music) {
        this.music = music;
    }

    /**
     * Retourne si la musique est activée.
     *
     * @return true si la musique est activée, sinon false.
     */
    public boolean getMusic() {
        return music;
    }

    /**
     * Définit si les sons de clic doivent être joués.
     *
     * @param click true si les sons de clic doivent être joués, sinon false.
     */
    public void setClick(boolean click) {
        this.click = click;
    }

    /**
     * Retourne si les sons de clic sont activés.
     *
     * @return true si les sons de clic sont activés, sinon false.
     */
    public boolean getclick() {
        return click;
    }

    /**
     * Définit le volume des sons.
     *
     * @param volume Le volume des sons.
     */
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            return;
        Sound.volume = volume;
        if (c == null)
            return;
        if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        } else if (c.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.VOLUME);
            gainControl.setValue(volume);
        } else {
            // DEBUG : System.out.println("Master Gain and Volume control not supported");
        }
    }

    /**
     * Retourne le volume des sons.
     *
     * @return Le volume des sons.
     */
    public int getVolume() {
        return (int) volume;
    }
}
