package client;

import shared.UpperCaseServer;
import shared.UpperCaseClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient implements UpperCaseClient{
    private UpperCaseServer server;
    public RMIClient() throws RemoteException{
        UnicastRemoteObject.exportObject((UpperCaseClient)this, 2019);
        // exportClient();
    }
    public void startClient()  throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 2022);
        server = (UpperCaseServer)registry.lookup("server");
    }
    // public void exportClient() throws RemoteException{
    //     Registry registry = LocateRegistry.getRegistry("localhost",2020);
    //     registry.rebind("client",this);
    // }
    public void toUpperCase(String str){
        try{
            server.toUpperCase(str,(UpperCaseClient)this);
        }catch(Exception e){
            System.out.println("Exception error: To Upper Case remote method failed!!!");
        }
    }
    public void upperCaseResult(String str){
        System.out.println("Result > "+str);
    }
}