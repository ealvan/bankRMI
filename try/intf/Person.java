package intf;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Person extends Remote{
    String getName() throws RemoteException;
    void write()  throws RemoteException;
}