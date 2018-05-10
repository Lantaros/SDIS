import java.net.InetAddress;
import java.net.Socket;

public class ClientConnection {
    int clientID;
    InetAddress clientIP;
    Socket socket;

    public ClientConnection(int nextClientID, InetAddress clientIP, Socket sslSocket) {
        this.clientID = nextClientID;
        this.clientIP = clientIP;
        socket = sslSocket;        
    }
}
