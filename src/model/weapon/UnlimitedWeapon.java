package model.weapon;

import model.Animator;
import model.Vector;

/**
 * 无敌道具
 */
public class UnlimitedWeapon extends Weapon {
    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public double getLoadTime() {
        return 0;
    }

    @Override
    public Vector getSpeed() {
        return null;
    }

    @Override
    public Animator getAnimator() {
        return null;
    }
    // TODO:
}
