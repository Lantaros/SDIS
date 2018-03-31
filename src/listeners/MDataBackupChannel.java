package listeners;

import protocol.Chunk;
import protocol.FileInfo;
import protocol.Message;
import protocol.Peer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.Charset;

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
                                FileInfo.saveChunk(peer, chunk);

                            }
                            else
                                break;
                        }
                        sendSTORED(message);
                    }
                    break;
            }
        }
    }

    public void sendSTORED(Message msg){
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, dataBackup.getLocalAddress(), dataBackup.getLocalPort());

        while(nTries <= MAX_PUTCHUNK_ATTEMPTS && peersStoredChunk.get(chunk).size() < repDegree) {

            try {
                dataBackup.send(packet);
            } catch (IOException e) {
                System.out.println("Failed sending PutChunk message");
            }

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted");
            }
        }

}