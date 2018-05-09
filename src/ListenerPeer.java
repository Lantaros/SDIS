import java.io.IOException;

class ListenerPeer implements Runnable {

	private int peerID;
	private byte[] message = new byte[1024];

	ListenerPeer(int peerID) {
		this.peerID = peerID;
	}

	@Override
	public void run() {

		while (true) {
			try {
				Client.receiveStreamPeer[peerID].read
					(message, 0, message.length);
				System.out.println(new String(message));
				handleMsg();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void handleMsg() throws NumberFormatException {
		//TODO: merdas com a mensagem
	}
}