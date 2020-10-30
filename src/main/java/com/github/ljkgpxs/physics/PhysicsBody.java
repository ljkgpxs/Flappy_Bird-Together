package com.github.ljkgpxs.physics;

import com.github.ljkgpxs.listeners.OnCollideListener;
import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.model.Sprite;
import com.github.ljkgpxs.model.Vector;
import com.github.ljkgpxs.physics.shape.BaseShape;

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
    private boolean mIsFixed = false;

    private Sprite mParentSprite;

    private OnCollideListener mOnCollideListener;

    public PhysicsBody() {
        mPosition = new Position(0, 0);
        mCollideCode = 0x0;
        mSpeed = new Vector(0, 0);
        mWeight = 0.0;
        mAngle = 0.0;
        mParentSprite = null;
    }


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
        if (mIsFixed)
            return Double.MAX_VALUE;
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

    /**
     * 对刚体施力
     * @param force 力的向量
     */
    public void applyForce(Vector force) {

    }

    /**
     * 对刚体施力
     * @param x x方向力的大小
     * @param y y方向力的大小
     */
    public void applyForce(int x, int y) {

    }

    public boolean isFixed() {
        return mIsFixed;
    }

    public void setFixed(boolean fixed) {
        mIsFixed = fixed;
    }
}
