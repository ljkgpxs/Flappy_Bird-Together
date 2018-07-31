package model.weapon;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import model.Animator;
import model.Position;
import model.Vector;

/**
 * 无敌道具
 */
public class UnlimitedWeapon extends Weapon {

    public UnlimitedWeapon(Position position) {
        super(position);
        try {
            for (int i = 0; i < 9; i++) {
                mAnimator.setDuration(1000);
                mAnimator.addFrame(ImageIO.read(new File("resources/star" + (i + 1) + ".png")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
