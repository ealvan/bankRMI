import java.rmi.Remote;
import java.rmi.RemoteException;

interface MyTransactor extends Transactor, Remote {
    public void deposit(Object key, float amount) throws BadAmount, KeyException, RemoteException;
    public void withdraw(Object key, float amount) throws BadAmount, KeyException, RemoteException;
    public float balance(Object key) throws KeyException, RemoteException;
    public void setBalance(Object key, float amount) throws BadAmount, KeyException, RemoteException;
    
}
