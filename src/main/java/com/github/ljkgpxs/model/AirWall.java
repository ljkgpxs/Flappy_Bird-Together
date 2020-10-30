package com.github.ljkgpxs.model;

import com.github.ljkgpxs.physics.PhysicsBody;
import com.github.ljkgpxs.physics.shape.RectangleShape;
import com.github.ljkgpxs.scenes.GameScene;

public class AirWall extends Sprite {
    public static final int WALL_HEIGHT = 500;

    private PhysicsBody mPhysicsBody;

    private boolean isSky = false;

    public AirWall(Position position) {
        if (position.y < 0)
            isSky = true;
        mPhysicsBody = new PhysicsBody();
        mPhysicsBody.setGravityEnable(false);
        mPhysicsBody.setShape(new RectangleShape(GameScene.WINDOW_WIDTH, WALL_HEIGHT));
        mPhysicsBody.setPosition(position);
        mPhysicsBody.setCollideCode(0x10);
        mPhysicsBody.setFixed(true);
        this.setPhysicsBody(mPhysicsBody);
    }


    public boolean isSky() {
        return isSky;
    }
}
