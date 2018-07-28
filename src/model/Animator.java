package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Animator {
    private long mDuration = 1000;
    private List<Image> mFrames;

    private int mIndex;
    private long mLastTime = 0;

    public Animator() {
        mFrames = new ArrayList<>();
    }

    public void addFrame(Image frame) {
        mFrames.add(frame);
    }

    public Image getNextFrame() {
        if (mLastTime == 0) {
            mIndex = 0;
            if (mFrames.size() > 0) {
                mLastTime = System.currentTimeMillis();
                return mFrames.get(0);
            }
            else return null;
        }

        long t = System.currentTimeMillis() - mLastTime;
        int size = mFrames.size();
        int plus = (int) (t / (mDuration / (double)size));
        if (plus == 0) {
            return mFrames.get(mIndex);
        }
        mIndex += plus;
        mIndex %= size;
        mLastTime = System.currentTimeMillis();
        return mFrames.get(mIndex);
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public void setFrames(List<Image> frames) {
        mFrames = frames;
    }
}
