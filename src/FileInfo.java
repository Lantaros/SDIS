import java.io.*;
import java.nio.charset.Charset;

public class FileInfo {


    public static void saveChunk(Peer peer, Chunk chunk) {
        File file = new File("peer" + peer.id + "/" + new String(chunk.getFileID()) +"|" + chunk.getOrderNum());
        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found Exception");
        }
    }

    /**TODO
     * Splits the file into chunks
     * @return Chunk array, all the chunks a file is composed of
     */
    public Chunk[] splitFile(){
        return null;
    }

    //public static void saveFile();
}