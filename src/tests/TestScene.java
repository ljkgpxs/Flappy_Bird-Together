package tests;

import model.Pipe;
import model.Player;
import networks.Server;
import networks.broadcast.ServerBroadcast;
import scenes.GameScene;
import scenes.RoomScene;
import utils.Map;
import utils.SpriteType;

public class TestScene {
    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
        ServerBroadcast.sendBroadcast(1234);

        new RoomScene(false, null);
    }
}
