package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class DraftObserverClient extends UnicastRemoteObject implements Observer{

    private ObservableServer server;

    public DraftObserverClient(ObservableServer server) throws RemoteException {
        super();
        this.server = server;
    }

    @Override
    public void onEvent(Payload payload) throws RemoteException {
        System.out.println(payload.information());
        System.out.println(getValue());

        System.out.println("Starting some hefty operation...");
        try {
            Thread.sleep(5000); // 5 seconds
        } catch (InterruptedException e){
            System.out.println("Couldn't finish the job");
        }
        System.out.println("Finished");
    }

    public int getValue() throws RemoteException {
        return server.getValue();
    }

    public void setValue(int newValue) throws RemoteException {
        server.changeValue(newValue);
    }

    public void subscribe() throws RemoteException {
        server.subscribeToUpdates(this);
    }

    public static void main(String[] args) throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry();
        ObservableServer server = (ObservableServer) registry.lookup("observable_server");
        DraftObserverClient client = new DraftObserverClient(server);
        client.subscribe();

        System.out.println("Client ready");
        Scanner stdin = new Scanner(System.in);

        boolean reading = true;

        while(reading){
            System.out.println("Perform an action: ");
            System.out.println("1. Read value from server");
            System.out.println("2. Write value to server");
            System.out.println("3. Quit");

            int choice = stdin.nextInt();
            switch (choice){
                case 1 -> System.out.println(client.getValue());
                case 2 -> {
                    System.out.print("Input new value: ");
                    client.setValue(stdin.nextInt());
                }
                case 3 -> {
                    reading = false;
                    System.out.println("Quitting...");
                }
                default -> {
                    System.out.println("Unrecognized value");
                }
            }
        }
    }
}
