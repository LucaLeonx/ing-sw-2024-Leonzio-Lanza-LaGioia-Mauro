package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class NewLoginScreen extends TUIScreen {

    private final AtomicReference<TUIScreen> nextState = new AtomicReference<>();

    public NewLoginScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
        this.nextState.set(new NewLobbyScreen(tui,scanner,controller));
    }

    private final Runnable registrationExecution = () -> {

        boolean validUsername = false;

        while(!validUsername) {
            try {

                System.out.print("Input your username: ");
                String username = scanner.nextLine().trim();

                int tempCode = controller.register(username);

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

        nextState.set(new NewLobbyScreen(tui, scanner, controller));
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

        try {
            if(controller.isWaitingInLobby()){
                System.out.println("Getting into joined lobby");
                nextState.set(new NewLobbyWaitScreen(tui, scanner, controller));
            }
        } catch (InvalidOperationException e){
            // Just skip
        }

        try {
            if(controller.isInGame()){
                System.out.println("A previous game is available. Coming back...");
                if(controller.getControlledPlayerInformation().secretObjective() == null){
                        nextState.set(new NewGameSetupScreen(tui, scanner, controller));
                } else {
                        nextState.set(new NewGamePlayScreen(tui, scanner, controller));
                }
            }
        } catch (InvalidOperationException | RemoteException e){
            System.out.println("Unable to retrieve previous player state: " + e.getMessage());
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
            transitionState(nextState.get());
        } catch (CancelChoiceException e) {
            transitionState(new NewConnectionChoiceScreen(tui, scanner, controller));
        }
    }
}
