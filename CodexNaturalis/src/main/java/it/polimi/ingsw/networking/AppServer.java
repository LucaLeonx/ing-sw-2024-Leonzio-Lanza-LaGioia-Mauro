package it.polimi.ingsw.networking;

import it.polimi.ingsw.networking.RMI.RMIServerController;
import it.polimi.ingsw.networking.socket.SocketServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        System.out.println("Building server");

        RMIServerController rmiServer = new RMIServerController();

        LocateRegistry.createRegistry(1099);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind("Codex_Naturalis_server", rmiServer);

        System.out.println("Registry bound, ready to listen for clients from RMI");

        SocketServer server = new SocketServer(6660);
        server.startServer();

    }
}
