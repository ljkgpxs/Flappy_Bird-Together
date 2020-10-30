package com.github.ljkgpxs.model;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.github.ljkgpxs.model.action.Action;
import com.github.ljkgpxs.physics.PhysicsBody;

public class Sprite {
    private List<Action> mActions = null;
    protected Animator mAnimator = null;
    protected PhysicsBody mPhysicsBody = null;

    private KeyListener mKeyListener;

    private boolean isEnable = true;

    private String mTag = "";

    public List<Action> getActions() {
        return mActions;
    }

    public Sprite() {
        mActions = new CopyOnWriteArrayList<>();
    }

    public void addAction(Action action) {
        mActions.add(action);
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

    public boolean hasAction() {
        return mActions.size() != 0;
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
