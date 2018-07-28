package physics.shape;

public class CircleShape extends BaseShape {
    private double mRadius;

    public CircleShape() {
        this(0.0);
    }

    public CircleShape(double radius) {
        mRadius = radius;
    }

    public double getRadius() {
        return mRadius;
    }

    public void setRadius(double radius) {
        mRadius = radius;
    }

    @Override
    public int getWidth() {
        return (int) (mRadius * 2);
    }

    @Override
    public int getHeight() {
        return (int) (mRadius * 2);
    }
}
