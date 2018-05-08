import java.io.IOException;

class SendServer implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				if(Client.toSendServer){
					Client.toSendServer = false;
					sendMsg(Client.msgSendServer);
				}
			} catch (IOException | NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendMsg(String msg) throws NumberFormatException, IOException {
		Client.client.sendStream.write(Message.convertToBytes(msg));
	}
	
}