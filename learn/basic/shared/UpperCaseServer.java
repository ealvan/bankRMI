package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UpperCaseServer extends Remote{
    public String toUpperCase(String str) throws RemoteException;
}