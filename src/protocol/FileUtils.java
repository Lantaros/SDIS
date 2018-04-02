package protocol;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FileUtils {


    public static void saveChunk(Peer peer, Chunk chunk) {
        System.out.println("saveChunk body size: " + chunk.getData().length);


        File file = new File("../peer" + peer.getId() + "/" + chunk.getFileID() +"/" + chunk.getOrderNum());
        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found Exception");
        }

        try {
            out.write(chunk.getData());
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }catch (NullPointerException e2){
            System.err.println("Null pointer exception at saveChunk");
        }
    }

    public static String computeHash (String rawfileID){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MessageDigest Algorithm: SHA-256 does not exist");
        }
        byte[] hash = digest.digest(rawfileID.getBytes());
        String hashHex = new BigInteger(1, hash).toString(16);


        return hashHex;
    }

    //public static void saveFile();
}