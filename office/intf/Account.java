package intf;

import java.rmi.Remote;
import java.rmi.RemoteException;

class TransactionException extends Exception{};

public interface Account extends Remote {
  public float getBalance() throws RemoteException;
  public void postTransaction(Transaction transaction)
    throws RemoteException, TransactionException;
}