package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Services extends Remote {
    String testConnection() throws RemoteException;
    boolean backup(String pathname, int repDegree) throws RemoteException;
}