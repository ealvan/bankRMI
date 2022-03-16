package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankInterface extends Remote{
	public MyTransactor browse(String accountID) throws RemoteException;
	// public MyTransactor returnObjectTest() throws RemoteException;//hhi
	public BankInterface getObject(String name) throws RemoteException;//
	public UserInterface login(String userID) throws RemoteException;
	//public void saveUsersObjects() throws RemoteException;
	//public void saveAccountsObjects() throws RemoteException;
}
