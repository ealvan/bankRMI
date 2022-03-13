import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;



public class Bank extends UnicastRemoteObject implements BankInterface {
	
	
	public Bank() throws RemoteException, BadAmount, KeyException, UserException {
		super();
		dataTest();
		System.out.println("bruh!");

	}

	private ArrayList<AccountInterface> AccountList = new ArrayList<AccountInterface>();
	private ArrayList<UserInterface> UserList = new ArrayList<UserInterface>();

	
	public UserInterface login(int id) throws RemoteException{
		if(id>=UserList.size()) {
			return null;
		}
//		for (int i = 0; i<AccountList.size()+1; i++) {
//			if(UserList.get(i).getId() == id) {
//				return UserList.get(i);
//			};
//		}
		return UserList.get(id);
	}
	
	public void dataTest() throws RemoteException, BadAmount, KeyException, UserException {
		//Leer el archivo xdd
		
		
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
		//
		
	}
	
	public static AccountInterface assignBrowse(String accountID) throws RemoteException {
		if(accountID.startsWith("A")) {
			return bankBaseObject.browse(accountID);
		} else if (accountID.startsWith("B")) {
			return firstBankRemoteObject.browse(accountID);
		} else if (accountID.startsWith("C")) {
			return secondBankRemoteObject.browse(accountID);
		} else {
			return null;
		}
		
	}
	
	public static UserInterface assignLogin(String bankSel, int userID) throws RemoteException {
		if(bankSel.equalsIgnoreCase("A")) {
			return bankBaseObject.login(userID);
		} else if (bankSel.equalsIgnoreCase("B")) {
			return firstBankRemoteObject.login(userID);
		} else if (bankSel.equalsIgnoreCase("C")) {
			return secondBankRemoteObject.login(userID);
		} else {
			return null;
		}
		
	}
	
	public static boolean listCanCommit(ArrayList<AccountInterface> temporalList, Key k) throws RemoteException, KeyException {
		for ( int i = 0; i < temporalList.size(); i++) {
			if(!temporalList.get(i).canCommit(k));{
				return false;
			}
		}
		return true;
		
	}
	
	public static void Menu() throws RemoteException, BadAmount, KeyException, UserException {
		
		System.out.println("BIENVENIDO");
		Scanner sc = new Scanner(System.in);
		boolean menuOption = true;
		boolean doTransaction;
		int op = 0;
		System.out.println("Desea realizar una transaccion");
		doTransaction = sc.nextBoolean();
		if(doTransaction) {
			ArrayList<AccountInterface> temporalList = new ArrayList<AccountInterface>();
			Key transactionIdentifier = new Key();
			UserInterface userInTransaction;
			while(op != 4) {
			System.out.println("Que operacion quiere realizar");
			System.out.println("1. Deposito");
			System.out.println("2. Retiro");
			System.out.println("3. Transferencia");
			System.out.println("4. Realizar Transaccion");
			op = sc.nextInt();
			switch (op) {
				case 1: {
					System.out.println("Ingrese la cuenta a depositar");
					String accountToDeposit = sc.next();
					AccountInterface cToDeposit = assignBrowse(accountToDeposit);
					try {
						cToDeposit.join(transactionIdentifier);
						temporalList.add(cToDeposit);
						System.out.println("Ingrese el monto a depositar");
						cToDeposit.deposit(transactionIdentifier, sc.nextFloat());
						System.out.println("Deposito realizado (Sigue usted en la transaccion)");
					} catch (KeyException e) {
						System.out.println("La cuenta esta siendo utilizada en otra transaccion, Vuelva a intentar");
						cToDeposit.abort(transactionIdentifier);
					}
					break;
				}
				
				case 2: {
					System.out.println("Usted Debe estar loguearse en el banco para realizar la transferencia");
					System.out.println("Indique de que banco retirará (A) (B) (C)");
					String bank = sc.next();
					System.out.println("Indique el ID de usuario");
					int userID = sc.nextInt();
					userInTransaction = assignLogin(bank,userID);
					
					if(userInTransaction == null) {
						System.out.println("No se ingreso un usuario correcto");
						break;
					}
					
					System.out.println("Ingrese la cuenta de la que retirara:");
					String accountToWithdraw = sc.next();
					AccountInterface cToWithdraw = assignBrowse(accountToWithdraw);
					try {
						cToWithdraw.join(transactionIdentifier);
						temporalList.add(cToWithdraw);
						System.out.println("Ingrese el monto a retirar");
						cToWithdraw.withdraw(transactionIdentifier, sc.nextFloat(), userInTransaction);
						System.out.println("Retiro realizado (Sigue usted en la transaccion)");
					} catch (Exception e) {
						System.out.println("Ocurrio algun error, vuelva a intentar");
						cToWithdraw.abort(transactionIdentifier);
					}
					break;
				}
				
				case 3: {
					float auxVar = 0;
					System.out.println("Usted Debe estar logueado para realizar el retiro");
					System.out.println("Indique de que banco retirará (A) (B) (C)");
					String bank = sc.next();
					System.out.println("Indique el ID de usuario");
					int userID = sc.nextInt();
					userInTransaction = assignLogin(bank,userID);
					
					if(userInTransaction == null) {
						System.out.println("No se ingreso un usuario c");
						break;
					}
					
					System.out.println("Ingrese la cuenta de la que retirara:");
					String accountToWithdraw = sc.next();
					
					AccountInterface cToWithdraw = assignBrowse(accountToWithdraw);
					try {
						cToWithdraw.join(transactionIdentifier);
						temporalList.add(cToWithdraw);
						System.out.println("Ingrese el monto a retirar de esta cuenta");
						auxVar = sc.nextFloat();
						cToWithdraw.withdraw(transactionIdentifier, auxVar, userInTransaction);
						System.out.println("Retiro realizado (Ahora ingrese a donde realizara la transferencia)");
						String accountToDeposit = sc.next();
						AccountInterface cToDeposit = assignBrowse(accountToDeposit);
						try {
							cToDeposit.join(transactionIdentifier);
							temporalList.add(cToDeposit);
							System.out.println("Ingrese el monto a depositar");
							cToDeposit.deposit(transactionIdentifier, auxVar);
							System.out.println("Deposito realizado (Sigue usted en la transaccion)");
						} catch (Exception e) {
							System.out.println("Ocurrio algun error, vuelva a intentar");
							cToDeposit.abort(transactionIdentifier);
							cToWithdraw.abort(transactionIdentifier);
							temporalList.remove(cToDeposit);
							temporalList.remove(cToWithdraw);
						}
						break;
						
						
					} catch (KeyException e) {
						System.out.println("Ocurrio algun error, vuelva a intentar");
						cToWithdraw.abort(transactionIdentifier);
						temporalList.remove(cToWithdraw);
					}
					break;
				}
				case 4: {
					
					if(listCanCommit(temporalList,transactionIdentifier)) {
						for (int i = 0; i < temporalList.size(); i++) {
							temporalList.get(i).commit(transactionIdentifier);
						}
					} else {
						for (int i = 0; i < temporalList.size(); i++) {
							temporalList.get(i).abort(transactionIdentifier);
						}
					}
					break;
				}
				
				default:
					throw new IllegalArgumentException("Unexpected value: " + op);
				}
			
			
			}
		}
		
		
//		AccountInterface a = bankBaseObject.browse("A0004");
//		AccountInterface b = firstBankRemoteObject.browse("B0001");
//		AccountInterface c = secondBankRemoteObject.browse("C0003");
//		
//		UserInterface ua = bankBaseObject.login(3);
//		UserInterface ub = firstBankRemoteObject.login(1);
//		UserInterface uc = secondBankRemoteObject.login(2);
//		
//		Key t1 = new Key();
//        b.join(t1);
//        float bal = b.balance(t1);
//        
//        
//        b.setBalance(t1,  bal*1.1f,ua);
//        a.join(t1);
//        
//        a.withdraw(t1, bal*0.1f,ua);
//        
//        
//        c.join(t1);
//        c.setBalance(t1, 150f,uc);
//
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
//        System.out.println("Valor de a: " + a.getBalance());
//        System.out.println("Valor de b: " + b.getBalance());
//        System.out.println("Valor de c: " + c.getBalance());
        
	}
	
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
		startServer("192.168.0.3", 1091);
		assignServer("192.168.0.3", 1092, "192.168.0.3", 1093);
		
		Menu();
		
		
		
		
		
//		Registry reg_host2 = LocateRegistry.getRegistry("192.168.0.3",1092);
//		BankInterface  b = (BankInterface) reg_host2.lookup("Asd");
//		BankInterface x = b.getObject();
//        System.out.println(x);
//        MyTransactor y =  b.returnObjectTest();
//        System.out.println("El objeto Cuenta: ");
//        System.out.println(y);
		
		
	}


}
