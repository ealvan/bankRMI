package bank1.server;

import bank1.shared.Bank;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.*;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunServerA {

    public static void main(String args[])throws RemoteException, NotBoundException,AlreadyBoundException{
        Bank bankA = new BankA(2000);
        Registry registry = LocateRegistry.createRegistry(2054);

        try{
            registry.bind("bankA",bankA);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Server Bank A Started!");    
    }
}
