package networks.broadcast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpPort {
    private InetAddress mAddress;
    private int port;


    public IpPort() throws UnknownHostException {
        this(InetAddress.getByName("0.0.0.0"), 0);
    }

    IpPort(InetAddress address, int p) {
        mAddress = address;
        port = p;
    }

    public InetAddress getAddress() {
        return mAddress;
    }

    public void setAddress(InetAddress address) {
        mAddress = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
