package bank3;

import shared.*;

import java.rmi.NotBoundException;
// import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class BankC extends UnicastRemoteObject implements BankInterface{
	private ArrayList<MyTransactor> lista= new ArrayList<MyTransactor>();
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
	
	private BankC bankBaseObject;
	private BankInterface firstBankRemoteObject;
	private BankInterface secondBankRemoteObject;


    public BankC() throws RemoteException {
		super();
        // lista = new ArrayList<MyTransactor>();
    }
	
    public void startServer(String ip, int port) {
        try {
            System.setProperty("java.rmi.server.hostname",ip);
            baseServer = LocateRegistry.createRegistry(port);
            bankBaseObject = new BankC();
            baseServer.bind("Bank", bankBaseObject);      
            System.err.println("Servidor C esta habilitado");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
	}

    public void assignServer(String firstServerIP, int firstPort, String secondServerIP, int secondPort) 
            throws RemoteException, NotBoundException {
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

    public void addBankAccount(MyTransactor transactor) throws RemoteException{
        if(transactor.getAccountID() != null){
            System.out.println("Nuevo transactor con AccountID agregado AccountID="+transactor.getAccountID());
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
    //retorna una Cuenta del Banco C
    @Override
	public MyTransactor browse(String accountID) throws RemoteException {
        if(this.lista.size() <= 0){
            System.out.println("NO SE AGREGARON MYTRANSATORS EN \"this.lista\"\n");
            return null;
        }
		for (int i = 0; i<this.lista.size(); i++) {
            // System.out.println("ACCOUNT ID : "+this.lista.get(i).getAccountID()+"\n");
			if(this.lista.get(i).getAccountID().equals(accountID)) {
                System.out.println("Se encontro!! en browse C AccountID="  +this.lista.get(i).getAccountID()+"\n");
				return this.lista.get(i);
			};
		}
        System.out.println("No se encontro "+accountID+"\n");
		return null;
	}
    public HashMap<String,UserInterface> getUserList() {
        return userList;
    }
    public ArrayList<MyTransactor> getAccounts(){
        return lista;
    }
    public static void main(String args[])
            throws RemoteException, NotBoundException, InterruptedException, BadAmount, KeyException 
    {
        BankC bankC = new BankC();        
        

        UserInterface user = new User("C1-User");
        MyTransactor testAccount = new MyBankAccount("C001",user,300f);        
        

        bankC.startServer("192.168.2.28", 1093);
        //REMOVE ALL content before started
        bankC.bankBaseObject.getUserList().clear();
        bankC.bankBaseObject.getAccounts().clear();

        //add remote object transactor to account list objects
        bankC.bankBaseObject.addBankAccount(testAccount);
        bankC.bankBaseObject.addUser(user);
        
        Thread.sleep(10000);//"192.168.2.28", 1091, "192.168.2.28", 1092

        bankC.assignServer("192.168.2.28", 1091, "192.168.2.28", 1092);
    }
}