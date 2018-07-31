package model.weapon;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Animator;
import model.Position;
import model.Sprite;
import model.Vector;
import physics.PhysicsBody;
import physics.shape.RectangleShape;

/**
 * 枪道具
 */
public class GunWeapon extends Weapon {

    private static final int WIDTH = 47;
    private static final int HEIGHT = 27;

    public GunWeapon(Position position) {
        super(position);
        try {
            mAnimator.addFrame(ImageIO.read(new File("resources/gun.png")));
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
        return 2000;
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
                mAnimator.addFrame(ImageIO.read(new File("resources/gun_bullet.png")));
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
