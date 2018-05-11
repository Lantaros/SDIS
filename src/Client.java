import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.net.ServerSocket;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;

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

    //To Thread ServerChannel use
    protected static boolean toReceiveServer = false;
    protected static byte[] msgReceivedServer = new byte[1024];

    protected static ClientData[] peer = new ClientData[100];

    private static String[] cypherSuites;


    
    private Client(String host, int port){
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            sslSocket = (SSLSocket) serverSocketFactory.createSocket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.clientID = clientID;

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

        try {
            Thread.sleep(1000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        client.connectRoom("Sala 1");


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

    }

    public static void requestPort(int nPorts) {
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        String msg = Integer.toString(clientID);

        int port = Client.nextFreePort(49152, 65535);
        msg += " ";
        msg += port;
        Message message = new Message(MessageType.PORT_TO_SEND, port, Client.sslSocket.getLocalAddress().getHostAddress());
        
        try {
            sendStream.write(message.getBytes());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //making the connection only between peers!!!
   /* public void connectToPeer(String address, int port, int nPlayer) {
        
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
    }*/

    public static int nextFreePort(int from, int to) {
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

    private static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
