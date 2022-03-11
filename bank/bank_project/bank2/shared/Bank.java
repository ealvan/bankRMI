package bank2.shared;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote{
    //TRANSACTOR ...
    public void join(Transaction key) 
            throws KeyException,RemoteException;

    public boolean canCommit(Transaction key) 
            throws KeyException,RemoteException;

    public void commit(Transaction key) 
            throws KeyException,RemoteException;

    public void abort(Transaction key) 
            throws KeyException,RemoteException;
//-----------------------------------------------------------------
    //BANK OPERATIONS...
    public void deposit(Transaction key, float amount) 
            throws BadAmount, KeyException,RemoteException;

    public void withdraw(Transaction key, float amount) 
            throws BadAmount, KeyException,RemoteException;

    public float balance(Transaction key) 
            throws KeyException,RemoteException;

    public void setBalance(Transaction key, float amount) 
            throws BadAmount, KeyException,RemoteException;
//------------------------------------------------------------------
    //TEST operations
    public String sayHello(String name) throws RemoteException;
    
}
