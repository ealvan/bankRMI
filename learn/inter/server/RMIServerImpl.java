package server;

import shared.UpperCaseServer;
import shared.UpperCaseClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerImpl implements UpperCaseServer{

    public RMIServerImpl() throws RemoteException{
        UnicastRemoteObject.exportObject(this,2021);
    }
    public void toUpperCase(String str,UpperCaseClient client){
        String result = str.toUpperCase();
        try{
            Thread.sleep(3000);
        }catch(Exception ignored){
            
        }

        try{
            client.upperCaseResult(result);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}