package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.LobbyObserver;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.sql.ClientInfoStatus;
import java.util.List;

public class TestLobbyEventsClient implements LobbyObserver {

    public static void main(String args[]) throws RemoteException {
        ClientController controller = null;
        try {
            controller = new RMIClientController();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LobbyUpdater updater = new LobbyUpdater();
        controller.subscribeToLobbyUpdates(updater);
        int tempCode = controller.register("LobbyObserver");
        controller.login("LobbyObserver", tempCode);
        System.out.println("Lobby updates");
    }

    @Override
    public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        lobbies.stream().forEach(System.out::println);
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        return;
    }


}

class LobbyUpdater implements LobbyObserver{

    private final int id = 10;

    int getId(){
        return id;
    }


    @Override
    public void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        lobbies.stream().forEach(System.out::println);
        System.out.println();
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        return;
    }
}
