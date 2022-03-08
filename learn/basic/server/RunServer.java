package server;
import shared.UpperCaseServer;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.*;

public class RunServer{
    public static void main(String args[]){
        try{
            UpperCaseServer server = new RMIServerImpl();
            Registry registry = LocateRegistry.createRegistry(2022);
            registry.bind("server",server);
            System.out.println("Server Started!");    
        }catch(Exception e){
            System.out.println("Error in run server");
        }
    }
}