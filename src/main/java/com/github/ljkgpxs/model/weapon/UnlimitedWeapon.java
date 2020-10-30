package com.github.ljkgpxs.model.weapon;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.ljkgpxs.model.Position;

/**
 * 无敌道具
 */
public class UnlimitedWeapon extends Weapon {

    public UnlimitedWeapon(Position position) {
        super(position);
        try {
            for (int i = 0; i < 9; i++) {
                mAnimator.setDuration(1000);
                mAnimator.addFrame(ImageIO.read(this.getClass().getResource("/resources/star" + (i + 1) + ".png")));
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
