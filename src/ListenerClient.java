import java.io.IOException;

class ListenerClient implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				Server.server.receiveStream.read(Server.msg, 0, Server.msg.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(new String(Server.msg));
		}
	}

	private void parseMsg() throws NumberFormatException {

	}
}