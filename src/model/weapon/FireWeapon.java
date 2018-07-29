package model.weapon;


import model.Animator;
import model.Vector;

/**
 * 喷火道具
 */
public class FireWeapon extends Weapon {
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
}
