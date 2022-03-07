package server;

import intf.Hello;
import intf.Person;

import java.rmi.Naming;
import java.rmi.RemoteException;
// import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello {

    String name;
    Person person;

  public HelloImpl() throws RemoteException {
       super();
  }
  public HelloImpl(String name) throws RemoteException {
    super();
    person = new PersonImpl(name);
    this.name = "Mi chica bella";
  }
  public String sayHello() {
        return  "Hello World: I am your remote object!";
  }
  public String sayHello2(){
      return name;
  }
  public Person getPerson(){
      return person;
  }
  public static void main(String args[]) {

    try {
         Hello obj = new HelloImpl();
         Hello obj2 = new HelloImpl("PEPE!!");
         
         // Bind this object instance to the name "HelloServer"
         Naming.rebind("hello", obj);
         Naming.rebind("hello2", obj2);
         
         System.out.println("HelloServer bound in registry");
    } catch (Exception e) {
           System.out.println("HelloImpl err: " + e.getMessage());
           e.printStackTrace();
    }
  }
 }