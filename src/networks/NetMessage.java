package networks;

public class NetMessage {
    DataType type;
    String data;

    public static final String START_GAME = "start_game";

    enum DataType {
        USER_POS,
        MAP_DATA,
        START_GAME
    }
}
