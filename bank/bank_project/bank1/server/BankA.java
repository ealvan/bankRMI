package bank1.server;
//package bank1.shared;
// import bank1.shared.Bank;
import shared.Transaction;
import shared.BadAmount;
import shared.KeyException;
import shared.Bank;

// import bank1.errors.KeyException;
// import bank1.errors.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class BankA extends UnicastRemoteObject implements Bank {
    private float balance = 0.0f;
    private float workingBalance = 0.0f;    
    private Transaction currentKey = null; 
    private Bank serverBankA;
    private Bank serverBankB;   

    public BankA(float initBalance) throws RemoteException{
        super();
        balance = initBalance;
        // UnicastRemoteObject.exportObject(this,2021);
    }
    //TRANSACTOR ...
    public void join(Transaction key) throws KeyException{
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
    public boolean canCommit(Transaction key) throws KeyException{
        return( key == currentKey );
    }
    public void commit(Transaction key) throws KeyException{
        if( key != currentKey )
            throw new KeyException();
 
        balance = workingBalance;    
        currentKey = null; 
    }
    public void abort(Transaction key) throws KeyException{
        //file con el anterior balance   
        currentKey = null;    
        // System.out.println("Abortado");
    }
    //BANK OPERATIONS...
    public void deposit(Transaction key, float amount) throws BadAmount, KeyException{
        if( key != currentKey )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance += amount;

    }
    public void withdraw(Transaction key, float amount) throws BadAmount, KeyException{
        deposit(key, -amount);
    }
    public float balance(Transaction key) throws KeyException{
        if( key != currentKey )
            throw new KeyException();
 
        return workingBalance;
    }
    public synchronized void setBalance(Transaction key, float amount)
      throws BadAmount, KeyException
    {
        if( key != currentKey )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance = amount;
    }
//------------------------------------------------------------------
    //TEST operations
    public String sayHello(String name){
        String str = "Hello Dear "+name;
        System.out.println(str);
        return str;
    }
//-------------------------------------------------------------------
    //SHARED OBJECTS VIA HELLO
    public void shareObjects(Bank A, Bank B) throws RemoteException{
        serverBankB = A;
        String str = serverBankB.sayHello("BANK B!!");
        System.out.println(str);
        // serverBankA = B;
    }
    public Bank getBank(String str) {
        if(str.toUpperCase() == "A"){
            return serverBankA;
        }if(str.toUpperCase() == "B"){
            return serverBankB;
        }
        return null;
    }

}
