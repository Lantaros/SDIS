package protocol;

public enum MessageType {
    ROOM_AVAILABLE,
    ROOM_CONNECT,
    ROOM_CREATE,

    ROOM_CREATED,
    MAX_ROOMS_REACHED,
    DUP_ROOM_NAME,

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

    GAME_FINISH,

    TURN_PEER_ID,
    TURN_CHECK,
    TURN_GO,

    TIMER_UP,

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
                return "START_GAME ";

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

            case GAME_FINISH:
                return "GAME_FINISH";

            case TURN_GO:
                return "TURN_GO";

            case TURN_CHECK:
                return "TURN_CHECK";

            case TURN_PEER_ID:
                return "TURN_PEER_ID";

            case ROOM_CREATED:
                return "ROOM_CREATED";

            case DUP_ROOM_NAME:
                return  "DUP_ROOM_NAME";

            case MAX_ROOMS_REACHED:
                return  "MAX_ROOMS_REACHED";

            case TIMER_UP:
                return "TIMER_UP";

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

            case "GAME_FINISH":
                return GAME_FINISH;

            case "TURN_PEER_ID":
                return TURN_PEER_ID;

            case "TURN_GO":
                return TURN_GO;

            case "TURN_CHECK":
                return TURN_CHECK;
        

            case "ROOM_CREATED":
                return ROOM_CREATED;

            case "DUP_ROOM_NAME":
                return  DUP_ROOM_NAME;

            case "MAX_ROOMS_REACHED":
                return  MAX_ROOMS_REACHED;

            case "TIMER_UP":
                return TIMER_UP;

            default:
                throw new InvalidMessage(type);
        }
    }
}


