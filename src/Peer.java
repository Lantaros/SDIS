import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;


public class Peer implements Services {
    private MulticastSocket controlSocket;
    private MulticastSocket dataBackup;
    private MulticastSocket dataRecovery;
    private String version;
   // private DatagramSocket testCliSocket;

    int id;
    double diskSpace;
    HashMap<String, Chunk> chunkTable;

    Peer(String version, String peerID, String rmiID,
         String ctrlSckIp, String ctrlSckPort, String dtaBackIp,
         String dtaBackPort, String dtaRecIp, String dtaRecPort, String diskSpace){

        if(!version.equals("1.0")) {
            System.out.println("Invalid Protocol version\n. The only peer version avaiable is the 1.0");
            System.exit(2);
        }
        this.version = "1.0";

        if(Double.parseDouble(diskSpace) > 2048)

        try {
            InetAddress group = InetAddress.getByName(ctrlSckIp);
            this.controlSocket = new MulticastSocket(Integer.parseInt(ctrlSckPort));
            this.controlSocket.joinGroup(group);
        }
        catch(IOException e){
            System.out.println("Error creating multicast CONTROL socket, with IP" + ctrlSckIp + " and port " + ctrlSckPort);
        }

        try {
            InetAddress group = InetAddress.getByName(dtaBackIp);
            this.dataBackup = new MulticastSocket(Integer.parseInt(dtaBackPort));
            this.dataBackup.joinGroup(group);
        }
        catch(IOException e){
            System.out.println("Error creating multicast BACKUP socket, with IP" + dtaBackIp + " and port " + dtaBackPort);
        }

        try {
            InetAddress group = InetAddress.getByName(dtaRecIp);
            this.dataRecovery = new MulticastSocket(Integer.parseInt(dtaRecPort));
            this.dataRecovery.joinGroup(group);
        }
        catch(IOException e){
            System.out.println("Error creating multicast RECOVERY socket, with IP" + dtaRecIp + " and port " + dtaRecPort);
        }
    }

    /**
     * Peer
     * <Peer_AP> - RMI Object name
     * <MC> <MDB> <MDR> - IP and port for MCast Channels
     * @param args <VERSION> <Peer_ID> <Peer_AP> <MC> <MDB> <MDR> <DISK_SPACE>
     */
    public static void main(String[] args){
        if(args.length != 10) {
            System.out.println("Wrong number of arguments.\n" +
                    "Expected:\"<VERSION> <Peer_ID> <Peer_AP> <MC> <MDB> <MDR> <DISK_SPACE>\"");
            System.exit(1);
        }

        Peer p = new Peer(args[0], args[1], args[2],
                args[3], args[4], args[5], args[6],
                args[7], args[8], args[9]);

    }


    void dispatcher(){

    }

    public String testConnection() {
        return "Show de Bola Galera!!!";
    }

}
