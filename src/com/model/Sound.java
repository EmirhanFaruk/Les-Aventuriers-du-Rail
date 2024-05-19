package com.model;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.*;

public class Sound {
    static Clip c;
    private static Clip musicClip;
    ArrayList<URL> soundURLInGame = new ArrayList<>();
    ArrayList <URL> soundURLMenu = new ArrayList<>();
    ArrayList<URL> soundURLPause = new ArrayList<>() ;
    ArrayList<URL> soundURLEnd = new ArrayList<>() ;
    static String path = System.getProperty("user.dir");
    static String s = findSlash(path);
    private boolean music = true ;
    private boolean click = true ;
    static float volume = 0.5f;
    private static final String[] soundsMenu = {  "tchu-tchu-song.wav" ,"ChangePageInTheMenu.wav" , "ChangeSizeOfTheScreen.wav" , "Colors.wav" ,
            "ModeNormal.wav" , "ModeNuke.wav" , "NormalBot.wav" , "RulesSelection.wav" , "SelectMap.wav" , "StartButtonSound.wav" ,
            "StrongBot.wav" , "WeakBot.wav" , "Player.wav" , "None.wav" , "click.wav" , "popUp.wav" } ;
    private static final String[] soundsInGame = {"mettreRoute.wav" ,"carte-dest.wav" , "carte-wagon.wav" , "inGame.wav" , "tactical-nuke.wav" ,
            "click.wav" , "popUp.wav"} ;
    private static final String[] soundsPause = { "pause.wav" , "click.wav" } ;
    private static final String[] soundsEnd = { "end.wav" , "click.wav" } ;
    private String currentScreen;
    private String currentMusic;


    public Sound(){
        initsoundURL();
    }

    /**
     * Une fonction qui initialise tout les sons
     */
    private void initsoundURL() {
        initSoundList(soundsMenu, soundURLMenu);
        initSoundList(soundsInGame, soundURLInGame);
        initSoundList(soundsPause, soundURLPause);
        initSoundList(soundsEnd, soundURLEnd);
    }

    /**
     * Une fonction qui permet d'ajouter les URL dans la liste
     * @param sounds le nom des sons
     * @param soundURLList la liste
     */
    private void initSoundList(String[] sounds, ArrayList<URL> soundURLList) {
        for (String sound : sounds) {
            File file = new File(prepath() + sound);
            try {
                soundURLList.add(file.toURI().toURL());
            } catch (Exception ignored) {
                // DEBUG : System.out.println("Le son " + sound + " est mal initialiser");
            }
        }
    }

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

    private static String prepath() {
        return s+path+s+"ressources" + s + "Sounds" + s;
    }

    /**
     * Une fonction qui renvoie le bon array en fonction de l'écran
     * @param screen l'écran
     * @return l'array
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
     * Une fonction qui renvoie la bonne liste en fonction de l'écran
     * @param screen l'écran
     * @return la liste
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
     * Une fonction qui renvoie la position de la musique dans la liste
     * @param listMusic la liste
     * @param nameMusic le nom du son
     * @return
     */
    private int searchList ( String[] listMusic , String nameMusic ){
        for ( int i = 0  ; i < listMusic.length ; i++){
            if ( listMusic[i].equals(nameMusic)) {
                return i ;
            }
        }
        return -1 ;
    }
    /**
     * Une fonction qui lance les petits sons
     * @param listMusic la liste de musique
     * @param i sa position dans la liste
     */
    public void setFileInGame( ArrayList<URL> listMusic , int i){
        try {
            c = AudioSystem.getClip();
            AudioInputStream aud = AudioSystem.getAudioInputStream(listMusic.get(i));
            c.open(aud);
        } catch (Exception ignored ) { }
    }

    /**
     * Une fonction qui lance le son de la musique en arriere plan
     * @param listMusic la liste de musique
     * @param i sa position dans la liste
     */
    public void setFileMusic( ArrayList<URL> listMusic , int i){
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
        } catch (Exception ignored) { }
    }

    /**
     * Une fonction qui change la musique en arriere plan
     * @param newScreen nouvel écran
     * @param newMusic nom du son
     */
    public void changeMusic(String newScreen, String newMusic) {
        stopMusic();
        playMusic(newScreen, newMusic);
    }

    /**
     * Une fonction qui joue les longs sons
     * @param screen l'écran
     * @param music nom du son
     */
    public void playMusic( String screen , String  music  ){
        this.currentScreen = screen;
        this.currentMusic = music;
        int i =searchList( listMusic( screen ) , music ) ;
        setFileMusic( arrayMusic( screen )  , i );
        setVolume(volume);
        musicClip.start();
    }

    /**
     * Une fonction qui joue les courts sons
     * @param screen l'écran
     * @param music nom du son
     */
    public void playSound(  String screen , String  music ){
        int i =searchList( listMusic(screen) , music ) ;
        setFileInGame( arrayMusic( screen ) , i );
        setVolume(volume);
        c.start();
    }

    public void stop(){
        c.stop();
    }

    /**
     * Une fonction qui permet d'arrêter la musique
     */
    public void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }


    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean getMusic() {
        return music;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public boolean getclick() {
        return click;
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            return;
        Sound.volume = volume;
        if(c == null)
            return;
        if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
        } else if (c.isControlSupported(FloatControl.Type.VOLUME)) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.VOLUME);
            gainControl.setValue(volume);
        } else {
           //DEBUG : System.out.println("Master Gain and Volume control not supported");
        }
    }

    public int getVolume() {
    return (int)volume;
    }
}
