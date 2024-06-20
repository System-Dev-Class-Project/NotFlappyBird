package flappymappydeluxe;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * The {@code AudioPlayer} class provides methods to play and stop audio files.
 * This class uses the Java Sound API to handle audio playback.
 */
public class AudioPlayer {
    private Clip clip;
    private boolean isPlaying = false;

    /**
     * Plays the audio file specified by the file path.
     * The audio will loop continuously until stopped.
     *
     * @param filePath the path to the audio file
     */
    public void play(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the audio
            isPlaying = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the audio playback if the audio is currently playing.
     * The audio clip is also closed to release system resources.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            isPlaying = false;
        }
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
}
    

