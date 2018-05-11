import java.io.IOException;
import java.util.Arrays;

class ServerChannel implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				Arrays.fill(Client.msgReceivedServer, (byte) 0);
				Client.receiveStream.read(Client.msgReceivedServer, 0, Client.msgReceivedServer.length);
				System.out.println(new String(Client.msgReceivedServer));

				Message message = new Message(new String(Client.msgReceivedServer));
				switch (message.getType()){
					case SEND_PORTS:
						if(message.nPorts == 0) {
							break;
						}
						Client.requestPort(message.nPorts);
					break;
					case OWN_CLIENT_ID:
						Client.clientID = message.getClientID();
					break;
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
}