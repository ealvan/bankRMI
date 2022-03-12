package bank1;

import shared.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Key extends UnicastRemoteObject implements KeyInterface{

	protected Key() throws RemoteException {
		super();
	}

	@Override
	public int getId() throws RemoteException {
		return 10;
	}

}