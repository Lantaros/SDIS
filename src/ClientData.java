import java.io.InputStream;
import java.net.Socket;
import java.io.OutputStream;

class ClientData {
    
	private int clientId;
    private InputStream receiveStream;
    private OutputStream sendStream;
    private Socket socket;
    private byte[] msg = new byte[1024];

	public ClientData(int id_client) {
		this.clientId = id_client;
    }

    public void setInputStream(InputStream s) {
        this.receiveStream = s;
    }

    public void setOutputStream(OutputStream s) {
        this.sendStream = s;
    }

    public void setSocket(Socket s) {
        this.socket = s;
    }

    public void setMessage(byte[] msg) {
        this.msg = msg;
    }

    public InputStream getInputStream() {
        return this.receiveStream;
    }

    public OutputStream getOutputStream() {
        return this.sendStream;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public byte[] getMessage() {
        return this.msg;
    }
}