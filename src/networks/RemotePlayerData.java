package networks;

import java.util.List;

public class RemotePlayerData {
    long playerDistance;
    long playerY;
    boolean isWudi;

    long weaponDistance;
    long weaponY;
    String weaponId;
    WeaponType weaponType;

    enum WeaponType {
        GUN,
        FIRE,
        UNLIMITED
    }
}
