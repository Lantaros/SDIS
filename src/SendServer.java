import java.io.IOException;

public class SendServer implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				if(Client.toSendServer){
					Client.toSendServer = false;
					sendMsg(Client.msgSendServer);
				}
			} catch (IOException | NumberFormatException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMsg(String msg) throws NumberFormatException, IOException, InterruptedException {
		Client.client.sendStream.write(Message.convertToBytes(msg));
	}
	
}