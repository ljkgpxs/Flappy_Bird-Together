package com.github.ljkgpxs.listeners;

import com.github.ljkgpxs.model.Sprite;

public interface OnCollideListener {
    /**
     * 碰撞发生时的回调函数
     * @param a 碰撞精灵a
     * @param b 碰撞精灵b
     * @return 返回true表示已经处理碰撞, false表示未处理碰撞
     */
    default boolean onCollide(Sprite a, Sprite b) {
        return false;
    }
}
