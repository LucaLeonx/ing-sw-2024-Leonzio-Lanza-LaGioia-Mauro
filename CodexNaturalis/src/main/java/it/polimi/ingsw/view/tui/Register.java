package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Register extends TUIScreen {

    public Register(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        System.out.println("\n-- Register --");
        System.out.print("Choose a username: ");
        String username = scanner.nextLine();
        int code;
        try {
            code = controller.register(username);
            controller.login(username, code);
        }
        catch (RemoteException RE) {
            System.out.println("It seems that your username is already in use, chose");
            while (true) {
                System.out.println("1. Chose another nickname");
                System.out.println("2. Go back");
                int choice = 0;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    scanner.nextLine(); // Consume newline
                    transitionState(new Register(tui, scanner, controller));
                }

                switch (choice) {
                    case 1:
                        transitionState(new Register(tui, scanner, controller));
                        break;
                    case 2:
                        transitionState(new LoginOrRegister(tui, scanner, controller));
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        System.out.println("Hello "+username+ " you secret code is " +code +" please remember it next time you login\n\n");
        transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
    }
}
