import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

class MyBankAccount3 extends UnicastRemoteObject implements MyTransactor{


	private float balance = 0.0f;
    private float workingBalance = 0.0f;    
    private KeyInterface KeyValue = null;

    protected MyBankAccount3() throws RemoteException {
        super();
        setValue(500f);
    }
    
    public void setValue(float a) {
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
	@Override
	public void tryPass(KeyInterface key) throws RemoteException {
		KeyValue = key;
		System.out.println(KeyValue.getId());
		
	}

	@Override
	public String getID() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyTransactor getObject() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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


public class Cliente {
 
    public static void main (String[] args) throws MalformedURLException, RemoteException, NotBoundException, KeyException, BadAmount {
        Registry reg_host = LocateRegistry.getRegistry("192.168.0.3",1091);
        Registry reg_host2 = LocateRegistry.getRegistry("192.168.0.3",1092);

        
        
        MyTransactor a = (MyTransactor) reg_host.lookup("Asd");
        MyTransactor  b = (MyTransactor) reg_host2.lookup("Asd");
        MyTransactor aux = new MyBankAccount3();
  
        
        Key t1 = new Key();
        MyTransactor x = a.getObject();
        System.out.println(x);
        a.getObjectKey();
        
        ArrayList<MyTransactor> lista = new ArrayList<MyTransactor>();
        lista.add(a);
        lista.add(b);
        lista.add(aux);
        
        b.join(t1);
        float bal = b.balance(t1);
        b.setBalance(t1,  bal*1.1f);
        a.join(t1);
        a.withdraw(t1, bal*0.1f);
        aux.join(t1);
        aux.setBalance(t1, 150f);
        System.out.println("Valor de a: " + a.balance(t1));
        System.out.println("Valor de b: " + b.balance(t1));
        System.out.println("Valor de c: " + aux.balance(t1));
        
        if( a.canCommit(t1) && b.canCommit(t1) && aux.canCommit(t1) ){
            a.commit(t1);
            b.commit(t1);
            aux.commit(t1);
        }
        else {
            a.abort(t1);
            b.abort(t1);
            aux.abort(t1);
        } 
    }
}
