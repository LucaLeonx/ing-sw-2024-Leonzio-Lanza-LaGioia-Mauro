package it.polimi.ingsw;

import it.polimi.ingsw.controller.clientcontroller.ConnectionDefaultSettings;
import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.controller.socket.SocketServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        System.out.println("CODEX NATURALIS - SERVER");

        UserList userList = new UserList();
        LobbyList lobbyList = new LobbyList();
        GameList gameList = new GameList();

        CoreServer server = new CoreServer(userList, lobbyList, gameList);

        LocateRegistry.createRegistry(ConnectionDefaultSettings.RMIRegistryPort);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind(ConnectionDefaultSettings.RMIServerName, (AuthenticationManager) server);
        System.out.println("Registry bound, ready to listen for clients from RMI");

        SocketServer socketServer = new SocketServer(ConnectionDefaultSettings.SocketServerPort, server);
        socketServer.startServer();
        System.out.println("Game server ready");
    }
}
