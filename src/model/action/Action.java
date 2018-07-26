package model.action;

import model.Position;

public abstract class Action {
    private long mDuration;

    protected int mPlayedCount = 0;

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    abstract public Position getNextPosition(Position current);

    public int getPlayedCount() {
        return mPlayedCount;
    }
}
