import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.io.OutputStream;

public class PeerConection implements Runnable {

    private SSLServerSocket sslSocket;
    private Socket socket;
    private InputStream receiveStream;
    private OutputStream sendStream;

    @Override
    public void run() {         
            
            SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            try {
                int port = Client.nextFreePort(49152, 65535);
                sslSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }

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

            //Server.saveClient(socket, receiveStream, sendStream, id);

    }
}