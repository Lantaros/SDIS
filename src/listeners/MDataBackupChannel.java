package listeners;

import protocol.Chunk;
import protocol.FileUtils;
import protocol.Message;
import protocol.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.Charset;
import java.util.Random;

public class MDataBackupChannel implements Runnable{
    private Peer peer;

    public MDataBackupChannel(Peer p){
        peer = p;
    }

    @Override
    public void run() {
        byte[] buff = new byte[Peer.CHUNK_SIZE];

        DatagramPacket receivedPacket = new DatagramPacket(buff, Peer.CHUNK_SIZE);

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
                        Chunk chunk = new Chunk(message.getFileID(), message.getChunkNum());
                        if (!peer.getStoredChunks().contains(chunk)){
                            if (peer.getDiskSpace() - message.getPayload().length >= 0) {
                                peer.setDiskSpace(peer.getDiskSpace() - message.getPayload().length);
                                chunk.setData(message.getPayload());
                                FileUtils.saveChunk(peer, chunk);
                                peer.getStoredChunks().put(message.getFileID(), chunk);
                            }
                            else
                                break;
                        }
                        sendStored(message);
                    }
                    break;
            }
        }
    }

    public void sendStored(Message msg){

        byte[] messageBytes = msg.toString().getBytes(Charset.forName("ISO_8859_1"));

        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, Peer.controlSocket.getLocalAddress(), Peer.controlSocket.getLocalPort());

            Random r = new Random(System.currentTimeMillis());
            try {
                Thread.sleep(r.nextInt(401));
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted");
            }

        try {
            Peer.controlSocket.send(packet);
        } catch (IOException e) {
            System.out.println("Failed sending STORED");
        }
    }

}