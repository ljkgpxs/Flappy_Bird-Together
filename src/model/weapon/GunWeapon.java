package model.weapon;

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

    static class GunBullet extends Sprite {
        GunBullet() {
            mPhysicsBody = new PhysicsBody();
            mPhysicsBody.setSpeed(new Vector(20, 0));
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setParentSprite(this);
            mPhysicsBody.setShape(new RectangleShape(WIDTH, HEIGHT));
            mPhysicsBody.setFixed(false);
            mPhysicsBody.setCollideCode(0x1000);

            mAnimator = new Animator();
            try {
                mAnimator.addFrame(ImageIO.read(new File("resources/gun.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onCollide(Sprite a) {
            return super.onCollide(a);
        }
    }
}
