package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Login extends TUIState{
    public Login(TUI tui, Scanner scanner, ClientController controller){
        super(tui, scanner, controller);
    }
    @Override
    public void display() {
        System.out.println("\n-- Login --");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        System.out.print("Enter the secret code: ");
        int code=0;
        try {
            code = scanner.nextInt();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
        }
        try {
            controller.login(username, code);
        }
        catch (RemoteException RE) {
            System.out.println("It seems that your username and or code are wrong, chose");
            while(true) {
                System.out.println("1. Try again");
                System.out.println("2. Go back");
                int choice = 0;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                switch (choice) {
                    case 1:
                        transitionState(new Login(tui, scanner, controller));
                        break;
                    case 2:
                        transitionState(new LoginOrRegister(tui, scanner, controller));
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        System.out.println("Hello "+username +"\n\n");
        transitionState(new CreateNewLobbyOrJoinLobby(tui, scanner, controller));
    }
}
