import java.io.IOException;

class ListenerServer implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				Client.receiveStream.read(Client.msgReceivedServer, 0, Client.msgReceivedServer.length);
				Client.toReceiveServer = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(new String(Client.msgReceivedServer));
		}
	}

	private void parseMsg() throws NumberFormatException {

	}
}