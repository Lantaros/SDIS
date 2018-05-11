public enum MessageType {
    ROOM_AVAILABLE,
    ROOM_CONNECT,
    ROOM_CREATE,
    PEERS_INFO,
    REQUEST_PORTS,
    SEND_PORTS,
    OWN_CLIENT_ID;

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
            default:
                throw new InvalidMessage(type);
        }
    }
}


