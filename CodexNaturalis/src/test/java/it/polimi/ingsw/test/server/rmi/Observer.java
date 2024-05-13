package it.polimi.ingsw.test.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Observer extends Remote {
    public void onEvent(Payload payload) throws RemoteException;
}
