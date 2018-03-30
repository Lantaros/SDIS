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
    private byte[] fileID; //File hash
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

    public byte[] getFileID() {
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
    public Message(MessageType type, String version, int senderId, byte[] fileHash, int chunkNum, int desiredRepDegree, byte[] body) {
        this.type = type;
        this.version = version;
        this.senderID = senderId;
        this.fileID = fileHash;
        this.chunkNum = chunkNum;
        this.desiredRepDeg = desiredRepDegree;
        this.payload = body;
    }

    public Message(String response) {
        String[] tokens = response.trim().split(" ");


        this.type = MessageType.fromString(tokens[0]);

        if(!version.equals("1.0"))
            return;

        this.senderID = Integer.parseInt(tokens[1]);
        this.fileID = tokens[2].getBytes();
        this.chunkNum = Integer.parseInt(tokens[3]);
        this.desiredRepDeg = Integer.parseInt(tokens[4]);
        this.payload = tokens[4].getBytes();

    }
}