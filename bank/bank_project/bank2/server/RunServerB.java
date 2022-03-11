package bank2.server;

import shared.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.*;

import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunServerB {

    public static void main(String args[])
            throws RemoteException, NotBoundException,AlreadyBoundException{
        // Bank bankB = new BankB(2000);
        // Registry registry = LocateRegistry.getRegistry();

        // try{
        //     registry.bind("bankB",bankB);
        // }catch(Exception e){
        //     e.printStackTrace();
        // }

        System.out.println("Server Bank B Started!");
    }
}
