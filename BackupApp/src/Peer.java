import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.HashMap;

public class Peer {
    private MulticastSocket controlSocket;
    private MulticastSocket dataBackup;
    private MulticastSocket dataRecovery;

    private DatagramSocket testCliSocket;

    int id;
    double diskSpace;
    HashMap<String, Chunk> chunkTable;



    public static void main(String[] args){
        Peer p = new Peer();

    }

    Peer(){

    }

    void dispatcher(){

    }
}
