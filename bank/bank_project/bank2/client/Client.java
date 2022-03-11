package bank2.client;

import java.rmi.registry.Registry;

import shared.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private Bank serverBank;
    public Client(){
        // startClient();
    }
    public void startClient() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 2022);
        serverBank = (Bank)registry.lookup("bankA");
    }
    public void sayHello(){
        String str = "";
        try{
        str = serverBank.sayHello("BANK OF AMERICA");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(str);
    }

    public static void main(String args[])throws RemoteException,NotBoundException{
        try{
            Client client = new Client();
            client.startClient();
            client.sayHello();
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
