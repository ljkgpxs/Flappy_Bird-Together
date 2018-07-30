package networks.broadcast;

import static java.lang.Thread.sleep;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class ServerBroadcast {
    private static Thread mSendThread;

    public static void sendBroadcast(int port) {
        mSendThread = new Thread(() -> {
            while (true) {
                try {
                    Enumeration<NetworkInterface> interfaces = null;
                    interfaces = NetworkInterface.getNetworkInterfaces();

                    while (interfaces.hasMoreElements()) {
                        NetworkInterface networkInterface = interfaces.nextElement();
                        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                            continue;
                        }

                        for (InterfaceAddress interfaceAddress : networkInterface
                                .getInterfaceAddresses()) {
                            InetAddress broadcast = interfaceAddress.getBroadcast();
                            if (broadcast == null) {
                                continue;
                            }

                            DatagramSocket socket = new DatagramSocket();
                            socket.setBroadcast(true);
                            BroadcastMessage data = new BroadcastMessage();
                            data.msg = ServerScanner.SERVER_MESSAGE;
                            data.port = port;

                            Gson gson = new Gson();
                            byte[] sendData = gson.toJson(data, BroadcastMessage.class).getBytes();
                            DatagramPacket sendPacketWild = new DatagramPacket(sendData,
                                    sendData.length,
                                    broadcast,
                                    ServerScanner.BROADCAST_PORT);
                            socket.send(sendPacketWild);
                            //System.out.println("Send to " + broadcast.getHostAddress());
                            socket.close();
                        }
                        sleep(500);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
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
