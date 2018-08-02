package model.action;

public abstract class Action<E> {
    private long mDuration;

    protected int mPlayedCount = 0;

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    abstract public E getNext(E current);

    public int getPlayedCount() {
        return mPlayedCount;
    }
}
