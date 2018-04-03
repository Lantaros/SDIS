package protocol;

import java.io.IOException;
import java.net.DatagramPacket;


public class Putchunk implements Runnable{
    private Peer peer;
    DatagramPacket packet;
    Chunk currentChunk;
    int desiredRepDegree;

    Putchunk(Peer peer, DatagramPacket packet, Chunk chunk, int desiredRepDegree){
        this.peer = peer;
        this.packet = packet;
        this.currentChunk = chunk;
        this.desiredRepDegree = desiredRepDegree;
    }

    @Override
    public void run() {
        System.out.println("Entered Putchunk");
        long nTries = 0;
        int waitTime = 500;

        System.out.println("Actual RepDegree " + peer.getPeersStoredChunk().get(currentChunk).size());

        while (nTries < Peer.MAX_PUTCHUNK_ATTEMPTS && peer.getPeersStoredChunk().get(currentChunk).size() < desiredRepDegree) {
            nTries++;
            waitTime = waitTime * 2;
            System.out.println("ENTROU!!");

            try {
                peer.dataBackup.send(packet);
                System.out.println("Enviou!!");

            } catch (IOException e) {
                System.out.println("Failed sending PutChunk message");
            }

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted");
            }
        }

        if(nTries > peer.MAX_PUTCHUNK_ATTEMPTS)
            System.out.println("Didn't achieve desired repDegree on Chunk ");
        else
            System.out.println("Desired repDegree Achieved");
    }
}