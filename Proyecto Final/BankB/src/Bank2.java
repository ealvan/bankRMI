import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Bank2 extends UnicastRemoteObject implements BankInterface {
	
	
	public Bank2() throws RemoteException, BadAmount, KeyException, UserException {
		super();
		//dataTest();
		System.out.println("bruh!");

	}

	private ArrayList<AccountInterface> AccountList = new ArrayList<AccountInterface>();
	private HashMap<String,UserInterface> userList = new HashMap<String,UserInterface>();

	
	public UserInterface login(String userID) throws RemoteException{
        if(!userList.containsKey(userID)){
            try{
                throw  new Exception("Usuario no esta en HashMap userList");
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        UserInterface tmp = userList.get(userID);
        return tmp;
	}
	
//	public void dataTest() throws RemoteException, BadAmount, KeyException, UserException {
//		//Leer el archivo xdd
//		
//		
//		UserList.add(null);
//		UserList.add(new User(1));
//		UserList.add(new User(2));
//		UserList.add(new User(3));
//		
//		AccountList.add(new Account(UserList.get(1),"A0001",100f));//0
//		AccountList.add(new Account(UserList.get(2),"A0002",200f));//1
//		AccountList.add(new Account(UserList.get(2),"A0003",100f));
//		AccountList.add(new Account(UserList.get(3),"A0004",100f));
//		AccountList.add(new Account(UserList.get(3),"A0005",100f));
//		AccountList.add(new Account(UserList.get(3),"A0006",100f));
//		
//		
//		
//		
//		
//		Key u1 = new Key();
//		System.out.println(u1);
//		
//		System.out.println("Tama�o " + AccountList.size());
//		AccountList.get(1).join(u1);
//		AccountList.get(1).withdraw(u1, 10f, UserList.get(2));
//		
//		
//	}
	
    public void addUser(UserInterface user) throws RemoteException{
        
        if(user.getUserId() != null){
            if(userList.containsKey(user.getUserId())){
                // try{
                //     throw  new Exception("Usuario con "+user.getUserId() + " ya existe!!, no agregado.");
                // }catch(Exception e){
                //     e.printStackTrace();
                // }            
                System.err.println("Usuario con "+user.getUserId() + " ya existe!!, no agregado.");
                return;
            }
            System.out.println("Nuevo usuario agregado UserID= "+user.getUserId());
            userList.put(user.getUserId(), user);
            return;
        }
        
        try{
            throw  new Exception("Usuario tiene UserID es NULL");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void addBankAccount(AccountInterface transactor) throws RemoteException{

        if(transactor.getAccountID() != null){
            for(AccountInterface item : this.AccountList){
                if(item.getAccountID().equals(transactor.getAccountID())){
                    System.out.println("Cuenta AccountID="+item.getAccountID() +" ya existe,no agregada");
                    return;
                }
            }
            System.out.println("Transactor AccountID agregado: "+transactor.getAccountID());
            this.AccountList.add(transactor);
            return;
        }

        try{
            throw new Exception("Transactor no tiene ID, no se le puede agregar al BANCO A");
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
	@Override
	public AccountInterface browse(String accountID) throws RemoteException {
        if(AccountList.size() <= 0){
            System.out.println("NO EXISTEN USUARIOS EN \"lista\"\n");
            return null;
        }
		for (int i = 0; i<AccountList.size(); i++) {
            // System.out.println("ACCOUNT ID : "+lista.get(i).getAccountID()+"\n");
			if(AccountList.get(i).getAccountID().equals(accountID)) {
                System.out.println("Se encontro!! en browse A AccountID=" +this.AccountList.get(i).getAccountID()+"\n");
				return AccountList.get(i);
			};
		}
        System.out.println("No se encontro AccountID= "+accountID+"\n");
		return null;
	}
	
	@Override
    public BankInterface getObject(String name){
        if(name.toLowerCase().equals("local")){
            System.out.println("ENTRO! LOCAL");
            return bankBaseObject;
        }else if (name.toLowerCase().equals("bank1")){
            System.out.println("ENTRO! BANK1");
            return firstBankRemoteObject;
        }else if (name.toLowerCase().equals("bank2")){
            System.out.println("ENTRO! BANK2 ");
            return secondBankRemoteObject;            
        }
        System.out.println("NO ENTRO!! ");
        return null;
    }
	
	public HashMap<String,UserInterface> getUserList() {
        return userList;
    }
    public ArrayList<AccountInterface> getAccounts(){
        return AccountList;
    }
	
    
    @SuppressWarnings("unchecked")
    public void retrieveUserObjects(){
        File f = new File(Storage.UserFile);
        if(f.exists() && !f.isDirectory()){
            this.userList = (HashMap<String,UserInterface>)Storage.retrieveObject(Storage.UserFile);
        }else{
            this.userList = new HashMap<String,UserInterface>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public void retrieveAccountsObjects(){
        File f = new File(Storage.AccountFile);
        if(f.exists() && !f.isDirectory()){
            this.AccountList.addAll((ArrayList<AccountInterface>) Storage.retrieveObject(Storage.AccountFile));
            // this.lista.addAll(this.lista);
        }else{
            this.AccountList = new ArrayList<AccountInterface>();
        }
    }
    
    public void saveUserObjects(){
        Storage.saveObject(this.userList, Storage.UserFile);
    }
    public void saveAccountObjects(){
        Storage.saveObject(this.AccountList, Storage.AccountFile);
    }
    
    public void printUsers() throws RemoteException{
        if(this.userList.isEmpty()){
            System.out.println("Esta vacia el this.userLista");
        }else
        for(String key: this.userList.keySet()) {
            UserInterface user = this.userList.get(key);
            System.out.println(user.getUserId() + " --- " + user.getUsername());
        }
    }
    
    public void printAccounts() throws RemoteException,KeyException{
        if(this.AccountList.isEmpty()){
            System.out.println("Esta vacia el this.lista");
        }else
        for(AccountInterface item: this.AccountList){
            System.out.println(item.getAccountID()+" --- "+item.getBalance());
        }
    }
    
	public static AccountInterface assignBrowse(String accountID) throws RemoteException {
		if(accountID.startsWith("A")) {
			return firstBankRemoteObject.browse(accountID);
		} else if (accountID.startsWith("B")) {
			return bankBaseObject.browse(accountID);
		} else if (accountID.startsWith("C")) {
			return secondBankRemoteObject.browse(accountID);
		} else {
			return null;
		}
		
	}
	
	public static UserInterface assignLogin(String bankSel, String userID) throws RemoteException {
		if(bankSel.equalsIgnoreCase("A")) {
			return firstBankRemoteObject.login(userID);
		} else if (bankSel.equalsIgnoreCase("B")) {
			return bankBaseObject.login(userID);
		} else if (bankSel.equalsIgnoreCase("C")) {
			return secondBankRemoteObject.login(userID);
		} else {
			return null;
		}
		
	}
	
	public static boolean listCanCommit(ArrayList<AccountInterface> temporalList, Key k) throws RemoteException, KeyException {
		for(int con = 0; con < temporalList.size();con++) {
			if(!temporalList.get(con).canCommit(k)) {
				return false;
			}
		}
//		for ( int i = 0; i < temporalList.size(); i++) {
//			if(!temporalList.get(i).canCommit(k));{
//				return false;
//			}
//		}
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
		
		while(doTransaction) {
			System.out.println("=====TRANSACCIOn");
			ArrayList<AccountInterface> temporalList = new ArrayList<AccountInterface>();
			Key transactionIdentifier = new Key((int)(System.currentTimeMillis() % Integer.MAX_VALUE));
			UserInterface userInTransaction;
			op = -1;
			while(op != 5) {
				System.out.println("Que operacion quiere realizar");
				System.out.println("1. Deposito");
				System.out.println("2. Retiro");
				System.out.println("3. Transferencia");
				System.out.println("4. ver saldo de cuenta");
				System.out.println("5. Realizar Transaccion");
			op = sc.nextInt();
			switch (op) {
				case 1: {
					System.out.println("Ingrese la cuenta a depositar");
					String accountToDeposit = sc.next();
					AccountInterface cToDeposit = assignBrowse(accountToDeposit);
					try {
						cToDeposit.join(transactionIdentifier);
						if(!temporalList.contains(cToDeposit)) {
							temporalList.add(cToDeposit);
						}
						System.out.println("Ingrese el monto a depositar");
						cToDeposit.deposit(transactionIdentifier, sc.nextFloat());
						System.out.println("Deposito realizado (Sigue usted en la transaccion)");
					} catch (KeyException e) {
						System.out.println("La cuenta esta siendo utilizada en otra transaccion, Vuelva a intentar");
						//cToDeposit.abort(transactionIdentifier);
					}
					break;
				}
				
				case 2: {
					System.out.println("Usted Debe estar loguearse en el banco para realizar la transferencia");
					System.out.println("Indique de que banco retirar� (A) (B) (C)");
					String bank = sc.next();
					System.out.println("Indique el ID de usuario");
					String userID = sc.next();
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
						if(!temporalList.contains(cToWithdraw)) {
							temporalList.add(cToWithdraw);
						}
						System.out.println("Ingrese el monto a retirar");
						cToWithdraw.withdraw(transactionIdentifier, sc.nextFloat(), userInTransaction);
						System.out.println("Retiro realizado (Sigue usted en la transaccion)");
					} catch (KeyException e) {
						System.out.println("La cuenta esta siendo utilizada");
						//cToWithdraw.abort(transactionIdentifier);
					}
					break;
				}
				
				case 3: {
					float auxVar = 0;
					System.out.println("Usted Debe estar logueado para realizar el retiro");
					System.out.println("Indique de que banco retirar� (A) (B) (C)");
					String bank = sc.next();
					System.out.println("Indique el ID de usuario");
					String userID = sc.next();
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
						if(!temporalList.contains(cToWithdraw)) {
							temporalList.add(cToWithdraw);
						}
						System.out.println("Ingrese el monto a retirar de esta cuenta");
						auxVar = sc.nextFloat();
						cToWithdraw.withdraw(transactionIdentifier, auxVar, userInTransaction);
						System.out.println("Retiro realizado (Ahora ingrese a donde realizara la transferencia)");
						String accountToDeposit = sc.next();
						AccountInterface cToDeposit = assignBrowse(accountToDeposit);
						try {
							cToDeposit.join(transactionIdentifier);
							if(!temporalList.contains(cToDeposit)) {
								temporalList.add(cToDeposit);
							}
							System.out.println("Ingrese el monto a depositar");
							cToDeposit.deposit(transactionIdentifier, auxVar);
							System.out.println("Deposito realizado (Sigue usted en la transaccion)");
						} catch (KeyException e) {
							System.out.println("La cuenta esta siendo utilizada");
							//temporalList.remove(cToDeposit);
							temporalList.remove(cToWithdraw);
							cToWithdraw.abort(transactionIdentifier);
						}
						break;
						
						
					} catch (KeyException e) {
						System.out.println("La cuenta esta siendo utilizada");
						//cToWithdraw.abort(transactionIdentifier);
						//temporalList.remove(cToWithdraw);
					}
					break;
				}
				case 4: {
					System.out.println("Ingrese la cuenta y se mostrara saldo");
					String accountToDeposit = sc.next();
					AccountInterface cToDeposit = assignBrowse(accountToDeposit);
					try {
						cToDeposit.join(transactionIdentifier);
						if(!temporalList.contains(cToDeposit)) {
							temporalList.add(cToDeposit);
						}
						
						System.out.println("Su saldo es " + cToDeposit.balance(transactionIdentifier));
					} catch (KeyException e) {
						System.out.println("La cuenta esta siendo utilizada en otra transaccion, Vuelva a intentar");
					}
					break;
				}
				case 5: {
					
					if(listCanCommit(temporalList,transactionIdentifier)) {
						System.out.println("Se guardo que exitosamente");
						for (int i = 0; i < temporalList.size(); i++) {
							temporalList.get(i).commit(transactionIdentifier);
						}
					} else {
						System.out.println("Se aborto");
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
			
			System.out.println("Desea realizar otra transaccion");
			doTransaction = sc.nextBoolean();
		}
		
		System.out.println("USTED SALIO DEL PROGRAMA");
        
	}
	
	private static Registry baseServer;
	private static Registry firstRemoteServer;
	private static Registry secondRemoteServer;
	
	
	private static BankInterface firstBankRemoteObject;
	private static Bank2 bankBaseObject;
	private static BankInterface secondBankRemoteObject;
	
	
	public static void startServer(String ip, int port) {
        try {
            System.setProperty("java.rmi.server.hostname",ip);
            baseServer = LocateRegistry.createRegistry(port);
            bankBaseObject = new Bank2();
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
	@SuppressWarnings("unchecked")
	public static void main(String [] args) throws RemoteException, NotBoundException, InterruptedException, BadAmount, KeyException, UserException {
//		startServer("192.168.0.3", 1091);
//		assignServer("192.168.0.3", 1092, "192.168.0.3", 1093);
//		
//		Menu();
		
        Bank2 bankB = new Bank2();
        
        bankB.startServer("192.168.0.10", 1091);
        
        // //REMOVE ALL content before started
        // bankA.bankBaseObject.getUserList().clear();
        // bankA.bankBaseObject.getAccounts().clear();
        
        bankB.bankBaseObject.retrieveAccountsObjects();
        bankB.bankBaseObject.retrieveUserObjects();

        //-------------------------------------------------------------------
        bankB.bankBaseObject.printAccounts();
        bankB.bankBaseObject.printUsers();
        //-------------------------------------------------------------------
        //add remote object transactor to account list objects
        
        UserInterface U5 = new User("U5","Luisito",22);
        UserInterface U6 = new User("U6","Jose",22);
        UserInterface U7 = new User("U7","Marco",22);

        UserInterface U3 = new User("U3","Pablo",22);
        UserInterface U4 = new User("U4","Luis",22);
        
        bankB.bankBaseObject.addUser(U5);
        bankB.bankBaseObject.addUser(U6);
        bankB.bankBaseObject.addUser(U7);

        bankB.bankBaseObject.addUser(U3);
        bankB.bankBaseObject.addUser(U4);
        
        bankB.bankBaseObject.addBankAccount(new Account(U5,"B001",100f));
        bankB.bankBaseObject.addBankAccount(new Account(U5,"B002",100f));
        bankB.bankBaseObject.addBankAccount(new Account(U5,"B003",100f));
        
        bankB.bankBaseObject.addBankAccount(new Account(U6,"B004",100f));
        bankB.bankBaseObject.addBankAccount(new Account(U6,"B005",100f));
        bankB.bankBaseObject.addBankAccount(new Account(U6,"B006",100f));
        
        bankB.bankBaseObject.addBankAccount(new Account(U7,"B007",100f));
        bankB.bankBaseObject.addBankAccount(new Account(U7,"B008",100f));
        bankB.bankBaseObject.addBankAccount(new Account(U7,"B009",100f));
        
        bankB.bankBaseObject.addBankAccount(new Account(U3,"B010",100f));
        
        //Cuando estes en B se pone un usuario con U3
        
        bankB.bankBaseObject.addBankAccount(new Account(U4,"B011",100f)); //Este U4 posee en los 3 bancos
        

        Thread.sleep(15000);
        System.out.println("Se asignaran los otros servidores");
        bankB.assignServer("192.168.0.3", 1091, "192.168.0.11", 1091);
        bankB.Menu();
        Thread.sleep(20000);
        System.out.println("SAVE objects...");
        bankB.bankBaseObject.saveAccountObjects();
        bankB.bankBaseObject.saveUserObjects();
        System.exit(0);
		
		
		
		
		
//		Registry reg_host2 = LocateRegistry.getRegistry("192.168.0.3",1092);
//		BankInterface  b = (BankInterface) reg_host2.lookup("Asd");
//		BankInterface x = b.getObject();
//        System.out.println(x);
//        MyTransactor y =  b.returnObjectTest();
//        System.out.println("El objeto Cuenta: ");
//        System.out.println(y);
		
		
	}


}
