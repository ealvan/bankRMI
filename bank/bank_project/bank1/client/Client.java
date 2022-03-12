package bank1.client;
import java.rmi.registry.Registry;

import shared.*;
import bank1.server.BankA;
import bank1.server.RunServerA;

import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    
    private Bank serverBankB;
    private Bank serverBankA;
    
    public Client() throws RemoteException{
        // startClient();
        startServerBankA();
    }
    public void startClient() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 2022);
        serverBankB = (Bank)registry.lookup("bankB");
    }
    public void startServerBankA() throws RemoteException{
        serverBankA = new BankA(3000);
    }

    public void sayHello(){
        String str = "";
        try{
            str = serverBankB.sayHello("BANK OF AMERICA");
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
            Thread one = new Thread() {
                public void run() {
                    try {
                        RunServerA run = new RunServerA(client.getBank("A"));
                        run.export("bankA", 2022);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }  
            };
            one.start();
            // client.startClient();
            // Thread.sleep(10000);
            Bank bankB = client.getBank("B");
            bankB.shareObjects(client.getBank("A"), null);
            client.sayHello();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
