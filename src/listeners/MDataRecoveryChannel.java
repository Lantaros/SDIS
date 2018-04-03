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
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = new String(receivedPacket.getData(), Charset.forName("ISO_8859_1"));
            Message message = new Message(response);

            Chunk chunk = new Chunk(message.getFileID(), message.getChunkNum(), message.getPayload());
            if(!peer.getRestoredChunks().contains(chunk))
                peer.getRestoredChunks().add(chunk);
            System.out.println("analyzed");

            for(int i = 0; i < peer.getRestoredChunks().size(); i++){
                System.out.println("Chunk Nr: " + peer.getRestoredChunks().get(i).getOrderNum());
                System.out.println("Length: " + peer.getRestoredChunks().get(i).getData().length);
            }

            System.out.println(peer.getRestoredChunks().size());


            if(peer.getRestoredChunks().size() == Peer.numChunksRestore) {
                Collections.sort(peer.getRestoredChunks());
            };
        }

    }
}
