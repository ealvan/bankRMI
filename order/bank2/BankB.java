package bank2;

import shared.*;
import shared.Key;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class BankB extends UnicastRemoteObject implements BankInterface{
	private ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();
    private HashMap<String,UserInterface> userList = new HashMap<String,UserInterface>();
    //---------------------------------------------------------------------------------
    @Override
    public UserInterface login(String userID){
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
    
    public void addUser(UserInterface user) throws RemoteException{
        
        if(user.getUserId() != null){
            if(userList.containsKey(user.getUserId())){
                try{
                    throw  new Exception("La clave "+user.getUserId() + " ya existe!!, cree una nueva");
                }catch(Exception e){
                    e.printStackTrace();
                }            
                return;
            }
            System.out.println("Nuevo usuario agregado UserID= "+user.getUserId());
            userList.put(user.getUserId(), user);
            return;
        }
        
        try{
            throw  new Exception("Usuario no tiene UserID es NULL");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private Registry baseServer;
	private Registry firstRemoteServer;
	private Registry secondRemoteServer;
	
	private BankB bankBaseObject;
	private BankInterface firstBankRemoteObject;
	private BankInterface secondBankRemoteObject;


    public BankB() throws RemoteException {
		super();
    }

	
    public void startServer(String ip, int port) {
        try {
            System.setProperty("java.rmi.server.hostname",ip);
            baseServer = LocateRegistry.createRegistry(port);
            bankBaseObject = new BankB();
            baseServer.bind("Bank", bankBaseObject);      
            System.err.println("Servidor B esta habilitado");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}

    public void assignServer(String firstServerIP, int firstPort, String secondServerIP, int secondPort) throws RemoteException, NotBoundException {
		// MyTransactor ad;
		firstRemoteServer = LocateRegistry.getRegistry(firstServerIP,firstPort);
		secondRemoteServer = LocateRegistry.getRegistry(secondServerIP,secondPort);
		firstBankRemoteObject = (BankInterface) firstRemoteServer.lookup("Bank");
		secondBankRemoteObject = (BankInterface) secondRemoteServer.lookup("Bank");
		System.err.println("Se completo carga de Bancos");
		// System.out.println(bankBaseObject);
		// System.out.println(firstBankRemoteObject);
		// System.out.println(secondBankRemoteObject);
		//MyTransactor a = firstBankRemoteObject.getObject();
		
	}

    public void addBankAccount(MyTransactor transactor) throws Exception{
        if(transactor.getAccountID() != null){
            System.out.println("Nuevo transactor con AccountID agregado: "+transactor.getAccountID());
            this.lista.add(transactor);//"importante!!"
            return;
        }

        try{
            throw new Exception("Transactor no tiene ID, no se le puede agregar al BANCO A");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void print(MyTransactor a,MyTransactor b,MyTransactor c) throws RemoteException, KeyException{
        System.out.println("Lista de cuentas con sus Balances: \nA: " + a.getBalance() + " B: " + b.getBalance() + " C: " + c.getBalance());
    }
//--------------------------IMPLEMENTS------------------------------------------------
    //retorna una Cuenta del Banco A
    @Override
	public MyTransactor browse(String accountID) throws RemoteException {
        if(lista.size() <= 0){
            System.out.println("NO SE AGREGARON MYTRANSATORS EN \"lista\"\n");
            return null;
        }
		for (int i = 0; i<lista.size(); i++) {
            // System.out.println("ACCOUNT ID : "+lista.get(i).getAccountID()+"\n");
			if(lista.get(i).getAccountID().equals(accountID)) {
                System.out.println("Se encontro!! en browse B AccountID="  +this.lista.get(i).getAccountID()+"\n");
				return lista.get(i);
			};
		}
        System.out.println("No se encontro AccountID="+accountID+"\n");
		return null;
	}

//TRANSACTIONS
    public static boolean transactionT(MyTransactor a, MyTransactor b) 
            throws KeyException,BadAmount,UserException, RemoteException{

        //ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();
        System.out.println("Owner of a obj es UserID= "+a.getOwner().getUserId());
        System.out.println("Owner of b obj es UserID= "+b.getOwner().getUserId());
        
        Key t1 = new Key();
        b.join(t1); //lock
        float bal = b.balance(t1);//220
        b.setBalance(t1, bal*1.1f,b.getOwner());//242
        a.join(t1);
        a.withdraw(t1, bal*0.1f,a.getOwner());

        // UserException nuevo;
        // try{
        //     a.withdraw(t1, bal*0.1f,a.getOwner());
        // }catch(UserException e){
        //     a.abort(t1);
        // }

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){}

        if( a.canCommit(t1) && b.canCommit(t1) ){
            a.commit(t1);
            b.commit(t1);
            return true;
        }
        else {
            
            a.abort(t1);
            b.abort(t1);
            return false;
        }         
    }
    //UserInterface parameter
    public static boolean transactionU(MyTransactor b, MyTransactor c) 
            throws KeyException,BadAmount, UserException, RemoteException{

        //ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();
        System.out.println("Owner of b obj es UserID= "+b.getOwner().getUserId());
        System.out.println("Owner of c obj es UserID= "+c.getOwner().getUserId());
        
        Key u1 = new Key();
        b.join(u1);
        
        float balance = b.balance(u1);//200
        b.setBalance(u1, balance*1.1f,b.getOwner()); //220    
        c.join(u1);           

        c.withdraw(u1, balance*0.1f,c.getOwner());//20
        //UserIntercae remoteUser = bankB.getObject("bank2").login(1)
        //c.withdraw(u1, balance*0.1f, remoteUser);//20

        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){}
        if( b.canCommit(u1) && c.canCommit(u1) ){
            c.commit(u1);
            b.commit(u1);
            return true;
        }
        else {
            c.abort(u1);
            b.abort(u1);
            return false;
        } 
        
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
    public ArrayList<MyTransactor> getAccounts(){
        return lista;
    }

    public static void main(String [] args) 
        throws RemoteException, NotBoundException, 
        InterruptedException, BadAmount, KeyException
    {
        UserInterface user = new User("B1-User");
        MyTransactor accountB = new MyBankAccount("B001",user,200f);

        BankB bankB = new BankB();
        bankB.startServer("192.168.2.21", 1092);
        try{
            // bankB.bankBaseObject.getUserList().clear();
            // bankB.bankBaseObject.getAccounts().clear();
            bankB.bankBaseObject.addBankAccount(accountB);
            bankB.bankBaseObject.addUser(user);
        }catch(Exception e){
            e.printStackTrace();
        }

        
        Thread.sleep(10000);
        bankB.assignServer("192.168.2.21", 1091, "192.168.2.21", 1093);
        
        //----------------------------------------------------------------s
        System.out.println("Llamada Simple: ");
		
        // MyTransactor a=null,b=null,c=null;
        
        MyTransactor b = bankB.getObject("local").browse("B001");
        MyTransactor a = bankB.getObject("bank1").browse("A001"); //A
        MyTransactor c = bankB.getObject("bank2").browse("C001"); //C
        UserInterface userC = bankB.getObject("bank2").login("C1-User");
        System.out.println("User ID**** = "+userC.getUserId());
        //----------------------------------------------------------------
        // UserInterface userOfB = b.getOwner();
        // UserInterface userOfA = a.getOwner();
        // UserInterface userOfC = c.getOwner();
        
        // System.out.println("User ID = "+userOfA.getUserId());
        // System.out.println("User ID = "+userOfB.getUserId());
        // System.out.println("User ID = "+userOfC.getUserId());
        
        //UserInterface remoteUser = bankB.getObject("bank2").login(1) 
        
        // if(a == null || b == null || c == null){
        //     System.out.println("ERROR NULL object in (a,b,c)");
        // }
        if(a == null){ 
            System.out.println("ERROR NULL object A");
            return;
        }
        if(b == null){ 
            System.out.println("ERROR NULL object B");
        }
        if(c == null){ 
            System.out.println("ERROR NULL object C");
        }

        Thread tT = new Thread(new Runnable() {
            public void run()  {
                try{
                    Thread.sleep(1000);
                    System.out.println("=== T Iniciando ===");
                    print(a,b,c);
                    
                                     
                    boolean etT = transactionT(a, b);
                    if (etT == true ) {
                        System.out.println("Transaccion T exitosa");
                    } else {
                        System.out.println("Transaccion T no exitosa");
                    }
                    print(a,b,c); 
                    System.out.println("=== T Finalizando ===");
                    //estado de objs final en tT
                }catch(Exception e){
                    e.printStackTrace();
                }
                
            }
        });
        Thread tU = new Thread(new Runnable() {
            public void run()  {
                try{
                    System.out.println("=== U Iniciando ===");
                    print(a,b,c); 
                    boolean etU =  transactionU(b, c);
                    if (etU == true ) {
                        System.out.println("Transaccion U exitosa");
                    } else {
                        System.out.println("Transaccion U no exitosa");
                    }
                    print(a,b,c); 
                    System.out.println("=== U Finalizando ===");
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        tT.start();
        tU.start();

        try {
            tT.join();
            tU.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        //------------------------
        System.out.println("Balance : " + c.getBalance());

    }
}

/*

	public static void main(String [] args) throws RemoteException, NotBoundException, InterruptedException, BadAmount, KeyException {
		
        startServer("192.168.2.28", 1092);
        
		Thread.sleep(10000);
		assignServer("192.168.2.28", 1091, "192.168.2.28", 1093);
		System.out.println("Llamada Simple: ");
		
		MyTransactor b = bankBaseObject.returnObjectTest();
		MyTransactor a = firstBankRemoteObject.returnObjectTest(); //A
		MyTransactor c = secondBankRemoteObject.returnObjectTest(); //C
		
        Thread tT = new Thread(new Runnable() {
            public void run()  {
                try{
                    System.out.println("=== T Iniciando ===");
                    print(a,b,c);
                    Thread.sleep(1000);
                                     
                    boolean etT = transactionT(a, b);
                    if (etT == true ) {
                        System.out.println("Transaccion T exitosa");
                    } else {
                        System.out.println("Transaccion T no exitosa");
                    }
                    print(a,b,c); 
                    System.out.println("=== T Finalizando ===");
                    //estado de objs final en tT
                }catch(Exception e){
                    e.printStackTrace();
                }
                
            }
        });
   
        Thread tU = new Thread(new Runnable() {
            public void run()  {
                try{
                    System.out.println("=== U Iniciando ===");
                    print(a,b,c); 
                    boolean etU =  transactionU(b, c);
                    if (etU == true ) {
                        System.out.println("Transaccion U exitosa");
                    } else {
                        System.out.println("Transaccion U no exitosa");
                    }
                    print(a,b,c); 
                    System.out.println("=== U Finalizando ===");
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        tT.start();
        tU.start();

        try {
            tT.join();
            tU.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
		
		
		
		
		//startServer("192.168.0.3", 1092);
		//Thread.sleep(10000);
		//assignServer("192.168.0.3", 1091, "192.168.0.3", 1093);
		//Menu();
		
	}

*/