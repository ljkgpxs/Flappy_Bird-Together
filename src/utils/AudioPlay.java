package utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlay {
    private static List<Clip> sClipList = new CopyOnWriteArrayList<>();

    public static void playSound(String path) {
        play(path, 0);
    }

    public static void playMusic(String path, int count) {
        play(path, count);
    }

    private static void play(String path, int count) {
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                sClipList.add(clip);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path));
                clip.open(inputStream);
                clip.addLineListener(lineEvent -> {
                    if (lineEvent.getFramePosition() != 0) {
                        clip.close();
                        clip.stop();
                    }
                });
                clip.loop(count);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void stopAll() {
        for (Clip c : sClipList) {
            if (c != null) {
                c.stop();
                c.close();
                c = null;
            }
        }
        sClipList.clear();
    }
}
