package it.polimi.ingsw.view.tuinew;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.Scanner;

public class NewLoginScreen extends TUIScreen {

    public NewLoginScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    private final Runnable registrationExecution = () -> {

        boolean validUsername = false;

        while(!validUsername) {
            try {

                System.out.print("Input your username: ");
                String username = scanner.nextLine().trim();

                int tempCode = controller.register(username);
                controller.login(username, tempCode);

                System.out.println("Registration successful. Temporary code: " + tempCode);

                validUsername = true;

            } catch (InvalidCredentialsException e) {
                System.out.println("Unable to login with provided username, quitting...");
                break;
            } catch (InvalidOperationException e){
                System.out.println("This username is already in use. Try another one ");
            } catch (RemoteException e){
                e.printStackTrace();
                break;
            }
        }
    };

    private final Runnable loginExecution = () -> {

        boolean validCredentials = false;

        while(!validCredentials) {
            try {
                System.out.print("Input your username: ");
                String username = scanner.nextLine().trim();
                System.out.print("Input your tempcode: ");
                int tempCode = Integer.parseInt(scanner.nextLine().trim());
                controller.login(username, tempCode);
                System.out.println("Login successful");
                validCredentials = true;
            } catch (NumberFormatException e){
                System.out.println("Invalid input e");
            }
            catch (InvalidCredentialsException e) {
                System.out.println("Invalid credentials supplied. Try again");
            } catch (RemoteException e){
                e.printStackTrace();
                break;
            }
        }
    };

    private final ExecutionDialog loginOptionChoice = new ExecutionDialog(
            "Choose login strategy: ",
            new DialogOption<>("Login", loginExecution),
            new DialogOption<>("Register", registrationExecution)
    );

    @Override
    public void display() {
        try {
            loginOptionChoice.askAndExecuteChoice(scanner);
            transitionState(new NewLobbyScreen(tui, scanner, controller));
        } catch (CancelChoiceException e) {
            transitionState(new NewConnectionChoiceScreen(tui, scanner, controller));
        }
    }
}
