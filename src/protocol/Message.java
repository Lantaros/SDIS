package protocol;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF>
 */
public class Message {
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";

    private MessageType type;
    private String version;

    private int senderID;
    private String fileID; //File hash
    private int chunkNum;
    private int desiredRepDeg;
    byte[] payload;


    public MessageType getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public int getSenderID() {
        return senderID;
    }

    public String getFileID() {
        return fileID;
    }

    public int getChunkNum() {
        return chunkNum;
    }

    public int getDesiredRepDeg() {
        return desiredRepDeg;
    }

    public byte[] getPayload() {
        return payload;
    }

    //PutChunk Protocol message
    public Message(MessageType type, String version, int senderId, String fileID, int chunkNum, int desiredRepDegree, byte[] body) {
        this.type = type;
        this.version = version;
        this.senderID = senderId;
        this.fileID = fileID;
        this.chunkNum = chunkNum;
        this.desiredRepDeg = desiredRepDegree;
        this.payload = body;
    }

    public Message(String response) {
        String[] splitedMsg = response.trim().split("\r\n\r\n");
        String[] tokens = splitedMsg[0].split(" ");

        this.type = MessageType.fromString(tokens[0]);

        if(!tokens[1].equals("1.0"))
            return;

        switch (this.type){
            case PUTCHUNK:
                this.senderID = Integer.parseInt(tokens[2]);
                this.fileID = tokens[3];
                this.chunkNum = Integer.parseInt(tokens[4]);
                this.desiredRepDeg = Integer.parseInt(tokens[5]);
                this.payload = splitedMsg[1].getBytes(Charset.forName("ISO_8859_1"));
            break;

            case STORED:
                this.senderID = Integer.parseInt(tokens[2]);
                this.fileID = tokens[3];
                this.chunkNum = Integer.parseInt(tokens[3]);
            break;
            default:
                return;
        }
    }

    public String toString(){

        switch (this.type){
            case PUTCHUNK:
                return type.toString() + ' ' + version + ' ' +
                        Integer.toString(senderID) + ' ' + fileID +
                        ' ' + Integer.toString(chunkNum) + ' ' +
                        Integer.toString(desiredRepDeg) + ' ' + CRLF +
                        CRLF + new String(payload, Charset.forName("ISO_8859_1"));
            case STORED:
                return type.toString() + ' ' + version + ' ' +
                        Integer.toString(senderID) + ' ' + fileID +
                        ' ' + Integer.toString(chunkNum) + CRLF + CRLF;
            default:
                return "";
        }
    }
}