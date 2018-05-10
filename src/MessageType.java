public enum MessageType {
    ROOM_AVAILABLE,
    ROOM_CONNECT,
    ROOM_CREATE,
    PEERS_INFO, PORT;

    @Override
    public String toString() {
        switch (this) {
            case ROOM_AVAILABLE:
                return "ROOM_AVAILABLE";

            case ROOM_CONNECT:
                return "ROOM_CONNECT";

            case ROOM_CREATE:
                return "ROOM_CREATE";
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
            default:
                throw new InvalidMessage(type);
        }
    }
}


