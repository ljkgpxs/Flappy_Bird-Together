package com.github.ljkgpxs.model.weapon;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.ljkgpxs.model.Animator;
import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.model.Sprite;
import com.github.ljkgpxs.model.Vector;
import com.github.ljkgpxs.physics.PhysicsBody;
import com.github.ljkgpxs.physics.shape.RectangleShape;

/**
 * 枪道具
 */
public class GunWeapon extends Weapon {

    private static final int WIDTH = 47;
    private static final int HEIGHT = 27;

    public GunWeapon(Position position) {
        super(position);
        try {
            mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/gun.png")));
            mAnimator.setDuration(10000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPhysicsBody.setShape(new RectangleShape(WIDTH, HEIGHT));
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    public static long getLoadTime() {
        return 1500;
    }

    public static Sprite getBullet() {
        return new GunBullet();
    }

    public static class GunBullet extends Sprite {
        public GunBullet() {
            mPhysicsBody = new PhysicsBody();
            mPhysicsBody.setSpeed(new Vector(10, 0));
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setParentSprite(this);
            mPhysicsBody.setShape(new RectangleShape(50, 30));
            mPhysicsBody.setFixed(false);
            mPhysicsBody.setCollideCode(0x10000);

            mAnimator = new Animator();
            try {
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/gun_bullet.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onCollide(Sprite a) {
            setEnable(false);
            mPhysicsBody.setCollideCode(0);
            return true;
        }

        @Override
        public void onKeyListener(KeyEvent event) {
            super.onKeyListener(event);
        }
    }
}
