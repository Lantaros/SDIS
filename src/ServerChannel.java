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
					case PEERS_INFO:
					break;
					case SEND_PORTS:
						Client.requestPorts(message.nPorts);
					break;
					case OWN_CLIENT_ID:
						Client.clientID = message.getClientID();
					break;
				}

				Client.toReceiveServer = true;

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
}