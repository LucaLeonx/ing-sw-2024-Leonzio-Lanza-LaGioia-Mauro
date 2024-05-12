package it.polimi.ingsw.controller.servercontroller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationManager extends Remote {
    public int register(String username) throws RemoteException;
    public AuthenticatedSession login(String username, int tempCode) throws RemoteException;
}
