class MyBankAccount implements MyTransactor{

    private float balance = 0.0f;
    private float workingBalance = 0.0f;    
    private Object currentKey = null;    

    public MyBankAccount(float initBalance){
        balance = initBalance;
    }
    public synchronized float balance(Object key)
      throws KeyException
    {
        if( key != currentKey )
            throw new KeyException();
 
        return workingBalance;
    }

    public synchronized void deposit(Object key, float amount)
      throws BadAmount, KeyException
    {
        if( key != currentKey )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance += amount;
    }

    public synchronized void setBalance(Object key, float amount)
      throws BadAmount, KeyException
    {
        if( key != currentKey )
            throw new KeyException();

        if( workingBalance < -amount )
            throw new BadAmount();
 
        workingBalance = amount;
    }

    public synchronized void withdraw(Object key, float amount)
      throws BadAmount, KeyException
    {
        deposit(key, -amount);
    }//

    public void join(Object key)
      throws KeyException
    {
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
      throws KeyException
    {
        return( key == currentKey );
    }

    public void commit(Object key)
      throws KeyException
    {
        if( key != currentKey )
            throw new KeyException();
 
        balance = workingBalance;    
        currentKey = null;  
    }

    public void abort(Object key)
      throws KeyException
    {
        //file con el anterior balance   
        currentKey = null;    
        // System.out.println("Abortado");
    }
    public String toString(){
      return "balance : "+ balance;
    }

}
