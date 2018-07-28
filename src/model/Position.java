package model;

import utils.MMath;

public class Position {
    public int x;
    public int y;

    /**
     * 构造位置类, 默认x, y方向为0
     */
    public Position() {
        this(0, 0);
    }

    /**
     * 构造位置类, 指定x, y位置
     * @param x
     * @param y
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public int getDistance(Position p) {
        return MMath.triangle(x - p.x, y - p.y);
    }
}
