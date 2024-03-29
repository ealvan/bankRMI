package bank1;


import shared.*;

import java.rmi.NotBoundException;
// import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class BankA extends UnicastRemoteObject implements BankInterface{
	private ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();
	// private ArrayList<Account> userList = new ArrayList<Account>();

    private Registry baseServer;
	private Registry firstRemoteServer;
	private Registry secondRemoteServer;
	
	private BankA bankBaseObject;
	private BankInterface firstBankRemoteObject;
	private BankInterface secondBankRemoteObject;


    public BankA() throws RemoteException {
		super();
    }
 
	
    public void startServer(String ip, int port) {
        try {
            System.setProperty("java.rmi.server.hostname",ip);
            baseServer = LocateRegistry.createRegistry(port);
            this.bankBaseObject = new BankA();
            baseServer.bind("Bank", this.bankBaseObject);      
            System.err.println("Servidor A esta habilitado");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}

    public void assignServer(String firstServerIP, int firstPort, String secondServerIP, int secondPort) throws RemoteException, NotBoundException {
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

    public void addBankAccount(MyTransactor transactor) throws RemoteException{
        if(transactor.getAccountID() != null){
            System.out.println("Transactor AccountID agregado: "+transactor.getAccountID());
            this.lista.add(transactor);
            return;
        }

        try{
            throw new Exception("Transactor no tiene ID, no se le puede agregar al BANCO A");
        }catch(Exception e){
            e.printStackTrace();
        }
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
                System.out.println("Se encontro!! en browse A AccountID=" +this.lista.get(i).getAccountID()+"\n");
				return lista.get(i);
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
    public static void main(String args[])throws RemoteException, NotBoundException, InterruptedException, BadAmount, KeyException {
        MyTransactor testAccount = new MyBankAccount("A001",100f);        
        
        BankA bankA = new BankA();
        bankA.startServer("192.168.2.28", 1091);

        //add remote object transactor to account list objects
        bankA.bankBaseObject.addBankAccount(testAccount);


        Thread.sleep(10000);

        bankA.assignServer("192.168.2.28", 1092, "192.168.2.28", 1093);

        // Thread.sleep(7000);
        MyTransactor accountC = bankA.getObject("bank2").browse("C001");
        Key key = new Key();
        accountC.join(key);
        accountC.deposit(key, 80);
        if(accountC.canCommit(key)){
            accountC.commit(key);
        }else{
            accountC.abort(key);
        }
        System.out.println("Balance: "+accountC.getBalance());


    }

}
// 	public static void maissn(String [] args) {
		
//         startServer("192.168.2.28", 1091);
        
// 		Thread.sleep(10000);
// 		assignServer("192.168.2.28", 1092, "192.168.2.28", 1093);
// //		Registry reg_host2 = LocateRegistry.getRegistry("192.168.0.3",1092);
// //		BankInterface  b = (BankInterface) reg_host2.lookup("Asd");
// //		BankInterface x = b.getObject();
// //        System.out.println(x);
// //        MyTransactor y =  b.returnObjectTest();
// //        System.out.println("El objeto Cuenta: ");
// //        System.out.println(y);
		
		
// 	}
