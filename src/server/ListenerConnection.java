package server;

import javax.net.ssl.SSLServerSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

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