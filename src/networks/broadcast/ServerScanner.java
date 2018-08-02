package networks.broadcast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerScanner {
    static final int BROADCAST_PORT = 8848;
    static final String SERVER_MESSAGE = "BROADCAST_FOR_GAME";

    public static IpPort scan() {
        try {
            DatagramSocket socket = new DatagramSocket(BROADCAST_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {
                byte[] recData = new byte[1024];
                DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
                socket.receive(recPacket);
                socket.close();
                String message = new String(recPacket.getData()).trim();
                Gson gson = new Gson();
                BroadcastMessage rec = gson.fromJson(message, BroadcastMessage.class);
                if (rec.msg.equalsIgnoreCase(SERVER_MESSAGE)) {
                    socket.close();
                    return new IpPort(recPacket.getAddress(), rec.port);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
