import java.io.IOException;

class ServerChannel implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				Client.receiveStream.read(Client.msgReceivedServer, 0, Client.msgReceivedServer.length);
				System.out.println(new String(Client.msgReceivedServer));

				Message message = new Message(new String(Client.msgReceivedServer));

				switch (message.getType()){
					case PEERS_INFO:
					break;

				}

				Client.toReceiveServer = true;
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

//	private void parseMsg() throws NumberFormatException {
//		Message message =
//		switch (){
//
//		}
//	}
}