import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;

public class UDPListener implements Runnable {
    byte[] buffer = new byte[1024];
    @Override
    public void run() {
        DatagramPacket clientRequest = new DatagramPacket(buffer, buffer.length);
        while (true) {

            try {
                Server.udpSocket.receive(clientRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            SSLSocket sslSocket = Server.createSSLSocket();

            int clientID = Server.getNextClientID();


            Message msg = new Message(MessageType.CLIENT_ID, sslServerSocket.getLocalPort(), Server.getNextClientID());

            byte[] answerBytes =  msg.getBytes();
            DatagramPacket answer = new DatagramPacket(answerBytes, answerBytes.length);

            System.out.println("Client ID");
            System.out.println(msg);
            try {
                Server.udpSocket.send(answer);
            } catch (IOException e) {
                e.printStackTrace();
            }


            Socket socket = null;
            try {
                socket = sslSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server.clientConnections.add(new ClientConnection(clientID, clientRequest.getAddress(), socket));
        }
    }
}
