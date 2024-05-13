
package it.polimi.ingsw.networking;

import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.networking.socket.SocketServer;

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

        AuthenticationManager authenticator = getAuthenticationManager();

        LocateRegistry.createRegistry(1099);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind("Codex_Naturalis_server", authenticator);
        System.out.println("Registry bound, ready to listen for clients from RMI");

        SocketServer server = new SocketServer(6660);
        server.startServer();
        System.out.println("Game server ready");

    }

    private static AuthenticationManager getAuthenticationManager() throws RemoteException {
        List<Game> gameList = Collections.synchronizedList(new LinkedList<>());
        UserList userList = new UserList();
        LobbyList lobbyList = new LobbyList();

        ExecutionLayer executionLayer = new ExecutionLayer(lobbyList, userList, gameList);
        ConversionLayer conversionLayer = new ConversionLayer(lobbyList, executionLayer);
        IntegrityLayer integrityLayer = new IntegrityLayer(lobbyList, conversionLayer);

        AuthenticationManager authenticator = new AuthenticationManagerImpl(integrityLayer, userList);
        return authenticator;
    }
}
