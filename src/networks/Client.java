package networks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import listeners.OnGameStateListener;
import listeners.OnWeaponBulletAddListener;
import model.Player;
import model.Position;
import model.RemotePlayer;
import model.Sprite;
import model.weapon.FireWeapon;
import model.weapon.GunWeapon;
import scenes.GameScene;
import utils.Map;

public class Client implements OnGameStateListener, OnWeaponBulletAddListener {
    private GameScene mGameScene;
    private Player mPlayer;
    private Socket client;

    private OutputStream mSender;
    private InputStream mReceiver;

    private Gson mGson;
    private boolean mGameStarted = false;

    private List<RemotePlayerData> mRemotePlayerData;
    private List<RemotePlayer> mRemotePlayers;

    private List<String> mHandledWeaponId;

    private Sprite mWeaponBullet;
    private int mWeaponId;

    private boolean mGameOver = false;
    private long mGameTime = 0;

    private int mErrorsNum;

    public Client(String sAddress, int port) {
        try {
            client = new Socket(sAddress, port);
            mSender = client.getOutputStream();
            mReceiver = client.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mGson = new Gson();
        mPlayer = new Player();
        mRemotePlayerData = new ArrayList<>();
        mRemotePlayers = new ArrayList<>();
        mHandledWeaponId = new ArrayList<>();

        mPlayer.setOnWeaponBulletAddListener(this);
    }

    public void start() {
        while (true) {
            for (int i = 0; i < mRemotePlayerData.size(); i++) {
                if (mRemotePlayerData.get(i).isWudi) {
                    mRemotePlayers.get(i).getPhysicsBody().setCollideCode(0);
                } else {
                    mRemotePlayers.get(i).getPhysicsBody().setCollideCode(0x1000);
                }

            }

            byte[] recData = new byte[5120];
            try {
                mReceiver.read(recData);
                //System.out.println("sasasa" + new String(recData).trim());
                NetMessage message = mGson.fromJson(new String(recData).trim(), NetMessage.class);
                if (message.type == NetMessage.DataType.MAP_DATA) {
                    Map map = mGson.fromJson(message.data, Map.class);
                    mGameScene = new GameScene(map);
                    mGameScene.setGameStateListener(this);
                    mGameScene.addSprite(mPlayer);
                } else if (message.type == NetMessage.DataType.START_GAME) {
                    int count = Integer.parseInt(message.data);
                    for (int i = 0; i < count; i++) {
                        mRemotePlayers.add(new RemotePlayer());
                        mRemotePlayerData.add(new RemotePlayerData());
                        mGameScene.addSprite(mRemotePlayers.get(i));
                    }

                    mGameScene.start();
                    mGameStarted = true;
                } else if (message.type == NetMessage.DataType.USER_POS) {
                    //System.out.println(message.data);
                    List<RemotePlayerData> data = mGson.fromJson(message.data,
                            new TypeToken<ArrayList<RemotePlayerData>>() {
                            }.getType());
                    for (int i = 0; i < mRemotePlayerData.size(); i++) {
                        RemotePlayerData d = mRemotePlayerData.get(i);
                        d.playerY = data.get(i).playerY;
                        d.playerDistance = data.get(i).playerDistance;
                        d.weaponDistance = data.get(i).weaponDistance;
                        d.isWudi = data.get(i).isWudi;
                        d.weaponY = data.get(i).weaponY;
                        mRemotePlayers.get(i).getPhysicsBody().setPosition(
                                new Position(d.playerDistance - mGameScene.getDistance(),
                                        d.playerY)
                        );
                        // 处理其他端发来的子弹信息
                        if (data.get(i).weaponY != -1) {
                            if (!mHandledWeaponId.contains(data.get(i).weaponId)) {
                                Sprite bullet;
                                if (data.get(i).weaponType == RemotePlayerData.WeaponType.FIRE) {
                                    bullet = new FireWeapon.FireBullet();
                                } else {
                                    bullet = new GunWeapon.GunBullet();
                                }
                                bullet.getPhysicsBody().setPosition(new Position(
                                        data.get(i).weaponDistance - mGameScene.getDistance(),
                                        data.get(i).weaponY));
                                mGameScene.addSprite(bullet);
                                mHandledWeaponId.add(data.get(i).weaponId);
                            }
                        }
                    }
                } else if (message.type == NetMessage.DataType.GAME_OVER) {
                    List<Long> time = mGson.fromJson(message.data,
                            new TypeToken<ArrayList<Long>>() {
                            }.getType());
                    for (long t : time) {
                        System.out.println(t);
                    }
                    mGameScene.stopGame();
                    client.close();
                    return;
                }

                if (mGameStarted) {
                    RemotePlayerData playerData = new RemotePlayerData();
                    playerData.playerDistance = mGameScene.getDistance() + 100;
                    playerData.playerY = mPlayer.getPhysicsBody().getPosition().y;
                    playerData.isWudi = mPlayer.isWudi();
                    if (mWeaponBullet != null) {
                        playerData.weaponY = mWeaponBullet.getPhysicsBody().getPosition().y;
                        playerData.weaponDistance = playerData.playerDistance + 180;
                        playerData.weaponId = client.getInetAddress().getHostAddress() + mWeaponId;
                        if (mWeaponBullet instanceof FireWeapon.FireBullet) {
                            playerData.weaponType = RemotePlayerData.WeaponType.FIRE;
                        } else {
                            playerData.weaponType = RemotePlayerData.WeaponType.GUN;
                        }

                    } else {
                        playerData.weaponY = -1;
                    }
                    String data = mGson.toJson(playerData, RemotePlayerData.class);
                    NetMessage sendMessage = new NetMessage();
                    sendMessage.type = NetMessage.DataType.USER_POS;
                    sendMessage.data = data;

                    if (mGameOver) {
                        sendMessage.gameOver = true;
                        sendMessage.time = mGameTime;
                    }

                    mSender.write(mGson.toJson(sendMessage, NetMessage.class).getBytes());
                }
                //sleep(5);
            } catch (Exception e) {
                if (++mErrorsNum >= 3) {
                    break;
                }
            }
        }
    }

    @Override
    public void onGameOver(long time) {
        mGameTime = time;
        mGameOver = true;
    }

    @Override
    public void onGameStart() {

    }

    @Override
    public void onWeaponBulletAdd(Sprite sprite) {
        mGameScene.addSprite(sprite);
        mWeaponBullet = sprite;
        mWeaponId++;
    }
}
