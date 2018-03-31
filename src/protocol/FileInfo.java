package protocol;

import java.io.*;


public class FileInfo {


    public static void saveChunk(Peer peer, Chunk chunk) {
        File file = new File("peer" + peer.id + "/" + new String(chunk.getFileID()) +"|" + chunk.getOrderNum());
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

            try {
                out.write(chunk.getData());
            } catch (IOException e1) {
                e1.printStackTrace();
            }catch (NullPointerException e2){
                System.err.println("Null pointer exception ata saveChunk");
            }

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