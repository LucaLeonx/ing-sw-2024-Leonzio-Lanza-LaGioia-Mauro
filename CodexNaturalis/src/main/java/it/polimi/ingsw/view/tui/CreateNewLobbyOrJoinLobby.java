package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.util.Scanner;

public class CreateNewLobbyOrJoinLobby extends TUIState {
    public CreateNewLobbyOrJoinLobby(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        System.out.println("1. Create new lobby ");
        System.out.println("2. Show existing lobby: ");
        System.out.println("3. Logout ");
        System.out.print("Please choose an option: ");
        int choice =0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        switch (choice) {
            case 1:
                transitionState(new CreateLobby(tui, scanner, controller));
                break;
            case 2:
                transitionState(new JoinLobby(tui, scanner, controller));
                break;
            case 3:
                transitionState(new LoginOrRegister(tui, scanner, controller));
            default:
                System.out.println("Invalid choice. Please try again.");
                transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
        }
    }
}
