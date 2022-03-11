import java.rmi.Remote;
import java.rmi.RemoteException;

class KeyException extends Exception {}
class BadAmount extends Exception {}

public interface Transactor extends Remote {
    public void join(Object t1) throws KeyException, RemoteException;
    public boolean canCommit(Object key) throws KeyException, RemoteException;
    public void commit(Object key) throws KeyException, RemoteException;
    public void abort(Object key) throws KeyException, RemoteException;
}