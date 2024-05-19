package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.rmi.RemoteException;
import java.util.Scanner;

public class JoinLobby extends TUIScreen {
    public JoinLobby(TUI tui, Scanner scanner, ClientController controller){
        super(tui, scanner, controller);
    }

    @Override
    public synchronized void display() {
        int choice = 0;
        String stringChoice;
        try {
            if (controller.getLobbyList().size() > 0) {
                for (int i = 0; i < controller.getLobbyList().size(); i++) {
                    System.out.println(i + 1 + ". " + controller.getLobbyList().get(i));
                }
                while(true) {
                    System.out.print("Select the lobby you want to join or 0 to go back: ");
                    stringChoice = scanner.nextLine().trim();
                    try{
                        choice=Integer.parseInt(stringChoice);
                        break;
                    }
                    catch(NumberFormatException e){
                        System.out.println("please enter a number\n");
                    }
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
                System.out.println("\n No lobby present right now we are sorry, try creating one by yourself");
                transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
            }
        }
        catch(RemoteException e) {
            System.out.println("We are sorry something went wrong but it is not your fault");
            System.out.println(e.getMessage());
            transitionState(new InitialScreen(tui, scanner, controller));
        }
      //  transitionState(new LobbyWaiting(tui, scanner, controller));
    }
}
