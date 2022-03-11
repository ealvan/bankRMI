package bank2.client;

import bank2.shared.Bank;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private Bank serverBank;
    public Client(){}
    public Bank getBankAccount(String accountNumber) throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 2022);
        serverBank = (Bank)registry.lookup("bankA");
        return serverBank;
    }

    public static void main(String args[])throws RemoteException,NotBoundException{
        Client client = new Client();
        Bank remoteServerBank = client.getBankAccount("accountNumber");
        String str = remoteServerBank.sayHello("Pepe");
        System.out.println(str+"\n Succesfully remote app!!");
    }
}
