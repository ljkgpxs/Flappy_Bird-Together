package networks.broadcast;

import static java.lang.Thread.sleep;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerBroadcast {
    private static Thread mSendThread;

    public static void sendBroadcast(int port) {
        mSendThread = new Thread(() -> {
            while (true) {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    socket.setBroadcast(true);
                    BroadcastMessage data = new BroadcastMessage();
                    data.msg = ServerScanner.SERVER_MESSAGE;
                    data.port = port;

                    Gson gson = new Gson();
                    byte[] sendData = gson.toJson(data, BroadcastMessage.class).getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            InetAddress.getByName("255.255.255.255"), ServerScanner.BROADCAST_PORT);
                    socket.send(sendPacket);
                    sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        mSendThread.start();
    }

    public static void stopBroadcast() {
        mSendThread.interrupt();
    }
}
