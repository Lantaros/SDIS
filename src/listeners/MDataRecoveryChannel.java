package listeners;

import protocol.Chunk;
import protocol.Message;
import protocol.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

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
                System.out.println("received message");
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = new String(receivedPacket.getData(), Charset.forName("ISO_8859_1"));
            Message message = new Message(response);

            peer.getRestoredChunks().add(new Chunk(message.getFileID(), message.getChunkNum(), message.getPayload()));
            System.out.println("analyzed");


            if(peer.getRestoredChunks().size() == Peer.numChunksRestore) {
                Collections.sort(peer.getRestoredChunks());
            };
        }

    }
}
