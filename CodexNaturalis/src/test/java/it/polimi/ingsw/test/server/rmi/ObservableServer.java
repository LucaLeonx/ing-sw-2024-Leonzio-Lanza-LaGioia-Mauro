package it.polimi.ingsw.test.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObservableServer extends Remote {

    public void changeValue(int newValue) throws RemoteException;

    public int getValue() throws RemoteException;

    public void subscribeToUpdates(Observer observerReference) throws RemoteException;

    // Not needed for now
    // public void unsubscribeToUpdates(Observer observerReference);
}
