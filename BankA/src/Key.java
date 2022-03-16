import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Key extends UnicastRemoteObject implements KeyInterface{
	
	int kID = 0;
	

	protected Key(int random) throws RemoteException {
		super();
		kID = random;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getId() throws RemoteException {
		// TODO Auto-generated method stub
		return kID;
	}

}
