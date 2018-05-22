package server;

import game.Room;
import protocol.ClientData;
import protocol.Message;
import protocol.MessageType;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Server {
    protected static SSLServerSocket sslSocket;
    private Socket socket;
    //protected KeyStore keystore;

    static Server server = new Server();

    protected static ClientData[] client = new ClientData[100];
    protected static Room[] rooms = new Room[25];

    private Server() {

    }

    private Server(int port) {
        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try {
            sslSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        rooms[1] = new Room(1);

    }

    //java -Djavax.net.ssl.keyStore=../server.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456  server.Server 3030
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("At least server port expected.\njava SSLServer <port> <cypher-suite>*");
            System.exit(1);
        }
        Server server = new Server(Integer.parseInt(args[0]));

        ListenerConnection listConnection = new ListenerConnection();
        new Thread(listConnection).start();


    }

    public static void saveClient(Socket socket, InputStream receiveStream,
                                  OutputStream sendStream, int id) {
        client[id] = new ClientData(id);
        client[id].setSocket(socket);
        client[id].setOutputStream(sendStream);
        client[id].setInputStream(receiveStream);

        System.out.println("*********DATA********");
        System.out.println(socket.getLocalPort());
        System.out.println(socket.getLocalAddress().getHostAddress());
        System.out.println(socket.getPort());
        System.out.println(socket.getRemoteSocketAddress());

        Message messageSend = new Message(MessageType.OWN_CLIENT_ID, id);
        Server.server.sendMessage(messageSend, id);

        ListenerClient listClient = new ListenerClient(id);
        new Thread(listClient).start();
    }

    public static void sendPortToClients(int port, String address, int id) {
        int roomsId = Server.client[id].getRoomId();
        int n = rooms[roomsId].getNClients();
        int[] clients = rooms[roomsId].getClients();

        System.out.println(Server.client[id].getSocket().getInetAddress().getHostAddress());
        Message messageSend = new Message(MessageType.PORT_TO_CONNECT, port, address);
        for (int i = 1; i <= n; i++) {
            int clientId = clients[i];
            try {
                Server.client[clientId].getOutputStream().write(messageSend.getBytes());
                System.out.println(messageSend);
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Server.rooms[roomsId].setClientId(id);
    }

    public void sendMessage(Message message, int clientId) {
        try {
            client[clientId].getOutputStream().write(message.getBytes());
            System.out.println(message);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
