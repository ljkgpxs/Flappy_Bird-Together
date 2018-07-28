package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Position;
import scenes.GameScene;

public class Map implements Serializable {

    private List<Component> mComponentList;
    private int mMapLength;

    public static class Component {
        public SpriteType spriteType;
        public int pipLength;
        public Position position;

        Component(SpriteType spriteType, int pipLength, Position position) {
            this.spriteType = spriteType;
            this.pipLength = pipLength;
            this.position = position;
        }
    }

    public Map() {
        mComponentList = new ArrayList<>();
    }

    public static Map create() {

        Random random = new Random();

        Map map = new Map();
        map.mMapLength = 7000 + random.nextInt(2000);
        for (int i = 1; i < map.mMapLength; i++) {
            if (i % 500 == 0) {
                Position position = new Position(i, 0);

                int l = 110 + random.nextInt(360);

                if (random.nextBoolean()) {
                    // 管道是否在上面
                    position.y = l - 600;
                } else position.y = GameScene.WINDOW_HEIGHT - l - 40;
                map.mComponentList.add(new Component(SpriteType.PIPE, l, position));
            }

            if ((i + 100) % 500 == 0 ) {
                if (random.nextInt(100) < 20) {
                    Position position = new Position(i, 100 + random.nextInt(GameScene.WINDOW_HEIGHT - 200));
                    map.mComponentList.add(new Component(SpriteType.WEAPON, 50, position));
                }
            }
        }

        return map;
    }

    public List<Component> getComponentList() {
        return mComponentList;
    }
}
