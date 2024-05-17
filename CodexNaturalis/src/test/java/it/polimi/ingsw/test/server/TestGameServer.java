package it.polimi.ingsw.test.server;

import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.model.Game;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class TestGameServer {
    public static void main(String[] args) throws RemoteException {

        UserList userList = new UserList();
        LobbyList lobbyList = new LobbyList();
        GameList gameList = new GameList();

        CoreServer server = new CoreServer(userList, lobbyList, gameList);
        AuthenticationManager authenticator = server;

        LocateRegistry.createRegistry(1099);
        Registry reg = LocateRegistry.getRegistry();
        reg.rebind("codex_naturalis_server", authenticator);

        System.out.println("Game server ready");
    }
}
