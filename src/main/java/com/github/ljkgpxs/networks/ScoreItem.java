package com.github.ljkgpxs.networks;

public class ScoreItem {
    public String tag;
    public Long time;

    public ScoreItem() {
        tag = "";
        time = Long.MAX_VALUE;
    }

    public ScoreItem(String tag, long time) {
        this.tag = tag;
        this.time = time;
    }
}
