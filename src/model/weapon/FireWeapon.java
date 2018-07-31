package model.weapon;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import model.Animator;
import model.Position;
import model.Sprite;
import model.Vector;
import physics.PhysicsBody;
import physics.shape.CircleShape;
import physics.shape.RectangleShape;

/**
 * 喷火道具
 */
public class FireWeapon extends Weapon {

    private static final int WIDTH = 47;
    private static final int HEIGHT = 29;

    public FireWeapon(Position position) {
        super(position);
        try {
            mAnimator.addFrame(ImageIO.read(new File("resources/fire.png")));
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
        return new FireBullet();
    }

    static class FireBullet extends Sprite {
        FireBullet() {
            mPhysicsBody = new PhysicsBody();
            mPhysicsBody.setSpeed(new Vector(10, 0));
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setParentSprite(this);
            mPhysicsBody.setShape(new RectangleShape(WIDTH, HEIGHT));
            mPhysicsBody.setFixed(false);
            mPhysicsBody.setCollideCode(0x1000);

            mAnimator = new Animator();
            try {
                mAnimator.addFrame(ImageIO.read(new File("resources/fire.png")));
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
