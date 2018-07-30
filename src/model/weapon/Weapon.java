package model.weapon;

import model.Animator;
import model.Sprite;
import model.Vector;
import physics.PhysicsBody;

public abstract class Weapon extends Sprite {

    public Weapon() {

    }

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract double getLoadTime();

    public abstract Vector getSpeed();
}
