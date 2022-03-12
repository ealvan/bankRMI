import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Bank extends UnicastRemoteObject implements BankInterface {
	
	public Bank() throws RemoteException, BadAmount, KeyException, UserException {
		super();
		dataTest();
		System.out.println("bruh!");

	}

	private ArrayList<AccountInterface> AccountList = new ArrayList<AccountInterface>();
	private ArrayList<UserInterface> UserList = new ArrayList<UserInterface>();

	public UserInterface login(int id) throws RemoteException{
		for (int i = 0; i<AccountList.size(); i++) {
			if(UserList.get(i).getId() == id) {
				return UserList.get(i);
			};
		}
		return null;
	}
	
	public void dataTest() throws RemoteException, BadAmount, KeyException, UserException {
		//Leer el archivo
		UserList.add(null);
		UserList.add(new User(1));
		UserList.add(new User(2));
		UserList.add(new User(3));
		
		AccountList.add(new Account(UserList.get(1),"A0001",100f));//0
		AccountList.add(new Account(UserList.get(2),"A0002",200f));//1
		AccountList.add(new Account(UserList.get(2),"A0003",100f));
		AccountList.add(new Account(UserList.get(3),"A0004",100f));
		AccountList.add(new Account(UserList.get(3),"A0005",100f));
		AccountList.add(new Account(UserList.get(3),"A0006",100f));
		
		
		Key u1 = new Key();
		System.out.println(u1);
		
		System.out.println("Tamaño " + AccountList.size());
		AccountList.get(1).join(u1);
		AccountList.get(1).withdraw(u1, 10f, UserList.get(2));
		
		
	}
	
	@Override
	public AccountInterface browse(String accountID) throws RemoteException {
		for (int i = 0; i<AccountList.size(); i++) {
			if(AccountList.get(i).getID().equals(accountID)) {
				return AccountList.get(i);
			};
		}
		return null;
	}
	
	public void LeerArchivo() {
		
	}
	
	
//	public static void Menu() throws RemoteException, BadAmount, KeyException {
//		MyTransactor a = bankBaseObject.returnObjectTest();
//		MyTransactor b = firstBankRemoteObject.returnObjectTest();
//		MyTransactor c = secondBankRemoteObject.returnObjectTest();
//		
//		Key t1 = new Key();
//        b.join(t1);
//        float bal = b.balance(t1);
//        b.setBalance(t1,  bal*1.1f);
//        a.join(t1);
//        a.withdraw(t1, bal*0.1f);
//        c.join(t1);
//        c.setBalance(t1, 150f);
//        System.out.println("Valor de a: " + a.balance(t1));
//        System.out.println("Valor de b: " + b.balance(t1));
//        System.out.println("Valor de c: " + c.balance(t1));
//        
//        if( a.canCommit(t1) && b.canCommit(t1) && c.canCommit(t1) ){
//            a.commit(t1);
//            b.commit(t1);
//            c.commit(t1);
//        }
//        else {
//            a.abort(t1);
//            b.abort(t1);
//            c.abort(t1);
//        } 
//        
//	}
	
	private static Registry baseServer;
	private static Registry firstRemoteServer;
	private static Registry secondRemoteServer;
	
	private static Bank bankBaseObject;
	private static BankInterface firstBankRemoteObject;
	private static BankInterface secondBankRemoteObject;
	
	
	public static void startServer(String ip, int port) {
        try {
            System.setProperty("java.rmi.server.hostname",ip);
            baseServer = LocateRegistry.createRegistry(port);
            bankBaseObject = new Bank();
            baseServer.bind("Bank", bankBaseObject);      
            System.err.println("Servidor A esta habilitado");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}
	static AccountInterface aux1;
	public static void assignServer(String firstServerIP, int firstPort, String secondServerIP, int secondPort) throws RemoteException, NotBoundException {
		AccountInterface ad;
		firstRemoteServer = LocateRegistry.getRegistry(firstServerIP,firstPort);
		secondRemoteServer = LocateRegistry.getRegistry(secondServerIP,secondPort);
		firstBankRemoteObject = (BankInterface) firstRemoteServer.lookup("Bank");
		secondBankRemoteObject = (BankInterface) secondRemoteServer.lookup("Bank");
		System.err.println("Se completo carga de Bancos");
		System.out.println(bankBaseObject);
		System.out.println(firstBankRemoteObject);
		System.out.println(secondBankRemoteObject);

		
	}
	
	
	@Override
	public AccountInterface returnObjectTest() throws RemoteException {
		return aux1;
	}
	
	@Override
	public BankInterface getObject() throws RemoteException {
		System.out.println("entro aca");
		//BankInterface aux = new Bank();
		return null;
	}
	
	
	public static void main(String [] args) throws RemoteException, NotBoundException, InterruptedException, BadAmount, KeyException, UserException {
		Bank a = new Bank();
		
//        startServer("192.168.0.3", 1091);
//        
//		Thread.sleep(10000);
//		assignServer("192.168.0.3", 1092, "192.168.0.3", 1093);
		
		
		
		
//		Registry reg_host2 = LocateRegistry.getRegistry("192.168.0.3",1092);
//		BankInterface  b = (BankInterface) reg_host2.lookup("Asd");
//		BankInterface x = b.getObject();
//        System.out.println(x);
//        MyTransactor y =  b.returnObjectTest();
//        System.out.println("El objeto Cuenta: ");
//        System.out.println(y);
		
		
	}
}
