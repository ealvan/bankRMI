package bank1.server;

import shared.*;
// import bank1.client.Client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.*;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunServerA {
    Bank exportLocalServerBankA;
    public RunServerA(Bank toExport ){
        exportLocalServerBankA = toExport;
    }
    public void export(String bindName, int port) throws RemoteException,AlreadyBoundException{
        Registry registry = LocateRegistry.createRegistry(port);
        try{
            registry.bind(bindName, exportLocalServerBankA);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Server Bank A Started!");    
    }

    // public static void main(String args[])throws RemoteException, NotBoundException,AlreadyBoundException{
    //     Bank bankA = new BankA(2000);
    //     Registry registry = LocateRegistry.createRegistry(2022);

    //     try{
    //         registry.bind("bankA",bankA);
    //     }catch(Exception e){
    //         e.printStackTrace();
    //     }
    //     System.out.println("Server Bank A Started!");    
    // }
}
