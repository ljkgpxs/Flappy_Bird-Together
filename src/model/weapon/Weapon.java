package model.weapon;

import model.Animator;
import model.Vector;

public abstract class Weapon {
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract double getLoadTime();

    public abstract Vector getSpeed();
    public abstract Animator getAnimator();
}
