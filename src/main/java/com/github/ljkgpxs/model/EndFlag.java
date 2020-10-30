package com.github.ljkgpxs.model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.ljkgpxs.physics.PhysicsBody;
import com.github.ljkgpxs.physics.shape.RectangleShape;

public class EndFlag extends Sprite {
    public EndFlag(Position position) {
        mAnimator = new Animator();
        mAnimator.setDuration(50000);
        try {
            mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/end_flag.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPhysicsBody = new PhysicsBody();

        mPhysicsBody.setPosition(position);
        mPhysicsBody.setCollideCode(0x0);
        mPhysicsBody.setSpeed(new Vector(0, 0));
        mPhysicsBody.setShape(new RectangleShape(130, 530));
        mPhysicsBody.setWeight(0);
        mPhysicsBody.setGravityEnable(false);
        mPhysicsBody.setFixed(true);

        this.setAnimator(mAnimator);
        this.setPhysicsBody(mPhysicsBody);
    }
}
