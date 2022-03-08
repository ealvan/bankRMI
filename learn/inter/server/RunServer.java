package server;
import shared.UpperCaseServer;
import java.rmi.registry.LocateRegistry;
import java.nio.channels.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;

public class RunServer{
    public static void main(String args[]) throws RemoteException, NotBoundException,AlreadyBoundException{
        UpperCaseServer server = new RMIServerImpl();
        Registry registry = LocateRegistry.createRegistry(2022);
        try{
            registry.bind("server",server);

        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Server Started!");    
    }
}