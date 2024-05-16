package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;

import java.util.Scanner;

public class LoginOrRegister extends TUIState{
    public LoginOrRegister(TUI tui, Scanner scanner, ClientController controller){
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Go back");
        System.out.print("Please choose an option: ");
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (Exception e) {
            System.out.println(e.getMessage());
            scanner.nextLine();
            transitionState(new LoginOrRegister(tui, scanner, controller));
        }

        switch(choice) {
            case 1:
                transitionState(new Login(tui, scanner, controller));
                break;
            case 2:
                transitionState(new Register(tui, scanner, controller));
                break;
            case 3:
                transitionState(new InitialScreen(tui, scanner, null));
            default:
                System.out.println("Invalid choice. Please try again.");
                transitionState(new LoginOrRegister(tui, scanner, controller));
        }
    }
}
