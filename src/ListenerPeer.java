import java.io.IOException;
import java.util.Arrays;

class ListenerPeer implements Runnable {
	private int id;
	private byte[] msg = new byte[1024];
	public ListenerPeer(int id) {
       this.id = id;
   }

	@Override
	public void run() {

		while (true) {
			try {
				Arrays.fill(msg, (byte) 0);
				Client.peer[this.id].getInputStream().read(msg, 0, msg.length);
				System.out.println(new String(msg));
				Client.peer[this.id].setMessage(msg);
				Message message = new Message(new String(msg));
				switch (message.getType()){
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
}