package util.sound;

import jdk.incubator.jpackage.main.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class WavPlayer {
    public static synchronized void playSound(final String url) {
        // The wrapper thread is unnecessary, unless it blocks on the
// Clip finishing; see comments.
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                        Main.class.getResourceAsStream("src/main/resources/sounds/" + url));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }
}
