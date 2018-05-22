package protocol;

public class Message {
    private MessageType type;
    private int clientID;
    private int roomID;
    private int nPorts;
    private int port;
    private String address;
    private String word;

    public static String CR = "\r";
    public static String LF = "\n";
    public static String CRLF = CR + LF;
    public static String TAB = "\t";

    public Message(MessageType type, int clientID, int roomID) {
        this.type = type;
        this.clientID = clientID;
        this.roomID = roomID;
    }

    public Message(MessageType type, int port, String address) {
        this.type = type;
        this.port = port;
        this.address = address;
    }

    public Message(MessageType type, String word) {
        this.type = type;
        this.word = word;
    }

    public Message(MessageType type, int nPorts) {
        this.type = type;

        try {
            if (type == MessageType.fromString("OWN_CLIENT_ID"))
                this.clientID = nPorts;
            else if (type == MessageType.fromString("SEND_PORTS"))
                this.nPorts = nPorts;
            else if (type == MessageType.fromString("PEER_INFO"))
                this.clientID = nPorts;
        } catch (InvalidMessage m) {
            System.out.println(m.toString());
            m.printStackTrace();
        }

    }

    public Message(String message) {
        String[] tokens = message.split(" ");
        try {
            switch (MessageType.fromString(tokens[0])) {
                case ROOM_CONNECT:
                    this.type = MessageType.fromString("ROOM_CONNECT");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    this.roomID = Integer.parseInt(tokens[2].trim());
                    break;

                case ROOM_CREATE:
                    break;

                case ROOM_AVAILABLE:
                    break;

                case SEND_PORTS:
                    this.type = MessageType.fromString("SEND_PORTS");
                    this.nPorts = Integer.parseInt(tokens[1].trim());
                    break;

                case REQUEST_PORTS:
                    this.type = MessageType.fromString("REQUEST_PORTS");
                    this.nPorts = Integer.parseInt(tokens[1].trim());
                    break;

                case OWN_CLIENT_ID:
                    this.type = MessageType.fromString("OWN_CLIENT_ID");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case PORT_TO_SEND:
                    this.type = MessageType.fromString("PORT_TO_SEND");
                    this.port = Integer.parseInt(tokens[1].trim());
                    this.address = tokens[2].trim();
                    break;

                case PORT_TO_CONNECT:
                    this.type = MessageType.fromString("PORT_TO_CONNECT");
                    this.port = Integer.parseInt(tokens[1].trim());
                    this.address = tokens[2].trim();
                    break;

                case PEER_INFO:
                    this.type = MessageType.fromString("PEER_INFO");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case WORD_TO_GUESS:
                    this.type = MessageType.fromString("WORD_TO_GUESS");
                    this.word = "";
                    for(int i = 1; i < tokens.length; i++)
                        this.word += tokens[i];
                    break;

            }

        } catch (InvalidMessage m) {
            System.out.println(m.toString());
            m.printStackTrace();
        }
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }

    @Override
    public String toString() {
        String message = type.toString();

        switch (type) {
            case ROOM_CONNECT:
                message += " " + clientID + " " + roomID;
                break;

            case SEND_PORTS:
                message += " " + nPorts;
                break;

            case OWN_CLIENT_ID:
                message += " " + clientID;
                break;

            case PORT_TO_SEND:
                message += " " + port + " " + address;
                break;

            case PORT_TO_CONNECT:
                message += " " + port + " " + address;
                break;

            case PEER_INFO:
                message += " " + clientID;
                break;

            case WORD_TO_GUESS:
                message += " " + word;
                break;
        }

        //message += CRLF;
        return message;
    }

    public MessageType getType() {
        return type;
    }

    public int getNPorts() {
        return nPorts;
    }

    public int getClientID() {
        return clientID;
    }

    public int getPort() {
        return this.port;
    }

    public int getRoomId() {
        return this.roomID;
    }

    public String getAddress() {
        return this.address;
    }

    public void setClientID(int id) {
        this.clientID = id;
    }

    public String getWord() {
        return this.word;
    }
}