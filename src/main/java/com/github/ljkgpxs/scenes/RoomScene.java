package com.github.ljkgpxs.scenes;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.github.ljkgpxs.listeners.OnGameStateListener;
import com.github.ljkgpxs.listeners.OnPlayerJoinedListener;
import com.github.ljkgpxs.networks.Client;
import com.github.ljkgpxs.networks.Server;
import com.github.ljkgpxs.networks.broadcast.IpPort;
import com.github.ljkgpxs.networks.broadcast.ServerBroadcast;
import com.github.ljkgpxs.networks.broadcast.ServerScanner;
import com.github.ljkgpxs.scenes.core.Render;
import com.github.ljkgpxs.utils.AudioPlay;

public class RoomScene extends Render implements OnGameStateListener, OnPlayerJoinedListener {

    private JLabel mJLabel;
    private JLabel mStartButton;
    private JLabel mPlayerCount;

    private IpPort mIpPort;
    private Client mClient;

    public RoomScene(boolean isMaster, Server server) {
        if (isMaster) {
            mPlayerCount = new JLabel("当前玩家数量: 0");
            mPlayerCount.setFont(new Font("宋体", Font.BOLD, 16));
            mPlayerCount.setForeground(Color.ORANGE);
            mPlayerCount.setBounds(100, 180, 300, 40);
            this.add(mPlayerCount);
            server.setOnPlayerJoinedListener(this);
        }

        mJLabel = new JLabel("正在搜索房间...");
        mJLabel.setFont(new Font("宋体", Font.BOLD, 16));
        mJLabel.setForeground(Color.ORANGE);
        if (isMaster) {
            try {
                BufferedImage image = ImageIO.read(this.getClass().getResource("/resources/button_start_game.png"));
                Image ic = image.getScaledInstance(100, 40, Image.SCALE_SMOOTH);
                mStartButton = new JLabel(new ImageIcon(ic));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mStartButton.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    ServerBroadcast.stopBroadcast();
                    AudioPlay.stopAll();
                    new Thread(mClient::start).start();
                    server.setStartGame(true);
                    dispose();
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });
        } else {
            try {
                BufferedImage image = ImageIO.read(this.getClass().getResource("/resources/button_join_game.png"));
                Image ic = image.getScaledInstance(100, 40, Image.SCALE_SMOOTH);
                mStartButton = new JLabel(new ImageIcon(ic));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mStartButton.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    mClient = new Client(mIpPort.getAddress().getHostAddress(), mIpPort.getPort());
                    new Thread(mClient::start).start();
                    mJLabel.setText("等待开始");
                    mStartButton.setEnabled(false);
                    mClient.setOnGameStateListener(RoomScene.this);
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });
        }
       mJLabel.setBounds(100, 100, 300, 40);
       mStartButton.setBounds(350, 290, 100, 40);
       mStartButton.setEnabled(false);



        this.add(mJLabel);
        this.add(mStartButton);
        add(new BackImage());

        setSize(500, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("房间搜索");
        new Thread(() -> {
            mIpPort =  ServerScanner.scan();
            if (isMaster) {
                mClient = new Client(mIpPort.getAddress().getHostAddress(), mIpPort.getPort());
            }
            mJLabel.setText("房间地址: " + mIpPort.getAddress().getHostAddress() + ":" + mIpPort.getPort());
            mStartButton.setEnabled(true);
        }).start();
    }

    @Override
    public void onGameOver(long time) {

    }

    @Override
    public void onGameStart() {
        AudioPlay.stopAll();
        dispose();
    }

    @Override
    public void onPlayerJoined(int count) {
        mPlayerCount.setText("当前玩家数量: " + count);
    }

    class BackImage extends JPanel {
        private Image mBg;

        BackImage() {
            try {
                mBg = ImageIO.read(this.getClass().getResource("/resources/bg_night.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.drawImage(mBg, 0, 0,
                    500, 380, null);
        }
    }
}
