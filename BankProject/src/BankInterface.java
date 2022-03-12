import java.rmi.Remote;
import java.rmi.RemoteException;

interface BankInterface extends Remote{
	public AccountInterface browse(String accountID) throws RemoteException;
	public AccountInterface returnObjectTest() throws RemoteException;//hhi
	public BankInterface getObject() throws RemoteException;//
}
