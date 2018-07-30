package tests;

import networks.broadcast.IpPort;
import networks.broadcast.ServerBroadcast;
import networks.broadcast.ServerScanner;

public class TestBroadcast {
    public static void main(String[] args) {
        ServerBroadcast.sendBroadcast(1234);

//        IpPort model = ServerScanner.scan();
//        System.out.println("IP: " + model.getAddress().getHostAddress() + ", Port: " + model.getPort());
//        ServerBroadcast.stopBroadcast();
    }
}
