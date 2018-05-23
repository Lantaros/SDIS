package protocol;

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
    WORD_TO_GUI,

    LETTER_TO_GUESS,
    LETTER_CHECK,
    LETTER_GO,
    LETTER_TO_CHECK,
    
    READY_TO_START;


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

            case START_REQUEST:
                return "START_REQUEST";

            case START_CHECK:
                return "START_CHECK";

            case START_GAME:
                return "START_GAME";

            case WORD_REQUEST:
                return "WORD_REQUEST";

            case WORD_CHECK:
                return "WORD_CHECK";

            case WORD_TO_GUESS:
                return "WORD_TO_GUESS";

            case LETTER_TO_GUESS:
                return "LETTER_TO_GUESS";

            case LETTER_CHECK:
                return "LETTER_CHECK";

            case LETTER_GO:
                return "LETTER_GO";

            case LETTER_TO_CHECK:
                return "LETTER_TO_CHECK";
                
            case READY_TO_START:
                return "READY_TO_START";

            case WORD_TO_GUI:
                return "WORD_TO_GUI";

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

            case "START_REQUEST":
                return START_REQUEST;

            case "START_CHECK":
                return START_CHECK;

            case "START_GAME":
                return START_GAME;

            case "WORD_REQUEST":
                return WORD_REQUEST;

            case "WORD_CHECK":
                return WORD_CHECK;

            case "WORD_TO_GUESS":
                return WORD_TO_GUESS;

            case "LETTER_TO_GUESS":
                return LETTER_TO_GUESS;

            case "LETTER_CHECK":
                return LETTER_CHECK;

            case "LETTER_GO":
                return LETTER_GO;

            case "LETTER_TO_CHECK":
                return LETTER_TO_CHECK;
            
            case "READY_TO_START":
                return READY_TO_START;

            case "WORD_TO_GUI":
                return WORD_TO_GUI;

            default:
                throw new InvalidMessage(type);
        }
    }
}


