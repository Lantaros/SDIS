import java.util.Scanner;

class Message {
	MessageType type;
	int clientID;
	int roomID;
	int nPorts;


	public static String CR = "\r";
	public static String LF = "\n";
	public static String CRLF = CR + LF;
	public static String TAB = "\t";

	public Message(MessageType type, int clientID, int roomID) {
		this.type = type;
		this.clientID = clientID;
		this.roomID = roomID;		
	}

	public Message(MessageType type, int nPorts) {
		this.type = type;
		this.nPorts = nPorts;	
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
			}
			
		}catch (InvalidMessage m){
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

		switch (type){
			case ROOM_CONNECT:
				message += " " + clientID + " " + roomID;
			break;
			case SEND_PORTS:
				message += " " + nPorts;
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
}