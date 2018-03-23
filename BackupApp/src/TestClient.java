import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClient {
    Socket tcpSocket;

    /**
     * <peer_ap> ip:port
     *Is the peer's access point <ip:port>. This depends on the implementation. (See section 4)
     *<operation>
     *Is the operation the peer of the backup servic
     *<opnd_1>
     *Is either the path name of the file to backup/restore/delete, for the respective 3 subprotocols, or, in the case of RECLAIM the maximum amount of disk space (in KByte) that the service can use to store the chunks. In the latter case, the peer should execute the RECLAIM protocol, upon deletion of any chunk. The STATE operation takes no operands.
     *<opnd_2>
     *This operand is an integer that specifies the desired replication degree and applies only to the backup protocol (or its enhancement)
     *
     * @param args command line arguments
     */
    public static void main(String[] args){
        if(args.length != 4){
            System.out.println("Wrong number of arguments.\n" +
                    "Expected:\"<peer_ap> <sub_protocol> <opnd_1> <opnd_2>\"");
            System.exit(1);
        }

        Pattern p = Pattern.compile("([^:]*)?:?(\\d)");
        Matcher m = p.matcher(args[0]);
        m.matches();

        for (int i = 1; i < m.groupCount(); i++) {
            System.out.println(m.group(i));
        }
        System.out.println( + "d" + m.groupCount());

        //Port 8086 - TestClient -> Peer port
        TestClient client = new TestClient(args[0]);

        /* try {
            Registry registry = LocateRegistry.getRegistry(host);
            Hello stub = (Hello) registry.lookup("Hello");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }*/

    }

    TestClient(String peerIP){
        try {
            tcpSocket = new Socket(peerIP, 8086);
        } catch (UnknownHostException e) {
            System.out.println("Couldn't connet to specified peer IP");
        }
        catch (IOException e){
            System.out.println("Failed to create the TCP Socket");
        }
    }


}
