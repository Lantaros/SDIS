 import sun.security.ssl.SSLSocketImpl;

 import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.net.SocketException;

 public class Client {

    private int port;
    private SSLSocket sslSocket;
    private InputStream receiveStream;
    private OutputStream sendStream;

     Client(String host, int port){
        SSLSocketFactory serverSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try {
            sslSocket = (SSLSocket) serverSocketFactory.createSocket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //-Djavax.net.debug=all -> Debug flag, a TON of information
    //java  -Djavax.net.ssl.keyStore=../client.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 Client 127.0.0.1 3030
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
            System.out.println("############################Cypher####################################");
            for(String cypher: cypherSuites){
                System.out.println(cypher);
            }
            System.out.println("#########################################################################");


            client.sslSocket.setEnabledCipherSuites(cypherSuites);
        }


        System.out.println("############################Supported Cyphers####################################");
        for(String protocol: client.sslSocket.getSupportedCipherSuites()){
            System.out.println(protocol);
        }
        System.out.println("#########################################################################");




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

        try {
            client.sendStream.write("Rumo ao Penta".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
