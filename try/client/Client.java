package client;

import intf.Hello;

import java.rmi.Naming;
import java.rmi.RemoteException;
// import java.rmi.RMISecurityManager;

public class Client {

    public static void main(String args[]) {

         // "obj" is the identifier that we'll use to refer
         // to the remote object that implements the "Hello"
         // interface
         Hello obj = null;
         String str = "";
         Hello obj2 = null;
        //  // Create and install a security manager
        //  if (System.getSecurityManager() == null) {
        //      System.setSecurityManager(new RMISecurityManager());

         try {
             String name = "hello", name2 = "hello2";

             obj = (Hello)Naming.lookup(name);
             str = obj.sayHello();
             obj2 = (Hello) Naming.lookup(name2) ;
             System.out.println(obj2 +" - "+obj2.getPerson());
             obj2.getPerson().write();
            //  System.out.println(obj2.sayHello2());
            //  System.out.println(obj2.getPerson().getName());
             System.out.println(str);

         } catch (Exception e) {
             System.out.println("HelloClient exception: " +
                  e.getMessage());
             e.printStackTrace();
         }
    }
}