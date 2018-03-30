/**
 * <MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>
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
    private int desiredRepDeg; //
    byte[] payload;

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
}