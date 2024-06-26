package it.polimi.ingsw;

import it.polimi.ingsw.controller.ConnectionSettings;
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

        ConnectionSettings connectionSettings = ConnectionSettings.parseConnectionSettings(args);

        System.out.println("Connection settings: ");
        System.out.println("RMI server name: " + connectionSettings.getRMIServerName());
        System.out.println("RMI port:        " + connectionSettings.getRMIPort());
        System.out.println("Socket port:     " + connectionSettings.getSocketPort());

        TimedServer server = new TimedServer(userList, lobbyList, gameList);

        LocateRegistry.createRegistry(connectionSettings.getRMIPort());
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind(connectionSettings.getRMIServerName(), (AuthenticationManager) server);
        System.out.println("Registry bound, ready to listen for clients from RMI");

        SocketServer socketServer = new SocketServer(connectionSettings.getSocketPort(), server);
        socketServer.startServer();
        System.out.println("Game server ready");
    }


}
