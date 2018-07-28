package physics.shape;

public class RectangleShape extends BaseShape {
    private double mWidth;
    private double mHeight;

    public RectangleShape() {
        this(0.0, 0.0);

    }

    public RectangleShape(double width, double height) {
        mHeight = height;
        mWidth = width;
    }

    public int getWidth() {
        return (int) mWidth;
    }

    public void setWidth(double width) {
        mWidth = width;
    }

    public int getHeight() {
        return (int) mHeight;
    }

    public void setHeight(double height) {
        mHeight = height;
    }
}
