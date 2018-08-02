package model;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import listeners.OnWeaponBulletAddListener;
import model.weapon.FireWeapon;
import model.weapon.GunWeapon;
import model.weapon.UnlimitedWeapon;
import physics.PhysicsBody;
import physics.shape.CircleShape;
import scenes.GameScene;
import utils.AudioPlay;
import utils.WeaponType;

public class Player extends Sprite {
    private Image mSkillSpeedUp;
    private Image mSkillLoading;

    private Image mWeaponSlot, mFireImage, mGunImage;

    private boolean mHandleKey = true;

    private boolean mWudi = false;

    private long mSkillSpeedUpTimer = 0;
    private long mWeaponTimer = 0;

    private WeaponType mWeaponType = WeaponType.NONE;

    private int mCollideCode = 0x11111;

    private OnWeaponBulletAddListener mOnWeaponBulletAddListener;

    public Player() {
        mAnimator = new Animator();
        mAnimator.setDuration(500);
        try {
            mAnimator.addFrame(ImageIO.read(new File("resources/bird0_0.png")));
            mAnimator.addFrame(ImageIO.read(new File("resources/bird0_1.png")));
            mAnimator.addFrame(ImageIO.read(new File("resources/bird0_2.png")));
            mSkillSpeedUp = ImageIO.read(new File("resources/speed_up.png"));
            mSkillLoading = ImageIO.read(new File("resources/skill_loading.png"));
            mWeaponSlot = ImageIO.read(new File("resources/weapon_slot.png"));
            mGunImage = ImageIO.read(new File("resources/gun.png"));
            mFireImage = ImageIO.read(new File("resources/fire.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mPhysicsBody = new PhysicsBody();

        mPhysicsBody.setPosition(new Position(100, 200));
        mPhysicsBody.setCollideCode(mCollideCode);
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
        if (!mHandleKey) {
            return;
        }
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            AudioPlay.playSound("resources/sounds/fly.wav");
            if (mPhysicsBody.getPosition().y
                    < GameScene.WINDOW_HEIGHT - 112 - mPhysicsBody.getShape().getHeight()) {
                mPhysicsBody.setSpeed(new Vector(0, -5));
            }
        } else if (event.getKeyCode() == KeyEvent.VK_K) {
            if (!mWudi) {
                skillSpeedUp();
            }
        } else if (event.getKeyCode() == KeyEvent.VK_J) {
            if (mWeaponType == WeaponType.NONE) {
                return;
            }
            long time = 0;
            Sprite bullet;
            if (mWeaponType == WeaponType.FIRE) {
                time = FireWeapon.getLoadTime();
                bullet = FireWeapon.getBullet();
            } else {
                time = GunWeapon.getLoadTime();
                bullet = GunWeapon.getBullet();
            }
            if (System.currentTimeMillis() - mWeaponTimer < time) {
                return;
            }

            if (mWeaponType == WeaponType.FIRE) {
                AudioPlay.playSound("resources/sounds/fire.wav");
            } else {
                AudioPlay.playSound("resources/sounds/gun.wav");
            }


            mWeaponTimer = System.currentTimeMillis();
            if (mOnWeaponBulletAddListener != null) {
                bullet.getPhysicsBody().setPosition(
                        new Position(
                                mPhysicsBody.getPosition().x + 80,
                                mPhysicsBody.getPosition().y)
                );
                mOnWeaponBulletAddListener.onWeaponBulletAdd(bullet);
            }
        }
    }

    private void skillSpeedUp() {
        if (System.currentTimeMillis() - mSkillSpeedUpTimer < 5000) {
            return;
        }
        AudioPlay.playSound("resources/sounds/wind.wav");
        mSkillSpeedUpTimer = System.currentTimeMillis();
        new Thread(() -> {
            try {
                GameScene.mRunSpeed = 5.0;
                sleep(2000);
                if (mHandleKey && !mWudi) {
                    GameScene.mRunSpeed = 2.0;
                }
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
            punishTime();
            return true;
        }

        if (a instanceof UnlimitedWeapon) {
            new Thread(() -> {
                AudioPlay.playSound("resources/sounds/star.wav");
                mWudi = true;
                GameScene.mRunSpeed = 8.0;
                mPhysicsBody.setCollideCode(0x10);
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mWudi = false;
                GameScene.mRunSpeed = 2.0;
                mPhysicsBody.setCollideCode(mCollideCode);
            }).start();
            return true;
        }

        if (a instanceof GunWeapon) {
            mWeaponTimer = 0;
            mWeaponType = WeaponType.GUN;
            return true;
        }

        if (a instanceof FireWeapon) {
            mWeaponTimer = 0;
            mWeaponType = WeaponType.FIRE;
            return true;
        }

        if (a instanceof GunWeapon.GunBullet || a instanceof FireWeapon.FireBullet) {
            System.out.println("i am shot");
            AudioPlay.playSound("resources/sounds/ao.wav");
            punishTime();
            return true;
        }

        if (a instanceof RemotePlayer) {
            return true;
        }

        return false;
    }

    private void punishTime() {
        new Thread(() -> {
            Vector speed = mPhysicsBody.getSpeed();
            speed.x = 0;
            speed.y = 0;

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
            GameScene.mRunSpeed = 2.0;
            mPhysicsBody.setCollideCode(0x10);
            mHandleKey = true;

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mPhysicsBody.setCollideCode(mCollideCode);
            mWudi = false;

        }).start();
    }

    private long isWeaponReady() {
        if (mWeaponType == WeaponType.NONE) {
            return Long.MAX_VALUE;
        }

        long time = 0;
        if (mWeaponType == WeaponType.GUN) {
            time = GunWeapon.getLoadTime();
        } else {
            time = FireWeapon.getLoadTime();
        }

        return -(System.currentTimeMillis() - mWeaponTimer - time);
    }

    public boolean isWudi() {
        return mWudi;
    }

    public void setOnWeaponBulletAddListener(OnWeaponBulletAddListener listener) {
        mOnWeaponBulletAddListener = listener;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        Vector speed = mPhysicsBody.getSpeed();
        double degree = speed.y * 5 > 90.0 ? 90.0 : speed.y * 5;
        mPhysicsBody.setAngle(degree);

        graphics.drawImage(mSkillSpeedUp,
                260, 30,
                60, 60,
                null);
        graphics.drawImage(mWeaponSlot,
                180, 30,
                60, 60,
                null);
        if (System.currentTimeMillis() - mSkillSpeedUpTimer < 5000) {
            graphics.drawImage(mSkillLoading,
                    260, 30,
                    60, 60,
                    null);
            //graphics.setFont(new Font(new File("resources/fonts/arial.ttf"), ));
            try {
                graphics.setFont(Font.createFont(Font.TRUETYPE_FONT,
                        new File("resources/fonts/Marker Felt.ttf")));
                graphics.setFont(new Font("Marker Felt", Font.BOLD, 30));
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
            graphics.setColor(Color.WHITE);
            graphics.drawString(String.format("%.1f",
                    5 - (System.currentTimeMillis() - mSkillSpeedUpTimer) / 1000.0),
                    265, 73);
        } else {
            graphics.setFont(new Font("宋体", Font.BOLD, 14));
            graphics.drawString("K", 260, 30);
        }

        if (mWeaponType == WeaponType.FIRE) {
            graphics.drawImage(mFireImage,
                    190, 45,
                    40, 28, null);
        }

        if (mWeaponType == WeaponType.GUN) {
            graphics.drawImage(mGunImage,
                    190, 50,
                    35, 25, null);
        }
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("宋体", Font.BOLD, 14));
        graphics.drawString("J", 180, 30);
        long t = isWeaponReady();
        if (t > 0) {
            if (t == Long.MAX_VALUE || t < 300000) {
                graphics.drawImage(mSkillLoading,
                        180, 30,
                        60, 60,
                        null);
            }
            graphics.setFont(new Font("宋体", Font.BOLD, 30));
            if (t < 300000) {
                graphics.setColor(Color.WHITE);
                graphics.drawString(String.format("%.1f", t / 1000.0),
                        182, 73);
            }
        }


    }
}
