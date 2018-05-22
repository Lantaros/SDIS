package game;

import java.net.Socket;

public class Connection {
    int peerID;
    String hostname;
    int port;
    Socket socket;

    private boolean isAlive() {
        //TODO Check whether the connection is up
        return true;
    }
}