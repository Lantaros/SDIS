package protocol;

import java.util.Objects;

/**
 * FileStuct's Chunk representation class
 */

public class Chunk implements  Comparable{
    private int orderNum;
    String fileID; //fileHash

    private int repDegree;
    private byte[] data;

    public Chunk(String fileID, int chunkNum) {
        this.fileID = fileID;
        this.orderNum = chunkNum;
    }

    public Chunk(String fileID, int chunkNum, byte[] payload) {
        this.fileID = fileID;
        this.orderNum = chunkNum;
        this.data = payload;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public int getRepDegree() {
        return repDegree;
    }

    public String getFileID() {
        return fileID;
    }

    public byte[] getData() {
        return data;
    }

    public int getSize(){
        return this.data.length;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Chunk) {
            Chunk chunk = (Chunk) obj;
            return fileID.equals(chunk.fileID) && this.orderNum == chunk.getOrderNum();
        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(fileID, orderNum);
    }

    public void setData(byte[] payload) {
        this.data = payload;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Chunk) {
            Chunk chunk = (Chunk) o;

            if(this.orderNum < chunk.orderNum)
                return -1;
            else
                return 1;
        }
        return 0;
    }

    //public Chunk()
}
