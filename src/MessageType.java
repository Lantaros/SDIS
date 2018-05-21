public enum MessageType {
    ROOM_AVAILABLE,
    ROOM_CONNECT,
    ROOM_CREATE,
    PEERS_INFO,
    REQUEST_PORTS,
    SEND_PORTS,
    OWN_CLIENT_ID,
    PORT_TO_SEND,
    PORT_TO_CONNECT,
    PEER_INFO,

    START_REQUEST,
    START_CHECK,
    START_GAME,

    WORD_REQUEST,
    WORD_CHECK,
    WORD_TO_GUESS,

    LETTER_REQUEST,
    LETTER_CHECK,
    LETTER_TO_CHECK;


    @Override
    public String toString() {
        switch (this) {
            case ROOM_AVAILABLE:
                return "ROOM_AVAILABLE";

            case ROOM_CONNECT:
                return "ROOM_CONNECT";

            case ROOM_CREATE:
                return "ROOM_CREATE";

            case REQUEST_PORTS:
                return "REQUEST_PORTS";

            case SEND_PORTS:
                return "SEND_PORTS";

            case OWN_CLIENT_ID:
                return "OWN_CLIENT_ID";

            case PORT_TO_SEND:
                return "PORT_TO_SEND";

            case PORT_TO_CONNECT:
                return "PORT_TO_CONNECT";
                
            case PEER_INFO:
                return "PEER_INFO";

            case PEER_INFO:
                return "PEER_INFO";

            default:
                return "FAIL";
        }
    }

    public static MessageType fromString(String type) throws InvalidMessage {
        switch (type) {
            case "ROOM_AVAILABLE":
                return ROOM_AVAILABLE;

            case "ROOM_CONNECT":
                return ROOM_CONNECT;

            case "ROOM_CREATE":
                return ROOM_CREATE;

            case "REQUEST_PORTS":
                return REQUEST_PORTS;

            case "SEND_PORTS":
                return SEND_PORTS;

            case "OWN_CLIENT_ID":
                return OWN_CLIENT_ID;

            case "PORT_TO_SEND":
                return PORT_TO_SEND;

            case "PORT_TO_CONNECT":
                return PORT_TO_CONNECT;

            case "PEER_INFO":
                return PEER_INFO;
                
            default:
                throw new InvalidMessage(type);
        }
    }
}


