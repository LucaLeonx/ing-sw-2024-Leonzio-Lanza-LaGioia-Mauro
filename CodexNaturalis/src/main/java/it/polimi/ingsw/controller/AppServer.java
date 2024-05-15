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
        System.out.println("Building server");

        List<Game> gameList = Collections.synchronizedList(new LinkedList<>());
        UserList userList = new UserList();
        LobbyList lobbyList = new LobbyList();

        ExecutionLayer executionLayer = new ExecutionLayer(lobbyList, userList, gameList);
        ConversionLayer conversionLayer = new ConversionLayer(lobbyList, executionLayer);
        IntegrityLayer integrityLayer = new IntegrityLayer(lobbyList, conversionLayer);

        AuthenticationManager authenticator = new AuthenticationManagerImpl(integrityLayer, userList);
        // MessageTranslator translator = MessageTranslator.init(integrityLayer);

        LocateRegistry.createRegistry(ConnectionDefaultSettings.RMIRegistryPort);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind(ConnectionDefaultSettings.RMIServerName, authenticator);
        System.out.println("Registry bound, ready to listen for clients from RMI");

        SocketServer server = new SocketServer(ConnectionDefaultSettings.SocketServerPort,authenticator);
        server.startServer();
        System.out.println("Game server ready");

    }
}
