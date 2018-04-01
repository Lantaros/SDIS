package protocol;

import java.util.Arrays;

/**
 * FileStuct's Chunk representation class
 */

public class Chunk {
    private int orderNum;
    private byte[] fileID; //fileHash

    private int repDegree;
    private byte[] data;

    public Chunk(byte[] fileID, int chunkNum) {
        this.fileID = fileID;
        this.orderNum = chunkNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public int getRepDegree() {
        return repDegree;
    }

    public byte[] getFileID() {
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
            return Arrays.equals(this.fileID, chunk.getFileID()) && this.orderNum == chunk.getOrderNum();
        }
        return false;
    }

    public void setData(byte[] payload) {
        this.data = payload;
    }

    //public Chunk()
}