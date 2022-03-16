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

public class Bank3 extends UnicastRemoteObject implements BankInterface {
	
	
	public Bank3() throws RemoteException, BadAmount, KeyException, UserException {
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
//		System.out.println("Tamaño " + AccountList.size());
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
			return secondBankRemoteObject.browse(accountID);
		} else if (accountID.startsWith("C")) {
			return bankBaseObject.browse(accountID);
		} else {
			return null;
		}
		
	}
	
	public static UserInterface assignLogin(String bankSel, String userID) throws RemoteException {
		if(bankSel.equalsIgnoreCase("A")) {
			return firstBankRemoteObject.login(userID);
		} else if (bankSel.equalsIgnoreCase("B")) {
			return secondBankRemoteObject.login(userID);
		} else if (bankSel.equalsIgnoreCase("C")) {
			return bankBaseObject.login(userID);
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
			Key transactionIdentifier = new Key((int)(Math.random()*(1000-1+1)+1));
			UserInterface userInTransaction;
			op = -1;
			while(op != 5) {
				System.out.println("Que operacion quiere realizar");
				System.out.println("1. Deposito");
				System.out.println("2. Retiro");
				System.out.println("3. Transferencia");
				System.out.println("4. ver saldo de cuenta");
				System.out.println("5. Realizar Transaccion");
				System.out.println("6. Ver id de la transaccion");
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
					System.out.println("Indique de que banco retirará (A) (B) (C)");
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
					} catch (BadAmount f) {
						System.out.println("Tu dinero es menor a lo que quieres retirar, tienes" + cToWithdraw.balance(transactionIdentifier));
					}
					break;
				}
				
				case 3: {
					float auxVar = 0;
					System.out.println("Usted Debe estar logueado para realizar el retiro");
					System.out.println("Indique de que banco retirará (A) (B) (C)");
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
					} catch (BadAmount f) {
						System.out.println("Tu dinero es menor a lo que quieres retirar, tienes" + cToWithdraw.balance(transactionIdentifier));
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

			
				case 6: {
					
					System.out.println(transactionIdentifier.getId());
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
	
	private static Bank3 bankBaseObject;
	private static BankInterface firstBankRemoteObject;
	private static BankInterface secondBankRemoteObject;
	
	
	public static void startServer(String ip, int port) {
        try {
            System.setProperty("java.rmi.server.hostname",ip);
            baseServer = LocateRegistry.createRegistry(port);
            bankBaseObject = new Bank3();
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
		
        Bank3 bankC = new Bank3();
        
        bankC.startServer("192.168.0.11", 1091);
        
        // //REMOVE ALL content before started
        // bankA.bankBaseObject.getUserList().clear();
        // bankA.bankBaseObject.getAccounts().clear();
        
        bankC.bankBaseObject.retrieveAccountsObjects();
        bankC.bankBaseObject.retrieveUserObjects();

        //-------------------------------------------------------------------
        bankC.bankBaseObject.printAccounts();
        bankC.bankBaseObject.printUsers();
        //-------------------------------------------------------------------
        //add remote object transactor to account list objects
        
        UserInterface U8 = new User("U8","MariaC",22);
        UserInterface U9 = new User("U9","Fafricio",22);
        UserInterface U10 = new User("U10","Ricardo",22);

        UserInterface U4 = new User("U4","Luis",22);
        
        bankC.bankBaseObject.addUser(U8);
        bankC.bankBaseObject.addUser(U9);
        bankC.bankBaseObject.addUser(U10);

        bankC.bankBaseObject.addUser(U4);
        
        bankC.bankBaseObject.addBankAccount(new Account(U8,"C001",100f));
        bankC.bankBaseObject.addBankAccount(new Account(U8,"C002",100f));
        bankC.bankBaseObject.addBankAccount(new Account(U8,"C003",100f));
        
        bankC.bankBaseObject.addBankAccount(new Account(U9,"C004",100f));
        bankC.bankBaseObject.addBankAccount(new Account(U9,"C005",100f));
        bankC.bankBaseObject.addBankAccount(new Account(U9,"C006",100f));
        
        bankC.bankBaseObject.addBankAccount(new Account(U10,"C007",100f));
        bankC.bankBaseObject.addBankAccount(new Account(U10,"C008",100f));
        bankC.bankBaseObject.addBankAccount(new Account(U10,"C009",100f));
        
        bankC.bankBaseObject.addBankAccount(new Account(U4,"C010",100f)); //Este U4 posee en los 3 bancos
        

        Thread.sleep(15000);
        System.out.println("Se asignaran los otros servidores");
        bankC.assignServer("192.168.0.3", 1091, "192.168.0.10", 1091);
        
        bankC.Menu();
        Thread.sleep(20000);
        System.out.println("SAVE objects...");
        bankC.bankBaseObject.saveAccountObjects();
        bankC.bankBaseObject.saveUserObjects();
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
