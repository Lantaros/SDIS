import java.io.IOException;

public class ListenerServer implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				String str = " ";
				parseMsg(str);

			} catch (IOException | NumberFormatException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void parseMsg(String msg) throws NumberFormatException, IOException, InterruptedException {

	}
}