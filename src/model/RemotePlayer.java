package model;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import physics.PhysicsBody;
import physics.shape.CircleShape;

public class RemotePlayer extends Sprite {
    private Animator mAnimator;
    private PhysicsBody mPhysicsBody;

    private boolean mWudi = false;

    public RemotePlayer() {
        mAnimator = new Animator();
        mAnimator.setDuration(500);
        try {
            mAnimator.addFrame(ImageIO.read(new File("resources/bird_remote1_0.png")));
            mAnimator.addFrame(ImageIO.read(new File("resources/bird_remote1_1.png")));
            mAnimator.addFrame(ImageIO.read(new File("resources/bird_remote1_2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPhysicsBody = new PhysicsBody();

        mPhysicsBody.setPosition(new Position(100, 200));
        mPhysicsBody.setCollideCode(0x1000);
        mPhysicsBody.setSpeed(new Vector(0, 0));
        mPhysicsBody.setShape(new CircleShape(30));
        mPhysicsBody.setWeight(10);
        mPhysicsBody.setGravityEnable(false);

        this.setAnimator(mAnimator);
        this.setPhysicsBody(mPhysicsBody);
        this.setTag("RemotePlayer");
    }

    public boolean isWudi() {
        return mWudi;
    }
}
