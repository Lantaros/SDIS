
public class FileInfo {
    private int id;
    private String name;
    private String pathname;
    private int desiredRepDeg;

    byte[] data;

    FileInfo(String pathname){
        //java.io.File
    }
    /**
     * Splits the file into chunks
     * @return Chunk array, all the chunks a file is composed of
     */
    public Chunk[] splitFile(){
        return null;
    }
}