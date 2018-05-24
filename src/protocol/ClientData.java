package protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientData {

    private int clientId;
    private int roomsId;
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

    public void setRoomId(int room) {
        this.roomsId = room;
    }

    public void setClientID(int id) {
        this.clientId = id;
    }
    
    public int getClientID() {
        return this.clientId;
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

    public int getRoomId() {
        return this.roomsId;
    }


}