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

import model.AirWall;
import model.Pipe;
import model.Player;
import model.Position;
import model.Sprite;
import physics.World;
import scenes.core.Scene;
import utils.Map;
import utils.SpriteType;

public class GameScene extends Scene implements KeyListener {
    private World mWorld;
    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720;

    private Screen mScreen;

    private int mDistance = 0;
    private int mMapLength = 0;
    public static double mRunSpeed = 1.0;

    public GameScene(Map map) {
        mSprites = new ArrayList<>();
        mWorld = new World();
        mScreen = new Screen();
        mMapLength = map.mMapLength;

        this.add(mScreen);
        this.addKeyListener(this);

        for (Map.Component component : map.getComponentList()) {
            if (component.spriteType == SpriteType.PIPE) {
                //System.out.println("Pipe pos: " + component.position.x + " " + component.position.y);
                this.addSprite(new Pipe(component.position, component.pipLength));
            }
        }
        this.addSprite(new AirWall(new Position(0, -112)));
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
        this.setVisible(true);
        mWorld.run();
        new Thread(() -> {
            long t;
            while (true) {
                t = System.currentTimeMillis();
                if (mDistance > mMapLength) {
                    dispose();
                } else
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
        for (Sprite s : mSprites) {
            s.onKeyListener(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public int getDistance() {
        return mDistance;
    }

    private class Screen extends JPanel {
        private Image mBackImage;
        private Image mLandImage;
        private Image mBubbleImage;

        private int mLandLocation = 0;

        Screen() {
            try {
                mBackImage = ImageIO.read(new File("resources/bg_day.png"));
                mLandImage = ImageIO.read(new File("resources/land.png"));
                mBubbleImage = ImageIO.read(new File("resources/bubble.png"));
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

            for (Sprite s : mSprites) {
                s.paint(graphics);
            }

            for (Sprite s : mSprites) {
                if (s.getPhysicsBody() == null || !s.isEnable())
                    continue;
                int width = s.getPhysicsBody().getShape().getWidth();
                int height = s.getPhysicsBody().getShape().getHeight();

                if (s.hasAction()) {
                    Position p = s.getPhysicsBody().getPosition();
                    p = s.getAction().getNextPosition(p);
                }

                if (s.getAnimator() != null) {
                    Image image = s.getAnimator().getNextFrame();
                    if (s.getPhysicsBody().isFixed()) {
                        graphics.drawImage(image,
                                s.getPhysicsBody().getPosition().x -= mRunSpeed,
                                s.getPhysicsBody().getPosition().y,
                                width, height, null);
                    }
                    else graphics.drawImage(image,
                            s.getPhysicsBody().getPosition().x,
                            s.getPhysicsBody().getPosition().y,
                            width, height, null);
                    if (s instanceof Player && ((Player) s).isWudi()) {
                        graphics.drawImage(mBubbleImage,
                                s.getPhysicsBody().getPosition().x -5,
                                s.getPhysicsBody().getPosition().y - 5,
                                width + 10, height + 10, null);
                    }
                }
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
        }
    }
}
