package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UpperCaseServer extends Remote{
    public void toUpperCase(String str,UpperCaseClient client) throws RemoteException;
}