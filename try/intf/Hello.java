package intf;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Hello extends Remote {
    String sayHello() throws RemoteException;
    String sayHello2() throws RemoteException;
    Person getPerson() throws RemoteException;
}

