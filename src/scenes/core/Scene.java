package scenes.core;

import java.util.List;

import javax.swing.*;

import model.Sprite;

public abstract class Scene extends JFrame {
    protected List<Sprite> mSprites;

    protected final static int FPS = 60;
    protected final static int TIME_PRE_FRAME = 1000 / FPS;

    public Scene() {
        setIconImage(new ImageIcon("resources/logo.png").getImage());
    }

    public void addSprite(Sprite sprite) {
        mSprites.add(sprite);
    }
}
