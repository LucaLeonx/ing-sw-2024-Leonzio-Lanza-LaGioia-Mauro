package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.util.List;

public class TestLobbyEventsClient{

    public static void main(String args[]) throws RemoteException {
        ClientController controller = null;
        try {
            controller = new RMIClientController();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Updater updater = new Updater();
        controller.subscribeToNotifications(updater);
        int tempCode = controller.register("LobbyObserver");
        controller.login("LobbyObserver", tempCode);
        System.out.println("Lobby updates");
    }
}

class Updater implements ClientNotificationSubscription {

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

    @Override
    public void onGameStarted() {

    }
}
