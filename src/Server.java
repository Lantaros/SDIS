import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
<<<<<<< HEAD
import java.io.OutputStream;
=======
import java.util.ArrayList;
import java.util.Queue;

>>>>>>> b0364a594a5ff6634abe6d7eec57645f5befc709

public class Server {
    private SSLServerSocket sslSocket;

    private ArrayList<ClientConnection> clientConnections;

    public static DatagramSocket udpSocket;

    private Socket socket;
    //protected KeyStore keystore;
    private int nextClientID = 1;
    protected static InputStream receiveStream;
    protected static OutputStream sendStream;
    protected static byte[] msg = new byte[1024];

    static Server server = new Server();

    private Server() {

    }

    private Server(int udpPort){

        try {
            udpSocket = new DatagramSocket(udpPort);
        } catch (SocketException e) {
            System.out.println("Failed Creating UDP Socket, port " + udpPort);
            e.printStackTrace();
        }

        clientConnections = new ArrayList<>();





    }

    //java -Djavax.net.ssl.keyStore=../server.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456  Server 3030
    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("At least server port expected.\njava SSLServer <port> <cypher-suite>*");
            System.exit(1);
        }
        Server server =  new Server(Integer.parseInt(args[0]));


        //TCP port Request Listener
        new Thread(new UDPListener()).start();

        //Setting up SSL configuration
            // Require client authentication
        server.sslSocket.setNeedClientAuth(true);

        if(args.length > 1){
            System.err.println("Specified Cipher Suites");

            String[] cypherSuites = new String[args.length-1];
            System.arraycopy(args, 1, cypherSuites, 0, args.length-1);
            server.sslSocket.setEnabledCipherSuites(cypherSuites);
            //throw new NullPointerException();
        }

        try {
            server.socket = server.sslSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            server.socket.setReceiveBufferSize(1024);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            server.receiveStream = server.socket.getInputStream();
            server.sendStream = server.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        System.out.println("############################PROTOCOLS####################################");
        for(String protocol: server.sslSocket.getSupportedProtocols()){
            System.out.println(protocol);
        }
        System.out.println("#########################################################################");
        */

        ListenerClient listClient = new ListenerClient();
        new Thread(listClient).start();




    }

<<<<<<< HEAD
    public void sendMessage(Message message, int clientId) {
        try {
            sendStream.write(message.getBytes());
            System.out.println(message.getBytes());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }   
=======
    public static int getFreeTCPPort(int nextClientID, String clientIP) {


        SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        SSLServerSocket sslSocket = null;
        try {
            sslSocket = (SSLServerSocket) serverSocketFactory.createServerSocket();
            this.clientConnections.add(new ClientConnection(nextClientID, clientIP, sslSocket))
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sslSocket();
    }

    public static int getNextClientID() {
        return ;
>>>>>>> b0364a594a5ff6634abe6d7eec57645f5befc709
    }
}
