package it.polimi.ingsw.controller.servercontroller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface AuthenticationManager extends Remote {
    public int register(String username) throws RemoteException;
    public void login(String username, int tempcode) throws RemoteException;
}
