package it.polimi.ingsw;

import it.polimi.ingsw.networking.RMIController;

import javax.naming.NamingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppClient {
    public static void main(String[] args)throws NamingException, RemoteException, NotBoundException {

        Registry registry= LocateRegistry.getRegistry();
        System.out.print("RMI registry bindings: ");
        String[] e = registry.list();
        for (String s : e) System.out.println(s);
        String remoteObjectName = "Codex_Naturalis_server";
        RMIController testController = (RMIController)  registry.lookup(remoteObjectName);

        testController.addLobby("Luca","Prima Lobby", 4);
        System.out.println(testController.getLobbiesNames());
    }
}
