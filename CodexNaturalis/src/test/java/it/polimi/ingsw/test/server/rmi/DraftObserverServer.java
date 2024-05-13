package it.polimi.ingsw.test.server.rmi;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DraftObserverServer extends UnicastRemoteObject implements ObservableServer  {

    private final AtomicInteger value = new AtomicInteger(0);
    private final List<Observer> observerList = new ArrayList<>();

    public DraftObserverServer() throws RemoteException {
        super();
    }

    @Override
    public void changeValue(int newValue) throws RemoteException {
        value.set(newValue);
        for(Observer observer : observerList){
            observer.onEvent(new Payload("Value updated"));
        }
    }

    @Override
    public int getValue() {
        return value.get();
    }

    @Override
    public void subscribeToUpdates(Observer observerReference) {
        observerList.add(observerReference);
    }

    public static void main(String[] args){
        try {
            ObservableServer observableServer = new DraftObserverServer();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("observable_server",observableServer);
            System.out.println("Observer server ready");
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }


}