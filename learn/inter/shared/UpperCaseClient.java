package shared;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface UpperCaseClient extends Remote{
    public void upperCaseResult(String str) throws RemoteException;
}
