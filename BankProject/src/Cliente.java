import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;


public class Cliente {
    private static MyTransactor a;
    private static MyTransactor b;
 
    public static void main (String[] args) throws MalformedURLException, RemoteException, NotBoundException, KeyException, BadAmount {
        Registry reg_host = LocateRegistry.getRegistry("192.168.0.3",1091);
        Registry reg_host2 = LocateRegistry.getRegistry("192.168.0.3",1092);

        a = (MyTransactor) reg_host.lookup("Asd");
        b = (MyTransactor) reg_host2.lookup("Asd");
        
        System.out.println("hola");
        System.out.println(a);
        System.out.println(b);
        
        KeyInterface t1 = new Key();
        b.join(t1);
        float bal = b.balance(t1);
        b.setBalance(t1,  bal*1.1f);
        a.join(t1);
        a.withdraw(t1, bal*0.1f);
        

    }
}
