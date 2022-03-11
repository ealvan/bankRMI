
public class AccountUser
{
    public boolean transfer(float amount, MyBankAccount src, MyBankAccount dst)
    {
        Object key = new Object();
        //dadsdsadsadas
        try{
            src.join(key);
            dst.join(key);



        }catch(Exception e){
            System.out.println("Error: in join() operation");
        }
        try{
            src.withdraw(key,amount);
            dst.deposit(key,amount);    
        }catch(Exception e){
            System.out.println("Error: in withdraw() or deposit() operation");
        }
        try{
            if( src.canCommit(key) && dst.canCommit(key) ){
                src.commit(key);
                dst.commit(key);
                return true;
            }
            else {
                src.abort(key);
                dst.abort(key);
                return false;
            }    
        }catch(Exception e){
            System.out.println("Error: commit() or abort()");            
            e.printStackTrace();
        }
        return false;
    }

    public static boolean transactionT(MyBankAccount a, MyBankAccount b) throws KeyException,BadAmount{
        
        Object t1 = new Object();
        b.join(t1); //lock
        float bal = b.balance(t1);//220
        b.setBalance(t1, new Float(bal*1.1));//242
        a.join(t1);
        a.withdraw(t1, new Float(bal*0.1));
        
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
    
    public static boolean transactionU(MyBankAccount b, MyBankAccount c)throws KeyException,BadAmount{
        Object u1 = new Object();

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





    public static void print(MyBankAccount a,MyBankAccount b,MyBankAccount c){
        System.out.println(" Valores en orden\nA: " + a + " B: " + b + " C: " + c);
    }

    public static void main(String args[]) throws KeyException,BadAmount {
        MyBankAccount a = new MyBankAccount(100);
        MyBankAccount b = new MyBankAccount(200);
        MyBankAccount c = new MyBankAccount(300);
        
        
            Thread tT = new Thread(new Runnable() {
                public void run()  {
                    try{
                        System.out.println("=== T Iniciando ===");
                        AccountUser.print(a,b,c);   
                                         
                        boolean etT = transactionT(a, b);
                        if (etT == true ) {
                            System.out.println("Transaccion T exitosa");
                        } else {
                            System.out.println("Transaccion T no exitosa");
                        }
                        AccountUser.print(a,b,c); 
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
                        AccountUser.print(a,b,c); 
                        boolean etU =  transactionU(b, c);
                        if (etU == true ) {
                            System.out.println("Transaccion U exitosa");
                        } else {
                            System.out.println("Transaccion U no exitosa");
                        }
                        AccountUser.print(a,b,c); 
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