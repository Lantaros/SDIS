public enum MessageType {
    ROOM_AVAILABLE,
    ROOM_CONNECT,
    ROOM_CREATE,
    PEERS_INFO,
    TCP_ID_REQ,
    REQUEST_PORTS,
    SEND_PORTS,
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
            case REQUEST_PORTS:
                return "REQUEST_PORTS";
            case SEND_PORTS:
                return "SEND_PORTS";
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

            case "REQUEST_PORTS":
                return REQUEST_PORTS;
            case "SEND_PORTS":
                return SEND_PORTS;
            default:
                throw new InvalidMessage(type);
        }
    }
}


