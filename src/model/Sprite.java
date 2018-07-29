package model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.action.Action;
import model.weapon.Weapon;
import physics.PhysicsBody;

public class Sprite {
    private Action mAction = null;
    private Animator mAnimator = null;
    private PhysicsBody mPhysicsBody = null;
    private Weapon mWeapon = null;

    private KeyListener mKeyListener;

    private boolean isEnable = true;

    private String mTag = "";

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
        mPhysicsBody.setParentSprite(this);
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

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void onKeyListener(KeyEvent event) {
    }

    /**
     * 发生碰撞时触发
     * @param a 与其碰撞的物体
     * @return 返回true表示已处理碰撞, 物理引擎不再处理; 返回false表示未处理碰撞,交给物理引擎处理
     */
    public boolean onCollide(Sprite a) {
        return false;
    }

    public void paint(Graphics graphics) {

    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }
}
