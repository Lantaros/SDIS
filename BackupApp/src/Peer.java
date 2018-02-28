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
    HashMap<Integer, Chunk> chunkTable;






}
