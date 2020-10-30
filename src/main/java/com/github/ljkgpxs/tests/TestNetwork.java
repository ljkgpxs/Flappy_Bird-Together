package com.github.ljkgpxs.tests;

import static java.lang.Thread.sleep;

import com.github.ljkgpxs.networks.Client;
import com.github.ljkgpxs.networks.Server;
import com.github.ljkgpxs.networks.broadcast.IpPort;
import com.github.ljkgpxs.networks.broadcast.ServerBroadcast;
import com.github.ljkgpxs.networks.broadcast.ServerScanner;

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
