import java.io.IOException;

class ListenerClient implements Runnable {
	@Override
	public void run() {

		while (true) {
			try {
				Server.server.receiveStream.read(Server.msg, 0, Server.msg.length);
				System.out.println(new String(Server.msg));
				Message message = new Message(new String(Server.msg));
				switch (message.getType()){
					case ROOM_CONNECT:
						Message messageSend = new Message(MessageType.SEND_PORTS, 1);
						Server.server.sendMessage(messageSend, 1);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}

	private void parseMsg() throws NumberFormatException {

	}
}