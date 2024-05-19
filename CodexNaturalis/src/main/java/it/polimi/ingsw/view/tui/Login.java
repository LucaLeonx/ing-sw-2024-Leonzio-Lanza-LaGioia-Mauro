package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Login extends TUIScreen {
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
        String stringCode;
        stringCode=scanner.nextLine().trim();
        try {
            code=Integer.parseInt(stringCode);
            controller.login(username, code);
        }
        // I leave a general exception since in theory the code could crush for controller or if stringCode contains letters
        catch (Exception e) {
            System.out.println("It seems that your username and or code are wrong, chose: ");
            while(true) {
                System.out.println("1. Try again");
                System.out.println("2. Go back");
                String choice;
                System.out.print("Please choose an option: ");
                choice=scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        transitionState(new Login(tui, scanner, controller));
                        break;
                    case "2":
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
