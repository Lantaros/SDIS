package protocol;

public class Message {
    private MessageType type;
    private int clientID;
    private int roomID;
    private int nPorts;
    private int port;
    private String address;
    private String word;
    private String letter;
    private String bool;
    private boolean gameOver;

    public static String CR = "\r";
    public static String LF = "\n";
    public static String CRLF = CR + LF;
    public static String TAB = "\t";

    public Message(MessageType type, int clientID, int roomID) {
        this.type = type;
        this.clientID = clientID;
        this.roomID = roomID;
    }

    public Message(MessageType type) {
        this.type = type;
    }

    public Message(MessageType type, boolean bool) {
        this.type = type;
        this.gameOver = bool;
    }

    public Message(MessageType type, int port, String address) {
        this.type = type;
        try {
            if(type == MessageType.fromString("LETTER_TO_GUESS")){
                this.bool = address;
                this.clientID = port;
            }else {
                this.port = port;
            this.address = address;
            }
        } catch (InvalidMessage m) {
            m.printStackTrace();
        }

        
    }

    public Message(MessageType type, String word) {
        this.type = type;
        try {
            if(type == MessageType.fromString("LETTER_TO_GUESS"))
                this.letter = word;
            
            else if(type == MessageType.fromString("WORD_TO_GUI"))
                this.word = word;

            else if(type == MessageType.fromString("TURN_CHECK"))
                this.word = word;

            else {
                this.word = word;
            }
        } catch (InvalidMessage m) {
            m.printStackTrace();
        }
        
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
            else if (type == MessageType.fromString("TURN_PEER_ID"))
                this.clientID = nPorts;
            else
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
                    for(int i = 1; i < tokens.length; i++)  {                      
                        this.word += tokens[i];
                        if(i != tokens.length)
                            this.word += " ";
                    }
                    break;

                case LETTER_TO_GUESS:
                    this.type = MessageType.fromString("LETTER_TO_GUESS");
                    this.letter = tokens[1];
                    break;

                case LETTER_CHECK:
                    this.type = MessageType.fromString("LETTER_CHECK");
                    this.bool = tokens[2];
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case LETTER_GO:
                    this.type = MessageType.fromString("LETTER_GO");
                    break;

                case READY_TO_START:
                    this.type = MessageType.fromString("READY_TO_START");
                    break;

                case WORD_TO_GUI:
                    this.type = MessageType.fromString("WORD_TO_GUI");
                    this.word = "";
                    for(int i = 1; i < tokens.length; i++)  {                      
                        this.word += tokens[i];
                        if(i != tokens.length)
                            this.word += " ";
                    }
                    break;

                case GAME_FINISH:
                    this.type = MessageType.fromString("GAME_FINISH");
                    if(tokens[1] == "true")
                        this.gameOver = true;
                    else if(tokens[1] == "false")  
                        this.gameOver = false;
                    break;

                case TURN_PEER_ID:
                    this.type = MessageType.fromString("TURN_PEER_ID");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case TURN_CHECK:
                    this.type = MessageType.fromString("TURN_CHECK");
                    this.word = tokens[1];
                    break;

                case TURN_GO:
                    this.type = MessageType.fromString("TURN_GO");
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

            case LETTER_TO_GUESS:
                message += " " + letter;
                break;

            case LETTER_CHECK:
                message += " " + clientID + " " + bool;
                break;

            case LETTER_GO:
                message += " " + "go";
                break;

            case READY_TO_START:
            	message += " " + clientID;
                break;

            case WORD_TO_GUI:
                message += " " + word;
                break;

            case GAME_FINISH:
                message += " " + gameOver;
                break;

            case TURN_PEER_ID:
                message += " " + clientID;
                break;

            case TURN_CHECK:
                message += " " + word;
                break;

            case TURN_GO:
                message += " " + "go";
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

    public String getLetter() {
        return this.letter;
    }

    public String getBool() {
        return this.bool;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }
}