package protocol;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public class Protocol {
   /* public static boolean putChunk(Peer peer, FileInputStream fileInput, byte[] fileID, int chunkNum, long waitTime){

        try {
            fileInput.read(buff, i*CHUNK_SIZE, CHUNK_SIZE);
        } catch (IOException e) {
            System.out.println("Chunk " + i + "IOException");
        }

        Message message = new Message(MessageType.PUTCHUNK, this.version, this.id, fileID, i, repDegree, buff);
        byte[] messageBytes = message.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, dataBackup.getLocalAddress(), dataBackup.getLocalPort());

        try {
            dataBackup.send(packet);
        } catch (IOException e) {
            System.out.println("Failed sending PutChunk message");
        }
    }*/

}
