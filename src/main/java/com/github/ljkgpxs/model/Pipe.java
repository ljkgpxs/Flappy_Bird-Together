package com.github.ljkgpxs.model;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.ljkgpxs.physics.PhysicsBody;
import com.github.ljkgpxs.physics.shape.RectangleShape;

public class Pipe extends Sprite {
    public Pipe(Position position, int length) {
        mAnimator = new Animator();
        mAnimator.setDuration(500);
        try {
            if (position.y < 0)
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/pipe_down.png")));
            else mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/pipe_up.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPhysicsBody = new PhysicsBody();

        mPhysicsBody.setPosition(position);
        mPhysicsBody.setCollideCode(0x01);
        mPhysicsBody.setSpeed(new Vector(0, 0));
        mPhysicsBody.setShape(new RectangleShape(90, 600));
        mPhysicsBody.setWeight(0);
        mPhysicsBody.setGravityEnable(false);
        mPhysicsBody.setFixed(true);

        this.setAnimator(mAnimator);
        this.setPhysicsBody(mPhysicsBody);
    }
}
