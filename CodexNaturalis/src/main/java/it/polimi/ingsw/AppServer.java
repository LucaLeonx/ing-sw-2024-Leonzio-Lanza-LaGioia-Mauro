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

        CoreServer server = new CoreServer(userList, lobbyList, gameList);

        for(int i = 0; i < args.length; i++){
            if(args[i].equals("-Tshort") || args[i].equals("--timeout-short")){
                TimeoutSettings timeoutSettings = new TimeoutSettings(60, 25, 25, 60);
                server = new TimedServer(userList, lobbyList, gameList, timeoutSettings);
                System.out.println("Using short timeouts settings: " + timeoutSettings);
            } else if(args[i].equals("-Tlong") || args[i].equals("--timeout-long")){
                TimeoutSettings timeoutSettings = new TimeoutSettings(600, 180, 180, 300);
                server = new TimedServer(userList, lobbyList, gameList, timeoutSettings);
                System.out.println("Using long timeouts settings: " + timeoutSettings);
            }
        }

        LocateRegistry.createRegistry(connectionSettings.getRMIPort());
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind(connectionSettings.getRMIServerName(), (AuthenticationManager) server);
        System.out.println("Registry bound, ready to listen for clients from RMI");

        SocketServer socketServer = new SocketServer(connectionSettings.getSocketPort(), server);
        socketServer.startServer();
        System.out.println("Game server ready");
    }


}
