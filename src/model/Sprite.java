package model;

import model.action.Action;
import model.weapon.Weapon;
import physics.PhysicsBody;

public class Sprite {
    private Action mAction = null;
    private Animator mAnimator = null;
    private PhysicsBody mPhysicsBody = null;
    private Weapon mWeapon = null;

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public Animator getAnimator() {
        return mAnimator;
    }

    public void setAnimator(Animator animator) {
        mAnimator = animator;
    }

    public PhysicsBody getPhysicsBody() {
        return mPhysicsBody;
    }

    public void setPhysicsBody(PhysicsBody physicsBody) {
        mPhysicsBody = physicsBody;
    }

    public Weapon getWeapon() {
        return mWeapon;
    }

    public void setWeapon(Weapon weapon) {
        mWeapon = weapon;
    }

    public boolean hasAction() {
        if (mAction != null && mAction.getPlayedCount() == 0)
            return true;
        else return false;
    }
}
