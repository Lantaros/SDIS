package protocol;

import listeners.MDataBackupChannel;
import listeners.MDataRecoveryChannel;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    public static Lock allChunksReceived;
    public static int numChunksRestore;

    private String version;

    private int id;
    private String rmiID;
    private double diskSpace;
    private ArrayList<Chunk> restoredChunks;
    private ConcurrentHashMap<String, ConcurrentLinkedDeque<Chunk>> storedChunks;
    private ConcurrentHashMap<Chunk, HashSet<Integer>> peersStoredChunk;
    public static ExecutorService poolExecutor;

    public String getVersion() {
        return version;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Chunk> getRestoredChunks() {
        return restoredChunks;
    }

    public double getDiskSpace() {
        return diskSpace;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedDeque<Chunk>> getStoredChunks() {
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
        restoredChunks = new ArrayList<Chunk>();

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

	 Pattern pattern = Pattern.compile("(?:(?:([^:]+):)?(\\d+)\\/)?(\\w+)");
	 Matcher m = pattern.matcher(args[2]);
	 m.matches();

	String rmiID = "";
	String registryIP = "127.0.0.1";
	int registryPort = 1099;

	System.out.println("Group Count "+ m.groupCount());
	if(m.groupCount() == 1)
		rmiID =  m.group(1);
	else if(m.groupCount() == 2){
		registryPort = Integer.parseInt(m.group(1));
		rmiID = m.group(2);
	}		
	else if(m.groupCount() == 3){
		registryIP = m.group(1);
		registryPort = Integer.parseInt(m.group(2));
		rmiID = m.group(3);	
	}
	else{
		System.out.println("Invalid peer AP specified");
		System.exit(1);	
	}

	

        Peer p = null;
        try {
            p = new Peer(args[0], args[1], rmiID,
                    args[3], args[4], args[5], args[6],
                    args[7], args[8], args[9]);

            Services stub = (Services) UnicastRemoteObject.exportObject(p, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(registryIP, registryPort);
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

        MDataRecoveryChannel dRcvChannel = new MDataRecoveryChannel(p);
        Peer.poolExecutor.execute(dRcvChannel);

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
                System.out.println("Received DELETE from senderID " + message.getSenderID() + " and chunckNr " + message.getChunkNum());
                if(this.id != message.getSenderID())
                    deleteChunks(message);
                break;
            case GETCHUNK:
                System.out.println("Received GETCHUNK from senderID " + message.getSenderID() + " and chunckNr " + message.getChunkNum());
                if(this.id != message.getSenderID())
                    restoreChunk(message);
                break;

        }
        //System.out.println("HashSet Size: " + peersStoredChunk.get(chunk).size() + " | " + peersStoredChunk.size());
    }

    public boolean restoreChunk(Message message){
        //envia para o recoveryChannel

        ConcurrentLinkedDeque<Chunk> fileChunks = storedChunks.get(message.getFileID());

        for (Chunk chunk : fileChunks) {

            File file = new File("../peer" + this.id + "/" + chunk.getFileID() + "/" + chunk.getOrderNum());

            //System.out.println("../peer" + this.id + "/" + chunk.getFileID() + "/" + chunk.getOrderNum());

            Message msg = new Message(MessageType.CHUNK, version, message.getSenderID(), chunk.getFileID(), chunk.getOrderNum(), chunk.getData());

            byte[] messageBytes = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, dataRecoveryIP, dataRecovery.getLocalPort());

            if (file.exists()) {
                try {
                    Random r = new Random(System.currentTimeMillis());
                    try {
                        Thread.sleep(r.nextInt(401));
                    } catch (InterruptedException e) {
                        System.out.println("Sleep Interrupted");
                    }

                    dataRecovery.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
                System.out.println("File to RECOVER not found!");
        }

        return true;
    }

    public boolean deleteChunks(Message message){
        System.out.println("entra 1");

        int fileNr = 0;

        System.out.println("tamanho: " + storedChunks.size());

        ConcurrentLinkedDeque<Chunk> fileChunks = storedChunks.get(message.getFileID());

        for (Chunk chunk : fileChunks) {

            File file = new File("../peer" + this.id + "/" + chunk.getFileID() + "/" + chunk.getOrderNum());

            System.out.println("../peer" + this.id + "/" + chunk.getFileID() + "/" + chunk.getOrderNum());

            if (file.exists()) {
                file.delete();
            } else
                System.out.println("File to delete not found!");
        }

        File folder = new File("../peer" + this.id + "/" + message.getFileID());
        folder.delete();

        return true;
    }


    public String testConnection() {
        return "Testing RMI Connection...";
    }

    public void backup(String pathname, int repDegree){

        System.out.println("Peer" + this.id + ": BACKUP request " + pathname + " repDeg " + repDegree);

        File file = new File(pathname);
        FileInputStream fileInput;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("Peer" + this.id + " File not found " + pathname);
            return;
        }


        String fileID = file.getName() + file.lastModified();
        String fileHash = FileUtils.computeHash(fileID);

        System.out.println("FileHash: " + fileHash);

        long nChunks = file.length() / CHUNK_SIZE + 1;
        byte[] buff = new byte[CHUNK_SIZE];


        long fileSize = file.length();
        System.out.println("File size: " + fileSize);
        System.out.println("N. Chunks: " + nChunks);

        Chunk chunk;
        int readBytes;
        for (int i = 0; i < nChunks; i++){

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

            poolExecutor.execute(new Putchunk(this, packet, chunk, repDegree));
        }
    }

    public boolean delete(String pathname){

        File file = new File(pathname);

        if(!file.exists()){
            System.out.println("File does not exist!");
            return false;
        }


        String fileID = file.getName() + file.lastModified();
        String fileHash = FileUtils.computeHash(fileID);

        Message message = new Message(MessageType.DELETE, this.version, this.id, fileHash);
        byte[] messageBytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, controlSocketIP, controlSocket.getLocalPort());

        //for(int i = 0; i < 4; i++){
            try {
                controlSocket.send(packet);
                System.out.println("\nSUCCESS Sending DELETE message with fileID: " +  message.getFileID());
            } catch (IOException e) {
                System.out.println("\nFAIL Sending DELETE message with fileID: " +  message.getFileID());
                e.printStackTrace();
            }
        //}

        return true;
    }

    //GETCHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>

    public boolean restore(String pathname){
        File file = new File(pathname);
        FileInputStream fileInput;

        if(!file.exists()){
            System.out.println("File not exists!");
            return false;
        }


        long nChunks = file.length() / CHUNK_SIZE + 1;
        numChunksRestore = (int)nChunks;

        long fileSize = file.length();
        System.out.println("File size: " + fileSize);
        System.out.println("N. Chunks: " + nChunks);


        for (int i = 0; i < nChunks; i++) {

            String fileID = file.getName() + file.lastModified();
            String fileHash = FileUtils.computeHash(fileID);

            Message message = new Message(MessageType.GETCHUNK, this.version, this.id, fileHash, i);
            byte[] messageBytes = message.getBytes();
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, controlSocketIP, controlSocket.getLocalPort());

            try {
                controlSocket.send(packet);
                System.out.println("\nSUCCESS Sending GETCHUNK message with fileID: " +  message.getFileID());
            } catch (IOException e) {
                System.out.println("\nFAIL Sending GETCHUNK message with fileID: " +  message.getFileID());
                e.printStackTrace();
            }


        }

        while(getRestoredChunks().size() < nChunks){

        }


        for(int i = 0; i < getRestoredChunks().size(); i++){
            System.out.println("Posicao "+ i +" do array getRestoredChunks = " + getRestoredChunks().get(i).getOrderNum());
        }


        File restoreFile = new File("../RestoredFiles/" + file.getName());
        restoreFile.getParentFile().mkdirs();

        try {
            restoreFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(restoreFile);
        } catch (FileNotFoundException e) {
        }

        byte[] fullFile = new byte[CHUNK_SIZE * (int) nChunks];
        for(int i = 0; i < getRestoredChunks().size(); i++){
            System.out.println(getRestoredChunks().get(i).getData().length);
            System.arraycopy(restoredChunks.get(i).getData(), 0, fullFile, i*CHUNK_SIZE, CHUNK_SIZE);
        }

        try {
            fileOut.write(fullFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        restoredChunks.clear();

        return true;
    }
}
