package it.polimi.ingsw.view.tui;
//TODO: try to make people exit from LobbyWaiting if needed.
import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ClientNotificationSubscription;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.util.Scanner;

class LobbyWaiting extends TUIScreen implements ClientNotificationSubscription {

    public LobbyWaiting(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        System.out.println("Waiting for players to join... Press 'q' to quit and go back to the menu");

        // Asking for input from scanner is already a blocking call
        // No need to create an additional thread
        //while(controller.) {
            String input = scanner.nextLine().trim();
            if (input.equals("q")) {
                try {
                    System.out.println("Exiting...");
                    controller.exitFromLobby();
                    transitionState(new JoinLobby(tui, scanner, controller));
                } catch (WrongPhaseException e) {
                    System.out.println("Unable to exit - game already started");
                } catch (RemoteException re) {
                    System.out.println(re.getMessage());
                }
            } else {
                transitionState(this);
            }
        }
    }

    /*@Override
    public synchronized void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        System.out.println(joinedLobby);
    }*/

    /*@Override
    public synchronized void onGameStarted() {
        System.out.println("Number of players reached - Game starting...");
        transitionState(new SetUpGame(tui, scanner, controller));
        this.notifyAll();
    }*/

}
