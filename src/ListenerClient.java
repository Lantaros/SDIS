import java.io.IOException;
import java.util.Arrays;

class ListenerClient implements Runnable {
	private int id;
	private byte[] msg = new byte[1024];
	public ListenerClient(int id) {
       this.id = id;
   }

	@Override
	public void run() {

		while (true) {
			try {
				Arrays.fill(msg, (byte) 0);
				Server.client[this.id].getInputStream().read(msg, 0, msg.length);
				System.out.println(new String(msg));
				Server.client[this.id].setMessage(msg);
				Message message = new Message(new String(msg));
				switch (message.getType()){
					case ROOM_CONNECT:
						Message messageSend = new Message(MessageType.SEND_PORTS, message.getClientID());
						Server.server.sendMessage(messageSend, this.id);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
}