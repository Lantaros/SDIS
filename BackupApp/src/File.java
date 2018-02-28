
public class File {
    private int id;
    private String name;
    private String pathname;
    private int desiredRepDeg;

    byte[] data;

    /**
     * Splits the file into chunks
     * @return Chunk array, all the chunks a file is composed of
     */
    public Chunk[] getChunks(){
        return null;
    }
}