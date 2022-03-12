package shared;

import java.rmi.Remote;

import java.rmi.RemoteException;

public interface MyTransactor extends Remote {
    public void deposit(KeyInterface key, float amount) throws BadAmount, KeyException, RemoteException;
    public void withdraw(KeyInterface key, float amount) throws BadAmount, KeyException, RemoteException;
    public float balance(KeyInterface key) throws KeyException, RemoteException;
    public void setBalance(KeyInterface key, float amount) throws BadAmount, KeyException, RemoteException;
    public String getID() throws RemoteException;
    public void setValue(float money) throws RemoteException;
    public MyTransactor getObject() throws RemoteException;
    public KeyInterface getObjectKey() throws RemoteException;
    public float getBalance() throws KeyException, RemoteException;
    public String getAccountID() throws RemoteException;

    ///TRANSACTOR
    public void join(KeyInterface t1) throws KeyException, RemoteException;
    public boolean canCommit(KeyInterface key) throws KeyException, RemoteException;
    public void commit(KeyInterface key) throws KeyException, RemoteException;
    public void abort(KeyInterface key) throws KeyException, RemoteException;
    public void tryPass(KeyInterface key) throws RemoteException;
    
}
