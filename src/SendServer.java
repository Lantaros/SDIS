import java.io.IOException;

class SendServer implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				if(Client.toSendServer){
					Client.toSendServer = false;
					sendMsg(Client.msgSendServer);
					System.out.println(new String(Client.msgSendServer));
				}
			} catch (IOException | NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendMsg(byte[] msg) throws NumberFormatException, IOException {
		Client.client.sendStream.write(msg);
	}
	
}