package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.ConnectionSettings;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.ConnectException;
import java.util.Scanner;

public class ConnectionChoiceScreen extends TUIScreen {

    private ConnectionSettings connectionSettings;

    public ConnectionChoiceScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
        this.connectionSettings = new ConnectionSettings();
    }

    @Override
    public void display() {

        AssignmentDialog<ClientController> connectionChoiceDialog = null;

        askForConnectionSettings(scanner);

        try {
            connectionChoiceDialog = new AssignmentDialog<>("Choose your connection option:",
                            new DialogOption<>("Socket", new SocketClientController(connectionSettings)),
                            new DialogOption<>("RMI", new RMIClientController(connectionSettings)));
        } catch (ConnectException connectException){
            System.out.println("Unable to connect to server");
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            ClientController controller = connectionChoiceDialog.askForChoice(scanner);
            transitionState(new LoginScreen(tui, scanner, controller));
        } catch (CancelChoiceException e){
            System.out.println("Quitting...");
            System.exit(0);
        }
    }

    private void askForConnectionSettings(Scanner input){
        Boolean changeNeeded = new AssignmentDialog<>(
                "Do you want to set connection settings?",
                new DialogOption<>("Yes", true),
                new DialogOption<>("No, use default", false)).askForChoice(input);


        if(changeNeeded){
            System.out.println("These are the current connection settings:");
            printConnectionSettings(connectionSettings);
            boolean confirmed = false;
            ConnectionSettings newSettings = new ConnectionSettings();
            while(!confirmed){
                try {
                    System.out.print("Enter new server IP address: ");
                    newSettings.setServerHost(input.nextLine().toLowerCase().trim());
                    System.out.print("Enter new RMI connection port: ");
                    newSettings.setRMIPort(input.nextInt());
                    System.out.print("Enter new Socket connection port: ");
                    newSettings.setSocketPort(input.nextInt());
                    input.nextLine();

                    System.out.print("Do you confirm the new settings? [y|N]: ");
                    String confirmation = input.nextLine().toLowerCase().trim();
                    if(confirmation.equals("y")){
                        confirmed = true;
                        connectionSettings = newSettings;
                    }
                } catch (NumberFormatException ignored){}
            }
        }
    }

    private void printConnectionSettings(ConnectionSettings connectionSettings){
        System.out.println("Server address: " + connectionSettings.getServerHost());
        System.out.println("RMI port:       " + connectionSettings.getRMIPort());
        System.out.println("Socket port:    " + connectionSettings.getSocketPort());

    }
}
