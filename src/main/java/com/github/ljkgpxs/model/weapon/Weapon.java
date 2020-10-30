package com.github.ljkgpxs.model.weapon;

import com.github.ljkgpxs.model.Animator;
import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.model.Sprite;
import com.github.ljkgpxs.model.Vector;
import com.github.ljkgpxs.physics.PhysicsBody;
import com.github.ljkgpxs.physics.shape.CircleShape;

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
