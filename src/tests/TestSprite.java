package tests;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.Animator;
import model.Position;
import model.Sprite;
import model.action.Action;
import model.action.FlyAction;
import model.action.ScaleAction;
import physics.PhysicsBody;
import physics.shape.CircleShape;
import physics.shape.RectangleShape;

public class TestSprite extends JFrame {

    private Sprite player;
    private Sprite[] mSprites = new Sprite[1];

    TestSprite() {
        player = new Sprite();
        mSprites[0] = player;
        PhysicsBody physicsBody = new PhysicsBody();

        Animator animator = new Animator();
        try {
            animator.addFrame(ImageIO.read(new File("resources/fireworks0.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //animator.setDuration(700);
        physicsBody.setShape(new CircleShape(10));
        player.setPhysicsBody(physicsBody);

        FlyAction action = new FlyAction(new Position(10, 10), new Position(95, 123), 20000);
        player.addAction(action);
        action = new ScaleAction(new Position(10, 10), new Position(100, 121), 20000);
        player.addAction(action);

        player.setAnimator(animator);

        Panel panel = new Panel();

        add(panel);
        panel.start();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 500);
        setVisible(true);
    }

    class Panel extends JPanel {
        Position pos = new Position(10, 10);
        @Override
        public void paint(Graphics graphics) {
            super.paint(graphics);

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
        }

        void start() {
            new Thread(() -> {
                while (true) {
                    repaint();
                    Toolkit.getDefaultToolkit().sync();
                    try {
                        sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        new TestSprite();
    }
}
