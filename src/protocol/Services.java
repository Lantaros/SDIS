package protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Services extends Remote {
    String testConnection() throws RemoteException;
    void backup(String pathname, int repDegree) throws RemoteException;
    boolean delete(String pathname) throws RemoteException;
    boolean restore(String pathname) throws RemoteException;
}