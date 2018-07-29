package scenes;

import static java.lang.Thread.sleep;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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

    public RoomScene(boolean isMaster, Server server) {
        mJLabel = new JLabel("Scanning...");
        if (isMaster) {
            mStartButton = new JButton("开始游戏");
            mStartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        Client client = new Client(mIpPort.getAddress().getHostAddress(), mIpPort.getPort());
                        ServerBroadcast.stopBroadcast();
                        new Thread(client::start).start();
                        server.setStartGame(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            mStartButton = new JButton("加入游戏");
            mStartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        Client client = new Client(mIpPort.getAddress().getHostAddress(), mIpPort.getPort());
                        new Thread(client::start).start();
                        mJLabel.setText("等待开始");
                        mStartButton.setEnabled(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
       mJLabel.setBounds(100, 100, 230, 40);
       mStartButton.setBounds(350, 290, 100, 40);
       mStartButton.setEnabled(false);


        this.add(mJLabel);
        this.add(mStartButton);

        add(new Panel());

        setSize(500, 380);
        //setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        new Thread(() -> {
            mIpPort =  ServerScanner.scan();
            System.out.println("Done");
            mJLabel.setText("房间地址: " + mIpPort.getAddress().getHostAddress() + ":" + mIpPort.getPort());
            mStartButton.setEnabled(true);
        }).start();
    }
}
