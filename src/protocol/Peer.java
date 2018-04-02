package protocol;

import listeners.MDataBackupChannel;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Peer implements Services {
    public static final double MAX_BACKUP_SIZE = 2048;
    public static final int MAX_PUTCHUNK_ATTEMPTS = 5;
    public static final int CHUNK_SIZE = 64000;
    public static final int PACKET_SIZE = 64200;

    public static MulticastSocket controlSocket;
    public static MulticastSocket dataBackup;
    public static MulticastSocket dataRecovery;

    public static InetAddress controlSocketIP;
    public static InetAddress dataBackupIP;
    public static InetAddress dataRecoveryIP;

    private String version;

    private int id;
    private String rmiID;
    private double diskSpace;
    private ConcurrentHashMap<String, Chunk> storedChunks;
    private ConcurrentHashMap<Chunk, HashSet<Integer>> peersStoredChunk;
    public static ExecutorService poolExecutor;

    public String getVersion() {
        return version;
    }

    public int getId() {
        return id;
    }

    public double getDiskSpace() {
        return diskSpace;
    }

    public ConcurrentHashMap<String, Chunk> getStoredChunks() {
        return storedChunks;
    }

    public ConcurrentHashMap<Chunk, HashSet<Integer>> getPeersStoredChunk() {
        return peersStoredChunk;
    }

    public void setDiskSpace(double diskSpacepace){
        this.diskSpace = diskSpace;
    }


    private Peer(String version, String peerID, String rmiID,
         String ctrlSckIp, String ctrlSckPort, String dtaBackIp,
         String dtaBackPort, String dtaRecIp, String dtaRecPort, String diskSpace){

        System.setProperty("java.net.preferIPv4Stack", "true");

        if(!version.equals("1.0")) {
            System.out.println("Invalid Protocol version\n. The only peer version avaiable is the 1.0");
            System.exit(2);
        }
        this.version = "1.0";

        this.diskSpace = Double.parseDouble(diskSpace);
//        if(this.diskSpace > MAX_BACKUP_SIZE /*|| this.diskSpace > FileStuct.getFreeSpace()  */){
//            System.out.println("Exceeded maximum peer disk backup space. Maximum allowed " + MAX_BACKUP_SIZE);
//            System.exit(3);
//        }

        this.id = Integer.parseInt(peerID);
        this.rmiID = rmiID;


        try {
            System.out.println("Control " + ctrlSckIp + ":" + ctrlSckPort);
            controlSocketIP = InetAddress.getByName(ctrlSckIp);
            this.controlSocket = new MulticastSocket(Integer.parseInt(ctrlSckPort));
            this.controlSocket.setTimeToLive(1);
            this.controlSocket.joinGroup(controlSocketIP);
        }
        catch(IOException e){
            System.out.println("Error creating multicast CONTROL socket, with IP" + ctrlSckIp + " and port " + ctrlSckPort);
            System.exit(1);
        }

        try {
            System.out.println("DataBckup " + dtaBackIp + ":" + dtaBackPort);
            dataBackupIP = InetAddress.getByName(dtaBackIp);
            this.dataBackup = new MulticastSocket(Integer.parseInt(dtaBackPort));
            this.dataBackup.setTimeToLive(1);
            this.dataBackup.joinGroup(dataBackupIP);
        }
        catch(IOException e){
            System.out.println("Error creating multicast BACKUP socket, with IP" + dtaBackIp + " and port " + dtaBackPort);
            System.exit(1);
        }

        try {
            System.out.println("DataRecovery " + dtaRecIp + ":" + dtaRecPort);
            dataRecoveryIP = InetAddress.getByName(dtaRecIp);
            this.dataRecovery = new MulticastSocket(Integer.parseInt(dtaRecPort));
            this.dataRecovery.setTimeToLive(1);
            this.dataRecovery.joinGroup(dataRecoveryIP);
        }
        catch(IOException e){
            System.out.println("Error creating multicast RECOVERY socket, with IP" + dtaRecIp + " and port " + dtaRecPort);
            System.exit(1);
        }

        poolExecutor = Executors.newCachedThreadPool();
        peersStoredChunk = new ConcurrentHashMap<>();
        storedChunks = new ConcurrentHashMap<>();
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
            registry.rebind(p.rmiID, stub);

            System.out.println("Peer " + p.id + " is ready");
        } catch (Exception e) {
            System.err.println("Peer " + args[1] + " exception: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }

        //Start Backup Data
        MDataBackupChannel dBckChannel = new MDataBackupChannel(p);
        Peer.poolExecutor.execute(dBckChannel);

        //Listen to Control Channel requests
        byte[] rcvBuffer = new byte[PACKET_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(rcvBuffer, rcvBuffer.length);

        while(true){
            try {
                Peer.controlSocket.receive(receivePacket);
                String resp = new String(receivePacket.getData());
                //System.out.println("Control " + resp);
                Message msg = new Message(resp);
                p.handleControlRequest(receivePacket);

                //System.out.println("\nSuccess receiving " + msg.getType() + "via <controlSocket channel> \n<senderID: " + msg.getSenderID()  + "> ");
            } catch (IOException e) {
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void handleControlRequest(DatagramPacket receivePacket) {
        Message message = new Message(new String(receivePacket.getData()));
        Chunk chunk = new Chunk(message.getFileID(), message.getChunkNum());

        switch (message.getType()) {
            case STORED:
                System.out.println("Received STORED from senderID " + message.getSenderID() + " and chunckNr " + message.getChunkNum());


                if(peersStoredChunk.get(chunk) == null){
                    peersStoredChunk.put(chunk, new HashSet<>()); //Create new chunk entry with no peers who stored it
                    peersStoredChunk.get(chunk).add(message.getSenderID());
                }
                else{
                    peersStoredChunk.get(chunk).add(message.getSenderID());
                }
            break;
            case DELETE:

        }
        System.out.println("HashSet Size: " + peersStoredChunk.get(chunk).size() + " | " + peersStoredChunk.size());
    }


    public String testConnection() {
        return "Testing RMI Connection...";
    }

    public boolean backup(String pathname, int repDegree){

        System.out.println("Peer" + this.id + ": BACKUP request " + pathname + " repDeg " + repDegree);

        File file = new File(pathname);
        FileInputStream fileInput;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Peer" + this.id + " File not found " + pathname);
            return false;
        }


        String fileID = file.getName() + file.lastModified();
        String fileHash = FileUtils.computeHash(fileID);

        System.out.println("FileHash: " + fileHash);

        long nChunks = file.length() / CHUNK_SIZE + 1;
        byte[] buff = new byte[CHUNK_SIZE];


        long fileSize = file.length();
        System.out.println("File size: " + fileSize);
        System.out.println("N. Chunks: " + nChunks);

        long waitTime;
        int nTries;

        Chunk chunk;
        int readBytes;
        for (int i = 0; i < nChunks; i++){
            nTries = 0;
            waitTime = 500;

            chunk = new Chunk(fileHash, i);
            if(!peersStoredChunk.containsValue(chunk))
                peersStoredChunk.put(chunk, new HashSet<>()); //Create new chunk entry with no peers who stored it


            try {
                readBytes = fileInput.read(buff);
                System.out.println("Read " + readBytes + " bytes from the file");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message message = new Message(MessageType.PUTCHUNK, this.version, this.id, fileHash, i, repDegree, buff);
            byte[] messageBytes = message.getBytes();

            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, dataBackupIP, dataBackup.getLocalPort());

            while(nTries < MAX_PUTCHUNK_ATTEMPTS && peersStoredChunk.get(chunk).size() < repDegree) {
                nTries++;
                waitTime = waitTime * 2;

                System.out.println("Current Replication degree " + peersStoredChunk.get(chunk).size());

                try {
                    dataBackup.send(packet);
                   System.out.println("\nTry nr." + nTries + " sending PUTCHUNK message " +  message.getFileID() + " chunk " + i);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    System.out.println("Sleep Interrupted");
                }
            }
            if(nTries > MAX_PUTCHUNK_ATTEMPTS)
                System.out.println("Didn't achieve desired repDegree on Chunk " + i);
            else
                System.out.println("Desired repDegree Achieved");

        }
        return true;
    }
}
