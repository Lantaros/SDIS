import javax.net.ssl.SSLServerSocket;

public class ClientConnection {
    int clientID;
    String clientIP;
    SSLServerSocket socket;

    public ClientConnection(int nextClientID, String clientIP, SSLServerSocket sslSocket) {
        this.clientID = nextClientID;
        this.clientIP = clientIP;
        socket = sslSocket;        
    }
}
