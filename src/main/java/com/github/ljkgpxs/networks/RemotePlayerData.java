package com.github.ljkgpxs.networks;

public class RemotePlayerData {
    long playerDistance;
    long playerY;
    boolean isWudi;

    long weaponDistance;
    long weaponY;
    String weaponId;
    WeaponType weaponType;

    String tag;

    enum WeaponType {
        GUN,
        FIRE,
        UNLIMITED
    }
}
