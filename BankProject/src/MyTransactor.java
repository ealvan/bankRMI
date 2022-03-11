import java.rmi.Remote;

import java.rmi.RemoteException;

interface MyTransactor extends Transactor, Remote {
    public void deposit(KeyInterface key, float amount) throws BadAmount, KeyException, RemoteException;
    public void withdraw(KeyInterface key, float amount) throws BadAmount, KeyException, RemoteException;
    public float balance(KeyInterface key) throws KeyException, RemoteException;
    public void setBalance(KeyInterface key, float amount) throws BadAmount, KeyException, RemoteException;
    public String getID() throws RemoteException;
    public void setValue(float money) throws RemoteException;
    public MyTransactor getObject() throws RemoteException;
    public KeyInterface getObjectKey() throws RemoteException;
    public float getBalance() throws KeyException, RemoteException;
}
