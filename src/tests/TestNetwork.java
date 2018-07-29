package tests;

import static java.lang.Thread.sleep;

import java.io.IOException;

import networks.Client;
import networks.Server;
import networks.broadcast.IpPort;
import networks.broadcast.ServerBroadcast;
import networks.broadcast.ServerScanner;

public class TestNetwork {
    public static void main(String[] args) {
        Server server = new Server(1234);
        new Thread(server::start).start();
        ServerBroadcast.sendBroadcast(1234);
        try {
            IpPort ipPort = ServerScanner.scan();
            //System.out.println("Got Server " + ipPort.getAddress().getHostAddress()) ;
            Client client = new Client(ipPort.getAddress().getHostAddress(), ipPort.getPort());
            //client.start();
            new Thread(client::start).start();
            sleep(1000);
            server.setStartGame(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
