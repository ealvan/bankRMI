import java.rmi.Remote;
import java.rmi.RemoteException;


interface UserInterface extends Remote {
	int getAge() throws RemoteException;

	String getUsername() throws RemoteException;

	String getUserId()throws RemoteException;
}