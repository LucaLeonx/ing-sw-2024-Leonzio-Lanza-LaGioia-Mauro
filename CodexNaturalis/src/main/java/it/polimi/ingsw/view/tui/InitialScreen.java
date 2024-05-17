package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;

import java.util.Scanner;


public class InitialScreen extends TUIScreen {
    public InitialScreen(TUI tui, Scanner scanner, ClientController controller){
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        printStylishMessage("                          WELCOME TO CODEX NATURALIS                            ", "\u001B[32m", "\u001B[31m");
        printMushroom();
        ClientController controller = null;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("\n1. Socket");
            System.out.println("2. RMI");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");
            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
                transitionState(new InitialScreen(tui, scanner, controller));
            }

            switch(choice) {
                case 1:
                    controller = new SocketClientController();
                    transitionState(new LoginOrRegister(tui, scanner, controller));
                    controller.subscribeToNotifications(tui);
                    break;
                case 2:
                    controller = new RMIClientController();
                    controller.subscribeToNotifications(tui);
                    transitionState(new LoginOrRegister(tui, scanner, controller));
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    transitionState(new InitialScreen(tui, scanner, controller));
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            transitionState(new InitialScreen(tui, scanner, controller));
        }
    }

    public static void printStylishMessage(String message, String borderColor, String textColor) {
        // Define ASCII art characters for styling
        final String TOP_LEFT = borderColor + "╔";
        final String TOP_RIGHT = borderColor + "╗";
        final String BOTTOM_LEFT = borderColor +"╚";
        final String BOTTOM_RIGHT =borderColor + "╝";
        final String HORIZONTAL_LINE =borderColor + "═";
        final String VERTICAL_LINE = borderColor + "║";

        String ANSI_BOLD = "\u001B[1m";
        // ANSI escape code to reset text formatting
        String ANSI_RESET = "\u001B[0m";

        // Determine message width based on length
        int messageLength = message.length();
        int boxWidth = messageLength + 4; // Additional padding for borders

        // Print top border with specified border color
        System.out.print(TOP_LEFT);
        for (int i = 0; i < boxWidth - 2; i++) {
            System.out.print(HORIZONTAL_LINE);
        }
        System.out.println(TOP_RIGHT);

        // Print message with specified text color
        System.out.println(VERTICAL_LINE + textColor + " " + ANSI_BOLD + message + ANSI_RESET + " " + VERTICAL_LINE);

        // Print bottom border with specified border color
        System.out.print(BOTTOM_LEFT);
        for (int i = 0; i < boxWidth - 2; i++) {
            System.out.print(HORIZONTAL_LINE);
        }
        System.out.println(BOTTOM_RIGHT);

        // Reset colors after printing
        System.out.print("\u001B[0m");
    }

    public static void printMushroom(){
        System.out.println("\u001B[31m"+ "\t\t\t\t\t\t        __.....__ ");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t     .'\"         \"`." );
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t   .'               `.\t\t\t\t"+ "\u001B[35m" + "(_\\|/_)");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t  .                   . \t\t\t "+ "\u001B[35m" + "(/|\\) ");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t .       __...__       .");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t. _.--\"\"\"       \"\"\"--._ .");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t:\"                     \";");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t `-.__    :   :    __.-'");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t     \"\"\"-:   :-\"\"\"   ");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t        J     L");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t        :     :  ");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t        J       L");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t        :       :");
        System.out.println("\u001B[31m"+"\t\t\t\t\t\t       `._____.'");
        System.out.println("\u001B[32m"+"_______\\|/_____________\\|/_________________________\\|/_________________\\|/__________");
        // Reset colors after printing
        System.out.print("\u001B[0m");
    }
}
