package client;
import shared.UpperCaseServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIClient{
    private UpperCaseServer server;
    public RMIClient(){
    }
    public void startClient()  throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry("localhost", 2022);
        server = (UpperCaseServer)registry.lookup("server");
    }
    public String toUpperCase(String str){
        String str2 ="";
        try{
            str2 = server.toUpperCase(str);
        }catch(Exception e){
            System.out.println("Exception error: To Upper Case remote method failed!!!");
        }
        return str2;
    }
}