package networks;

public class NetMessage {
    DataType type;
    String data;
    boolean gameOver;
    long time;

    enum DataType {
        USER_POS, // 发送位置数据
        MAP_DATA, // 发送地图数据
        START_GAME, // 发送游戏人数
        GAME_OVER // 发送总用时
    }
}
