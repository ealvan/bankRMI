package server;

import shared.UpperCaseServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerImpl implements UpperCaseServer{

    public RMIServerImpl() throws RemoteException{
        UnicastRemoteObject.exportObject(this, 2022);
    }
    public String toUpperCase(String str){
        return str.toUpperCase();
    }
}