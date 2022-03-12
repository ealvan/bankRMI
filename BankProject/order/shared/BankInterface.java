package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

interface BankInterface extends Remote{
	public MyTransactor browse(String accountID) throws RemoteException;
	// public MyTransactor returnObjectTest() throws RemoteException;//hhi
	// public BankInterface getObject() throws RemoteException;//
}
