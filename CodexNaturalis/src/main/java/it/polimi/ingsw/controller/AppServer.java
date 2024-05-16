package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.clientcontroller.ConnectionDefaultSettings;
import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.controller.socket.SocketServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AppServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        System.out.println("CODEX NATURALIS - SERVER");

        UserList userList = new UserList();
        LobbyList lobbyList = new LobbyList();
        GameList gameList = new GameList();

        CoreServer server = new CoreServer(userList, lobbyList, gameList);
        AuthenticationManager authenticator = new AuthenticationManagerImpl(server, userList);

        LocateRegistry.createRegistry(ConnectionDefaultSettings.RMIRegistryPort);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind(ConnectionDefaultSettings.RMIServerName, authenticator);
        System.out.println("Registry bound, ready to listen for clients from RMI");

        SocketServer socketServer = new SocketServer(ConnectionDefaultSettings.SocketServerPort,authenticator);
        socketServer.startServer();
        System.out.println("Game server ready");
    }
}
