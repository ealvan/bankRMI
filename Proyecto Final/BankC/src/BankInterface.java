import java.rmi.Remote;
import java.rmi.RemoteException;

interface BankInterface extends Remote{
	public AccountInterface browse(String accountID) throws RemoteException;
	// public MyTransactor returnObjectTest() throws RemoteException;//hhi
	public BankInterface getObject(String name) throws RemoteException;//
	public UserInterface login(String userID) throws RemoteException;
}
