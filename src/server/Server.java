package server;

import client.Client;
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

    private static int MAX_NROOMS = 25;
    private static int MAX_NCLIENTS = 100;
    protected static SSLServerSocket sslSocket;
    private Socket socket;
    //protected KeyStore keystore;

    static Server server = new Server();

    protected static ClientData[] client = new ClientData[MAX_NCLIENTS];

    private static int nRooms = 0;
    protected static Room[] rooms = new Room[MAX_NROOMS];

    private Server() {

    }

    private Server(int port) {
        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try {
            sslSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sslSocket.setEnabledCipherSuites(
            new String[]{"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                    "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384",
                    "TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384",
                    "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384"}
        );

        rooms[1] = new Room(1);

    }

    //java -Djavax.net.ssl.keyStore=../server.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456  server.Server 3030
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Server port expected.\njava Server <port>");
            System.exit(1);
        }
        Server server = new Server(Integer.parseInt(args[0]));

        ListenerConnection listConnection = new ListenerConnection();
        new Thread(listConnection).start();


    }

    /**
     * Creates a new room and joining
     * @param roomName name of the new room
     * @param clientID clientID from the request maker
     * @return roomID if successful
     *  -1 if there is no space for a new room
     * -2 if the name is a duplicated one
     */
    public static int createRoom(String roomName, int clientID){
        if(Server.nRooms + 1  == Server.MAX_NROOMS)
            return -1;

        for (int i = 0; i < nRooms; i++){
            System.out.println("Room name - '" + rooms[i].getName() + "'");
            if(rooms[i].getName().equals(roomName))
                return -2;
        }

        int roomID = Server.nRooms;
        Server.rooms[roomID] = new Room(roomID);
        Server.rooms[roomID].addClientId(clientID);

        Server.nRooms ++;

        return roomID;
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
        Server.rooms[roomsId].addClientId(id);
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
