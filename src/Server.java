import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Queue;


public class Server {
    private SSLServerSocket sslSocket;

    public static ArrayList<ClientConnection> clientConnections;

    public static DatagramSocket udpSocket;
    private static String[] cypherSuites;

    //protected KeyStore keystore;
    private static int nextClientID = 1;
    protected static InputStream receiveStream;

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

            cypherSuites = new String[args.length-1];
            System.arraycopy(args, 1, cypherSuites, 0, args.length-1);
        }

//        try {
//            server.socket = server.sslSocket.accept();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            server.socket.setReceiveBufferSize(1024);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            server.receiveStream = server.socket.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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

    public static SSLSocket createSSLSocket() {
        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = null;
        try {
            sslSocket = (SSLSocket) socketFactory.createSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }

        configureSSLSocket(sslSocket);

        return sslSocket;
    }

    private static void configureSSLSocket(SSLSocket sslSocket) {
        if(Server.cypherSuites != null)
            sslSocket.setEnabledCipherSuites(Server.cypherSuites);

        try {
            sslSocket.setReceiveBufferSize(1024);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static int getNextClientID() {
        return nextClientID++;

    }
}
