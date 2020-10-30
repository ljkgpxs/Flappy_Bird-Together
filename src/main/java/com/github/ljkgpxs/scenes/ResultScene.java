package com.github.ljkgpxs.scenes;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.github.ljkgpxs.model.Animator;
import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.model.Sprite;
import com.github.ljkgpxs.model.action.Action;
import com.github.ljkgpxs.model.action.FlyAction;
import com.github.ljkgpxs.model.action.ScaleAction;
import com.github.ljkgpxs.networks.ScoreItem;
import com.github.ljkgpxs.physics.PhysicsBody;
import com.github.ljkgpxs.physics.shape.RectangleShape;
import com.github.ljkgpxs.scenes.core.Render;
import com.github.ljkgpxs.utils.AudioPlay;

public class ResultScene extends Render implements MouseListener {
    private List<ScoreItem> mUserTime;

    private int mRunSpeed = 1;
    private Screen mScreen;

    private List<String> mResult;

    private final static int WINDOW_WIDTH = 1280;
    private final static int WINDOW_HEIGHT = 720;

    private boolean mBoardLoaded = false;
    private boolean mIsOver = false;

    public ResultScene(List<ScoreItem> userTime) {
        mUserTime = userTime;
        mScreen = new Screen();
        mResult = new CopyOnWriteArrayList<>();

        userTime.sort(Comparator.comparing(scoreItem -> scoreItem.time));


        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.add(mScreen);
        this.setVisible(true);
        this.addMouseListener(this);


        new Thread(() -> {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                if (mIsOver) {
                    break;
                }
                List<Sprite> sprites = new CopyOnWriteArrayList<>();
                for (int i = 0; i < 9; i++) {
                    Sprite s = new Fireworks();
                    sprites.add(s);
                    addSprite(s);
                }
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 9; i++) {
                    removeSprite(sprites.get(i));
                }
                sprites.clear();
            }
        }).start();

        addSprite(new ScoreBoard());

        AudioPlay.playMusic("/resources/sounds/bgm3.wav", 10);

        new Thread(() -> {
            while (true) {
                if (mIsOver) {
                    dispose();
                    break;
                }
                repaint();
                if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
                    Toolkit.getDefaultToolkit().sync();
                }

                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < mUserTime.size(); i++) {
                try {
                    if (mIsOver) {
                        return;
                    }
                    sleep(500);
                    StringBuffer buffer = new StringBuffer("");
                    mResult.add(buffer.toString());
                    buffer.append("第");
                    mResult.set(i, buffer.toString());
                    sleep(400);
                    buffer.append(i + 1);
                    buffer.append("名");
                    mResult.set(i, buffer.toString());
                    sleep(400);
                    buffer.append("用");
                    mResult.set(i, buffer.toString());
                    sleep(400);
                    buffer.append("时");
                    mResult.set(i, buffer.toString());
                    sleep(400);
                    buffer.append(": ");
                    mResult.set(i, buffer.toString());
                    sleep(1000);
                    buffer.append(String.format("%7.2f", mUserTime.get(i).time / 1000.0));
                    mResult.set(i, buffer.toString());
                    sleep(400);
                    buffer.append("秒");
                    sleep(400);
                    buffer.append("    ").append(mUserTime.get(i).tag);
                    mResult.set(i, buffer.toString());
                    sleep(400);
                    if (i == 0) {
                        addSprite(new Blink());
                        addSprite(new GoldMedal());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        mIsOver = true;
        AudioPlay.stopAll();
        new MenuScene().start();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    private void removeSprite(Sprite s) {
        mSprites.remove(s);
    }

    class Screen extends JPanel {
        private Image mBackImage;
        private Image mLandImage;
        private Image mGoldMedal;

        private int mLandLocation = 0;


        Screen() {
            try {
                mBackImage = ImageIO.read(this.getClass().getResource("/resources/bg_day.png"));
                mLandImage = ImageIO.read(this.getClass().getResource("/resources/land.png"));
                mGoldMedal = ImageIO.read(this.getClass().getResource("/resources/medals_1.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            mLandLocation -= mRunSpeed;
            if (-mLandLocation >= WINDOW_WIDTH) {
                mLandLocation = 0;
            }

            graphics.drawImage(mBackImage,
                    0, 0,
                    WINDOW_WIDTH, WINDOW_HEIGHT,
                    null);
            graphics.drawImage(mLandImage,
                    mLandLocation, WINDOW_HEIGHT - mLandImage.getHeight(null),
                    WINDOW_WIDTH, mLandImage.getHeight(null),
                    null);
            graphics.drawImage(mLandImage,
                    mLandLocation + WINDOW_WIDTH,
                    WINDOW_HEIGHT - mLandImage.getHeight(null),
                    WINDOW_WIDTH, mLandImage.getHeight(null),
                    null);

            for (Sprite s : mSprites) {
                if (s.hasAction()) {
                    for (Action a : s.getActions()) {
                        if (a instanceof ScaleAction) {
                            Position p = new Position(s.getPhysicsBody().getShape().getWidth(),
                                    s.getPhysicsBody().getShape().getWidth());
                            p = (Position) a.getNext(p);
                            s.getPhysicsBody().setShape(new RectangleShape(p.x, p.y));
                        } else if (a instanceof FlyAction) {
                            Position p = s.getPhysicsBody().getPosition();
                            if (s.getPhysicsBody().getPosition() == null) {
                                System.out.println("ssssssss");
                            }
                            p = (Position) a.getNext(p);
                            s.getPhysicsBody().setPosition(p);
                        }
                    }
                }

                graphics.drawImage(s.getAnimator().getNextFrame(),
                        s.getPhysicsBody().getPosition().x,
                        s.getPhysicsBody().getPosition().y,
                        s.getPhysicsBody().getShape().getWidth(),
                        s.getPhysicsBody().getShape().getHeight(), null);

            }

            graphics.setFont(new Font("黑体", Font.BOLD, 20));
            graphics.setColor(Color.WHITE);
            for (int i = 0; i < mResult.size(); i++) {
                graphics.drawString(mResult.get(i), 500, 150 + i * 60);
            }
        }
    }


    class ScoreBoard extends Sprite {
        ScoreBoard() {
            mPhysicsBody = new PhysicsBody();
            mAnimator = new Animator();

            mPhysicsBody.setFixed(true);
            Position pos = new Position(400, -600);
            mPhysicsBody.setPosition(pos);
            mPhysicsBody.setCollideCode(0);
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setShape(new RectangleShape(500, 400));

            try {
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/score_borad.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            addAction(new FlyAction(pos, new Position(400, 100), 1000));
        }
    }

    class Fireworks extends Sprite {
        private Random mRandom;

        Fireworks() {
            mRandom = new Random();
            mPhysicsBody = new PhysicsBody();
            mAnimator = new Animator();

            mPhysicsBody.setFixed(true);
            Position pos = new Position(WINDOW_WIDTH / 2, WINDOW_HEIGHT - 30);
            mPhysicsBody.setPosition(pos);
            mPhysicsBody.setCollideCode(0);
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setShape(new RectangleShape(0, 0));

            try {
                mAnimator.addFrame(ImageIO.read(
                        this.getClass().getResource("/resources/fireworks" + mRandom.nextInt(9) + ".png")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            int x = mRandom.nextInt(WINDOW_WIDTH) - 250;
            int y = mRandom.nextInt(WINDOW_HEIGHT) - 250;
            long t = 500 + mRandom.nextInt(1000);
            addAction(new ScaleAction(new Position(0, 0), new Position(200, 200), t));
            addAction(new FlyAction(pos,
                    new Position(x < 0 ? 10 : x, y < 0 ? 10 : y), t));
        }
    }

    class Blink extends Sprite {
        Blink() {
            mPhysicsBody = new PhysicsBody();
            mAnimator = new Animator();

            mPhysicsBody.setFixed(true);
            Position pos = new Position(490, 0);
            mPhysicsBody.setPosition(pos);
            mPhysicsBody.setCollideCode(0);
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setShape(new RectangleShape(10, 10));

            try {
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/blink_00.png")));
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/blink_01.png")));
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/blink_02.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAnimator.setDuration(400);
            addAction(new FlyAction(pos, new Position(490, 120), 500));
        }
    }

    class GoldMedal extends Sprite {
        GoldMedal() {
            mPhysicsBody = new PhysicsBody();
            mAnimator = new Animator();

            mPhysicsBody.setFixed(true);
            Position pos = new Position(440, 0);
            mPhysicsBody.setPosition(pos);
            mPhysicsBody.setCollideCode(0);
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setShape(new RectangleShape(50, 50));

            try {
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/medals_1.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            addAction(new FlyAction(pos, new Position(440, 120), 500));
        }
    }
}
