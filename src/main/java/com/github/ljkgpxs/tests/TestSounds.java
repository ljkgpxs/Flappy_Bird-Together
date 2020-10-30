package com.github.ljkgpxs.tests;

import static java.lang.Thread.sleep;

import com.github.ljkgpxs.utils.AudioPlay;

public class TestSounds {
    public static void main(String[] args) {
        AudioPlay.playMusic("/resources/sounds/bgm1.wav", 5);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AudioPlay.stopAll();

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
