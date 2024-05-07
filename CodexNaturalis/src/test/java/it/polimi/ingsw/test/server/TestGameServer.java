package it.polimi.ingsw.test.server;

import it.polimi.ingsw.controller.servercontroller.RMIGameManager;
import it.polimi.ingsw.controller.servercontroller.RMIGameManagerImpl;
import it.polimi.ingsw.controller.servercontroller.SetupState;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.networking.RMIServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class TestGameServer {
    public static void main(String[] args) throws RemoteException {
        System.out.println("Building server");
        Game game = new Game(List.of("Steve", "Giovanni", "Luca"));
        RMIGameManagerImpl gameManager = new RMIGameManagerImpl("Steve", game);
        
        LocateRegistry.createRegistry(1099);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind("Game server", gameManager);

        System.out.println("Game server ready");
    }
}