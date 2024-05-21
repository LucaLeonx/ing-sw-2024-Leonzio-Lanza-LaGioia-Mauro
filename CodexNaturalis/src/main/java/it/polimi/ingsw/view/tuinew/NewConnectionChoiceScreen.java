package it.polimi.ingsw.view.tuinew;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.ConnectException;
import java.util.Scanner;

public class NewConnectionChoiceScreen extends TUIScreen {

    public NewConnectionChoiceScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {

        AssignmentDialog<ClientController> connectionChoiceDialog = null;

        try {
            connectionChoiceDialog = new AssignmentDialog<>("Choose your connection option:",
                            new DialogOption<>("Socket", new SocketClientController()),
                            new DialogOption<>("RMI", new RMIClientController()));
        } catch (ConnectException connectException){
            System.out.println("Unable to connect to server");
        } catch (Exception e){
            e.printStackTrace();
        }

        try{
            ClientController controller = connectionChoiceDialog.askForChoice(scanner);
            transitionState(new NewLoginScreen(tui, scanner, controller));
        } catch (CancelChoiceException e){
            System.out.println("Quitting...");
        }

    }
}
