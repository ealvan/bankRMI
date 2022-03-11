import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Key extends UnicastRemoteObject implements KeyInterface{

	protected Key() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getId() throws RemoteException {
		// TODO Auto-generated method stub
		return 10;
	}

}
