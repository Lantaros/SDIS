import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Client {


     static private int clientID;
     //conection with the server
    //protected int port;

    private static SSLSocket serverConnection;
    protected static InputStream receiveStream;
    protected static OutputStream sendStream;

    //To Thread SendServer use
    protected static boolean toSendServer = false;
    protected static byte[] msgSendServer = new byte[1024];

    //To Thread ServerChannel use
    protected static boolean toReceiveServer = false;
    protected static byte[] msgReceivedServer = new byte[1024];

    //Connection within peers
    //protected int[] portPeer = new int[4];
    private SSLSocket[] sslSocketPeer = new SSLSocket[4];
    private InputStream[] receiveStreamPeer;
    private OutputStream[] sendStreamPeer;

    private static String[] cypherSuites;


    
    private Client(String host, int port){
        DatagramSocket udpSocket;

        try {
            udpSocket = new DatagramSocket(port, InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();

        Message msg = new Message(MessageType.TCP_ID_REQ);

        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLSocket sslSocket = serverSocketFactory.createServerSocket()

        try {
            sslSocket = (SSLSocket) socketFactory.createSocket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }



        //-Djavax.net.debug=all -> Debug flag, a TON of information
    //java  -Djavax.net.ssl.keyStore=../client.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 Client 127.0.0.1 3030 "qualquer coisa"
    public static void main(String[] args){
        Client client = new Client(args[0], Integer.parseInt(args[1]));


        int nOperands;

        switch (args[2]){
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
        if(args.length > 3 + nOperands){
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
/*
        SendServer sendServer = new SendServer();
        new Thread(sendServer).start();
        */
        client.connectRoom("Sala 1");

        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        int port = client.nextFreePort(49152, 65535);
        System.out.println(port);
        try {
            client.sslSocketPeer[0] = (SSLSocket) serverSocketFactory.createSocket(args[0], port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println(client.sslSocketPeer[0].getLocalPort());


    }

    public void connectRoom(String roomName) {
        //roomID -> roomID, neste caso 1

        //send to server to connect to room
        Message connectRequest = new Message(MessageType.ROOM_CONNECT, this.clientID, 1);

        try {
            sendStream.write(connectRequest.getBytes());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
       

        //wait to response
        while(!toReceiveServer){

        }
        toReceiveServer = false;
    }

    //making the connection only between peers!!!
    public void connectToPeer(String address, int port, int nPlayer) {
        
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            this.sslSocketPeer[nPlayer] = (SSLSocket) serverSocketFactory.createSocket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.sslSocketPeer[nPlayer].setReceiveBufferSize(1024);
            this.sslSocketPeer[nPlayer].setSendBufferSize(1024);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            this.receiveStreamPeer[nPlayer] = this.sslSocketPeer[nPlayer].getInputStream();
            this.sendStreamPeer[nPlayer] = this.sslSocketPeer[nPlayer].getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.sslSocketPeer[nPlayer].startHandshake();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int nextFreePort(int from, int to) {
        Random rand = new Random();
        int port = rand.nextInt(to-from) + from;
        while (true) {
            if (isLocalPortFree(port)) {
                return port;
            } else {
                port = ThreadLocalRandom.current().nextInt(from, to);
            }
        }
    }

    private boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
