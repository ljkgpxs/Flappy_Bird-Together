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
import model.action.FlyAction;

public class TestSprite extends JFrame {

    private Sprite player;

    TestSprite() {
        player = new Sprite();
        Animator animator = new Animator();
        try {
            animator.addFrame(ImageIO.read(new File("resources/fireworks0.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        animator.setDuration(700);

        FlyAction action = new FlyAction(new Position(10, 10), new Position(100, 100), 3000);

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

            pos = (Position) player.getActions().get(0).getNext(pos);

            graphics.drawImage(player.getAnimator().getNextFrame(), 100, 100, pos.x, pos.y, null);
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
