import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * Used to play background music.
 * Topic of choice 2.
 */
public class Music {
    private Clip clip;

    /**
     * Stops background music.
     */
    public void stopBackgroundMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void playBackgroundMusic(String fileName) {
        stopBackgroundMusic();  // Stop any currently playing music
        try {
            // Open an audio input stream.
            URL soundFile = getClass().getClassLoader().getResource(fileName);

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            // Get a sound clip resource.
            clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);

            // Loop the clip continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Start playing the clip
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


  
    public void playTrack(String track){
        try {
            File sound = new File(track);
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        
        } catch (Exception e) {
            
        }

    }


}
