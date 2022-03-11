import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class MyBankAccount2 extends UnicastRemoteObject implements MyTransactor{


	private float balance = 0.0f;
    private float workingBalance = 0.0f;    
    private Object currentKey = null;    
    private Key asd = null;

    protected MyBankAccount2() throws RemoteException {
        super();
    }
    
    public void setValue() {
    	balance = 10.00f;
    }
    
    public synchronized float balance(Object key) 
      throws KeyException, RemoteException
    {
    	Key a = (Key) key;
    	int dato = a.getId();
    	Key b = (Key) currentKey;
    	int dato2 = b.getId();
    	
    	
        if(  dato != dato2 )
            throw new KeyException();
 
        return workingBalance;
    }

    public synchronized void deposit(Object key, float amount)
      throws BadAmount, KeyException, RemoteException
    {
        if( key != currentKey )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance += amount;
    }

    public synchronized void setBalance(Object key, float amount)
      throws BadAmount, KeyException, RemoteException
    {
        if( key != currentKey )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance = amount;
    }

    public synchronized void withdraw(Object key, float amount)
      throws BadAmount, KeyException, RemoteException
    {
        deposit(key, -amount);
    }//

    public void join(Object key) 
      throws KeyException, RemoteException
    {
    	System.out.println(key);
        if( currentKey != null ){
          int i = 15; //Intentos de preguntar
          boolean tiene = true;
          while (tiene && i-- > 0) { //Vuelvo a preguntar por 1 segundo
            if( currentKey != null ){
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

        currentKey = key;    
        workingBalance = balance;          
       
    }

    public boolean canCommit(Object key)
      throws KeyException, RemoteException
    {
        return( key == currentKey);
    }

    public void commit(Object key)
      throws KeyException, RemoteException
    {
        if( key != currentKey )
            throw new KeyException();
 
        balance = workingBalance;    
        currentKey = null;  
    }

    public void abort(Object key)
      throws KeyException, RemoteException
    {
        //file con el anterior balance   
        currentKey = null;    
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
            aux.setValue();
            a.bind("Asd", aux);     
            
            System.err.println("Server 2 ready");
 
        } catch (Exception e) {
 
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
 
        }
    }

}
