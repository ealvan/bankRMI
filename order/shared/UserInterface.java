package shared;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface UserInterface extends Remote {
    public String getUserId() throws RemoteException;
    public String getUsername() throws RemoteException;
    public int getAge() throws RemoteException;
}