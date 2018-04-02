package protocol;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

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
    public Message(MessageType type, String version, int senderID, String fileID, int chunkNum, int desiredRepDegree, byte[] body) {
        this.type = type;
        this.version = version;
        this.senderID = senderID;
        this.fileID = fileID;
        this.chunkNum = chunkNum;
        this.desiredRepDeg = desiredRepDegree;
        this.payload = body;
    }

    public Message(MessageType type, String version, int senderID, String fileID, int chunkNum) {
        this.type = type;
        this.version = version;
        this.senderID = senderID;
        this.fileID = fileID;
        this.chunkNum = chunkNum;
    }

    public Message(String response) {
        String[] splitedMsg = response.trim().split("\r\n\r\n");
        String[] tokens = splitedMsg[0].split(" ");

        //System.out.println(splitedMsg[0]);
        this.type = MessageType.fromString(tokens[0]);

        this.version = tokens[1];
        this.senderID = Integer.parseInt(tokens[2]);
        this.fileID = tokens[3];

        if(type != MessageType.DELETE)
            this.chunkNum = Integer.parseInt(tokens[4]);

        if(type == MessageType.PUTCHUNK)
            this.desiredRepDeg = Integer.parseInt(tokens[5]);

        if(type == MessageType.PUTCHUNK || type == MessageType.CHUNK)
            this.payload = splitedMsg[1].getBytes(Charset.forName("ISO_8859_1"));
    }

    public byte[] getBytes(){
        String header =
                type + " " +
                version + " " +
                senderID + " " +
                fileID + " ";

        if(type != MessageType.DELETE)
            header += chunkNum + " ";

        if(type == MessageType.PUTCHUNK)
            header += desiredRepDeg + " ";

        header += CRLF + CRLF;


        byte[] headerBytes = header.getBytes();

        //System.out.println("headerBytes: " + headerBytes.length);

        if(type == MessageType.PUTCHUNK || type == MessageType.CHUNK){
            byte[] fullMessage = new byte[headerBytes.length + payload.length];

            System.arraycopy(headerBytes, 0, fullMessage, 0, headerBytes.length);
            System.arraycopy(payload, 0, fullMessage, headerBytes.length, payload.length);

            //System.out.println("Payload: " + payload.length);

            //System.out.println("fullMessage: " + fullMessage.length);

            return fullMessage;
        }
        return headerBytes;
    }
}