package model;

public class Vector {
    public double x;
    public double y;

    /**
     * 构造向量, 默认x和y方向为0
     */
    public Vector() {
        this(0.0, 0.0);
    }

    /**
     * 构造向量, 指定x, y方向的大小
     * @param x x方向大小
     * @param y y方向大小
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector a) {
        x = a.x;
        y = a.y;
    }
}
