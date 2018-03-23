import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Peer implements Services {
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

    public String testConnection() {
        return "Show de Bola Galera!!!";
    }
}
