import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.io.OutputStream;

public class ListenerConnection implements Runnable {

    private SSLServerSocket sslSocket;
    private Socket socket;
    private InputStream receiveStream;
    private OutputStream sendStream;
    //protected static byte[] msg = new byte[1024];
    private int id = 1;

    @Override
    public void run() {

        while (true) {          

            try {
                socket = Server.sslSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                socket.setReceiveBufferSize(1024);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                receiveStream = socket.getInputStream();
                sendStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }       

            Server.saveClient(socket, receiveStream, sendStream, id);
            id++;
        }
    }
}