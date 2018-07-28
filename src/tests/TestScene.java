package tests;

import model.Pipe;
import model.Player;
import scenes.GameScene;
import utils.Map;
import utils.SpriteType;

public class TestScene {
    public static void main(String[] args) {
        GameScene scene = new GameScene(Map.create());

        scene.addSprite(new Player());

        scene.start();
    }
}
