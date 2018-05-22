package client;

import game.Hangman;
import game.Room;
import protocol.Message;
import protocol.MessageType;
import protocol.ClientData;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


class Client {

    protected static int clientID;
    //conection with the server
    //protected int port;
    protected static SSLSocket sslSocket;
    protected static InputStream receiveStream;
    protected static OutputStream sendStream;

    //To Thread SendServer use
    protected static boolean toSendServer = false;
    protected static byte[] msgSendServer = new byte[1024];

    //To Thread client.ServerChannel use
    protected static boolean toReceiveServer = false;
    protected static byte[] msgReceivedServer = new byte[1024];

    protected static Room[] rooms = new Room[3];
    protected static ClientData[] peer = new ClientData[100];
    protected static int countPeer = 0;

    private static String[] cypherSuites;

    private Client(String host, int port) {
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            sslSocket = (SSLSocket) serverSocketFactory.createSocket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //-Djavax.net.debug=all -> Debug flag, a TON of information
    //java  -Djavax.net.ssl.keyStore=../client.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 client.Client 127.0.0.1 3030 "qualquer coisa"
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Wrong number of arguments\nExpected at least client.Client <SERVER_HOST> <PORT>");
            System.exit(1);
        }

        Client client = new Client(args[0], Integer.parseInt(args[1]));


        int nOperands;

        switch (args[2]) {
            case "REGISTER":
                nOperands = 2;
                break;
            case "LOOKUP":
                nOperands = 1;
                break;
            default:
                nOperands = 0;
                break;
        }

        //Setting up SSL configuration
        if (args.length > 3 + nOperands) {
            int nCyphers = 3 + nOperands;
            System.out.println("Specified Cipher Suites");
            String[] cypherSuites = new String[args.length - nCyphers];
            System.arraycopy(args, 3 + nOperands, cypherSuites, 0, cypherSuites.length);

            System.out.println("Args Lenght: " + args.length);
            /*
            System.out.println("############################Cypher####################################");
            for(String cypher: cypherSuites){
                System.out.println(cypher);
            }
            System.out.println("#########################################################################");

            */
            client.sslSocket.setEnabledCipherSuites(cypherSuites);
        }

/*
        System.out.println("############################Supported Cyphers####################################");
        for(String protocol: client.sslSocket.getSupportedCipherSuites()){
            System.out.println(protocol);
        }
        System.out.println("#########################################################################");

*/


        try {
            client.sslSocket.setReceiveBufferSize(1024);
            client.sslSocket.setSendBufferSize(1024);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            client.receiveStream = client.sslSocket.getInputStream();
            client.sendStream = client.sslSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            client.sslSocket.startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ServerChannel listServer = new ServerChannel();
        new Thread(listServer).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        //TODO::create the looby properly
        Client.connectRoom("Sala 1");

    }

    public static void connectRoom(String roomsName) {
        //roomsID -> roomsID, neste caso 1

        //send to server to connect to rooms
        Message connectRequest = new Message(MessageType.ROOM_CONNECT, clientID, 1);

        System.out.println(connectRequest);

        try {
            sendStream.write(connectRequest.getBytes());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        Client.rooms[1] = new Room(1);
        Hangman game = new Hangman(1);
        rooms[1].addGame(game);
        System.out.println(Client.rooms[1].getRoomId());
    }

    public static void requestPort(int nPorts) {
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        String msg = Integer.toString(clientID);
        String address = "";
        int port = Client.nextFreePort(49152, 65535);
        msg += " ";
        msg += port;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) networkInterfaces
                        .nextElement();
                Enumeration<InetAddress> nias = ni.getInetAddresses();
                while (nias.hasMoreElements()) {
                    InetAddress ia = (InetAddress) nias.nextElement();
                    if (!ia.isLinkLocalAddress()
                            && !ia.isLoopbackAddress()
                            && ia instanceof Inet4Address) {
                        address = ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println("Local IP " + client.Client.sslSocket.getLocalAddress().getHostAddress());
        try {
            Message message = new Message(MessageType.PORT_TO_SEND, port, address);
            sendStream.write(message.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }


        PeerConnection listConnection = new PeerConnection(nPorts, port);
        new Thread(listConnection).start();
    }

    public static int nextFreePort(int from, int to) {
        Random rand = new Random();
        int port = rand.nextInt(to - from) + from;
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            } else {
                port = ThreadLocalRandom.current().nextInt(from, to);
            }
        }
    }

    private static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void saveClient(Socket socket, InputStream receiveStream,
                                  OutputStream sendStream) {
        int id = countPeer;
        countPeer++;

        peer[id] = new ClientData(id);
        peer[id].setSocket(socket);
        peer[id].setOutputStream(sendStream);
        peer[id].setInputStream(receiveStream);

        System.out.println("*********DATA********");
        System.out.println(socket.getLocalPort());
        System.out.println(socket.getLocalAddress().getHostAddress());
        System.out.println(socket.getPort());
        System.out.println(socket.getRemoteSocketAddress());

        rooms[1].setClientId(id);

        ListenerPeer listPeer = new ListenerPeer(id);
        new Thread(listPeer).start();
    }

    public static void connectPeer(int port, String address) {

        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocketP;

        try {
            System.out.println(address);
            System.out.println(port);
            sslSocketP = (SSLSocket) serverSocketFactory.createSocket(address, port);

            peer[countPeer] = new ClientData(countPeer);

            try {
                sslSocketP.setReceiveBufferSize(1024);
                sslSocketP.setSendBufferSize(1024);
            } catch (SocketException e) {
                e.printStackTrace();
            }

            try {
                peer[countPeer].setInputStream(sslSocketP.getInputStream());
                peer[countPeer].setOutputStream(sslSocketP.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                sslSocketP.startHandshake();
            } catch (IOException e) {
                e.printStackTrace();
            }

            rooms[1].setClientId(countPeer);

            ListenerPeer listPeer = new ListenerPeer(countPeer);
            new Thread(listPeer).start();

            countPeer++;

        } catch (IOException e) {
            e.printStackTrace();
        }

        Message message = new Message(MessageType.PEER_INFO, Client.clientID);
        try {
            peer[countPeer - 1].getOutputStream().write(message.getBytes());
            System.out.println(message.toString());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void addPeer(int clientID, int generalID) {
        peer[clientID].setClientID(generalID);
    }
}