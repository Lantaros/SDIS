import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestClient {

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
        if(args.length < 3){
            System.out.println("Wrong number of arguments.\n" +
                    "Expected:\"<peer_ap> <sub_protocol> <opnd_1> <opnd_2>\"");
            System.exit(1);
        }

        //Splits initiator Peer's AP ( IP and Port )
        Pattern p = Pattern.compile("(?:([^:]+)?:)?(\\d+)");
        Matcher m = p.matcher(args[0]);
        m.matches();



        //Port 8086 - TestClient -> Peer port
        TestClient client = new TestClient(args[0]);
         try {
            Registry registry = LocateRegistry.getRegistry();
            Services stub = (Services) registry.lookup(args[0]);
            if (stub.backup(args[2], Integer.parseInt(args[3])))
                System.out.println("Peer" + args[0] + ": backup file" + args[3] + "succefuly");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }

    private TestClient(String peerIP){

    }


}
