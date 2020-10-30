package com.github.ljkgpxs.tests;

import com.github.ljkgpxs.networks.broadcast.ServerBroadcast;

public class TestBroadcast {
    public static void main(String[] args) {
        ServerBroadcast.sendBroadcast(1234);

//        IpPort com.github.ljkgpxs.model = ServerScanner.scan();
//        System.out.println("IP: " + com.github.ljkgpxs.model.getAddress().getHostAddress() + ", Port: " + com.github.ljkgpxs.model.getPort());
//        ServerBroadcast.stopBroadcast();
    }
}
