import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.HashMap;

public class Peer {
    private MulticastSocket controlSocket;
    private MulticastSocket dataBackup;
    private MulticastSocket dataRecovery;

   // private DatagramSocket testCliSocket;

    int id;
    double diskSpace;
    HashMap<String, Chunk> chunkTable;

    Peer(String ctrlSckIp, int ctrlSckPort, String dtaBackIp, int dtaBackPort, String dtaRecIp, int dtaRecPort){

        try {
            InetAddress group = InetAddress.getByName(ctrlSckIp);
            this.controlSocket = new MulticastSocket(ctrlSckPort);
            this.controlSocket.joinGroup(group);
        }
        catch(IOException e){
            System.out.println("Error creating multicast CONTROL socket, with IP" + ctrlSckIp + " and port " + ctrlSckPort);
        }

        try {
            InetAddress group = InetAddress.getByName(dtaBackIp);
            this.dataBackup = new MulticastSocket(dtaBackPort);
            this.dataBackup.joinGroup(group);
        }
        catch(IOException e){
            System.out.println("Error creating multicast BACKUP socket, with IP" + dtaBackIp + " and port " + dtaBackPort);
        }

        try {
            InetAddress group = InetAddress.getByName(dtaRecIp);
            this.dataBackup = new MulticastSocket(dtaRecPort);
            this.dataBackup.joinGroup(group);
        }
        catch(IOException e){
            System.out.println("Error creating multicast RECOVERY socket, with IP" + dtaRecIp + " and port " + dtaRecPort);
        }



    }

    public static void main(String[] args){
        Peer p = new Peer();

    }

    Peer(){

    }

    void dispatcher(){

    }
}
