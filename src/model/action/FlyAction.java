package model.action;

import model.Position;
import model.Vector;
import utils.MMath;

public class FlyAction extends Action {

    private long mLastTime = 0;

    private Position mDestPos;
    private int mXDistance, mYDistance;

    /**
     * 从src移动到dest
     * @param src 起始位置
     * @param dest 终止位置
     * @param duration 动作持续时间
     */
    public FlyAction(Position src, Position dest, long duration) {
        mDestPos = dest;
        mXDistance = dest.x - src.x;
        mYDistance = dest.y - src.y;
        setDuration(duration);
    }

    /**
     * 从src移动distance的距离, distance为向量
     * @param src 起始位置
     * @param distance 移动距离向量
     * @param duration 持续时间
     */
    public FlyAction(Position src, Vector distance, long duration) {
        mDestPos = new Position(src.x + distance.x, src.y + distance.y);
        mXDistance = distance.x;
        mYDistance = distance.y;
        setDuration(duration);
    }

    @Override
    public Position getNextPosition(Position current) {
        if (mPlayedCount >= 1) {
            return current;
        }
        Position pos = new Position(current.x, current.y);
        if (mLastTime == 0) {
            mLastTime = System.currentTimeMillis();
            return current;
        }

        long t = System.currentTimeMillis() - mLastTime;
        int xs = (int) (mXDistance * ((double)t / getDuration()));
        int ys = (int) (mYDistance * ((double)t / getDuration()));

        System.out.println("t: " + t + ", xs: " + xs + ", ys: " + ys);

        // 检查相隔时间是否足够运动
        if (MMath.abs(xs) <= 1) {
            if (mXDistance != 0)
                return pos;
        }
        if (MMath.abs(ys) <= 1) {
            if (mYDistance != 0)
                return pos;
        }

        pos.x = pos.x + xs;
        pos.y = pos.y + ys;

        System.out.println(pos.getDistance(mDestPos) + ", " + pos.x + " " + pos.y + ", " + mDestPos.x + " " + mDestPos.y);
        if (pos.getDistance(mDestPos) < 10) {
            mPlayedCount++;
            mLastTime = 0;
        }
        mLastTime = System.currentTimeMillis();
        return pos;
    }
}
