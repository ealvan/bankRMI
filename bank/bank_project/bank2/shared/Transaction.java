package bank2.shared;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface Transaction extends Remote{
    int getID() throws RemoteException;
}
