package listeners;

import protocol.Message;
import protocol.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.Charset;

public class MDataRecoveryChannel implements Runnable {
    private Peer peer;

    public MDataRecoveryChannel(Peer p){
        this.peer = p;
    }
    @Override
    public void run() {

        byte[] buff = new byte[Peer.PACKET_SIZE];

        DatagramPacket receivedPacket = new DatagramPacket(buff, Peer.PACKET_SIZE);

        while (true) {

            try {
                Peer.dataRecovery.receive(receivedPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = new String(receivedPacket.getData(), Charset.forName("ISO_8859_1"));
            Message message = new Message(response);
        }

    }
}
