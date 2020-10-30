package com.github.ljkgpxs.tests;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import com.github.ljkgpxs.model.Player;
import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.model.Sprite;
import com.github.ljkgpxs.model.Vector;
import com.github.ljkgpxs.model.action.Action;
import com.github.ljkgpxs.model.action.FlyAction;
import com.github.ljkgpxs.physics.World;
import com.github.ljkgpxs.scenes.core.Render;


/**
 * 测试完全弹性碰撞
 */

public class TestRender extends Render {

    private World mWorld = new World();
    private Screen mScreen;

    TestRender() {
        Player player = new Player();
        player.getPhysicsBody().setSpeed(new Vector(5, 1));
        player.getPhysicsBody().setGravityEnable(false);
        player.getPhysicsBody().setPosition(new Position(60, 590));

        this.addSprite(player);
        mWorld.addComponent(player.getPhysicsBody());

        player = new Player();
        player.getPhysicsBody().setSpeed(new Vector(1, 5));
        player.getPhysicsBody().setGravityEnable(false);
        player.getPhysicsBody().setPosition(new Position(900, 190));


        mWorld.addComponent(player.getPhysicsBody());
        this.addSprite(player);
        this.setSize(2000, 1300);

        mScreen = new Screen();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.add(mScreen);

    }

    public static void main(String[] args) {
        new TestRender().start();
    }


    public void start() {

        this.setVisible(true);
        mWorld.start();

        new Thread(() -> {
            while (true) {
                mScreen.paintImmediately(0, 0, 2000, 1300);
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private class Screen extends JPanel {

        private int mLandLocation = 0;

        Screen() {

        }

        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);


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

                    }
                }
                g2d.setTransform(old);
            }

            for (Sprite s : mSprites) {
                s.paint(graphics);
            }

        }
    }
}
