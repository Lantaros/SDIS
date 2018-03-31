package protocol;

import java.security.InvalidParameterException;

public enum MessageType {
    PUTCHUNK, //BACKUP chunk request
    STORED,   //Successfully stored chunk notification

    GETCHUNK, //Restore chunk request
    CHUNK,

    DELETE,   //Delete file request
    REMOVED,

    MANAGE_STORAGE,
    RETRIEVE_INFO;

    @Override
    public String toString() {
        switch (this) {
            case PUTCHUNK:
                return "PUTCHUNK";

            case STORED:
                return "STORED";

            case GETCHUNK:
                return "GETCHUNK";

            case CHUNK:
                return "CHUNK";

            case DELETE:
                return "DELETE";

            case REMOVED:
                return "REMOVED";

            case MANAGE_STORAGE:
                return "MANAGE_STORAGE";

            case RETRIEVE_INFO:
                return "RETRIEVE_INFO";

            default:
                return "FAIL";
        }
    }

    public static MessageType fromString(String type){
        switch (type){
            case "PUTCHUNK":
                return MessageType.PUTCHUNK;

            case "STORED":
                return MessageType.PUTCHUNK;

            case "GETCHUNK":
                return MessageType.GETCHUNK;

            case "CHUNK":
                return MessageType.CHUNK;

            case "DELETE":
                return MessageType.DELETE;

            case "REMOVED":
                return MessageType.REMOVED;

            case "MANAGE_STORAGE":
                return MessageType.MANAGE_STORAGE;

            case "RETRIEVE_INFO":
                return MessageType.RETRIEVE_INFO;

            default:
                throw new InvalidParameterException();
        }
    }
}