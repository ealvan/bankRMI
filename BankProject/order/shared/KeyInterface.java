package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface KeyInterface extends Remote {
    public int getId() throws RemoteException;
}