import java.rmi.Remote;

import java.rmi.RemoteException;

class KeyException extends Exception {}
class BadAmount extends Exception {}
class UserException extends Exception {}

public interface AccountInterface extends Remote {
	
    public void join(KeyInterface t1) throws KeyException, RemoteException;
    public boolean canCommit(KeyInterface key) throws KeyException, RemoteException;
    public void commit(KeyInterface key) throws KeyException, RemoteException;
    public void abort(KeyInterface key) throws KeyException, RemoteException;
    public void tryPass(KeyInterface key) throws RemoteException;
    
    public void deposit(KeyInterface key, float amount) throws BadAmount, KeyException, RemoteException;
    public void withdraw(KeyInterface key, float amount, UserInterface user) throws BadAmount, KeyException, RemoteException, UserException;
    public float balance(KeyInterface key) throws KeyException, RemoteException;
    public void setBalance(KeyInterface key, float amount, UserInterface user) throws BadAmount, KeyException, RemoteException, UserException;
    
    public String getID() throws RemoteException;
    public void setValue(float money) throws RemoteException;
    public AccountInterface getObject() throws RemoteException;
    public KeyInterface getObjectKey() throws RemoteException;
    public float getBalance() throws KeyException, RemoteException;
}
