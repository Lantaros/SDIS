/**
 * File's Chunk representation class
 */

public class Chunk {
    private int orderNum;
    private int repDegree;
    private String fileID;
    private String fileName;

    private byte[] data;

    public int getOrderNum() {
        return orderNum;
    }

    public int getRepDegree() {
        return repDegree;
    }

    public String getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getData() {
        return data;
    }

    public int getSize(){
        return this.data.length;
    }

}
