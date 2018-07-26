package model;

import utils.MMath;

public class Position extends Vector {

    /**
     * 构造位置类, 默认x, y方向为0
     */
    public Position() {
        super(0, 0);
    }

    /**
     * 构造位置类, 指定x, y位置
     * @param x
     * @param y
     */
    public Position(int x, int y) {
        super(x, y);
    }

    public int getDistance(Position p) {
        return MMath.triangle(x - p.x, y - p.y);
    }
}
