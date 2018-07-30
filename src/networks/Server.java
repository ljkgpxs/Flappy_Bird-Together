package networks;

import static java.lang.Thread.sleep;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.Position;
import utils.Map;

public class Server {

    private int mPort;

    private Map mGameMap;

    private ServerSocket mServerSocket;

    private List<Socket> mClientList;
    private List<RemotePlayerData> mClientsPosition;

    private List<Boolean> mStartGame;
    private List<Boolean> mGameStarted;

    private List<Thread> mHandlers;

    private Gson mGson;

    public Server(int port) {
        mPort = port;
        mClientList = new ArrayList<>();
        mGson = new Gson();
        mClientsPosition = new ArrayList<>();
        mGameMap = Map.create();
        mStartGame = new ArrayList<>();
        mGameStarted = new ArrayList<>();
        mHandlers = new ArrayList<>();
        try {
            mServerSocket = new ServerSocket(mPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(() -> {
            int i = 0;
            while (true) {
                try {
                    Socket socket = mServerSocket.accept();
                    mGameStarted.add(false);
                    mStartGame.add(false);
                    mClientList.add(socket);
                    mClientsPosition.add(new RemotePlayerData());
                    System.out.println("Got a client");
                    mHandlers.add(new Thread(new Handler(socket, i)));
                    mHandlers.get(i).start();
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setStartGame(boolean startGame) {
        for (int i = 0; i < mStartGame.size(); i++) {
            mStartGame.set(i, true);
        }
    }

    class Handler implements Runnable {
        private Socket mSocket;
        private int id;
        private int mErrorsNum;

        Handler(Socket socket, int id) {
            mSocket = socket;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                NetMessage message = new NetMessage();
                message.type = NetMessage.DataType.MAP_DATA;
                message.data = mGson.toJson(mGameMap, Map.class);
                mSocket.getOutputStream().write(mGson.toJson(message, NetMessage.class).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    if (!mSocket.isConnected() || mSocket.isClosed()) {
                        System.out.println("Disconnected");
                        mHandlers.get(id).interrupt();
                    }
                    NetMessage message = new NetMessage();
                    if (mGameStarted.get(id)) {
                        byte[] data = new byte[5120];
                        mSocket.getInputStream().read(data);
                        NetMessage clientMessage = mGson.fromJson(new String(data).trim(),
                                NetMessage.class);
                        if (clientMessage.type == NetMessage.DataType.USER_POS) {
                            mClientsPosition.set(id,
                                    mGson.fromJson(clientMessage.data, RemotePlayerData.class));
                            //System.out.println("Got " + id + " position " + clientMessage.data);
                        } else {
                            System.out.println(clientMessage.data);
                        }

                        message.type = NetMessage.DataType.USER_POS;
                        List<RemotePlayerData> responseData = new ArrayList<>();
                        for (int i = 0; i < mClientsPosition.size(); i++) {
                            if (i != id) {
                                responseData.add(mClientsPosition.get(i));
                            }
                        }
                        message.data = mGson.toJson(responseData, List.class);
                        mSocket.getOutputStream().write(
                                mGson.toJson(message, NetMessage.class).getBytes());
                    }
                    if (mStartGame.get(id) && !mGameStarted.get(id)) {
                        message.type = NetMessage.DataType.START_GAME;
                        message.data = String.valueOf(mClientList.size() - 1);
                        mSocket.getOutputStream().write(
                                mGson.toJson(message, NetMessage.class).getBytes());
                        mStartGame.set(id, false);
                        mGameStarted.set(id, true);
                        continue;
                    }
                    sleep(10);
                } catch (Exception e) {
                    if (++mErrorsNum >= 3) {
                        mHandlers.get(id).interrupt();
                        break;
                    }
                }
            }
        }
    }
}
