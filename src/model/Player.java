package model;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import physics.PhysicsBody;
import physics.shape.CircleShape;
import scenes.GameScene;

public class Player extends Sprite {
    private Image mSkillSpeedUp;
    private Image mSkillLoading;

    private boolean mHandleKey = true;

    private boolean mWudi = false;

    private long mSkillSpeedUpTimer = 0;


    public Player() {
        mAnimator = new Animator();
        mAnimator.setDuration(500);
        try {
            mAnimator.addFrame(ImageIO.read(new File("resources/bird0_0.png")));
            mAnimator.addFrame(ImageIO.read(new File("resources/bird0_1.png")));
            mAnimator.addFrame(ImageIO.read(new File("resources/bird0_2.png")));
            mSkillSpeedUp = ImageIO.read(new File("resources/speed_up.png"));
            mSkillLoading = ImageIO.read(new File("resources/skill_loading.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPhysicsBody = new PhysicsBody();

        mPhysicsBody.setPosition(new Position(100, 200));
        mPhysicsBody.setCollideCode(0x111);
        mPhysicsBody.setSpeed(new Vector(0, 0));
        mPhysicsBody.setShape(new CircleShape(30));
        mPhysicsBody.setWeight(10);
        mPhysicsBody.setGravityEnable(true);

        this.setAnimator(mAnimator);
        this.setPhysicsBody(mPhysicsBody);
        this.setTag("Player");
    }

    @Override
    public void onKeyListener(KeyEvent event) {
        if (!mHandleKey)
            return;
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            if (mPhysicsBody.getPosition().y < GameScene.WINDOW_HEIGHT - 112 - mPhysicsBody.getShape().getHeight())
                mPhysicsBody.setSpeed(new Vector(0, -5));
        } else if (event.getKeyCode() == KeyEvent.VK_K)
            skillSpeedUp();
    }

    private void skillSpeedUp() {
        if (System.currentTimeMillis() - mSkillSpeedUpTimer < 5000) {
            return;
        }
        mSkillSpeedUpTimer = System.currentTimeMillis();
        new Thread(() -> {
            try {
                GameScene.mRunSpeed = 3.0;
                sleep(2000);
                if (mHandleKey)
                    GameScene.mRunSpeed = 1.0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

    }

    @Override
    public boolean onCollide(Sprite a) {
        Vector speed = mPhysicsBody.getSpeed();
        Position position = mPhysicsBody.getPosition();
        if (a instanceof Pipe) {
            speed.x = 0;
            speed.y = 0;
            punishTime();
            return true;
        }

        if (a instanceof RemotePlayer)
            return true;

        return false;
    }

    private void punishTime() {
        new Thread(() -> {
            mHandleKey = false;
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setCollideCode(0);
            GameScene.mRunSpeed = 0;

            for (int i = 0; i < 4; i++) {
                try {
                    sleep(300);
                    setEnable(false);
                    sleep(300);
                    setEnable(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mWudi = true;
            mPhysicsBody.setGravityEnable(true);
            GameScene.mRunSpeed = 1.0;
            mPhysicsBody.setCollideCode(0x10);
            mHandleKey = true;

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mPhysicsBody.setCollideCode(0x11);
            mWudi = false;

        }).start();
    }

    public boolean isWudi() {
        return mWudi;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawImage(mSkillSpeedUp,
                100, 30,
                60, 60,
                null);
        if (System.currentTimeMillis() - mSkillSpeedUpTimer < 5000) {
            graphics.drawImage(mSkillLoading,
                    100, 30,
                    60, 60,
                    null);
            //graphics.setFont(new Font(new File("resources/fonts/arial.ttf"), ));
            try {
                graphics.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/Marker Felt.ttf")));
                graphics.setFont(new Font("Marker Felt", Font.BOLD, 36));
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            graphics.setColor(Color.WHITE);
            graphics.drawString("" + (5 -(System.currentTimeMillis() - mSkillSpeedUpTimer) / 1000), 117, 73);
        } else {
            graphics.setFont(new Font("宋体", Font.BOLD, 14));
            graphics.drawString("K", 100, 30);
        }

    }
}
