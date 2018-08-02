package tests;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import scenes.ResultScene;

public class TestScene {
    public static void main(String[] args) {

        List<Long> longs = new CopyOnWriteArrayList<>();
        longs.add(100000L);
        longs.add(200000L);
        longs.add(50000L);
        new ResultScene(longs);
    }
}
