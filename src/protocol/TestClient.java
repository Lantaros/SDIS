package protocol;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestClient {

    /**
     * <peer_ap> [resgistryIP:port/]rmi object name
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

        /*//Parse peer Access Point - registry IP and Port + rmi name
        Pattern p = Pattern.compile("(?:(?:([^:]+)?:?(\\d+)\\/)?(\\w+))");
        Matcher m = p.matcher(args[0]);
        m.matches();



         try {
             Registry registry;
             Services stub;
             if(m.groupCount() == 1) {
                 registry = LocateRegistry.getRegistry();
                 stub = (Services) registry.lookup(m.group(1));
             }
             else if (m.groupCount() == 2) {
                 registry = LocateRegistry.getRegistry(Integer.parseInt(m.group(1)));
                 stub = (Services) registry.lookup(m.group(2));

             }
             else {
                 registry = LocateRegistry.getRegistry(m.group(1), Integer.parseInt(m.group(2)));
                 stub = (Services) registry.lookup(m.group(3));
             }*/

        try {
            Registry registry = LocateRegistry.getRegistry();
            Services stub = (Services) registry.lookup(args[0]);


                switch (args[1]){
                    case "BACKUP":
                        if (stub.backup(args[2], Integer.parseInt(args[3])))
                            System.out.println("Peer" + args[0] + ": file " + args[3] + " BACKUP RMI Message sent successfully");
                        else
                            System.out.println("Error sending BACKUP message via RMI");
                        break;
                    case "DELETE":
                        if (stub.delete(args[2]))
                            System.out.println("Peer" + args[0] + ": file " + args[3] + " DELETE RMI message sent successfully");
                        else
                            System.out.println("Error sending DELETE message via RMI");
                        break;
                }

        } catch (Exception e) {
            System.err.println("TestClient exception: " + e.toString());
            e.printStackTrace();
        }

    }
}
