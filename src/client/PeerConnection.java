package client;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class PeerConnection implements Runnable {

    private SSLServerSocket sslSocket;
    private Socket socket;
    private InputStream receiveStream;
    private OutputStream sendStream;
    private int nConnections;
    private int port;

    PeerConnection(int i, int port) {
        this.nConnections = i;
        this.port = port;
    }

    @Override
    public void run() {

        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        int i = 1;
        try {
            System.out.println(this.port);
            sslSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (i <= nConnections) {

            try {
                socket = sslSocket.accept();
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

            Client.saveClient(socket, receiveStream, sendStream);

            i++;
        }

    }
}