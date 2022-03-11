package bank2.client;

import java.rmi.registry.Registry;

import shared.*;
import bank2.server.BankB;

import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private Bank serverBankA;
    private Bank serverBankB;

    public Client() throws RemoteException{
        // startClient();
        startServerBankB();
    }

    public void startClient() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 2022);
        serverBankA = (Bank)registry.lookup("bankA");
    }

    public void startServerBankB() throws RemoteException{
        serverBankB = new BankB(3000);
    }

    public void sayHello(){
        String str = "";
        try{
            str = serverBankA.sayHello("BANK OF A!!!");
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(str);
    }

    public Bank getBank(String str){
        if(str.toUpperCase() == "A"){
            return serverBankA;
        }if(str.toUpperCase() == "B"){
            return serverBankB;
        }
        return null;
    }

    public static void main(String args[])throws RemoteException,NotBoundException{
        try{
            Client client = new Client();
            client.startClient();
            client.getBank("A").shareObjects(client.getBank("B"), null);

            client.sayHello();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
