package shared;
import java.rmi.Remote;
import java.rmi.RemoteException;


interface UserInterface extends Remote {
    public int getId() throws RemoteException;
}