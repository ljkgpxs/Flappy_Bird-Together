package scenes;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.Position;
import model.Sprite;
import physics.World;
import scenes.core.Scene;

public class MenuScene extends Scene implements KeyListener {
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
        this.add(mScreen);
        this.addKeyListener(this);

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
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_J :
                mRunSpeed = 3.0;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    private class Screen extends JPanel {
        private Image mBackImage;
        private Image mLandImage;

        private int mLandLocation = 0;

        Screen() {
            try {
                mBackImage = ImageIO.read(new File("resources/bg_day.png"));
                mLandImage = ImageIO.read(new File("resources/land.png"));
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
}
