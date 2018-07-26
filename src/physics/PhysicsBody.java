package physics;

import listeners.OnCollideListener;
import model.Position;
import model.Sprite;
import model.Vector;
import physics.shape.BaseShape;

/**
 * 物理物体, 用于添加至World
 */
public class PhysicsBody {
    private Position mPosition;
    private BaseShape mShape;
    private long mCollideCode;
    private Vector mSpeed;
    private double mWeight;
    private double mAngle;

    private boolean mIsGravityEnable = true;

    private Sprite mParentSprite;

    private OnCollideListener mOnCollideListener;


    public boolean tigerCollide(PhysicsBody body) {
        return mOnCollideListener.onCollide(mParentSprite, body.mParentSprite);
    }

    public void setOnCollideListener(OnCollideListener listener) {
        mOnCollideListener = listener;
    }

    public Sprite getParentSprite() {
        return mParentSprite;
    }

    public void setParentSprite(Sprite sprite) {
        mParentSprite = sprite;
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    public BaseShape getShape() {
        return mShape;
    }

    public void setShape(BaseShape shape) {
        mShape = shape;
    }

    public long getCollideCode() {
        return mCollideCode;
    }

    public void setCollideCode(long collideCode) {
        mCollideCode = collideCode;
    }

    public Vector getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Vector speed) {
        mSpeed = speed;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public double getAngle() {
        return mAngle;
    }

    public void setAngle(double angle) {
        mAngle = angle;
    }

    public boolean isGravityEnable() {
        return mIsGravityEnable;
    }

    public void setGravityEnable(boolean gravityEnable) {
        mIsGravityEnable = gravityEnable;
    }
}
