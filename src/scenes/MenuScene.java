package scenes;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.Animator;
import model.Position;
import model.Sprite;
import model.Vector;
import networks.Server;
import networks.broadcast.ServerBroadcast;
import physics.PhysicsBody;
import physics.World;
import physics.shape.CircleShape;
import physics.shape.RectangleShape;
import scenes.core.Scene;

public class MenuScene extends Scene implements MouseListener {
    private World mWorld;
    private final static int WINDOW_WIDTH = 1280;
    private final static int WINDOW_HEIGHT = 720;

    private Screen mScreen;

    private int mDistance = 0;
    private double mRunSpeed = 1.0;

    public MenuScene() {
        mSprites = new ArrayList<>();
        mWorld = new World();
        mScreen = new Screen();

        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.add(mScreen);
        this.addSprite(new Bird(0));
        this.addSprite(new Bird(1));
        this.addSprite(new Bird(2));
        this.addMouseListener(this);
    }

    @Override
    public void addSprite(Sprite sprite) {
        super.addSprite(sprite);
        mWorld.addComponent(sprite.getPhysicsBody());
    }

    public void start() {
        this.setVisible(true);
        mWorld.run();
        new Thread(() -> {
            long t;
            while (true) {
                t = System.currentTimeMillis();
                mDistance += mRunSpeed;
                mScreen.repaint();
                Toolkit.getDefaultToolkit().sync();
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
    public void mouseClicked(MouseEvent mouseEvent) {
        int x = mouseEvent.getPoint().x;
        int y = mouseEvent.getPoint().y;

        if (x >= (WINDOW_WIDTH - 200) / 2 && x <=WINDOW_WIDTH / 2 + 100) {
            if (y >= 300 && y <= 350) {
                Server server = new Server(2333);
                server.start();
                ServerBroadcast.sendBroadcast(2333);
                new RoomScene(true, server);
                dispose();
                return;
            }
            if (y >= 370 && y <= 420) {
                new RoomScene(false, null);
                dispose();
                return;
            }
            if (y >= 440 && y <= 490) {
                System.exit(0);
            }
        }
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

    private class Screen extends JPanel {
        private Image mBackImage;
        private Image mLandImage;
        private Image mTitleImage;

        private Image mCreate, mJoin, mQuit;

        private int mLandLocation = 0;

        Screen() {
            try {
                mBackImage = ImageIO.read(new File("resources/bg_day.png"));
                mLandImage = ImageIO.read(new File("resources/land.png"));
                mTitleImage = ImageIO.read(new File("resources/title.png"));
                mCreate = ImageIO.read(new File("resources/button_create_room.png"));
                mJoin = ImageIO.read(new File("resources/button_join_room.png"));
                mQuit = ImageIO.read(new File("resources/button_quit.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);

            mLandLocation -= mRunSpeed;
            if (-mLandLocation >= WINDOW_WIDTH)
                mLandLocation = 0;

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
            graphics.drawImage(mTitleImage,
                    (WINDOW_WIDTH - mTitleImage.getWidth(null)) / 2, 100,
                    null);

            graphics.drawImage(mCreate,
                    (WINDOW_WIDTH - 200) / 2, 300,
                    150, 50, null);
            graphics.drawImage(mJoin,
                    (WINDOW_WIDTH - 200) / 2, 370,
                    150, 50, null);
            graphics.drawImage(mQuit,
                    (WINDOW_WIDTH - 200) / 2, 440,
                    150, 50, null);

            for (Sprite s : mSprites) {
                if (s.getPhysicsBody() == null || !s.isEnable())
                    continue;
                int width = s.getPhysicsBody().getShape().getWidth();
                int height = s.getPhysicsBody().getShape().getHeight();

                if (s.hasAction()) {
                    Position p = s.getPhysicsBody().getPosition();
                    p = s.getAction().getNextPosition(p);
                }

                if (s.getAnimator() != null)
                    graphics.drawImage(s.getAnimator().getNextFrame(),
                            s.getPhysicsBody().getPosition().x,
                            s.getPhysicsBody().getPosition().y,
                            width, height, null);
            }
        }
    }

    class Bird extends Sprite {
        Bird(int i) {
            mAnimator = new Animator();
            try {
                mAnimator.addFrame(ImageIO.read(new File("resources/bird" + i + "_0.png")));
                mAnimator.addFrame(ImageIO.read(new File("resources/bird" + i + "_1.png")));
                mAnimator.addFrame(ImageIO.read(new File("resources/bird" + i + "_2.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mAnimator.setDuration(500);
            setAnimator(mAnimator);
            setEnable(true);

            mPhysicsBody = new PhysicsBody();
            mPhysicsBody.setPosition(new Position((i + 1) * 300, 200));
            mPhysicsBody.setShape(new CircleShape(20));
            mPhysicsBody.setGravityEnable(false);
            mPhysicsBody.setWeight(0);
            mPhysicsBody.setCollideCode(0);
            mPhysicsBody.setParentSprite(this);
            mPhysicsBody.setSpeed(new Vector(0, 0));

            setPhysicsBody(mPhysicsBody);
        }
    }
}
