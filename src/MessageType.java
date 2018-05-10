public enum MessageType {
    ROOM_AVAILABLE,
    ROOM_CONNECT,
    ROOM_CREATE,
    PEERS_INFO,
    TCP_ID_REQ,
    CLIENT_ID;

    @Override
    public String toString() {
        switch (this) {
            case ROOM_AVAILABLE:
                return "ROOM_AVAILABLE";

            case ROOM_CONNECT:
                return "ROOM_CONNECT";

            case ROOM_CREATE:
                return "ROOM_CREATE";
            case TCP_ID_REQ:
                return "TCP_ID_REQ";
            case CLIENT_ID:
                return "CLIENT_ID";
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

            case "TCP_ID_REQ":
                return TCP_ID_REQ;

            case "CLIENT_ID":
                return CLIENT_ID;

            default:
                throw new InvalidMessage(type);
        }
    }
}


