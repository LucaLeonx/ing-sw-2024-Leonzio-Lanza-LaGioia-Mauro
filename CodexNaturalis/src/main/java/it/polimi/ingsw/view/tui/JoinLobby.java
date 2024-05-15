package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.LobbyObserver;
import it.polimi.ingsw.dataobject.LobbyInfo;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class JoinLobby extends TUIState implements LobbyObserver {
    public JoinLobby(TUI tui, Scanner scanner, ClientController controller){
        super(tui, scanner, controller);
        controller.subscribeToLobbyUpdates(this);
    }

    @Override
    public synchronized void display() {
        try {
            if (controller.getLobbyList().size() > 0) {
                for (int i = 0; i < controller.getLobbyList().size(); i++) {
                    System.out.println(i + 1 + ". " + controller.getLobbyList().get(i));
                }
                System.out.println("Select the lobby you want to join or 0 to go back");
                int choice = 0;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine(); // Consume newline
                    transitionState(new JoinLobby(tui, scanner, controller));
                }
                if (choice == 0) {
                    transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
                }
                try {
                    controller.joinLobby(controller.getLobbyList().get(choice - 1).id());
                } catch (RemoteException RE) {
                    System.out.println("It seems like the lobby you are trying to enter doesn't exists, make another choice");
                    transitionState(new JoinLobby(tui, scanner, controller));
                }
            } else {
                System.out.println("No lobby present right now we are sorry, try creating one by yourself");
                transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
            }
        }
        catch(RemoteException e) {
            System.out.println("We are sorry something went wrong but it is not your fault");
            System.out.println(e.getMessage());
            transitionState(new InitialScreen(tui, scanner, controller));
        }
    }

    @Override
    public synchronized void onLobbyListUpdate(List<LobbyInfo> lobbies) {
        transitionState(new JoinLobby(tui, scanner, controller));
    }

    @Override
    public void onJoinedLobbyUpdate(LobbyInfo joinedLobby) {
        // nothing
    }
}
