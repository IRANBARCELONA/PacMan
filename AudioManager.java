import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class AudioManager {

    private static Map<String, Boolean> loopingMap = new HashMap<>();
    private static Map<String, Clip> sounds = new HashMap<>();
    private static Map<String, Boolean> isMusicMap = new HashMap<>();
    private static float musicVolume = 0.5f;
    private static float sfxVolume = 0.5f;

    public static void loadSound(String name, String filePath, boolean isMusic) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            sounds.put(name, clip);
            isMusicMap.put(name, isMusic);
            setVolume(name, isMusic ? musicVolume : sfxVolume);
        } catch (Exception e) {
            System.out.println("Error loading sound: " + name + " - " + e.getMessage());
        }
    }

    public static void playLooping(String name) {
        Clip clip = sounds.get(name);
        if (clip != null) {
            loopingMap.put(name, true);

            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (loopingMap.getOrDefault(name, false)) {
                        clip.setFramePosition(0);
                        clip.start();
                    }
                }
            });

            clip.start();
        }
    }

    public static void play(String name, boolean loop) {
        Clip clip = sounds.get(name);
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public static void stop(String name) {
        Clip clip = sounds.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void stopLooping(String name) {
        loopingMap.put(name, false);
        Clip clip = sounds.get(name);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void setMusicVolume(float volume) {
        musicVolume = volume;
        for (String name : sounds.keySet()) {
            if (isMusicMap.getOrDefault(name, false)) {
                setVolume(name, musicVolume);
            }
        }
    }

    public static void setSfxVolume(float volume) {
        sfxVolume = volume;
        for (String name : sounds.keySet()) {
            if (!isMusicMap.getOrDefault(name, false)) {
                setVolume(name, sfxVolume);
            }
        }
    }

    public static void setVolume(String name, float volumeLevel) {
        Clip clip = sounds.get(name);
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(Math.max(volumeLevel, 0.0001)) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } catch (Exception e) {
                System.out.println("Volume control not supported for: " + name);
            }
        }
    }
}