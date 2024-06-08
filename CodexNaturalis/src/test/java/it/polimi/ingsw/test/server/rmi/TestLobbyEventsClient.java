package it.polimi.ingsw.test.server.rmi;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
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

        int tempCode = controller.register("LobbyObserver");
        controller.login("LobbyObserver", tempCode);
        System.out.println("Lobby updates");
    }
}

