public class Message {
    private String version;
    private MessageType type;

    private String senderID;
    private String fileID;
    private int chunkNum;
    private char desiredRepDeg; //
    byte[] payload;

}