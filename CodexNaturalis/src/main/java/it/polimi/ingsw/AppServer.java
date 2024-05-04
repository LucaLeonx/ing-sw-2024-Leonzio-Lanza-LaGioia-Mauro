package it.polimi.ingsw;

import it.polimi.ingsw.networking.RMIServer;
import it.polimi.ingsw.networking.RMIController;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServer {
    public static void main(String[] args)throws RemoteException, AlreadyBoundException {
        System.out.println("Building server");

        RMIServer rmiServer = new RMIServer();

        LocateRegistry.createRegistry(1099);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind("Codex_Naturalis_server",reg);

        System.out.println("Registry bound, ready to listen for clients");
    }
}
