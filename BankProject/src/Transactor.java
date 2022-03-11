import java.rmi.Remote;
import java.rmi.RemoteException;

class KeyException extends Exception {}
class BadAmount extends Exception {}

public interface Transactor extends Remote {
    public void join(KeyInterface t1) throws KeyException, RemoteException;
    public boolean canCommit(KeyInterface key) throws KeyException, RemoteException;
    public void commit(KeyInterface key) throws KeyException, RemoteException;
    public void abort(KeyInterface key) throws KeyException, RemoteException;
    public void tryPass(KeyInterface key) throws RemoteException;
}