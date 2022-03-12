import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class User extends UnicastRemoteObject implements UserInterface{

	int userID;
	
	protected User(int ID) throws RemoteException {
		super();
		userID = ID;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getId() throws RemoteException {
		// TODO Auto-generated method stub
		return userID;
	}

}
