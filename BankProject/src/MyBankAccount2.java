import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;



public class MyBankAccount2 extends UnicastRemoteObject implements AccountInterface{


	private float balance = 0.0f;
    private float workingBalance = 0.0f;    
    private KeyInterface KeyValue = null;
    private String accountID;

    protected MyBankAccount2() throws RemoteException {
        super();
    }
    
    public void setValue(float a) 
      throws RemoteException
    {
    	balance = a;
    }
    
    public synchronized float balance(KeyInterface key) 
      throws KeyException, RemoteException
    {	
        if(  key.getId() != KeyValue.getId() )
            throw new KeyException();
 
        return workingBalance;
    }

    public synchronized void deposit(KeyInterface key, float amount)
      throws BadAmount, KeyException, RemoteException
    {
        if( key.getId() != KeyValue.getId() )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance += amount;
    }

    public synchronized void setBalance(KeyInterface key, float amount)
      throws BadAmount, KeyException, RemoteException
    {
        if( key.getId() != KeyValue.getId() )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance = amount;
    }

    public synchronized void withdraw(KeyInterface key, float amount)
      throws BadAmount, KeyException, RemoteException
    {
        deposit(key, -amount);
    }//

    public void join(KeyInterface key) 
      throws KeyException, RemoteException
    {
    	System.out.println(key);
        if( KeyValue != null ){
          int i = 15; //Intentos de preguntar
          boolean tiene = true;
          while (tiene && i-- > 0) { //Vuelvo a preguntar por 1 segundo
            if( KeyValue != null ){
              try{
                System.out.println("Esperara 100 ms");
                Thread.sleep(100);
              }catch(InterruptedException e){
                e.printStackTrace();
              }
            }else{
              tiene = false;
            }
           
          }
          if(i <= 0){
            throw new KeyException();
          } 
       }

        KeyValue = key;    
        workingBalance = balance;          
       
    }

    public boolean canCommit(KeyInterface key)
      throws KeyException, RemoteException
    {
        return( key.getId() == KeyValue.getId());
    }

    public void commit(KeyInterface key)
      throws KeyException, RemoteException
    {
        if( key.getId() != KeyValue.getId()  )
            throw new KeyException();
 
        balance = workingBalance;    
        KeyValue = null;  
    }

    public void abort(KeyInterface key)
      throws KeyException, RemoteException
    {
        //file con el anterior balance   
        KeyValue = null;    
        // System.out.println("Abortado");
    }
    public String toString(){
      return "balance : "+ balance;
    }
    
    public static void main(String [] args) {
        try {
            System.setProperty("java.rmi.server.hostname","192.168.0.3");
            Registry a = LocateRegistry.createRegistry(1092);
            MyBankAccount2 aux = new MyBankAccount2();
            aux.setValue(100f);
            a.bind("Asd", aux);     
            
            System.err.println("Server 2  ready");
 
        } catch (Exception e) {
 
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
 
        }
    }

	@Override
	public void tryPass(KeyInterface key) throws RemoteException {
		KeyValue = key;
		System.out.println(KeyValue.getId());
		
	}

	@Override
	public String getID() throws RemoteException {
		return accountID;
		// TODO Auto-generated method stub
		
	}

	@Override
	public AccountInterface getObject() throws RemoteException {
		AccountInterface aux = new MyBankAccount2();
		return aux;
	}
	
	@Override
	public KeyInterface getObjectKey() throws RemoteException {
		KeyInterface ret = new Key();
		return ret;
	}

	@Override
	public float getBalance() throws KeyException, RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}




}
