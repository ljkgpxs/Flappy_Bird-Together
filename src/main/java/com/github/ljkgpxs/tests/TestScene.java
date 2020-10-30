package com.github.ljkgpxs.tests;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.ljkgpxs.networks.ScoreItem;
import com.github.ljkgpxs.scenes.ResultScene;

public class TestScene {
    public static void main(String[] args) {

        List<ScoreItem> longs = new CopyOnWriteArrayList<>();
        longs.add(new ScoreItem("ljkgpxs", 100021));
        longs.add(new ScoreItem("asdasdasdasd", 1203));
        longs.add(new ScoreItem("as", 212));
        new ResultScene(longs);
    }
}
