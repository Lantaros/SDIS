import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


public class Peer implements Services {
    private static final double MAX_BACKUP_SIZE = 2048;
    private final int CHUNK_SIZE = 64000;

    private MulticastSocket controlSocket;
    private MulticastSocket dataBackup;
    private MulticastSocket dataRecovery;
    private String version;

    int id;
    String rmiID;
    double diskSpace;
    ArrayList<Chunk> storedChunks;
    ConcurrentHashMap<Chunk, ArrayList<Integer>> peersStoredChunk;

    private Peer(String version, String peerID, String rmiID,
         String ctrlSckIp, String ctrlSckPort, String dtaBackIp,
         String dtaBackPort, String dtaRecIp, String dtaRecPort, String diskSpace){

        if(!version.equals("1.0")) {
            System.out.println("Invalid Protocol version\n. The only peer version avaiable is the 1.0");
            System.exit(2);
        }
        this.version = "1.0";

        this.diskSpace = Double.parseDouble(diskSpace);
        if(this.diskSpace > MAX_BACKUP_SIZE /*|| this.diskSpace > FileStuct.getFreeSpace()  */){
            System.out.println("Exceeded maximum peer disk backup space. Maximum allowed " + MAX_BACKUP_SIZE);
            System.exit(3);
        }

        this.id = Integer.parseInt(peerID);
        this.rmiID = rmiID;


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

        peersStoredChunk = new ConcurrentHashMap<>();
    }

    /**
     * Peer dispatcher thread
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


        Peer p = null;
        try {
            p = new Peer(args[0], args[1], args[2],
                    args[3], args[4], args[5], args[6],
                    args[7], args[8], args[9]);

            Services stub = (Services) UnicastRemoteObject.exportObject(p, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(p.rmiID, stub);

            System.out.println("Peer " + p.id + " is ready");
        } catch (Exception e) {
            System.err.println("Peer " + args[1] + " exception: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }

        //Read requests
        byte[] rcvBuffer = new byte[64512];
        DatagramPacket receivePacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);
        while(true){
            try {
                p.controlSocket.receive(receivePacket);
            } catch (IOException e) {
                System.out.println("Error receiving ");
            }
            
            p.handleRequest(receivePacket);
        }
    }

    private void handleRequest(DatagramPacket receivePacket) {
        String response = new String(receivePacket.getData(), Charset.forName("ISO_8859_1"));
        Message message = new Message(response);

        if(!message.getVersion().equals(this.version))
            return; //Ignore message

        switch (message.getType()){
            case PUTCHUNK:
                if(message.getSenderID() != this.id){
                    Chunk chunk = new Chunk(message.getFileID(), message.getChunkNum());
                    if(!storedChunks.contains(chunk))
                        if(this.diskSpace - message.getPayload().length >= 0){
                            this.diskSpace -= message.getPayload().length;
                            chunk.setData(message.getPayload());
                            FileInfo.saveChunk(this, chunk);

                        }
                    //Protocol.sendSTORED(message)
                }
            break;
        }
    }


    public String testConnection() {
        return "Show de Bola Galera!!!";
    }

    public void backup(String pathname, int repDegree){
        java.io.File file = new java.io.File(pathname);
        FileInputStream fileInput;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Peer " + this.id + "File not found " + pathname);
            return;
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MessageDigest Algorithm does not exist");
        }

        String nameLastModification = file.getName() + file.lastModified();
        byte[] fileID = digest.digest(nameLastModification.getBytes(StandardCharsets.UTF_8));


        long nChunks = file.length() / CHUNK_SIZE;
        byte[] buff = new byte[CHUNK_SIZE];
        long waitTime;
        int nTries = 0;

        for (int i = 0; i < nChunks; i++){
            nTries++;
            waitTime = 1000;
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

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted");
            }
        }
    }

}
