package networks;

public class RemotePlayerData {
    long playerDistance;
    long playerY;
    boolean isWudi;

    long weaponDistance;
    long weaponY;

    enum WeaponType {
        GUN,
        FIRE,
        UNLIMITED
    }
}
