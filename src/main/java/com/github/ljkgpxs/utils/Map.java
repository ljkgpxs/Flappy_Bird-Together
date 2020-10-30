package com.github.ljkgpxs.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.ljkgpxs.model.Position;
import com.github.ljkgpxs.scenes.GameScene;

public class Map implements Serializable {

    private List<Component> mComponentList;
    public int mMapLength;

    public static class Component {
        public SpriteType spriteType;
        public WeaponType weaponType;
        public int pipLength;
        public Position position;

        Component(SpriteType spriteType, int pipLength, Position position) {
            this.spriteType = spriteType;
            this.pipLength = pipLength;
            this.position = position;
        }

        Component(SpriteType spriteType, WeaponType weaponType, Position position) {
            this.spriteType = spriteType;
            this.weaponType = weaponType;
            this.position = position;
        }
    }

    public Map() {
        mComponentList = new ArrayList<>();
    }

    public static Map create() {

        Random random = new Random();

        Map map = new Map();
        map.mMapLength = 8000 + random.nextInt(2000);
        for (int i = 1; i < map.mMapLength; i++) {
            if (i % 500 == 0) {
                Position position = new Position(i, 0);

                int l = 110 + random.nextInt(360);

                if (random.nextBoolean()) {
                    // 管道是否在上面
                    position.y = l - 600 - 50;
                } else {
                    position.y = GameScene.WINDOW_HEIGHT - l - 40;
                }
                map.mComponentList.add(new Component(SpriteType.PIPE, l, position));
            }

            if (i % 500 == 0) {
                if (random.nextInt(100) < 50) {
                    Position position = new Position(i + 250,
                            100 + random.nextInt(GameScene.WINDOW_HEIGHT - 200));
                    int weaponCode = random.nextInt(100);
                    if (weaponCode < 20) {
                        map.mComponentList.add(
                                new Component(SpriteType.WEAPON, WeaponType.UNLIMITED, position));
                    } else if (weaponCode < 60) {
                        map.mComponentList.add(
                                new Component(SpriteType.WEAPON, WeaponType.FIRE, position));
                    } else {
                        map.mComponentList.add(
                                new Component(SpriteType.WEAPON, WeaponType.GUN, position));
                    }
                }
            }
        }

        return map;
    }

    public List<Component> getComponentList() {
        return mComponentList;
    }
}
