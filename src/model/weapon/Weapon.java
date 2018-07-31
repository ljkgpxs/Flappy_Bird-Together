package model.weapon;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Animator;
import model.Position;
import model.Sprite;
import model.Vector;
import physics.PhysicsBody;
import physics.shape.CircleShape;
import physics.shape.RectangleShape;

public abstract class Weapon extends Sprite {

    public Weapon(Position position) {
        mPhysicsBody = new PhysicsBody();
        mAnimator = new Animator();

        mPhysicsBody = new PhysicsBody();
        mPhysicsBody.setSpeed(new Vector());
        mPhysicsBody.setParentSprite(this);
        mPhysicsBody.setCollideCode(0x1000);
        mPhysicsBody.setWeight(0);
        mPhysicsBody.setShape(new CircleShape(20));
        mPhysicsBody.setFixed(true);
        mPhysicsBody.setPosition(position);
        mPhysicsBody.setGravityEnable(false);
    }

    public abstract int getWidth();
    public abstract int getHeight();

    @Override
    public boolean onCollide(Sprite a) {
        setEnable(false);
        mPhysicsBody.setCollideCode(0);
        return true;
    }
}
