package com.github.ljkgpxs.model.action;

import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.model.Vector;

public class ScaleAction extends FlyAction {

    public ScaleAction(Vector src, Vector dest, long duration) {
        super(new Position(src.x, src.y), new Position(dest.x, dest.y), duration);
    }

    public ScaleAction(Position src, Position dest, long duration) {
        super(src, dest, duration);
    }
}
