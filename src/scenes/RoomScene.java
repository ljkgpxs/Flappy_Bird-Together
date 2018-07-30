package scenes;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;

import networks.Client;
import networks.Server;
import networks.broadcast.IpPort;
import networks.broadcast.ServerBroadcast;
import networks.broadcast.ServerScanner;
import scenes.core.Scene;

public class RoomScene extends Scene {

    private JLabel mJLabel;
    private JButton mStartButton;

    private IpPort mIpPort;
    private Server mServer;
    private Client mClient;

    public RoomScene(boolean isMaster, Server server) {
        mJLabel = new JLabel("Scanning...");
        if (isMaster) {
            mStartButton = new JButton("开始游戏");
            mStartButton.addActionListener(actionEvent -> {
                mClient = new Client(mIpPort.getAddress().getHostAddress(), mIpPort.getPort());
                ServerBroadcast.stopBroadcast();
                new Thread(mClient::start).start();
                server.setStartGame(true);
                RoomScene.this.setVisible(false);
            });
        } else {
            mStartButton = new JButton("加入游戏");
            mStartButton.addActionListener(actionEvent -> {
                mClient = new Client(mIpPort.getAddress().getHostAddress(), mIpPort.getPort());
                new Thread(mClient::start).start();
                mJLabel.setText("等待开始");
                mStartButton.setEnabled(false);
            });
        }
       mJLabel.setBounds(100, 100, 230, 40);
       mStartButton.setBounds(350, 290, 100, 40);
       mStartButton.setEnabled(false);


        this.add(mJLabel);
        this.add(mStartButton);

        add(new Panel());

        setSize(500, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        new Thread(() -> {
            mIpPort =  ServerScanner.scan();
            mJLabel.setText("房间地址: " + mIpPort.getAddress().getHostAddress() + ":" + mIpPort.getPort());
            mStartButton.setEnabled(true);
        }).start();
    }
}
