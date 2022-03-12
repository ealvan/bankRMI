package bank2;

import shared.MyTransactor;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class BankB extends UnicastRemoteObject implements BankInterface{
	private ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();

    private Registry baseServer;
	private Registry firstRemoteServer;
	private Registry secondRemoteServer;
	
	private Bank bankBaseObject;
	private BankInterface firstBankRemoteObject;
	private BankInterface secondBankRemoteObject;


    public BankB() throws RemoteException {
		super();
    }
 
	
    public void startServer(String ip, int port) {
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

    public void assignServer(String firstServerIP, int firstPort, String secondServerIP, int secondPort) throws RemoteException, NotBoundException {
		MyTransactor ad;
		firstRemoteServer = LocateRegistry.getRegistry(firstServerIP,firstPort);
		secondRemoteServer = LocateRegistry.getRegistry(secondServerIP,secondPort);
		firstBankRemoteObject = (BankInterface) firstRemoteServer.lookup("Bank");
		secondBankRemoteObject = (BankInterface) secondRemoteServer.lookup("Bank");
		System.err.println("Se completo carga de Bancos");
		System.out.println(bankBaseObject);
		System.out.println(firstBankRemoteObject);
		System.out.println(secondBankRemoteObject);
		//MyTransactor a = firstBankRemoteObject.getObject();
		
	}

    public void addBankAccount(MyTransactor transactor){
        if(transactor.getAccountID() != null)
            lista.add(transactor);
        throw new Exception("Transactor no tiene ID, no se le puede agregar al BANCO A");
    }
    public static void print(MyTransactor a,MyTransactor b,MyTransactor c) throws RemoteException, KeyException{
        System.out.println(" Valores en orden\nA: " + a.getBalance() + " B: " + b.getBalance() + " C: " + c.getBalance());
    }
//--------------------------IMPLEMENTS------------------------------------------------
    //retorna una Cuenta del Banco A
    @Override
	public MyTransactor browse(String accountID) throws RemoteException {
		for (int i = 0; i<lista.size(); i++) {
			if(lista.get(i).getAccountID().equals(accountID)) {
				return lista.get(i);
			};
		}
		return null;
	}

//TRANSACTIONS
    public static boolean transactionT(MyTransactor a, MyTransactor b) 
            throws KeyException,BadAmount, RemoteException{

        //ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();
        
        Key t1 = new Key();
        b.join(t1); //lock
        float bal = b.balance(t1);//220
        b.setBalance(t1, bal*1.1f);//242
        a.join(t1);
        a.withdraw(t1, bal*0.1f);
        
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

    public static boolean transactionU(MyTransactor b, MyTransactor c) throws KeyException,BadAmount, RemoteException{

        //ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();
        
        Key u1 = new Key();
        b.join(u1);
        
        float balance = b.balance(u1);//200
        b.setBalance(u1, balance*1.1f); //220    
        c.join(u1);           
        c.withdraw(u1, balance*0.1f);//20

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
    public Bank getObject(String name){
        if(name.toLowerCase() == "local"){
            return bankBaseObject;
        }else if (name.toLowerCase() == "bank1"){
            return firstBankRemoteObject;
        }else if (name.toLowerCase() == "bank2"){
            return secondBankRemoteObject;            
        }
        throw new Exception("El objeto "+name+ " no existe....");
    }
    public static void main(String [] args) 
        throws RemoteException, NotBoundException, InterruptedException, BadAmount, KeyException 
    {
        Transactor accountB = new MyBankAccount("B001",200);

        BankB bankB = new BankB();
        bankB.addBankAccount(accountB);

        bankB.startServer("192.168.2.28", 1092);
        Thread.sleep(10000);
        bankB.assignServer("192.168.2.28", 1091, "192.168.2.28", 1093);
        
        //----------------------------------------------------------------s
        System.out.println("Llamada Simple: ");
		
        
        MyTransactor b = bankB.getObject("local").browse("B001");
		MyTransactor a = bankB.getObject("bank1").browse("A001"); //A
		MyTransactor c = bankB.getObject("bank2").browse("C001"); //C
        if(a == null || b == null || c == null){
            throw new Exception("ERROR NULL object in (a,b,c)");
        }
        
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