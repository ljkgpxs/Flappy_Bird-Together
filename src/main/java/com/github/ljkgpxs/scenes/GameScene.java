package com.github.ljkgpxs.scenes;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.github.ljkgpxs.listeners.OnGameStateListener;
import com.github.ljkgpxs.model.AirWall;
import com.github.ljkgpxs.model.EndFlag;
import com.github.ljkgpxs.model.Pipe;
import com.github.ljkgpxs.model.Player;
import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.model.Sprite;
import com.github.ljkgpxs.model.action.Action;
import com.github.ljkgpxs.model.action.FlyAction;
import com.github.ljkgpxs.model.weapon.FireWeapon;
import com.github.ljkgpxs.model.weapon.GunWeapon;
import com.github.ljkgpxs.model.weapon.UnlimitedWeapon;
import com.github.ljkgpxs.physics.World;
import com.github.ljkgpxs.scenes.core.Render;
import com.github.ljkgpxs.utils.AudioPlay;
import com.github.ljkgpxs.utils.MMath;
import com.github.ljkgpxs.utils.Map;
import com.github.ljkgpxs.utils.SpriteType;

public class GameScene extends Render implements KeyListener {
    private World mWorld;
    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720;

    private Screen mScreen;

    private int mDistance = 0;
    private int mMapLength = 0;
    public static double mRunSpeed = 2.0;

    private boolean mGameReady = true;
    private long mGameTime = 0;
    private boolean mGameOver = false;
    private boolean mStopGame = false;

    private int mId;

    private OnGameStateListener mGameStateListener = null;

    public GameScene(Map map) {
        mWorld = new World();
        mScreen = new Screen();
        mMapLength = map.mMapLength;

        this.add(mScreen);
        this.addKeyListener(this);

        for (Map.Component component : map.getComponentList()) {
            if (component.spriteType == SpriteType.PIPE) {
                //System.out.println("Pipe pos: " + component.position.x + " " + component
                // .position.y);
                this.addSprite(new Pipe(component.position, component.pipLength));
            } else if (component.spriteType == SpriteType.WEAPON) {
                switch (component.weaponType) {
                    case GUN:
                        this.addSprite(new GunWeapon(component.position));
                        break;
                    case FIRE:
                        this.addSprite(new FireWeapon(component.position));
                        break;
                    case UNLIMITED:
                        this.addSprite(new UnlimitedWeapon(component.position));
                }
            }
        }

        this.addSprite(new EndFlag(new Position(map.mMapLength + 100, WINDOW_HEIGHT - 112 - 520)));

        this.addSprite(new AirWall(new Position(0, -AirWall.WALL_HEIGHT)));
        this.addSprite(new AirWall(new Position(0, WINDOW_HEIGHT - 112)));


        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    @Override
    public void addSprite(Sprite sprite) {
        super.addSprite(sprite);
        mWorld.addComponent(sprite.getPhysicsBody());
    }

    public void start() {
        if (mGameStateListener != null) {
            mGameStateListener.onGameStart();
        }

        this.setVisible(true);
        new Thread(() -> {
            mGameTime = System.currentTimeMillis();
            mRunSpeed = 0.0;
            try {
                AudioPlay.playSound("/resources/sounds/du.wav");
                sleep(1000);
                AudioPlay.playSound("/resources/sounds/du.wav");
                sleep(1000);
                AudioPlay.playSound("/resources/sounds/du.wav");
                sleep(1000);
                AudioPlay.playSound("/resources/sounds/du1.wav");
                sleep(1000);
                mGameReady = false;
                AudioPlay.playMusic("/resources/sounds/bgm2.wav", 10);
                mWorld.start();
                mRunSpeed = 2.0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            long t;
            while (true) {
                if (mStopGame) {
                    break;
                }
                t = System.currentTimeMillis();
                if (mDistance >= mMapLength && !mGameOver) {
                    if (mGameStateListener != null) {
                        mGameStateListener.onGameOver(
                                mGameTime = System.currentTimeMillis() - mGameTime - 2000);
                        mGameOver = true;
                    }
                } else {
                    mDistance += mRunSpeed;
                    for (Sprite s : mSprites) {
                        if (s.getPhysicsBody() != null) {
                            if (s.getPhysicsBody().isFixed() && !(s instanceof AirWall)) {
                                Position p = s.getPhysicsBody().getPosition();
                                p.x -= mRunSpeed;
                            }
                        }
                    }
                }
                mScreen.paintImmediately(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
                    Toolkit.getDefaultToolkit().sync();
                }
                try {
                    long dt = TIME_PRE_FRAME + t - System.currentTimeMillis();
                    sleep(dt > 0 ? dt : 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (mGameOver || mGameReady) {
            return;
        }

        for (Sprite mSprite : mSprites) {
            mSprite.onKeyListener(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public void stopGame() {
        AudioPlay.stopAll();
        mStopGame = true;
        mWorld.stopEmulator();
        dispose();
    }

    public int getDistance() {
        return mDistance;
    }

    public void setGameStateListener(OnGameStateListener listener) {
        this.mGameStateListener = listener;
    }

    private class Screen extends JPanel {
        private Image mBackImage;
        private Image mLandImage;
        private Image mBubbleImage;
        private Image mReadyImage;
        private Image[] mReadyNumbers;
        private Image mGameOverGif;

        private int mLandLocation = 0;

        Screen() {
            mReadyNumbers = new Image[4];
            try {
                mBackImage = ImageIO.read(this.getClass().getResource("/resources/bg_day.png"));
                mLandImage = ImageIO.read(this.getClass().getResource("/resources/land.png"));
                mBubbleImage = ImageIO.read(this.getClass().getResource("/resources/bubble.png"));
                mReadyImage = ImageIO.read(this.getClass().getResource("/resources/text_ready.png"));
                mReadyNumbers[0] = ImageIO.read(this.getClass().getResource("/resources/number_context_00.png"));
                mReadyNumbers[1] = ImageIO.read(this.getClass().getResource("/resources/number_context_01.png"));
                mReadyNumbers[2] = ImageIO.read(this.getClass().getResource("/resources/number_context_02.png"));
                mReadyNumbers[3] = ImageIO.read(this.getClass().getResource("/resources/number_context_03.png"));
                mGameOverGif = Toolkit.getDefaultToolkit().createImage("/resources/game_over.gif");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);

            if (!mGameReady) {
                mLandLocation -= mRunSpeed;
            }

            if (-mLandLocation >= WINDOW_WIDTH) {
                mLandLocation = 0;
            }

            graphics.drawImage(mBackImage,
                    0, 0,
                    WINDOW_WIDTH, WINDOW_HEIGHT,
                    null);

            for (Sprite s : mSprites) {

                if (!s.isEnable()) {
                    continue;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                AffineTransform old = g2d.getTransform();

                int width = s.getPhysicsBody().getShape().getWidth();
                int height = s.getPhysicsBody().getShape().getHeight();

                if (s.hasAction()) {
                    for (Action a : s.getActions()) {
                        if (a instanceof FlyAction) {
                            Position p = s.getPhysicsBody().getPosition();
                            p = (Position) a.getNext(p);
                        }

                    }
                }

                if (s.getAnimator() != null) {
                    Image image = s.getAnimator().getNextFrame();
                    int x = s.getPhysicsBody().getPosition().x;
                    int y = s.getPhysicsBody().getPosition().y;
                    g2d.rotate(Math.toRadians(s.getPhysicsBody().getAngle()),
                            x + image.getWidth(null) / 2,
                            y + image.getHeight(null) / 2);
                    if (s.getPhysicsBody().isFixed()) {
                        graphics.drawImage(image,
                                s.getPhysicsBody().getPosition().x,
                                s.getPhysicsBody().getPosition().y,
                                width, height, null);
                    } else {
                        graphics.drawImage(image,
                                s.getPhysicsBody().getPosition().x,
                                s.getPhysicsBody().getPosition().y,
                                width, height, null);
                    }
                    if (s instanceof Player && ((Player) s).isWudi()) {
                        graphics.drawImage(mBubbleImage,
                                s.getPhysicsBody().getPosition().x - 5,
                                s.getPhysicsBody().getPosition().y - 5,
                                width + 10, height + 10, null);
                    }
                }
                g2d.setTransform(old);
            }

            for (Sprite s : mSprites) {
                s.paint(graphics);
            }

            graphics.drawImage(mLandImage,
                    mLandLocation, WINDOW_HEIGHT - mLandImage.getHeight(null),
                    WINDOW_WIDTH, mLandImage.getHeight(null),
                    null);
            graphics.drawImage(mLandImage,
                    mLandLocation + WINDOW_WIDTH,
                    WINDOW_HEIGHT - mLandImage.getHeight(null),
                    WINDOW_WIDTH, mLandImage.getHeight(null),
                    null);

            if (mGameReady) {
                graphics.drawImage(mReadyImage,
                        (WINDOW_WIDTH - 400) / 2, (WINDOW_HEIGHT - 300) / 2,
                        400, 130, null);
                int n = MMath.abs((int) (3 - (System.currentTimeMillis() - mGameTime) / 1000));
                graphics.drawImage(mReadyNumbers[n > 3 ? 3 : n],
                        (WINDOW_WIDTH - 36) / 2, 350,
                        36, 42, null);
            }

            if (mGameOver) {
                graphics.drawImage(mGameOverGif,
                        (WINDOW_WIDTH - 400) / 2, (WINDOW_HEIGHT - 300) / 2,
                        400, 130, null);
                graphics.setFont(new Font("宋体", Font.BOLD, 24));
                graphics.setColor(Color.WHITE);
                graphics.drawString(String.format("用时%.2f秒", mGameTime / 1000.0),
                        500, 400);
            }

        }
    }
}
