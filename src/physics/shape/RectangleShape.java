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

    public double getWidth() {
        return mWidth;
    }

    public void setWidth(double width) {
        mWidth = width;
    }

    public double getHeight() {
        return mHeight;
    }

    public void setHeight(double height) {
        mHeight = height;
    }
}
