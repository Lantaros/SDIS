import java.io.IOException;
import java.net.DatagramPacket;

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

            Message msg = new Message(MessageType.PORT, Server.getFreeTCPPort(), Server.getNextClientID());
            DatagramPacket answer = new
            Server.udpSocket.send();
        }
    }
}
