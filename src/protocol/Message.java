package protocol;

import game.Room;

import java.util.ArrayList;

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
    private ArrayList<Room> availableRooms;

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
            }else if(type == MessageType.ROOM_CREATE){
                this.clientID = port;
                this.address = address;
            }
            else if(type == MessageType.ROOM_CREATED) {
                this.roomID = nPorts;
                this.address = address;
            } else if(type == MessageType.LETTER_CHECK) {
                this.clientID = port;
				this.bool = address;
			} else if (type == MessageType.WORD_CHECK) {
				this.clientID = port;
				this.bool = address;
			}

			else {
				this.port = port;
            this.address = address;
            }
        } catch (InvalidMessage m) {
            m.printStackTrace();
        }

        
    }

    public Message(MessageType type, String word) {
        this.type = type;

        availableRooms = new ArrayList<>();

        try {
            if(type == MessageType.fromString("LETTER_TO_GUESS"))
                this.letter = word;
            
            else if(type == MessageType.fromString("WORD_TO_GUESS_PEER"))
                this.word = word;

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
            this.type = MessageType.fromString(tokens[0].trim());
        } catch (InvalidMessage invalidMessage) {
            invalidMessage.printStackTrace();
        }

        try {
            switch (MessageType.fromString(tokens[0].trim())) {
                case ROOM_CONNECT:
                    this.type = MessageType.fromString("ROOM_CONNECT");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    this.roomID = Integer.parseInt(tokens[2].trim());
                    break;

                case ROOM_CREATE:
                    this.type = MessageType.ROOM_CREATE;
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    this.address = tokens[2].trim();
                    break;

                case ROOM_CREATED:
                    this.type = MessageType.ROOM_CREATED;
                    this.roomID = Integer.parseInt(tokens[1].trim());
                    this.address = tokens[2].trim();
                    break;

                case GET_ROOMS_AVAILABLE:
                    break;
                    
                case START_GAME:
                	this.type = MessageType.fromString("START_GAME");
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
                        this.word += tokens[i].trim();
                        if(i != tokens.length)
                            this.word += " ";
                    }
                    break;

                case LETTER_TO_GUESS:
                    this.type = MessageType.fromString("LETTER_TO_GUESS");
                    this.letter = tokens[1].trim();
                    break;

                case LETTER_CHECK:
                    this.type = MessageType.fromString("LETTER_CHECK");
                    this.bool = tokens[2].trim();
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case LETTER_GO:
                    this.type = MessageType.fromString("LETTER_GO");
                    break;

                case WORD_TO_GUESS_PEER:
                    this.type = MessageType.fromString("WORD_TO_GUESS_PEER");
                    this.word = "";
                    for(int i = 1; i < tokens.length; i++)  {                      
                        this.word += tokens[i].trim();
                        if(i != tokens.length)
                            this.word += " ";
                    }
                    break;

                case WORD_CHECK:
                    this.type = MessageType.fromString("WORD_CHECK");
                    this.bool = tokens[2].trim();
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case WORD_GO:
                    this.type = MessageType.fromString("WORD_GO");
                    break;

                case READY_TO_START:
                    this.type = MessageType.fromString("READY_TO_START");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case WORD_TO_GUI:
                    this.type = MessageType.fromString("WORD_TO_GUI");
                    this.word = "";
                    for(int i = 1; i < tokens.length; i++)  {                      
                        this.word += tokens[i].trim();
                        if(i != tokens.length)
                            this.word += " ";
                    }
                    break;

                case GAME_FINISH:
                    this.type = MessageType.fromString("GAME_FINISH");

                    if(tokens[1].equals("true"))
                        this.gameOver = true;
                    else
                        this.gameOver = false;

                    break;

                case TURN_PEER_ID:
                    this.type = MessageType.fromString("TURN_PEER_ID");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;

                case TURN_CHECK:
                    this.type = MessageType.fromString("TURN_CHECK");
                    this.word = tokens[1].trim();
                    break;

                case TURN_GO:
                    this.type = MessageType.fromString("TURN_GO");
                    break;

                case TIMER_UP:
                    this.type = MessageType.fromString("TIMER_UP");
                    break;
                case ROOMS_AVAILABLE:
                    availableRooms = new ArrayList<>();
                    for(int i = 1; i < tokens.length; i+=3){
                        Room room = new Room(Integer.parseInt(tokens[i].trim()));
                        room.setName(tokens[i + 1].trim());
                        room.setNClients(Integer.parseInt(tokens[i + 2].trim()));
                        availableRooms.add(room);
                    }
                    break;
                case PASS_OWNERSHIP:
                    this.type = MessageType.fromString("PASS_OWNERSHIP");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;
                case PEER_DISCONNECTED:
                    this.type = MessageType.fromString("PASS_OWNERSHIP");
                    this.clientID = Integer.parseInt(tokens[1].trim());
                    break;
            }

        } catch (InvalidMessage m) {
            System.out.println(m.toString());
            m.printStackTrace();
        }
    }

    public String getRoomName(){
        if(type == MessageType.ROOM_CREATE || type == MessageType.ROOM_CREATED)
            return address;

        return "";
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

            case WORD_TO_GUESS_PEER:
                message += " " + word;
                break;

            case WORD_CHECK:
                message += " " + clientID + " " + bool;
                break;

            case WORD_GO:
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
                break;

            case ROOM_CREATE:
                message += " " + clientID + " " + address;
                break;

            case ROOM_CREATED:
                message += " " + roomID;
                message += " " + address;
                break;

            case TIMER_UP:
                message += " " + "go";
                break;
            case ROOMS_AVAILABLE:
                message += " " + word;
//                for (Room availableRoom : availableRooms) {
//                    message += " " + availableRoom.getRoomId()
//                            + " " + availableRoom.getName()
//                            + " " + availableRoom.getNClients();
//                }
            break;
            case PASS_OWNERSHIP:
                message += " " + clientID;
                break;
                
            case PEER_DISCONNECTED:
                message += " " + clientID;
                break;

            default:
    			break;
        }

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

    public ArrayList<Room> getAvailableRooms() {
        return  availableRooms;
    }
}