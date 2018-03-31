import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Services extends Remote {
    String testConnection() throws RemoteException;
    String backup(String pathname, int repDegree) throws RemoteException;
}