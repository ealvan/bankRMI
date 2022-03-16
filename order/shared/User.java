package shared;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class User extends UnicastRemoteObject implements UserInterface{
    private String name=null;
    private int age = -1;
	private String userID = null;

    
	public User(String ID,String name) throws RemoteException {
		super();
		this.userID = ID;
        this.name = name;
	}
	//OFFICE CTOR-------------------------------------------------------------
	public User(String ID,String name, int age) throws RemoteException {
		super();
		this.userID = ID;
        this.age = age;
        this.name = name;
	}
	//-------------------------------------------------------------
	public User(String ID) throws RemoteException {
		super();
		userID = ID;
	}

	@Override
	public String getUserId() {
		return this.userID;
	}
	@Override
	public String getUsername() {
		return this.name;
	}
    @Override
	public int getAge() {
		return this.age;
	}
}
