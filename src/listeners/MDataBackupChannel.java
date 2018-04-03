package listeners;

import protocol.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class MDataBackupChannel implements Runnable{
    private Peer peer;

    public MDataBackupChannel(Peer p){
        peer = p;
    }

    @Override
    public void run() {
        byte[] buff = new byte[Peer.PACKET_SIZE];

        DatagramPacket receivedPacket = new DatagramPacket(buff, Peer.PACKET_SIZE);

        while (true) {

            try {
                Peer.dataBackup.receive(receivedPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String response = new String(receivedPacket.getData(), Charset.forName("ISO_8859_1"));
            Message message = new Message(response);

            if (!message.getVersion().equals(peer.getVersion()))
                continue; //Ignore message

            switch (message.getType()) {
                case PUTCHUNK:
                    if (message.getSenderID() != peer.getId()) {
                        System.out.println("Received PUTCHUNK " + message.getFileID() + " " + message.getChunkNum());
                        Chunk chunk = new Chunk(message.getFileID(), message.getChunkNum());

                        if(!peer.getStoredChunks().containsKey(message.getFileID()))
                            peer.getStoredChunks().put(message.getFileID(), new ConcurrentLinkedDeque<>());

                        if (!peer.getStoredChunks().get(message.getFileID()).contains(chunk)){
                            System.out.println("Saving new chunk!!");

                                chunk.setData(message.getPayload());
                                FileUtils.saveChunk(peer, chunk);

                                peer.getStoredChunks().get(message.getFileID()).add(chunk);
                        }
                        Message storedMsg = new Message(MessageType.STORED, message.getVersion(), peer.getId(), message.getFileID(), message.getChunkNum());
                        sendStored(storedMsg);
                    }
                    break;
            }
        }
    }

    public void sendStored(Message msg){

        byte[] messageBytes = msg.getBytes();

        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, Peer.controlSocketIP, Peer.controlSocket.getLocalPort());

            Random r = new Random(System.currentTimeMillis());
            try {
                Thread.sleep(r.nextInt(401));
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted");
            }

        try {
            Peer.controlSocket.send(packet);
            System.out.println("\nSuccess sending STORED  <senderID: " + msg.getSenderID()  + "> \n<fileID: " + msg.getFileID() + " chunkN " + msg.getChunkNum());
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

}