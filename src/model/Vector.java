package model;

public class Vector {
    public int x;
    public int y;

    /**
     * 构造向量, 默认x和y方向为0
     */
    public Vector() {
        this(0, 0);
    }

    /**
     * 构造向量, 指定x, y方向的大小
     * @param x x方向大小
     * @param y y方向大小
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
